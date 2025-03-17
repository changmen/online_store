package com.example.onlinestore.dto;

import java.io.Serializable;

/**
 * 类目商品数量统计DTO
 */
public class CategoryItemCountDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long categoryId;
    private String categoryName;
    private Long itemCount;
    
    public CategoryItemCountDTO() {
    }
    
    public CategoryItemCountDTO(Long categoryId, String categoryName, Long itemCount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.itemCount = itemCount;
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
    
    public Long getItemCount() {
        return itemCount;
    }
    
    public void setItemCount(Long itemCount) {
        this.itemCount = itemCount;
    }
    
    @Override
    public String toString() {
        return "CategoryItemCountDTO{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", itemCount=" + itemCount +
                '}';
    }
} 