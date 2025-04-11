package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.ItemAttributeRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemAttributeRelationMapper {
    /**
     * 插入单条物品属性关联记录
     *
     * @param itemAttributeRelationEntity 包含物品ID、属性ID等关联信息的实体对象
     * @return 受影响的数据行数（1=成功，0=失败）
     */
    int insert(ItemAttributeRelationEntity itemAttributeRelationEntity);

    /**
     * 批量插入物品属性关联记录
     *
     * @param itemAttributeRelationEntities 包含多个物品属性关联信息的实体对象集合
     * @return 成功插入的记录总数
     */
    int batchInsert(List<ItemAttributeRelationEntity> itemAttributeRelationEntities);

    /**
     * 根据物品ID查询关联属性列表
     *
     * @param itemId 需要查询关联属性的物品ID
     * @return 匹配的关联属性实体列表（可能为空列表）
     */
    List<ItemAttributeRelationEntity> findByItemIdAndSkuId(Long itemId, Long skuId);

    /**
     * 根据复合条件删除关联关系
     *
     * @param itemId      需要删除关联关系的物品ID
     * @param attributeId 需要删除关联关系的属性ID
     * @return 被删除的关联关系数量
     */
    int deleteByItemIdAndAttributeId(@Param("itemId") Long itemId, @Param("attributeId") Long attributeId);

    /**
     * 根据条目ID和属性ID列表删除关联记录
     *
     * @param itemId       要删除的关联条目ID，非空
     * @param attributeIds 要删除的属性ID列表，非空列表
     * @return 返回被删除的记录数量。当返回值大于0时表示成功删除指定数量的记录，
     * 返回0表示没有符合条件的记录被删除
     */
    int deleteByItemIdAndAttributeIds(@Param("itemId") Long itemId, @Param("attributeIds") List<Long> attributeIds);


    /**
     * 分页查询指定属性和商品的关联关系
     *
     * @param attributeId 属性唯一标识，不能为null
     * @param offset      分页起始位置偏移量（从0开始计数）
     * @param limit       每页最大返回记录数
     * @return 符合查询条件的商品属性关联实体列表，当无结果时返回空列表
     */
    List<ItemAttributeRelationEntity> findByAndAttributeId(@Param("attributeId") Long attributeId, @Param("offset") int offset, @Param("limit") int limit);


}
