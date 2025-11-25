package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.AddressEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {
    
    /**
     * 根据ID查找地址
     */
    AddressEntity findById(Long id);
    
    /**
     * 根据用户ID查找所有地址
     */
    List<AddressEntity> findByMemberId(Long memberId);
    
    /**
     * 插入新地址
     */
    int insertAddress(AddressEntity address);
    
    /**
     * 更新地址信息
     */
    int updateAddress(AddressEntity address);
    
    /**
     * 删除地址
     */
    int deleteAddress(Long id);
    
    /**
     * 根据用户ID获取默认地址
     */
    AddressEntity findDefaultByMemberId(Long memberId);
    
    /**
     * 将用户的所有地址设为非默认
     */
    int clearDefaultByMemberId(Long memberId);
}