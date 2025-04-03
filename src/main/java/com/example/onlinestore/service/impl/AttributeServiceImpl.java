package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.dto.UpdateAttributeRequest;
import com.example.onlinestore.entity.AttributeEntity;
import com.example.onlinestore.entity.AttributeValueEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AttributeMapper;
import com.example.onlinestore.mapper.AttributeValueMapper;
import com.example.onlinestore.service.AttributeService;
import com.example.onlinestore.utils.CommonUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttributeServiceImpl implements AttributeService {

    private static final Logger logger = LoggerFactory.getLogger(AttributeServiceImpl.class);

    @Autowired
    private AttributeMapper attributeMapper;

    @Autowired
    private AttributeValueMapper attributeValueMapper;

    @Override
    public Attribute createAttribute(@Valid CreateAttributeRequest request) {
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
            throw new BizException(ErrorCode.INTERNAL_ERROR);
        }

        return convertToAttribute(attributeEntity);
    }



    @Override
    public void updateAttribute(@NotNull Long id, @Valid UpdateAttributeRequest request) {
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
            throw new BizException(ErrorCode.INTERNAL_ERROR);
        }
    }

    @Override
    public Attribute deleteAttribute(@Valid Long id) {
        getAttributeById(id);
        return null;
    }

    @Override
    public Attribute getAttributeByIdWithValues(Long id) {
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
            List<AttributeValueEntity> values =  attributeValueMapper.findAllAttributeValuesByAttributeId(attributeId);
            return values.stream().map(this::convertToAttributeValue).toList();
        }
        return List.of();
    }

    @Override
    public AttributeValue getAttributeValueById(Long id) {
        AttributeValueEntity attributeValueEntity = attributeValueMapper.findById(id);
        if (attributeValueEntity != null) {
            return convertToAttributeValue(attributeValueEntity);
        }
        logger.error("attribute value not found, id: {}", id);
        throw new BizException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND);
    }

    private  AttributeEntity getAttributeEntity(CreateAttributeRequest request, String name, LocalDateTime now) {
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
