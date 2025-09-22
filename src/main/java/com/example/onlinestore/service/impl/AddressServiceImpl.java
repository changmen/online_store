package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
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

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);
    
    private static final int MAX_ADDRESS_COUNT = 20; // 每个用户最多20个地址

    @Autowired
    private AddressMapper addressMapper;

    @Override
    @Transactional
    public Address createAddress(@Valid Address address) {
        // 验证地址数量限制
        if (!canAddMoreAddresses(address.getMemberId())) {
            logger.warn("Member {} has reached maximum address limit", address.getMemberId());
            throw new BizException(ErrorCode.MEMBER_ADDRESS_LIMIT_EXCEEDED);
        }

        // 如果设置为默认地址，先清除其他默认地址
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressMapper.clearDefaultByMemberId(address.getMemberId());
        }

        AddressEntity entity = AddressEntity.fromAddress(address);
        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        int effectRows = addressMapper.insert(entity);
        if (effectRows != 1) {
            logger.error("Failed to insert address for member: {}", address.getMemberId());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        logger.info("Successfully created address with ID: {} for member: {}", entity.getId(), address.getMemberId());
        return entity.toAddress();
    }

    @Override
    @Transactional
    public Address updateAddress(@Valid Address address) {
        // 验证地址是否存在且属于该用户
        AddressEntity existingEntity = addressMapper.selectById(address.getId());
        if (existingEntity == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, address.getId());
        }
        
        if (!existingEntity.getMemberId().equals(address.getMemberId())) {
            logger.warn("Member {} attempted to update address {} belonging to member {}", 
                       address.getMemberId(), address.getId(), existingEntity.getMemberId());
            throw new BizException(ErrorCode.ADDRESS_ACCESS_DENIED);
        }

        // 如果设置为默认地址，先清除其他默认地址
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressMapper.clearDefaultByMemberId(address.getMemberId());
        }

        AddressEntity entity = AddressEntity.fromAddress(address);
        entity.setUpdatedAt(LocalDateTime.now());
        
        int effectRows = addressMapper.update(entity);
        if (effectRows != 1) {
            logger.error("Failed to update address: {} for member: {}", address.getId(), address.getMemberId());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        logger.info("Successfully updated address: {} for member: {}", address.getId(), address.getMemberId());
        return addressMapper.selectById(address.getId()).toAddress();
    }

    @Override
    @Transactional
    public void deleteAddress(@NotNull Long addressId, @NotNull Long memberId) {
        // 验证地址是否存在且属于该用户
        AddressEntity entity = addressMapper.selectById(addressId);
        if (entity == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, addressId);
        }
        
        if (!entity.getMemberId().equals(memberId)) {
            logger.warn("Member {} attempted to delete address {} belonging to member {}", 
                       memberId, addressId, entity.getMemberId());
            throw new BizException(ErrorCode.ADDRESS_ACCESS_DENIED);
        }

        int effectRows = addressMapper.deleteById(addressId);
        if (effectRows != 1) {
            logger.error("Failed to delete address: {} for member: {}", addressId, memberId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        logger.info("Successfully deleted address: {} for member: {}", addressId, memberId);
    }

    @Override
    public Address getAddressById(@NotNull Long addressId, @NotNull Long memberId) {
        AddressEntity entity = addressMapper.selectById(addressId);
        if (entity == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, addressId);
        }
        
        if (!entity.getMemberId().equals(memberId)) {
            logger.warn("Member {} attempted to access address {} belonging to member {}", 
                       memberId, addressId, entity.getMemberId());
            throw new BizException(ErrorCode.ADDRESS_ACCESS_DENIED);
        }

        return entity.toAddress();
    }

    @Override
    public List<Address> getAddressesByMemberId(@NotNull Long memberId) {
        List<AddressEntity> entities = addressMapper.selectByMemberId(memberId);
        return entities.stream()
                      .map(AddressEntity::toAddress)
                      .collect(Collectors.toList());
    }

    @Override
    public Address getDefaultAddress(@NotNull Long memberId) {
        AddressEntity entity = addressMapper.selectDefaultByMemberId(memberId);
        return entity != null ? entity.toAddress() : null;
    }

    @Override
    @Transactional
    public void setDefaultAddress(@NotNull Long addressId, @NotNull Long memberId) {
        // 验证地址是否存在且属于该用户
        AddressEntity entity = addressMapper.selectById(addressId);
        if (entity == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, addressId);
        }
        
        if (!entity.getMemberId().equals(memberId)) {
            logger.warn("Member {} attempted to set default address {} belonging to member {}", 
                       memberId, addressId, entity.getMemberId());
            throw new BizException(ErrorCode.ADDRESS_ACCESS_DENIED);
        }

        // 先清除所有默认地址标记
        addressMapper.clearDefaultByMemberId(memberId);
        
        // 设置指定地址为默认
        int effectRows = addressMapper.setAsDefault(addressId, memberId);
        if (effectRows != 1) {
            logger.error("Failed to set default address: {} for member: {}", addressId, memberId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        logger.info("Successfully set address: {} as default for member: {}", addressId, memberId);
    }

    @Override
    public boolean canAddMoreAddresses(@NotNull Long memberId) {
        int count = addressMapper.countByMemberId(memberId);
        return count < MAX_ADDRESS_COUNT;
    }
}