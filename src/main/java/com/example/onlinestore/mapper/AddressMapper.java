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
     * 插入地址记录
     */
    int insertAddress(AddressEntity addressEntity);

    /**
     * 根据ID查询地址
     */
    AddressEntity findById(@Param("id") Long id);

    /**
     * 根据用户ID查询所有地址
     */
    List<AddressEntity> findByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据用户ID查询默认地址
     */
    AddressEntity findDefaultByMemberId(@Param("memberId") Long memberId);

    /**
     * 更新地址信息
     */
    int updateAddress(AddressEntity addressEntity);

    /**
     * 根据ID删除地址
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据用户ID和地址ID删除地址
     */
    int deleteByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);

    /**
     * 将用户的所有地址设置为非默认
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