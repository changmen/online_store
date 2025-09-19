package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.bean.Member;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.service.MemberService;
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

    @Mock
    private MemberService memberService;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address testAddress;
    private AddressEntity testAddressEntity;
    private Member testMember;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testAddress = new Address();
        testAddress.setId(1L);
        testAddress.setMemberId(100L);
        testAddress.setReceiverName("张三");
        testAddress.setReceiverPhone("13812345678");
        testAddress.setProvince("广东省");
        testAddress.setCity("深圳市");
        testAddress.setDistrict("南山区");
        testAddress.setDetailAddress("科技园南区123号");
        testAddress.setDefault(false);

        testAddressEntity = new AddressEntity();
        testAddressEntity.setId(1L);
        testAddressEntity.setMemberId(100L);
        testAddressEntity.setReceiverName("张三");
        testAddressEntity.setReceiverPhone("13812345678");
        testAddressEntity.setProvince("广东省");
        testAddressEntity.setCity("深圳市");
        testAddressEntity.setDistrict("南山区");
        testAddressEntity.setDetailAddress("科技园南区123号");
        testAddressEntity.setIsDefault(0);
        testAddressEntity.setCreatedAt(LocalDateTime.now());
        testAddressEntity.setUpdatedAt(LocalDateTime.now());

        testMember = new Member();
        testMember.setId(100L);
    }

    @Test
    void createAddress_Success() {
        // Given
        when(addressMapper.insertAddress(any(AddressEntity.class))).thenReturn(1);

        // When
        Address result = addressService.createAddress(testAddress);

        // Then
        assertNotNull(result);
        assertEquals(testAddress.getReceiverName(), result.getReceiverName());
        assertEquals(testAddress.getReceiverPhone(), result.getReceiverPhone());
        verify(addressMapper).insertAddress(any(AddressEntity.class));
    }

    @Test
    void createAddress_WithDefaultAddress_ShouldClearOtherDefaults() {
        // Given
        testAddress.setDefault(true);
        when(addressMapper.clearDefaultByMemberId(testAddress.getMemberId())).thenReturn(1);
        when(addressMapper.insertAddress(any(AddressEntity.class))).thenReturn(1);

        // When
        Address result = addressService.createAddress(testAddress);

        // Then
        assertNotNull(result);
        assertTrue(result.isDefault());
        verify(addressMapper).clearDefaultByMemberId(testAddress.getMemberId());
        verify(addressMapper).insertAddress(any(AddressEntity.class));
    }

    @Test
    void createAddress_InsertFailed_ShouldThrowException() {
        // Given
        when(addressMapper.insertAddress(any(AddressEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.createAddress(testAddress));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(addressMapper).insertAddress(any(AddressEntity.class));
    }

    @Test
    void updateAddress_Success() {
        // Given
        when(addressMapper.findById(testAddress.getId())).thenReturn(testAddressEntity);
        when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(1);

        // When
        Address result = addressService.updateAddress(testAddress);

        // Then
        assertNotNull(result);
        assertEquals(testAddress.getReceiverName(), result.getReceiverName());
        verify(addressMapper).findById(testAddress.getId());
        verify(addressMapper).updateAddress(any(AddressEntity.class));
    }

    @Test
    void updateAddress_WithNullId_ShouldThrowException() {
        // Given
        testAddress.setId(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.updateAddress(testAddress));
        assertEquals(ErrorCode.PARAMETER_ERROR, exception.getErrorCode());
        assertTrue(exception.getMessage().contains("地址ID不能为空"));
    }

    @Test
    void updateAddress_AddressNotFound_ShouldThrowException() {
        // Given
        when(addressMapper.findById(testAddress.getId())).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.updateAddress(testAddress));
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
        verify(addressMapper).findById(testAddress.getId());
    }

    @Test
    void updateAddress_WithDefaultAddress_ShouldClearOtherDefaults() {
        // Given
        testAddress.setDefault(true);
        when(addressMapper.findById(testAddress.getId())).thenReturn(testAddressEntity);
        when(addressMapper.clearDefaultByMemberId(testAddress.getMemberId())).thenReturn(1);
        when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(1);

        // When
        Address result = addressService.updateAddress(testAddress);

        // Then
        assertNotNull(result);
        assertTrue(result.isDefault());
        verify(addressMapper).clearDefaultByMemberId(testAddress.getMemberId());
        verify(addressMapper).updateAddress(any(AddressEntity.class));
    }

    @Test
    void deleteAddress_Success() {
        // Given
        when(addressMapper.findById(testAddress.getId())).thenReturn(testAddressEntity);
        when(addressMapper.deleteAddress(testAddress.getId())).thenReturn(1);

        // When
        assertDoesNotThrow(() -> addressService.deleteAddress(testAddress.getId()));

        // Then
        verify(addressMapper).findById(testAddress.getId());
        verify(addressMapper).deleteAddress(testAddress.getId());
    }

    @Test
    void deleteAddress_AddressNotFound_ShouldThrowException() {
        // Given
        when(addressMapper.findById(testAddress.getId())).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.deleteAddress(testAddress.getId()));
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
        verify(addressMapper).findById(testAddress.getId());
        verify(addressMapper, never()).deleteAddress(any());
    }

    @Test
    void deleteAddress_DeleteFailed_ShouldThrowException() {
        // Given
        when(addressMapper.findById(testAddress.getId())).thenReturn(testAddressEntity);
        when(addressMapper.deleteAddress(testAddress.getId())).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.deleteAddress(testAddress.getId()));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(addressMapper).deleteAddress(testAddress.getId());
    }

    @Test
    void getAddressById_Success() {
        // Given
        when(addressMapper.findById(testAddress.getId())).thenReturn(testAddressEntity);

        // When
        Address result = addressService.getAddressById(testAddress.getId());

        // Then
        assertNotNull(result);
        assertEquals(testAddress.getId(), result.getId());
        assertEquals(testAddress.getReceiverName(), result.getReceiverName());
        verify(addressMapper).findById(testAddress.getId());
    }

    @Test
    void getAddressById_AddressNotFound_ShouldThrowException() {
        // Given
        when(addressMapper.findById(testAddress.getId())).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.getAddressById(testAddress.getId()));
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
        verify(addressMapper).findById(testAddress.getId());
    }

    @Test
    void getAddressesByMemberId_Success() {
        // Given
        Long memberId = 100L;
        List<AddressEntity> entities = Arrays.asList(testAddressEntity);
        when(addressMapper.findByMemberId(memberId)).thenReturn(entities);

        // When
        List<Address> result = addressService.getAddressesByMemberId(memberId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAddress.getReceiverName(), result.get(0).getReceiverName());
        verify(addressMapper).findByMemberId(memberId);
    }

    @Test
    void getAddressesByMemberId_EmptyList() {
        // Given
        Long memberId = 100L;
        when(addressMapper.findByMemberId(memberId)).thenReturn(Arrays.asList());

        // When
        List<Address> result = addressService.getAddressesByMemberId(memberId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressMapper).findByMemberId(memberId);
    }

    @Test
    void getDefaultAddress_Success() {
        // Given
        Long memberId = 100L;
        testAddressEntity.setIsDefault(1);
        when(addressMapper.findDefaultByMemberId(memberId)).thenReturn(testAddressEntity);

        // When
        Address result = addressService.getDefaultAddress(memberId);

        // Then
        assertNotNull(result);
        assertTrue(result.isDefault());
        assertEquals(testAddress.getReceiverName(), result.getReceiverName());
        verify(addressMapper).findDefaultByMemberId(memberId);
    }

    @Test
    void getDefaultAddress_NoDefaultAddress_ShouldReturnNull() {
        // Given
        Long memberId = 100L;
        when(addressMapper.findDefaultByMemberId(memberId)).thenReturn(null);

        // When
        Address result = addressService.getDefaultAddress(memberId);

        // Then
        assertNull(result);
        verify(addressMapper).findDefaultByMemberId(memberId);
    }

    @Test
    void setDefaultAddress_Success() {
        // Given
        Long addressId = 1L;
        Long memberId = 100L;
        when(addressMapper.findById(addressId)).thenReturn(testAddressEntity);
        when(addressMapper.clearDefaultByMemberId(memberId)).thenReturn(1);
        when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(1);

        // When
        assertDoesNotThrow(() -> addressService.setDefaultAddress(addressId, memberId));

        // Then
        verify(addressMapper).findById(addressId);
        verify(addressMapper).clearDefaultByMemberId(memberId);
        verify(addressMapper).updateAddress(any(AddressEntity.class));
    }

    @Test
    void setDefaultAddress_AddressNotFound_ShouldThrowException() {
        // Given
        Long addressId = 1L;
        Long memberId = 100L;
        when(addressMapper.findById(addressId)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.setDefaultAddress(addressId, memberId));
        assertEquals(ErrorCode.ADDRESS_NOT_FOUND, exception.getErrorCode());
        verify(addressMapper).findById(addressId);
        verify(addressMapper, never()).clearDefaultByMemberId(any());
    }

    @Test
    void setDefaultAddress_AddressNotBelongToMember_ShouldThrowException() {
        // Given
        Long addressId = 1L;
        Long memberId = 200L; // 不同的用户ID
        testAddressEntity.setMemberId(100L); // 地址属于用户100
        when(addressMapper.findById(addressId)).thenReturn(testAddressEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> addressService.setDefaultAddress(addressId, memberId));
        assertEquals(ErrorCode.ADDRESS_NOT_BELONG_TO_MEMBER, exception.getErrorCode());
        verify(addressMapper).findById(addressId);
        verify(addressMapper, never()).clearDefaultByMemberId(any());
    }

    @Test
    void getCurrentUserAddresses_Success() {
        // Given
        when(memberService.getLoginMember()).thenReturn(testMember);
        List<AddressEntity> entities = Arrays.asList(testAddressEntity);
        when(addressMapper.findByMemberId(testMember.getId())).thenReturn(entities);

        // When
        List<Address> result = addressService.getCurrentUserAddresses();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testAddress.getReceiverName(), result.get(0).getReceiverName());
        verify(memberService).getLoginMember();
        verify(addressMapper).findByMemberId(testMember.getId());
    }

    @Test
    void getCurrentUserDefaultAddress_Success() {
        // Given
        testAddressEntity.setIsDefault(1);
        when(memberService.getLoginMember()).thenReturn(testMember);
        when(addressMapper.findDefaultByMemberId(testMember.getId())).thenReturn(testAddressEntity);

        // When
        Address result = addressService.getCurrentUserDefaultAddress();

        // Then
        assertNotNull(result);
        assertTrue(result.isDefault());
        assertEquals(testAddress.getReceiverName(), result.getReceiverName());
        verify(memberService).getLoginMember();
        verify(addressMapper).findDefaultByMemberId(testMember.getId());
    }

    @Test
    void getCurrentUserDefaultAddress_NoDefaultAddress_ShouldReturnNull() {
        // Given
        when(memberService.getLoginMember()).thenReturn(testMember);
        when(addressMapper.findDefaultByMemberId(testMember.getId())).thenReturn(null);

        // When
        Address result = addressService.getCurrentUserDefaultAddress();

        // Then
        assertNull(result);
        verify(memberService).getLoginMember();
        verify(addressMapper).findDefaultByMemberId(testMember.getId());
    }
}