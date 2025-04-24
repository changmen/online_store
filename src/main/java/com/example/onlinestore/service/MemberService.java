package com.example.onlinestore.service;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.bean.Member;
import com.example.onlinestore.bean.PointRecord;
import com.example.onlinestore.bean.PointRule;
import com.example.onlinestore.dto.AddressRequest;
import com.example.onlinestore.dto.LoginRequest;
import com.example.onlinestore.dto.LoginResponse;
import com.example.onlinestore.dto.MemberRegistryRequest;
import com.example.onlinestore.enums.PointRuleStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public interface MemberService {
    /**
     * 处理用户登录请求
     *
     * @param request 包含登录凭证的请求对象，需通过参数校验（@Valid）
     *                应包含username和password字段
     * @return 登录响应对象，包含：
     * - 登录成功状态
     * - JWT令牌（成功时）
     * - 错误信息（失败时）
     */
    LoginResponse login(@Valid LoginRequest request);

    /**
     * 注册新会员
     *
     * @param request 会员注册请求对象，需通过参数校验（@Valid）
     *                包含必填字段：用户名、密码、联系方式等
     * @return 持久化后的会员对象
     * @throws DataIntegrityViolationException 当用户名已存在时抛出
     */
    Member registry(@Valid MemberRegistryRequest request);

    /**
     * 根据会员ID获取会员信息
     *
     * @param id 会员唯一标识符，不能为null（@NotNull）
     *           应符合数据库主键约束
     * @return 匹配的会员对象，未找到时返回null
     */
    Member getMemberById(@NotNull Long id);

    /**
     * 根据用户名获取会员信息
     *
     * @param name 会员登录用户名，不能为null（@NotNull）
     *             需完全匹配大小写
     * @return 匹配的会员对象，未找到时返回null
     */
    Member getMemberByName(@NotNull String name);

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     * @throws com.example.onlinestore.exceptions.BizException 如果未登录，抛出异常
     */
    Member getLoginMember();

    // Address接口

    /**
     * 添加收货地址
     *
     * @param request 收货地址请求
     * @return 添加后的收货地址
     * @throws com.example.onlinestore.exceptions.BizException 或者操作DB失败的时候
     */
    Address addAddress(@NotNull @Valid AddressRequest request);

    /**
     * 更新收货地址
     *
     * @param id      地址ID
     * @param request 收货地址请求
     * @return 更新后的收货地址
     * @throws com.example.onlinestore.exceptions.BizException 收货地址不存在,或者操作DB失败的时候
     */
    Address updateAddress(@NotNull @Min(value = 1, message = "地址ID必须大于0") Long id, @NotNull @Valid AddressRequest request);

    /**
     * 删除收货地址
     *
     * @param id       收货地址ID
     * @param memberId 会员ID
     * @throws com.example.onlinestore.exceptions.BizException 收货地址不存在,或者操作DB失败的时候
     */
    void deleteAddress(@NotNull @Min(value = 1, message = "地址ID必须大于0") Long id, @NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId);

    /**
     * 获取会员的所有收货地址
     *
     * @param memberId 会员ID
     * @return 收货地址列表
     * @throws com.example.onlinestore.exceptions.BizException 收货地址不存在,或者操作DB失败的时候
     */
    List<Address> getAddressesByMemberId(@NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId);

    /**
     * 获取会员的默认收货地址
     *
     * @param memberId 会员ID
     * @return 默认收货地址
     * @throws com.example.onlinestore.exceptions.BizException 收货地址不存在,或者操作DB失败的时候
     */
    Address getDefaultAddress(@NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId);

    /**
     * 设置默认收货地址
     *
     * @param addressId 收货地址ID
     * @param memberId  会员ID
     * @throws com.example.onlinestore.exceptions.BizException 收货地址不存在,或者操作DB失败的时候
     */
    void setDefaultAddress(@NotNull @Min(value = 1, message = "地址ID必须大于0") Long addressId, @NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId);

    /**
     * 根据ID获取收货地址
     *
     * @param id 收货地址ID
     * @return 收货地址
     * @throws com.example.onlinestore.exceptions.BizException 收货地址不存在,或者查询DB失败的时候
     */
    Address getAddressById(@NotNull @Min(value = 1, message = "地址ID必须大于0") Long id);

    //会员积分

    /**
     * 创建积分规则
     *
     * @param name        规则名称
     * @param description 规则描述
     * @param points      积分数量
     * @return 创建的积分规则
     * @throws com.example.onlinestore.exceptions.BizException 当操作DB失败的时候，抛出该异常
     */
    PointRule createRule(@NotNull @Size(max = 64, message = "规则名称长度不能超过64个字符") String name,
                         @NotNull @Size(max = 128, message = "规则描述长度不能超过128个字符") String description,
                         @NotNull @DecimalMin(value = "1", message = "积分数量必须大于0") BigDecimal points);

    /**
     * 更新积分规则状态
     *
     * @param ruleId 规则ID
     * @param status 新状态
     */
    void updateRuleStatus(@NotNull @Min(value = 1, message = "规则ID必须大于0") Long ruleId, @NotNull PointRuleStatus status);

    /**
     * 获取会员积分余额
     *
     * @param memberId 会员ID
     * @return 积分余额
     */
    BigDecimal getMemberPointBalance(@NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId);

    /**
     * 获取会员积分记录
     *
     * @param memberId 会员ID
     * @return 积分记录列表
     */
    List<PointRecord> getMemberPointRecords(@NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId);

    /**
     * 增加积分
     *
     * @param memberId    会员ID
     * @param orderNo     订单号
     * @param points      积分数量
     * @param description 描述
     */
    void earnPoints(@NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId,
                    @NotBlank(message = "订单号不能为空") @Size(max = 32, message = "订单号长度不能超过32个字符") String orderNo,
                    @NotNull @DecimalMin(value = "0", inclusive = false, message = "积分必须大于0") BigDecimal points,
                    @NotNull @Size(max = 128, message = "描述长度不能超过128个字符") String description);

    /**
     * 消费积分
     *
     * @param memberId    会员ID
     * @param orderNo     订单号
     * @param points      积分数量
     * @param description 描述
     */
    void consumePoints(@NotNull @Min(value = 1, message = "会员ID必须大于0") Long memberId,
                       @NotBlank(message = "订单号不能为空") @Size(max = 32, message = "订单号长度不能超过32个字符") String orderNo,
                       @NotNull @DecimalMin(value = "0", inclusive = false, message = "积分必须大于0") BigDecimal points,
                       @NotNull @Size(max = 128, message = "描述长度不能超过128个字符") String description);

}