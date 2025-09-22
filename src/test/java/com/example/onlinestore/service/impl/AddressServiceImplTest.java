package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.dto.CreateAddressRequest;
import com.example.onlinestore.dto.UpdateAddressRequest;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("AddressServiceImpl单元测试")
class AddressServiceImplTest {

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Long memberId;
    private Long addressId;
    private CreateAddressRequest createRequest;
    private UpdateAddressRequest updateRequest;
    private AddressEntity addressEntity;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        addressId = 100L;
        
        // 创建地址请求
        createRequest = new CreateAddressRequest();
        createRequest.setReceiverName("张三");
        createRequest.setReceiverPhone("13800138000");
        createRequest.setProvince("广东省");
        createRequest.setCity("深圳市");
        createRequest.setDistrict("南山区");
        createRequest.setDetailAddress("科技园南区软件大厦1001");
        createRequest.setZipCode("518000");
        createRequest.setIsDefault(false);
        
        // 更新地址请求
        updateRequest = new UpdateAddressRequest();
        updateRequest.setId(addressId);
        updateRequest.setReceiverName("李四");
        updateRequest.setReceiverPhone("13900139000");
        updateRequest.setProvince("广东省");
        updateRequest.setCity("广州市");
        updateRequest.setDistrict("天河区");
        updateRequest.setDetailAddress("珠江新城商务大厦2002");
        updateRequest.setZipCode("510000");
        updateRequest.setIsDefault(true);
        
        // 地址实体
        addressEntity = new AddressEntity();
        addressEntity.setId(addressId);
        addressEntity.setMemberId(memberId);
        addressEntity.setReceiverName("张三");
        addressEntity.setReceiverPhone("13800138000");
        addressEntity.setProvince("广东省");
        addressEntity.setCity("深圳市");
        addressEntity.setDistrict("南山区");
        addressEntity.setDetailAddress("科技园南区软件大厦1001");
        addressEntity.setZipCode("518000");
        addressEntity.setIsDefault(0);
        addressEntity.setCreatedAt(LocalDateTime.now());
        addressEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("创建地址 - 成功")
    void createAddress_Success() {
        // Given
        when(addressMapper.countByMemberId(memberId)).thenReturn(5);
        when(addressMapper.insertAddress(any(AddressEntity.class))).thenReturn(1);
        
        // When
        Address result = addressService.createAddress(createRequest, memberId);
        
        // Then
        assertNotNull(result);
        assertEquals(memberId, result.getMemberId());
        assertEquals(createRequest.getReceiverName(), result.getReceiverName());
        assertEquals(createRequest.getReceiverPhone(), result.getReceiverPhone());
        assertEquals(createRequest.getProvince(), result.getProvince());
        assertEquals(createRequest.getCity(), result.getCity());
        assertEquals(createRequest.getDistrict(), result.getDistrict());
        assertEquals(createRequest.getDetailAddress(), result.getDetailAddress());
        assertEquals(createRequest.getZipCode(), result.getZipCode());
        assertEquals(createRequest.getIsDefault(), result.getIsDefault());
        
        verify(addressMapper).countByMemberId(memberId);
        verify(addressMapper).insertAddress(any(AddressEntity.class));
        verify(addressMapper, never()).clearDefaultByMemberId(memberId);
    }

    @Test
    @DisplayName("创建地址 - 设置为默认地址")
    void createAddress_SetAsDefault_Success() {
        // Given
        createRequest.setIsDefault(true);
        when(addressMapper.countByMemberId(memberId)).thenReturn(3);
        when(addressMapper.insertAddress(any(AddressEntity.class))).thenReturn(1);
        
        // When
        Address result = addressService.createAddress(createRequest, memberId);
        
        // Then
        assertNotNull(result);
        assertTrue(result.getIsDefault());
        
        verify(addressMapper).clearDefaultByMemberId(memberId);
        verify(addressMapper).insertAddress(any(AddressEntity.class));
    }

    @Test
    @DisplayName("创建地址 - 超过数量限制")
    void createAddress_ExceedLimit_ThrowsException() {
        // Given
        when(addressMapper.countByMemberId(memberId)).thenReturn(20);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.createAddress(createRequest, memberId));
        assertEquals(ErrorCode.ADDRESS_LIMIT_EXCEEDED, exception.getErrorCode());
        
        verify(addressMapper).countByMemberId(memberId);
        verify(addressMapper, never()).insertAddress(any(AddressEntity.class));
    }

    @Test
    @DisplayName("创建地址 - 插入失败")
    void createAddress_InsertFailed_ThrowsException() {
        // Given
        when(addressMapper.countByMemberId(memberId)).thenReturn(5);
        when(addressMapper.insertAddress(any(AddressEntity.class))).thenReturn(0);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.createAddress(createRequest, memberId));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @Test
    @DisplayName("更新地址 - 成功")
    void updateAddress_Success() {
        // Given
        when(addressMapper.findById(addressId)).thenReturn(addressEntity);
        when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(1);
        
        AddressEntity updatedEntity = new AddressEntity();
        updatedEntity.setId(addressId);
        updatedEntity.setMemberId(memberId);
        updatedEntity.setReceiverName(updateRequest.getReceiverName());
        updatedEntity.setReceiverPhone(updateRequest.getReceiverPhone());
        updatedEntity.setProvince(updateRequest.getProvince());
        updatedEntity.setCity(updateRequest.getCity());
        updatedEntity.setDistrict(updateRequest.getDistrict());
        updatedEntity.setDetailAddress(updateRequest.getDetailAddress());
        updatedEntity.setZipCode(updateRequest.getZipCode());
        updatedEntity.setIsDefault(1);
        
        when(addressMapper.findById(addressId)).thenReturn(updatedEntity);
        
        // When
        Address result = addressService.updateAddress(updateRequest, memberId);
        
        // Then
        assertNotNull(result);
        assertEquals(updateRequest.getReceiverName(), result.getReceiverName());
        assertEquals(updateRequest.getReceiverPhone(), result.getReceiverPhone());
        assertEquals(updateRequest.getProvince(), result.getProvince());
        assertEquals(updateRequest.getCity(), result.getCity());
        assertEquals(updateRequest.getDistrict(), result.getDistrict());
        assertEquals(updateRequest.getDetailAddress(), result.getDetailAddress());
        assertEquals(updateRequest.getZipCode(), result.getZipCode());
        assertTrue(result.getIsDefault());
        
        verify(addressMapper, times(2)).findById(addressId);
        verify(addressMapper).clearDefaultByMemberId(memberId);
        verify(addressMapper).updateAddress(any(AddressEntity.class));
    }

    @Test
    @DisplayName("更新地址 - 地址不存在")
    void updateAddress_AddressNotFound_ThrowsException() {
        // Given
        when(addressMapper.findById(addressId)).thenReturn(null);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.updateAddress(updateRequest, memberId));
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
        
        verify(addressMapper).findById(addressId);
        verify(addressMapper, never()).updateAddress(any(AddressEntity.class));
    }

    @Test
    @DisplayName("更新地址 - 无权访问")
    void updateAddress_AccessDenied_ThrowsException() {
        // Given
        addressEntity.setMemberId(999L); // 不同的用户ID
        when(addressMapper.findById(addressId)).thenReturn(addressEntity);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.updateAddress(updateRequest, memberId));
        assertEquals(ErrorCode.ADDRESS_ACCESS_DENIED, exception.getErrorCode());
        
        verify(addressMapper).findById(addressId);
        verify(addressMapper, never()).updateAddress(any(AddressEntity.class));
    }

    @Test
    @DisplayName("更新地址 - 更新失败")
    void updateAddress_UpdateFailed_ThrowsException() {
        // Given
        when(addressMapper.findById(addressId)).thenReturn(addressEntity);
        when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(0);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.updateAddress(updateRequest, memberId));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @Test
    @DisplayName("删除地址 - 成功")
    void deleteAddress_Success() {
        // Given
        when(addressMapper.findById(addressId)).thenReturn(addressEntity);
        when(addressMapper.deleteById(addressId)).thenReturn(1);
        
        // When
        assertDoesNotThrow(() -> addressService.deleteAddress(addressId, memberId));
        
        // Then
        verify(addressMapper).findById(addressId);
        verify(addressMapper).deleteById(addressId);
    }

    @Test
    @DisplayName("删除地址 - 地址不存在")
    void deleteAddress_AddressNotFound_ThrowsException() {
        // Given
        when(addressMapper.findById(addressId)).thenReturn(null);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.deleteAddress(addressId, memberId));
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
        
        verify(addressMapper).findById(addressId);
        verify(addressMapper, never()).deleteById(addressId);
    }

    @Test
    @DisplayName("删除地址 - 无权访问")
    void deleteAddress_AccessDenied_ThrowsException() {
        // Given
        addressEntity.setMemberId(999L);
        when(addressMapper.findById(addressId)).thenReturn(addressEntity);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.deleteAddress(addressId, memberId));
        assertEquals(ErrorCode.ADDRESS_ACCESS_DENIED, exception.getErrorCode());
        
        verify(addressMapper).findById(addressId);
        verify(addressMapper, never()).deleteById(addressId);
    }

    @Test
    @DisplayName("删除地址 - 删除失败")
    void deleteAddress_DeleteFailed_ThrowsException() {
        // Given
        when(addressMapper.findById(addressId)).thenReturn(addressEntity);
        when(addressMapper.deleteById(addressId)).thenReturn(0);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.deleteAddress(addressId, memberId));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @Test
    @DisplayName("根据ID获取地址 - 成功")
    void getAddressById_Success() {
        // Given
        when(addressMapper.findById(addressId)).thenReturn(addressEntity);
        
        // When
        Address result = addressService.getAddressById(addressId, memberId);
        
        // Then
        assertNotNull(result);
        assertEquals(addressEntity.getId(), result.getId());
        assertEquals(addressEntity.getMemberId(), result.getMemberId());
        assertEquals(addressEntity.getReceiverName(), result.getReceiverName());
        
        verify(addressMapper).findById(addressId);
    }

    @Test
    @DisplayName("根据ID获取地址 - 地址不存在")
    void getAddressById_AddressNotFound_ThrowsException() {
        // Given
        when(addressMapper.findById(addressId)).thenReturn(null);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.getAddressById(addressId, memberId));
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("根据ID获取地址 - 无权访问")
    void getAddressById_AccessDenied_ThrowsException() {
        // Given
        addressEntity.setMemberId(999L);
        when(addressMapper.findById(addressId)).thenReturn(addressEntity);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.getAddressById(addressId, memberId));
        assertEquals(ErrorCode.ADDRESS_ACCESS_DENIED, exception.getErrorCode());
    }

    @Test
    @DisplayName("获取用户所有地址 - 成功")
    void getAddressesByMemberId_Success() {
        // Given
        AddressEntity address1 = new AddressEntity();
        address1.setId(1L);
        address1.setMemberId(memberId);
        address1.setReceiverName("地址1");
        
        AddressEntity address2 = new AddressEntity();
        address2.setId(2L);
        address2.setMemberId(memberId);
        address2.setReceiverName("地址2");
        
        List<AddressEntity> addressEntities = Arrays.asList(address1, address2);
        when(addressMapper.findByMemberId(memberId)).thenReturn(addressEntities);
        
        // When
        List<Address> result = addressService.getAddressesByMemberId(memberId);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("地址1", result.get(0).getReceiverName());
        assertEquals("地址2", result.get(1).getReceiverName());
        
        verify(addressMapper).findByMemberId(memberId);
    }

    @Test
    @DisplayName("获取用户所有地址 - 空列表")
    void getAddressesByMemberId_EmptyList() {
        // Given
        when(addressMapper.findByMemberId(memberId)).thenReturn(Arrays.asList());
        
        // When
        List<Address> result = addressService.getAddressesByMemberId(memberId);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(addressMapper).findByMemberId(memberId);
    }

    @Test
    @DisplayName("获取默认地址 - 成功")
    void getDefaultAddress_Success() {
        // Given
        addressEntity.setIsDefault(1);
        when(addressMapper.findDefaultByMemberId(memberId)).thenReturn(addressEntity);
        
        // When
        Address result = addressService.getDefaultAddress(memberId);
        
        // Then
        assertNotNull(result);
        assertTrue(result.getIsDefault());
        assertEquals(addressEntity.getId(), result.getId());
        
        verify(addressMapper).findDefaultByMemberId(memberId);
    }

    @Test
    @DisplayName("获取默认地址 - 无默认地址")
    void getDefaultAddress_NoDefaultAddress() {
        // Given
        when(addressMapper.findDefaultByMemberId(memberId)).thenReturn(null);
        
        // When
        Address result = addressService.getDefaultAddress(memberId);
        
        // Then
        assertNull(result);
        
        verify(addressMapper).findDefaultByMemberId(memberId);
    }

    @Test
    @DisplayName("设置默认地址 - 成功")
    void setDefaultAddress_Success() {
        // Given
        when(addressMapper.findById(addressId)).thenReturn(addressEntity);
        when(addressMapper.setDefaultAddress(addressId, memberId)).thenReturn(1);
        
        // When
        assertDoesNotThrow(() -> addressService.setDefaultAddress(addressId, memberId));
        
        // Then
        verify(addressMapper).findById(addressId);
        verify(addressMapper).clearDefaultByMemberId(memberId);
        verify(addressMapper).setDefaultAddress(addressId, memberId);
    }

    @Test
    @DisplayName("设置默认地址 - 地址不存在")
    void setDefaultAddress_AddressNotFound_ThrowsException() {
        // Given
        when(addressMapper.findById(addressId)).thenReturn(null);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.setDefaultAddress(addressId, memberId));
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
        
        verify(addressMapper).findById(addressId);
        verify(addressMapper, never()).setDefaultAddress(addressId, memberId);
    }

    @Test
    @DisplayName("设置默认地址 - 无权访问")
    void setDefaultAddress_AccessDenied_ThrowsException() {
        // Given
        addressEntity.setMemberId(999L);
        when(addressMapper.findById(addressId)).thenReturn(addressEntity);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.setDefaultAddress(addressId, memberId));
        assertEquals(ErrorCode.ADDRESS_ACCESS_DENIED, exception.getErrorCode());
        
        verify(addressMapper).findById(addressId);
        verify(addressMapper, never()).setDefaultAddress(addressId, memberId);
    }

    @Test
    @DisplayName("设置默认地址 - 设置失败")
    void setDefaultAddress_SetFailed_ThrowsException() {
        // Given
        when(addressMapper.findById(addressId)).thenReturn(addressEntity);
        when(addressMapper.setDefaultAddress(addressId, memberId)).thenReturn(0);
        
        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.setDefaultAddress(addressId, memberId));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }
}