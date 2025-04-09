package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.CartEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {
    /**
     * 插入新的购物车项
     *
     * @param cartEntity 待插入的购物车项
     * @return 受影响的行数
     */
    int insert(CartEntity cartEntity);

    /**
     * 更新购物车项
     *
     * @param cartEntity 待更新的购物车项
     * @return 受影响的行数
     */
    int update(CartEntity cartEntity);

    /**
     * 删除购物车项
     *
     * @param id 购物车项ID
     * @return 受影响的行数
     */
    int deleteById(Long id);

    /**
     * 根据ID查询购物车项
     *
     * @param id 购物车项ID
     * @return 购物车项
     */
    CartEntity findById(Long id);

    /**
     * 根据会员ID查询购物车项列表
     *
     * @param memberId 会员ID
     * @return 购物车项列表
     */
    List<CartEntity> findByMemberId(Long memberId);

    /**
     * 根据会员ID和SKU ID查询购物车项
     *
     * @param memberId 会员ID
     * @param skuId SKU ID
     * @return 购物车项
     */
    CartEntity findByMemberIdAndSkuId(@Param("memberId") Long memberId, @Param("skuId") Long skuId);

    /**
     * 更新购物车项数量
     *
     * @param id       购物车项ID
     * @param quantity 新的数量
     */
    void updateQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

    /**
     * 更新购物车项选中状态
     *
     * @param id       购物车项ID
     * @param selected 选中状态（0-未选中，1-已选中）
     */
    void updateSelected(@Param("id") Long id, @Param("selected") Integer selected);

    /**
     * 清空会员的购物车
     *
     * @param memberId 会员ID
     * @return 受影响的行数
     */
    int clearByMemberId(Long memberId);
} 