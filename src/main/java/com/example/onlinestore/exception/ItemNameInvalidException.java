package com.example.onlinestore.exception;

public class ItemNameInvalidException extends BusinessException {
    public ItemNameInvalidException(String message) {
        super("ITEM_NAME_INVALID", message);
    }
}
