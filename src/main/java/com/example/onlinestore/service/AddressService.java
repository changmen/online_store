package com.example.onlinestore.service;

import com.example.onlinestore.bean.Address;

import java.util.List;

/**
 * 地址服务接口
 */
public interface AddressService {

    /**
     * 根据ID查询地址信息
     *
     * @param id 地址ID
     * @return 地址信息
     */
    Address getById(Long id);

    /**
     * 根据用户ID查询地址列表
     *
     * @param memberId 用户ID
     * @return 地址列表
     */
    List<Address> getByMemberId(Long memberId);

    /**
     * 查询用户的默认地址
     *
     * @param memberId 用户ID
     * @return 默认地址信息
     */
    Address getDefaultByMemberId(Long memberId);

    /**
     * 创建地址
     *
     * @param address 地址信息
     * @return 创建成功的地址信息
     */
    Address create(Address address);

    /**
     * 更新地址信息
     *
     * @param address 地址信息
     * @return 更新后的地址信息
     */
    Address update(Address address);

    /**
     * 删除地址
     *
     * @param id       地址ID
     * @param memberId 用户ID
     */
    void delete(Long id, Long memberId);

    /**
     * 设置默认地址
     *
     * @param id       地址ID
     * @param memberId 用户ID
     */
    void setDefault(Long id, Long memberId);

    /**
     * 验证地址是否属于指定用户
     *
     * @param id       地址ID
     * @param memberId 用户ID
     * @return 是否属于该用户
     */
    boolean validateOwnership(Long id, Long memberId);

    /**
     * 检查用户是否可以添加更多地址
     *
     * @param memberId 用户ID
     * @return 是否可以添加
     */
    boolean canAddMore(Long memberId);
}