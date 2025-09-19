package com.example.onlinestore.service;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.dto.CreateAddressRequest;
import com.example.onlinestore.dto.UpdateAddressRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 地址服务接口
 */
public interface AddressService {
    
    /**
     * 创建地址
     * 
     * @param request 创建地址请求
     * @param memberId 用户ID
     * @return 创建的地址对象
     */
    Address createAddress(@Valid @NotNull CreateAddressRequest request, @NotNull Long memberId);
    
    /**
     * 更新地址
     * 
     * @param request 更新地址请求
     * @param memberId 用户ID
     * @return 更新后的地址对象
     */
    Address updateAddress(@Valid @NotNull UpdateAddressRequest request, @NotNull Long memberId);
    
    /**
     * 根据ID获取地址
     * 
     * @param id 地址ID
     * @param memberId 用户ID（用于权限验证）
     * @return 地址对象
     */
    Address getAddressById(@NotNull Long id, @NotNull Long memberId);
    
    /**
     * 根据用户ID获取地址列表
     * 
     * @param memberId 用户ID
     * @return 地址列表
     */
    List<Address> getAddressesByMemberId(@NotNull Long memberId);
    
    /**
     * 获取用户默认地址
     * 
     * @param memberId 用户ID
     * @return 默认地址，可能为null
     */
    Address getDefaultAddress(@NotNull Long memberId);
    
    /**
     * 设置默认地址
     * 
     * @param id 地址ID
     * @param memberId 用户ID
     * @return 设置后的地址对象
     */
    Address setDefaultAddress(@NotNull Long id, @NotNull Long memberId);
    
    /**
     * 删除地址
     * 
     * @param id 地址ID
     * @param memberId 用户ID
     */
    void deleteAddress(@NotNull Long id, @NotNull Long memberId);
    
    /**
     * 验证地址是否属于指定用户
     * 
     * @param id 地址ID
     * @param memberId 用户ID
     * @return 是否属于该用户
     */
    boolean validateAddressOwnership(@NotNull Long id, @NotNull Long memberId);
}