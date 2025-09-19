package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
    
    private static final int MAX_ADDRESS_COUNT = 20; // 每个用户最多20个地址

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public Address getById(Long id) {
        if (id == null) {
            throw new BizException(ErrorCode.INVALID_PARAMETER, "地址ID不能为空");
        }
        
        AddressEntity entity = addressMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ErrorCode.DATA_NOT_FOUND, "地址信息不存在");
        }
        
        return convertToBean(entity);
    }

    @Override
    public List<Address> getByMemberId(Long memberId) {
        if (memberId == null) {
            throw new BizException(ErrorCode.INVALID_PARAMETER, "用户ID不能为空");
        }
        
        List<AddressEntity> entities = addressMapper.selectByMemberId(memberId);
        return entities.stream()
                .map(this::convertToBean)
                .collect(Collectors.toList());
    }

    @Override
    public Address getDefaultByMemberId(Long memberId) {
        if (memberId == null) {
            throw new BizException(ErrorCode.INVALID_PARAMETER, "用户ID不能为空");
        }
        
        AddressEntity entity = addressMapper.selectDefaultByMemberId(memberId);
        return entity != null ? convertToBean(entity) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address create(Address address) {
        validateAddress(address);
        
        if (!canAddMore(address.getMemberId())) {
            throw new BizException(ErrorCode.OPERATION_NOT_ALLOWED, "地址数量已达上限，最多添加" + MAX_ADDRESS_COUNT + "个地址");
        }
        
        AddressEntity entity = convertToEntity(address);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        
        // 如果设置为默认地址，需要先清除其他默认地址
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressMapper.clearDefaultByMemberId(address.getMemberId());
            entity.setIsDefault(1);
        } else {
            entity.setIsDefault(0);
        }
        
        int result = addressMapper.insert(entity);
        if (result <= 0) {
            throw new BizException(ErrorCode.OPERATION_FAILED, "创建地址失败");
        }
        
        logger.info("用户 {} 创建了新地址，地址ID: {}", address.getMemberId(), entity.getId());
        return convertToBean(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Address update(Address address) {
        validateAddress(address);
        
        if (address.getId() == null) {
            throw new BizException(ErrorCode.INVALID_PARAMETER, "地址ID不能为空");
        }
        
        // 验证地址所有权
        if (!validateOwnership(address.getId(), address.getMemberId())) {
            throw new BizException(ErrorCode.OPERATION_NOT_ALLOWED, "无权限修改该地址");
        }
        
        AddressEntity entity = convertToEntity(address);
        entity.setUpdateTime(LocalDateTime.now());
        
        // 如果设置为默认地址，需要先清除其他默认地址
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressMapper.clearDefaultByMemberId(address.getMemberId());
            entity.setIsDefault(1);
        } else {
            entity.setIsDefault(0);
        }
        
        int result = addressMapper.update(entity);
        if (result <= 0) {
            throw new BizException(ErrorCode.OPERATION_FAILED, "更新地址失败");
        }
        
        logger.info("用户 {} 更新了地址，地址ID: {}", address.getMemberId(), address.getId());
        return getById(address.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id, Long memberId) {
        if (id == null || memberId == null) {
            throw new BizException(ErrorCode.INVALID_PARAMETER, "地址ID和用户ID不能为空");
        }
        
        // 验证地址所有权
        if (!validateOwnership(id, memberId)) {
            throw new BizException(ErrorCode.OPERATION_NOT_ALLOWED, "无权限删除该地址");
        }
        
        int result = addressMapper.deleteById(id);
        if (result <= 0) {
            throw new BizException(ErrorCode.OPERATION_FAILED, "删除地址失败");
        }
        
        logger.info("用户 {} 删除了地址，地址ID: {}", memberId, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long id, Long memberId) {
        if (id == null || memberId == null) {
            throw new BizException(ErrorCode.INVALID_PARAMETER, "地址ID和用户ID不能为空");
        }
        
        // 验证地址所有权
        if (!validateOwnership(id, memberId)) {
            throw new BizException(ErrorCode.OPERATION_NOT_ALLOWED, "无权限设置该地址为默认地址");
        }
        
        // 先清除其他默认地址
        addressMapper.clearDefaultByMemberId(memberId);
        
        // 设置新的默认地址
        int result = addressMapper.setDefaultById(id);
        if (result <= 0) {
            throw new BizException(ErrorCode.OPERATION_FAILED, "设置默认地址失败");
        }
        
        logger.info("用户 {} 设置了默认地址，地址ID: {}", memberId, id);
    }

    @Override
    public boolean validateOwnership(Long id, Long memberId) {
        if (id == null || memberId == null) {
            return false;
        }
        
        AddressEntity entity = addressMapper.selectByIdAndMemberId(id, memberId);
        return entity != null;
    }

    @Override
    public boolean canAddMore(Long memberId) {
        if (memberId == null) {
            return false;
        }
        
        int count = addressMapper.countByMemberId(memberId);
        return count < MAX_ADDRESS_COUNT;
    }

    /**
     * 验证地址信息
     *
     * @param address 地址信息
     */
    private void validateAddress(Address address) {
        if (address == null) {
            throw new BizException(ErrorCode.INVALID_PARAMETER, "地址信息不能为空");
        }
        if (address.getMemberId() == null) {
            throw new BizException(ErrorCode.INVALID_PARAMETER, "用户ID不能为空");
        }
    }

    /**
     * 实体转Bean
     *
     * @param entity 实体对象
     * @return Bean对象
     */
    private Address convertToBean(AddressEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Address address = new Address();
        BeanUtils.copyProperties(entity, address);
        address.setIsDefault(entity.getIsDefault() != null && entity.getIsDefault() == 1);
        return address;
    }

    /**
     * Bean转实体
     *
     * @param address Bean对象
     * @return 实体对象
     */
    private AddressEntity convertToEntity(Address address) {
        if (address == null) {
            return null;
        }
        
        AddressEntity entity = new AddressEntity();
        BeanUtils.copyProperties(address, entity);
        return entity;
    }
}