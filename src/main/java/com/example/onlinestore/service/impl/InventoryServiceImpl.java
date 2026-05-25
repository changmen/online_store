package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.InventoryEntity;
import com.example.onlinestore.exception.InventoryException;
import com.example.onlinestore.mapper.InventoryMapper;
import com.example.onlinestore.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(InventoryMapper inventoryMapper) {
        this.inventoryMapper = inventoryMapper;
    }

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
        int affected = inventoryMapper.atomicLockInventory(inventoryId, quantity);
        if (affected == 0) {
            InventoryEntity inventory = getInventoryById(inventoryId);
            if (inventory == null) {
                throw new InventoryException("库存不存在");
            }
            throw new InventoryException("可用库存不足");
        }
    }

    @Override
    @Transactional
    public void unlockInventory(Long inventoryId, Integer quantity) {
        int affected = inventoryMapper.atomicUnlockInventory(inventoryId, quantity);
        if (affected == 0) {
            InventoryEntity inventory = getInventoryById(inventoryId);
            if (inventory == null) {
                throw new InventoryException("库存不存在");
            }
            throw new InventoryException("锁定库存不足");
        }
    }

    @Override
    @Transactional
    public void deductInventory(Long inventoryId, Integer quantity) {
        if (inventoryId == null) {
            throw new InventoryException("库存ID不能为空");
        }
        if (quantity == null || quantity <= 0) {
            throw new InventoryException("扣减数量必须大于0");
        }

        int affected = inventoryMapper.atomicDeductInventory(inventoryId, quantity);
        if (affected == 0) {
            InventoryEntity inventory = getInventoryById(inventoryId);
            if (inventory == null) {
                throw new InventoryException("库存不存在");
            }
            throw new InventoryException("可用库存不足");
        }
        logger.info("扣减库存成功: inventoryId={}, quantity={}", inventoryId, quantity);
    }

    @Override
    @Transactional
    public void increaseInventory(Long inventoryId, Integer quantity) {
        if (inventoryId == null) {
            throw new InventoryException("库存ID不能为空");
        }
        if (quantity == null || quantity <= 0) {
            throw new InventoryException("增加数量必须大于0");
        }

        int affected = inventoryMapper.atomicIncreaseInventory(inventoryId, quantity);
        if (affected == 0) {
            InventoryEntity inventory = getInventoryById(inventoryId);
            if (inventory == null) {
                throw new InventoryException("库存不存在");
            }
            throw new InventoryException("增加库存失败");
        }
        logger.info("增加库存成功: inventoryId={}, quantity={}", inventoryId, quantity);
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
        InventoryEntity inventory = getInventoryById(inventoryId);
        if (inventory == null) {
            throw new InventoryException("库存不存在");
        }

        if (!"NORMAL".equals(inventory.getStatus())) {
            throw new InventoryException("库存状态异常: " + inventory.getStatus());
        }

        if (quantity <= 0) {
            throw new InventoryException("操作数量必须大于0");
        }

        int affected;
        switch (operationType) {
            case "LOCK":
                affected = inventoryMapper.atomicLockInventory(inventoryId, quantity);
                if (affected == 0) {
                    throw new InventoryException("可用库存不足");
                }
                logger.info("锁定库存: inventoryId={}, quantity={}, batchNumber={}", inventoryId, quantity, batchNumber);
                break;

            case "UNLOCK":
                affected = inventoryMapper.atomicUnlockInventory(inventoryId, quantity);
                if (affected == 0) {
                    throw new InventoryException("锁定库存不足");
                }
                logger.info("解锁库存: inventoryId={}, quantity={}, batchNumber={}", inventoryId, quantity, batchNumber);
                break;

            case "DEDUCT":
                affected = inventoryMapper.atomicDeductInventory(inventoryId, quantity);
                if (affected == 0) {
                    throw new InventoryException("可用库存不足");
                }
                logger.info("扣减库存: inventoryId={}, quantity={}, batchNumber={}", inventoryId, quantity, batchNumber);
                break;

            case "INCREASE":
                affected = inventoryMapper.atomicIncreaseInventory(inventoryId, quantity);
                if (affected == 0) {
                    throw new InventoryException("增加库存失败");
                }
                logger.info("增加库存: inventoryId={}, quantity={}, batchNumber={}", inventoryId, quantity, batchNumber);
                break;

            default:
                throw new InventoryException("不支持的操作类型: " + operationType);
        }

        InventoryEntity updatedInventory = getInventoryById(inventoryId);
        if (updatedInventory.getAvailableQuantity() <= updatedInventory.getWarningQuantity()) {
            logger.warn("库存预警: inventoryId={}, availableQuantity={}, warningQuantity={}, batchNumber={}",
                inventoryId, updatedInventory.getAvailableQuantity(), updatedInventory.getWarningQuantity(), batchNumber);
        }

        if (updatedInventory.getExpiryDate() != null &&
            updatedInventory.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.warn("库存过期: inventoryId={}, expiryDate={}, batchNumber={}",
                inventoryId, updatedInventory.getExpiryDate(), batchNumber);
        }

        if (updatedInventory.getQuantity() == 0) {
            inventory.setStatus("EMPTY");
            inventoryMapper.updateInventory(inventory);
            logger.info("库存清空: inventoryId={}, batchNumber={}", inventoryId, batchNumber);
        }
    }

    @Override
    @Transactional
    public void updateSkuInventory(
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
    ) {
        if (skuId == null) {
            throw new InventoryException("SKU ID不能为空");
        }
        if (quantity == null || quantity < 0) {
            throw new InventoryException("库存数量不能为空且必须大于等于0");
        }
        if (lockedQuantity == null || lockedQuantity < 0) {
            throw new InventoryException("锁定数量不能为空且必须大于等于0");
        }
        if (availableQuantity == null || availableQuantity < 0) {
            throw new InventoryException("可用数量不能为空且必须大于等于0");
        }
        if (quantity < lockedQuantity) {
            throw new InventoryException("库存数量不能小于锁定数量");
        }
        if (quantity < availableQuantity) {
            throw new InventoryException("库存数量不能小于可用数量");
        }
        if (quantity < (lockedQuantity + availableQuantity)) {
            throw new InventoryException("库存数量不能小于锁定数量和可用数量之和");
        }

        List<InventoryEntity> inventories = inventoryMapper.findBySkuId(skuId);
        InventoryEntity inventory;

        if (inventories.isEmpty()) {
            inventory = new InventoryEntity();
            inventory.setSkuId(skuId);
            inventory.setCreatedAt(LocalDateTime.now());
        } else {
            inventory = inventories.get(0);
        }

        inventory.setQuantity(quantity);
        inventory.setLockedQuantity(lockedQuantity);
        inventory.setAvailableQuantity(availableQuantity);
        inventory.setWarningQuantity(warningQuantity);
        inventory.setLocation(location);
        inventory.setBatchNumber(batchNumber);
        inventory.setProductionDate(productionDate);
        inventory.setExpiryDate(expiryDate);
        inventory.setStatus(status);
        inventory.setUpdatedAt(LocalDateTime.now());

        if (inventory.getId() == null) {
            inventoryMapper.insertInventory(inventory);
        } else {
            inventoryMapper.updateInventory(inventory);
        }

        logger.info("更新SKU库存: skuId={}, quantity={}, lockedQuantity={}, availableQuantity={}, " +
                   "warningQuantity={}, location={}, batchNumber={}, operator={}, remark={}",
            skuId, quantity, lockedQuantity, availableQuantity, warningQuantity,
            location, batchNumber, operator, remark);

        if (availableQuantity <= warningQuantity) {
            logger.warn("SKU库存预警: skuId={}, availableQuantity={}, warningQuantity={}",
                skuId, availableQuantity, warningQuantity);
        }

        if (expiryDate != null && expiryDate.isBefore(LocalDateTime.now())) {
            logger.warn("SKU库存过期: skuId={}, expiryDate={}", skuId, expiryDate);
        }
    }
} 