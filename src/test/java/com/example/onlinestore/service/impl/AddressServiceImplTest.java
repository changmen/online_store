package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.dto.CreateAddressRequest;
import com.example.onlinestore.dto.UpdateAddressRequest;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.dto.PageRequest;
import com.example.onlinestore.entity.AddressEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AddressMapper;
import com.example.onlinestore.service.MemberService;
import com.example.onlinestore.testdata.AddressEntityBuilder;
import com.example.onlinestore.testdata.AddressRequestBuilder;
import com.example.onlinestore.testdata.MemberBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

/**
 * AddressServiceImpl单元测试类
 * 
 * 测试覆盖范围：
 * 1. 地址创建功能的完整测试用例
 * 2. 地址查询功能的完整测试用例
 * 3. 地址更新功能的完整测试用例
 * 4. 地址删除功能的完整测试用例
 * 5. 异常处理和边界条件测试用例
 * 6. 缓存相关功能测试用例
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AddressServiceImpl单元测试")
class AddressServiceImplTest {
    
    @Mock
    private AddressMapper addressMapper;
    
    @Mock
    private MemberService memberService;
    
    @Mock
    private MessageSource messageSource;
    
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    
    @Mock
    private ValueOperations<String, Object> valueOperations;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private AddressServiceImpl addressService;
    
    private Member testMember;
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_ADDRESS_ID = 100L;
    
    @BeforeEach
    void setUp() {
        // 初始化测试用户
        testMember = MemberBuilder.builder()
                .asTestUser(TEST_USER_ID)
                .buildMember();
        
        // Mock Redis ValueOperations
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        
        // Mock当前登录用户
        when(memberService.getLoginMember()).thenReturn(testMember);
    }
    
    @Nested
    @DisplayName("地址创建功能测试")
    class CreateAddressTests {
        
        @Test
        @DisplayName("创建首个地址 - 应该自动设为默认地址")
        void createFirstAddress_ShouldSetAsDefault() {
            // Given
            CreateAddressRequest request = AddressRequestBuilder.builder()
                    .valid()
                    .asNonDefault() // 请求中设置为非默认
                    .buildCreateRequest();
            
            // Mock用户当前没有地址
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(0);
            given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
            
            // When
            AddressEntity result = addressService.createAddress(request);
            
            // Then
            ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
            then(addressMapper).should().insertAddress(entityCaptor.capture());
            
            AddressEntity capturedEntity = entityCaptor.getValue();
            assertThat(capturedEntity.getIsDefault()).isTrue(); // 应该被设为默认地址
            assertThat(capturedEntity.getMemberId()).isEqualTo(TEST_USER_ID);
            assertThat(capturedEntity.getReceiverName()).isEqualTo(request.getReceiverName());
            
            // 验证清除了默认地址标记
            then(addressMapper).should().clearDefaultByMemberId(TEST_USER_ID);
            
            // 验证清除了缓存
            then(redisTemplate).should().delete("user_addresses:" + TEST_USER_ID);
        }
        
        @Test
        @DisplayName("创建非默认地址 - 用户已有默认地址时")
        void createNonDefaultAddress_WhenUserHasDefaultAddress() {
            // Given
            CreateAddressRequest request = AddressRequestBuilder.builder()
                    .valid()
                    .asNonDefault()
                    .buildCreateRequest();
            
            // Mock用户已有1个地址
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(1);
            given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
            
            // When
            addressService.createAddress(request);
            
            // Then
            ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
            then(addressMapper).should().insertAddress(entityCaptor.capture());
            
            AddressEntity capturedEntity = entityCaptor.getValue();
            assertThat(capturedEntity.getIsDefault()).isFalse(); // 应该不是默认地址
            
            // 验证没有清除默认地址标记（因为不需要设为默认）
            then(addressMapper).should(never()).clearDefaultByMemberId(any());
        }
        
        @Test
        @DisplayName("创建并设为默认地址 - 应该清除原默认地址")
        void createAndSetAsDefault_ShouldClearOriginalDefault() {
            // Given
            CreateAddressRequest request = AddressRequestBuilder.builder()
                    .valid()
                    .asDefault()
                    .buildCreateRequest();
            
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(1);
            given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
            
            // When
            addressService.createAddress(request);
            
            // Then
            ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
            then(addressMapper).should().insertAddress(entityCaptor.capture());
            
            AddressEntity capturedEntity = entityCaptor.getValue();
            assertThat(capturedEntity.getIsDefault()).isTrue();
            
            // 验证清除了原默认地址
            then(addressMapper).should().clearDefaultByMemberId(TEST_USER_ID);
        }
        
        @Test
        @DisplayName("地址数量超限 - 应该抛出异常")
        void createAddress_WhenLimitExceeded_ShouldThrowException() {
            // Given
            CreateAddressRequest request = AddressRequestBuilder.builder()
                    .valid()
                    .buildCreateRequest();
            
            // Mock用户已有20个地址（达到上限）
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(20);
            
            // When & Then
            assertThatThrownBy(() -> addressService.createAddress(request))
                    .isInstanceOf(BizException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_LIMIT_EXCEEDED);
            
            // 验证没有尝试插入地址
            then(addressMapper).should(never()).insertAddress(any());
        }
        
        @Test
        @DisplayName("数据库插入失败 - 应该抛出系统异常")
        void createAddress_WhenInsertFails_ShouldThrowSystemException() {
            // Given
            CreateAddressRequest request = AddressRequestBuilder.builder()
                    .valid()
                    .buildCreateRequest();
            
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(0);
            given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(0); // 插入失败
            
            // When & Then
            assertThatThrownBy(() -> addressService.createAddress(request))
                    .isInstanceOf(BizException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Nested
    @DisplayName("地址查询功能测试")
    class GetAddressTests {
        
        @Test
        @DisplayName("根据ID查询地址 - 缓存命中")
        void getAddressById_WhenCacheHit_ShouldReturnCachedAddress() {
            // Given
            AddressEntity cachedAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .valid()
                    .build();
            
            // Mock地址归属验证
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            
            // Mock缓存命中
            given(valueOperations.get("address:" + TEST_ADDRESS_ID)).willReturn(cachedAddress);
            
            // When
            AddressEntity result = addressService.getAddressById(TEST_ADDRESS_ID);
            
            // Then
            assertThat(result).isEqualTo(cachedAddress);
            
            // 验证没有查询数据库
            then(addressMapper).should(never()).findById(any());
            
            // 验证没有设置缓存（因为已经存在）
            then(valueOperations).should(never()).set(anyString(), any(), anyLong(), any(TimeUnit.class));
        }
        
        @Test
        @DisplayName("根据ID查询地址 - 缓存未命中，从数据库查询")
        void getAddressById_WhenCacheMiss_ShouldQueryDatabaseAndCache() {
            // Given
            AddressEntity dbAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .valid()
                    .build();
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(valueOperations.get("address:" + TEST_ADDRESS_ID)).willReturn(null); // 缓存未命中
            given(addressMapper.findById(TEST_ADDRESS_ID)).willReturn(dbAddress);
            
            // When
            AddressEntity result = addressService.getAddressById(TEST_ADDRESS_ID);
            
            // Then
            assertThat(result).isEqualTo(dbAddress);
            
            // 验证查询了数据库
            then(addressMapper).should().findById(TEST_ADDRESS_ID);
            
            // 验证设置了缓存
            then(valueOperations).should().set("address:" + TEST_ADDRESS_ID, dbAddress, 3600L, TimeUnit.SECONDS);
        }
        
        @Test
        @DisplayName("查询不存在的地址 - 应该抛出异常")
        void getAddressById_WhenNotExists_ShouldThrowException() {
            // Given
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(false);
            
            // When & Then
            assertThatThrownBy(() -> addressService.getAddressById(TEST_ADDRESS_ID))
                    .isInstanceOf(BizException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_NOT_FOUND);
        }
        
        @Test
        @DisplayName("查询用户所有地址 - 缓存命中")
        void getMyAddresses_WhenCacheHit_ShouldReturnCachedList() {
            // Given
            List<AddressEntity> cachedAddresses = Arrays.asList(
                    AddressEntityBuilder.builder().id(1L).memberId(TEST_USER_ID).build(),
                    AddressEntityBuilder.builder().id(2L).memberId(TEST_USER_ID).build()
            );
            
            given(valueOperations.get("user_addresses:" + TEST_USER_ID)).willReturn(cachedAddresses);
            
            // When
            List<AddressEntity> result = addressService.getMyAddresses();
            
            // Then
            assertThat(result).isEqualTo(cachedAddresses);
            assertThat(result).hasSize(2);
            
            // 验证没有查询数据库
            then(addressMapper).should(never()).findByMemberId(any());
        }
        
        @Test
        @DisplayName("查询用户所有地址 - 缓存未命中")
        void getMyAddresses_WhenCacheMiss_ShouldQueryDatabaseAndCache() {
            // Given
            List<AddressEntity> dbAddresses = Arrays.asList(
                    AddressEntityBuilder.builder().id(1L).memberId(TEST_USER_ID).build(),
                    AddressEntityBuilder.builder().id(2L).memberId(TEST_USER_ID).build()
            );
            
            given(valueOperations.get("user_addresses:" + TEST_USER_ID)).willReturn(null);
            given(addressMapper.findByMemberId(TEST_USER_ID)).willReturn(dbAddresses);
            
            // When
            List<AddressEntity> result = addressService.getMyAddresses();
            
            // Then
            assertThat(result).isEqualTo(dbAddresses);
            
            // 验证查询了数据库
            then(addressMapper).should().findByMemberId(TEST_USER_ID);
            
            // 验证设置了缓存
            then(valueOperations).should().set("user_addresses:" + TEST_USER_ID, dbAddresses, 3600L, TimeUnit.SECONDS);
        }
        
        @Test
        @DisplayName("查询用户默认地址")
        void getMyDefaultAddress_ShouldReturnDefaultAddress() {
            // Given
            AddressEntity defaultAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .asDefault()
                    .build();
            
            given(addressMapper.findDefaultByMemberId(TEST_USER_ID)).willReturn(defaultAddress);
            
            // When
            AddressEntity result = addressService.getMyDefaultAddress();
            
            // Then
            assertThat(result).isEqualTo(defaultAddress);
            assertThat(result.getIsDefault()).isTrue();
        }
        
        @Test
        @DisplayName("查询用户默认地址 - 无默认地址时返回null")
        void getMyDefaultAddress_WhenNoDefault_ShouldReturnNull() {
            // Given
            given(addressMapper.findDefaultByMemberId(TEST_USER_ID)).willReturn(null);
            
            // When
            AddressEntity result = addressService.getMyDefaultAddress();
            
            // Then
            assertThat(result).isNull();
        }
        
        @Test
        @DisplayName("分页查询用户地址")
        void getMyAddressesPaged_ShouldReturnPagedResult() {
            // Given
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPageNum(1);
            pageRequest.setPageSize(10);
            
            List<AddressEntity> addresses = Arrays.asList(
                    AddressEntityBuilder.builder().id(1L).memberId(TEST_USER_ID).build(),
                    AddressEntityBuilder.builder().id(2L).memberId(TEST_USER_ID).build()
            );
            
            given(addressMapper.findByMemberIdWithPaging(TEST_USER_ID, 0, 10)).willReturn(addresses);
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(15);
            
            // When
            Page<AddressEntity> result = addressService.getMyAddressesPaged(pageRequest);
            
            // Then
            assertThat(result.getList()).isEqualTo(addresses);
            assertThat(result.getTotal()).isEqualTo(15);
            assertThat(result.getPageNum()).isEqualTo(1);
            assertThat(result.getPageSize()).isEqualTo(10);
        }
    }
    
    @Nested
    @DisplayName("地址更新功能测试")
    class UpdateAddressTests {
        
        @Test
        @DisplayName("更新地址基本信息 - 不改变默认状态")
        void updateAddress_BasicInfo_ShouldUpdateCorrectly() {
            // Given
            UpdateAddressRequest request = AddressRequestBuilder.builder()
                    .receiverName("新收货人")
                    .phone("13999999999")
                    .province("浙江省")
                    .city("杭州市")
                    .detailAddress("西湖区123号")
                    .asNonDefault()
                    .buildUpdateRequest();
            
            AddressEntity existingAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .asNonDefault()
                    .build();
            
            AddressEntity updatedAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .receiverName("新收货人")
                    .phone("13999999999")
                    .asNonDefault()
                    .build();
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(addressMapper.findById(TEST_ADDRESS_ID)).willReturn(existingAddress, updatedAddress);
            given(addressMapper.updateAddress(any(AddressEntity.class))).willReturn(1);
            
            // When
            AddressEntity result = addressService.updateAddress(TEST_ADDRESS_ID, request);
            
            // Then
            assertThat(result).isEqualTo(updatedAddress);
            
            // 验证更新操作
            ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
            then(addressMapper).should().updateAddress(entityCaptor.capture());
            
            AddressEntity capturedEntity = entityCaptor.getValue();
            assertThat(capturedEntity.getReceiverName()).isEqualTo("新收货人");
            assertThat(capturedEntity.getPhone()).isEqualTo("13999999999");
            assertThat(capturedEntity.getIsDefault()).isFalse();
            
            // 验证没有清除默认地址标记（因为不是设为默认）
            then(addressMapper).should(never()).clearDefaultByMemberId(any());
            
            // 验证清除了缓存
            then(redisTemplate).should().delete("address:" + TEST_ADDRESS_ID);
            then(redisTemplate).should().delete("user_addresses:" + TEST_USER_ID);
        }
        
        @Test
        @DisplayName("设置为默认地址 - 应该清除原默认地址")
        void updateAddress_SetAsDefault_ShouldClearOriginalDefault() {
            // Given
            UpdateAddressRequest request = AddressRequestBuilder.builder()
                    .valid()
                    .asDefault() // 设置为默认地址
                    .buildUpdateRequest();
            
            AddressEntity existingAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .asNonDefault() // 原来不是默认地址
                    .build();
            
            AddressEntity updatedAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .asDefault()
                    .build();
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(addressMapper.findById(TEST_ADDRESS_ID)).willReturn(existingAddress, updatedAddress);
            given(addressMapper.updateAddress(any(AddressEntity.class))).willReturn(1);
            
            // When
            addressService.updateAddress(TEST_ADDRESS_ID, request);
            
            // Then
            // 验证清除了原默认地址
            then(addressMapper).should().clearDefaultByMemberId(TEST_USER_ID);
            
            // 验证更新操作
            ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
            then(addressMapper).should().updateAddress(entityCaptor.capture());
            
            AddressEntity capturedEntity = entityCaptor.getValue();
            assertThat(capturedEntity.getIsDefault()).isTrue();
        }
        
        @Test
        @DisplayName("更新不存在的地址 - 应该抛出异常")
        void updateAddress_WhenNotExists_ShouldThrowException() {
            // Given
            UpdateAddressRequest request = AddressRequestBuilder.builder()
                    .valid()
                    .buildUpdateRequest();
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(false);
            
            // When & Then
            assertThatThrownBy(() -> addressService.updateAddress(TEST_ADDRESS_ID, request))
                    .isInstanceOf(BizException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_NOT_FOUND);
        }
        
        @Test
        @DisplayName("更新失败 - 应该抛出系统异常")
        void updateAddress_WhenUpdateFails_ShouldThrowSystemException() {
            // Given
            UpdateAddressRequest request = AddressRequestBuilder.builder()
                    .valid()
                    .buildUpdateRequest();
            
            AddressEntity existingAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .build();
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(addressMapper.findById(TEST_ADDRESS_ID)).willReturn(existingAddress);
            given(addressMapper.updateAddress(any(AddressEntity.class))).willReturn(0); // 更新失败
            
            // When & Then
            assertThatThrownBy(() -> addressService.updateAddress(TEST_ADDRESS_ID, request))
                    .isInstanceOf(BizException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Nested
    @DisplayName("设置默认地址功能测试")
    class SetDefaultAddressTests {
        
        @Test
        @DisplayName("设置默认地址 - 成功")
        void setDefaultAddress_ShouldSetSuccessfully() {
            // Given
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(addressMapper.setAsDefault(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(1);
            
            // When
            addressService.setDefaultAddress(TEST_ADDRESS_ID);
            
            // Then
            // 验证清除了原默认地址
            then(addressMapper).should().clearDefaultByMemberId(TEST_USER_ID);
            
            // 验证设置了新的默认地址
            then(addressMapper).should().setAsDefault(TEST_ADDRESS_ID, TEST_USER_ID);
            
            // 验证清除了缓存
            then(redisTemplate).should().delete("address:" + TEST_ADDRESS_ID);
            then(redisTemplate).should().delete("user_addresses:" + TEST_USER_ID);
        }
        
        @Test
        @DisplayName("设置不存在的默认地址 - 应该抛出异常")
        void setDefaultAddress_WhenNotExists_ShouldThrowException() {
            // Given
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(false);
            
            // When & Then
            assertThatThrownBy(() -> addressService.setDefaultAddress(TEST_ADDRESS_ID))
                    .isInstanceOf(BizException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_NOT_FOUND);
        }
        
        @Test
        @DisplayName("设置默认地址失败 - 应该抛出系统异常")
        void setDefaultAddress_WhenSetFails_ShouldThrowSystemException() {
            // Given
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(addressMapper.setAsDefault(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(0); // 设置失败
            
            // When & Then
            assertThatThrownBy(() -> addressService.setDefaultAddress(TEST_ADDRESS_ID))
                    .isInstanceOf(BizException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Nested
    @DisplayName("地址删除功能测试")
    class DeleteAddressTests {
        
        @Test
        @DisplayName("删除普通地址 - 成功")
        void deleteAddress_NormalAddress_ShouldDeleteSuccessfully() {
            // Given
            AddressEntity existingAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .asNonDefault() // 非默认地址
                    .build();
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(addressMapper.findById(TEST_ADDRESS_ID)).willReturn(existingAddress);
            given(addressMapper.deleteById(TEST_ADDRESS_ID)).willReturn(1);
            
            // When
            addressService.deleteAddress(TEST_ADDRESS_ID);
            
            // Then
            then(addressMapper).should().deleteById(TEST_ADDRESS_ID);
            
            // 验证清除了缓存
            then(redisTemplate).should().delete("address:" + TEST_ADDRESS_ID);
            then(redisTemplate).should().delete("user_addresses:" + TEST_USER_ID);
            
            // 验证不需要处理默认地址逻辑
            then(addressMapper).should(never()).setAsDefault(anyLong(), anyLong());
        }
        
        @Test
        @DisplayName("删除默认地址 - 应该重新选择默认地址")
        void deleteAddress_DefaultAddress_ShouldSetNewDefault() {
            // Given
            AddressEntity defaultAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .asDefault() // 默认地址
                    .build();
            
            List<AddressEntity> remainingAddresses = Arrays.asList(
                AddressEntityBuilder.builder().id(101L).memberId(TEST_USER_ID).asNonDefault().build(),
                AddressEntityBuilder.builder().id(102L).memberId(TEST_USER_ID).asNonDefault().build()
            );
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(addressMapper.findById(TEST_ADDRESS_ID)).willReturn(defaultAddress);
            given(addressMapper.deleteById(TEST_ADDRESS_ID)).willReturn(1);
            given(addressMapper.findByMemberId(TEST_USER_ID)).willReturn(remainingAddresses);
            given(addressMapper.setAsDefault(101L, TEST_USER_ID)).willReturn(1);
            
            // When
            addressService.deleteAddress(TEST_ADDRESS_ID);
            
            // Then
            then(addressMapper).should().deleteById(TEST_ADDRESS_ID);
            
            // 验证设置了新的默认地址（第一个剩余地址）
            then(addressMapper).should().setAsDefault(101L, TEST_USER_ID);
            
            // 验证清除了缓存
            then(redisTemplate).should().delete("address:" + TEST_ADDRESS_ID);
            then(redisTemplate).should().delete("address:101"); // 新默认地址的缓存
            then(redisTemplate).should().delete("user_addresses:" + TEST_USER_ID);
        }
        
        @Test
        @DisplayName("删除最后一个地址 - 无需设置默认地址")
        void deleteAddress_LastAddress_ShouldNotSetDefault() {
            // Given
            AddressEntity lastAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .asDefault()
                    .build();
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(addressMapper.findById(TEST_ADDRESS_ID)).willReturn(lastAddress);
            given(addressMapper.deleteById(TEST_ADDRESS_ID)).willReturn(1);
            given(addressMapper.findByMemberId(TEST_USER_ID)).willReturn(Collections.emptyList()); // 无剩余地址
            
            // When
            addressService.deleteAddress(TEST_ADDRESS_ID);
            
            // Then
            then(addressMapper).should().deleteById(TEST_ADDRESS_ID);
            
            // 验证不设置新的默认地址（因为没有剩余地址）
            then(addressMapper).should(never()).setAsDefault(anyLong(), anyLong());
        }
        
        @Test
        @DisplayName("删除不存在的地址 - 应该抛出异常")
        void deleteAddress_WhenNotExists_ShouldThrowException() {
            // Given
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(false);
            
            // When & Then
            assertThatThrownBy(() -> addressService.deleteAddress(TEST_ADDRESS_ID))
                    .isInstanceOf(BizException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_NOT_FOUND);
        }
        
        @Test
        @DisplayName("删除失败 - 应该抛出系统异常")
        void deleteAddress_WhenDeleteFails_ShouldThrowSystemException() {
            // Given
            AddressEntity existingAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .build();
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(addressMapper.findById(TEST_ADDRESS_ID)).willReturn(existingAddress);
            given(addressMapper.deleteById(TEST_ADDRESS_ID)).willReturn(0); // 删除失败
            
            // When & Then
            assertThatThrownBy(() -> addressService.deleteAddress(TEST_ADDRESS_ID))
                    .isInstanceOf(BizException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}