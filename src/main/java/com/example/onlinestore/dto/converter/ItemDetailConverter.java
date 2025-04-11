package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.ItemDetail;
import com.example.onlinestore.dto.ItemDetailResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ItemDetailConverter {

    @Autowired
    private ItemResponseConverter itemResponseConverter;

    @Autowired
    private SkuConverter skuConverter;

    public ItemDetailResponse convert(ItemDetail itemDetail) {
        if (itemDetail == null) {
            return null;
        }
        ItemDetailResponse response = new ItemDetailResponse();
        response.setItem(itemResponseConverter.convert(itemDetail.getItem()));
        if (CollectionUtils.isNotEmpty(itemDetail.getSkus())){
            response.setSkus(itemDetail.getSkus().stream().map(sku -> skuConverter.convert(sku)).collect(Collectors.toList()));
        }
        return response;

    }
}
