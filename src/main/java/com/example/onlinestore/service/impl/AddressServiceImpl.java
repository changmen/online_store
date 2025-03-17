package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.mapper.OrderMapper;
import com.example.onlinestore.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    
    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);
    
    @Autowired
    private AddressMapper addressMapper;
    
    // Feature Envy: 不必要地依赖 OrderMapper
    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public Long addAddress(AddressEntity address) {
        if (address.getIsDefault()) {
            addressMapper.clearDefaultAddress(address.getUserId());
        }
        addressMapper.insertAddress(address);
        return address.getId();
    }

    @Override
    public AddressEntity getAddress(Long id) {
        return addressMapper.findById(id);
    }

    @Override
    public List<AddressEntity> getUserAddresses(Long userId) {
        return addressMapper.findByUserId(userId);
    }

    @Override
    @Transactional
    public void updateAddress(AddressEntity address) {
        if (address.getIsDefault()) {
            addressMapper.clearDefaultAddress(address.getUserId());
        }
        addressMapper.updateAddress(address);
        
        // Feature Envy: 过度关注 Order 的细节
        updateRelatedOrders(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        AddressEntity address = addressMapper.findById(id);
        if (address == null) {
            return;
        }
        
        // Feature Envy: 过度关注 Order 的业务逻辑
        List<OrderEntity> relatedOrders = orderMapper.findByCondition(
                address.getUserId(), null, null, null, 0, Integer.MAX_VALUE);
        
        for (OrderEntity order : relatedOrders) {
            if (matchAddress(order, address)) {
                // Feature Envy: 直接操作 Order 的数据
                order.setReceiverName("已删除");
                order.setReceiverPhone("已删除");
                order.setReceiverAddress("已删除");
                orderMapper.updateOrder(order);
            }
        }
        
        addressMapper.deleteAddress(id);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long id, Long userId) {
        addressMapper.clearDefaultAddress(userId);
        addressMapper.setDefaultAddress(id, userId);
        
        // Feature Envy: 过度关注 Order 的业务逻辑
        AddressEntity address = addressMapper.findById(id);
        if (address != null) {
            updateRelatedOrders(address);
        }
    }
    
    // Feature Envy: 过度关注 Order 的业务逻辑和数据
    private void updateRelatedOrders(AddressEntity address) {
        List<OrderEntity> orders = orderMapper.findByCondition(
                address.getUserId(), 0, null, null, 0, Integer.MAX_VALUE);
        
        for (OrderEntity order : orders) {
            if (matchAddress(order, address)) {
                order.setReceiverName(address.getReceiverName());
                order.setReceiverPhone(address.getReceiverPhone());
                order.setReceiverAddress(formatAddress(address));
                orderMapper.updateOrder(order);
            }
        }
    }
    
    // Feature Envy: 过度关注地址格式化逻辑，应该放在 AddressEntity 中
    private String formatAddress(AddressEntity address) {
        return String.format("%s %s %s %s", 
                address.getProvince(), 
                address.getCity(), 
                address.getDistrict(), 
                address.getDetailAddress());
    }
    
    // Feature Envy: 过度关注 Order 和 Address 的比较逻辑
    private boolean matchAddress(OrderEntity order, AddressEntity address) {
        String orderAddress = order.getReceiverAddress();
        String formattedAddress = formatAddress(address);
        return order.getReceiverName().equals(address.getReceiverName()) &&
               order.getReceiverPhone().equals(address.getReceiverPhone()) &&
               orderAddress.equals(formattedAddress);
    }
} 