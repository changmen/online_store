package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.bean.ItemAttributeAndValue;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.CreateSkuRequest;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.entity.ItemAttributeRelationEntity;
import com.example.onlinestore.entity.SkuEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.ItemAttributeRelationMapper;
import com.example.onlinestore.mapper.ItemMapper;
import com.example.onlinestore.mapper.SkuMapper;
import com.example.onlinestore.service.AttributeService;
import com.example.onlinestore.service.ItemDetailCacheService;
import com.example.onlinestore.service.SkuService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Validated
public class SkuServiceImpl implements SkuService {
    private static final Logger logger = LoggerFactory.getLogger(SkuServiceImpl.class);

    private final SkuMapper skuMapper;
    private final ItemMapper itemMapper;
    private final AttributeService attributeService;
    private final ItemAttributeRelationMapper itemAttributeRelationMapper;
    private final ItemDetailCacheService itemDetailCacheService;

    public SkuServiceImpl(SkuMapper skuMapper, ItemMapper itemMapper,
                          AttributeService attributeService,
                          ItemAttributeRelationMapper itemAttributeRelationMapper,
                          ItemDetailCacheService itemDetailCacheService) {
        this.skuMapper = skuMapper;
        this.itemMapper = itemMapper;
        this.attributeService = attributeService;
        this.itemAttributeRelationMapper = itemAttributeRelationMapper;
        this.itemDetailCacheService = itemDetailCacheService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Sku createSku(@NotNull @Valid CreateSkuRequest createSkuRequest) {
        //判断商品是否存在
        if (itemMapper.findById(createSkuRequest.getItemId()) == null) {
            throw new BizException(ErrorCode.ITEM_NOT_FOUND);
        }
        // 需要判断skuCode是否存在
        if (skuMapper.findBySkuCode(createSkuRequest.getSkuCode()) != null) {
            throw new BizException(ErrorCode.SKU_CODE_EXISTS, createSkuRequest.getSkuCode());
        }

        if (createSkuRequest.getWarningQuantity() > createSkuRequest.getStockQuantity()) {
            logger.error("SKU预警数量 {} 超过库存数量 {}", createSkuRequest.getWarningQuantity(), createSkuRequest.getStockQuantity());
            throw new BizException(ErrorCode.SKU_WARNING_QUANTITY_EXCEEDS_STOCK_QUANTITY);
        }

        // 校验属性（批量查询替代循环查询）
        List<Long> attributeIds = createSkuRequest.getAttributes().stream()
                .map(ItemAttributeRequest::getAttributeId).toList();
        List<Long> valueIds = createSkuRequest.getAttributes().stream()
                .map(ItemAttributeRequest::getAttributeValueId)
                .filter(Objects::nonNull).toList();

        Map<Long, Attribute> attributeMap = attributeService.getAttributesByIds(attributeIds).stream()
                .collect(Collectors.toMap(Attribute::getId, Function.identity()));
        Map<Long, AttributeValue> valueMap = attributeService.getAttributeValuesByIds(valueIds);

        createSkuRequest.getAttributes().forEach(attributeRequest -> {
            Attribute attribute = attributeMap.get(attributeRequest.getAttributeId());
            if (attribute == null) {
                throw new BizException(ErrorCode.ATTRIBUTE_NOT_FOUND, attributeRequest.getAttributeId());
            }
            if (attribute.getAttributeType() != AttributeType.SKU) {
                throw new BizException(ErrorCode.ATTRIBUTE_TYPE_NOT_SKU, attributeRequest.getAttributeId());
            }

            if (attribute.getInputType() != AttributeInputType.SINGLE_SELECT && attribute.getInputType() != AttributeInputType.MULTI_SELECT){
                throw new BizException(ErrorCode.SKU_ATTRIBUTE_INPUT_TYPE_INVALID, attributeRequest.getAttributeId());
            }

            if (attributeRequest.getAttributeValueId() == null) {
                throw new BizException(ErrorCode.SKU_ATTRIBUTE_VALUE_EMPTY, attributeRequest.getAttributeId());
            }

            if (!valueMap.containsKey(attributeRequest.getAttributeValueId())) {
                throw new BizException(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND, attributeRequest.getAttributeValueId());
            }
        });

        LocalDateTime now = LocalDateTime.now();

        SkuEntity skuEntity = new SkuEntity();
        skuEntity.setItemId(createSkuRequest.getItemId());
        skuEntity.setSkuCode(createSkuRequest.getSkuCode());
        skuEntity.setName(createSkuRequest.getName());
        skuEntity.setDescription(createSkuRequest.getDescription());
        skuEntity.setPrice(createSkuRequest.getPrice());
        skuEntity.setImage(createSkuRequest.getImage());
        skuEntity.setStockQuantity(createSkuRequest.getStockQuantity());
        skuEntity.setWarningQuantity(createSkuRequest.getWarningQuantity());
        skuEntity.setDefaultSku(createSkuRequest.getDefaultSku());
        skuEntity.setSoldQuantity(0);
        skuEntity.setCreatedAt(now);
        skuEntity.setUpdatedAt(now);
        int effectRows = skuMapper.insert(skuEntity);
        if (effectRows != 1) {
            logger.error("insert sku failed. because effect rows is 0. skuCode:{}", createSkuRequest.getSkuCode());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        //记录属性
        attributeService.ensureItemAttributes(createSkuRequest.getItemId(), skuEntity.getId(), createSkuRequest.getAttributes());
        itemDetailCacheService.evictItemDetailCache(createSkuRequest.getItemId());
        return convertSkuEntity(skuEntity, null);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Sku> getSkusByItemId(@NotNull Long itemId) {
        List<SkuEntity> skuEntities = skuMapper.findByItemId(itemId);
        if (CollectionUtils.isEmpty(skuEntities)) {
            return Collections.emptyList();
        }

        List<ItemAttributeRelationEntity> allRelations = itemAttributeRelationMapper.findByItemId(itemId);
        Map<Long, List<ItemAttributeRelationEntity>> relationsBySkuId = allRelations.stream()
                .collect(Collectors.groupingBy(ItemAttributeRelationEntity::getSkuId));

        Set<Long> allAttributeIds = new HashSet<>();
        Set<Long> allValueIds = new HashSet<>();
        for (ItemAttributeRelationEntity r : allRelations) {
            allAttributeIds.add(r.getAttributeId());
            if (r.getValueId() != null) {
                allValueIds.add(r.getValueId());
            }
        }

        Map<Long, Attribute> attributeMap = attributeService.getAttributesByIds(new ArrayList<>(allAttributeIds)).stream()
                .collect(Collectors.toMap(Attribute::getId, Function.identity()));
        Map<Long, List<AttributeValue>> valuesByAttributeId = attributeService.getAttributeValuesByAttributeIds(new ArrayList<>(allAttributeIds));
        Map<Long, AttributeValue> valueMap = attributeService.getAttributeValuesByIds(new ArrayList<>(allValueIds));

        return skuEntities.stream()
                .map(skuEntity -> convertSkuEntity(skuEntity, relationsBySkuId.getOrDefault(skuEntity.getId(), Collections.emptyList()), attributeMap, valuesByAttributeId, valueMap))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStockQuantity(@NotNull Long skuId, @NotNull @Min(value = 1, message = "库存数量必须大于0") Integer quantity) {
        SkuEntity skuEntity = skuMapper.findById(skuId);
        if (skuEntity == null) {
            logger.error("sku not found, id: {}", skuId);
            throw new BizException(ErrorCode.SKU_NOT_FOUND);
        }

        if (skuEntity.getWarningQuantity() > quantity) {
            throw new BizException(ErrorCode.SKU_WARNING_QUANTITY_EXCEEDS_STOCK_QUANTITY);
        }

        if (skuMapper.updateStockQuantity(skuId, quantity) != 1) {
            logger.error("update sku stock quantity failed. because effect rows is 0. skuId:{}", skuId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        itemDetailCacheService.evictItemDetailCache(skuEntity.getItemId());
    }

    @Override
    @Transactional(readOnly = true)
    public Sku getSkuById(Long skuId) {
        SkuEntity skuEntity = skuMapper.findById(skuId);
        if (skuEntity == null) {
            logger.error("sku not found, id: {}", skuId);
            throw new BizException(ErrorCode.SKU_NOT_FOUND);
        }

        List<ItemAttributeRelationEntity> relations = itemAttributeRelationMapper.findByItemIdAndSkuId(skuEntity.getItemId(), skuId);
        if (CollectionUtils.isEmpty(relations)) {
            return buildSku(skuEntity, Collections.emptyList(), Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
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
        Map<Long, AttributeValue> valueMap = attributeService.getAttributeValuesByIds(new ArrayList<>(valueIds));

        return buildSku(skuEntity, relations, attributeMap, Collections.emptyMap(), valueMap);
    }

    private Sku convertSkuEntity(SkuEntity skuEntity, List<ItemAttributeRelationEntity> relationEntities) {
        if (skuEntity == null) {
            return null;
        }

        if (relationEntities == null) {
            relationEntities = itemAttributeRelationMapper.findByItemIdAndSkuId(skuEntity.getItemId(), skuEntity.getId());
        }

        if (CollectionUtils.isEmpty(relationEntities)) {
            return buildSku(skuEntity, Collections.emptyList(), Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
        }

        Set<Long> attributeIds = new HashSet<>();
        Set<Long> valueIds = new HashSet<>();
        for (ItemAttributeRelationEntity r : relationEntities) {
            attributeIds.add(r.getAttributeId());
            if (r.getValueId() != null) {
                valueIds.add(r.getValueId());
            }
        }

        Map<Long, Attribute> attributeMap = attributeService.getAttributesByIds(new ArrayList<>(attributeIds)).stream()
                .collect(Collectors.toMap(Attribute::getId, Function.identity()));
        Map<Long, List<AttributeValue>> valuesByAttributeId = attributeService.getAttributeValuesByAttributeIds(new ArrayList<>(attributeIds));
        Map<Long, AttributeValue> valueMap = attributeService.getAttributeValuesByIds(new ArrayList<>(valueIds));

        return buildSku(skuEntity, relationEntities, attributeMap, valuesByAttributeId, valueMap);
    }

    private Sku convertSkuEntity(SkuEntity skuEntity,
                                  List<ItemAttributeRelationEntity> relationEntities,
                                  Map<Long, Attribute> attributeMap,
                                  Map<Long, List<AttributeValue>> valuesByAttributeId,
                                  Map<Long, AttributeValue> valueMap) {
        if (skuEntity == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(relationEntities)) {
            return buildSku(skuEntity, Collections.emptyList(), attributeMap, valuesByAttributeId, valueMap);
        }
        return buildSku(skuEntity, relationEntities, attributeMap, valuesByAttributeId, valueMap);
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
                // 填充属性值列表
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
