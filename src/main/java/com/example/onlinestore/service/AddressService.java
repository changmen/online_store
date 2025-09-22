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
     */
    Address createAddress(@NotNull Long memberId, @Valid CreateAddressRequest request);

    /**
     * 更新地址
     */
    Address updateAddress(@NotNull Long memberId, @Valid UpdateAddressRequest request);

    /**
     * 根据ID获取地址
     */
    Address getAddressById(@NotNull Long id);

    /**
     * 根据ID和用户ID获取地址
     */
    Address getAddressByIdAndMemberId(@NotNull Long id, @NotNull Long memberId);

    /**
     * 获取用户的所有地址
     */
    List<Address> getAddressesByMemberId(@NotNull Long memberId);

    /**
     * 获取用户的默认地址
     */
    Address getDefaultAddressByMemberId(@NotNull Long memberId);

    /**
     * 设置默认地址
     */
    void setDefaultAddress(@NotNull Long id, @NotNull Long memberId);

    /**
     * 删除地址
     */
    void deleteAddress(@NotNull Long id, @NotNull Long memberId);

    /**
     * 验证地址是否属于指定用户
     */
    boolean validateAddressOwnership(@NotNull Long addressId, @NotNull Long memberId);

    /**
     * 获取用户地址数量
     */
    int getAddressCountByMemberId(@NotNull Long memberId);
}