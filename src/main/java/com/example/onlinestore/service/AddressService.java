package com.example.onlinestore.service;

import com.example.onlinestore.bean.Address;

import java.util.List;

public interface AddressService {

    /**
     * 添加新地址
     * 如果是默认地址，会先清除其他默认地址标记
     * 
     * @param address 地址信息
     * @return 创建成功的地址信息（包含ID）
     */
    Address createAddress(Address address);

    /**
     * 更新地址信息
     * 
     * @param address 地址信息
     * @return 更新后的地址信息
     */
    Address updateAddress(Address address);

    /**
     * 删除地址
     * 
     * @param addressId 地址ID
     * @param memberId 会员ID（用于验证权限）
     */
    void deleteAddress(Long addressId, Long memberId);

    /**
     * 根据ID获取地址信息
     * 
     * @param addressId 地址ID
     * @param memberId 会员ID（用于验证权限）
     * @return 地址信息
     */
    Address getAddressById(Long addressId, Long memberId);

    /**
     * 获取会员的所有地址
     * 
     * @param memberId 会员ID
     * @return 地址列表
     */
    List<Address> getAddressesByMemberId(Long memberId);

    /**
     * 获取会员的默认地址
     * 
     * @param memberId 会员ID
     * @return 默认地址，如果没有则返回null
     */
    Address getDefaultAddress(Long memberId);

    /**
     * 设置默认地址
     * 会先清除其他默认地址标记，再设置指定地址为默认
     * 
     * @param addressId 地址ID
     * @param memberId 会员ID（用于验证权限）
     */
    void setDefaultAddress(Long addressId, Long memberId);

    /**
     * 验证会员地址数量限制
     * 
     * @param memberId 会员ID
     * @return 是否可以添加更多地址
     */
    boolean canAddMoreAddresses(Long memberId);
}