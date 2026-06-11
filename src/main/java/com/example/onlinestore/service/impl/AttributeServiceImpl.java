package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.dto.UpdateAttributeRequest;
import com.example.onlinestore.entity.AttributeEntity;
import com.example.onlinestore.entity.AttributeValueEntity;
import com.example.onlinestore.entity.ItemAttributeRelationEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AttributeMapper;
import com.example.onlinestore.mapper.AttributeValueMapper;
import com.example.onlinestore.mapper.ItemAttributeRelationMapper;
import com.example.onlinestore.service.AttributeService;
import com.example.onlinestore.utils.CommonUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private static final Logger logger = LoggerFactory.getLogger(AttributeServiceImpl.class);

    private final Map<Long, Attribute> attributeCache = new ConcurrentHashMap<>();
    private final Map<Long, AttributeValue> attributeValueCache = new ConcurrentHashMap<>();
    private final Map<Long, List<AttributeValue>> valuesByAttributeIdCache = new ConcurrentHashMap<>();

    private final AttributeMapper attributeMapper;
    private final AttributeValueMapper attributeValueMapper;
    private final ItemAttributeRelationMapper itemAttributeRelationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Attribute createAttribute(@NotNull @Valid CreateAttributeRequest request) {
        // 校验名称是否重复
        String name = request.getName();
        if (attributeMapper.findByName(name) != null) {
            throw new BizException(ErrorCode.ATTRIBUTE_NAME_DUPLICATED, request.getName());
        }
        LocalDateTime now = LocalDateTime.now();
        AttributeEntity attributeEntity = getAttributeEntity(request, name, now);
        int effectRows = attributeMapper.insert(attributeEntity);
        if (effectRows != 1) {
            logger.error("insert attribute failed. because effect rows is 0. attributeName:{}", request.getName());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        Attribute attribute = convertToAttribute(attributeEntity);
        attributeCache.put(attributeEntity.getId(), attribute);
        return attribute;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAttribute(@NotNull Long id, @NotNull @Valid UpdateAttributeRequest request) {
        Attribute attribute = getAttributeById(id);
        AttributeEntity updatingEntity = new AttributeEntity();
        boolean needUpdate = CommonUtils.updateFieldIfChanged(request.getName(), attribute.getName(), updatingEntity::setName)
                || CommonUtils.updateFieldIfChanged(request.getAttributeType(), attribute.getAttributeType().name(), updatingEntity::setAttributeType)
                || CommonUtils.updateFieldIfChanged(request.getInputType(), attribute.getInputType().name(), updatingEntity::setInputType)
                || CommonUtils.updateFieldIfChanged(request.getRequired(), attribute.getRequired(), updatingEntity::setRequired)
                || CommonUtils.updateFieldIfChanged(request.getSearchable(), attribute.getSearchable(), updatingEntity::setSearchable)
                || CommonUtils.updateFieldIfChanged(request.getSortScore(), attribute.getSortScore(), updatingEntity::setSortScore)
                || CommonUtils.updateFieldIfChanged(request.getVisible(), attribute.getVisible(), updatingEntity::setVisible);

        if (!needUpdate) {
            logger.info("no attribute field changed, id: {}, name:{}", id, attribute.getName());
            return;
        }
        updatingEntity.setId(id);
        if (attributeMapper.update(updatingEntity) != 1) {
            logger.error("update attribute failed. because effect rows is 0. attributeName:{}", request.getName());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        attributeCache.remove(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttribute(@NotNull Long id) {

        //校验属性是否存在
        getAttributeById(id);

        // 有商品或者SKU用到该属性就不能删除，所以只要有一条记录存在就不能删除
        List<ItemAttributeRelationEntity> relationEntities = itemAttributeRelationMapper.findByAttributeId(id, 0, 1);
        if (CollectionUtils.isNotEmpty(relationEntities)) {
            Set<Long> referenceIds = relationEntities.stream().map(ItemAttributeRelationEntity::getItemId).collect(Collectors.toSet());
            logger.error("attribute:{} is reference by item, can not delete, itemIds:{}", id, referenceIds);
            throw new BizException(ErrorCode.ATTRIBUTE_IS_REFERENCE_BY_ITEM, id);
        }

        int effectRows = attributeMapper.deleteById(id);
        if (effectRows != 1) {
            logger.error("delete attribute failed. because effect rows is 0. attributeId:{}", id);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        attributeValueMapper.deleteByAttributeId(id);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                attributeCache.remove(id);
                attributeValueCache.values().removeIf(v -> Objects.equals(v.getAttributeId(), id));
                valuesByAttributeIdCache.remove(id);
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Attribute getAttributeByIdWithValues(@NotNull Long id) {
        Attribute attribute = getAttributeById(id);
        if (attribute.getInputType() == AttributeInputType.SINGLE_SELECT || attribute.getInputType() == AttributeInputType.MULTI_SELECT) {
            List<AttributeValue> values = findAllAttributeValuesByAttributeId(id);
            attribute.setValues(values);
        }
        return attribute;
    }

    @Override
    @Transactional(readOnly = true)
    public Attribute getAttributeById(@NotNull Long id) {
        Attribute cached = attributeCache.get(id);
        if (cached != null) {
            return cached;
        }

        AttributeEntity attributeEntity = attributeMapper.findById(id);
        if (attributeEntity == null) {
            logger.error("attribute not found, id: {}, name:{}", id);
            throw new BizException(ErrorCode.ATTRIBUTE_NOT_FOUND);
        }

        Attribute attribute = convertToAttribute(attributeEntity);
        attributeCache.put(id, attribute);
        return attribute;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttributeValue> findAllAttributeValuesByAttributeId(Long attributeId) {
        List<AttributeValue> cached = valuesByAttributeIdCache.get(attributeId);
        if (cached != null) {
            return cached;
        }

        List<AttributeValueEntity> values = attributeValueMapper.findAllAttributeValuesByAttributeId(attributeId);
        if (CollectionUtils.isEmpty(values)) {
            valuesByAttributeIdCache.put(attributeId, Collections.emptyList());
            return Collections.emptyList();
        }
        List<AttributeValue> result = values.stream().map(entity -> {
            AttributeValue av = convertToAttributeValue(entity);
            attributeValueCache.put(entity.getId(), av);
            return av;
        }).toList();
        valuesByAttributeIdCache.put(attributeId, result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public AttributeValue getAttributeValueById(@NotNull Long id) {
        AttributeValueEntity attributeValueEntity = attributeValueMapper.findById(id);
        if (attributeValueEntity != null) {
            return convertToAttributeValue(attributeValueEntity);
        }
        logger.error("attribute value not found, id: {}", id);
        throw new BizException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ensureItemAttributes(@NotNull Long itemId, @NotNull Long skuId, @NotNull @Valid List<ItemAttributeRequest> attributes) {

        List<ItemAttributeRelationEntity> relationEntities = itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId);

        List<ItemAttributeRelationEntity> newRelations;

        LocalDateTime now = LocalDateTime.now();
        if (CollectionUtils.isEmpty(relationEntities)) {
            newRelations = attributes.stream().map(attribute -> {
                ItemAttributeRelationEntity relationEntity = new ItemAttributeRelationEntity();
                relationEntity.setItemId(itemId);
                relationEntity.setSkuId(skuId);
                relationEntity.setAttributeId(attribute.getAttributeId());
                relationEntity.setValueId(attribute.getAttributeValueId());
                relationEntity.setInputValue(attribute.getValue());
                relationEntity.setCreatedAt(now);
                relationEntity.setUpdatedAt(now);
                return relationEntity;
            }).toList();
        } else {
            List<Long> curAttributeIds = relationEntities.stream().map(ItemAttributeRelationEntity::getAttributeId).collect(Collectors.toList());

            int effectRows = itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(itemId, curAttributeIds);
            if (effectRows != curAttributeIds.size()) {
                logger.error("delete item attribute relations failed. because effect rows is {}", effectRows);
                throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            newRelations = attributes.stream().map(attribute -> {
                ItemAttributeRelationEntity relationEntity = new ItemAttributeRelationEntity();
                relationEntity.setItemId(itemId);
                relationEntity.setSkuId(skuId);
                relationEntity.setAttributeId(attribute.getAttributeId());
                relationEntity.setValueId(attribute.getAttributeValueId());
                relationEntity.setInputValue(attribute.getValue());
                relationEntity.setCreatedAt(now);
                relationEntity.setUpdatedAt(now);
                return relationEntity;
            }).collect(Collectors.toList());

        }

        if (CollectionUtils.isEmpty(newRelations)) {
            logger.info("no new attribute relations to insert, itemId: {}", itemId);
            return;
        }
        if (itemAttributeRelationMapper.batchInsert(newRelations) != newRelations.size()) {
            logger.error("insert item attribute relations failed. because effect rows is {}", newRelations.size());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    @Transactional(readOnly = true)
    public List<Attribute> getAttributesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<Attribute> result = new ArrayList<>(ids.size());
        List<Long> missedIds = new ArrayList<>();
        for (Long id : ids) {
            Attribute cached = attributeCache.get(id);
            if (cached != null) {
                result.add(cached);
            } else {
                missedIds.add(id);
            }
        }

        if (!missedIds.isEmpty()) {
            List<AttributeEntity> entities = attributeMapper.findByIds(missedIds);
            for (AttributeEntity entity : entities) {
                Attribute attr = convertToAttribute(entity);
                attributeCache.put(entity.getId(), attr);
                result.add(attr);
            }
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, List<AttributeValue>> getAttributeValuesByAttributeIds(List<Long> attributeIds) {
        if (attributeIds == null || attributeIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, List<AttributeValue>> result = new HashMap<>(attributeIds.size());
        List<Long> missedIds = new ArrayList<>();
        for (Long attrId : attributeIds) {
            List<AttributeValue> cached = valuesByAttributeIdCache.get(attrId);
            if (cached != null) {
                result.put(attrId, cached);
            } else {
                missedIds.add(attrId);
            }
        }

        if (!missedIds.isEmpty()) {
            List<AttributeValueEntity> entities = attributeValueMapper.findByAttributeIds(missedIds);
            Map<Long, List<AttributeValue>> dbResults = entities.stream()
                    .map(entity -> {
                        AttributeValue av = convertToAttributeValue(entity);
                        attributeValueCache.put(entity.getId(), av);
                        return av;
                    })
                    .collect(Collectors.groupingBy(AttributeValue::getAttributeId));

            for (Long attrId : missedIds) {
                List<AttributeValue> values = dbResults.getOrDefault(attrId, Collections.emptyList());
                valuesByAttributeIdCache.put(attrId, values);
                result.put(attrId, values);
            }
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, AttributeValue> getAttributeValuesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, AttributeValue> result = new HashMap<>(ids.size());
        List<Long> missedIds = new ArrayList<>();
        for (Long id : ids) {
            AttributeValue cached = attributeValueCache.get(id);
            if (cached != null) {
                result.put(id, cached);
            } else {
                missedIds.add(id);
            }
        }

        if (!missedIds.isEmpty()) {
            List<AttributeValueEntity> entities = attributeValueMapper.findByIds(missedIds);
            for (AttributeValueEntity entity : entities) {
                AttributeValue av = convertToAttributeValue(entity);
                attributeValueCache.put(entity.getId(), av);
                result.put(entity.getId(), av);
            }
        }

        return result;
    }

    private AttributeEntity getAttributeEntity(CreateAttributeRequest request, String name, LocalDateTime now) {
        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setName(name);
        attributeEntity.setAttributeType(request.getAttributeType());
        attributeEntity.setInputType(request.getInputType());
        attributeEntity.setRequired(request.getRequired());
        attributeEntity.setSearchable(request.getSearchable());
        attributeEntity.setSortScore(request.getSortScore());
        attributeEntity.setVisible(request.getVisible());

        attributeEntity.setCreatedAt(now);
        attributeEntity.setUpdatedAt(now);
        return attributeEntity;
    }

    private Attribute convertToAttribute(AttributeEntity attributeEntity) {
        Attribute attribute = new Attribute();
        attribute.setId(attributeEntity.getId());
        attribute.setName(attributeEntity.getName());
        attribute.setAttributeType(AttributeType.valueOf(attributeEntity.getAttributeType()));
        attribute.setInputType(AttributeInputType.valueOf(attributeEntity.getInputType()));
        attribute.setRequired(attributeEntity.getRequired());
        attribute.setSearchable(attributeEntity.getSearchable());
        attribute.setSortScore(attributeEntity.getSortScore());
        attribute.setVisible(attributeEntity.getVisible());
        return attribute;
    }

    private AttributeValue convertToAttributeValue(AttributeValueEntity attributeValueEntity) {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setId(attributeValueEntity.getId());
        attributeValue.setAttributeId(attributeValueEntity.getAttributeId());
        attributeValue.setValue(attributeValueEntity.getValue());
        attributeValue.setSortScore(attributeValueEntity.getSortScore());
        return attributeValue;
    }
}
