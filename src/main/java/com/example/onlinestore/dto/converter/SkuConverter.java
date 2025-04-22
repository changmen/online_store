package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.SkuResponse;
import org.springframework.stereotype.Component;

@Component
public class SkuConverter {
    public SkuResponse convert(Sku sku) {
        if (sku == null) {
            return null;
        }
        SkuResponse skuResponse = new SkuResponse();
        skuResponse.setId(sku.getId());
        skuResponse.setItemId(sku.getItemId());
        skuResponse.setSkuCode(sku.getSkuCode());
        skuResponse.setName(sku.getName());
        skuResponse.setDescription(sku.getDescription());
        skuResponse.setPrice(sku.getPrice());
        skuResponse.setDefaultSku(sku.getDefaultSku());
        skuResponse.setStockQuantity(sku.getStockQuantity());
        skuResponse.setSoldQuantity(sku.getSoldQuantity());
        skuResponse.setWarningQuantity(sku.getWarningQuantity());
        skuResponse.setImage(sku.getImage());
        skuResponse.setAttributes(sku.getAttributes());
        return skuResponse;
    }
}
