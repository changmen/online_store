package com.example.onlinestore.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品查询DTO，用于封装查询参数
 */
public class ItemQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4983112718912894206L;
    private Long categoryId;
    private String name;
    private int page = 1;
    private int size = 10;
    
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
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
} 