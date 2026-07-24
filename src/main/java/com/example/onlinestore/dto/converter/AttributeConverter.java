package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.entity.AttributeEntity;
import com.example.onlinestore.entity.AttributeValueEntity;
import com.example.onlinestore.entity.ItemAttributeRelationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AttributeConverter {

    @Mapping(target = "values", ignore = true)
    public abstract Attribute toAttribute(AttributeEntity entity);

    @Mapping(target = "values", ignore = true)
    public abstract Attribute copyAttribute(Attribute source);

    public abstract AttributeValue toAttributeValue(AttributeValueEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", source = "now")
    @Mapping(target = "updatedAt", source = "now")
    public abstract AttributeEntity toEntity(CreateAttributeRequest request, LocalDateTime now);

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
