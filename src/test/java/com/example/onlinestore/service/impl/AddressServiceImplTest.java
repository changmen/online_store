package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address testAddress;
    private AddressEntity testAddressEntity;
    private Long testMemberId = 1L;
    private Long testAddressId = 100L;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testAddress = new Address();
        testAddress.setId(testAddressId);
        testAddress.setMemberId(testMemberId);
        testAddress.setReceiverName("张三");
        testAddress.setReceiverPhone("13800138000");
        testAddress.setProvince("北京市");
        testAddress.setCity("北京市");
        testAddress.setDistrict("朝阳区");
        testAddress.setDetailAddress("三里屯街道123号");
        testAddress.setPostalCode("100000");
        testAddress.setIsDefault(false);
        testAddress.setTag("家");

        testAddressEntity = new AddressEntity();
        testAddressEntity.setId(testAddressId);
        testAddressEntity.setMemberId(testMemberId);
        testAddressEntity.setReceiverName("张三");
        testAddressEntity.setReceiverPhone("13800138000");
        testAddressEntity.setProvince("北京市");
        testAddressEntity.setCity("北京市");
        testAddressEntity.setDistrict("朝阳区");
        testAddressEntity.setDetailAddress("三里屯街道123号");
        testAddressEntity.setPostalCode("100000");
        testAddressEntity.setIsDefault(false);
        testAddressEntity.setTag("家");
        testAddressEntity.setCreatedAt(LocalDateTime.now());
        testAddressEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createAddress_Success() {
        // 准备数据
        Address newAddress = new Address();
        newAddress.setMemberId(testMemberId);
        newAddress.setReceiverName("李四");
        newAddress.setReceiverPhone("13900139000");
        newAddress.setProvince("上海市");
        newAddress.setCity("上海市");
        newAddress.setDistrict("浦东新区");
        newAddress.setDetailAddress("陆家嘴金融中心456号");
        newAddress.setPostalCode("200000");
        newAddress.setIsDefault(false);
        newAddress.setTag("公司");

        // Mock 依赖
        when(addressMapper.countByMemberId(testMemberId)).thenReturn(5);
        when(addressMapper.insert(any(AddressEntity.class))).thenReturn(1);

        // 执行方法
        Address result = addressService.createAddress(newAddress);

        // 验证结果
        assertNotNull(result);
        assertEquals(newAddress.getMemberId(), result.getMemberId());
        assertEquals(newAddress.getReceiverName(), result.getReceiverName());
        
        // 验证调用
        verify(addressMapper).countByMemberId(testMemberId);
        verify(addressMapper).insert(any(AddressEntity.class));
        verify(addressMapper, never()).clearDefaultByMemberId(testMemberId);
    }

    @Test
    void createAddress_WithDefaultFlag_Success() {
        // 准备数据
        testAddress.setIsDefault(true);
        testAddress.setId(null); // 新地址没有ID

        // Mock 依赖
        when(addressMapper.countByMemberId(testMemberId)).thenReturn(5);
        when(addressMapper.clearDefaultByMemberId(testMemberId)).thenReturn(1);
        when(addressMapper.insert(any(AddressEntity.class))).thenReturn(1);

        // 执行方法
        Address result = addressService.createAddress(testAddress);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.getIsDefault());
        
        // 验证调用
        verify(addressMapper).countByMemberId(testMemberId);
        verify(addressMapper).clearDefaultByMemberId(testMemberId);
        verify(addressMapper).insert(any(AddressEntity.class));
    }

    @Test
    void createAddress_ExceedsLimit_ThrowsException() {
        // Mock 依赖
        when(addressMapper.countByMemberId(testMemberId)).thenReturn(20);

        // 执行并验证异常
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.createAddress(testAddress));
        
        assertEquals(ErrorCode.MEMBER_ADDRESS_LIMIT_EXCEEDED, exception.getErrorCode());
        
        // 验证调用
        verify(addressMapper).countByMemberId(testMemberId);
        verify(addressMapper, never()).insert(any(AddressEntity.class));
    }

    @Test
    void createAddress_InsertFailed_ThrowsException() {
        // Mock 依赖
        when(addressMapper.countByMemberId(testMemberId)).thenReturn(5);
        when(addressMapper.insert(any(AddressEntity.class))).thenReturn(0);

        // 执行并验证异常
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.createAddress(testAddress));
        
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @Test
    void updateAddress_Success() {
        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(testAddressEntity, testAddressEntity);
        when(addressMapper.update(any(AddressEntity.class))).thenReturn(1);

        // 执行方法
        Address result = addressService.updateAddress(testAddress);

        // 验证结果
        assertNotNull(result);
        
        // 验证调用
        verify(addressMapper, times(2)).selectById(testAddressId);
        verify(addressMapper).update(any(AddressEntity.class));
    }

    @Test
    void updateAddress_AddressNotFound_ThrowsException() {
        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(null);

        // 执行并验证异常
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.updateAddress(testAddress));
        
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateAddress_AccessDenied_ThrowsException() {
        // 准备数据 - 地址属于其他用户
        AddressEntity otherUserAddress = new AddressEntity();
        otherUserAddress.setId(testAddressId);
        otherUserAddress.setMemberId(999L); // 不同的用户ID

        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(otherUserAddress);

        // 执行并验证异常
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.updateAddress(testAddress));
        
        assertEquals(ErrorCode.ADDRESS_ACCESS_DENIED, exception.getErrorCode());
    }

    @Test
    void updateAddress_WithDefaultFlag_Success() {
        // 准备数据
        testAddress.setIsDefault(true);

        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(testAddressEntity);
        when(addressMapper.clearDefaultByMemberId(testMemberId)).thenReturn(1);
        when(addressMapper.update(any(AddressEntity.class))).thenReturn(1);

        // 执行方法
        Address result = addressService.updateAddress(testAddress);

        // 验证结果
        assertNotNull(result);
        
        // 验证调用
        verify(addressMapper).clearDefaultByMemberId(testMemberId);
        verify(addressMapper).update(any(AddressEntity.class));
    }

    @Test
    void deleteAddress_Success() {
        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(testAddressEntity);
        when(addressMapper.deleteById(testAddressId)).thenReturn(1);

        // 执行方法
        assertDoesNotThrow(() -> addressService.deleteAddress(testAddressId, testMemberId));

        // 验证调用
        verify(addressMapper).selectById(testAddressId);
        verify(addressMapper).deleteById(testAddressId);
    }

    @Test
    void deleteAddress_AddressNotFound_ThrowsException() {
        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(null);

        // 执行并验证异常
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.deleteAddress(testAddressId, testMemberId));
        
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void deleteAddress_AccessDenied_ThrowsException() {
        // 准备数据 - 地址属于其他用户
        AddressEntity otherUserAddress = new AddressEntity();
        otherUserAddress.setId(testAddressId);
        otherUserAddress.setMemberId(999L);

        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(otherUserAddress);

        // 执行并验证异常
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.deleteAddress(testAddressId, testMemberId));
        
        assertEquals(ErrorCode.ADDRESS_ACCESS_DENIED, exception.getErrorCode());
    }

    @Test
    void getAddressById_Success() {
        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(testAddressEntity);

        // 执行方法
        Address result = addressService.getAddressById(testAddressId, testMemberId);

        // 验证结果
        assertNotNull(result);
        assertEquals(testAddressId, result.getId());
        assertEquals(testMemberId, result.getMemberId());
        
        // 验证调用
        verify(addressMapper).selectById(testAddressId);
    }

    @Test
    void getAddressById_AddressNotFound_ThrowsException() {
        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(null);

        // 执行并验证异常
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.getAddressById(testAddressId, testMemberId));
        
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void getAddressesByMemberId_Success() {
        // 准备数据
        AddressEntity address1 = new AddressEntity();
        address1.setId(1L);
        address1.setMemberId(testMemberId);
        address1.setReceiverName("张三");
        
        AddressEntity address2 = new AddressEntity();
        address2.setId(2L);
        address2.setMemberId(testMemberId);
        address2.setReceiverName("李四");

        List<AddressEntity> addressEntities = Arrays.asList(address1, address2);

        // Mock 依赖
        when(addressMapper.selectByMemberId(testMemberId)).thenReturn(addressEntities);

        // 执行方法
        List<Address> result = addressService.getAddressesByMemberId(testMemberId);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("张三", result.get(0).getReceiverName());
        assertEquals("李四", result.get(1).getReceiverName());
        
        // 验证调用
        verify(addressMapper).selectByMemberId(testMemberId);
    }

    @Test
    void getDefaultAddress_Success() {
        // Mock 依赖
        when(addressMapper.selectDefaultByMemberId(testMemberId)).thenReturn(testAddressEntity);

        // 执行方法
        Address result = addressService.getDefaultAddress(testMemberId);

        // 验证结果
        assertNotNull(result);
        assertEquals(testAddressId, result.getId());
        
        // 验证调用
        verify(addressMapper).selectDefaultByMemberId(testMemberId);
    }

    @Test
    void getDefaultAddress_NoDefault_ReturnsNull() {
        // Mock 依赖
        when(addressMapper.selectDefaultByMemberId(testMemberId)).thenReturn(null);

        // 执行方法
        Address result = addressService.getDefaultAddress(testMemberId);

        // 验证结果
        assertNull(result);
        
        // 验证调用
        verify(addressMapper).selectDefaultByMemberId(testMemberId);
    }

    @Test
    void setDefaultAddress_Success() {
        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(testAddressEntity);
        when(addressMapper.clearDefaultByMemberId(testMemberId)).thenReturn(1);
        when(addressMapper.setAsDefault(testAddressId, testMemberId)).thenReturn(1);

        // 执行方法
        assertDoesNotThrow(() -> addressService.setDefaultAddress(testAddressId, testMemberId));

        // 验证调用
        verify(addressMapper).selectById(testAddressId);
        verify(addressMapper).clearDefaultByMemberId(testMemberId);
        verify(addressMapper).setAsDefault(testAddressId, testMemberId);
    }

    @Test
    void setDefaultAddress_AddressNotFound_ThrowsException() {
        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(null);

        // 执行并验证异常
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.setDefaultAddress(testAddressId, testMemberId));
        
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void setDefaultAddress_AccessDenied_ThrowsException() {
        // 准备数据 - 地址属于其他用户
        AddressEntity otherUserAddress = new AddressEntity();
        otherUserAddress.setId(testAddressId);
        otherUserAddress.setMemberId(999L);

        // Mock 依赖
        when(addressMapper.selectById(testAddressId)).thenReturn(otherUserAddress);

        // 执行并验证异常
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.setDefaultAddress(testAddressId, testMemberId));
        
        assertEquals(ErrorCode.ADDRESS_ACCESS_DENIED, exception.getErrorCode());
    }

    @Test
    void canAddMoreAddresses_CanAdd_ReturnsTrue() {
        // Mock 依赖
        when(addressMapper.countByMemberId(testMemberId)).thenReturn(10);

        // 执行方法
        boolean result = addressService.canAddMoreAddresses(testMemberId);

        // 验证结果
        assertTrue(result);
        
        // 验证调用
        verify(addressMapper).countByMemberId(testMemberId);
    }

    @Test
    void canAddMoreAddresses_ReachedLimit_ReturnsFalse() {
        // Mock 依赖
        when(addressMapper.countByMemberId(testMemberId)).thenReturn(20);

        // 执行方法
        boolean result = addressService.canAddMoreAddresses(testMemberId);

        // 验证结果
        assertFalse(result);
        
        // 验证调用
        verify(addressMapper).countByMemberId(testMemberId);
    }
}