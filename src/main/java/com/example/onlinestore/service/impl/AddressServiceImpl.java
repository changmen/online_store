package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.bean.Member;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.service.AddressService;
import com.example.onlinestore.service.MemberService;
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

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private MemberService memberService;

    @Override
    @Transactional
    public Address createAddress(@Valid Address address) {
        LocalDateTime now = LocalDateTime.now();
        
        // 如果设置为默认地址，先清除该用户的其他默认地址
        if (address.isDefault()) {
            addressMapper.clearDefaultByMemberId(address.getMemberId());
        }
        
        AddressEntity entity = AddressEntity.fromAddress(address);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        
        int effectRows = addressMapper.insertAddress(entity);
        if (effectRows != 1) {
            logger.error("insert address failed. effect rows: {}, memberId: {}", effectRows, address.getMemberId());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        return entity.toAddress();
    }

    @Override
    @Transactional
    public Address updateAddress(@Valid Address address) {
        if (address.getId() == null) {
            throw new BizException(ErrorCode.PARAMETER_ERROR, "地址ID不能为空");
        }
        
        AddressEntity existingEntity = addressMapper.findById(address.getId());
        if (existingEntity == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, address.getId());
        }
        
        // 如果设置为默认地址，先清除该用户的其他默认地址
        if (address.isDefault()) {
            addressMapper.clearDefaultByMemberId(address.getMemberId());
        }
        
        AddressEntity entity = AddressEntity.fromAddress(address);
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setCreatedAt(existingEntity.getCreatedAt()); // 保持原创建时间
        
        int effectRows = addressMapper.updateAddress(entity);
        if (effectRows != 1) {
            logger.error("update address failed. effect rows: {}, addressId: {}", effectRows, address.getId());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        return entity.toAddress();
    }

    @Override
    @Transactional
    public void deleteAddress(@NotNull Long id) {
        AddressEntity existingEntity = addressMapper.findById(id);
        if (existingEntity == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, id);
        }
        
        int effectRows = addressMapper.deleteAddress(id);
        if (effectRows != 1) {
            logger.error("delete address failed. effect rows: {}, addressId: {}", effectRows, id);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Address getAddressById(@NotNull Long id) {
        AddressEntity entity = addressMapper.findById(id);
        if (entity == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, id);
        }
        return entity.toAddress();
    }

    @Override
    public List<Address> getAddressesByMemberId(@NotNull Long memberId) {
        List<AddressEntity> entities = addressMapper.findByMemberId(memberId);
        return entities.stream()
                .map(AddressEntity::toAddress)
                .collect(Collectors.toList());
    }

    @Override
    public Address getDefaultAddress(@NotNull Long memberId) {
        AddressEntity entity = addressMapper.findDefaultByMemberId(memberId);
        return entity != null ? entity.toAddress() : null;
    }

    @Override
    @Transactional
    public void setDefaultAddress(@NotNull Long addressId, @NotNull Long memberId) {
        AddressEntity existingEntity = addressMapper.findById(addressId);
        if (existingEntity == null) {
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, addressId);
        }
        
        if (!existingEntity.getMemberId().equals(memberId)) {
            throw new BizException(ErrorCode.ADDRESS_NOT_BELONG_TO_MEMBER, addressId, memberId);
        }
        
        // 先清除该用户的其他默认地址
        addressMapper.clearDefaultByMemberId(memberId);
        
        // 设置当前地址为默认
        existingEntity.setIsDefault(1);
        existingEntity.setUpdatedAt(LocalDateTime.now());
        
        int effectRows = addressMapper.updateAddress(existingEntity);
        if (effectRows != 1) {
            logger.error("set default address failed. effect rows: {}, addressId: {}", effectRows, addressId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Address> getCurrentUserAddresses() {
        Member currentMember = memberService.getLoginMember();
        return getAddressesByMemberId(currentMember.getId());
    }

    @Override
    public Address getCurrentUserDefaultAddress() {
        Member currentMember = memberService.getLoginMember();
        return getDefaultAddress(currentMember.getId());
    }
}