package com.example.onlinestore.utils;

import java.text.SimpleDateFormat;

public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String getCurrentDate() {
        return DATE_FORMAT.format(System.currentTimeMillis());
    }
}
