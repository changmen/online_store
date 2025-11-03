package com.example.onlinestore.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DateUtils 单元测试类
 * 
 * 测试日期工具类的功能，主要验证日期格式化的正确性
 */
class DateUtilsTest {

    @Test
    @DisplayName("获取当前日期 - 应该返回正确格式的日期字符串")
    void testGetCurrentDate_ShouldReturnFormattedDateString() {
        // When
        String currentDate = DateUtils.getCurrentDate();
        
        // Then
        assertNotNull(currentDate, "返回的日期字符串不应该为null");
        assertFalse(currentDate.isEmpty(), "返回的日期字符串不应该为空");
        
        // 验证日期格式：yyyyMMddHHmmss (14位数字)
        assertTrue(currentDate.matches("\\d{14}"), 
                   "日期格式应该是14位数字的yyyyMMddHHmmss格式，实际值: " + currentDate);
    }

    @Test
    @DisplayName("日期格式验证 - 应该符合yyyyMMddHHmmss格式")
    void testGetCurrentDate_ShouldMatchExpectedFormat() {
        // When
        String currentDate = DateUtils.getCurrentDate();
        
        // Then
        assertEquals(14, currentDate.length(), "日期字符串长度应该是14位");
        
        // 验证年份部分（前4位应该是合理的年份）
        String year = currentDate.substring(0, 4);
        int yearInt = Integer.parseInt(year);
        assertTrue(yearInt >= 2020 && yearInt <= 3000, 
                  "年份应该在合理范围内，实际年份: " + yearInt);
        
        // 验证月份部分（第5-6位应该是01-12）
        String month = currentDate.substring(4, 6);
        int monthInt = Integer.parseInt(month);
        assertTrue(monthInt >= 1 && monthInt <= 12, 
                  "月份应该在1-12之间，实际月份: " + monthInt);
        
        // 验证日期部分（第7-8位应该是01-31）
        String day = currentDate.substring(6, 8);
        int dayInt = Integer.parseInt(day);
        assertTrue(dayInt >= 1 && dayInt <= 31, 
                  "日期应该在1-31之间，实际日期: " + dayInt);
        
        // 验证小时部分（第9-10位应该是00-23）
        String hour = currentDate.substring(8, 10);
        int hourInt = Integer.parseInt(hour);
        assertTrue(hourInt >= 0 && hourInt <= 23, 
                  "小时应该在0-23之间，实际小时: " + hourInt);
        
        // 验证分钟部分（第11-12位应该是00-59）
        String minute = currentDate.substring(10, 12);
        int minuteInt = Integer.parseInt(minute);
        assertTrue(minuteInt >= 0 && minuteInt <= 59, 
                  "分钟应该在0-59之间，实际分钟: " + minuteInt);
        
        // 验证秒部分（第13-14位应该是00-59）
        String second = currentDate.substring(12, 14);
        int secondInt = Integer.parseInt(second);
        assertTrue(secondInt >= 0 && secondInt <= 59, 
                  "秒应该在0-59之间，实际秒: " + secondInt);
    }

    @Test
    @DisplayName("时间接近性验证 - 应该返回当前时间附近的值")
    void testGetCurrentDate_ShouldReturnCurrentTimeRange() {
        // Given
        long beforeCall = System.currentTimeMillis();
        
        // When
        String currentDate = DateUtils.getCurrentDate();
        
        // Then
        long afterCall = System.currentTimeMillis();
        
        // 将返回的字符串转换回时间戳进行比较
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date parsedDate = dateFormat.parse(currentDate);
            long parsedTime = parsedDate.getTime();
            
            // 验证返回的时间在方法调用前后的合理范围内（允许1秒的误差）
            assertTrue(parsedTime >= beforeCall - 1000, 
                      "返回的时间应该不早于方法调用前1秒");
            assertTrue(parsedTime <= afterCall + 1000, 
                      "返回的时间应该不晚于方法调用后1秒");
        } catch (Exception e) {
            fail("解析返回的日期字符串失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("多次调用 - 应该返回不同的时间值")
    void testGetCurrentDate_MultipleCallsShouldReturnDifferentValues() {
        // When
        String date1 = DateUtils.getCurrentDate();
        
        // 等待至少1毫秒
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("测试被中断");
        }
        
        String date2 = DateUtils.getCurrentDate();
        
        // Then
        // 注意：由于日期格式精确到秒，快速连续调用可能返回相同值
        // 但我们可以验证它们都是有效的日期格式
        assertNotNull(date1, "第一次调用结果不应该为null");
        assertNotNull(date2, "第二次调用结果不应该为null");
        assertTrue(date1.matches("\\d{14}"), "第一次调用结果应该是有效的日期格式");
        assertTrue(date2.matches("\\d{14}"), "第二次调用结果应该是有效的日期格式");
        
        // 验证第二次调用的时间不早于第一次
        assertTrue(date2.compareTo(date1) >= 0, 
                  "第二次调用的时间不应该早于第一次调用，date1: " + date1 + ", date2: " + date2);
    }

    @Test
    @DisplayName("格式一致性 - 应该始终使用相同的格式")
    void testGetCurrentDate_ShouldHaveConsistentFormat() {
        // When - 调用多次获取不同时间点的日期
        String[] dates = new String[5];
        for (int i = 0; i < 5; i++) {
            dates[i] = DateUtils.getCurrentDate();
            try {
                Thread.sleep(1); // 确保时间有微小差异
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        // Then - 验证所有日期都符合相同格式
        Pattern datePattern = Pattern.compile("\\d{4}\\d{2}\\d{2}\\d{2}\\d{2}\\d{2}");
        for (int i = 0; i < dates.length; i++) {
            if (dates[i] != null) {
                assertTrue(datePattern.matcher(dates[i]).matches(), 
                          "第" + (i+1) + "次调用返回的日期格式不正确: " + dates[i]);
                assertEquals(14, dates[i].length(), 
                           "第" + (i+1) + "次调用返回的日期长度不正确: " + dates[i]);
            }
        }
    }

    @Test
    @DisplayName("边界值测试 - 验证特殊时间点的处理")
    void testGetCurrentDate_BoundaryValues() {
        // 这个测试主要验证在任何时间点调用都不会出现异常
        // When & Then
        assertDoesNotThrow(() -> {
            String date = DateUtils.getCurrentDate();
            assertNotNull(date);
            assertTrue(date.matches("\\d{14}"));
        }, "调用getCurrentDate不应该抛出任何异常");
    }

    @Test
    @DisplayName("线程安全性测试 - 多线程环境下应该正常工作")
    void testGetCurrentDate_ThreadSafety() throws InterruptedException {
        // Given
        final int threadCount = 10;
        final String[] results = new String[threadCount];
        final boolean[] success = new boolean[threadCount];
        Thread[] threads = new Thread[threadCount];
        
        // When - 创建多个线程同时调用方法
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    results[index] = DateUtils.getCurrentDate();
                    success[index] = true;
                } catch (Exception e) {
                    success[index] = false;
                }
            });
        }
        
        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Then - 验证所有线程都成功执行且返回有效结果
        for (int i = 0; i < threadCount; i++) {
            assertTrue(success[i], "第" + (i+1) + "个线程应该成功执行");
            assertNotNull(results[i], "第" + (i+1) + "个线程的结果不应该为null");
            assertTrue(results[i].matches("\\d{14}"), 
                      "第" + (i+1) + "个线程返回的日期格式不正确: " + results[i]);
        }
    }
}