package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.InventoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InventoryMapper {
    void insertInventory(InventoryEntity inventory);
    InventoryEntity findById(Long id);
    List<InventoryEntity> findBySkuId(Long skuId);
    void updateInventory(InventoryEntity inventory);
    void deleteInventory(Long id);
    
    /**
     * 更新库存数量
     * @param id 库存ID
     * @param quantity 新的库存数量
     * @param lockedQuantity 新的锁定数量
     * @param availableQuantity 新的可用数量
     */
    void updateQuantity(
        @Param("id") Long id,
        @Param("quantity") Integer quantity,
        @Param("lockedQuantity") Integer lockedQuantity,
        @Param("availableQuantity") Integer availableQuantity
    );
    
    /**
     * 按条件查询库存
     * @param skuId SKU ID
     * @param location 库位
     * @param status 状态
     * @param offset 分页偏移量
     * @param limit 分页大小
     */
    List<InventoryEntity> findByCondition(
        @Param("skuId") Long skuId,
        @Param("location") String location,
        @Param("status") String status,
        @Param("offset") int offset,
        @Param("limit") int limit
    );
    
    /**
     * 按条件统计库存总数
     */
    long countByCondition(
        @Param("skuId") Long skuId,
        @Param("location") String location,
        @Param("status") String status
    );
    
    /**
     * 查询库存预警的商品
     */
    List<InventoryEntity> findWarningInventory(
        @Param("offset") int offset,
        @Param("limit") int limit
    );
    
    /**
     * 查询即将过期的库存
     */
    List<InventoryEntity> findExpiringInventory(
        @Param("days") int days,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    /**
     * 原子锁定库存：仅在可用库存充足时执行
     * @return 受影响的行数（0 表示库存不足）
     */
    int atomicLockInventory(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 原子扣减库存：仅在可用库存充足时执行
     * @return 受影响的行数（0 表示库存不足）
     */
    int atomicDeductInventory(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 原子解锁库存
     * @return 受影响的行数
     */
    int atomicUnlockInventory(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 原子增加库存
     * @return 受影响的行数
     */
    int atomicIncreaseInventory(@Param("id") Long id, @Param("quantity") Integer quantity);
} 