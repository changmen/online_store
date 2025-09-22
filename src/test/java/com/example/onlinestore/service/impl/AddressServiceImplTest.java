package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.dto.CreateAddressRequest;
import com.example.onlinestore.dto.UpdateAddressRequest;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AddressServiceImpl 单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("地址服务实现类测试")
class AddressServiceImplTest {

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Long memberId;
    private CreateAddressRequest createRequest;
    private UpdateAddressRequest updateRequest;
    private AddressEntity addressEntity;

    @BeforeEach
    void setUp() {
        memberId = 1L;
        
        // 准备创建地址请求
        createRequest = new CreateAddressRequest();
        createRequest.setConsigneeName("张三");
        createRequest.setConsigneePhone("13800138000");
        createRequest.setProvince("北京市");
        createRequest.setCity("北京市");
        createRequest.setDistrict("朝阳区");
        createRequest.setDetailAddress("三里屯街道1号");
        createRequest.setPostalCode("100000");
        createRequest.setIsDefault(false);
        createRequest.setTag("家");

        // 准备更新地址请求
        updateRequest = new UpdateAddressRequest();
        updateRequest.setId(1L);
        updateRequest.setConsigneeName("李四");
        updateRequest.setConsigneePhone("13900139000");
        updateRequest.setProvince("上海市");
        updateRequest.setCity("上海市");
        updateRequest.setDistrict("黄浦区");
        updateRequest.setDetailAddress("南京路1号");
        updateRequest.setPostalCode("200000");
        updateRequest.setIsDefault(true);
        updateRequest.setTag("公司");

        // 准备地址实体
        addressEntity = new AddressEntity();
        addressEntity.setId(1L);
        addressEntity.setMemberId(memberId);
        addressEntity.setConsigneeName("张三");
        addressEntity.setConsigneePhone("13800138000");
        addressEntity.setProvince("北京市");
        addressEntity.setCity("北京市");
        addressEntity.setDistrict("朝阳区");
        addressEntity.setDetailAddress("三里屯街道1号");
        addressEntity.setPostalCode("100000");
        addressEntity.setIsDefault(0);
        addressEntity.setTag("家");
        addressEntity.setCreatedAt(LocalDateTime.now());
        addressEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("创建地址 - 成功")
    void createAddress_Success() {
        // Given
        when(addressMapper.countByMemberId(memberId)).thenReturn(5);
        when(addressMapper.insertAddress(any(AddressEntity.class))).thenAnswer(invocation -> {
            AddressEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return 1;
        });

        // When
        Address result = addressService.createAddress(memberId, createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMemberId()).isEqualTo(memberId);
        assertThat(result.getConsigneeName()).isEqualTo(createRequest.getConsigneeName());
        assertThat(result.getConsigneePhone()).isEqualTo(createRequest.getConsigneePhone());
        assertThat(result.getProvince()).isEqualTo(createRequest.getProvince());
        assertThat(result.getCity()).isEqualTo(createRequest.getCity());
        assertThat(result.getDistrict()).isEqualTo(createRequest.getDistrict());
        assertThat(result.getDetailAddress()).isEqualTo(createRequest.getDetailAddress());
        assertThat(result.getPostalCode()).isEqualTo(createRequest.getPostalCode());
        assertThat(result.getIsDefault()).isEqualTo(createRequest.getIsDefault());
        assertThat(result.getTag()).isEqualTo(createRequest.getTag());

        // 验证方法调用
        verify(addressMapper).countByMemberId(memberId);
        verify(addressMapper).insertAddress(any(AddressEntity.class));
        verify(addressMapper, never()).clearDefaultByMemberId(any());
    }

    @Test
    @DisplayName("创建地址 - 设置为默认地址")
    void createAddress_SetAsDefault() {
        // Given
        createRequest.setIsDefault(true);
        when(addressMapper.countByMemberId(memberId)).thenReturn(5);
        when(addressMapper.clearDefaultByMemberId(memberId)).thenReturn(1);
        when(addressMapper.insertAddress(any(AddressEntity.class))).thenAnswer(invocation -> {
            AddressEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return 1;
        });

        // When
        Address result = addressService.createAddress(memberId, createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIsDefault()).isTrue();

        // 验证方法调用
        verify(addressMapper).clearDefaultByMemberId(memberId);
        verify(addressMapper).insertAddress(any(AddressEntity.class));
        
        // 验证传给mapper的实体对象
        ArgumentCaptor<AddressEntity> captor = ArgumentCaptor.forClass(AddressEntity.class);
        verify(addressMapper).insertAddress(captor.capture());
        assertThat(captor.getValue().getIsDefault()).isEqualTo(1);
    }

    @Test
    @DisplayName("创建地址 - 超出数量限制")
    void createAddress_ExceedsLimit() {
        // Given
        when(addressMapper.countByMemberId(memberId)).thenReturn(20);

        // When & Then
        assertThatThrownBy(() -> addressService.createAddress(memberId, createRequest))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.ADDRESS_COUNT_LIMIT_EXCEEDED);

        verify(addressMapper).countByMemberId(memberId);
        verify(addressMapper, never()).insertAddress(any());
    }

    @Test
    @DisplayName("创建地址 - 插入失败")
    void createAddress_InsertFailed() {
        // Given
        when(addressMapper.countByMemberId(memberId)).thenReturn(5);
        when(addressMapper.insertAddress(any(AddressEntity.class))).thenReturn(0);

        // When & Then
        assertThatThrownBy(() -> addressService.createAddress(memberId, createRequest))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR);

        verify(addressMapper).insertAddress(any(AddressEntity.class));
    }

    @Test
    @DisplayName("更新地址 - 成功")
    void updateAddress_Success() {
        // Given
        when(addressMapper.findById(updateRequest.getId())).thenReturn(addressEntity);
        when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(1);
        when(addressMapper.clearDefaultByMemberId(memberId)).thenReturn(1);

        // When
        Address result = addressService.updateAddress(memberId, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(updateRequest.getId());
        assertThat(result.getConsigneeName()).isEqualTo(updateRequest.getConsigneeName());
        assertThat(result.getConsigneePhone()).isEqualTo(updateRequest.getConsigneePhone());
        assertThat(result.getProvince()).isEqualTo(updateRequest.getProvince());
        assertThat(result.getCity()).isEqualTo(updateRequest.getCity());
        assertThat(result.getDistrict()).isEqualTo(updateRequest.getDistrict());
        assertThat(result.getDetailAddress()).isEqualTo(updateRequest.getDetailAddress());
        assertThat(result.getPostalCode()).isEqualTo(updateRequest.getPostalCode());
        assertThat(result.getIsDefault()).isEqualTo(updateRequest.getIsDefault());
        assertThat(result.getTag()).isEqualTo(updateRequest.getTag());

        verify(addressMapper).findById(updateRequest.getId());
        verify(addressMapper).clearDefaultByMemberId(memberId);
        verify(addressMapper).updateAddress(any(AddressEntity.class));
    }

    @Test
    @DisplayName("更新地址 - 地址不存在")
    void updateAddress_NotFound() {
        // Given
        when(addressMapper.findById(updateRequest.getId())).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> addressService.updateAddress(memberId, updateRequest))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);

        verify(addressMapper).findById(updateRequest.getId());
        verify(addressMapper, never()).updateAddress(any());
    }

    @Test
    @DisplayName("更新地址 - 地址不属于该用户")
    void updateAddress_AccessDenied() {
        // Given
        addressEntity.setMemberId(2L); // 不同的用户ID
        when(addressMapper.findById(updateRequest.getId())).thenReturn(addressEntity);

        // When & Then
        assertThatThrownBy(() -> addressService.updateAddress(memberId, updateRequest))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);

        verify(addressMapper).findById(updateRequest.getId());
        verify(addressMapper, never()).updateAddress(any());
    }

    @Test
    @DisplayName("更新地址 - 更新失败")
    void updateAddress_UpdateFailed() {
        // Given
        updateRequest.setIsDefault(false); // 不设置为默认地址
        when(addressMapper.findById(updateRequest.getId())).thenReturn(addressEntity);
        when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(0);

        // When & Then
        assertThatThrownBy(() -> addressService.updateAddress(memberId, updateRequest))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR);

        verify(addressMapper).updateAddress(any(AddressEntity.class));
        verify(addressMapper, never()).clearDefaultByMemberId(any());
    }

    @Test
    @DisplayName("根据ID获取地址 - 成功")
    void getAddressById_Success() {
        // Given
        when(addressMapper.findById(1L)).thenReturn(addressEntity);

        // When
        Address result = addressService.getAddressById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(addressEntity.getId());
        assertThat(result.getMemberId()).isEqualTo(addressEntity.getMemberId());

        verify(addressMapper).findById(1L);
    }

    @Test
    @DisplayName("根据ID获取地址 - 地址不存在")
    void getAddressById_NotFound() {
        // Given
        when(addressMapper.findById(1L)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> addressService.getAddressById(1L))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);

        verify(addressMapper).findById(1L);
    }

    @Test
    @DisplayName("根据ID和用户ID获取地址 - 成功")
    void getAddressByIdAndMemberId_Success() {
        // Given
        when(addressMapper.findById(1L)).thenReturn(addressEntity);

        // When
        Address result = addressService.getAddressByIdAndMemberId(1L, memberId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(addressEntity.getId());
        assertThat(result.getMemberId()).isEqualTo(addressEntity.getMemberId());

        verify(addressMapper).findById(1L);
    }

    @Test
    @DisplayName("根据ID和用户ID获取地址 - 地址不存在")
    void getAddressByIdAndMemberId_NotFound() {
        // Given
        when(addressMapper.findById(1L)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> addressService.getAddressByIdAndMemberId(1L, memberId))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);

        verify(addressMapper).findById(1L);
    }

    @Test
    @DisplayName("根据ID和用户ID获取地址 - 访问权限被拒绝")
    void getAddressByIdAndMemberId_AccessDenied() {
        // Given
        addressEntity.setMemberId(2L); // 不同的用户ID
        when(addressMapper.findById(1L)).thenReturn(addressEntity);

        // When & Then
        assertThatThrownBy(() -> addressService.getAddressByIdAndMemberId(1L, memberId))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);

        verify(addressMapper).findById(1L);
    }

    @Test
    @DisplayName("获取用户所有地址 - 成功")
    void getAddressesByMemberId_Success() {
        // Given
        AddressEntity address1 = new AddressEntity();
        address1.setId(1L);
        address1.setMemberId(memberId);
        address1.setConsigneeName("张三");

        AddressEntity address2 = new AddressEntity();
        address2.setId(2L);
        address2.setMemberId(memberId);
        address2.setConsigneeName("李四");

        when(addressMapper.findByMemberId(memberId)).thenReturn(Arrays.asList(address1, address2));

        // When
        List<Address> result = addressService.getAddressesByMemberId(memberId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);

        verify(addressMapper).findByMemberId(memberId);
    }

    @Test
    @DisplayName("获取用户所有地址 - 空列表")
    void getAddressesByMemberId_EmptyList() {
        // Given
        when(addressMapper.findByMemberId(memberId)).thenReturn(Collections.emptyList());

        // When
        List<Address> result = addressService.getAddressesByMemberId(memberId);

        // Then
        assertThat(result).isEmpty();

        verify(addressMapper).findByMemberId(memberId);
    }

    @Test
    @DisplayName("获取用户默认地址 - 成功")
    void getDefaultAddressByMemberId_Success() {
        // Given
        addressEntity.setIsDefault(1);
        when(addressMapper.findDefaultByMemberId(memberId)).thenReturn(addressEntity);

        // When
        Address result = addressService.getDefaultAddressByMemberId(memberId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(addressEntity.getId());
        assertThat(result.getIsDefault()).isTrue();

        verify(addressMapper).findDefaultByMemberId(memberId);
    }

    @Test
    @DisplayName("获取用户默认地址 - 无默认地址")
    void getDefaultAddressByMemberId_NoDefault() {
        // Given
        when(addressMapper.findDefaultByMemberId(memberId)).thenReturn(null);

        // When
        Address result = addressService.getDefaultAddressByMemberId(memberId);

        // Then
        assertThat(result).isNull();

        verify(addressMapper).findDefaultByMemberId(memberId);
    }

    @Test
    @DisplayName("设置默认地址 - 成功")
    void setDefaultAddress_Success() {
        // Given
        when(addressMapper.findById(1L)).thenReturn(addressEntity);
        when(addressMapper.clearDefaultByMemberId(memberId)).thenReturn(1);
        when(addressMapper.setDefaultAddress(1L, memberId)).thenReturn(1);

        // When
        addressService.setDefaultAddress(1L, memberId);

        // Then
        verify(addressMapper).findById(1L);
        verify(addressMapper).clearDefaultByMemberId(memberId);
        verify(addressMapper).setDefaultAddress(1L, memberId);
    }

    @Test
    @DisplayName("设置默认地址 - 地址不存在")
    void setDefaultAddress_NotFound() {
        // Given
        when(addressMapper.findById(1L)).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> addressService.setDefaultAddress(1L, memberId))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);

        verify(addressMapper).findById(1L);
        verify(addressMapper, never()).clearDefaultByMemberId(any());
        verify(addressMapper, never()).setDefaultAddress(any(), any());
    }

    @Test
    @DisplayName("设置默认地址 - 访问权限被拒绝")
    void setDefaultAddress_AccessDenied() {
        // Given
        addressEntity.setMemberId(2L); // 不同的用户ID
        when(addressMapper.findById(1L)).thenReturn(addressEntity);

        // When & Then
        assertThatThrownBy(() -> addressService.setDefaultAddress(1L, memberId))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);

        verify(addressMapper).findById(1L);
        verify(addressMapper, never()).clearDefaultByMemberId(any());
        verify(addressMapper, never()).setDefaultAddress(any(), any());
    }

    @Test
    @DisplayName("设置默认地址 - 设置失败")
    void setDefaultAddress_SetFailed() {
        // Given
        when(addressMapper.findById(1L)).thenReturn(addressEntity);
        when(addressMapper.clearDefaultByMemberId(memberId)).thenReturn(1);
        when(addressMapper.setDefaultAddress(1L, memberId)).thenReturn(0);

        // When & Then
        assertThatThrownBy(() -> addressService.setDefaultAddress(1L, memberId))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR);

        verify(addressMapper).setDefaultAddress(1L, memberId);
    }

    @Test
    @DisplayName("删除地址 - 成功")
    void deleteAddress_Success() {
        // Given
        when(addressMapper.deleteByIdAndMemberId(1L, memberId)).thenReturn(1);

        // When
        addressService.deleteAddress(1L, memberId);

        // Then
        verify(addressMapper).deleteByIdAndMemberId(1L, memberId);
    }

    @Test
    @DisplayName("删除地址 - 地址不存在或访问权限被拒绝")
    void deleteAddress_NotFoundOrAccessDenied() {
        // Given
        when(addressMapper.deleteByIdAndMemberId(1L, memberId)).thenReturn(0);

        // When & Then
        assertThatThrownBy(() -> addressService.deleteAddress(1L, memberId))
                .isInstanceOf(BizException.class)
                .extracting(ex -> ((BizException) ex).getErrorCode())
                .isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);

        verify(addressMapper).deleteByIdAndMemberId(1L, memberId);
    }

    @Test
    @DisplayName("验证地址所有权 - 成功")
    void validateAddressOwnership_Success() {
        // Given
        when(addressMapper.findById(1L)).thenReturn(addressEntity);

        // When
        boolean result = addressService.validateAddressOwnership(1L, memberId);

        // Then
        assertThat(result).isTrue();

        verify(addressMapper).findById(1L);
    }

    @Test
    @DisplayName("验证地址所有权 - 地址不存在")
    void validateAddressOwnership_NotFound() {
        // Given
        when(addressMapper.findById(1L)).thenReturn(null);

        // When
        boolean result = addressService.validateAddressOwnership(1L, memberId);

        // Then
        assertThat(result).isFalse();

        verify(addressMapper).findById(1L);
    }

    @Test
    @DisplayName("验证地址所有权 - 不属于该用户")
    void validateAddressOwnership_WrongOwner() {
        // Given
        addressEntity.setMemberId(2L); // 不同的用户ID
        when(addressMapper.findById(1L)).thenReturn(addressEntity);

        // When
        boolean result = addressService.validateAddressOwnership(1L, memberId);

        // Then
        assertThat(result).isFalse();

        verify(addressMapper).findById(1L);
    }

    @Test
    @DisplayName("获取用户地址数量")
    void getAddressCountByMemberId() {
        // Given
        when(addressMapper.countByMemberId(memberId)).thenReturn(5);

        // When
        int result = addressService.getAddressCountByMemberId(memberId);

        // Then
        assertThat(result).isEqualTo(5);

        verify(addressMapper).countByMemberId(memberId);
    }

    @Test
    @DisplayName("参数验证 - memberId为null")
    void validateMemberIdNotNull() {
        // When & Then
        assertThatThrownBy(() -> addressService.createAddress(null, createRequest))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("参数验证 - 创建请求为null")
    void validateCreateRequestNotNull() {
        // When & Then
        assertThatThrownBy(() -> addressService.createAddress(memberId, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("参数验证 - 更新请求为null")
    void validateUpdateRequestNotNull() {
        // When & Then
        assertThatThrownBy(() -> addressService.updateAddress(memberId, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("参数验证 - ID为null")
    void validateIdNotNull() {
        // When & Then
        assertThatThrownBy(() -> addressService.getAddressById(null))
                .isInstanceOf(Exception.class);
    }
}