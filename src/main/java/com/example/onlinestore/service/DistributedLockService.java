package com.example.onlinestore.service;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁服务接口
 */
public interface DistributedLockService {
    
    /**
     * 尝试获取分布式锁
     * 
     * @param lockKey 锁的键
     * @param expireTime 超期时间
     * @param timeUnit 时间单位
     * @return 是否获取成功
     */
    boolean tryLock(String lockKey, long expireTime, TimeUnit timeUnit);
    
    /**
     * 尝试获取分布式锁（带重试机制）
     * 
     * @param lockKey 锁的键
     * @param expireTime 超期时间
     * @param timeUnit 时间单位
     * @param retryTimes 重试次数
     * @param retryInterval 重试间隔（毫秒）
     * @return 是否获取成功
     */
    boolean tryLockWithRetry(String lockKey, long expireTime, TimeUnit timeUnit, int retryTimes, long retryInterval);
    
    /**
     * 释放分布式锁
     *
     * @param lockKey 锁的键
     */
    void releaseLock(String lockKey);
    
    /**
     * 执行需要加锁的操作
     * 
     * @param lockKey 锁的键
     * @param expireTime 超期时间
     * @param timeUnit 时间单位
     * @param operation 需要执行的操作
     * @param <T> 操作返回值类型
     * @return 操作结果
     * @throws Exception 执行过程中的异常
     */
    <T> T executeWithLock(String lockKey, long expireTime, TimeUnit timeUnit, LockOperation<T> operation) throws Exception;
    
    /**
     * 执行需要加锁的操作（带重试机制）
     * 
     * @param lockKey 锁的键
     * @param expireTime 超期时间
     * @param timeUnit 时间单位
     * @param retryTimes 重试次数
     * @param retryInterval 重试间隔（毫秒）
     * @param operation 需要执行的操作
     * @param <T> 操作返回值类型
     * @return 操作结果
     * @throws Exception 执行过程中的异常
     */
    <T> T executeWithLockWithRetry(String lockKey, long expireTime, TimeUnit timeUnit, 
                                  int retryTimes, long retryInterval, LockOperation<T> operation) throws Exception;
    
    /**
     * 锁操作接口
     * 
     * @param <T> 操作返回值类型
     */
    @FunctionalInterface
    interface LockOperation<T> {
        T execute() throws Exception;
    }
} 