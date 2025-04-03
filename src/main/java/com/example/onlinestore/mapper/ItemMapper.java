package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.ItemEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemMapper {
    int insert(ItemEntity itemEntity);
}
