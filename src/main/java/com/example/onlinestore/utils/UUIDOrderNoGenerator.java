package com.example.onlinestore.utils;

import java.util.UUID;

public class UUIDOrderNoGenerator implements OrderNoGenerator {
    @Override
    public String generateOrderNo() {
        return "ORD" + UUID.randomUUID().toString().replaceAll("-", "");
    }
}
