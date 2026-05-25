package com.example.onlinestore.exception;

public class OrderException extends BusinessException {
    public OrderException(String message) {
        super("ORDER_ERROR", message);
    }
}
