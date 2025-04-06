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
    List<ItemAttributeRelationEntity> findByItemId(Long itemId);

    /**
     * 根据复合条件删除关联关系
     *
     * @param itemId      需要删除关联关系的物品ID
     * @param attributeId 需要删除关联关系的属性ID
     * @return 被删除的关联关系数量
     */
    int deleteByItemIdAndAttributeId(@Param("itemId") Long itemId, @Param("attributeId") Long attributeId);


    int deleteByItemIdAndAttributeIds(@Param("itemId") Long itemId, @Param("attributeIds") List<Long> attributeIds);

}
