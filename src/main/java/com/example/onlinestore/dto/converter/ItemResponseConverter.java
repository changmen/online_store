package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.ItemResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

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
        if (CollectionUtils.isNotEmpty(item.getAttributes())){
            response.setAttributes(item.getAttributes());
        }
        return response;
    }

}
