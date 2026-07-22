package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.entity.AttributeEntity;
import com.example.onlinestore.entity.AttributeValueEntity;
import com.example.onlinestore.entity.ItemAttributeRelationEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AttributeConverter {

    public Attribute toAttribute(AttributeEntity entity) {
        Attribute attribute = new Attribute();
        attribute.setId(entity.getId());
        attribute.setName(entity.getName());
        attribute.setAttributeType(AttributeType.valueOf(entity.getAttributeType()));
        attribute.setInputType(AttributeInputType.valueOf(entity.getInputType()));
        attribute.setRequired(entity.getRequired());
        attribute.setSearchable(entity.getSearchable());
        attribute.setSortScore(entity.getSortScore());
        attribute.setVisible(entity.getVisible());
        return attribute;
    }

    public Attribute copyAttribute(Attribute source) {
        Attribute copy = new Attribute();
        BeanUtils.copyProperties(source, copy, "values");
        return copy;
    }

    public AttributeValue toAttributeValue(AttributeValueEntity entity) {
        AttributeValue av = new AttributeValue();
        av.setId(entity.getId());
        av.setAttributeId(entity.getAttributeId());
        av.setValue(entity.getValue());
        av.setSortScore(entity.getSortScore());
        return av;
    }

    public AttributeEntity toEntity(CreateAttributeRequest request, LocalDateTime now) {
        AttributeEntity entity = new AttributeEntity();
        entity.setName(request.getName());
        entity.setAttributeType(request.getAttributeType());
        entity.setInputType(request.getInputType());
        entity.setRequired(request.getRequired());
        entity.setSearchable(request.getSearchable());
        entity.setSortScore(request.getSortScore());
        entity.setVisible(request.getVisible());
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entity;
    }

    public List<ItemAttributeRelationEntity> toRelationEntities(Long itemId, Long skuId, List<ItemAttributeRequest> attributes) {
        LocalDateTime now = LocalDateTime.now();
        return attributes.stream().map(attr -> toRelationEntity(itemId, skuId, attr, now)).toList();
    }

    private ItemAttributeRelationEntity toRelationEntity(Long itemId, Long skuId, ItemAttributeRequest attr, LocalDateTime now) {
        ItemAttributeRelationEntity entity = new ItemAttributeRelationEntity();
        entity.setItemId(itemId);
        entity.setSkuId(skuId);
        entity.setAttributeId(attr.getAttributeId());
        entity.setValueId(attr.getAttributeValueId());
        entity.setInputValue(attr.getValue());
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entity;
    }
}
