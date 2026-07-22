package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.*;
import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.enums.ItemStatus;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.ItemMapper;
import com.example.onlinestore.service.*;
import com.example.onlinestore.utils.JacksonJsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;


@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final boolean uploadDescriptionToOSS;
    private final int defaultItemSortScore;
    private static final String DEFAULT_ITEM_LIST_QUERY_ORDERBY = "id DESC";

    private final AttributeService attributeService;
    private final OssService ossService;
    private final ItemMapper itemMapper;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ItemDetailCacheService itemDetailCacheService;
    private final ContentValidationService contentValidationService;

    public ItemServiceImpl(@Value("${item.upload-description-to-oss:false}") boolean uploadDescriptionToOSS,
                           @Value("${item.default-sort-score:1}") int defaultItemSortScore,
                           AttributeService attributeService,
                           OssService ossService,
                           ItemMapper itemMapper,
                           BrandService brandService,
                           CategoryService categoryService,
                           ItemDetailCacheService itemDetailCacheService,
                           ContentValidationService contentValidationService) {
        this.uploadDescriptionToOSS = uploadDescriptionToOSS;
        this.defaultItemSortScore = defaultItemSortScore;
        this.attributeService = attributeService;
        this.ossService = ossService;
        this.itemMapper = itemMapper;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.itemDetailCacheService = itemDetailCacheService;
        this.contentValidationService = contentValidationService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Item createItem(@NotNull @Valid CreateItemRequest request) {
        validateForbiddenWords(request.getName(), request.getDescription());
        validateAttributes(request.getAttributes());

        if (categoryService.getCategoryById(request.getCategoryId()) == null) {
            throw new BizException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        brandService.getBrandById(request.getBrandId());

        ItemEntity itemEntity = new ItemEntity();
        if (uploadDescriptionToOSS) {
            String url = ossService.uploadItemDescription(request.getDescription());
            itemEntity.setDescriptionURL(url);
        }

        itemEntity.setName(request.getName());
        itemEntity.setMainImageURL(request.getMainImageUrl());
        itemEntity.setSubImageURLs(serializeSubImageUrls(request.getSubImageUrls()));

        LocalDateTime now = LocalDateTime.now();
        itemEntity.setBrandId(request.getBrandId());
        itemEntity.setCategoryId(request.getCategoryId());
        itemEntity.setStatus(ItemStatus.DRAFT.name());
        itemEntity.setSortScore(Objects.requireNonNullElse(request.getSortScore(), defaultItemSortScore));
        itemEntity.setCreatedAt(now);
        itemEntity.setUpdatedAt(now);

        int effectRows = itemMapper.insert(itemEntity);
        if (effectRows != 1) {
            logger.error("insert item failed. because effect rows is 0. itemName:{}", request.getName());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        attributeService.ensureItemAttributes(itemEntity.getId(), 0L, request.getAttributes());

        return convertToEntity(itemEntity, item -> request.getDescription());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateItem(@NotNull Long id, @NotNull @Valid UpdateItemRequest request) {
        if (itemMapper.findByIdBasic(id) == null) {
            logger.error("item not found, id: {}", id);
            throw new BizException(ErrorCode.ITEM_NOT_FOUND);
        }
        validateForbiddenWords(request.getName(), request.getDescription());
        validateAttributes(request.getAttributes());

        ItemEntity updateItemEntity = new ItemEntity();
        updateItemEntity.setId(id);
        if (uploadDescriptionToOSS) {
            String url = ossService.uploadItemDescription(request.getDescription());
            updateItemEntity.setDescriptionURL(url);
        } else {
            updateItemEntity.setDescription(request.getDescription());
        }

        updateItemEntity.setUpdatedAt(LocalDateTime.now());
        updateItemEntity.setName(request.getName());
        updateItemEntity.setMainImageURL(request.getMainImageUrl());
        updateItemEntity.setSubImageURLs(serializeSubImageUrls(request.getSubImageUrls()));

        int effectRows = itemMapper.update(updateItemEntity);
        if (effectRows != 1) {
            logger.error("update item failed. because effect rows is 0. itemId:{}", id);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        attributeService.ensureItemAttributes(id, 0L, request.getAttributes());
        itemDetailCacheService.evictItemDetailCache(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItemById(@NotNull Long id) {
        ItemEntity itemEntity = itemMapper.findById(id);
        if (itemEntity == null) {
            logger.error("item not found, id: {}", id);
            throw new BizException(ErrorCode.ITEM_NOT_FOUND);
        }
        return convertToEntity(itemEntity, this::getItemDescription);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Item> listItems(@NotNull @Valid ItemListQueryRequest queryRequest) {
        String orderBy = StringUtils.isNotBlank(queryRequest.getOrderBy())
                ? queryRequest.getOrderBy()
                : DEFAULT_ITEM_LIST_QUERY_ORDERBY;
        PageHelper.startPage(queryRequest.getPageNum(), queryRequest.getPageSize(), orderBy);
        List<ItemEntity> itemEntities = itemMapper.queryItemsByOptions(queryRequest);
        PageInfo<ItemEntity> pageInfo = new PageInfo<>(itemEntities);
        return Page.of(itemEntities.stream().map(itemEntity -> convertToEntity(itemEntity, e -> null)).toList(), pageInfo.getTotal(), queryRequest.getPageNum(), queryRequest.getPageSize());
    }

    private Item convertToEntity(ItemEntity itemEntity, Function<ItemEntity, String> descriptionMap) {
        Item item = new Item();
        item.setId(itemEntity.getId());
        item.setBrandId(itemEntity.getBrandId());
        item.setCategoryId(itemEntity.getCategoryId());
        item.setName(itemEntity.getName());
        item.setDescription(descriptionMap.apply(itemEntity));
        item.setMainImageURL(itemEntity.getMainImageURL());
        if (StringUtils.isNotBlank(itemEntity.getSubImageURLs())) {
            try {
                item.setSubImageURLs(JacksonJsonUtils.toListString(itemEntity.getSubImageURLs()));
            } catch (IOException e) {
                logger.error("parse subImageUrls failed. itemEntity:{}", itemEntity, e);
                item.setSubImageURLs(Collections.emptyList());
            }
        }
        if (StringUtils.isNotBlank(itemEntity.getStatus())) {
            try {
                item.setStatus(ItemStatus.valueOf(itemEntity.getStatus()));
            } catch (IllegalArgumentException e) {
                logger.warn("Unknown item status: {}, defaulting to DRAFT", itemEntity.getStatus());
                item.setStatus(ItemStatus.DRAFT);
            }
        }
        item.setSortScore(itemEntity.getSortScore());
        return item;
    }

    private String getItemDescription(ItemEntity itemEntity) {
        if (StringUtils.isNotBlank(itemEntity.getDescriptionURL())) {
            return ossService.getItemDescription(itemEntity.getDescriptionURL());
        }
        return itemEntity.getDescription();
    }


    private void validateForbiddenWords(String name, String description) {
        contentValidationService.validateForbiddenWords(name, ErrorCode.ITEM_NAME_CONTAINS_FORBIDDEN_WORDS);
        contentValidationService.validateForbiddenWords(description, ErrorCode.ITEM_DESCRIPTION_CONTAINS_FORBIDDEN_WORDS);
    }

    private void validateAttributes(List<ItemAttributeRequest> attributes) {
        attributeService.validateItemAttributes(attributes);
    }

    private String serializeSubImageUrls(List<String> subImageUrls) {
        try {
            return JacksonJsonUtils.toString(subImageUrls);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize subImageUrls: {}", subImageUrls, e);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
