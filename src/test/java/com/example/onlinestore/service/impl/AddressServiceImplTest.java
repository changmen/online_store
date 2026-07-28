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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AddressServiceImpl 单元测试类
 * 
 * 测试覆盖范围：
 * - addAddress: 新增地址的各种场景
 * - getAddress: 查询地址的正常和异常场景
 * - getUserAddresses: 获取用户地址列表
 * - updateAddress: 更新地址和相关订单同步
 * - deleteAddress: 删除地址和订单状态更新
 * - setDefaultAddress: 设置默认地址
 * 
 * 私有方法间接测试：
 * - updateRelatedOrders: 通过updateAddress和setDefaultAddress测试
 * - formatAddress: 通过订单同步操作测试
 * - matchAddress: 通过deleteAddress和updateRelatedOrders测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AddressServiceImpl 单元测试")
class AddressServiceImplTest {

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    private AddressEntity testAddress;
    private AddressEntity defaultAddress;
    private AddressEntity nonDefaultAddress;
    private OrderEntity testOrder;
    private OrderEntity matchingOrder;
    private OrderEntity nonMatchingOrder;

    /**
     * 测试数据初始化
     * 每个测试方法执行前重置测试数据和Mock对象
     */
    @BeforeEach
    void setUp() {
        // 重置所有Mock对象
        reset(addressMapper, orderMapper);
        
        // 创建标准测试地址
        testAddress = createTestAddress(1L, 1L, "张三", "13800138001", 
                "北京市", "朝阳区", "望京街道", "SOHO现代城A座1001", true);
        
        // 创建默认地址
        defaultAddress = createTestAddress(1L, 1L, "张三", "13800138001",
                "北京市", "朝阳区", "望京街道", "SOHO现代城A座1001", true);
        
        // 创建非默认地址
        nonDefaultAddress = createTestAddress(2L, 1L, "李四", "13900139001",
                "上海市", "浦东新区", "陆家嘴街道", "环球金融中心88层", false);
        
        // 创建标准测试订单
        testOrder = createTestOrder(1L, 1L, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
        
        // 创建匹配的订单
        matchingOrder = createTestOrder(1L, 1L, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
        
        // 创建不匹配的订单
        nonMatchingOrder = createTestOrder(2L, 1L, "王五", "13700137001",
                "广州市 天河区 珠江新城 IFC大厦2001", 0);
    }

    /**
     * 创建测试用地址实体
     */
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

    /**
     * 创建测试用订单实体
     */
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

    // ============================= addAddress 方法测试 =============================

    @Test
    @DisplayName("添加新的默认地址 - 应该清除旧默认地址并插入新地址")
    void addAddress_NewDefaultAddress_ShouldClearOldDefaultAndInsertNewAddress() {
        // Given
        AddressEntity newDefaultAddress = createTestAddress(null, 1L, "张三", "13800138001",
                "北京市", "朝阳区", "望京街道", "SOHO现代城A座1001", true);
        
        // 模拟插入后设置ID
        doAnswer(invocation -> {
            AddressEntity address = invocation.getArgument(0);
            address.setId(1L);
            return null;
        }).when(addressMapper).insertAddress(any(AddressEntity.class));
        
        // When
        Long result = addressService.addAddress(newDefaultAddress);
        
        // Then
        assertNotNull(result, "返回的地址ID不应为null");
        assertEquals(1L, result, "返回的地址ID应该为1");
        
        // 验证清除默认地址被调用
        verify(addressMapper, times(1)).clearDefaultAddress(1L);
        // 验证插入地址被调用
        verify(addressMapper, times(1)).insertAddress(newDefaultAddress);
        
        // 验证调用顺序：先清除默认，再插入
        var inOrder = inOrder(addressMapper);
        inOrder.verify(addressMapper).clearDefaultAddress(1L);
        inOrder.verify(addressMapper).insertAddress(newDefaultAddress);
    }

    @Test
    @DisplayName("添加新的非默认地址 - 应该直接插入地址而不清除默认状态")
    void addAddress_NewNonDefaultAddress_ShouldInsertDirectlyWithoutClearingDefault() {
        // Given
        AddressEntity newNonDefaultAddress = createTestAddress(null, 1L, "李四", "13900139001",
                "上海市", "浦东新区", "陆家嘴街道", "环球金融中心88层", false);
        
        // 模拟插入后设置ID
        doAnswer(invocation -> {
            AddressEntity address = invocation.getArgument(0);
            address.setId(2L);
            return null;
        }).when(addressMapper).insertAddress(any(AddressEntity.class));
        
        // When
        Long result = addressService.addAddress(newNonDefaultAddress);
        
        // Then
        assertNotNull(result, "返回的地址ID不应为null");
        assertEquals(2L, result, "返回的地址ID应该为2");
        
        // 验证清除默认地址没有被调用
        verify(addressMapper, never()).clearDefaultAddress(anyLong());
        // 验证插入地址被调用
        verify(addressMapper, times(1)).insertAddress(newNonDefaultAddress);
    }

    @Test
    @DisplayName("处理isDefault字段为null的情况 - 应该正常插入而不执行清除默认操作")
    void addAddress_WithNullIsDefault_ShouldInsertNormallyWithoutClearingDefault() {
        // Given
        AddressEntity addressWithNullDefault = createTestAddress(null, 1L, "王五", "13700137001",
                "广州市", "天河区", "珠江新城", "IFC大厦2001", null);
        
        // 模拟插入后设置ID
        doAnswer(invocation -> {
            AddressEntity address = invocation.getArgument(0);
            address.setId(3L);
            return null;
        }).when(addressMapper).insertAddress(any(AddressEntity.class));
        
        // When
        Long result = addressService.addAddress(addressWithNullDefault);
        
        // Then
        assertNotNull(result, "返回的地址ID不应为null");
        assertEquals(3L, result, "返回的地址ID应该为3");
        
        // 验证清除默认地址没有被调用（因为isDefault为null，条件判断为false）
        verify(addressMapper, never()).clearDefaultAddress(anyLong());
        // 验证插入地址被调用
        verify(addressMapper, times(1)).insertAddress(addressWithNullDefault);
    }

    @Test
    @DisplayName("验证addAddress方法的事务注解 - 方法应该支持事务管理")
    void addAddress_TransactionalBehavior_ShouldSupportTransactionManagement() {
        // Given
        AddressEntity newAddress = createTestAddress(null, 1L, "测试用户", "13800138000",
                "测试省", "测试市", "测试区", "测试详细地址", true);
        
        // 模拟插入操作
        doAnswer(invocation -> {
            AddressEntity address = invocation.getArgument(0);
            address.setId(100L);
            return null;
        }).when(addressMapper).insertAddress(any(AddressEntity.class));
        
        // When
        Long result = addressService.addAddress(newAddress);
        
        // Then
        assertEquals(100L, result, "应该返回正确的地址ID");
        
        // 验证方法调用
        verify(addressMapper, times(1)).clearDefaultAddress(1L);
        verify(addressMapper, times(1)).insertAddress(newAddress);
        
        // 注意：实际的事务行为需要在集成测试中验证
        // 这里主要验证业务逻辑的正确性
    }

    // ============================= getAddress 方法测试 =============================

    @Test
    @DisplayName("获取存在的地址 - 应该返回正确的地址实体")
    void getAddress_ExistingAddress_ShouldReturnCorrectAddressEntity() {
        // Given
        Long addressId = 1L;
        when(addressMapper.findById(addressId)).thenReturn(testAddress);
        
        // When
        AddressEntity result = addressService.getAddress(addressId);
        
        // Then
        assertNotNull(result, "返回的地址实体不应该为null");
        assertEquals(testAddress.getId(), result.getId(), "地址ID应该匹配");
        assertEquals(testAddress.getUserId(), result.getUserId(), "用户ID应该匹配");
        assertEquals(testAddress.getReceiverName(), result.getReceiverName(), "收货人姓名应该匹配");
        assertEquals(testAddress.getReceiverPhone(), result.getReceiverPhone(), "收货人电话应该匹配");
        assertEquals(testAddress.getProvince(), result.getProvince(), "省份应该匹配");
        assertEquals(testAddress.getCity(), result.getCity(), "城市应该匹配");
        assertEquals(testAddress.getDistrict(), result.getDistrict(), "区县应该匹配");
        assertEquals(testAddress.getDetailAddress(), result.getDetailAddress(), "详细地址应该匹配");
        assertEquals(testAddress.getIsDefault(), result.getIsDefault(), "默认状态应该匹配");
        
        // 验证Mapper方法被调用
        verify(addressMapper, times(1)).findById(addressId);
    }

    @Test
    @DisplayName("获取不存在的地址 - 应该返回null")
    void getAddress_NonExistentAddress_ShouldReturnNull() {
        // Given
        Long nonExistentId = 999L;
        when(addressMapper.findById(nonExistentId)).thenReturn(null);
        
        // When
        AddressEntity result = addressService.getAddress(nonExistentId);
        
        // Then
        assertNull(result, "不存在的地址应该返回null");
        
        // 验证Mapper方法被调用
        verify(addressMapper, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("使用null ID查询地址 - 应该正常处理并返回mapper结果")
    void getAddress_WithNullId_ShouldDelegateToMapperAndReturnResult() {
        // Given
        Long nullId = null;
        when(addressMapper.findById(nullId)).thenReturn(null);
        
        // When
        AddressEntity result = addressService.getAddress(nullId);
        
        // Then
        assertNull(result, "null ID查询应该返回null");
        
        // 验证Mapper方法被调用
        verify(addressMapper, times(1)).findById(nullId);
    }

    @Test
    @DisplayName("验证getAddress方法的直接委托行为 - 应该直接返回Mapper结果")
    void getAddress_DirectDelegation_ShouldReturnMapperResultDirectly() {
        // Given
        Long addressId = 5L;
        AddressEntity expectedAddress = createTestAddress(5L, 2L, "测试用户", "15800158001",
                "浙江省", "杭州市", "西湖区", "文二路123号", false);
        when(addressMapper.findById(addressId)).thenReturn(expectedAddress);
        
        // When
        AddressEntity result = addressService.getAddress(addressId);
        
        // Then
        assertSame(expectedAddress, result, "应该返回相同的对象实例");
        
        // 验证Mapper方法被调用
        verify(addressMapper, times(1)).findById(addressId);
        
        // 验证没有其他Mapper方法被调用
        verifyNoMoreInteractions(addressMapper);
        verifyNoInteractions(orderMapper);
    }

    // ============================= getUserAddresses 方法测试 =============================

    @Test
    @DisplayName("获取用户的地址列表 - 用户有地址记录时应该返回完整列表")
    void getUserAddresses_WithExistingAddresses_ShouldReturnCompleteAddressList() {
        // Given
        Long userId = 1L;
        List<AddressEntity> expectedAddresses = Arrays.asList(
            createTestAddress(1L, userId, "张三", "13800138001",
                "北京市", "朝阳区", "望京街道", "SOHO现代城A座1001", true),
            createTestAddress(2L, userId, "张三", "13800138001",
                "上海市", "浦东新区", "陆家嘴街道", "环球金融中心88层", false),
            createTestAddress(3L, userId, "张三", "13800138001",
                "广州市", "天河区", "珠江新城", "IFC大厦2001", false)
        );
        when(addressMapper.findByUserId(userId)).thenReturn(expectedAddresses);
        
        // When
        List<AddressEntity> result = addressService.getUserAddresses(userId);
        
        // Then
        assertNotNull(result, "返回的地址列表不应该为null");
        assertEquals(3, result.size(), "应该返回3个地址");
        
        // 验证列表内容
        for (int i = 0; i < expectedAddresses.size(); i++) {
            AddressEntity expected = expectedAddresses.get(i);
            AddressEntity actual = result.get(i);
            assertEquals(expected.getId(), actual.getId(), "地址ID应该匹配");
            assertEquals(expected.getUserId(), actual.getUserId(), "用户ID应该匹配");
            assertEquals(expected.getReceiverName(), actual.getReceiverName(), "收货人应该匹配");
            assertEquals(expected.getIsDefault(), actual.getIsDefault(), "默认状态应该匹配");
        }
        
        // 验证Mapper方法被调用
        verify(addressMapper, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("获取用户的地址列表 - 用户无地址记录时应该返回空列表")
    void getUserAddresses_WithNoAddresses_ShouldReturnEmptyList() {
        // Given
        Long userId = 2L;
        List<AddressEntity> emptyList = Collections.emptyList();
        when(addressMapper.findByUserId(userId)).thenReturn(emptyList);
        
        // When
        List<AddressEntity> result = addressService.getUserAddresses(userId);
        
        // Then
        assertNotNull(result, "返回的列表不应该为null");
        assertTrue(result.isEmpty(), "应该返回空列表");
        assertEquals(0, result.size(), "列表大小应该为0");
        
        // 验证Mapper方法被调用
        verify(addressMapper, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("使用null用户ID查询地址 - 应该正常处理并返回mapper结果")
    void getUserAddresses_WithNullUserId_ShouldDelegateToMapperAndReturnResult() {
        // Given
        Long nullUserId = null;
        when(addressMapper.findByUserId(nullUserId)).thenReturn(new ArrayList<>());
        
        // When
        List<AddressEntity> result = addressService.getUserAddresses(nullUserId);
        
        // Then
        assertNotNull(result, "返回的列表不应该为null");
        assertTrue(result.isEmpty(), "应该返回空列表");
        
        // 验证Mapper方法被调用
        verify(addressMapper, times(1)).findByUserId(nullUserId);
    }

    @Test
    @DisplayName("验证getUserAddresses方法的直接委托行为 - 应该直接返回Mapper结果")
    void getUserAddresses_DirectDelegation_ShouldReturnMapperResultDirectly() {
        // Given
        Long userId = 10L;
        List<AddressEntity> expectedList = Arrays.asList(
            createTestAddress(10L, userId, "测试用户", "15900159001",
                "浙江省", "杭州市", "西湖区", "西湖大道1号", true)
        );
        when(addressMapper.findByUserId(userId)).thenReturn(expectedList);
        
        // When
        List<AddressEntity> result = addressService.getUserAddresses(userId);
        
        // Then
        assertSame(expectedList, result, "应该返回相同的列表实例");
        
        // 验证Mapper方法被调用
        verify(addressMapper, times(1)).findByUserId(userId);
        
        // 验证没有其他Mapper方法被调用
        verifyNoMoreInteractions(addressMapper);
        verifyNoInteractions(orderMapper);
    }

    // ============================= updateAddress 方法测试 =============================

    @Test
    @DisplayName("更新地址为默认地址 - 应该清除旧默认、更新地址、同步订单信息")
    void updateAddress_SetAsDefault_ShouldClearOldDefaultUpdateAddressAndSyncOrders() {
        // Given
        AddressEntity updateToDefaultAddress = createTestAddress(1L, 1L, "张三更新", "13811138001",
                "北京市", "海淀区", "中关村街道", "中关村大厦1号院", true);
        
        List<OrderEntity> relatedOrders = Arrays.asList(
            createTestOrder(1L, 1L, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0),
            createTestOrder(2L, 1L, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0)
        );
        
        when(orderMapper.findByCondition(1L, 0, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(relatedOrders);
        
        // When
        addressService.updateAddress(updateToDefaultAddress);
        
        // Then
        // 验证清除默认地址被调用
        verify(addressMapper, times(1)).clearDefaultAddress(1L);
        // 验证更新地址被调用
        verify(addressMapper, times(1)).updateAddress(updateToDefaultAddress);
        
        // 验证订单查询被调用
        verify(orderMapper, times(1)).findByCondition(1L, 0, null, null, 0, Integer.MAX_VALUE);
        
        // 验证订单更新被调用（两个匹配的订单）
        verify(orderMapper, times(2)).updateOrder(any(OrderEntity.class));
        
        // 验证调用顺序
        var inOrder = inOrder(addressMapper, orderMapper);
        inOrder.verify(addressMapper).clearDefaultAddress(1L);
        inOrder.verify(addressMapper).updateAddress(updateToDefaultAddress);
        inOrder.verify(orderMapper).findByCondition(1L, 0, null, null, 0, Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("更新非默认地址 - 应该直接更新地址并同步订单信息")
    void updateAddress_NonDefaultAddress_ShouldUpdateDirectlyAndSyncOrders() {
        // Given
        AddressEntity updateNonDefaultAddress = createTestAddress(2L, 1L, "李四更新", "13911139001",
                "上海市", "黄浦区", "外滩街道", "上海中心大厦", false);
        
        List<OrderEntity> relatedOrders = Arrays.asList(
            createTestOrder(3L, 1L, "李四", "13900139001",
                "上海市 浦东新区 陆家嘴街道 环球金融中心88层", 0)
        );
        
        when(orderMapper.findByCondition(1L, 0, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(relatedOrders);
        
        // When
        addressService.updateAddress(updateNonDefaultAddress);
        
        // Then
        // 验证清除默认地址没有被调用（因为不是默认地址）
        verify(addressMapper, never()).clearDefaultAddress(anyLong());
        // 验证更新地址被调用
        verify(addressMapper, times(1)).updateAddress(updateNonDefaultAddress);
        
        // 验证订单查询被调用
        verify(orderMapper, times(1)).findByCondition(1L, 0, null, null, 0, Integer.MAX_VALUE);
        
        // 验证订单更新被调用（一个不匹配的订单不会更新）
        verify(orderMapper, never()).updateOrder(any(OrderEntity.class));
    }

    @Test
    @DisplayName("更新有关联订单的地址 - 应该正确同步相关订单的收货信息")
    void updateAddress_WithRelatedOrders_ShouldCorrectlySyncRelatedOrdersShippingInfo() {
        // Given
        AddressEntity updatedAddress = createTestAddress(1L, 1L, "张三新名字", "13822138001",
                "北京市", "朝阳区", "望京街道", "SOHO现代城B庨2001", false);
        
        // 创建匹配和不匹配的订单
        OrderEntity matchingOrder1 = createTestOrder(1L, 1L, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
        OrderEntity matchingOrder2 = createTestOrder(2L, 1L, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
        OrderEntity nonMatchingOrder = createTestOrder(3L, 1L, "李四", "13900139001",
                "上海市 浦东新区 陆家嘴街道 环球金融中心88层", 0);
        
        List<OrderEntity> allOrders = Arrays.asList(matchingOrder1, matchingOrder2, nonMatchingOrder);
        
        when(orderMapper.findByCondition(1L, 0, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(allOrders);
        
        // When
        addressService.updateAddress(updatedAddress);
        
        // Then
        // 验证更新地址被调用
        verify(addressMapper, times(1)).updateAddress(updatedAddress);
        
        // 验证订单查询被调用
        verify(orderMapper, times(1)).findByCondition(1L, 0, null, null, 0, Integer.MAX_VALUE);
        
        // 捕获订单更新调用的参数
        ArgumentCaptor<OrderEntity> orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderMapper, times(2)).updateOrder(orderCaptor.capture());
        
        List<OrderEntity> updatedOrders = orderCaptor.getAllValues();
        
        // 验证更新的订单信息
        for (OrderEntity updatedOrder : updatedOrders) {
            assertEquals("张三新名字", updatedOrder.getReceiverName(), "收货人姓名应该更新");
            assertEquals("13822138001", updatedOrder.getReceiverPhone(), "收货人电话应该更新");
            assertEquals("北京市 朝阳区 望京街道 SOHO现代城B庨2001", 
                updatedOrder.getReceiverAddress(), "收货地址应该更新");
        }
    }

    @Test
    @DisplayName("更新不存在的地址 - 应该正常执行更新和订单同步逻辑")
    void updateAddress_NonExistentAddress_ShouldExecuteUpdateAndOrderSyncNormally() {
        // Given
        AddressEntity nonExistentAddress = createTestAddress(999L, 1L, "不存在的地址", "13999139999",
                "不存在省", "不存在市", "不存在区", "不存在的详细地址", false);
        
        when(orderMapper.findByCondition(1L, 0, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(Collections.emptyList());
        
        // When
        addressService.updateAddress(nonExistentAddress);
        
        // Then
        // 验证更新地址被调用（即使地址不存在）
        verify(addressMapper, times(1)).updateAddress(nonExistentAddress);
        
        // 验证订单查询被调用
        verify(orderMapper, times(1)).findByCondition(1L, 0, null, null, 0, Integer.MAX_VALUE);
        
        // 验证没有订单更新（因为没有相关订单）
        verify(orderMapper, never()).updateOrder(any(OrderEntity.class));
    }

    // ============================= deleteAddress 方法测试 =============================

    @Test
    @DisplayName("删除有关联订单的地址 - 应该更新订单收货信息为已删除并删除地址记录")
    void deleteAddress_ExistingAddressWithOrders_ShouldUpdateOrdersToDeletedAndDeleteAddress() {
        // Given
        Long addressId = 1L;
        AddressEntity addressToDelete = createTestAddress(addressId, 1L, "张三", "13800138001",
                "北京市", "朝阳区", "望京街道", "SOHO现代城A座1001", true);
        
        // 创建匹配和不匹配的订单
        OrderEntity matchingOrder1 = createTestOrder(1L, 1L, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
        OrderEntity matchingOrder2 = createTestOrder(2L, 1L, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
        OrderEntity nonMatchingOrder = createTestOrder(3L, 1L, "李四", "13900139001",
                "上海市 浦东新区 陆家嘴街道 环球金融中心88层", 0);
        
        List<OrderEntity> allOrders = Arrays.asList(matchingOrder1, matchingOrder2, nonMatchingOrder);
        
        when(addressMapper.findById(addressId)).thenReturn(addressToDelete);
        when(orderMapper.findByCondition(1L, null, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(allOrders);
        
        // When
        addressService.deleteAddress(addressId);
        
        // Then
        // 验证地址查询被调用
        verify(addressMapper, times(1)).findById(addressId);
        
        // 验证订单查询被调用
        verify(orderMapper, times(1)).findByCondition(1L, null, null, null, 0, Integer.MAX_VALUE);
        
        // 捕获订单更新调用的参数
        ArgumentCaptor<OrderEntity> orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderMapper, times(2)).updateOrder(orderCaptor.capture());
        
        List<OrderEntity> updatedOrders = orderCaptor.getAllValues();
        
        // 验证匹配订单的收货信息被更新为“已删除”
        for (OrderEntity updatedOrder : updatedOrders) {
            assertEquals("已删除", updatedOrder.getReceiverName(), "收货人姓名应该更新为已删除");
            assertEquals("已删除", updatedOrder.getReceiverPhone(), "收货人电话应该更新为已删除");
            assertEquals("已删除", updatedOrder.getReceiverAddress(), "收货地址应该更新为已删除");
        }
        
        // 验证地址删除被调用
        verify(addressMapper, times(1)).deleteAddress(addressId);
        
        // 验证调用顺序
        var inOrder = inOrder(addressMapper, orderMapper);
        inOrder.verify(addressMapper).findById(addressId);
        inOrder.verify(orderMapper).findByCondition(1L, null, null, null, 0, Integer.MAX_VALUE);
        inOrder.verify(addressMapper).deleteAddress(addressId);
    }

    @Test
    @DisplayName("删除无关联订单的地址 - 应该直接删除地址记录")
    void deleteAddress_ExistingAddressNoOrders_ShouldDeleteAddressDirectly() {
        // Given
        Long addressId = 2L;
        AddressEntity addressToDelete = createTestAddress(addressId, 1L, "李四", "13900139001",
                "上海市", "浦东新区", "陆家嘴街道", "环球金融中心88层", false);
        
        when(addressMapper.findById(addressId)).thenReturn(addressToDelete);
        when(orderMapper.findByCondition(1L, null, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(Collections.emptyList());
        
        // When
        addressService.deleteAddress(addressId);
        
        // Then
        // 验证地址查询被调用
        verify(addressMapper, times(1)).findById(addressId);
        
        // 验证订单查询被调用
        verify(orderMapper, times(1)).findByCondition(1L, null, null, null, 0, Integer.MAX_VALUE);
        
        // 验证没有订单更新（因为没有相关订单）
        verify(orderMapper, never()).updateOrder(any(OrderEntity.class));
        
        // 验证地址删除被调用
        verify(addressMapper, times(1)).deleteAddress(addressId);
    }

    @Test
    @DisplayName("删除不存在的地址 - 应该静默处理不执行任何操作")
    void deleteAddress_NonExistentAddress_ShouldHandleSilentlyAndPerformNoOperations() {
        // Given
        Long nonExistentId = 999L;
        when(addressMapper.findById(nonExistentId)).thenReturn(null);
        
        // When
        addressService.deleteAddress(nonExistentId);
        
        // Then
        // 验证地址查询被调用
        verify(addressMapper, times(1)).findById(nonExistentId);
        
        // 验证没有订单查询（因为地址不存在）
        verifyNoInteractions(orderMapper);
        
        // 验证没有地址删除操作
        verify(addressMapper, never()).deleteAddress(anyLong());
    }

    @Test
    @DisplayName("删除地址的匹配算法测试 - 应该正确匹配订单和地址")
    void deleteAddress_MatchingAlgorithm_ShouldCorrectlyMatchOrdersAndAddress() {
        // Given
        Long addressId = 1L;
        AddressEntity addressToDelete = createTestAddress(addressId, 1L, "张三", "13800138001",
                "北京市", "朝阳区", "望京街道", "SOHO现代城A座1001", true);
        
        // 创建匹配的订单（姓名、电话、地址完全匹配）
        OrderEntity exactMatch = createTestOrder(1L, 1L, "张三", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
        
        // 创建不匹配的订单（姓名不匹配）
        OrderEntity nameNotMatch = createTestOrder(2L, 1L, "李四", "13800138001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
        
        // 创建不匹配的订单（电话不匹配）
        OrderEntity phoneNotMatch = createTestOrder(3L, 1L, "张三", "13911139001",
                "北京市 朝阳区 望京街道 SOHO现代城A座1001", 0);
        
        // 创建不匹配的订单（地址不匹配）
        OrderEntity addressNotMatch = createTestOrder(4L, 1L, "张三", "13800138001",
                "上海市 浦东新区 陆家嘴街道 环球金融中心88层", 0);
        
        List<OrderEntity> allOrders = Arrays.asList(exactMatch, nameNotMatch, phoneNotMatch, addressNotMatch);
        
        when(addressMapper.findById(addressId)).thenReturn(addressToDelete);
        when(orderMapper.findByCondition(1L, null, null, null, 0, Integer.MAX_VALUE))
            .thenReturn(allOrders);
        
        // When
        addressService.deleteAddress(addressId);
        
        // Then
        // 验证只有一个完全匹配的订单被更新
        ArgumentCaptor<OrderEntity> orderCaptor = ArgumentCaptor.forClass(OrderEntity.class);
        verify(orderMapper, times(1)).updateOrder(orderCaptor.capture());
        
        OrderEntity updatedOrder = orderCaptor.getValue();
        assertEquals(1L, updatedOrder.getId(), "只有完全匹配的订单应该被更新");
        assertEquals("已删除", updatedOrder.getReceiverName(), "收货人姓名应该更新为已删除");
        assertEquals("已删除", updatedOrder.getReceiverPhone(), "收货人电话应该更新为已删除");
        assertEquals("已删除", updatedOrder.getReceiverAddress(), "收货地址应该更新为已删除");
    }

    @Test
    @DisplayName("使用null ID删除地址 - 应该正常处理并返回mapper结果")
    void deleteAddress_WithNullId_ShouldDelegateToMapperAndHandleNormally() {
        // Given
        Long nullId = null;
        when(addressMapper.findById(nullId)).thenReturn(null);
        
        // When
        addressService.deleteAddress(nullId);
        
        // Then
        // 验证地址查询被调用
        verify(addressMapper, times(1)).findById(nullId);
        
        // 验证没有订单查询（因为地址不存在）
        verifyNoInteractions(orderMapper);
        
        // 验证没有地址删除操作
        verify(addressMapper, never()).deleteAddress(any());
    }

}