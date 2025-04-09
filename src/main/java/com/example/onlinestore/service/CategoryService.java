package com.example.onlinestore.service;

import com.example.onlinestore.bean.Category;

import java.util.List;

/**
 * 商品类目服务
 */
public interface CategoryService {

    /**
     * 判断是否为根类目
     */
    boolean isRootCategory(Long categoryId);
    /**
     * 获取根类目
     */
    List<Category> getRootCategories();

    
    /**
     * 根据ID获取类目
     */
    Category getCategoryById(Long id);

    
    /**
     * 获取所有类目
     */
    List<Category> getAllCategories();
    
    /**
     * 获取子类目
     */
    List<Category> getChildCategories(Long parentId);

    /**
     * 获取类目路径
     * @param categoryId 类目ID
     * @return 类目路径，格式为：根类目ID->父类目ID->...->当前类目ID
     */
    String getCategoryPath(Long categoryId);
}