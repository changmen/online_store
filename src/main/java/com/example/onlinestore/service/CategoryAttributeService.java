package com.example.onlinestore.service;

import com.example.onlinestore.bean.CategoryAttribute;
import com.example.onlinestore.dto.CategoryAttributeQuery;

import java.util.List;
import java.util.Map;

/**
 * 类目属性服务接口
 */
public interface CategoryAttributeService {

    /**
     * 添加类目属性
     *
     * @param categoryAttribute 类目属性
     * @return 添加后的类目属性ID
     */
    Long addCategoryAttribute(CategoryAttribute categoryAttribute);

    /**
     * 修改类目属性
     *
     * @param categoryAttribute 类目属性
     * @return 是否修改成功
     */
    boolean updateCategoryAttribute(CategoryAttribute categoryAttribute);

    /**
     * 删除类目属性
     *
     * @param id 类目属性ID
     * @return 是否删除成功
     */
    boolean deleteCategoryAttribute(Long id);

    /**
     * 批量删除类目属性
     *
     * @param ids 类目属性ID列表
     * @return 是否删除成功
     */
    boolean piLiangDeleteCategoryAttributes(List<Long> ids);

    /**
     * 根据ID获取类目属性
     *
     * @param id 类目属性ID
     * @return 类目属性
     */
    CategoryAttribute getCategoryAttributeById(Long id);

    /**
     * 根据类目ID获取类目属性列表
     *
     * @param categoryId 类目ID
     * @return 类目属性列表
     */
    List<CategoryAttribute> getCategoryAttributesByCategoryId(Long categoryId);

    /**
     * 分页查询类目属性
     *
     * @param query 查询参数
     * @return 分页结果，包含列表和总数
     */
    Map<String, Object> pageCategoryAttributes(CategoryAttributeQuery query);

    /**
     * 检查类目属性名称是否已存在
     *
     * @param categoryId 类目ID
     * @param name 属性名称
     * @param excludeId 排除的属性ID（修改时使用）
     * @return 是否已存在
     */
    boolean checkNameExists(Long categoryId, String name, Long excludeId);
} 