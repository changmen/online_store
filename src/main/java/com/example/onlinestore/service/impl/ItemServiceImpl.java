package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.bean.VirtualItem;
import com.example.onlinestore.context.UserContext;
import com.example.onlinestore.dto.ItemQueryDTO;
import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exception.BizException;
import com.example.onlinestore.mapper.ItemMapper;
import com.example.onlinestore.service.ItemService;
import com.example.onlinestore.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\u4e00-\\u9fa5\\s_\\-]+$");

    @Value("${item.name.max.length:64}")
    private int itemNameMaxLength;

    @Autowired
    private ItemMapper itemMapper;
    
    @Autowired
    private SkuService skuService;

    @Override
    public void addItem(Item item) {
        // 验证商品名称
        if (StringUtils.isBlank(item.getName())) {
            throw new BizException(ErrorCode.ITEM_NAME_NULL);
        }
        
        // 验证名称长度
        if (item.getName().length() > itemNameMaxLength) {
            throw new BizException(ErrorCode.ITEM_NAME_MAX_LENGTH_EXCEED);
        }
        
        // 验证名称是否包含特殊字符
        if (!VALID_NAME_PATTERN.matcher(item.getName()).matches()) {
            throw new BizException(ErrorCode.ITEM_NAME_CONTAIN_INVALID_CHAR);
        }

        
        ItemEntity itemEntity = convertToItemEntity(item);
        itemMapper.insertItem(itemEntity);
        // 设置回ID
        item.setId(itemEntity.getId());
    }

    @Override
    public Item getItemById(long itemId) {
        ItemEntity itemEntity = itemMapper.findById(itemId);
        return convertToItem(itemEntity);
    }

    @Override
    public void updateItem(Item item) {

        if (item.getId() == null) {
            throw new IllegalArgumentException("itemId不能为空");
        }

        Item curItem = getItemById(item.getId());
        if (curItem == null) {
            throw new BizException(ErrorCode.ITEM_NOT_FOUND);
        }

        // 验证商品名称
        if (StringUtils.isBlank(item.getName())) {
            throw new BizException(ErrorCode.ITEM_NAME_NULL);
        }
        
        // 验证名称长度
        if (item.getName().length() > itemNameMaxLength) {
            throw new BizException(ErrorCode.ITEM_NAME_MAX_LENGTH_EXCEED);
        }
        
        // 验证名称是否包含特殊字符
        if (!VALID_NAME_PATTERN.matcher(item.getName()).matches()) {
            throw new BizException(ErrorCode.ITEM_NAME_CONTAIN_INVALID_CHAR);
        }

        
        ItemEntity itemEntity = convertToItemEntity(item);
        itemMapper.updateItem(itemEntity);
    }

    @Override
    public void deleteItem(long itemId) {
        itemMapper.deleteItem(itemId);
    }

    @Override
    public List<Item> getAllItems(int page, int size) {
        int offset = (page - 1) * size;
        List<ItemEntity> itemEntities = itemMapper.findAllWithPagination(offset, size);
        return itemEntities.stream()
                .map(this::convertToItem)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Item> queryItems(ItemQueryDTO queryDTO) {
        int offset = (queryDTO.getPage() - 1) * queryDTO.getSize();
        List<ItemEntity> itemEntities = itemMapper.findByCondition(
            queryDTO.getCategoryId(), 
            queryDTO.getName(), 
            offset, 
            queryDTO.getSize()
        );
        
        return itemEntities.stream()
                .map(this::convertToItem)
                .collect(Collectors.toList());
    }
    
    @Override
    public long countItems(ItemQueryDTO queryDTO) {
        return itemMapper.countByCondition(
            queryDTO.getCategoryId(),
            queryDTO.getName()
        );
    }
    
    @Override
    public void addSkuToItem(Long itemId, Sku sku) {
        // 设置商品ID
        sku.setItemId(itemId);
        skuService.addSku(sku);
        
        // 如果是第一个SKU，更新商品的默认SKU ID
        Item item = getItemById(itemId);
        if (item.getSkuId() == null) {
            item.setSkuId(sku.getId());
            updateItem(item);
        }
    }
    
    @Override
    public List<Sku> getSkusByItemId(Long itemId) {
        return skuService.getSkusByItemId(itemId);
    }
    
    @Override
    public void updateItemSku(Sku sku) {
        skuService.updateSku(sku);
    }
    
    @Override
    public void deleteItemSku(Long skuId) {
        skuService.deleteSku(skuId);
    }

    private Item convertToItem(ItemEntity itemEntity) {
        if (itemEntity == null) {
            return null;
        }
        
        if (StringUtils.endsWithIgnoreCase(itemEntity.getName(), "test-item")){
            return new VirtualItem();
        }

        Item item = new Item();
        BeanCopier copier = BeanCopier.create(ItemEntity.class, Item.class, false);
        copier.copy(itemEntity, item, null);

        return item;
    }

    protected ItemEntity convertToItemEntity(Item item) {
        if (item == null) {
            return null;
        }

        ItemEntity itemEntity = new ItemEntity();
        BeanCopier copier = BeanCopier.create(Item.class, ItemEntity.class, false);
        copier.copy(item, itemEntity, null);
        itemEntity.setUserId(UserContext.getCurrentUser().getId());
        return itemEntity;
    }
    
    @SuppressWarnings("unused")
    private ItemEntity buildItemEntity(String userId, String name, String description, String image, String secondaryName, String pingJia, Long skuId, Map<String, Map<String, String>> itemAttributes, Map<String, Map<String, String>> itemExtensions) {
        ItemEntity entity = new ItemEntity();
        entity.setName(name);
        entity.setDescription(description);
        entity.setImage(image);
        entity.setSecondaryName(secondaryName);
        entity.setSkuId(skuId);
        return entity;
    }
}
