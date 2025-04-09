package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.CartItem;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.CartItemResponse;
import com.example.onlinestore.service.ItemService;
import com.example.onlinestore.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartItemConverter {

    @Autowired
    private ItemResponseConverter itemResponseConverter;

    @Autowired
    private SkuService skuService;

    @Autowired
    private SkuConverter skuConverter;

    @Autowired
    private ItemService itemService;

    public CartItemResponse convert(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setId(cartItem.getId());
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setSelected(cartItem.getSelected());
        Sku sku = skuService.getSkuById(cartItem.getSkuId());
        cartItemResponse.setSku(skuConverter.convert(sku));

        Item item = itemService.getItemById(sku.getItemId());
        cartItemResponse.setItem(itemResponseConverter.convert(item));
        return cartItemResponse;
    }
}
