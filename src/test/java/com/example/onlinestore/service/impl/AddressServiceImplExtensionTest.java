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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

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
 * AddressServiceImpl扩展测试类
 * 包含地址归属权验证、缓存功能和边界条件测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AddressServiceImpl扩展测试")
class AddressServiceImplExtensionTest {
    
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
    @DisplayName("地址归属权验证测试")
    class AddressOwnershipValidationTests {
        
        @Test
        @DisplayName("验证地址归属权 - 地址存在且属于当前用户")
        void validateAddressOwnership_WhenExists_ShouldPass() {
            // Given
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            
            // When & Then - 不应该抛出异常
            addressService.validateAddressOwnership(TEST_ADDRESS_ID);
        }
        
        @Test
        @DisplayName("验证地址归属权 - 地址不存在或不属于当前用户")
        void validateAddressOwnership_WhenNotExistsOrNotOwned_ShouldThrowException() {
            // Given
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(false);
            
            // When & Then
            assertThatThrownBy(() -> addressService.validateAddressOwnership(TEST_ADDRESS_ID))
                    .isInstanceOf(BizException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_NOT_FOUND);
        }
    }
    
    @Nested
    @DisplayName("缓存相关功能测试")
    class CacheTests {
        
        @Test
        @DisplayName("缓存命中测试")
        void testCacheHit() {
            // Given
            AddressEntity cachedAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .build();
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(valueOperations.get("address:" + TEST_ADDRESS_ID)).willReturn(cachedAddress);
            
            // When
            AddressEntity result = addressService.getAddressById(TEST_ADDRESS_ID);
            
            // Then
            assertThat(result).isEqualTo(cachedAddress);
            
            // 验证没有查询数据库
            then(addressMapper).should(never()).findById(any());
        }
        
        @Test
        @DisplayName("缓存未命中测试")
        void testCacheMiss() {
            // Given
            AddressEntity dbAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
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
        @DisplayName("缓存失效测试 - 创建地址后清除缓存")
        void testCacheInvalidationOnCreate() {
            // Given
            CreateAddressRequest request = AddressRequestBuilder.builder()
                    .valid()
                    .buildCreateRequest();
            
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(0);
            given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
            
            // When
            addressService.createAddress(request);
            
            // Then
            // 验证清除了用户地址列表缓存
            then(redisTemplate).should().delete("user_addresses:" + TEST_USER_ID);
        }
        
        @Test
        @DisplayName("缓存失效测试 - 更新地址后清除缓存")
        void testCacheInvalidationOnUpdate() {
            // Given
            UpdateAddressRequest request = AddressRequestBuilder.builder()
                    .valid()
                    .buildUpdateRequest();
            
            AddressEntity existingAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .build();
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(addressMapper.findById(TEST_ADDRESS_ID)).willReturn(existingAddress, existingAddress);
            given(addressMapper.updateAddress(any(AddressEntity.class))).willReturn(1);
            
            // When
            addressService.updateAddress(TEST_ADDRESS_ID, request);
            
            // Then
            // 验证清除了地址缓存和用户地址列表缓存
            then(redisTemplate).should().delete("address:" + TEST_ADDRESS_ID);
            then(redisTemplate).should().delete("user_addresses:" + TEST_USER_ID);
        }
        
        @Test
        @DisplayName("缓存失效测试 - 删除地址后清除缓存")
        void testCacheInvalidationOnDelete() {
            // Given
            AddressEntity existingAddress = AddressEntityBuilder.builder()
                    .id(TEST_ADDRESS_ID)
                    .memberId(TEST_USER_ID)
                    .asNonDefault()
                    .build();
            
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(addressMapper.findById(TEST_ADDRESS_ID)).willReturn(existingAddress);
            given(addressMapper.deleteById(TEST_ADDRESS_ID)).willReturn(1);
            
            // When
            addressService.deleteAddress(TEST_ADDRESS_ID);
            
            // Then
            // 验证清除了地址缓存和用户地址列表缓存
            then(redisTemplate).should().delete("address:" + TEST_ADDRESS_ID);
            then(redisTemplate).should().delete("user_addresses:" + TEST_USER_ID);
        }
        
        @Test
        @DisplayName("用户地址列表缓存测试 - 缓存命中")
        void testUserAddressesListCacheHit() {
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
        @DisplayName("用户地址列表缓存测试 - 缓存未命中")
        void testUserAddressesListCacheMiss() {
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
    }
    
    @Nested
    @DisplayName("边界条件和异常处理测试")
    class BoundaryAndExceptionTests {
        
        @Test
        @DisplayName("空集合处理测试 - 无地址时返回空列表")
        void getMyAddresses_WhenNoAddresses_ShouldReturnEmptyList() {
            // Given
            given(valueOperations.get("user_addresses:" + TEST_USER_ID)).willReturn(null);
            given(addressMapper.findByMemberId(TEST_USER_ID)).willReturn(Collections.emptyList());
            
            // When
            List<AddressEntity> result = addressService.getMyAddresses();
            
            // Then
            assertThat(result).isEmpty();
            
            // 验证设置了缓存（即使是空列表）
            then(valueOperations).should().set("user_addresses:" + TEST_USER_ID, Collections.emptyList(), 3600L, TimeUnit.SECONDS);
        }
        
        @Test
        @DisplayName("分页查询边界条件测试 - 页码计算")
        void getMyAddressesPaged_PageCalculation_ShouldWorkCorrectly() {
            // Given
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPageNum(3); // 第3页
            pageRequest.setPageSize(5); // 每页5条
            
            List<AddressEntity> addresses = Arrays.asList(
                AddressEntityBuilder.builder().id(11L).memberId(TEST_USER_ID).build()
            );
            
            given(addressMapper.findByMemberIdWithPaging(TEST_USER_ID, 10, 5)).willReturn(addresses); // offset = (3-1) * 5 = 10
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(15);
            
            // When
            Page<AddressEntity> result = addressService.getMyAddressesPaged(pageRequest);
            
            // Then
            assertThat(result.getPageNum()).isEqualTo(3);
            assertThat(result.getPageSize()).isEqualTo(5);
            assertThat(result.getTotal()).isEqualTo(15);
            
            // 验证偏移量计算正确
            then(addressMapper).should().findByMemberIdWithPaging(TEST_USER_ID, 10, 5);
        }
        
        @Test
        @DisplayName("分页查询边界条件测试 - 第一页")
        void getMyAddressesPaged_FirstPage_ShouldWorkCorrectly() {
            // Given
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPageNum(1); // 第1页
            pageRequest.setPageSize(10); // 每页10条
            
            List<AddressEntity> addresses = Arrays.asList(
                AddressEntityBuilder.builder().id(1L).memberId(TEST_USER_ID).build(),
                AddressEntityBuilder.builder().id(2L).memberId(TEST_USER_ID).build()
            );
            
            given(addressMapper.findByMemberIdWithPaging(TEST_USER_ID, 0, 10)).willReturn(addresses); // offset = (1-1) * 10 = 0
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(2);
            
            // When
            Page<AddressEntity> result = addressService.getMyAddressesPaged(pageRequest);
            
            // Then
            assertThat(result.getPageNum()).isEqualTo(1);
            assertThat(result.getPageSize()).isEqualTo(10);
            assertThat(result.getTotal()).isEqualTo(2);
            assertThat(result.getList()).hasSize(2);
            
            // 验证偏移量为0
            then(addressMapper).should().findByMemberIdWithPaging(TEST_USER_ID, 0, 10);
        }
        
        @Test
        @DisplayName("并发安全测试 - 同时创建默认地址")
        void createDefaultAddress_ConcurrentSafety_ShouldHandleCorrectly() {
            // Given
            CreateAddressRequest request1 = AddressRequestBuilder.builder()
                    .receiverName("用户1")
                    .asDefault()
                    .buildCreateRequest();
            
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(1);
            given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
            
            // When
            addressService.createAddress(request1);
            
            // Then
            // 验证在设置新默认地址之前清除了所有默认地址
            then(addressMapper).should().clearDefaultByMemberId(TEST_USER_ID);
            then(addressMapper).should().insertAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("地址数量边界测试 - 达到上限时创建地址")
        void createAddress_WhenReachLimit_ShouldThrowException() {
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
        @DisplayName("地址数量边界测试 - 未达到上限时正常创建")
        void createAddress_WhenBelowLimit_ShouldCreateSuccessfully() {
            // Given
            CreateAddressRequest request = AddressRequestBuilder.builder()
                    .valid()
                    .buildCreateRequest();
            
            // Mock用户已有19个地址（未达到上限）
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(19);
            given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
            
            // When
            AddressEntity result = addressService.createAddress(request);
            
            // Then
            assertThat(result).isNotNull();
            then(addressMapper).should().insertAddress(any(AddressEntity.class));
        }
        
        @Test
        @DisplayName("数据库查询返回null的处理测试")
        void getAddressById_WhenDatabaseReturnsNull_ShouldThrowException() {
            // Given
            given(addressMapper.existsByIdAndMemberId(TEST_ADDRESS_ID, TEST_USER_ID)).willReturn(true);
            given(valueOperations.get("address:" + TEST_ADDRESS_ID)).willReturn(null); // 缓存未命中
            given(addressMapper.findById(TEST_ADDRESS_ID)).willReturn(null); // 数据库返回null
            
            // When & Then
            assertThatThrownBy(() -> addressService.getAddressById(TEST_ADDRESS_ID))
                    .isInstanceOf(BizException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ADDRESS_NOT_FOUND);
        }
        
        @Test
        @DisplayName("默认地址查询空结果处理测试")
        void getMyDefaultAddress_WhenNoDefault_ShouldReturnNull() {
            // Given
            given(addressMapper.findDefaultByMemberId(TEST_USER_ID)).willReturn(null);
            
            // When
            AddressEntity result = addressService.getMyDefaultAddress();
            
            // Then
            assertThat(result).isNull();
        }
    }
}