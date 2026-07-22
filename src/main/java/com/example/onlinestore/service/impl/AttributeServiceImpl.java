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
import com.example.onlinestore.cache.AttributeCacheManager;
import com.example.onlinestore.dto.converter.AttributeConverter;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private static final Logger logger = LoggerFactory.getLogger(AttributeServiceImpl.class);

    private final AttributeMapper attributeMapper;
    private final AttributeValueMapper attributeValueMapper;
    private final ItemAttributeRelationMapper itemAttributeRelationMapper;
    private final AttributeCacheManager cacheManager;
    private final AttributeConverter converter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Attribute createAttribute(@NotNull @Valid CreateAttributeRequest request) {
        // 校验名称是否重复
        if (attributeMapper.findByName(request.getName()) != null) {
            throw new BizException(ErrorCode.ATTRIBUTE_NAME_DUPLICATED, request.getName());
        }
        LocalDateTime now = LocalDateTime.now();
        AttributeEntity attributeEntity = converter.toEntity(request, now);
        int effectRows = attributeMapper.insert(attributeEntity);
        if (effectRows != 1) {
            logger.error("insert attribute failed. because effect rows is 0. attributeName:{}", request.getName());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        Attribute attribute = converter.toAttribute(attributeEntity);
        afterCommit(() -> cacheManager.putAttribute(attribute));
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
        afterCommit(() -> cacheManager.invalidateAttribute(id));
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

        afterCommit(() -> cacheManager.invalidateAllByAttributeId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Attribute getAttributeByIdWithValues(@NotNull Long id) {
        Attribute cached = getAttributeById(id);
        Attribute attribute = converter.copyAttribute(cached);
        if (attribute.getInputType() == AttributeInputType.SINGLE_SELECT || attribute.getInputType() == AttributeInputType.MULTI_SELECT) {
            attribute.setValues(findAllAttributeValuesByAttributeId(id));
        }
        return attribute;
    }

    @Override
    @Transactional(readOnly = true)
    public Attribute getAttributeById(@NotNull Long id) {
        return cacheManager.getOrLoadAttribute(id, key -> {
            AttributeEntity entity = attributeMapper.findById(key);
            if (entity == null) {
                logger.error("attribute not found, id: {}", key);
                throw new BizException(ErrorCode.ATTRIBUTE_NOT_FOUND);
            }
            return converter.toAttribute(entity);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttributeValue> findAllAttributeValuesByAttributeId(Long attributeId) {
        return cacheManager.getOrLoadValuesByAttributeId(attributeId, key -> {
            List<AttributeValueEntity> values = attributeValueMapper.findAllAttributeValuesByAttributeId(key);
            if (CollectionUtils.isEmpty(values)) {
                return Collections.emptyList();
            }
            return values.stream()
                    .map(converter::toAttributeValue)
                    .toList();
        });
    }

    @Override
    @Transactional(readOnly = true)
    public AttributeValue getAttributeValueById(@NotNull Long id) {
        return cacheManager.getOrLoadAttributeValue(id, key -> {
            AttributeValueEntity entity = attributeValueMapper.findById(key);
            if (entity == null) {
                logger.error("attribute value not found, id: {}", key);
                throw new BizException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND);
            }
            return converter.toAttributeValue(entity);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ensureItemAttributes(@NotNull Long itemId, @NotNull Long skuId, @NotNull @Valid List<ItemAttributeRequest> attributes) {
        List<ItemAttributeRelationEntity> relationEntities = itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId);

        if (CollectionUtils.isNotEmpty(relationEntities)) {
            List<Long> curAttributeIds = relationEntities.stream()
                    .map(ItemAttributeRelationEntity::getAttributeId)
                    .collect(Collectors.toList());
            int effectRows = itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(itemId, curAttributeIds);
            if (effectRows != curAttributeIds.size()) {
                logger.error("delete item attribute relations failed. because effect rows is {}", effectRows);
                throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }

        List<ItemAttributeRelationEntity> newRelations = converter.toRelationEntities(itemId, skuId, attributes);
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

        return cacheManager.batchGetAttributes(ids, missedIds ->
                attributeMapper.findByIds(missedIds).stream()
                        .map(converter::toAttribute)
                        .toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, List<AttributeValue>> getAttributeValuesByAttributeIds(List<Long> attributeIds) {
        if (attributeIds == null || attributeIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return cacheManager.batchGetValuesByAttributeIds(attributeIds, missedIds ->
                attributeValueMapper.findByAttributeIds(missedIds).stream()
                        .map(converter::toAttributeValue)
                        .collect(Collectors.groupingBy(AttributeValue::getAttributeId))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, AttributeValue> getAttributeValuesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyMap();
        }

        return cacheManager.batchGetAttributeValues(ids, missedIds ->
                attributeValueMapper.findByIds(missedIds).stream()
                        .map(converter::toAttributeValue)
                        .toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public void validateSkuAttributes(@NotNull @Valid List<ItemAttributeRequest> attributes) {
        List<Long> attributeIds = attributes.stream().map(ItemAttributeRequest::getAttributeId).toList();
        List<Long> valueIds = attributes.stream()
                .map(ItemAttributeRequest::getAttributeValueId)
                .filter(Objects::nonNull).toList();

        Map<Long, Attribute> attributeMap = getAttributesByIds(attributeIds).stream()
                .collect(Collectors.toMap(Attribute::getId, Function.identity()));
        Map<Long, AttributeValue> valueMap = getAttributeValuesByIds(valueIds);

        for (ItemAttributeRequest attributeRequest : attributes) {
            Attribute attribute = attributeMap.get(attributeRequest.getAttributeId());
            if (attribute == null) {
                throw new BizException(ErrorCode.ATTRIBUTE_NOT_FOUND, attributeRequest.getAttributeId());
            }
            if (attribute.getAttributeType() != AttributeType.SKU) {
                throw new BizException(ErrorCode.ATTRIBUTE_TYPE_NOT_SKU, attributeRequest.getAttributeId());
            }
            if (attribute.getInputType() != AttributeInputType.SINGLE_SELECT && attribute.getInputType() != AttributeInputType.MULTI_SELECT) {
                throw new BizException(ErrorCode.SKU_ATTRIBUTE_INPUT_TYPE_INVALID, attributeRequest.getAttributeId());
            }
            if (attributeRequest.getAttributeValueId() == null) {
                throw new BizException(ErrorCode.SKU_ATTRIBUTE_VALUE_EMPTY, attributeRequest.getAttributeId());
            }
            if (!valueMap.containsKey(attributeRequest.getAttributeValueId())) {
                throw new BizException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND, attributeRequest.getAttributeValueId());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void validateItemAttributes(@NotNull @Valid List<ItemAttributeRequest> attributes) {
        List<Long> attributeIds = attributes.stream().map(ItemAttributeRequest::getAttributeId).toList();
        Map<Long, Attribute> attributeMap = getAttributesByIds(attributeIds).stream()
                .collect(Collectors.toMap(Attribute::getId, Function.identity()));

        for (ItemAttributeRequest attributeRequest : attributes) {
            Attribute attribute = attributeMap.get(attributeRequest.getAttributeId());
            if (attribute == null) {
                throw new BizException(ErrorCode.ATTRIBUTE_NOT_FOUND, attributeRequest.getAttributeId());
            }
            if (attribute.getInputType() == AttributeInputType.SINGLE_SELECT
                    || attribute.getInputType() == AttributeInputType.MULTI_SELECT) {
                if (attributeRequest.getAttributeValueId() == null) {
                    throw new BizException(ErrorCode.ITEM_ATTRIBUTE_VALUE_IS_EMPTY, attributeRequest.getAttributeId());
                }
            } else {
                if (org.apache.commons.lang3.StringUtils.isBlank(attributeRequest.getValue())) {
                    throw new BizException(ErrorCode.ITEM_ATTRIBUTE_VALUE_IS_EMPTY, attributeRequest.getAttributeId());
                }
            }
        }
    }

    private void afterCommit(Runnable action) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                action.run();
            }
        });
    }
}
