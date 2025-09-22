package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.dto.CreateAddressRequest;
import com.example.onlinestore.dto.UpdateAddressRequest;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 地址服务实现类
 */
@Service
public class AddressServiceImpl implements AddressService {
    
    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);
    
    // 用户地址数量限制
    private static final int MAX_ADDRESS_COUNT = 20;
    
    @Autowired
    private AddressMapper addressMapper;
    
    @Override
    @Transactional
    public Address createAddress(@Valid CreateAddressRequest request, @NotNull Long memberId) {
        // 检查用户地址数量是否超过限制
        int addressCount = addressMapper.countByMemberId(memberId);
        if (addressCount >= MAX_ADDRESS_COUNT) {
            logger.warn("Member {} exceeded maximum address count: {}", memberId, addressCount);
            throw new BizException(ErrorCode.ADDRESS_LIMIT_EXCEEDED);
        }
        
        LocalDateTime now = LocalDateTime.now();
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setMemberId(memberId);
        addressEntity.setReceiverName(request.getReceiverName());
        addressEntity.setReceiverPhone(request.getReceiverPhone());
        addressEntity.setProvince(request.getProvince());
        addressEntity.setCity(request.getCity());
        addressEntity.setDistrict(request.getDistrict());
        addressEntity.setDetailAddress(request.getDetailAddress());
        addressEntity.setZipCode(request.getZipCode());
        addressEntity.setIsDefault(request.getIsDefault() != null && request.getIsDefault() ? 1 : 0);
        addressEntity.setCreatedAt(now);
        addressEntity.setUpdatedAt(now);
        
        // 如果设置为默认地址，先清除该用户的其他默认地址
        if (addressEntity.getIsDefault() == 1) {
            addressMapper.clearDefaultByMemberId(memberId);
        }
        
        int effectRows = addressMapper.insertAddress(addressEntity);
        if (effectRows != 1) {
            logger.error("Insert address failed for member: {}", memberId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        logger.info("Address created successfully for member: {}, addressId: {}", memberId, addressEntity.getId());
        return addressEntity.toAddress();
    }
    
    @Override
    @Transactional
    public Address updateAddress(@Valid UpdateAddressRequest request, @NotNull Long memberId) {
        // 验证地址是否存在且属于该用户
        AddressEntity existingAddress = addressMapper.findById(request.getId());
        if (existingAddress == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, request.getId());
        }
        
        if (!existingAddress.getMemberId().equals(memberId)) {
            throw new BizException(ErrorCode.ADDRESS_ACCESS_DENIED);
        }
        
        // 如果设置为默认地址，先清除该用户的其他默认地址
        if (request.getIsDefault() != null && request.getIsDefault()) {
            addressMapper.clearDefaultByMemberId(memberId);
        }
        
        LocalDateTime now = LocalDateTime.now();
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(request.getId());
        addressEntity.setMemberId(memberId);
        addressEntity.setReceiverName(request.getReceiverName());
        addressEntity.setReceiverPhone(request.getReceiverPhone());
        addressEntity.setProvince(request.getProvince());
        addressEntity.setCity(request.getCity());
        addressEntity.setDistrict(request.getDistrict());
        addressEntity.setDetailAddress(request.getDetailAddress());
        addressEntity.setZipCode(request.getZipCode());
        addressEntity.setIsDefault(request.getIsDefault() != null && request.getIsDefault() ? 1 : 0);
        addressEntity.setUpdatedAt(now);
        
        int effectRows = addressMapper.updateAddress(addressEntity);
        if (effectRows != 1) {
            logger.error("Update address failed for member: {}, addressId: {}", memberId, request.getId());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        logger.info("Address updated successfully for member: {}, addressId: {}", memberId, request.getId());
        return addressMapper.findById(request.getId()).toAddress();
    }
    
    @Override
    @Transactional
    public void deleteAddress(@NotNull Long addressId, @NotNull Long memberId) {
        // 验证地址是否存在且属于该用户
        AddressEntity existingAddress = addressMapper.findById(addressId);
        if (existingAddress == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, addressId);
        }
        
        if (!existingAddress.getMemberId().equals(memberId)) {
            throw new BizException(ErrorCode.ADDRESS_ACCESS_DENIED);
        }
        
        int effectRows = addressMapper.deleteById(addressId);
        if (effectRows != 1) {
            logger.error("Delete address failed for member: {}, addressId: {}", memberId, addressId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        logger.info("Address deleted successfully for member: {}, addressId: {}", memberId, addressId);
    }
    
    @Override
    public Address getAddressById(@NotNull Long addressId, @NotNull Long memberId) {
        AddressEntity addressEntity = addressMapper.findById(addressId);
        if (addressEntity == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, addressId);
        }
        
        if (!addressEntity.getMemberId().equals(memberId)) {
            throw new BizException(ErrorCode.ADDRESS_ACCESS_DENIED);
        }
        
        return addressEntity.toAddress();
    }
    
    @Override
    public List<Address> getAddressesByMemberId(@NotNull Long memberId) {
        List<AddressEntity> addressEntities = addressMapper.findByMemberId(memberId);
        return addressEntities.stream()
                .map(AddressEntity::toAddress)
                .collect(Collectors.toList());
    }
    
    @Override
    public Address getDefaultAddress(@NotNull Long memberId) {
        AddressEntity defaultAddress = addressMapper.findDefaultByMemberId(memberId);
        return defaultAddress != null ? defaultAddress.toAddress() : null;
    }
    
    @Override
    @Transactional
    public void setDefaultAddress(@NotNull Long addressId, @NotNull Long memberId) {
        // 验证地址是否存在且属于该用户
        AddressEntity existingAddress = addressMapper.findById(addressId);
        if (existingAddress == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, addressId);
        }
        
        if (!existingAddress.getMemberId().equals(memberId)) {
            throw new BizException(ErrorCode.ADDRESS_ACCESS_DENIED);
        }
        
        // 先清除该用户的所有默认地址
        addressMapper.clearDefaultByMemberId(memberId);
        
        // 设置新的默认地址
        int effectRows = addressMapper.setDefaultAddress(addressId, memberId);
        if (effectRows != 1) {
            logger.error("Set default address failed for member: {}, addressId: {}", memberId, addressId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        logger.info("Default address set successfully for member: {}, addressId: {}", memberId, addressId);
    }
}