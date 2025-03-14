package com.example.onlinestore.bean;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class CategoryAttribute implements Serializable {


    @Serial
    private static final long serialVersionUID = 9116389788560235067L;
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private Integer valueType; // 1-文本，2-数字，3-日期，4-枚举，5-布尔
    private String valueTypeName;
    private Integer required; // 0-否，1-是
    private Integer searchable; // 0-否，1-是
    private Integer multiValue; // 0-否，1-是（仅当valueType=4时有效）
    private Integer sort;
    private String unit;
    private List<String> options; // 可选值列表（仅当valueType=4时有效）
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
        // 设置值类型名称
        if (valueType != null) {
            switch (valueType) {
                case 1 -> this.valueTypeName = "文本";
                case 2 -> this.valueTypeName = "数字";
                case 3 -> this.valueTypeName = "日期";
                case 4 -> this.valueTypeName = "枚举";
                case 5 -> this.valueTypeName = "布尔";
                default -> this.valueTypeName = "未知";
            }
        }
    }

    public String getValueTypeName() {
        return valueTypeName;
    }

    public void setValueTypeName(String valueTypeName) {
        this.valueTypeName = valueTypeName;
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

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
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