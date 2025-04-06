package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.AttributeResponse;
import com.example.onlinestore.dto.ItemResponse;
import com.example.onlinestore.service.AttributeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemResponseConverter {

    @Autowired
    private AttributeService attributeService;

    public ItemResponse convert(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item must not be null");
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
            response.setAttributes(item.getAttributes().stream().map(attribute -> {
                return AttributeResponse.of(attributeService.getAttributeByIdWithValues(attribute.getId()));
            }).toList());
        }
        return response;
    }

}
