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

    // 每个用户最多可以保存的地址数量
    private static final int MAX_ADDRESS_COUNT = 20;

    @Autowired
    private AddressMapper addressMapper;

    @Override
    @Transactional
    public Address createAddress(@NotNull Long memberId, @Valid CreateAddressRequest request) {
        logger.info("Creating address for member: {}", memberId);

        // 检查地址数量限制
        int addressCount = addressMapper.countByMemberId(memberId);
        if (addressCount >= MAX_ADDRESS_COUNT) {
            logger.warn("Member {} has reached maximum address limit: {}", memberId, MAX_ADDRESS_COUNT);
            throw new BizException(ErrorCode.ADDRESS_COUNT_LIMIT_EXCEEDED);
        }

        LocalDateTime now = LocalDateTime.now();
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setMemberId(memberId);
        addressEntity.setConsigneeName(request.getConsigneeName());
        addressEntity.setConsigneePhone(request.getConsigneePhone());
        addressEntity.setProvince(request.getProvince());
        addressEntity.setCity(request.getCity());
        addressEntity.setDistrict(request.getDistrict());
        addressEntity.setDetailAddress(request.getDetailAddress());
        addressEntity.setPostalCode(request.getPostalCode());
        addressEntity.setIsDefault(request.getIsDefault() ? 1 : 0);
        addressEntity.setTag(request.getTag());
        addressEntity.setCreatedAt(now);
        addressEntity.setUpdatedAt(now);

        // 如果设置为默认地址，先清除该用户的其他默认地址
        if (request.getIsDefault()) {
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
    public Address updateAddress(@NotNull Long memberId, @Valid UpdateAddressRequest request) {
        logger.info("Updating address: {} for member: {}", request.getId(), memberId);

        // 检查地址是否存在且属于该用户
        AddressEntity existingAddress = addressMapper.findById(request.getId());
        if (existingAddress == null || !existingAddress.getMemberId().equals(memberId)) {
            logger.warn("Address not found or access denied: {} for member: {}", request.getId(), memberId);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        LocalDateTime now = LocalDateTime.now();
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(request.getId());
        addressEntity.setMemberId(memberId);
        addressEntity.setConsigneeName(request.getConsigneeName());
        addressEntity.setConsigneePhone(request.getConsigneePhone());
        addressEntity.setProvince(request.getProvince());
        addressEntity.setCity(request.getCity());
        addressEntity.setDistrict(request.getDistrict());
        addressEntity.setDetailAddress(request.getDetailAddress());
        addressEntity.setPostalCode(request.getPostalCode());
        addressEntity.setIsDefault(request.getIsDefault() ? 1 : 0);
        addressEntity.setTag(request.getTag());
        addressEntity.setUpdatedAt(now);

        // 如果设置为默认地址，先清除该用户的其他默认地址
        if (request.getIsDefault()) {
            addressMapper.clearDefaultByMemberId(memberId);
        }

        int effectRows = addressMapper.updateAddress(addressEntity);
        if (effectRows != 1) {
            logger.error("Update address failed: {} for member: {}", request.getId(), memberId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        logger.info("Address updated successfully: {} for member: {}", request.getId(), memberId);
        return addressEntity.toAddress();
    }

    @Override
    public Address getAddressById(@NotNull Long id) {
        logger.debug("Getting address by id: {}", id);
        AddressEntity addressEntity = addressMapper.findById(id);
        if (addressEntity == null) {
            logger.warn("Address not found: {}", id);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        return addressEntity.toAddress();
    }

    @Override
    public Address getAddressByIdAndMemberId(@NotNull Long id, @NotNull Long memberId) {
        logger.debug("Getting address by id: {} and memberId: {}", id, memberId);
        AddressEntity addressEntity = addressMapper.findById(id);
        if (addressEntity == null || !addressEntity.getMemberId().equals(memberId)) {
            logger.warn("Address not found or access denied: {} for member: {}", id, memberId);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        return addressEntity.toAddress();
    }

    @Override
    public List<Address> getAddressesByMemberId(@NotNull Long memberId) {
        logger.debug("Getting addresses for member: {}", memberId);
        List<AddressEntity> addressEntities = addressMapper.findByMemberId(memberId);
        return addressEntities.stream()
                .map(AddressEntity::toAddress)
                .collect(Collectors.toList());
    }

    @Override
    public Address getDefaultAddressByMemberId(@NotNull Long memberId) {
        logger.debug("Getting default address for member: {}", memberId);
        AddressEntity addressEntity = addressMapper.findDefaultByMemberId(memberId);
        if (addressEntity == null) {
            logger.info("No default address found for member: {}", memberId);
            return null;
        }
        return addressEntity.toAddress();
    }

    @Override
    @Transactional
    public void setDefaultAddress(@NotNull Long id, @NotNull Long memberId) {
        logger.info("Setting default address: {} for member: {}", id, memberId);

        // 检查地址是否存在且属于该用户
        if (!validateAddressOwnership(id, memberId)) {
            logger.warn("Address not found or access denied: {} for member: {}", id, memberId);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        // 先清除该用户的其他默认地址
        addressMapper.clearDefaultByMemberId(memberId);

        // 设置新的默认地址
        int effectRows = addressMapper.setDefaultAddress(id, memberId);
        if (effectRows != 1) {
            logger.error("Set default address failed: {} for member: {}", id, memberId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        logger.info("Default address set successfully: {} for member: {}", id, memberId);
    }

    @Override
    @Transactional
    public void deleteAddress(@NotNull Long id, @NotNull Long memberId) {
        logger.info("Deleting address: {} for member: {}", id, memberId);

        int effectRows = addressMapper.deleteByIdAndMemberId(id, memberId);
        if (effectRows != 1) {
            logger.warn("Address not found or access denied: {} for member: {}", id, memberId);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        logger.info("Address deleted successfully: {} for member: {}", id, memberId);
    }

    @Override
    public boolean validateAddressOwnership(@NotNull Long addressId, @NotNull Long memberId) {
        logger.debug("Validating address ownership: {} for member: {}", addressId, memberId);
        AddressEntity addressEntity = addressMapper.findById(addressId);
        return addressEntity != null && addressEntity.getMemberId().equals(memberId);
    }

    @Override
    public int getAddressCountByMemberId(@NotNull Long memberId) {
        logger.debug("Getting address count for member: {}", memberId);
        return addressMapper.countByMemberId(memberId);
    }
}