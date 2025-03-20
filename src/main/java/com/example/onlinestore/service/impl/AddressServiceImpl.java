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
        
        updateRelatedOrders(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        AddressEntity address = addressMapper.findById(id);
        if (address == null) {
            return;
        }
        
        List<OrderEntity> relatedOrders = orderMapper.findByCondition(
                address.getUserId(), null, null, null, 0, Integer.MAX_VALUE);
        
        for (OrderEntity order : relatedOrders) {
            if (matchAddress(order, address)) {
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
        
        AddressEntity address = addressMapper.findById(id);
        if (address != null) {
            updateRelatedOrders(address);
        }
    }
    
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
    
    private String formatAddress(AddressEntity address) {
        return String.format("%s %s %s %s", 
                address.getProvince(), 
                address.getCity(), 
                address.getDistrict(), 
                address.getDetailAddress());
    }
    
    private boolean matchAddress(OrderEntity order, AddressEntity address) {
        String orderAddress = order.getReceiverAddress();
        String formattedAddress = formatAddress(address);
        return order.getReceiverName().equals(address.getReceiverName()) &&
               order.getReceiverPhone().equals(address.getReceiverPhone()) &&
               orderAddress.equals(formattedAddress);
    }
} 