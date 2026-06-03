package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.*;
import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.enums.AttributeInputType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Value("${forbidden-words:刀}")
    private String forbiddenWords;

    @Value("${item.upload-description-to-oss:false}")
    private boolean uploadDescriptionToOSS;

    @Value("${item.default-sort-score:1}")
    private int defaultItemSortScore;

    private static final String DEFAULT_ITEM_LIST_QUERY_ORDERBY = "id DESC";

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
    @Transactional(rollbackFor = Exception.class)
    public Item createItem(@NotNull @Valid CreateItemRequest request) {
        validateForbiddenWords(request.getName(), request.getDescription());
        validateAttributes(request.getAttributes());

        categoryService.getCategoryById(request.getCategoryId());
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
        getItemById(id);
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
    }

    @Override
    public Item getItemById(@NotNull Long id) {
        ItemEntity itemEntity = itemMapper.findById(id);
        if (itemEntity == null) {
            logger.error("item not found, id: {}", id);
            throw new BizException(ErrorCode.ITEM_NOT_FOUND);
        }
        return convertToEntity(itemEntity, this::getItemDescription);
    }

    @Override
    public Page<Item> listItems(@NotNull @Valid ItemListQueryRequest queryRequest) {
        PageHelper.startPage(queryRequest.getPageNum(), queryRequest.getPageSize(), DEFAULT_ITEM_LIST_QUERY_ORDERBY);
        List<ItemEntity> itemEntities = itemMapper.queryItemsByOptions(queryRequest);
        PageInfo<ItemEntity> pageInfo = new PageInfo<>(itemEntities);
        return Page.of(itemEntities.stream().map(itemEntity -> convertToEntity(itemEntity, this::getItemDescription)).toList(), pageInfo.getTotal(), queryRequest.getPageNum(), queryRequest.getPageSize());
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
        item.setStatus(ItemStatus.valueOf(itemEntity.getStatus()));
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
        Set<String> words = getForbiddenWords();
        if (words.stream().anyMatch(StringUtils.toRootLowerCase(StringUtils.trim(name))::contains)) {
            throw new BizException(ErrorCode.ITEM_NAME_CONTAINS_FORBIDDEN_WORDS, name);
        }
        if (StringUtils.isNotBlank(description)
                && words.stream().anyMatch(StringUtils.toRootLowerCase(StringUtils.trim(description))::contains)) {
            throw new BizException(ErrorCode.ITEM_DESCRIPTION_CONTAINS_FORBIDDEN_WORDS, description);
        }
    }

    private void validateAttributes(List<ItemAttributeRequest> attributes) {
        List<Long> attributeIds = attributes.stream()
                .map(ItemAttributeRequest::getAttributeId).toList();
        Map<Long, Attribute> attributeMap = attributeService.getAttributesByIds(attributeIds).stream()
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
                if (StringUtils.isBlank(attributeRequest.getValue())) {
                    throw new BizException(ErrorCode.ITEM_ATTRIBUTE_VALUE_IS_EMPTY, attributeRequest.getAttributeId());
                }
            }
        }
    }

    private String serializeSubImageUrls(List<String> subImageUrls) {
        try {
            return JacksonJsonUtils.toString(subImageUrls);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize subImageUrls: {}", subImageUrls, e);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Set<String> getForbiddenWords() {
        if (StringUtils.isBlank(this.forbiddenWords)) {
            return Collections.emptySet();
        }
        return Arrays.stream(this.forbiddenWords.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }
}
