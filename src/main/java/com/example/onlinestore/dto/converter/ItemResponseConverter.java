package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.ItemResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ItemResponseConverter {

    public ItemResponse convert(Item item) {
        if (item == null) {
            return null;
        }

        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setMainImageURL(item.getMainImageURL());
        response.setSubImageURLs(item.getSubImageURLs());
        response.setCategoryId(item.getCategoryId());
        response.setBrandId(item.getBrandId());
        if (item.getStatus() != null) {
            response.setStatus(item.getStatus().name());
        }
        response.setSortScore(item.getSortScore());
        response.setAttributes(item.getAttributes() != null ? item.getAttributes() : Collections.emptyList());
        return response;
    }

}
