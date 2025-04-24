package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.AddressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {
    /**
     * 根据ID查找地址实体。
     *
     * @param id 地址ID
     * @return 匹配的地址实体，未找到时返回null
     */
    AddressEntity findById(Long id);

    /**
     * 根据会员ID查找所有地址实体。
     *
     * @param memberId 会员ID
     * @return 该会员的所有地址实体列表
     */
    List<AddressEntity> findByMemberId(Long memberId);

    /**
     * 根据会员ID查找默认地址实体。
     *
     * @param memberId 会员ID
     * @return 该会员的默认地址实体，未找到时返回null
     */
    AddressEntity findDefaultByMemberId(Long memberId);

    /**
     * 插入新的地址实体。
     *
     * @param address 待插入的地址实体
     * @return 受影响的行数，成功插入返回1
     */
    int insert(AddressEntity address);

    /**
     * 更新地址实体信息。
     *
     * @param address 包含更新后信息的地址实体
     * @return 受影响的行数，成功更新返回1
     */
    int update(AddressEntity address);

    /**
     * 根据ID删除地址实体。
     *
     * @param id 待删除的地址ID
     * @return 受影响的行数，成功删除返回1
     */
    int delete(Long id);

    /**
     * 更新指定会员的默认地址状态。
     *
     * @param memberId 会员ID
     * @param addressId 待设为默认的地址ID
     * @return 受影响的行数，成功更新返回1
     */
    int updateDefaultStatus(@Param("memberId") Long memberId, @Param("addressId") Long addressId);

    /**
     * 根据ID和会员ID查找地址实体。
     *
     * @param id 地址ID
     * @param memberId 会员ID
     * @return 匹配的地址实体，未找到时返回null
     */
    AddressEntity findByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);
}