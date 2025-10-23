package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.AddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 地址信息数据访问层接口
 */
@Mapper
public interface AddressMapper {

    /**
     * 根据ID查询地址信息
     *
     * @param id 地址ID
     * @return 地址实体对象
     */
    AddressEntity selectById(@Param("id") Long id);

    /**
     * 根据用户ID查询地址列表
     *
     * @param memberId 用户ID
     * @return 地址实体列表
     */
    List<AddressEntity> selectByMemberId(@Param("memberId") Long memberId);

    /**
     * 查询用户的默认地址
     *
     * @param memberId 用户ID
     * @return 默认地址实体对象
     */
    AddressEntity selectDefaultByMemberId(@Param("memberId") Long memberId);

    /**
     * 插入地址信息
     *
     * @param addressEntity 地址实体对象
     * @return 插入的记录数
     */
    int insert(AddressEntity addressEntity);

    /**
     * 更新地址信息
     *
     * @param addressEntity 地址实体对象
     * @return 更新的记录数
     */
    int update(AddressEntity addressEntity);

    /**
     * 根据ID删除地址信息（逻辑删除）
     *
     * @param id 地址ID
     * @return 删除的记录数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 清除用户所有地址的默认标识
     *
     * @param memberId 用户ID
     * @return 更新的记录数
     */
    int clearDefaultByMemberId(@Param("memberId") Long memberId);

    /**
     * 设置指定地址为默认地址
     *
     * @param id 地址ID
     * @return 更新的记录数
     */
    int setDefaultById(@Param("id") Long id);

    /**
     * 根据用户ID和地址ID查询地址信息
     *
     * @param id       地址ID
     * @param memberId 用户ID
     * @return 地址实体对象
     */
    AddressEntity selectByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);

    /**
     * 统计用户地址数量
     *
     * @param memberId 用户ID
     * @return 地址数量
     */
    int countByMemberId(@Param("memberId") Long memberId);
}