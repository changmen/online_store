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
     * 插入新地址
     * 
     * @param address 地址实体
     * @return 受影响的行数
     */
    int insertAddress(AddressEntity address);
    
    /**
     * 根据ID查询地址
     * 
     * @param id 地址ID
     * @return 地址实体，不存在时返回null
     */
    AddressEntity findById(Long id);
    
    /**
     * 根据用户ID查询所有地址
     * 
     * @param memberId 用户ID
     * @return 地址列表，按创建时间倒序排列
     */
    List<AddressEntity> findByMemberId(Long memberId);
    
    /**
     * 根据用户ID查询默认地址
     * 
     * @param memberId 用户ID
     * @return 默认地址实体，不存在时返回null
     */
    AddressEntity findDefaultByMemberId(Long memberId);
    
    /**
     * 根据用户ID分页查询地址
     * 
     * @param memberId 用户ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 地址列表
     */
    List<AddressEntity> findByMemberIdWithPaging(@Param("memberId") Long memberId, 
                                                  @Param("offset") int offset, 
                                                  @Param("limit") int limit);
    
    /**
     * 统计用户地址数量
     * 
     * @param memberId 用户ID
     * @return 地址数量
     */
    int countByMemberId(Long memberId);
    
    /**
     * 更新地址信息
     * 
     * @param address 地址实体
     * @return 受影响的行数
     */
    int updateAddress(AddressEntity address);
    
    /**
     * 删除地址
     * 
     * @param id 地址ID
     * @return 受影响的行数
     */
    int deleteById(Long id);
    
    /**
     * 取消用户的所有默认地址标记
     * 
     * @param memberId 用户ID
     * @return 受影响的行数
     */
    int clearDefaultByMemberId(Long memberId);
    
    /**
     * 设置默认地址
     * 
     * @param id 地址ID
     * @param memberId 用户ID
     * @return 受影响的行数
     */
    int setAsDefault(@Param("id") Long id, @Param("memberId") Long memberId);
    
    /**
     * 检查地址是否属于指定用户
     * 
     * @param id 地址ID
     * @param memberId 用户ID
     * @return 存在返回true，否则返回false
     */
    boolean existsByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);
}