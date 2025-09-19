package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.AddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 地址数据访问层接口
 */
@Mapper
public interface AddressMapper {
    
    /**
     * 插入地址记录
     * 
     * @param addressEntity 地址实体
     * @return 影响的记录数
     */
    int insertAddress(AddressEntity addressEntity);
    
    /**
     * 根据ID查询地址
     * 
     * @param id 地址ID
     * @return 地址实体
     */
    AddressEntity findById(@Param("id") Long id);
    
    /**
     * 根据用户ID和地址ID查询地址（用于权限控制）
     * 
     * @param id 地址ID
     * @param memberId 用户ID
     * @return 地址实体
     */
    AddressEntity findByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);
    
    /**
     * 根据用户ID查询地址列表
     * 
     * @param memberId 用户ID
     * @return 地址实体列表
     */
    List<AddressEntity> findByMemberId(@Param("memberId") Long memberId);
    
    /**
     * 根据用户ID查询默认地址
     * 
     * @param memberId 用户ID
     * @return 默认地址实体
     */
    AddressEntity findDefaultByMemberId(@Param("memberId") Long memberId);
    
    /**
     * 更新地址信息
     * 
     * @param addressEntity 地址实体
     * @return 影响的记录数
     */
    int updateAddress(AddressEntity addressEntity);
    
    /**
     * 取消用户的所有默认地址
     * 
     * @param memberId 用户ID
     * @return 影响的记录数
     */
    int clearDefaultAddress(@Param("memberId") Long memberId);
    
    /**
     * 逻辑删除地址
     * 
     * @param id 地址ID
     * @param memberId 用户ID
     * @return 影响的记录数
     */
    int deleteAddress(@Param("id") Long id, @Param("memberId") Long memberId);
    
    /**
     * 统计用户地址数量
     * 
     * @param memberId 用户ID
     * @return 地址数量
     */
    int countByMemberId(@Param("memberId") Long memberId);
    
    /**
     * 检查地址是否存在
     * 
     * @param id 地址ID
     * @param memberId 用户ID
     * @return 是否存在
     */
    boolean existsByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);
}