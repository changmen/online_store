package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Address;
import com.example.onlinestore.bean.Member;
import com.example.onlinestore.bean.MemberBaseInfo;
import com.example.onlinestore.dto.CreateAddressRequest;
import com.example.onlinestore.dto.UpdateAddressRequest;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.enums.GenderType;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AddressServiceImpl 单元测试类
 * 
 * 测试策略：
 * 1. 使用 Mockito 模拟依赖组件
 * 2. 测试正常业务流程和异常场景
 * 3. 验证业务逻辑正确性和异常处理
 * 4. 确保数据验证和权限控制
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AddressServiceImpl 单元测试")
class AddressServiceImplTest {
    
    @Mock
    private AddressMapper addressMapper;
    
    @Mock
    private MemberService memberService;
    
    @Mock
    private MessageSource messageSource;
    
    @InjectMocks
    private AddressServiceImpl addressService;
    
    // 测试数据常量
    private static final Long MEMBER_ID = 1L;
    private static final Long ADDRESS_ID = 100L;
    private static final Long NON_EXISTENT_ADDRESS_ID = 999L;
    private static final Long NON_EXISTENT_MEMBER_ID = 999L;
    
    private Member testMember;
    private AddressEntity testAddressEntity;
    private CreateAddressRequest createAddressRequest;
    private UpdateAddressRequest updateAddressRequest;
    
    @BeforeEach
    void setUp() {
        setupTestData();
    }
    
    /**
     * 初始化测试数据
     */
    private void setupTestData() {
        // 创建测试会员
        testMember = createTestMember();
        
        // 创建测试地址实体
        testAddressEntity = createTestAddressEntity();
        
        // 创建测试请求对象
        createAddressRequest = createTestCreateAddressRequest();
        updateAddressRequest = createTestUpdateAddressRequest();
    }
    
    /**
     * 创建地址功能测试
     */
    @Nested
    @DisplayName("创建地址功能测试")
    class CreateAddressTests {
        
        @Test
        @DisplayName("正常创建地址 - 非默认地址")
        void createAddress_Success_NonDefault() {
            // Given
            when(memberService.getMemberById(MEMBER_ID)).thenReturn(testMember);
            when(addressMapper.countByMemberId(MEMBER_ID)).thenReturn(5);
            when(addressMapper.insertAddress(any(AddressEntity.class))).thenAnswer(invocation -> {
                AddressEntity entity = invocation.getArgument(0);
                entity.setId(ADDRESS_ID);
                return 1;
            });
            
            createAddressRequest.setIsDefault(false);
            
            // When
            Address result = addressService.createAddress(createAddressRequest, MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(ADDRESS_ID);
            assertThat(result.getMemberId()).isEqualTo(MEMBER_ID);
            assertThat(result.getReceiverName()).isEqualTo(createAddressRequest.getReceiverName());
            assertThat(result.getIsDefault()).isFalse();
            
            // 验证交互
            verify(memberService).getMemberById(MEMBER_ID);
            verify(addressMapper).countByMemberId(MEMBER_ID);
            verify(addressMapper).insertAddress(any(AddressEntity.class));
            verify(addressMapper, never()).clearDefaultAddress(MEMBER_ID);
        }
        
        @Test
        @DisplayName("正常创建地址 - 默认地址")
        void createAddress_Success_DefaultAddress() {
            // Given
            when(memberService.getMemberById(MEMBER_ID)).thenReturn(testMember);
            when(addressMapper.countByMemberId(MEMBER_ID)).thenReturn(3);
            when(addressMapper.clearDefaultAddress(MEMBER_ID)).thenReturn(1);
            when(addressMapper.insertAddress(any(AddressEntity.class))).thenAnswer(invocation -> {
                AddressEntity entity = invocation.getArgument(0);
                entity.setId(ADDRESS_ID);
                return 1;
            });
            
            createAddressRequest.setIsDefault(true);
            
            // When
            Address result = addressService.createAddress(createAddressRequest, MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(ADDRESS_ID);
            assertThat(result.getIsDefault()).isTrue();
            
            // 验证清除默认地址操作
            verify(addressMapper).clearDefaultAddress(MEMBER_ID);
            verify(addressMapper).insertAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("创建地址失败 - 用户不存在")
        void createAddress_Fail_MemberNotFound() {
            // Given
            when(memberService.getMemberById(NON_EXISTENT_MEMBER_ID))
                    .thenThrow(new BizException(ErrorCode.MEMBER_NOT_FOUND));
            
            // When & Then
            assertThatThrownBy(() -> addressService.createAddress(createAddressRequest, NON_EXISTENT_MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
            
            verify(memberService).getMemberById(NON_EXISTENT_MEMBER_ID);
            verify(addressMapper, never()).insertAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("创建地址失败 - 地址数量超过限制")
        void createAddress_Fail_AddressLimitExceeded() {
            // Given
            when(memberService.getMemberById(MEMBER_ID)).thenReturn(testMember);
            when(addressMapper.countByMemberId(MEMBER_ID)).thenReturn(20); // 达到限制
            
            // When & Then
            assertThatThrownBy(() -> addressService.createAddress(createAddressRequest, MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ADDRESS_LIMIT_EXCEEDED);
            
            verify(memberService).getMemberById(MEMBER_ID);
            verify(addressMapper).countByMemberId(MEMBER_ID);
            verify(addressMapper, never()).insertAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("创建地址失败 - 数据库插入失败")
        void createAddress_Fail_DatabaseInsertError() {
            // Given
            when(memberService.getMemberById(MEMBER_ID)).thenReturn(testMember);
            when(addressMapper.countByMemberId(MEMBER_ID)).thenReturn(5);
            when(addressMapper.insertAddress(any(AddressEntity.class))).thenReturn(0); // 插入失败
            
            // When & Then
            assertThatThrownBy(() -> addressService.createAddress(createAddressRequest, MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR);
            
            verify(addressMapper).insertAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("创建地址 - 参数验证测试")
        void createAddress_InvalidParameters() {
            // When & Then - 空的请求对象
            assertThatThrownBy(() -> addressService.createAddress(null, MEMBER_ID))
                    .isInstanceOf(Exception.class); // 参数校验异常
            
            // When & Then - 空的用户ID
            assertThatThrownBy(() -> addressService.createAddress(createAddressRequest, null))
                    .isInstanceOf(Exception.class); // 参数校验异常
        }
    }
    
    /**
     * 更新地址功能测试
     */
    @Nested
    @DisplayName("更新地址功能测试")
    class UpdateAddressTests {
        
        @Test
        @DisplayName("正常更新地址 - 非默认地址")
        void updateAddress_Success_NonDefault() {
            // Given
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(testAddressEntity);
            when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(1);
            
            updateAddressRequest.setReceiverName("新收货人");
            updateAddressRequest.setIsDefault(false);
            
            // When
            Address result = addressService.updateAddress(updateAddressRequest, MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getReceiverName()).isEqualTo("新收货人");
            assertThat(result.getIsDefault()).isFalse();
            
            verify(addressMapper).findByIdAndMemberId(ADDRESS_ID, MEMBER_ID);
            verify(addressMapper).updateAddress(any(AddressEntity.class));
            verify(addressMapper, never()).clearDefaultAddress(MEMBER_ID);
        }
        
        @Test
        @DisplayName("正常更新地址 - 设置为默认地址")
        void updateAddress_Success_SetAsDefault() {
            // Given
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(testAddressEntity);
            when(addressMapper.clearDefaultAddress(MEMBER_ID)).thenReturn(1);
            when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(1);
            
            updateAddressRequest.setIsDefault(true);
            
            // When
            Address result = addressService.updateAddress(updateAddressRequest, MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getIsDefault()).isTrue();
            
            verify(addressMapper).clearDefaultAddress(MEMBER_ID);
            verify(addressMapper).updateAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("更新地址失败 - 地址不存在或无权限")
        void updateAddress_Fail_AddressNotFoundOrAccessDenied() {
            // Given
            when(addressMapper.findByIdAndMemberId(NON_EXISTENT_ADDRESS_ID, MEMBER_ID)).thenReturn(null);
            
            updateAddressRequest.setId(NON_EXISTENT_ADDRESS_ID);
            
            // When & Then
            assertThatThrownBy(() -> addressService.updateAddress(updateAddressRequest, MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ADDRESS_ACCESS_DENIED);
            
            verify(addressMapper).findByIdAndMemberId(NON_EXISTENT_ADDRESS_ID, MEMBER_ID);
            verify(addressMapper, never()).updateAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("更新地址失败 - 数据库更新失败")
        void updateAddress_Fail_DatabaseUpdateError() {
            // Given
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(testAddressEntity);
            when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(0); // 更新失败
            
            // When & Then
            assertThatThrownBy(() -> addressService.updateAddress(updateAddressRequest, MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR);
            
            verify(addressMapper).updateAddress(any(AddressEntity.class));
        }
    }
    
    /**
     * 查询地址功能测试
     */
    @Nested
    @DisplayName("查询地址功能测试")
    class GetAddressTests {
        
        @Test
        @DisplayName("根据ID查询地址 - 成功")
        void getAddressById_Success() {
            // Given
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(testAddressEntity);
            
            // When
            Address result = addressService.getAddressById(ADDRESS_ID, MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(ADDRESS_ID);
            assertThat(result.getMemberId()).isEqualTo(MEMBER_ID);
            
            verify(addressMapper).findByIdAndMemberId(ADDRESS_ID, MEMBER_ID);
        }
        
        @Test
        @DisplayName("根据ID查询地址 - 地址不存在")
        void getAddressById_NotFound() {
            // Given
            when(addressMapper.findByIdAndMemberId(NON_EXISTENT_ADDRESS_ID, MEMBER_ID)).thenReturn(null);
            
            // When & Then
            assertThatThrownBy(() -> addressService.getAddressById(NON_EXISTENT_ADDRESS_ID, MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);
            
            verify(addressMapper).findByIdAndMemberId(NON_EXISTENT_ADDRESS_ID, MEMBER_ID);
        }
        
        @Test
        @DisplayName("根据用户ID查询地址列表 - 成功")
        void getAddressesByMemberId_Success() {
            // Given
            AddressEntity address1 = createTestAddressEntity();
            address1.setId(1L);
            AddressEntity address2 = createTestAddressEntity();
            address2.setId(2L);
            
            when(memberService.getMemberById(MEMBER_ID)).thenReturn(testMember);
            when(addressMapper.findByMemberId(MEMBER_ID)).thenReturn(Arrays.asList(address1, address2));
            
            // When
            List<Address> result = addressService.getAddressesByMemberId(MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            assertThat(result.get(1).getId()).isEqualTo(2L);
            
            verify(memberService).getMemberById(MEMBER_ID);
            verify(addressMapper).findByMemberId(MEMBER_ID);
        }
        
        @Test
        @DisplayName("根据用户ID查询地址列表 - 用户不存在")
        void getAddressesByMemberId_MemberNotFound() {
            // Given
            when(memberService.getMemberById(NON_EXISTENT_MEMBER_ID))
                    .thenThrow(new BizException(ErrorCode.MEMBER_NOT_FOUND));
            
            // When & Then
            assertThatThrownBy(() -> addressService.getAddressesByMemberId(NON_EXISTENT_MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
            
            verify(memberService).getMemberById(NON_EXISTENT_MEMBER_ID);
            verify(addressMapper, never()).findByMemberId(NON_EXISTENT_MEMBER_ID);
        }
        
        @Test
        @DisplayName("获取默认地址 - 成功")
        void getDefaultAddress_Success() {
            // Given
            when(memberService.getMemberById(MEMBER_ID)).thenReturn(testMember);
            when(addressMapper.findDefaultByMemberId(MEMBER_ID)).thenReturn(testAddressEntity);
            
            // When
            Address result = addressService.getDefaultAddress(MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getIsDefault()).isTrue();
            
            verify(memberService).getMemberById(MEMBER_ID);
            verify(addressMapper).findDefaultByMemberId(MEMBER_ID);
        }
        
        @Test
        @DisplayName("获取默认地址 - 无默认地址")
        void getDefaultAddress_NoDefaultAddress() {
            // Given
            when(memberService.getMemberById(MEMBER_ID)).thenReturn(testMember);
            when(addressMapper.findDefaultByMemberId(MEMBER_ID)).thenReturn(null);
            
            // When
            Address result = addressService.getDefaultAddress(MEMBER_ID);
            
            // Then
            assertThat(result).isNull();
            
            verify(memberService).getMemberById(MEMBER_ID);
            verify(addressMapper).findDefaultByMemberId(MEMBER_ID);
        }
    }
    
    /**
     * 设置默认地址功能测试
     */
    @Nested
    @DisplayName("设置默认地址功能测试")
    class SetDefaultAddressTests {
        
        @Test
        @DisplayName("设置默认地址 - 成功")
        void setDefaultAddress_Success() {
            // Given
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(testAddressEntity);
            when(addressMapper.clearDefaultAddress(MEMBER_ID)).thenReturn(1);
            when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(1);
            
            // When
            Address result = addressService.setDefaultAddress(ADDRESS_ID, MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getIsDefault()).isTrue();
            
            verify(addressMapper).findByIdAndMemberId(ADDRESS_ID, MEMBER_ID);
            verify(addressMapper).clearDefaultAddress(MEMBER_ID);
            verify(addressMapper).updateAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("设置默认地址失败 - 地址不存在")
        void setDefaultAddress_Fail_AddressNotFound() {
            // Given
            when(addressMapper.findByIdAndMemberId(NON_EXISTENT_ADDRESS_ID, MEMBER_ID)).thenReturn(null);
            
            // When & Then
            assertThatThrownBy(() -> addressService.setDefaultAddress(NON_EXISTENT_ADDRESS_ID, MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ADDRESS_ACCESS_DENIED);
            
            verify(addressMapper).findByIdAndMemberId(NON_EXISTENT_ADDRESS_ID, MEMBER_ID);
            verify(addressMapper, never()).clearDefaultAddress(MEMBER_ID);
            verify(addressMapper, never()).updateAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("设置默认地址失败 - 数据库更新失败")
        void setDefaultAddress_Fail_DatabaseUpdateError() {
            // Given
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(testAddressEntity);
            when(addressMapper.clearDefaultAddress(MEMBER_ID)).thenReturn(1);
            when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(0); // 更新失败
            
            // When & Then
            assertThatThrownBy(() -> addressService.setDefaultAddress(ADDRESS_ID, MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR);
            
            verify(addressMapper).updateAddress(any(AddressEntity.class));
        }
    }
    
    /**
     * 删除地址功能测试
     */
    @Nested
    @DisplayName("删除地址功能测试")
    class DeleteAddressTests {
        
        @Test
        @DisplayName("删除地址 - 成功")
        void deleteAddress_Success() {
            // Given
            AddressEntity nonDefaultAddress = createTestAddressEntity();
            nonDefaultAddress.setIsDefault(0); // 非默认地址
            
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(nonDefaultAddress);
            when(addressMapper.deleteAddress(ADDRESS_ID, MEMBER_ID)).thenReturn(1);
            
            // When
            addressService.deleteAddress(ADDRESS_ID, MEMBER_ID);
            
            // Then
            verify(addressMapper).findByIdAndMemberId(ADDRESS_ID, MEMBER_ID);
            verify(addressMapper).deleteAddress(ADDRESS_ID, MEMBER_ID);
        }
        
        @Test
        @DisplayName("删除地址 - 默认地址也允许删除")
        void deleteAddress_Success_DefaultAddress() {
            // Given
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(testAddressEntity); // 默认地址
            when(addressMapper.deleteAddress(ADDRESS_ID, MEMBER_ID)).thenReturn(1);
            
            // When
            addressService.deleteAddress(ADDRESS_ID, MEMBER_ID);
            
            // Then
            verify(addressMapper).findByIdAndMemberId(ADDRESS_ID, MEMBER_ID);
            verify(addressMapper).deleteAddress(ADDRESS_ID, MEMBER_ID);
        }
        
        @Test
        @DisplayName("删除地址失败 - 地址不存在或无权限")
        void deleteAddress_Fail_AddressNotFoundOrAccessDenied() {
            // Given
            when(addressMapper.findByIdAndMemberId(NON_EXISTENT_ADDRESS_ID, MEMBER_ID)).thenReturn(null);
            
            // When & Then
            assertThatThrownBy(() -> addressService.deleteAddress(NON_EXISTENT_ADDRESS_ID, MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.ADDRESS_ACCESS_DENIED);
            
            verify(addressMapper).findByIdAndMemberId(NON_EXISTENT_ADDRESS_ID, MEMBER_ID);
            verify(addressMapper, never()).deleteAddress(NON_EXISTENT_ADDRESS_ID, MEMBER_ID);
        }
        
        @Test
        @DisplayName("删除地址失败 - 数据库删除失败")
        void deleteAddress_Fail_DatabaseDeleteError() {
            // Given
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(testAddressEntity);
            when(addressMapper.deleteAddress(ADDRESS_ID, MEMBER_ID)).thenReturn(0); // 删除失败
            
            // When & Then
            assertThatThrownBy(() -> addressService.deleteAddress(ADDRESS_ID, MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR);
            
            verify(addressMapper).deleteAddress(ADDRESS_ID, MEMBER_ID);
        }
    }
    
    /**
     * 地址所有权验证测试
     */
    @Nested
    @DisplayName("地址所有权验证测试")
    class ValidateAddressOwnershipTests {
        
        @Test
        @DisplayName("验证地址所有权 - 拥有权限")
        void validateAddressOwnership_HasPermission() {
            // Given
            when(addressMapper.existsByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(true);
            
            // When
            boolean result = addressService.validateAddressOwnership(ADDRESS_ID, MEMBER_ID);
            
            // Then
            assertThat(result).isTrue();
            verify(addressMapper).existsByIdAndMemberId(ADDRESS_ID, MEMBER_ID);
        }
        
        @Test
        @DisplayName("验证地址所有权 - 无权限")
        void validateAddressOwnership_NoPermission() {
            // Given
            when(addressMapper.existsByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(false);
            
            // When
            boolean result = addressService.validateAddressOwnership(ADDRESS_ID, MEMBER_ID);
            
            // Then
            assertThat(result).isFalse();
            verify(addressMapper).existsByIdAndMemberId(ADDRESS_ID, MEMBER_ID);
        }
    }
    
    /**
     * 边界条件和异常场景测试
     */
    @Nested
    @DisplayName("边界条件和异常场景测试")
    class BoundaryAndExceptionTests {
        
        @Test
        @DisplayName("创建地址 - 临界地址数量测试")
        void createAddress_BoundaryAddressCount() {
            // Given - 19个地址，还可以创建1个
            when(memberService.getMemberById(MEMBER_ID)).thenReturn(testMember);
            when(addressMapper.countByMemberId(MEMBER_ID)).thenReturn(19);
            when(addressMapper.insertAddress(any(AddressEntity.class))).thenAnswer(invocation -> {
                AddressEntity entity = invocation.getArgument(0);
                entity.setId(ADDRESS_ID);
                return 1;
            });
            
            // When
            Address result = addressService.createAddress(createAddressRequest, MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            verify(addressMapper).insertAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("查询地址列表 - 空列表")
        void getAddressesByMemberId_EmptyList() {
            // Given
            when(memberService.getMemberById(MEMBER_ID)).thenReturn(testMember);
            when(addressMapper.findByMemberId(MEMBER_ID)).thenReturn(Collections.emptyList());
            
            // When
            List<Address> result = addressService.getAddressesByMemberId(MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            
            verify(memberService).getMemberById(MEMBER_ID);
            verify(addressMapper).findByMemberId(MEMBER_ID);
        }
        
        @Test
        @DisplayName("MemberService 抛出非 MEMBER_NOT_FOUND 异常")
        void handleMemberServiceOtherException() {
            // Given
            when(memberService.getMemberById(MEMBER_ID))
                    .thenThrow(new BizException(ErrorCode.INTERNAL_SERVER_ERROR));
            
            // When & Then
            assertThatThrownBy(() -> addressService.createAddress(createAddressRequest, MEMBER_ID))
                    .isInstanceOf(BizException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR);
            
            verify(memberService).getMemberById(MEMBER_ID);
        }
        
        @Test
        @DisplayName("更新地址 - 部分字段更新")
        void updateAddress_PartialUpdate() {
            // Given
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(testAddressEntity);
            when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(1);
            
            UpdateAddressRequest partialRequest = new UpdateAddressRequest();
            partialRequest.setId(ADDRESS_ID);
            partialRequest.setReceiverName("新姓名"); // 只更新姓名
            // 其他字段为空，不更新
            
            // When
            Address result = addressService.updateAddress(partialRequest, MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            verify(addressMapper).updateAddress(argThat(entity -> 
                "新姓名".equals(entity.getReceiverName()) &&
                "13912345678".equals(entity.getReceiverPhone()) // 原有字段保持不变
            ));
        }
        
        @Test
        @DisplayName("更新地址 - 空字符串字段不更新")
        void updateAddress_EmptyStringFieldsNotUpdated() {
            // Given
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(testAddressEntity);
            when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(1);
            
            UpdateAddressRequest emptyFieldRequest = new UpdateAddressRequest();
            emptyFieldRequest.setId(ADDRESS_ID);
            emptyFieldRequest.setReceiverName(""); // 空字符串
            emptyFieldRequest.setReceiverPhone("   "); // 空白字符串
            
            // When
            Address result = addressService.updateAddress(emptyFieldRequest, MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            verify(addressMapper).updateAddress(argThat(entity -> 
                "张三".equals(entity.getReceiverName()) && // 原有值保持不变
                "13912345678".equals(entity.getReceiverPhone()) // 原有值保持不变
            ));
        }
    }
    
    /**
     * 并发和事务测试
     */
    @Nested
    @DisplayName("并发和事务测试")
    class ConcurrencyAndTransactionTests {
        
        @Test
        @DisplayName("创建默认地址 - 模拟并发场景")
        void createDefaultAddress_ConcurrencyScenario() {
            // Given
            when(memberService.getMemberById(MEMBER_ID)).thenReturn(testMember);
            when(addressMapper.countByMemberId(MEMBER_ID)).thenReturn(5);
            when(addressMapper.clearDefaultAddress(MEMBER_ID)).thenReturn(2); // 清除了2个默认地址
            when(addressMapper.insertAddress(any(AddressEntity.class))).thenAnswer(invocation -> {
                AddressEntity entity = invocation.getArgument(0);
                entity.setId(ADDRESS_ID);
                return 1;
            });
            
            createAddressRequest.setIsDefault(true);
            
            // When
            Address result = addressService.createAddress(createAddressRequest, MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getIsDefault()).isTrue();
            
            // 验证操作顺序：先清除默认地址，再插入新地址
            var inOrder = inOrder(addressMapper);
            inOrder.verify(addressMapper).clearDefaultAddress(MEMBER_ID);
            inOrder.verify(addressMapper).insertAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("设置默认地址 - 事务一致性测试")
        void setDefaultAddress_TransactionConsistency() {
            // Given
            when(addressMapper.findByIdAndMemberId(ADDRESS_ID, MEMBER_ID)).thenReturn(testAddressEntity);
            when(addressMapper.clearDefaultAddress(MEMBER_ID)).thenReturn(1);
            when(addressMapper.updateAddress(any(AddressEntity.class))).thenReturn(1);
            
            // When
            Address result = addressService.setDefaultAddress(ADDRESS_ID, MEMBER_ID);
            
            // Then
            assertThat(result).isNotNull();
            
            // 验证事务操作顺序
            var inOrder = inOrder(addressMapper);
            inOrder.verify(addressMapper).findByIdAndMemberId(ADDRESS_ID, MEMBER_ID);
            inOrder.verify(addressMapper).clearDefaultAddress(MEMBER_ID);
            inOrder.verify(addressMapper).updateAddress(any(AddressEntity.class));
        }
    }
    
    // 测试数据构建方法
    private Member createTestMember() {
        Member member = new Member();
        member.setId(MEMBER_ID);
        
        MemberBaseInfo baseInfo = new MemberBaseInfo();
        baseInfo.setName("测试用户");
        baseInfo.setPhone("13812345678");
        baseInfo.setGender(GenderType.MALE);
        baseInfo.setAge(25);
        
        member.setBaseInfo(baseInfo);
        return member;
    }
    
    private AddressEntity createTestAddressEntity() {
        AddressEntity entity = new AddressEntity();
        entity.setId(ADDRESS_ID);
        entity.setMemberId(MEMBER_ID);
        entity.setReceiverName("张三");
        entity.setReceiverPhone("13912345678");
        entity.setProvince("江苏省");
        entity.setCity("南京市");
        entity.setDistrict("玄武区");
        entity.setDetailAddress("中山路1号");
        entity.setPostalCode("210000");
        entity.setIsDefault(1);
        entity.setAddressLabel("家");
        entity.setDeleted(0);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
    
    private CreateAddressRequest createTestCreateAddressRequest() {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setReceiverName("李四");
        request.setReceiverPhone("13800138000");
        request.setProvince("北京市");
        request.setCity("北京市");
        request.setDistrict("朝阳区");
        request.setDetailAddress("建国路88号");
        request.setPostalCode("100000");
        request.setIsDefault(false);
        request.setAddressLabel("公司");
        return request;
    }
    
    private UpdateAddressRequest createTestUpdateAddressRequest() {
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setId(ADDRESS_ID);
        request.setReceiverName("王五");
        request.setReceiverPhone("13700137000");
        request.setProvince("上海市");
        request.setCity("上海市");
        request.setDistrict("黄浦区");
        request.setDetailAddress("南京东路100号");
        request.setPostalCode("200000");
        request.setIsDefault(false);
        request.setAddressLabel("办公室");
        return request;
    }
}