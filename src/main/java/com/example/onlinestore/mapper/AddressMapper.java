package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.AddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 地址数据访问接口
 */
@Mapper
public interface AddressMapper {
    
    /**
     * 根据ID查询地址
     */
    AddressEntity findById(@Param("id") Long id);
    
    /**
     * 根据用户ID查询地址列表
     */
    List<AddressEntity> findByMemberId(@Param("memberId") Long memberId);
    
    /**
     * 查询用户的默认地址
     */
    AddressEntity findDefaultByMemberId(@Param("memberId") Long memberId);
    
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
    int deleteById(@Param("id") Long id);
    
    /**
     * 取消用户的所有默认地址
     */
    int clearDefaultByMemberId(@Param("memberId") Long memberId);
    
    /**
     * 设置默认地址
     */
    int setDefaultAddress(@Param("id") Long id, @Param("memberId") Long memberId);
    
    /**
     * 统计用户地址数量
     */
    int countByMemberId(@Param("memberId") Long memberId);
}