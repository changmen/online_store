package com.example.onlinestore.exception;

public class CartException extends BusinessException {
    public CartException(String message) {
        super("CART_ERROR", message);
    }
}
