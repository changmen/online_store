package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.CategoryAttributeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryAttributeMapper {
    
    // 插入类目属性
    Long insertCategoryAttribute(CategoryAttributeEntity entity);
    
    // 更新类目属性
    int updateCategoryAttribute(CategoryAttributeEntity entity);
    
    // 删除类目属性
    int deleteCategoryAttributeById(@Param("id") Long id);
    
    // 批量删除类目属性
    int batchDeleteCategoryAttributes(@Param("ids") List<Long> ids);
    
    // 根据ID查询类目属性
    CategoryAttributeEntity findCategoryAttributeById(@Param("id") Long id);
    
    // 根据类目ID查询类目属性列表
    List<CategoryAttributeEntity> findCategoryAttributesByCategoryId(@Param("categoryId") Long categoryId);
    
    // 根据类目ID和属性名称查询类目属性
    CategoryAttributeEntity findCategoryAttributeByCategoryIdAndName(
            @Param("categoryId") Long categoryId, 
            @Param("name") String name);
    
    // 检查类目属性名称是否已存在
    int checkNameExists(
            @Param("categoryId") Long categoryId, 
            @Param("name") String name);
    
    // 检查类目属性名称是否已存在（排除指定ID）
    int checkNameExistsExcludeId(
            @Param("categoryId") Long categoryId, 
            @Param("name") String name, 
            @Param("id") Long id);
    
    // 分页查询类目属性
    List<CategoryAttributeEntity> findCategoryAttributesByConditions(
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            @Param("valueType") Integer valueType,
            @Param("required") Integer required,
            @Param("searchable") Integer searchable,
            @Param("offset") int offset,
            @Param("limit") int limit);
    
    // 统计类目属性数量
    int countCategoryAttributesByConditions(
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            @Param("valueType") Integer valueType,
            @Param("required") Integer required,
            @Param("searchable") Integer searchable);
} 