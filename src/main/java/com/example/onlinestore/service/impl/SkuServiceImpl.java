package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.entity.SkuEntity;
import com.example.onlinestore.mapper.SkuMapper;
import com.example.onlinestore.service.SkuService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkuServiceImpl implements SkuService {

    private final SkuMapper skuMapper;

    public SkuServiceImpl(SkuMapper skuMapper) {
        this.skuMapper = skuMapper;
    }

    @Override
    public void addSku(Sku sku) {
        SkuEntity skuEntity = convertToSkuEntity(sku);
        skuEntity.setCreatedAt(LocalDateTime.now());
        skuEntity.setUpdatedAt(LocalDateTime.now());
        skuMapper.insertSku(skuEntity);
    }

    @Override
    public Sku getSkuById(Long id) {
        SkuEntity skuEntity = skuMapper.findById(id);
        return convertToSku(skuEntity);
    }

    @Override
    public List<Sku> getSkusByItemId(Long itemId) {
        List<SkuEntity> skuEntities = skuMapper.findByItemId(itemId);
        return skuEntities.stream()
                .map(this::convertToSku)
                .collect(Collectors.toList());
    }

    @Override
    public void updateSku(Sku sku) {
        SkuEntity skuEntity = convertToSkuEntity(sku);
        skuEntity.setUpdatedAt(LocalDateTime.now());
        skuMapper.updateSku(skuEntity);
    }

    @Override
    public void deleteSku(Long id) {
        skuMapper.deleteSku(id);
    }

    @Override
    public List<Sku> getSkusByItemIds(List<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            return List.of();
        }
        List<SkuEntity> skuEntities = skuMapper.findByItemIds(itemIds);
        return skuEntities.stream()
                .map(this::convertToSku)
                .collect(Collectors.toList());
    }

    private Sku convertToSku(SkuEntity skuEntity) {
        if (skuEntity == null) {
            return null;
        }
        
        Sku sku = new Sku();
        sku.setId(skuEntity.getId());
        sku.setItemId(skuEntity.getItemId());
        sku.setName(skuEntity.getTitle());
        // 可以根据需要设置其他属性
        
        return sku;
    }

    private SkuEntity convertToSkuEntity(Sku sku) {
        if (sku == null) {
            return null;
        }
        
        SkuEntity skuEntity = new SkuEntity();
        skuEntity.setId(sku.getId());
        skuEntity.setItemId(sku.getItemId());
        skuEntity.setTitle(sku.getName());
        // 可以根据需要设置其他属性
        
        return skuEntity;
    }
} 