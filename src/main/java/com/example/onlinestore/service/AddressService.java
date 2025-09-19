package com.example.onlinestore.service;

import com.example.onlinestore.bean.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface AddressService {
    
    /**
     * 创建新地址
     *
     * @param address 地址信息，需通过参数校验（@Valid）
     * @return 持久化后的地址对象
     */
    Address createAddress(@Valid Address address);
    
    /**
     * 更新地址信息
     *
     * @param address 需要更新的地址信息，需通过参数校验（@Valid）
     * @return 更新后的地址对象
     * @throws com.example.onlinestore.exceptions.BizException 当地址不存在时抛出
     */
    Address updateAddress(@Valid Address address);
    
    /**
     * 根据ID删除地址
     *
     * @param id 地址ID，不能为null（@NotNull）
     * @throws com.example.onlinestore.exceptions.BizException 当地址不存在时抛出
     */
    void deleteAddress(@NotNull Long id);
    
    /**
     * 根据ID获取地址信息
     *
     * @param id 地址ID，不能为null（@NotNull）
     * @return 匹配的地址对象
     * @throws com.example.onlinestore.exceptions.BizException 当地址不存在时抛出
     */
    Address getAddressById(@NotNull Long id);
    
    /**
     * 根据用户ID获取该用户的所有地址
     *
     * @param memberId 用户ID，不能为null（@NotNull）
     * @return 用户的地址列表，如果没有地址则返回空列表
     */
    List<Address> getAddressesByMemberId(@NotNull Long memberId);
    
    /**
     * 获取用户的默认地址
     *
     * @param memberId 用户ID，不能为null（@NotNull）
     * @return 用户的默认地址，如果没有默认地址则返回null
     */
    Address getDefaultAddress(@NotNull Long memberId);
    
    /**
     * 设置默认地址
     *
     * @param addressId 要设为默认地址的ID，不能为null（@NotNull）
     * @param memberId 用户ID，不能为null（@NotNull）
     * @throws com.example.onlinestore.exceptions.BizException 当地址不存在或不属于该用户时抛出
     */
    void setDefaultAddress(@NotNull Long addressId, @NotNull Long memberId);
    
    /**
     * 获取当前登录用户的所有地址
     *
     * @return 当前登录用户的地址列表
     * @throws com.example.onlinestore.exceptions.BizException 如果未登录，抛出异常
     */
    List<Address> getCurrentUserAddresses();
    
    /**
     * 获取当前登录用户的默认地址
     *
     * @return 当前登录用户的默认地址，如果没有默认地址则返回null
     * @throws com.example.onlinestore.exceptions.BizException 如果未登录，抛出异常
     */
    Address getCurrentUserDefaultAddress();
}