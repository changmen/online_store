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
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class InventoryServiceImpl implements InventoryService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);
    
    @Autowired
    private InventoryMapper inventoryMapper;

    // 库存锁映射，用于控制并发访问
    private final Map<Long, Lock> inventoryLocks = new ConcurrentHashMap<>();
    
    // 商品锁映射，用于控制并发访问
    private final Map<Long, Lock> skuLocks = new ConcurrentHashMap<>();
    
    // 获取库存锁
    private Lock getInventoryLock(Long inventoryId) {
        return inventoryLocks.computeIfAbsent(inventoryId, k -> new ReentrantLock());
    }
    
    // 获取商品锁
    private Lock getSkuLock(Long skuId) {
        return skuLocks.computeIfAbsent(skuId, k -> new ReentrantLock());
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

    /**
     * 扣减库存 - BAD CASE: 存在死锁风险
     * 
     * 死锁风险说明：
     * 1. 当多个线程同时操作不同库存记录但关联到相同SKU时
     * 2. 线程A获取了库存A的锁，等待SKU X的锁
     * 3. 线程B获取了SKU X的锁，等待库存A的锁
     * 4. 两个线程互相等待对方释放锁，形成死锁
     */
    @Override
    @Transactional
    public void deductInventory(Long inventoryId, Integer quantity) {
        if (inventoryId == null) {
            throw new InventoryException("库存ID不能为空");
        }
        
        if (quantity == null || quantity <= 0) {
            throw new InventoryException("扣减数量必须大于0");
        }
        
        // 获取库存锁
        Lock inventoryLock = getInventoryLock(inventoryId);
        boolean inventoryLockAcquired = false;
        
        try {
            // 尝试获取库存锁，超时时间5秒
            inventoryLockAcquired = inventoryLock.tryLock(5, TimeUnit.SECONDS);
            if (!inventoryLockAcquired) {
                throw new InventoryException("获取库存锁超时");
            }
            
            logger.debug("获取库存锁: inventoryId={}", inventoryId);
            
            // 获取库存信息
            InventoryEntity inventory = getInventoryById(inventoryId);
            if (inventory == null) {
                throw new InventoryException("库存不存在");
            }
            
            // 检查可用库存
            if (inventory.getAvailableQuantity() < quantity) {
                throw new InventoryException("可用库存不足");
            }
            
            // BAD CASE: 在持有库存锁的情况下，尝试获取SKU锁，可能导致死锁
            Lock skuLock = getSkuLock(inventory.getSkuId());
            boolean skuLockAcquired = false;
            
            try {
                // 尝试获取SKU锁，超时时间5秒
                skuLockAcquired = skuLock.tryLock(5, TimeUnit.SECONDS);
                if (!skuLockAcquired) {
                    throw new InventoryException("获取SKU锁超时");
                }
                
                logger.debug("获取SKU锁: skuId={}", inventory.getSkuId());
                
                // 执行库存扣减
                inventoryMapper.updateQuantity(
                    inventoryId,
                    inventory.getQuantity() - quantity,
                    inventory.getLockedQuantity(),
                    inventory.getAvailableQuantity() - quantity
                );
                
                logger.info("扣减库存成功: inventoryId={}, quantity={}, newQuantity={}, newAvailableQuantity={}",
                    inventoryId, quantity, inventory.getQuantity() - quantity, inventory.getAvailableQuantity() - quantity);
            } finally {
                if (skuLockAcquired) {
                    skuLock.unlock();
                    logger.debug("释放SKU锁: skuId={}", inventory.getSkuId());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InventoryException("获取锁被中断");
        } finally {
            if (inventoryLockAcquired) {
                inventoryLock.unlock();
                logger.debug("释放库存锁: inventoryId={}", inventoryId);
            }
        }
    }

    /**
     * 增加库存 - BAD CASE: 存在死锁风险
     * 
     * 死锁风险说明：
     * 1. 与deductInventory方法相反的锁获取顺序
     * 2. 这里先获取SKU锁，再获取库存锁
     * 3. 当deductInventory和increaseInventory同时执行时，可能导致死锁
     */
    @Override
    @Transactional
    public void increaseInventory(Long inventoryId, Integer quantity) {
        if (inventoryId == null) {
            throw new InventoryException("库存ID不能为空");
        }
        
        if (quantity == null || quantity <= 0) {
            throw new InventoryException("增加数量必须大于0");
        }
        
        // 获取库存信息（不加锁）
        InventoryEntity inventory = getInventoryById(inventoryId);
        if (inventory == null) {
            throw new InventoryException("库存不存在");
        }
        
        // BAD CASE: 与deductInventory相反的锁获取顺序，先获取SKU锁，再获取库存锁
        Lock skuLock = getSkuLock(inventory.getSkuId());
        boolean skuLockAcquired = false;
        
        try {
            // 尝试获取SKU锁，超时时间5秒
            skuLockAcquired = skuLock.tryLock(5, TimeUnit.SECONDS);
            if (!skuLockAcquired) {
                throw new InventoryException("获取SKU锁超时");
            }
            
            logger.debug("获取SKU锁: skuId={}", inventory.getSkuId());
            
            // 获取库存锁
            Lock inventoryLock = getInventoryLock(inventoryId);
            boolean inventoryLockAcquired = false;
            
            try {
                // 尝试获取库存锁，超时时间5秒
                inventoryLockAcquired = inventoryLock.tryLock(5, TimeUnit.SECONDS);
                if (!inventoryLockAcquired) {
                    throw new InventoryException("获取库存锁超时");
                }
                
                logger.debug("获取库存锁: inventoryId={}", inventoryId);
                
                // 执行库存增加
                inventoryMapper.updateQuantity(
                    inventoryId,
                    inventory.getQuantity() + quantity,
                    inventory.getLockedQuantity(),
                    inventory.getAvailableQuantity() + quantity
                );
                
                logger.info("增加库存成功: inventoryId={}, quantity={}, newQuantity={}, newAvailableQuantity={}",
                    inventoryId, quantity, inventory.getQuantity() + quantity, inventory.getAvailableQuantity() + quantity);
            } finally {
                if (inventoryLockAcquired) {
                    inventoryLock.unlock();
                    logger.debug("释放库存锁: inventoryId={}", inventoryId);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InventoryException("获取锁被中断");
        } finally {
            if (skuLockAcquired) {
                skuLock.unlock();
                logger.debug("释放SKU锁: skuId={}", inventory.getSkuId());
            }
        }
    }
    
    /**
     * 扣减库存 - 安全版本
     * 
     * 解决死锁风险的方法：
     * 1. 始终按照固定顺序获取锁（例如，总是先获取SKU锁，再获取库存锁）
     * 2. 使用超时锁，避免无限等待
     */
    public void deductInventorySafe(Long inventoryId, Integer quantity) {
        if (inventoryId == null) {
            throw new InventoryException("库存ID不能为空");
        }
        
        if (quantity == null || quantity <= 0) {
            throw new InventoryException("扣减数量必须大于0");
        }
        
        // 获取库存信息（不加锁）
        InventoryEntity inventory = getInventoryById(inventoryId);
        if (inventory == null) {
            throw new InventoryException("库存不存在");
        }
        
        // 安全版本：始终按照固定顺序获取锁，先获取SKU锁，再获取库存锁
        Lock skuLock = getSkuLock(inventory.getSkuId());
        boolean skuLockAcquired = false;
        
        try {
            // 尝试获取SKU锁，超时时间5秒
            skuLockAcquired = skuLock.tryLock(5, TimeUnit.SECONDS);
            if (!skuLockAcquired) {
                throw new InventoryException("获取SKU锁超时");
            }
            
            logger.debug("获取SKU锁: skuId={}", inventory.getSkuId());
            
            // 获取库存锁
            Lock inventoryLock = getInventoryLock(inventoryId);
            boolean inventoryLockAcquired = false;
            
            try {
                // 尝试获取库存锁，超时时间5秒
                inventoryLockAcquired = inventoryLock.tryLock(5, TimeUnit.SECONDS);
                if (!inventoryLockAcquired) {
                    throw new InventoryException("获取库存锁超时");
                }
                
                logger.debug("获取库存锁: inventoryId={}", inventoryId);
                
                // 重新获取库存信息（加锁后）
                inventory = getInventoryById(inventoryId);
                if (inventory == null) {
                    throw new InventoryException("库存不存在");
                }
                
                // 检查可用库存
                if (inventory.getAvailableQuantity() < quantity) {
                    throw new InventoryException("可用库存不足");
                }
                
                // 执行库存扣减
                inventoryMapper.updateQuantity(
                    inventoryId,
                    inventory.getQuantity() - quantity,
                    inventory.getLockedQuantity(),
                    inventory.getAvailableQuantity() - quantity
                );
                
                logger.info("扣减库存成功: inventoryId={}, quantity={}, newQuantity={}, newAvailableQuantity={}",
                    inventoryId, quantity, inventory.getQuantity() - quantity, inventory.getAvailableQuantity() - quantity);
            } finally {
                if (inventoryLockAcquired) {
                    inventoryLock.unlock();
                    logger.debug("释放库存锁: inventoryId={}", inventoryId);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InventoryException("获取锁被中断");
        } finally {
            if (skuLockAcquired) {
                skuLock.unlock();
                logger.debug("释放SKU锁: skuId={}", inventory.getSkuId());
            }
        }
    }
    
    /**
     * 增加库存 - 安全版本
     * 
     * 解决死锁风险的方法：
     * 1. 始终按照固定顺序获取锁（例如，总是先获取SKU锁，再获取库存锁）
     * 2. 使用超时锁，避免无限等待
     */
    public void increaseInventorySafe(Long inventoryId, Integer quantity) {
        if (inventoryId == null) {
            throw new InventoryException("库存ID不能为空");
        }
        
        if (quantity == null || quantity <= 0) {
            throw new InventoryException("增加数量必须大于0");
        }
        
        // 获取库存信息（不加锁）
        InventoryEntity inventory = getInventoryById(inventoryId);
        if (inventory == null) {
            throw new InventoryException("库存不存在");
        }
        
        // 安全版本：始终按照固定顺序获取锁，先获取SKU锁，再获取库存锁
        Lock skuLock = getSkuLock(inventory.getSkuId());
        boolean skuLockAcquired = false;
        
        try {
            // 尝试获取SKU锁，超时时间5秒
            skuLockAcquired = skuLock.tryLock(5, TimeUnit.SECONDS);
            if (!skuLockAcquired) {
                throw new InventoryException("获取SKU锁超时");
            }
            
            logger.debug("获取SKU锁: skuId={}", inventory.getSkuId());
            
            // 获取库存锁
            Lock inventoryLock = getInventoryLock(inventoryId);
            boolean inventoryLockAcquired = false;
            
            try {
                // 尝试获取库存锁，超时时间5秒
                inventoryLockAcquired = inventoryLock.tryLock(5, TimeUnit.SECONDS);
                if (!inventoryLockAcquired) {
                    throw new InventoryException("获取库存锁超时");
                }
                
                logger.debug("获取库存锁: inventoryId={}", inventoryId);
                
                // 重新获取库存信息（加锁后）
                inventory = getInventoryById(inventoryId);
                if (inventory == null) {
                    throw new InventoryException("库存不存在");
                }
                
                // 执行库存增加
                inventoryMapper.updateQuantity(
                    inventoryId,
                    inventory.getQuantity() + quantity,
                    inventory.getLockedQuantity(),
                    inventory.getAvailableQuantity() + quantity
                );
                
                logger.info("增加库存成功: inventoryId={}, quantity={}, newQuantity={}, newAvailableQuantity={}",
                    inventoryId, quantity, inventory.getQuantity() + quantity, inventory.getAvailableQuantity() + quantity);
            } finally {
                if (inventoryLockAcquired) {
                    inventoryLock.unlock();
                    logger.debug("释放库存锁: inventoryId={}", inventoryId);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InventoryException("获取锁被中断");
        } finally {
            if (skuLockAcquired) {
                skuLock.unlock();
                logger.debug("释放SKU锁: skuId={}", inventory.getSkuId());
            }
        }
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
        if ("NORMAL" == inventory.getStatus()) {
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
        // 1. 参数验证
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
        
        // 2. 查找或创建库存记录
        List<InventoryEntity> inventories = inventoryMapper.findBySkuId(skuId);
        InventoryEntity inventory;
        
        if (inventories.isEmpty()) {
            // 创建新的库存记录
            inventory = new InventoryEntity();
            inventory.setSkuId(skuId);
            inventory.setCreatedAt(LocalDateTime.now());
        } else {
            // 使用现有的库存记录
            inventory = inventories.get(0);
        }
        
        // 3. 更新库存信息
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
        
        // 4. 保存更新
        if (inventory.getId() == null) {
            inventoryMapper.insertInventory(inventory);
        } else {
            inventoryMapper.updateInventory(inventory);
        }
        
        // 5. 记录操作日志
        logger.info("更新SKU库存: skuId={}, quantity={}, lockedQuantity={}, availableQuantity={}, " +
                   "warningQuantity={}, location={}, batchNumber={}, operator={}, remark={}",
            skuId, quantity, lockedQuantity, availableQuantity, warningQuantity,
            location, batchNumber, operator, remark);
            
        // 6. 检查是否需要发送预警
        if (availableQuantity <= warningQuantity) {
            logger.warn("SKU库存预警: skuId={}, availableQuantity={}, warningQuantity={}",
                skuId, availableQuantity, warningQuantity);
        }
        
        // 7. 检查是否过期
        if (expiryDate != null && expiryDate.isBefore(LocalDateTime.now())) {
            logger.warn("SKU库存过期: skuId={}, expiryDate={}", skuId, expiryDate);
        }
    }
} 