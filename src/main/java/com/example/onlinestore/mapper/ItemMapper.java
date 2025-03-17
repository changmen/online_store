package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.dto.CategoryItemCountDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemMapper {
    void insertItem(ItemEntity item);
    ItemEntity findById(Long id);
    void updateItem(ItemEntity item);
    void deleteItem(Long id);
    List<ItemEntity> findAllWithPagination(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 按条件查询商品
     * @param categoryId 类目ID，可为null
     * @param name 商品名称，可为null，模糊匹配
     * @param offset 分页偏移量
     * @param limit 分页大小
     * @return 商品列表
     */
    List<ItemEntity> findByCondition(
        @Param("categoryId") Long categoryId, 
        @Param("name") String name, 
        @Param("offset") int offset, 
        @Param("limit") int limit
    );
    
    /**
     * 按条件统计商品总数
     * @param categoryId 类目ID，可为null
     * @param name 商品名称，可为null，模糊匹配
     * @return 商品总数
     */
    long countByCondition(
        @Param("categoryId") Long categoryId,
        @Param("name") String name
    );
    
    /**
     * 根据商品名称查询商品
     * @param name 商品名称
     * @return 商品实体
     */
    ItemEntity findByName(@Param("name") String name);
    
    /**
     * 根据商品名称查询商品（排除指定ID）
     * @param name 商品名称
     * @param excludeId 排除的商品ID
     * @return 商品实体
     */
    ItemEntity findByNameExcludeId(@Param("name") String name, @Param("excludeId") Long excludeId);
    
    /**
     * 按类目统计商品数量
     * @return 类目商品数量统计列表
     */
    List<CategoryItemCountDTO> countItemsByCategory();
    
    /**
     * 按类目统计商品数量（包含子类目）
     * @param categoryIds 类目ID列表
     * @return 类目ID到商品数量的映射
     */
    List<Map<String, Object>> countItemsByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
}
