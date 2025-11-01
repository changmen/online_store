package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.dto.CreateAddressRequest;
import com.example.onlinestore.dto.UpdateAddressRequest;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.service.AddressService;
import com.example.onlinestore.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 地址服务实现类
 */
@Service
@Validated
public class AddressServiceImpl implements AddressService {
    
    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);
    
    /**
     * 用户最大地址数量限制
     */
    private static final int MAX_ADDRESS_COUNT = 20;
    
    @Autowired
    private AddressMapper addressMapper;
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private MessageSource messageSource;
    
    @Override
    @Transactional
    public Address createAddress(@Valid @NotNull CreateAddressRequest request, @NotNull Long memberId) {
        logger.info("Creating address for member: {}, request: {}", memberId, request);
        
        // 验证用户是否存在
        validateMemberExists(memberId);
        
        // 检查地址数量限制
        validateAddressLimit(memberId);
        
        // 如果设置为默认地址，先清除其他默认地址
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressMapper.clearDefaultAddress(memberId);
        }
        
        // 创建地址实体
        AddressEntity addressEntity = createAddressEntity(request, memberId);
        
        // 插入数据库
        int result = addressMapper.insertAddress(addressEntity);
        if (result != 1) {
            logger.error("Failed to create address for member: {}", memberId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        logger.info("Successfully created address with ID: {} for member: {}", addressEntity.getId(), memberId);
        return addressEntity.toAddress();
    }
    
    @Override
    @Transactional
    public Address updateAddress(@Valid @NotNull UpdateAddressRequest request, @NotNull Long memberId) {
        logger.info("Updating address: {} for member: {}", request.getId(), memberId);
        
        // 验证地址所有权
        AddressEntity existingAddress = validateAddressOwnership(request.getId(), memberId);
        
        // 如果设置为默认地址，先清除其他默认地址
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressMapper.clearDefaultAddress(memberId);
        }
        
        // 更新地址信息
        updateAddressEntity(existingAddress, request);
        
        // 更新到数据库
        int result = addressMapper.updateAddress(existingAddress);
        if (result != 1) {
            logger.error("Failed to update address: {} for member: {}", request.getId(), memberId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        logger.info("Successfully updated address: {} for member: {}", request.getId(), memberId);
        return existingAddress.toAddress();
    }
    
    @Override
    public Address getAddressById(@NotNull Long id, @NotNull Long memberId) {
        logger.debug("Getting address: {} for member: {}", id, memberId);
        
        AddressEntity addressEntity = addressMapper.findByIdAndMemberId(id, memberId);
        if (addressEntity == null) {
            logger.warn("Address not found: {} for member: {}", id, memberId);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        
        return addressEntity.toAddress();
    }
    
    @Override
    public List<Address> getAddressesByMemberId(@NotNull Long memberId) {
        logger.debug("Getting addresses for member: {}", memberId);
        
        // 验证用户是否存在
        validateMemberExists(memberId);
        
        List<AddressEntity> addressEntities = addressMapper.findByMemberId(memberId);
        return addressEntities.stream()
                .map(AddressEntity::toAddress)
                .collect(Collectors.toList());
    }
    
    @Override
    public Address getDefaultAddress(@NotNull Long memberId) {
        logger.debug("Getting default address for member: {}", memberId);
        
        // 验证用户是否存在
        validateMemberExists(memberId);
        
        AddressEntity addressEntity = addressMapper.findDefaultByMemberId(memberId);
        return addressEntity != null ? addressEntity.toAddress() : null;
    }
    
    @Override
    @Transactional
    public Address setDefaultAddress(@NotNull Long id, @NotNull Long memberId) {
        logger.info("Setting default address: {} for member: {}", id, memberId);
        
        // 验证地址所有权
        AddressEntity addressEntity = validateAddressOwnership(id, memberId);
        
        // 清除其他默认地址
        addressMapper.clearDefaultAddress(memberId);
        
        // 设置为默认地址
        addressEntity.setIsDefault(1);
        addressEntity.setUpdatedAt(LocalDateTime.now());
        
        int result = addressMapper.updateAddress(addressEntity);
        if (result != 1) {
            logger.error("Failed to set default address: {} for member: {}", id, memberId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        logger.info("Successfully set default address: {} for member: {}", id, memberId);
        return addressEntity.toAddress();
    }
    
    @Override
    @Transactional
    public void deleteAddress(@NotNull Long id, @NotNull Long memberId) {
        logger.info("Deleting address: {} for member: {}", id, memberId);
        
        // 验证地址所有权
        AddressEntity addressEntity = validateAddressOwnership(id, memberId);
        
        // 检查是否为默认地址（可选：根据业务需求决定是否允许删除默认地址）
        if (addressEntity.getIsDefault() == 1) {
            logger.warn("Attempting to delete default address: {} for member: {}", id, memberId);
            // 可以选择抛出异常或者允许删除
            // throw new BizException(ErrorCode.DEFAULT_ADDRESS_CANNOT_DELETE);
        }
        
        // 执行逻辑删除
        int result = addressMapper.deleteAddress(id, memberId);
        if (result != 1) {
            logger.error("Failed to delete address: {} for member: {}", id, memberId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        logger.info("Successfully deleted address: {} for member: {}", id, memberId);
    }
    
    @Override
    public boolean validateAddressOwnership(@NotNull Long id, @NotNull Long memberId) {
        return addressMapper.existsByIdAndMemberId(id, memberId);
    }
    
    /**
     * 验证用户是否存在
     */
    private void validateMemberExists(Long memberId) {
        try {
            memberService.getMemberById(memberId);
        } catch (BizException e) {
            if (ErrorCode.MEMBER_NOT_FOUND.Is(e.getErrorCode())) {
                throw e;
            }
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 验证地址数量限制
     */
    private void validateAddressLimit(Long memberId) {
        int addressCount = addressMapper.countByMemberId(memberId);
        if (addressCount >= MAX_ADDRESS_COUNT) {
            logger.warn("Address count exceeded limit for member: {}, current count: {}", memberId, addressCount);
            throw new BizException(ErrorCode.ADDRESS_LIMIT_EXCEEDED);
        }
    }
    
    /**
     * 验证地址所有权并返回地址实体
     */
    private AddressEntity validateAddressOwnership(Long id, Long memberId) {
        AddressEntity addressEntity = addressMapper.findByIdAndMemberId(id, memberId);
        if (addressEntity == null) {
            logger.warn("Address not found or access denied: {} for member: {}", id, memberId);
            throw new BizException(ErrorCode.ADDRESS_ACCESS_DENIED);
        }
        return addressEntity;
    }
    
    /**
     * 创建地址实体
     */
    private AddressEntity createAddressEntity(CreateAddressRequest request, Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setMemberId(memberId);
        addressEntity.setReceiverName(request.getReceiverName());
        addressEntity.setReceiverPhone(request.getReceiverPhone());
        addressEntity.setProvince(request.getProvince());
        addressEntity.setCity(request.getCity());
        addressEntity.setDistrict(request.getDistrict());
        addressEntity.setDetailAddress(request.getDetailAddress());
        addressEntity.setPostalCode(request.getPostalCode());
        addressEntity.setIsDefault(Boolean.TRUE.equals(request.getIsDefault()) ? 1 : 0);
        addressEntity.setAddressLabel(request.getAddressLabel());
        addressEntity.setDeleted(0);
        addressEntity.setCreatedAt(now);
        addressEntity.setUpdatedAt(now);
        
        return addressEntity;
    }
    
    /**
     * 更新地址实体
     */
    private void updateAddressEntity(AddressEntity existingAddress, UpdateAddressRequest request) {
        LocalDateTime now = LocalDateTime.now();
        
        if (StringUtils.isNotBlank(request.getReceiverName())) {
            existingAddress.setReceiverName(request.getReceiverName());
        }
        if (StringUtils.isNotBlank(request.getReceiverPhone())) {
            existingAddress.setReceiverPhone(request.getReceiverPhone());
        }
        if (StringUtils.isNotBlank(request.getProvince())) {
            existingAddress.setProvince(request.getProvince());
        }
        if (StringUtils.isNotBlank(request.getCity())) {
            existingAddress.setCity(request.getCity());
        }
        if (StringUtils.isNotBlank(request.getDistrict())) {
            existingAddress.setDistrict(request.getDistrict());
        }
        if (StringUtils.isNotBlank(request.getDetailAddress())) {
            existingAddress.setDetailAddress(request.getDetailAddress());
        }
        if (StringUtils.isNotBlank(request.getPostalCode())) {
            existingAddress.setPostalCode(request.getPostalCode());
        }
        if (request.getIsDefault() != null) {
            existingAddress.setIsDefault(request.getIsDefault() ? 1 : 0);
        }
        if (StringUtils.isNotBlank(request.getAddressLabel())) {
            existingAddress.setAddressLabel(request.getAddressLabel());
        }
        
        existingAddress.setUpdatedAt(now);
    }
}