package com.example.onlinestore.exception;

public class InventoryException extends BusinessException {
    public InventoryException(String message) {
        super("INVENTORY_ERROR", message);
    }
}
