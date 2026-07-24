package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.ItemResponse;
import com.example.onlinestore.enums.ItemStatus;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ItemResponseConverterTest {

    private final ItemResponseConverter converter = new ItemResponseConverterImpl();

    @Test
    void convert_nullItem_returnsNull() {
        assertNull(converter.convert(null));
    }

    @Test
    void convert_mapsBasicFields() {
        Item item = new Item();
        item.setId(1L);
        item.setName("测试商品");
        item.setDescription("描述");
        item.setMainImageURL("http://example.com/img.jpg");
        item.setSubImageURLs(Collections.singletonList("http://example.com/sub.jpg"));
        item.setCategoryId(10L);
        item.setBrandId(20L);

        ItemResponse response = converter.convert(item);

        assertEquals(1L, response.getId());
        assertEquals("测试商品", response.getName());
        assertEquals("描述", response.getDescription());
        assertEquals("http://example.com/img.jpg", response.getMainImageURL());
        assertEquals(10L, response.getCategoryId());
        assertEquals(20L, response.getBrandId());
    }

    @Test
    void convert_mapsStatusAndSortScore() {
        Item item = new Item();
        item.setId(1L);
        item.setName("商品");
        item.setStatus(ItemStatus.DRAFT);
        item.setSortScore(5);

        ItemResponse response = converter.convert(item);

        assertEquals("DRAFT", response.getStatus());
        assertEquals(5, response.getSortScore());
    }

    @Test
    void convert_nullStatus_doesNotThrow() {
        Item item = new Item();
        item.setId(1L);
        item.setName("商品");
        item.setStatus(null);
        item.setSortScore(1);

        ItemResponse response = converter.convert(item);

        assertNull(response.getStatus());
        assertEquals(1, response.getSortScore());
    }

    @Test
    void convert_onSaleStatus_mapsCorrectly() {
        Item item = new Item();
        item.setId(2L);
        item.setName("上架商品");
        item.setStatus(ItemStatus.ON_SALE);
        item.setSortScore(10);

        ItemResponse response = converter.convert(item);

        assertEquals("ON_SALE", response.getStatus());
        assertEquals(10, response.getSortScore());
    }
}
