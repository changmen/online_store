package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    // 查询所有分类
    List<CategoryEntity> findAllCategories(@Param("offset") int offset, @Param("limit") int limit);
}
