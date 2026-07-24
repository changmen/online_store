package com.example.onlinestore.dto.converter;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.ItemResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Collections;

@Mapper(componentModel = "spring")
public interface ItemResponseConverter {

    ItemResponse convert(Item item);

    @AfterMapping
    default void defaultAttributes(Item item, @MappingTarget ItemResponse response) {
        if (response.getAttributes() == null) {
            response.setAttributes(Collections.emptyList());
        }
    }
}
