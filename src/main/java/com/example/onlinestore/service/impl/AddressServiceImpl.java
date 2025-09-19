package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.dto.CreateAddressRequest;
import com.example.onlinestore.dto.UpdateAddressRequest;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.service.AddressService;
import com.example.onlinestore.service.MemberService;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.dto.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 地址服务实现类
 */
@Slf4j
@Service
public class AddressServiceImpl implements AddressService {
    
    private static final String ADDRESS_CACHE_PREFIX = "address:";
    private static final String USER_ADDRESSES_CACHE_PREFIX = "user_addresses:";
    private static final long CACHE_EXPIRE_SECONDS = 3600; // 1小时
    private static final int MAX_ADDRESSES_PER_USER = 20; // 每个用户最多20个地址
    
    @Autowired
    private AddressMapper addressMapper;
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public AddressEntity createAddress(@NotNull CreateAddressRequest request) {
        log.info("Creating address for request: {}", request);
        
        // 获取当前登录用户
        Member currentMember = memberService.getLoginMember();
        Long memberId = currentMember.getId();
        
        // 检查用户地址数量限制
        int addressCount = addressMapper.countByMemberId(memberId);
        if (addressCount >= MAX_ADDRESSES_PER_USER) {
            log.warn("User {} has reached maximum address limit: {}", memberId, addressCount);
            throw new BizException(ErrorCode.ADDRESS_LIMIT_EXCEEDED);
        }
        
        // 如果是第一个地址或者指定为默认地址，需要处理默认地址逻辑
        boolean isFirstAddress = (addressCount == 0);
        boolean shouldSetAsDefault = isFirstAddress || Boolean.TRUE.equals(request.getIsDefault());
        
        if (shouldSetAsDefault) {
            // 清除现有的默认地址标记
            addressMapper.clearDefaultByMemberId(memberId);
        }
        
        // 构建地址实体
        AddressEntity addressEntity = buildAddressEntity(request, memberId, shouldSetAsDefault);
        
        // 保存地址
        int result = addressMapper.insertAddress(addressEntity);
        if (result != 1) {
            log.error("Failed to insert address for user {}", memberId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        // 清除用户地址缓存
        clearUserAddressesCache(memberId);
        
        log.info("Successfully created address with ID: {} for user: {}", addressEntity.getId(), memberId);
        return addressEntity;
    }
    
    @Override
    public AddressEntity getAddressById(@NotNull Long id) {
        log.debug("Getting address by ID: {}", id);
        
        // 验证地址归属权
        validateAddressOwnership(id);
        
        // 尝试从缓存获取
        String cacheKey = ADDRESS_CACHE_PREFIX + id;
        AddressEntity cached = (AddressEntity) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.debug("Address {} found in cache", id);
            return cached;
        }
        
        // 从数据库查询
        AddressEntity address = addressMapper.findById(id);
        if (address == null) {
            log.warn("Address not found: {}", id);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, id);
        }
        
        // 存入缓存
        redisTemplate.opsForValue().set(cacheKey, address, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
        
        return address;
    }
    
    @Override
    public List<AddressEntity> getMyAddresses() {
        log.debug("Getting current user's addresses");
        
        Member currentMember = memberService.getLoginMember();
        Long memberId = currentMember.getId();
        
        // 尝试从缓存获取
        String cacheKey = USER_ADDRESSES_CACHE_PREFIX + memberId;
        @SuppressWarnings("unchecked")
        List<AddressEntity> cached = (List<AddressEntity>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.debug("User {} addresses found in cache", memberId);
            return cached;
        }
        
        // 从数据库查询
        List<AddressEntity> addresses = addressMapper.findByMemberId(memberId);
        
        // 存入缓存
        redisTemplate.opsForValue().set(cacheKey, addresses, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
        
        return addresses;
    }
    
    @Override
    public AddressEntity getMyDefaultAddress() {
        log.debug("Getting current user's default address");
        
        Member currentMember = memberService.getLoginMember();
        Long memberId = currentMember.getId();
        
        return addressMapper.findDefaultByMemberId(memberId);
    }
    
    @Override
    public Page<AddressEntity> getMyAddressesPaged(PageRequest pageRequest) {
        log.debug("Getting current user's addresses with paging: {}", pageRequest);
        
        Member currentMember = memberService.getLoginMember();
        Long memberId = currentMember.getId();
        
        // 计算偏移量
        int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
        
        // 查询数据
        List<AddressEntity> addresses = addressMapper.findByMemberIdWithPaging(
            memberId, offset, pageRequest.getPageSize());
        
        // 查询总数
        int total = addressMapper.countByMemberId(memberId);
        
        // 构建分页结果
        Page<AddressEntity> page = new Page<>();
        page.setList(addresses);
        page.setTotal(total);
        page.setPageNum(pageRequest.getPageNum());
        page.setPageSize(pageRequest.getPageSize());
        
        return page;
    }
    
    @Override
    @Transactional
    public AddressEntity updateAddress(@NotNull Long id, @NotNull UpdateAddressRequest request) {
        log.info("Updating address {} with request: {}", id, request);
        
        // 验证地址归属权
        validateAddressOwnership(id);
        
        Member currentMember = memberService.getLoginMember();
        Long memberId = currentMember.getId();
        
        // 查询现有地址
        AddressEntity existingAddress = addressMapper.findById(id);
        if (existingAddress == null) {
            log.warn("Address not found for update: {}", id);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, id);
        }
        
        // 处理默认地址逻辑
        if (Boolean.TRUE.equals(request.getIsDefault()) && !Boolean.TRUE.equals(existingAddress.getIsDefault())) {
            // 设置为默认地址，需要清除其他默认地址
            addressMapper.clearDefaultByMemberId(memberId);
        }
        
        // 更新地址信息
        AddressEntity updatedAddress = buildAddressEntityForUpdate(request, existingAddress);
        
        int result = addressMapper.updateAddress(updatedAddress);
        if (result != 1) {
            log.error("Failed to update address {}", id);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        // 清除缓存
        clearAddressCache(id);
        clearUserAddressesCache(memberId);
        
        log.info("Successfully updated address: {}", id);
        return addressMapper.findById(id);
    }
    
    @Override
    @Transactional
    public void setDefaultAddress(@NotNull Long id) {
        log.info("Setting default address: {}", id);
        
        // 验证地址归属权
        validateAddressOwnership(id);
        
        Member currentMember = memberService.getLoginMember();
        Long memberId = currentMember.getId();
        
        // 清除现有默认地址
        addressMapper.clearDefaultByMemberId(memberId);
        
        // 设置新的默认地址
        int result = addressMapper.setAsDefault(id, memberId);
        if (result != 1) {
            log.error("Failed to set default address {}", id);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        // 清除缓存
        clearAddressCache(id);
        clearUserAddressesCache(memberId);
        
        log.info("Successfully set default address: {}", id);
    }
    
    @Override
    @Transactional
    public void deleteAddress(@NotNull Long id) {
        log.info("Deleting address: {}", id);
        
        // 验证地址归属权
        validateAddressOwnership(id);
        
        Member currentMember = memberService.getLoginMember();
        Long memberId = currentMember.getId();
        
        // 查询地址信息以确定是否为默认地址
        AddressEntity address = addressMapper.findById(id);
        if (address == null) {
            log.warn("Address not found for deletion: {}", id);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, id);
        }
        
        // 删除地址
        int result = addressMapper.deleteById(id);
        if (result != 1) {
            log.error("Failed to delete address {}", id);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        
        // 如果删除的是默认地址，尝试设置下一个地址为默认地址
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            List<AddressEntity> remainingAddresses = addressMapper.findByMemberId(memberId);
            if (!CollectionUtils.isEmpty(remainingAddresses)) {
                // 设置第一个地址为默认地址
                AddressEntity firstAddress = remainingAddresses.get(0);
                addressMapper.setAsDefault(firstAddress.getId(), memberId);
                clearAddressCache(firstAddress.getId());
            }
        }
        
        // 清除缓存
        clearAddressCache(id);
        clearUserAddressesCache(memberId);
        
        log.info("Successfully deleted address: {}", id);
    }
    
    @Override
    public void validateAddressOwnership(@NotNull Long id) {
        Member currentMember = memberService.getLoginMember();
        Long memberId = currentMember.getId();
        
        boolean exists = addressMapper.existsByIdAndMemberId(id, memberId);
        if (!exists) {
            log.warn("Address {} does not belong to user {} or does not exist", id, memberId);
            throw new BizException(ErrorCode.ADDRESS_NOT_FOUND, id);
        }
    }
    
    /**
     * 构建地址实体（创建时使用）
     */
    private AddressEntity buildAddressEntity(CreateAddressRequest request, Long memberId, boolean isDefault) {
        LocalDateTime now = LocalDateTime.now();
        
        AddressEntity entity = new AddressEntity();
        entity.setMemberId(memberId);
        entity.setReceiverName(request.getReceiverName());
        entity.setPhone(request.getPhone());
        entity.setProvince(request.getProvince());
        entity.setCity(request.getCity());
        entity.setDistrict(request.getDistrict());
        entity.setDetailAddress(request.getDetailAddress());
        entity.setPostCode(request.getPostCode());
        entity.setIsDefault(isDefault);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        
        return entity;
    }
    
    /**
     * 构建地址实体（更新时使用）
     */
    private AddressEntity buildAddressEntityForUpdate(UpdateAddressRequest request, AddressEntity existing) {
        AddressEntity entity = new AddressEntity();
        entity.setId(existing.getId());
        entity.setMemberId(existing.getMemberId());
        entity.setReceiverName(request.getReceiverName());
        entity.setPhone(request.getPhone());
        entity.setProvince(request.getProvince());
        entity.setCity(request.getCity());
        entity.setDistrict(request.getDistrict());
        entity.setDetailAddress(request.getDetailAddress());
        entity.setPostCode(request.getPostCode());
        entity.setIsDefault(request.getIsDefault());
        entity.setCreatedAt(existing.getCreatedAt());
        entity.setUpdatedAt(LocalDateTime.now());
        
        return entity;
    }
    
    /**
     * 清除地址缓存
     */
    private void clearAddressCache(Long addressId) {
        String cacheKey = ADDRESS_CACHE_PREFIX + addressId;
        redisTemplate.delete(cacheKey);
        log.debug("Cleared address cache for ID: {}", addressId);
    }
    
    /**
     * 清除用户地址列表缓存
     */
    private void clearUserAddressesCache(Long memberId) {
        String cacheKey = USER_ADDRESSES_CACHE_PREFIX + memberId;
        redisTemplate.delete(cacheKey);
        log.debug("Cleared user addresses cache for member: {}", memberId);
    }
}