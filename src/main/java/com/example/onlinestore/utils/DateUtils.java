package com.example.onlinestore.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String getCurrentDate() {
        return LocalDateTime.now().format(DATE_FORMATTER);
    }
}
