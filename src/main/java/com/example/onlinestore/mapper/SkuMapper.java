package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.SkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkuMapper {
    /**
     * 插入SKU
     */
    void insertSku(SkuEntity skuEntity);
    
    /**
     * 根据ID查找SKU
     */
    SkuEntity findById(Long id);
    
    /**
     * 根据商品ID查找所有SKU
     */
    List<SkuEntity> findByItemId(Long itemId);
    
    /**
     * 更新SKU
     */
    void updateSku(SkuEntity skuEntity);
    
    /**
     * 删除SKU
     */
    void deleteSku(Long id);

    /**
     * 根据多个商品ID批量查找SKU
     */
    List<SkuEntity> findByItemIds(@Param("itemIds") List<Long> itemIds);
} 