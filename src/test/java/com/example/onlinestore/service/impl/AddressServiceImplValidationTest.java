package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.dto.CreateAddressRequest;
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

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

/**
 * AddressServiceImpl验证和输入测试类
 * 专门测试输入验证、参数校验和边界条件
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AddressServiceImpl验证和输入测试")
class AddressServiceImplValidationTest {
    
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
    
    @Mock
    private Validator validator;
    
    @InjectMocks
    private AddressServiceImpl addressService;
    
    private Member testMember;
    private static final Long TEST_USER_ID = 1L;
    
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
    
    @Test
    @DisplayName("输入验证测试 - 有效的创建地址请求")
    void validateCreateAddressRequest_ValidInput_ShouldPass() {
        // Given
        CreateAddressRequest request = AddressRequestBuilder.builder()
                .valid()
                .buildCreateRequest();
        
        given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(0);
        given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
        
        // When
        AddressEntity result = addressService.createAddress(request);
        
        // Then
        assertThat(result).isNotNull();
        
        ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
        then(addressMapper).should().insertAddress(entityCaptor.capture());
        
        AddressEntity capturedEntity = entityCaptor.getValue();
        assertThat(capturedEntity.getReceiverName()).isEqualTo(request.getReceiverName());
        assertThat(capturedEntity.getPhone()).isEqualTo(request.getPhone());
        assertThat(capturedEntity.getProvince()).isEqualTo(request.getProvince());
        assertThat(capturedEntity.getCity()).isEqualTo(request.getCity());
        assertThat(capturedEntity.getDistrict()).isEqualTo(request.getDistrict());
        assertThat(capturedEntity.getDetailAddress()).isEqualTo(request.getDetailAddress());
        assertThat(capturedEntity.getPostCode()).isEqualTo(request.getPostCode());
    }
    
    @Test
    @DisplayName("边界值测试 - 收货人姓名长度边界")
    void createAddress_ReceiverNameBoundary_ShouldHandleCorrectly() {
        // Given - 测试1个字符的姓名
        CreateAddressRequest shortNameRequest = AddressRequestBuilder.builder()
                .receiverName("李")
                .valid()
                .buildCreateRequest();
        
        given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(0);
        given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
        
        // When
        AddressEntity result = addressService.createAddress(shortNameRequest);
        
        // Then
        assertThat(result).isNotNull();
        
        ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
        then(addressMapper).should().insertAddress(entityCaptor.capture());
        assertThat(entityCaptor.getValue().getReceiverName()).isEqualTo("李");
    }
    
    @Test
    @DisplayName("边界值测试 - 详细地址长度边界")
    void createAddress_DetailAddressBoundary_ShouldHandleCorrectly() {
        // Given - 测试接近最大长度的详细地址
        StringBuilder longAddress = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            longAddress.append("测试地址");
        }
        
        CreateAddressRequest longAddressRequest = AddressRequestBuilder.builder()
                .detailAddress(longAddress.toString())
                .valid()
                .buildCreateRequest();
        
        given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(0);
        given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
        
        // When
        AddressEntity result = addressService.createAddress(longAddressRequest);
        
        // Then
        assertThat(result).isNotNull();
        
        ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
        then(addressMapper).should().insertAddress(entityCaptor.capture());
        assertThat(entityCaptor.getValue().getDetailAddress()).isEqualTo(longAddress.toString());
    }
    
    @Test
    @DisplayName("电话号码格式测试 - 有效的手机号")
    void createAddress_ValidPhoneNumbers_ShouldPass() {
        // Given - 测试不同的有效手机号格式
        String[] validPhones = {
            "13800138000",
            "15900159000", 
            "18600186000",
            "17700177000",
            "19900199000"
        };
        
        for (String phone : validPhones) {
            // Reset mocks for each iteration
            reset(addressMapper);
            
            CreateAddressRequest request = AddressRequestBuilder.builder()
                    .phone(phone)
                    .valid()
                    .buildCreateRequest();
            
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(0);
            given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
            
            // When & Then
            AddressEntity result = addressService.createAddress(request);
            assertThat(result).isNotNull();
            
            ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
            then(addressMapper).should().insertAddress(entityCaptor.capture());
            assertThat(entityCaptor.getValue().getPhone()).isEqualTo(phone);
        }
    }
    
    @Test
    @DisplayName("邮政编码格式测试 - 有效的邮编")
    void createAddress_ValidPostCodes_ShouldPass() {
        // Given - 测试不同的有效邮政编码
        String[] validPostCodes = {
            "100000", // 北京
            "200000", // 上海  
            "510000", // 广州
            "310000", // 杭州
            "400000"  // 重庆
        };
        
        for (String postCode : validPostCodes) {
            // Reset mocks for each iteration
            reset(addressMapper);
            
            CreateAddressRequest request = AddressRequestBuilder.builder()
                    .postCode(postCode)
                    .valid()
                    .buildCreateRequest();
            
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(0);
            given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
            
            // When & Then
            AddressEntity result = addressService.createAddress(request);
            assertThat(result).isNotNull();
            
            ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
            then(addressMapper).should().insertAddress(entityCaptor.capture());
            assertThat(entityCaptor.getValue().getPostCode()).isEqualTo(postCode);
        }
    }
    
    @Test
    @DisplayName("空值处理测试 - null参数应该抛出异常")
    void createAddress_NullRequest_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> addressService.createAddress(null))
                .isInstanceOf(NullPointerException.class);
    }
    
    @Test
    @DisplayName("默认地址逻辑测试 - 布尔值处理")
    void createAddress_BooleanHandling_ShouldWorkCorrectly() {
        // Given - 测试不同的布尔值组合
        Boolean[] defaultValues = {true, false, null};
        
        for (Boolean isDefault : defaultValues) {
            // Reset mocks for each iteration
            reset(addressMapper);
            
            CreateAddressRequest request = AddressRequestBuilder.builder()
                    .isDefault(isDefault)
                    .valid()
                    .buildCreateRequest();
            
            given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(1); // 非首个地址
            given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
            
            if (Boolean.TRUE.equals(isDefault)) {
                // 如果明确设置为默认地址，应该清除其他默认地址
                given(addressMapper.clearDefaultByMemberId(TEST_USER_ID)).willReturn(1);
            }
            
            // When
            AddressEntity result = addressService.createAddress(request);
            
            // Then
            assertThat(result).isNotNull();
            
            ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
            then(addressMapper).should().insertAddress(entityCaptor.capture());
            
            AddressEntity capturedEntity = entityCaptor.getValue();
            if (Boolean.TRUE.equals(isDefault)) {
                assertThat(capturedEntity.getIsDefault()).isTrue();
                then(addressMapper).should().clearDefaultByMemberId(TEST_USER_ID);
            } else {
                assertThat(capturedEntity.getIsDefault()).isFalse();
                then(addressMapper).should(never()).clearDefaultByMemberId(TEST_USER_ID);
            }
        }
    }
    
    @Test
    @DisplayName("中文字符处理测试 - 支持中文地址")
    void createAddress_ChineseCharacters_ShouldHandleCorrectly() {
        // Given - 包含各种中文字符的地址
        CreateAddressRequest request = AddressRequestBuilder.builder()
                .receiverName("王小明")
                .province("广东省")
                .city("深圳市")
                .district("南山区")
                .detailAddress("深南大道10000号腾讯大厦50层")
                .buildCreateRequest();
        
        given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(0);
        given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
        
        // When
        AddressEntity result = addressService.createAddress(request);
        
        // Then
        assertThat(result).isNotNull();
        
        ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
        then(addressMapper).should().insertAddress(entityCaptor.capture());
        
        AddressEntity capturedEntity = entityCaptor.getValue();
        assertThat(capturedEntity.getReceiverName()).isEqualTo("王小明");
        assertThat(capturedEntity.getProvince()).isEqualTo("广东省");
        assertThat(capturedEntity.getCity()).isEqualTo("深圳市");
        assertThat(capturedEntity.getDistrict()).isEqualTo("南山区");
        assertThat(capturedEntity.getDetailAddress()).isEqualTo("深南大道10000号腾讯大厦50层");
    }
    
    @Test
    @DisplayName("特殊字符处理测试 - 地址包含特殊字符")
    void createAddress_SpecialCharacters_ShouldHandleCorrectly() {
        // Given - 包含特殊字符的地址
        CreateAddressRequest request = AddressRequestBuilder.builder()
                .receiverName("张三-先生")
                .detailAddress("某某路123号A座2-3-4室（靠近地铁站）")
                .valid()
                .buildCreateRequest();
        
        given(addressMapper.countByMemberId(TEST_USER_ID)).willReturn(0);
        given(addressMapper.insertAddress(any(AddressEntity.class))).willReturn(1);
        
        // When
        AddressEntity result = addressService.createAddress(request);
        
        // Then
        assertThat(result).isNotNull();
        
        ArgumentCaptor<AddressEntity> entityCaptor = ArgumentCaptor.forClass(AddressEntity.class);
        then(addressMapper).should().insertAddress(entityCaptor.capture());
        
        AddressEntity capturedEntity = entityCaptor.getValue();
        assertThat(capturedEntity.getReceiverName()).isEqualTo("张三-先生");
        assertThat(capturedEntity.getDetailAddress()).isEqualTo("某某路123号A座2-3-4室（靠近地铁站）");
    }
}