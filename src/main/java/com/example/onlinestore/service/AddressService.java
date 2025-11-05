package com.example.onlinestore.service;

import com.example.onlinestore.dto.CreateAddressRequest;
import com.example.onlinestore.dto.UpdateAddressRequest;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.dto.PageRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 地址服务接口
 */
public interface AddressService {
    
    /**
     * 创建新地址
     * 
     * @param request 创建地址请求
     * @return 创建的地址实体
     */
    AddressEntity createAddress(@NotNull CreateAddressRequest request);
    
    /**
     * 根据ID查询地址
     * 
     * @param id 地址ID
     * @return 地址实体
     * @throws com.example.onlinestore.exceptions.BizException 地址不存在或无权限访问时抛出
     */
    AddressEntity getAddressById(@NotNull Long id);
    
    /**
     * 查询当前用户的所有地址
     * 
     * @return 地址列表，按创建时间倒序排列
     */
    List<AddressEntity> getMyAddresses();
    
    /**
     * 查询当前用户的默认地址
     * 
     * @return 默认地址实体，不存在时返回null
     */
    AddressEntity getMyDefaultAddress();
    
    /**
     * 分页查询当前用户的地址
     * 
     * @param pageRequest 分页请求参数
     * @return 分页结果
     */
    Page<AddressEntity> getMyAddressesPaged(PageRequest pageRequest);
    
    /**
     * 更新地址信息
     * 
     * @param id 地址ID
     * @param request 更新地址请求
     * @return 更新后的地址实体
     * @throws com.example.onlinestore.exceptions.BizException 地址不存在或无权限访问时抛出
     */
    AddressEntity updateAddress(@NotNull Long id, @NotNull UpdateAddressRequest request);
    
    /**
     * 设置默认地址
     * 
     * @param id 地址ID
     * @throws com.example.onlinestore.exceptions.BizException 地址不存在或无权限访问时抛出
     */
    void setDefaultAddress(@NotNull Long id);
    
    /**
     * 删除地址
     * 
     * @param id 地址ID
     * @throws com.example.onlinestore.exceptions.BizException 地址不存在或无权限访问时抛出
     */
    void deleteAddress(@NotNull Long id);
    
    /**
     * 验证地址是否属于当前用户
     * 
     * @param id 地址ID
     * @throws com.example.onlinestore.exceptions.BizException 地址不存在或无权限访问时抛出
     */
    void validateAddressOwnership(@NotNull Long id);
}