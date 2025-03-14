package com.example.onlinestore.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

// 类目属性表
public class CategoryAttributeEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long categoryId;
    private String name;
    private Integer valueType; // 1-文本，2-数字，3-日期，4-枚举，5-布尔
    private Integer required; // 0-否，1-是
    private Integer searchable; // 0-否，1-是
    private Integer multiValue; // 0-否，1-是（仅当valueType=4时有效）
    private Integer sort;
    private String unit;
    private String options; // JSON格式的可选值列表（仅当valueType=4时有效）
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }

    public Integer getSearchable() {
        return searchable;
    }

    public void setSearchable(Integer searchable) {
        this.searchable = searchable;
    }

    public Integer getMultiValue() {
        return multiValue;
    }

    public void setMultiValue(Integer multiValue) {
        this.multiValue = multiValue;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 