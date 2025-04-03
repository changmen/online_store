package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.bean.Category;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.CreateItemRequest;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.ItemStatus;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.ItemMapper;
import com.example.onlinestore.service.*;
import com.example.onlinestore.utils.JacksonJsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;


@Service
public class ItemServiceImpl implements ItemService {

    @Value("${forbidden-words:刀}")
    private String forbiddenWords;

    @Value("${item.upload-description-to-oss:false}")
    private boolean uploadDescriptionToOSS;

    @Value("${item.default-sort-score:1}")
    private int defaultItemSortScore;

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private OssService ossService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Item createItem(@Valid CreateItemRequest request) {
        // 校验名称是否包含敏感字符
        if (getForbiddenWords().stream().anyMatch(request.getName()::contains)) {
            throw new BizException(ErrorCode.ITEM_NAME_CONTAINS_FORBIDDEN_WORDS, request.getName());
        }

        if (StringUtils.isNotBlank(request.getDescription())) {
            if (getForbiddenWords().stream().anyMatch(request.getDescription()::contains)) {
                throw new BizException(ErrorCode.ITEM_DESCRIPTION_CONTAINS_FORBIDDEN_WORDS, request.getDescription());
            }
        }

        for (ItemAttributeRequest attributeRequest : request.getAttributes()) {
            Attribute attribute = attributeService.getAttributeById(attributeRequest.getAttributeId());
            if (attribute.getInputType() == AttributeInputType.SINGLE_SELECT || attribute.getInputType() == AttributeInputType.MULTI_SELECT) {
                // 此时需要校验value
                if (attributeRequest.getAttributeValueId() == null) {
                    throw new BizException(ErrorCode.ITEM_ATTRIBUTE_VALUE_IS_EMPTY, attributeRequest.getAttributeId());
                }

            } else {
                if (StringUtils.isBlank(attributeRequest.getValue())) {
                    throw new BizException(ErrorCode.ITEM_ATTRIBUTE_VALUE_IS_EMPTY, attributeRequest.getAttributeId());
                }
            }
        }

        Category category = categoryService.getCategoryById(request.getCategoryId());
        Brand brand = brandService.getBrandById(request.getBrandId());

        ItemEntity itemEntity = new ItemEntity();
        if (uploadDescriptionToOSS) {
            // 存储描述到OSS
            String url = ossService.uploadItemDescription(request.getDescription());
            itemEntity.setDescriptionUrl(url);
        }

        itemEntity.setName(request.getName());
        itemEntity.setMainImageUrl(request.getMainImageUrl());
        try {
            itemEntity.setSubImageUrls(JacksonJsonUtils.toString(request.getSubImageUrls()));
        } catch (JsonProcessingException e) {
            itemEntity.setDescription(request.getDescription());
        }

        LocalDateTime now = LocalDateTime.now();
        itemEntity.setBrandId(request.getBrandId());
        itemEntity.setCategoryId(request.getCategoryId());
        itemEntity.setStatus(ItemStatus.DRAFT.name());
        itemEntity.setSortScore(Objects.requireNonNullElse(request.getSortScore(), defaultItemSortScore));
        itemEntity.setCreatedAt(now);
        itemEntity.setUpdatedAt(now);

        int effectRows = itemMapper.insert(itemEntity);
        if (effectRows != 1) {
            throw new BizException(ErrorCode.INTERNAL_ERROR);
        }
        return convertToEntity(itemEntity,item -> request.getDescription(), item -> category, item -> brand);
    }

    @Override
    public void updateItem(Long id, CreateItemRequest request) {

    }


    private Item convertToEntity(ItemEntity itemEntity, Function<ItemEntity, String> descriptionMap, Function<ItemEntity, Category> categoryMap, Function<ItemEntity, Brand> brandMap) {
        Item item = new Item();
        item.setId(itemEntity.getId());
        item.setBrand(brandMap.apply(itemEntity));
        item.setCategory(categoryMap.apply(itemEntity));
        item.setName(itemEntity.getName());
        item.setDescription(descriptionMap.apply(itemEntity));
        item.setMainImageUrl(itemEntity.getMainImageUrl());
        item.setSubImageUrls(itemEntity.getSubImageUrls());
        item.setStatus(ItemStatus.valueOf(itemEntity.getStatus()));
        item.setSortScore(itemEntity.getSortScore());
        return item;
    }

    private String getItemDescription(ItemEntity itemEntity) {
        if (StringUtils.isNotBlank(itemEntity.getDescriptionUrl())) {
            return ossService.getItemDescription(itemEntity.getDescriptionUrl());
        }
        return itemEntity.getDescription();
    }
    private Brand getItemBrand(ItemEntity itemEntity) {
        if (itemEntity.getBrandId() != null) {
            return brandService.getBrandById(itemEntity.getBrandId());
        }
        return null;
    }
    private Category getItemCategory(ItemEntity itemEntity) {
        if (itemEntity.getCategoryId() != null) {
            return categoryService.getCategoryById(itemEntity.getCategoryId());
        }
        return null;
    }

    private Set<String> getForbiddenWords() {
        return new HashSet<>(Arrays.asList(forbiddenWords.split(",")));
    }
}
