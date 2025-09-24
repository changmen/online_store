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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 测试数据构造工厂类
 * 
 * 提供各种测试场景所需的数据对象构造方法，支持：
 * - 正常业务数据构造
 * - 边界条件数据构造
 * - 异常场景数据构造
 * - Mock响应数据构造
 */
public class TestDataFactory {

    // ==================== 常量定义 ====================
    
    public static final Long TEST_ATTRIBUTE_ID = 1L;
    public static final String TEST_ATTRIBUTE_NAME = "测试属性";
    public static final String TEST_DUPLICATE_NAME = "重复属性名";
    public static final Long TEST_ATTRIBUTE_VALUE_ID = 10L;
    public static final String TEST_ATTRIBUTE_VALUE = "测试属性值";
    public static final Long TEST_ITEM_ID = 100L;
    public static final Long TEST_SKU_ID = 200L;
    public static final Integer DEFAULT_SORT_SCORE = 10;
    public static final Integer DEFAULT_REQUIRED = 1;
    public static final Integer DEFAULT_SEARCHABLE = 1;
    public static final Integer DEFAULT_VISIBLE = 1;

    // ==================== CreateAttributeRequest 构造方法 ====================

    /**
     * 创建有效的属性创建请求对象
     */
    public CreateAttributeRequest buildValidCreateAttributeRequest() {
        CreateAttributeRequest request = new CreateAttributeRequest();
        request.setName(TEST_ATTRIBUTE_NAME);
        request.setAttributeType(AttributeType.SKU.name());
        request.setInputType(AttributeInputType.SINGLE_SELECT.name());
        request.setRequired(DEFAULT_REQUIRED);
        request.setSearchable(DEFAULT_SEARCHABLE);
        request.setSortScore(DEFAULT_SORT_SCORE);
        request.setVisible(DEFAULT_VISIBLE);
        return request;
    }

    /**
     * 创建重复名称的属性创建请求
     */
    public CreateAttributeRequest buildDuplicateNameCreateRequest() {
        CreateAttributeRequest request = buildValidCreateAttributeRequest();
        request.setName(TEST_DUPLICATE_NAME);
        return request;
    }

    /**
     * 创建包含最大长度名称的请求
     */
    public CreateAttributeRequest buildMaxLengthNameRequest() {
        CreateAttributeRequest request = buildValidCreateAttributeRequest();
        request.setName("a".repeat(255)); // 最大长度名称
        return request;
    }

    /**
     * 创建包含特殊字符的属性名请求
     */
    public CreateAttributeRequest buildSpecialCharacterNameRequest() {
        CreateAttributeRequest request = buildValidCreateAttributeRequest();
        request.setName("测试属性@#$%^&*()");
        return request;
    }

    /**
     * 创建输入类型的属性请求
     */
    public CreateAttributeRequest buildInputTypeRequest() {
        CreateAttributeRequest request = buildValidCreateAttributeRequest();
        request.setInputType(AttributeInputType.INPUT.name());
        return request;
    }

    // ==================== UpdateAttributeRequest 构造方法 ====================

    /**
     * 创建有效的属性更新请求
     */
    public UpdateAttributeRequest buildValidUpdateAttributeRequest() {
        UpdateAttributeRequest request = new UpdateAttributeRequest();
        request.setName("更新后的属性名");
        request.setAttributeType(AttributeType.SALE.name());
        request.setInputType(AttributeInputType.MULTI_SELECT.name());
        request.setRequired(0);
        request.setSearchable(0);
        request.setSortScore(20);
        request.setVisible(0);
        return request;
    }

    /**
     * 创建无变更的更新请求
     */
    public UpdateAttributeRequest buildNoChangeUpdateRequest() {
        UpdateAttributeRequest request = new UpdateAttributeRequest();
        request.setName(TEST_ATTRIBUTE_NAME);
        request.setAttributeType(AttributeType.SKU.name());
        request.setInputType(AttributeInputType.SINGLE_SELECT.name());
        request.setRequired(DEFAULT_REQUIRED);
        request.setSearchable(DEFAULT_SEARCHABLE);
        request.setSortScore(DEFAULT_SORT_SCORE);
        request.setVisible(DEFAULT_VISIBLE);
        return request;
    }

    /**
     * 创建部分字段更新请求
     */
    public UpdateAttributeRequest buildPartialUpdateRequest() {
        UpdateAttributeRequest request = new UpdateAttributeRequest();
        request.setName("新属性名");
        request.setSortScore(30);
        // 其他字段为null，表示不更新
        return request;
    }

    // ==================== AttributeEntity 构造方法 ====================

    /**
     * 创建属性实体对象
     */
    public AttributeEntity buildAttributeEntity() {
        AttributeEntity entity = new AttributeEntity();
        entity.setId(TEST_ATTRIBUTE_ID);
        entity.setName(TEST_ATTRIBUTE_NAME);
        entity.setAttributeType(AttributeType.SKU.name());
        entity.setInputType(AttributeInputType.SINGLE_SELECT.name());
        entity.setRequired(DEFAULT_REQUIRED);
        entity.setSearchable(DEFAULT_SEARCHABLE);
        entity.setSortScore(DEFAULT_SORT_SCORE);
        entity.setVisible(DEFAULT_VISIBLE);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    /**
     * 创建输入类型的属性实体
     */
    public AttributeEntity buildInputTypeAttributeEntity() {
        AttributeEntity entity = buildAttributeEntity();
        entity.setInputType(AttributeInputType.INPUT.name());
        return entity;
    }

    /**
     * 创建重复名称的属性实体
     */
    public AttributeEntity buildDuplicateNameEntity() {
        AttributeEntity entity = buildAttributeEntity();
        entity.setName(TEST_DUPLICATE_NAME);
        return entity;
    }

    // ==================== Attribute 构造方法 ====================

    /**
     * 创建属性域对象
     */
    public Attribute buildAttribute() {
        Attribute attribute = new Attribute();
        attribute.setId(TEST_ATTRIBUTE_ID);
        attribute.setName(TEST_ATTRIBUTE_NAME);
        attribute.setAttributeType(AttributeType.SKU);
        attribute.setInputType(AttributeInputType.SINGLE_SELECT);
        attribute.setRequired(DEFAULT_REQUIRED);
        attribute.setSearchable(DEFAULT_SEARCHABLE);
        attribute.setSortScore(DEFAULT_SORT_SCORE);
        attribute.setVisible(DEFAULT_VISIBLE);
        return attribute;
    }

    /**
     * 创建包含属性值的属性对象
     */
    public Attribute buildAttributeWithValues() {
        Attribute attribute = buildAttribute();
        attribute.setValues(buildAttributeValueList());
        return attribute;
    }

    /**
     * 创建输入类型的属性对象
     */
    public Attribute buildInputTypeAttribute() {
        Attribute attribute = buildAttribute();
        attribute.setInputType(AttributeInputType.INPUT);
        return attribute;
    }

    // ==================== AttributeValueEntity 构造方法 ====================

    /**
     * 创建属性值实体
     */
    public AttributeValueEntity buildAttributeValueEntity() {
        AttributeValueEntity entity = new AttributeValueEntity();
        entity.setId(TEST_ATTRIBUTE_VALUE_ID);
        entity.setAttributeId(TEST_ATTRIBUTE_ID);
        entity.setValue(TEST_ATTRIBUTE_VALUE);
        entity.setSortScore(DEFAULT_SORT_SCORE);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    /**
     * 创建属性值实体列表
     */
    public List<AttributeValueEntity> buildAttributeValueEntityList() {
        AttributeValueEntity entity1 = buildAttributeValueEntity();
        
        AttributeValueEntity entity2 = new AttributeValueEntity();
        entity2.setId(TEST_ATTRIBUTE_VALUE_ID + 1);
        entity2.setAttributeId(TEST_ATTRIBUTE_ID);
        entity2.setValue("属性值2");
        entity2.setSortScore(DEFAULT_SORT_SCORE + 1);
        entity2.setCreatedAt(LocalDateTime.now());
        entity2.setUpdatedAt(LocalDateTime.now());

        return Arrays.asList(entity1, entity2);
    }

    // ==================== AttributeValue 构造方法 ====================

    /**
     * 创建属性值域对象
     */
    public AttributeValue buildAttributeValue() {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setId(TEST_ATTRIBUTE_VALUE_ID);
        attributeValue.setAttributeId(TEST_ATTRIBUTE_ID);
        attributeValue.setValue(TEST_ATTRIBUTE_VALUE);
        attributeValue.setSortScore(DEFAULT_SORT_SCORE);
        return attributeValue;
    }

    /**
     * 创建属性值列表
     */
    public List<AttributeValue> buildAttributeValueList() {
        AttributeValue value1 = buildAttributeValue();
        
        AttributeValue value2 = new AttributeValue();
        value2.setId(TEST_ATTRIBUTE_VALUE_ID + 1);
        value2.setAttributeId(TEST_ATTRIBUTE_ID);
        value2.setValue("属性值2");
        value2.setSortScore(DEFAULT_SORT_SCORE + 1);

        return Arrays.asList(value1, value2);
    }

    // ==================== ItemAttributeRequest 构造方法 ====================

    /**
     * 创建商品属性请求
     */
    public ItemAttributeRequest buildItemAttributeRequest() {
        ItemAttributeRequest request = new ItemAttributeRequest();
        request.setAttributeId(TEST_ATTRIBUTE_ID);
        request.setAttributeValueId(TEST_ATTRIBUTE_VALUE_ID);
        request.setValue("自定义输入值");
        return request;
    }

    /**
     * 创建商品属性请求列表
     */
    public List<ItemAttributeRequest> buildItemAttributeRequestList() {
        ItemAttributeRequest request1 = buildItemAttributeRequest();
        
        ItemAttributeRequest request2 = new ItemAttributeRequest();
        request2.setAttributeId(TEST_ATTRIBUTE_ID + 1);
        request2.setAttributeValueId(TEST_ATTRIBUTE_VALUE_ID + 1);
        request2.setValue("自定义值2");

        return Arrays.asList(request1, request2);
    }

    // ==================== ItemAttributeRelationEntity 构造方法 ====================

    /**
     * 创建商品属性关系实体
     */
    public ItemAttributeRelationEntity buildItemAttributeRelationEntity() {
        ItemAttributeRelationEntity entity = new ItemAttributeRelationEntity();
        entity.setId(1L);
        entity.setItemId(TEST_ITEM_ID);
        entity.setSkuId(TEST_SKU_ID);
        entity.setAttributeId(TEST_ATTRIBUTE_ID);
        entity.setValueId(TEST_ATTRIBUTE_VALUE_ID);
        entity.setInputValue("输入值");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    /**
     * 创建商品属性关系实体列表
     */
    public List<ItemAttributeRelationEntity> buildItemAttributeRelationEntityList() {
        ItemAttributeRelationEntity entity1 = buildItemAttributeRelationEntity();
        
        ItemAttributeRelationEntity entity2 = new ItemAttributeRelationEntity();
        entity2.setId(2L);
        entity2.setItemId(TEST_ITEM_ID);
        entity2.setSkuId(TEST_SKU_ID);
        entity2.setAttributeId(TEST_ATTRIBUTE_ID + 1);
        entity2.setValueId(TEST_ATTRIBUTE_VALUE_ID + 1);
        entity2.setInputValue("输入值2");
        entity2.setCreatedAt(LocalDateTime.now());
        entity2.setUpdatedAt(LocalDateTime.now());

        return Arrays.asList(entity1, entity2);
    }

    // ==================== 边界条件数据构造 ====================

    /**
     * 创建空的商品属性请求列表
     */
    public List<ItemAttributeRequest> buildEmptyItemAttributeRequestList() {
        return List.of();
    }

    /**
     * 创建null值的创建请求（用于参数验证测试）
     */
    public CreateAttributeRequest buildNullCreateRequest() {
        return null;
    }

    /**
     * 创建无效ID（负数、零、null）
     */
    public Long buildInvalidId() {
        return -1L;
    }

    public Long buildZeroId() {
        return 0L;
    }

    public Long buildNullId() {
        return null;
    }
}