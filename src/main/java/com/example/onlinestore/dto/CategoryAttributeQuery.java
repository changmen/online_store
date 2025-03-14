package com.example.onlinestore.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * 类目属性查询参数
 */
public class CategoryAttributeQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * 类目ID
     */
    private Long categoryId;
    
    /**
     * 属性名称（模糊查询）
     */
    private String name;
    
    /**
     * 属性值类型：1-文本，2-数字，3-日期，4-枚举，5-布尔
     */
    private Integer valueType;
    
    /**
     * 是否必填：0-否，1-是
     */
    private Integer required;
    
    /**
     * 是否可搜索：0-否，1-是
     */
    private Integer searchable;
    
    /**
     * 页码
     */
    private Integer pageNum = 1;
    
    /**
     * 每页记录数
     */
    private Integer pageSize = 10;

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

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
} 