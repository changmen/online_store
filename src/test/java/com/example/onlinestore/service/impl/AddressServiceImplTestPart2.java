package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AddressServiceImpl setDefaultAddress 方法的补充测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AddressServiceImpl setDefaultAddress 方法测试")
class AddressServiceImplTestPart2 {

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    private AddressEntity testAddress;
    private OrderEntity testOrder;

    @BeforeEach
    void setUp() {
        reset(addressMapper, orderMapper);
        
        testAddress = createTestAddress(1L, 1L, "张三", "13800138001",
                "北京市", "朝阳区", "望京街道", "SOHO现代城A座1001", false);
        
        testOrder = createTestOrder(1L, 1L, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
    }

    private AddressEntity createTestAddress(Long id, Long userId, String receiverName, 
            String receiverPhone, String province, String city, String district, 
            String detailAddress, Boolean isDefault) {
        AddressEntity address = new AddressEntity();
        address.setId(id);
        address.setUserId(userId);
        address.setReceiverName(receiverName);
        address.setReceiverPhone(receiverPhone);
        address.setProvince(province);
        address.setCity(city);
        address.setDistrict(district);
        address.setDetailAddress(detailAddress);
        address.setIsDefault(isDefault);
        address.setPostcode("100000");
        address.setTag("家");
        return address;
    }

    private OrderEntity createTestOrder(Long id, Long userId, String receiverName, 
            String receiverPhone, String receiverAddress, Integer status) {
        OrderEntity order = new OrderEntity();
        order.setId(id);
        order.setOrderNo("ORDER" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        order.setStatus(status);
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setPayAmount(new BigDecimal("100.00"));
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }

    // ============================= setDefaultAddress 方法测试 =============================

    @Test
    @DisplayName("设置存在的地址为默认 - 应该清除旧默认、设置新默认、同步订单")
    void setDefaultAddress_ExistingAddress_ShouldClearOldDefaultSetNewDefaultAndSyncOrders() {
        // Given
        Long addressId = 1L;
        Long userId = 1L;
        
        when(addressMapper.findById(addressId)).thenReturn(testAddress);
        when(orderMapper.findByCondition(userId, 0, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(Arrays.asList(testOrder));
        
        // When
        addressService.setDefaultAddress(addressId, userId);
        
        // Then
        // 验证清除默认地址被调用
        verify(addressMapper, times(1)).clearDefaultAddress(userId);
        // 验证设置默认地址被调用
        verify(addressMapper, times(1)).setDefaultAddress(addressId, userId);
        // 验证查询地址被调用
        verify(addressMapper, times(1)).findById(addressId);
        
        // 验证订单查询被调用
        verify(orderMapper, times(1)).findByCondition(userId, 0, null, null, 0, Integer.MAX_VALUE);
        // 验证订单更新被调用（一个匹配的订单）
        verify(orderMapper, times(1)).updateOrder(any(OrderEntity.class));
        
        // 验证调用顺序
        var inOrder = inOrder(addressMapper, orderMapper);
        inOrder.verify(addressMapper).clearDefaultAddress(userId);
        inOrder.verify(addressMapper).setDefaultAddress(addressId, userId);
        inOrder.verify(addressMapper).findById(addressId);
        inOrder.verify(orderMapper).findByCondition(userId, 0, null, null, 0, Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("设置不存在的地址为默认 - 应该清除旧默认并设置操作，不执行订单同步")
    void setDefaultAddress_NonExistentAddress_ShouldClearOldDefaultAndSetWithoutOrderSync() {
        // Given
        Long nonExistentId = 999L;
        Long userId = 1L;
        
        when(addressMapper.findById(nonExistentId)).thenReturn(null);
        
        // When
        addressService.setDefaultAddress(nonExistentId, userId);
        
        // Then
        // 验证清除默认地址被调用
        verify(addressMapper, times(1)).clearDefaultAddress(userId);
        // 验证设置默认地址被调用（即使地址不存在）
        verify(addressMapper, times(1)).setDefaultAddress(nonExistentId, userId);
        // 验证查询地址被调用
        verify(addressMapper, times(1)).findById(nonExistentId);
        
        // 验证没有订单查询和更新（因为地址不存在）
        verifyNoInteractions(orderMapper);
    }

    @Test
    @DisplayName("设置默认地址时同步相关订单 - 应该正确更新匹配订单的收货信息")
    void setDefaultAddress_SyncRelatedOrders_ShouldCorrectlyUpdateMatchingOrdersShippingInfo() {
        // Given
        Long addressId = 1L;
        Long userId = 1L;
        
        AddressEntity newDefaultAddress = createTestAddress(addressId, userId, "张三新名字", "13822138001",
                "北京市", "朝阳区", "望京街道", "SOHO现代城B座2001", false);
        
        // 创建匹配和不匹配的订单
        OrderEntity matchingOrder1 = createTestOrder(1L, userId, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
        OrderEntity matchingOrder2 = createTestOrder(2L, userId, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
        OrderEntity nonMatchingOrder = createTestOrder(3L, userId, "李四", "13900139001",
                "上海市 浦东新区 陆家嘴街道 环球金融中心88层", 0);
        
        List<OrderEntity> allOrders = Arrays.asList(matchingOrder1, matchingOrder2, nonMatchingOrder);
        
        when(addressMapper.findById(addressId)).thenReturn(newDefaultAddress);
        when(orderMapper.findByCondition(userId, 0, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(allOrders);
        
        // When
        addressService.setDefaultAddress(addressId, userId);
        
        // Then
        // 验证订单查询被调用
        verify(orderMapper, times(1)).findByCondition(userId, 0, null, null, 0, Integer.MAX_VALUE);
        
        // 捕获订单更新调用的参数
        ArgumentCaptor<OrderEntity> orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderMapper, times(2)).updateOrder(orderCaptor.capture());
        
        List<OrderEntity> updatedOrders = orderCaptor.getAllValues();
        
        // 验证更新的订单信息
        for (OrderEntity updatedOrder : updatedOrders) {
            assertEquals("张三新名字", updatedOrder.getReceiverName(), "收货人姓名应该更新");
            assertEquals("13822138001", updatedOrder.getReceiverPhone(), "收货人电话应该更新");
            assertEquals("北京市 朝阳区 望京街道 SOHO现代城B座2001", 
                updatedOrder.getReceiverAddress(), "收货地址应该更新");
        }
    }

    @Test
    @DisplayName("设置默认地址时无相关订单 - 应该正常执行设置操作但不进行订单更新")
    void setDefaultAddress_NoRelatedOrders_ShouldExecuteSetOperationWithoutOrderUpdate() {
        // Given
        Long addressId = 2L;
        Long userId = 2L;
        
        AddressEntity addressWithoutOrders = createTestAddress(addressId, userId, "李四", "13900139001",
                "上海市", "浦东新区", "陆家嘴街道", "环球金融中心88层", false);
        
        when(addressMapper.findById(addressId)).thenReturn(addressWithoutOrders);
        when(orderMapper.findByCondition(userId, 0, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(Collections.emptyList());
        
        // When
        addressService.setDefaultAddress(addressId, userId);
        
        // Then
        // 验证清除和设置默认地址被调用
        verify(addressMapper, times(1)).clearDefaultAddress(userId);
        verify(addressMapper, times(1)).setDefaultAddress(addressId, userId);
        verify(addressMapper, times(1)).findById(addressId);
        
        // 验证订单查询被调用
        verify(orderMapper, times(1)).findByCondition(userId, 0, null, null, 0, Integer.MAX_VALUE);
        // 验证没有订单更新（因为没有相关订单）
        verify(orderMapper, never()).updateOrder(any(OrderEntity.class));
    }

    @Test
    @DisplayName("使用null参数设置默认地址 - 应该正常处理并委托给mapper")
    void setDefaultAddress_WithNullParameters_ShouldDelegateToMapperAndHandleNormally() {
        // Given
        Long nullAddressId = null;
        Long nullUserId = null;
        
        when(addressMapper.findById(nullAddressId)).thenReturn(null);
        
        // When
        addressService.setDefaultAddress(nullAddressId, nullUserId);
        
        // Then
        // 验证清除默认地址被调用
        verify(addressMapper, times(1)).clearDefaultAddress(nullUserId);
        // 验证设置默认地址被调用
        verify(addressMapper, times(1)).setDefaultAddress(nullAddressId, nullUserId);
        // 验证查询地址被调用
        verify(addressMapper, times(1)).findById(nullAddressId);
        
        // 验证没有订单操作（因为地址不存在）
        verifyNoInteractions(orderMapper);
    }

    @Test
    @DisplayName("验证setDefaultAddress方法的事务注解 - 方法应该支持事务管理")
    void setDefaultAddress_TransactionalBehavior_ShouldSupportTransactionManagement() {
        // Given
        Long addressId = 10L;
        Long userId = 10L;
        
        AddressEntity testAddress = createTestAddress(addressId, userId, "测试用户", "13800138000",
                "测试省", "测试市", "测试区", "测试详细地址", false);
        
        when(addressMapper.findById(addressId)).thenReturn(testAddress);
        when(orderMapper.findByCondition(userId, 0, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(Collections.emptyList());
        
        // When
        addressService.setDefaultAddress(addressId, userId);
        
        // Then
        // 验证所有mapper方法调用
        verify(addressMapper, times(1)).clearDefaultAddress(userId);
        verify(addressMapper, times(1)).setDefaultAddress(addressId, userId);
        verify(addressMapper, times(1)).findById(addressId);
        verify(orderMapper, times(1)).findByCondition(userId, 0, null, null, 0, Integer.MAX_VALUE);
        
        // 注意：实际的事务行为需要在集成测试中验证
        // 这里主要验证业务逻辑的正确性
    }

    @Test
    @DisplayName("验证地址格式化方法的间接测试 - 通过订单同步验证formatAddress方法")
    void setDefaultAddress_FormatAddressIndirectTest_VerifyFormatAddressThroughOrderSync() {
        // Given
        Long addressId = 1L;
        Long userId = 1L;
        
        AddressEntity addressWithSpecialFormat = createTestAddress(addressId, userId, "特殊格式测试", "13700137000",
                "特殊省份", "特殊城市", "特殊区县", "特殊详细地址信息", false);
        
        OrderEntity orderToUpdate = createTestOrder(1L, userId, "特殊格式测试", "13700137000",
                "特殊省份 特殊城市 特殊区县 特殊详细地址信息", 0);
        
        when(addressMapper.findById(addressId)).thenReturn(addressWithSpecialFormat);
        when(orderMapper.findByCondition(userId, 0, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(Arrays.asList(orderToUpdate));
        
        // When
        addressService.setDefaultAddress(addressId, userId);
        
        // Then
        ArgumentCaptor<OrderEntity> orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderMapper, times(1)).updateOrder(orderCaptor.capture());
        
        OrderEntity updatedOrder = orderCaptor.getValue();
        
        // 验证地址格式化的结果（省 市 区 详细地址的组合格式）
        String expectedFormattedAddress = "特殊省份 特殊城市 特殊区县 特殊详细地址信息";
        assertEquals(expectedFormattedAddress, updatedOrder.getReceiverAddress(), 
            "地址格式化应该按照省市区详细地址的格式");
    }
}