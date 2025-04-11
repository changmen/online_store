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
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttributeServiceImpl implements AttributeService {

    private static final Logger logger = LoggerFactory.getLogger(AttributeServiceImpl.class);

    @Autowired
    private AttributeMapper attributeMapper;

    @Autowired
    private AttributeValueMapper attributeValueMapper;

    @Autowired
    private ItemAttributeRelationMapper itemAttributeRelationMapper;

    @Override
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

        return convertToAttribute(attributeEntity);
    }


    @Override
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
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttribute(@NotNull Long id) {

        //校验属性是否存在
        getAttributeById(id);

        // 有商品或者SKU用到该属性就不能删除，所以只要有一条记录存在就不能删除
        List<ItemAttributeRelationEntity> relationEntities = itemAttributeRelationMapper.findByAndAttributeId(id, 0, 1);
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

        // 删除属性值
        int valueCount = attributeValueMapper.countValuesByAttributeId(id);
        if (valueCount > 0) {
            int valueEffectRows = attributeValueMapper.deleteByAttributeId(id);
            if (valueEffectRows != valueCount) {
                logger.error("delete attribute value failed. because effect rows is not equal valueCount. attributeId:{}, valueCount:{}", id, valueCount);
                throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }

    }

    @Override
    public Attribute getAttributeByIdWithValues(@NotNull Long id) {
        Attribute attribute = getAttributeById(id);
        if (attribute.getInputType() == AttributeInputType.SINGLE_SELECT || attribute.getInputType() == AttributeInputType.MULTI_SELECT) {
            List<AttributeValue> values = findAllAttributeValuesByAttributeId(id);
            attribute.setValues(values);
        }
        return attribute;
    }

    @Override
    public Attribute getAttributeById(@NotNull Long id) {
        AttributeEntity attributeEntity = attributeMapper.findById(id);
        if (attributeEntity == null) {
            logger.error("attribute not found, id: {}, name:{}", id);
            throw new BizException(ErrorCode.ATTRIBUTE_NOT_FOUND);
        }

        return convertToAttribute(attributeEntity);
    }

    @Override
    public List<AttributeValue> findAllAttributeValuesByAttributeId(Long attributeId) {
        Attribute attribute = getAttributeById(attributeId);
        if (attribute.getInputType() == AttributeInputType.SINGLE_SELECT || attribute.getInputType() == AttributeInputType.MULTI_SELECT) {
            List<AttributeValueEntity> values = attributeValueMapper.findAllAttributeValuesByAttributeId(attributeId);
            return values.stream().map(this::convertToAttributeValue).toList();
        }
        return Collections.emptyList();
    }

    @Override
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

            newRelations = attributes.stream().filter(attribute -> !curAttributeIds.contains(attribute.getAttributeId())).map(attribute -> {
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
