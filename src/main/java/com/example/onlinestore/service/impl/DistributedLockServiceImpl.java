package com.example.onlinestore.service.impl;

import com.example.onlinestore.service.DistributedLockService;
import com.example.onlinestore.utils.RedisDistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁服务实现
 */
@Service
public class DistributedLockServiceImpl implements DistributedLockService {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockServiceImpl.class);
    
    @Autowired
    private RedisDistributedLock redisDistributedLock;
    
    @Override
    public boolean tryLock(String lockKey, long expireTime, TimeUnit timeUnit) {
        return redisDistributedLock.tryLock(lockKey, expireTime, timeUnit);
    }
    
    @Override
    public boolean tryLockWithRetry(String lockKey, long expireTime, TimeUnit timeUnit, int retryTimes, long retryInterval) {
        return redisDistributedLock.tryLockWithRetry(lockKey, expireTime, timeUnit, retryTimes, retryInterval);
    }
    
    @Override
    public void releaseLock(String lockKey) {
        redisDistributedLock.releaseLock(lockKey);
    }
    
    @Override
    public <T> T executeWithLock(String lockKey, long expireTime, TimeUnit timeUnit, LockOperation<T> operation) throws Exception {
        boolean locked = false;
        try {
            locked = tryLock(lockKey, expireTime, timeUnit);
            if (!locked) {
                throw new IllegalStateException("Failed to acquire lock for key: " + lockKey);
            }
            return operation.execute();
        } finally {
            if (locked) {
                releaseLock(lockKey);
            }
        }
    }
    
    @Override
    public <T> T executeWithLockWithRetry(String lockKey, long expireTime, TimeUnit timeUnit, 
                                         int retryTimes, long retryInterval, LockOperation<T> operation) throws Exception {
        boolean locked = false;
        try {
            locked = tryLockWithRetry(lockKey, expireTime, timeUnit, retryTimes, retryInterval);
            if (!locked) {
                throw new IllegalStateException("Failed to acquire lock for key: " + lockKey + " after " + retryTimes + " retries");
            }
            return operation.execute();
        } finally {
            if (locked) {
                releaseLock(lockKey);
            }
        }
    }
} 