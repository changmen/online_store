package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.InventoryEntity;
import com.example.onlinestore.exception.InventoryException;
import com.example.onlinestore.mapper.InventoryMapper;
import com.example.onlinestore.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);
    
    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public void addInventory(InventoryEntity inventory) {
        inventoryMapper.insertInventory(inventory);
    }

    @Override
    public InventoryEntity getInventoryById(Long id) {
        return inventoryMapper.findById(id);
    }

    @Override
    public void updateInventory(InventoryEntity inventory) {
        inventoryMapper.updateInventory(inventory);
    }

    @Override
    public void deleteInventory(Long id) {
        inventoryMapper.deleteInventory(id);
    }

    @Override
    public List<InventoryEntity> getInventoriesBySkuId(Long skuId) {
        return inventoryMapper.findBySkuId(skuId);
    }

    @Override
    @Transactional
    public void lockInventory(Long inventoryId, Integer quantity) {
        InventoryEntity inventory = getInventoryById(inventoryId);
        if (inventory == null) {
            throw new InventoryException("库存不存在");
        }
        
        if (inventory.getAvailableQuantity() < quantity) {
            throw new InventoryException("可用库存不足");
        }
        
        inventoryMapper.updateQuantity(
            inventoryId,
            inventory.getQuantity(),
            inventory.getLockedQuantity() + quantity,
            inventory.getAvailableQuantity() - quantity
        );
    }

    @Override
    @Transactional
    public void unlockInventory(Long inventoryId, Integer quantity) {
        InventoryEntity inventory = getInventoryById(inventoryId);
        if (inventory == null) {
            throw new InventoryException("库存不存在");
        }
        
        if (inventory.getLockedQuantity() < quantity) {
            throw new InventoryException("锁定库存不足");
        }
        
        inventoryMapper.updateQuantity(
            inventoryId,
            inventory.getQuantity(),
            inventory.getLockedQuantity() - quantity,
            inventory.getAvailableQuantity() + quantity
        );
    }

    @Override
    @Transactional
    public void deductInventory(Long inventoryId, Integer quantity) {
        InventoryEntity inventory = getInventoryById(inventoryId);
        if (inventory == null) {
            throw new InventoryException("库存不存在");
        }
        
        if (inventory.getAvailableQuantity() < quantity) {
            throw new InventoryException("可用库存不足");
        }
        
        inventoryMapper.updateQuantity(
            inventoryId,
            inventory.getQuantity() - quantity,
            inventory.getLockedQuantity(),
            inventory.getAvailableQuantity() - quantity
        );
    }

    @Override
    @Transactional
    public void increaseInventory(Long inventoryId, Integer quantity) {
        InventoryEntity inventory = getInventoryById(inventoryId);
        if (inventory == null) {
            throw new InventoryException("库存不存在");
        }
        
        inventoryMapper.updateQuantity(
            inventoryId,
            inventory.getQuantity() + quantity,
            inventory.getLockedQuantity(),
            inventory.getAvailableQuantity() + quantity
        );
    }

    @Override
    public List<InventoryEntity> queryInventories(Long skuId, String location, String status, int page, int size) {
        int offset = (page - 1) * size;
        return inventoryMapper.findByCondition(skuId, location, status, offset, size);
    }

    @Override
    public long countInventories(Long skuId, String location, String status) {
        return inventoryMapper.countByCondition(skuId, location, status);
    }

    @Override
    public List<InventoryEntity> queryWarningInventory(int page, int size) {
        int offset = (page - 1) * size;
        return inventoryMapper.findWarningInventory(offset, size);
    }

    @Override
    public List<InventoryEntity> queryExpiringInventory(int days, int page, int size) {
        int offset = (page - 1) * size;
        return inventoryMapper.findExpiringInventory(days, offset, size);
    }

    @Override
    @Transactional
    public void processBatchInventory(Long inventoryId, Integer quantity, String operationType, String batchNumber) {
        // 1. 获取并验证库存信息
        InventoryEntity inventory = getInventoryById(inventoryId);
        if (inventory == null) {
            throw new InventoryException("库存不存在");
        }
        
        // 2. 验证库存状态
        if (!"NORMAL".equals(inventory.getStatus())) {
            throw new InventoryException("库存状态异常: " + inventory.getStatus());
        }
        
        // 3. 验证库存数量
        if (quantity <= 0) {
            throw new InventoryException("操作数量必须大于0");
        }
        
        // 4. 根据操作类型进行不同的处理
        switch (operationType) {
            case "LOCK":
                // 锁定库存
                if (inventory.getAvailableQuantity() < quantity) {
                    throw new InventoryException("可用库存不足");
                }
                inventoryMapper.updateQuantity(
                    inventoryId,
                    inventory.getQuantity(),
                    inventory.getLockedQuantity() + quantity,
                    inventory.getAvailableQuantity() - quantity
                );
                logger.info("锁定库存: inventoryId={}, quantity={}, batchNumber={}", inventoryId, quantity, batchNumber);
                break;
                
            case "UNLOCK":
                // 解锁库存
                if (inventory.getLockedQuantity() < quantity) {
                    throw new InventoryException("锁定库存不足");
                }
                inventoryMapper.updateQuantity(
                    inventoryId,
                    inventory.getQuantity(),
                    inventory.getLockedQuantity() - quantity,
                    inventory.getAvailableQuantity() + quantity
                );
                logger.info("解锁库存: inventoryId={}, quantity={}, batchNumber={}", inventoryId, quantity, batchNumber);
                break;
                
            case "DEDUCT":
                // 扣减库存
                if (inventory.getAvailableQuantity() < quantity) {
                    throw new InventoryException("可用库存不足");
                }
                inventoryMapper.updateQuantity(
                    inventoryId,
                    inventory.getQuantity() - quantity,
                    inventory.getLockedQuantity(),
                    inventory.getAvailableQuantity() - quantity
                );
                logger.info("扣减库存: inventoryId={}, quantity={}, batchNumber={}", inventoryId, quantity, batchNumber);
                break;
                
            case "INCREASE":
                // 增加库存
                inventoryMapper.updateQuantity(
                    inventoryId,
                    inventory.getQuantity() + quantity,
                    inventory.getLockedQuantity(),
                    inventory.getAvailableQuantity() + quantity
                );
                logger.info("增加库存: inventoryId={}, quantity={}, batchNumber={}", inventoryId, quantity, batchNumber);
                break;
                
            default:
                throw new InventoryException("不支持的操作类型: " + operationType);
        }
        
        // 5. 检查是否需要发送库存预警
        InventoryEntity updatedInventory = getInventoryById(inventoryId);
        if (updatedInventory.getAvailableQuantity() <= updatedInventory.getWarningQuantity()) {
            logger.warn("库存预警: inventoryId={}, availableQuantity={}, warningQuantity={}, batchNumber={}",
                inventoryId, updatedInventory.getAvailableQuantity(), updatedInventory.getWarningQuantity(), batchNumber);
        }
        
        // 6. 检查库存是否过期
        if (updatedInventory.getExpiryDate() != null && 
            updatedInventory.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.warn("库存过期: inventoryId={}, expiryDate={}, batchNumber={}",
                inventoryId, updatedInventory.getExpiryDate(), batchNumber);
        }
        
        // 7. 更新库存状态
        if (updatedInventory.getQuantity() == 0) {
            inventory.setStatus("EMPTY");
            inventoryMapper.updateInventory(inventory);
            logger.info("库存清空: inventoryId={}, batchNumber={}", inventoryId, batchNumber);
        }
    }
} 