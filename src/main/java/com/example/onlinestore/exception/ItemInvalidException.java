package com.example.onlinestore.exception;

import java.io.Serial;

public class ItemInvalidException extends BusinessException {
    @Serial
    private static final long serialVersionUID = -1010068544071911675L;

    public ItemInvalidException(String message) {
        super("ITEM_INVALID", message);
    }
}
