package com.example.onlinestore.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class OrderNoGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private final AtomicLong sequence = new AtomicLong(0);

    public String generate() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        long seq = sequence.incrementAndGet() % 10000;
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return timestamp + String.format("%04d", seq) + random;
    }
}
