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
     * 创建新地址
     *
     * @param request 创建地址请求对象，需通过参数校验（@Valid）
     * @param memberId 用户ID，不能为null（@NotNull）
     * @return 创建后的地址对象
     * @throws com.example.onlinestore.exceptions.BizException 当创建失败时抛出
     */
    Address createAddress(@Valid CreateAddressRequest request, @NotNull Long memberId);
    
    /**
     * 更新地址信息
     *
     * @param request 更新地址请求对象，需通过参数校验（@Valid）
     * @param memberId 用户ID，不能为null（@NotNull）
     * @return 更新后的地址对象
     * @throws com.example.onlinestore.exceptions.BizException 当地址不存在或不属于该用户时抛出
     */
    Address updateAddress(@Valid UpdateAddressRequest request, @NotNull Long memberId);
    
    /**
     * 删除地址
     *
     * @param addressId 地址ID，不能为null（@NotNull）
     * @param memberId 用户ID，不能为null（@NotNull）
     * @throws com.example.onlinestore.exceptions.BizException 当地址不存在或不属于该用户时抛出
     */
    void deleteAddress(@NotNull Long addressId, @NotNull Long memberId);
    
    /**
     * 根据ID获取地址信息
     *
     * @param addressId 地址ID，不能为null（@NotNull）
     * @param memberId 用户ID，不能为null（@NotNull）
     * @return 地址对象
     * @throws com.example.onlinestore.exceptions.BizException 当地址不存在或不属于该用户时抛出
     */
    Address getAddressById(@NotNull Long addressId, @NotNull Long memberId);
    
    /**
     * 获取用户的所有地址
     *
     * @param memberId 用户ID，不能为null（@NotNull）
     * @return 地址列表
     */
    List<Address> getAddressesByMemberId(@NotNull Long memberId);
    
    /**
     * 获取用户的默认地址
     *
     * @param memberId 用户ID，不能为null（@NotNull）
     * @return 默认地址，如果没有默认地址则返回null
     */
    Address getDefaultAddress(@NotNull Long memberId);
    
    /**
     * 设置默认地址
     *
     * @param addressId 地址ID，不能为null（@NotNull）
     * @param memberId 用户ID，不能为null（@NotNull）
     * @throws com.example.onlinestore.exceptions.BizException 当地址不存在或不属于该用户时抛出
     */
    void setDefaultAddress(@NotNull Long addressId, @NotNull Long memberId);
}