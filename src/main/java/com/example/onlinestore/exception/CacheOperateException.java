package com.example.onlinestore.exception;


/**
 * CacheOperateException 是一个自定义异常类，继承自 Exception。
 * 该异常类通常用于在缓存操作过程中发生错误时抛出，以便调用者能够捕获并处理这些异常。
 */
public class CacheOperateException extends Exception{

    public CacheOperateException(String message) {
        super(message);
    }
    public CacheOperateException(String message, Throwable cause) {
        super(message, cause);
    }
    public CacheOperateException(Throwable cause) {
        super(cause);
    }
}
