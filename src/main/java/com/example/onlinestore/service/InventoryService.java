package com.example.onlinestore.service;

import com.example.onlinestore.entity.InventoryEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryService {
    /**
     * 添加库存
     */
    void addInventory(InventoryEntity inventory);
    
    /**
     * 获取库存信息
     */
    InventoryEntity getInventoryById(Long id);
    
    /**
     * 更新库存信息
     */
    void updateInventory(InventoryEntity inventory);
    
    /**
     * 删除库存
     */
    void deleteInventory(Long id);
    
    /**
     * 获取SKU的所有库存
     */
    List<InventoryEntity> getInventoriesBySkuId(Long skuId);
    
    /**
     * 锁定库存
     * @param inventoryId 库存ID
     * @param quantity 锁定数量
     */
    void lockInventory(Long inventoryId, Integer quantity);
    
    /**
     * 解锁库存
     * @param inventoryId 库存ID
     * @param quantity 解锁数量
     */
    void unlockInventory(Long inventoryId, Integer quantity);
    
    /**
     * 扣减库存
     * @param inventoryId 库存ID
     * @param quantity 扣减数量
     */
    void deductInventory(Long inventoryId, Integer quantity);
    
    /**
     * 增加库存
     * @param inventoryId 库存ID
     * @param quantity 增加数量
     */
    void increaseInventory(Long inventoryId, Integer quantity);
    
    /**
     * 按条件查询库存
     */
    List<InventoryEntity> queryInventories(Long skuId, String location, String status, int page, int size);
    
    /**
     * 按条件统计库存总数
     */
    long countInventories(Long skuId, String location, String status);
    
    /**
     * 查询库存预警的商品
     */
    List<InventoryEntity> queryWarningInventory(int page, int size);
    
    /**
     * 查询即将过期的库存
     */
    List<InventoryEntity> queryExpiringInventory(int days, int page, int size);
    
    /**
     * 批量处理库存（包含复杂的业务逻辑，超过80行）
     * 1. 检查库存状态
     * 2. 验证库存数量
     * 3. 处理库存锁定
     * 4. 更新库存数量
     * 5. 记录库存变动
     * 6. 发送库存预警
     * 7. 更新库存状态
     */
    void processBatchInventory(Long inventoryId, Integer quantity, String operationType, String batchNumber);

    /**
     * 更新SKU库存
     * @param skuId SKU ID
     * @param quantity 库存数量
     * @param lockedQuantity 锁定数量
     * @param availableQuantity 可用数量
     * @param warningQuantity 预警数量
     * @param location 库位
     * @param batchNumber 批次号
     * @param productionDate 生产日期
     * @param expiryDate 过期日期
     * @param status 状态
     * @param operator 操作人
     * @param remark 备注
     */
    void updateSkuInventory(
        Long skuId,
        Integer quantity,
        Integer lockedQuantity,
        Integer availableQuantity,
        Integer warningQuantity,
        String location,
        String batchNumber,
        LocalDateTime productionDate,
        LocalDateTime expiryDate,
        String status,
        String operator,
        String remark
    );
} 