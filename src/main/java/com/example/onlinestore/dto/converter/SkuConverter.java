package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.bean.ItemAttributeAndValue;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.SkuResponse;
import com.example.onlinestore.entity.ItemAttributeRelationEntity;
import com.example.onlinestore.entity.SkuEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.service.AttributeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SkuConverter {

    private final AttributeService attributeService;

    public Sku toSku(SkuEntity skuEntity, List<ItemAttributeRelationEntity> relations) {
        if (skuEntity == null) {
            return null;
        }

        if (relations == null) {
            return toSkuWithEmptyAttributes(skuEntity);
        }

        if (CollectionUtils.isEmpty(relations)) {
            return toSkuWithEmptyAttributes(skuEntity);
        }

        Set<Long> attributeIds = new HashSet<>();
        Set<Long> valueIds = new HashSet<>();
        for (ItemAttributeRelationEntity r : relations) {
            attributeIds.add(r.getAttributeId());
            if (r.getValueId() != null) {
                valueIds.add(r.getValueId());
            }
        }

        Map<Long, Attribute> attributeMap = attributeService.getAttributesByIds(new ArrayList<>(attributeIds)).stream()
                .collect(Collectors.toMap(Attribute::getId, Function.identity()));
        Map<Long, List<AttributeValue>> valuesByAttributeId = attributeService.getAttributeValuesByAttributeIds(new ArrayList<>(attributeIds));
        Map<Long, AttributeValue> valueMap = attributeService.getAttributeValuesByIds(new ArrayList<>(valueIds));

        return toSku(skuEntity, relations, attributeMap, valuesByAttributeId, valueMap);
    }

    public Sku toSku(SkuEntity skuEntity,
                     List<ItemAttributeRelationEntity> relations,
                     Map<Long, Attribute> attributeMap,
                     Map<Long, List<AttributeValue>> valuesByAttributeId,
                     Map<Long, AttributeValue> valueMap) {
        if (skuEntity == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(relations)) {
            return buildSku(skuEntity, Collections.emptyList(), attributeMap, valuesByAttributeId, valueMap);
        }
        return buildSku(skuEntity, relations, attributeMap, valuesByAttributeId, valueMap);
    }

    public SkuResponse convert(Sku sku) {
        if (sku == null) {
            return null;
        }
        SkuResponse skuResponse = new SkuResponse();
        skuResponse.setId(sku.getId());
        skuResponse.setItemId(sku.getItemId());
        skuResponse.setSkuCode(sku.getSkuCode());
        skuResponse.setName(sku.getName());
        skuResponse.setDescription(sku.getDescription());
        skuResponse.setPrice(sku.getPrice());
        skuResponse.setDefaultSku(sku.getDefaultSku());
        skuResponse.setStockQuantity(sku.getStockQuantity());
        skuResponse.setSoldQuantity(sku.getSoldQuantity());
        skuResponse.setWarningQuantity(sku.getWarningQuantity());
        skuResponse.setImage(sku.getImage());
        skuResponse.setAttributes(sku.getAttributes());
        return skuResponse;
    }

    private Sku toSkuWithEmptyAttributes(SkuEntity skuEntity) {
        return buildSku(skuEntity, Collections.emptyList(), Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
    }

    private Sku buildSku(SkuEntity skuEntity,
                         List<ItemAttributeRelationEntity> relationEntities,
                         Map<Long, Attribute> attributeMap,
                         Map<Long, List<AttributeValue>> valuesByAttributeId,
                         Map<Long, AttributeValue> valueMap) {
        Sku sku = new Sku();
        sku.setId(skuEntity.getId());
        sku.setItemId(skuEntity.getItemId());
        sku.setSkuCode(skuEntity.getSkuCode());
        sku.setName(skuEntity.getName());
        sku.setDescription(skuEntity.getDescription());
        sku.setPrice(skuEntity.getPrice());
        sku.setImage(skuEntity.getImage());
        sku.setDefaultSku(skuEntity.getDefaultSku());

        if (CollectionUtils.isEmpty(relationEntities)) {
            sku.setAttributes(Collections.emptyList());
        } else {
            sku.setAttributes(relationEntities.stream().map(relationEntity -> {
                ItemAttributeAndValue itemAttributeAndValue = new ItemAttributeAndValue();
                Attribute attribute = attributeMap.get(relationEntity.getAttributeId());
                List<AttributeValue> values = valuesByAttributeId.getOrDefault(relationEntity.getAttributeId(), Collections.emptyList());
                Attribute attributeWithValues = cloneAttributeWithValues(attribute, values);
                itemAttributeAndValue.setAttribute(attributeWithValues);
                if (attribute.getInputType() == AttributeInputType.INPUT) {
                    itemAttributeAndValue.setInputValue(relationEntity.getInputValue());
                } else {
                    AttributeValue attributeValue = valueMap.get(relationEntity.getValueId());
                    itemAttributeAndValue.setAttributeValue(attributeValue);
                }
                return itemAttributeAndValue;
            }).collect(Collectors.toList()));
        }
        return sku;
    }

    private Attribute cloneAttributeWithValues(Attribute attribute, List<AttributeValue> values) {
        Attribute clone = new Attribute();
        clone.setId(attribute.getId());
        clone.setName(attribute.getName());
        clone.setAttributeType(attribute.getAttributeType());
        clone.setInputType(attribute.getInputType());
        clone.setRequired(attribute.getRequired());
        clone.setSearchable(attribute.getSearchable());
        clone.setSortScore(attribute.getSortScore());
        clone.setVisible(attribute.getVisible());
        clone.setValues(values);
        return clone;
    }
}
