package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.SkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SkuMapper {
    /**
     * 插入新的SKU实体
     *
     * @param skuEntity 待插入的SKU实体对象
     * @return 受影响的数据行数
     */
    int insert(SkuEntity skuEntity);

    /**
     * 批量插入SKU实体
     *
     * @param skuEntities 待插入的SKU实体对象列表
     * @return 受影响的数据行数
     */
    int batchInsert(List<SkuEntity> skuEntities);

    /**
     * 更新已存在的SKU实体
     *
     * @param skuEntity 待更新的SKU实体对象
     * @return 受影响的数据行数
     */
    int update(SkuEntity skuEntity);

    /**
     * 根据主键ID删除SKU记录
     *
     * @param id 要删除的SKU记录主键ID
     * @return 受影响的数据行数
     */
    int deleteById(Long id);

    /**
     * 根据主键ID查询SKU实体
     *
     * @param id 要查询的SKU记录主键ID
     * @return 查找到的SKU实体对象
     */
    SkuEntity findById(Long id);

    /**
     * 根据商品ID查询所有SKU
     *
     * @param itemId 商品ID
     * @return SKU列表
     */
    List<SkuEntity> findByItemId(Long itemId);

    /**
     * 根据SKU编码查询SKU
     *
     * @param skuCode SKU编码
     * @return SKU实体
     */
    SkuEntity findBySkuCode(String skuCode);

    /**
     * 更新SKU库存
     *
     * @param id SKU ID
     * @param quantity 要更新的库存数量
     * @return 受影响的数据行数
     */
    int updateStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 更新SKU销售数量
     *
     * @param id SKU ID
     * @param quantity 要增加的销售数量
     * @return 受影响的数据行数
     */
    int updateSoldQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);


    /**
     * 更新SKU库存
     *
     * @param id SKU ID
     * @param stockQuantity 更改的库存数量
     * @return 受影响的数据行数
     */
    int updateStockQuantity(@Param("id") Long id, @Param("stockQuantity") Integer stockQuantity);
} 