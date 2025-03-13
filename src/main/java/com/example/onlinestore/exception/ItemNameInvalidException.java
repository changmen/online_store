package com.example.onlinestore.exception;

/**
 * 商品名称无效异常
 */
public class ItemNameInvalidException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public ItemNameInvalidException(String message) {
        super(message);
    }
} 