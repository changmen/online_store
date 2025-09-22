package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.AddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {

    /**
     * 插入新地址
     * 
     * @param address 地址实体
     * @return 影响的行数
     */
    int insert(AddressEntity address);

    /**
     * 根据ID删除地址
     * 
     * @param id 地址ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 更新地址信息
     * 
     * @param address 地址实体
     * @return 影响的行数
     */
    int update(AddressEntity address);

    /**
     * 根据ID查询地址
     * 
     * @param id 地址ID
     * @return 地址实体
     */
    AddressEntity selectById(@Param("id") Long id);

    /**
     * 根据会员ID查询所有地址
     * 
     * @param memberId 会员ID
     * @return 地址列表
     */
    List<AddressEntity> selectByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据会员ID查询默认地址
     * 
     * @param memberId 会员ID
     * @return 默认地址实体
     */
    AddressEntity selectDefaultByMemberId(@Param("memberId") Long memberId);

    /**
     * 清除会员的所有默认地址标记
     * 
     * @param memberId 会员ID
     * @return 影响的行数
     */
    int clearDefaultByMemberId(@Param("memberId") Long memberId);

    /**
     * 设置指定地址为默认地址
     * 
     * @param id 地址ID
     * @param memberId 会员ID
     * @return 影响的行数
     */
    int setAsDefault(@Param("id") Long id, @Param("memberId") Long memberId);

    /**
     * 根据会员ID统计地址数量
     * 
     * @param memberId 会员ID
     * @return 地址数量
     */
    int countByMemberId(@Param("memberId") Long memberId);
}