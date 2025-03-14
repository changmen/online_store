package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 查询所有分类
    List<CategoryEntity> findAllCategories(@Param("offset") int offset, @Param("limit") int limit);
    
    // 根据ID查询分类
    CategoryEntity findCategoryById(@Param("id") Long id);
    
    // 插入分类
    int insertCategory(CategoryEntity entity);
    
    // 更新分类
    int updateCategory(CategoryEntity entity);
    
    // 删除分类
    int deleteCategory(@Param("id") Long id);
    
    // 根据父ID查询子分类
    List<CategoryEntity> findCategoriesByParentId(@Param("parentId") Long parentId);
    
    // 检查分类名称是否已存在
    int checkNameExists(@Param("name") String name, @Param("parentId") Long parentId);
    
    // 检查分类名称是否已存在（排除指定ID）
    int checkNameExistsExcludeId(@Param("name") String name, @Param("parentId") Long parentId, @Param("id") Long id);
}
