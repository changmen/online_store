package com.example.onlinestore.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class TimestampOrderNoGenerator implements OrderNoGenerator {
    private final AtomicLong sequence = new AtomicLong(0);
    private static final int MAX_SEQUENCE = 9999;
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Override
    public String generateOrderNo() {
        long currentTimestamp = System.currentTimeMillis();
        long seqValue = sequence.getAndUpdate(prev -> (prev >= MAX_SEQUENCE) ? 0 : prev + 1);

        String timestampPart = Instant.ofEpochMilli(currentTimestamp)
                .atZone(ZoneId.of("Asia/Shanghai"))
                .format(formatter);

        return String.format("%s%04d",
                timestampPart,
                seqValue);
    }
}
