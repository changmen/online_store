package com.example.onlinestore.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderNoGeneratorTest {

    private OrderNoGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new OrderNoGenerator();
    }

    @Test
    void generate_shouldReturnValidOrderNo() {
        String orderNo = generator.generate();
        assertNotNull(orderNo);
        assertEquals(25, orderNo.length());
        assertTrue(orderNo.matches("\\d{25}"));
    }

    @Test
    void generate_shouldBeUniqueInSingleThread() {
        String no1 = generator.generate();
        String no2 = generator.generate();
        assertNotEquals(no1, no2);
    }

    @Test
    void generate_shouldBeUniqueInMultiThread() throws InterruptedException {
        int threadCount = 100;
        int iterationsPerThread = 100;
        var orderNos = java.util.concurrent.ConcurrentHashMap.<String>newKeySet();

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < iterationsPerThread; j++) {
                    orderNos.add(generator.generate());
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }

        assertEquals(threadCount * iterationsPerThread, orderNos.size(),
                "All generated order numbers should be unique");
    }
}
