package com.example.onlinestore.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageTest {

    @Test
    void testDefaultConstructor() {
        Page<String> page = new Page<>();
        assertNotNull(page);
        assertNull(page.getItems());
        assertEquals(0, page.getTotalCount());
        assertEquals(0, page.getPageNum());
        assertEquals(0, page.getPageSize());
    }

    @Test
    void testParameterizedConstructor() {
        List<String> items = Arrays.asList("item1", "item2", "item3");
        Page<String> page = new Page<>(items, 100, 2, 10);

        assertEquals(items, page.getItems());
        assertEquals(100, page.getTotalCount());
        assertEquals(2, page.getPageNum());
        assertEquals(10, page.getPageSize());
    }

    @Test
    void testOfWithValidItems() {
        List<String> items = Arrays.asList("a", "b", "c");
        Page<String> page = Page.of(items, 50, 1, 20);

        assertEquals(items, page.getItems());
        assertEquals(50, page.getTotalCount());
        assertEquals(1, page.getPageNum());
        assertEquals(20, page.getPageSize());
    }

    @Test
    void testOfWithNullItems() {
        Page<String> page = Page.of(null, 30, 3, 15);

        assertNotNull(page.getItems());
        assertEquals(Collections.emptyList(), page.getItems());
        assertEquals(30, page.getTotalCount());
        assertEquals(3, page.getPageNum());
        assertEquals(15, page.getPageSize());
    }

    @Test
    void testOfWithEmptyList() {
        List<String> emptyItems = Collections.emptyList();
        Page<String> page = Page.of(emptyItems, 0, 1, 10);

        assertEquals(emptyItems, page.getItems());
        assertEquals(0, page.getTotalCount());
        assertEquals(1, page.getPageNum());
        assertEquals(10, page.getPageSize());
    }

    @Test
    void testSetters() {
        Page<String> page = new Page<>();

        List<String> items = Arrays.asList("x", "y");
        page.setItems(items);
        page.setTotalCount(200);
        page.setPageNum(5);
        page.setPageSize(25);

        assertEquals(items, page.getItems());
        assertEquals(200, page.getTotalCount());
        assertEquals(5, page.getPageNum());
        assertEquals(25, page.getPageSize());
    }

    @Test
    void testEqualsAndHashCode() {
        List<String> items = Arrays.asList("a", "b");

        Page<String> page1 = new Page<>(items, 10, 1, 5);
        Page<String> page2 = new Page<>(items, 10, 1, 5);
        Page<String> page3 = new Page<>(items, 20, 1, 5);

        assertEquals(page1, page2);
        assertEquals(page1.hashCode(), page2.hashCode());
        assertNotEquals(page1, page3);
    }

    @Test
    void testToString() {
        List<String> items = Arrays.asList("test1", "test2");
        Page<String> page = new Page<>(items, 10, 1, 5);

        String toString = page.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("items"));
        assertTrue(toString.contains("totalCount"));
        assertTrue(toString.contains("pageNum"));
        assertTrue(toString.contains("pageSize"));
    }

    @Test
    void testWithDifferentTypes() {
        List<Integer> intItems = Arrays.asList(1, 2, 3);
        Page<Integer> intPage = Page.of(intItems, 100, 1, 10);
        assertEquals(intItems, intPage.getItems());

        List<Long> longItems = Arrays.asList(1L, 2L, 3L);
        Page<Long> longPage = Page.of(longItems, 200, 2, 20);
        assertEquals(longItems, longPage.getItems());
    }
}
