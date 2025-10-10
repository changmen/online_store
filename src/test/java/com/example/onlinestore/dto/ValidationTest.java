package com.example.onlinestore.dto;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.enums.GenderType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DTO验证测试
 */
class ValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testLoginRequestValidation() {
        // 测试有效的登录请求
        LoginRequest validRequest = new LoginRequest();
        validRequest.setUsername("testuser123");
        validRequest.setPassword("Password123!");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(validRequest);
        assertTrue(violations.isEmpty(), "Valid LoginRequest should have no violations");

        // 测试无效的用户名 - 太短
        LoginRequest invalidUsernameRequest = new LoginRequest();
        invalidUsernameRequest.setUsername("a");
        invalidUsernameRequest.setPassword("Password123!");
        
        violations = validator.validate(invalidUsernameRequest);
        assertFalse(violations.isEmpty(), "Invalid username should cause violations");

        // 测试无效的密码 - 不符合复杂度要求
        LoginRequest invalidPasswordRequest = new LoginRequest();
        invalidPasswordRequest.setUsername("testuser123");
        invalidPasswordRequest.setPassword("123");
        
        violations = validator.validate(invalidPasswordRequest);
        assertFalse(violations.isEmpty(), "Invalid password should cause violations");

        // 测试空用户名
        LoginRequest nullUsernameRequest = new LoginRequest();
        nullUsernameRequest.setUsername(null);
        nullUsernameRequest.setPassword("Password123!");
        
        violations = validator.validate(nullUsernameRequest);
        assertFalse(violations.isEmpty(), "Null username should cause violations");

        // 测试空密码
        LoginRequest nullPasswordRequest = new LoginRequest();
        nullPasswordRequest.setUsername("testuser123");
        nullPasswordRequest.setPassword(null);
        
        violations = validator.validate(nullPasswordRequest);
        assertFalse(violations.isEmpty(), "Null password should cause violations");
    }

    @Test
    void testMemberRegistryRequestValidation() {
        // 测试有效的注册请求
        MemberRegistryRequest validRequest = new MemberRegistryRequest();
        validRequest.setName("testuser123");
        validRequest.setPassword("Password123!");
        validRequest.setNickName("Test User");
        validRequest.setGender(GenderType.MALE);
        validRequest.setAge(25);
        validRequest.setPhone("13800138000");
        
        Set<ConstraintViolation<MemberRegistryRequest>> violations = validator.validate(validRequest);
        assertTrue(violations.isEmpty(), "Valid MemberRegistryRequest should have no violations");

        // 测试无效的名称 - 太短
        MemberRegistryRequest invalidNameRequest = new MemberRegistryRequest();
        invalidNameRequest.setName("a");
        invalidNameRequest.setPassword("Password123!");
        invalidNameRequest.setNickName("Test User");
        invalidNameRequest.setGender(GenderType.MALE);
        invalidNameRequest.setAge(25);
        invalidNameRequest.setPhone("13800138000");
        
        violations = validator.validate(invalidNameRequest);
        assertFalse(violations.isEmpty(), "Invalid name should cause violations");

        // 测试无效的密码
        MemberRegistryRequest invalidPasswordRequest = new MemberRegistryRequest();
        invalidPasswordRequest.setName("testuser123");
        invalidPasswordRequest.setPassword("123");
        invalidPasswordRequest.setNickName("Test User");
        invalidPasswordRequest.setGender(GenderType.MALE);
        invalidPasswordRequest.setAge(25);
        invalidPasswordRequest.setPhone("13800138000");
        
        violations = validator.validate(invalidPasswordRequest);
        assertFalse(violations.isEmpty(), "Invalid password should cause violations");

        // 测试无效的年龄 - 小于18
        MemberRegistryRequest invalidAgeRequest = new MemberRegistryRequest();
        invalidAgeRequest.setName("testuser123");
        invalidAgeRequest.setPassword("Password123!");
        invalidAgeRequest.setNickName("Test User");
        invalidAgeRequest.setGender(GenderType.MALE);
        invalidAgeRequest.setAge(16);
        invalidAgeRequest.setPhone("13800138000");
        
        violations = validator.validate(invalidAgeRequest);
        assertFalse(violations.isEmpty(), "Invalid age should cause violations");

        // 测试无效的手机号
        MemberRegistryRequest invalidPhoneRequest = new MemberRegistryRequest();
        invalidPhoneRequest.setName("testuser123");
        invalidPhoneRequest.setPassword("Password123!");
        invalidPhoneRequest.setNickName("Test User");
        invalidPhoneRequest.setGender(GenderType.MALE);
        invalidPhoneRequest.setAge(25);
        invalidPhoneRequest.setPhone("1234567890");
        
        violations = validator.validate(invalidPhoneRequest);
        assertFalse(violations.isEmpty(), "Invalid phone should cause violations");

        // 测试昵称过长
        MemberRegistryRequest longNickNameRequest = new MemberRegistryRequest();
        longNickNameRequest.setName("testuser123");
        longNickNameRequest.setPassword("Password123!");
        longNickNameRequest.setNickName("This is a very long nickname that exceeds the maximum length");
        longNickNameRequest.setGender(GenderType.MALE);
        longNickNameRequest.setAge(25);
        longNickNameRequest.setPhone("13800138000");
        
        violations = validator.validate(longNickNameRequest);
        assertFalse(violations.isEmpty(), "Long nickname should cause violations");

        // 测试必填字段为null
        MemberRegistryRequest nullFieldsRequest = new MemberRegistryRequest();
        nullFieldsRequest.setName(null);
        nullFieldsRequest.setPassword(null);
        nullFieldsRequest.setGender(null);
        nullFieldsRequest.setPhone(null);
        
        violations = validator.validate(nullFieldsRequest);
        assertFalse(violations.isEmpty(), "Null required fields should cause violations");
        assertTrue(violations.size() >= 4, "Should have multiple violations for null fields");
    }

    @Test
    void testBrandValidation() {
        // 测试有效的品牌
        Brand validBrand = new Brand();
        validBrand.setName("NIKE");
        validBrand.setDescription("Just Do It - Global Athletic Brand");
        validBrand.setLogo("https://example.com/nike-logo.png");
        validBrand.setStory("Nike is a multinational corporation that designs and develops athletic footwear");
        validBrand.setSortScore(100);
        validBrand.setVisible(1);
        
        Set<ConstraintViolation<Brand>> violations = validator.validate(validBrand);
        assertTrue(violations.isEmpty(), "Valid Brand should have no violations");

        // 测试名称为空
        Brand emptyNameBrand = new Brand();
        emptyNameBrand.setName("");
        emptyNameBrand.setDescription("Just Do It - Global Athletic Brand");
        emptyNameBrand.setLogo("https://example.com/nike-logo.png");
        emptyNameBrand.setStory("Nike is a multinational corporation that designs and develops athletic footwear");
        emptyNameBrand.setSortScore(100);
        emptyNameBrand.setVisible(1);
        
        violations = validator.validate(emptyNameBrand);
        assertFalse(violations.isEmpty(), "Empty name should cause violations");

        // 测试名称过长
        Brand longNameBrand = new Brand();
        longNameBrand.setName("A".repeat(65)); // 超过64字符限制
        longNameBrand.setDescription("Just Do It - Global Athletic Brand");
        longNameBrand.setLogo("https://example.com/nike-logo.png");
        longNameBrand.setStory("Nike is a multinational corporation that designs and develops athletic footwear");
        longNameBrand.setSortScore(100);
        longNameBrand.setVisible(1);
        
        violations = validator.validate(longNameBrand);
        assertFalse(violations.isEmpty(), "Long name should cause violations");

        // 测试描述过长
        Brand longDescriptionBrand = new Brand();
        longDescriptionBrand.setName("NIKE");
        longDescriptionBrand.setDescription("A".repeat(513)); // 超过512字符限制
        longDescriptionBrand.setLogo("https://example.com/nike-logo.png");
        longDescriptionBrand.setStory("Nike is a multinational corporation that designs and develops athletic footwear");
        longDescriptionBrand.setSortScore(100);
        longDescriptionBrand.setVisible(1);
        
        violations = validator.validate(longDescriptionBrand);
        assertFalse(violations.isEmpty(), "Long description should cause violations");

        // 测试无效的Logo URL
        Brand invalidLogoBrand = new Brand();
        invalidLogoBrand.setName("NIKE");
        invalidLogoBrand.setDescription("Just Do It - Global Athletic Brand");
        invalidLogoBrand.setLogo("invalid-url");
        invalidLogoBrand.setStory("Nike is a multinational corporation that designs and develops athletic footwear");
        invalidLogoBrand.setSortScore(100);
        invalidLogoBrand.setVisible(1);
        
        violations = validator.validate(invalidLogoBrand);
        assertFalse(violations.isEmpty(), "Invalid logo URL should cause violations");

        // 测试故事太短
        Brand shortStoryBrand = new Brand();
        shortStoryBrand.setName("NIKE");
        shortStoryBrand.setDescription("Just Do It - Global Athletic Brand");
        shortStoryBrand.setLogo("https://example.com/nike-logo.png");
        shortStoryBrand.setStory("Short"); // 少于16字符
        shortStoryBrand.setSortScore(100);
        shortStoryBrand.setVisible(1);
        
        violations = validator.validate(shortStoryBrand);
        assertFalse(violations.isEmpty(), "Short story should cause violations");

        // 测试故事过长
        Brand longStoryBrand = new Brand();
        longStoryBrand.setName("NIKE");
        longStoryBrand.setDescription("Just Do It - Global Athletic Brand");
        longStoryBrand.setLogo("https://example.com/nike-logo.png");
        longStoryBrand.setStory("A".repeat(1025)); // 超过1024字符限制
        longStoryBrand.setSortScore(100);
        longStoryBrand.setVisible(1);
        
        violations = validator.validate(longStoryBrand);
        assertFalse(violations.isEmpty(), "Long story should cause violations");

        // 测试无效的可见性值
        Brand invalidVisibleBrand = new Brand();
        invalidVisibleBrand.setName("NIKE");
        invalidVisibleBrand.setDescription("Just Do It - Global Athletic Brand");
        invalidVisibleBrand.setLogo("https://example.com/nike-logo.png");
        invalidVisibleBrand.setStory("Nike is a multinational corporation that designs and develops athletic footwear");
        invalidVisibleBrand.setSortScore(100);
        invalidVisibleBrand.setVisible(2); // 超出0-1范围
        
        violations = validator.validate(invalidVisibleBrand);
        assertFalse(violations.isEmpty(), "Invalid visible value should cause violations");

        // 测试必填字段为null
        Brand nullFieldsBrand = new Brand();
        nullFieldsBrand.setName(null);
        nullFieldsBrand.setDescription(null);
        nullFieldsBrand.setLogo(null);
        nullFieldsBrand.setStory(null);
        nullFieldsBrand.setSortScore(null);
        nullFieldsBrand.setVisible(null);
        
        violations = validator.validate(nullFieldsBrand);
        assertFalse(violations.isEmpty(), "Null required fields should cause violations");
        assertTrue(violations.size() >= 6, "Should have multiple violations for null fields");
    }

    @Test
    void testBrandListQueryOptionsValidation() {
        // 测试有效的查询选项
        BrandListQueryOptions validOptions = new BrandListQueryOptions();
        validOptions.setPageNum(1);
        validOptions.setPageSize(10);
        validOptions.setVisible(1);
        validOptions.setOrderBy("name_asc");
        
        Set<ConstraintViolation<BrandListQueryOptions>> violations = validator.validate(validOptions);
        assertTrue(violations.isEmpty(), "Valid BrandListQueryOptions should have no violations");

        // 测试无效的可见性值
        BrandListQueryOptions invalidVisibleOptions = new BrandListQueryOptions();
        invalidVisibleOptions.setPageNum(1);
        invalidVisibleOptions.setPageSize(10);
        invalidVisibleOptions.setVisible(2); // 超出0-1范围
        
        violations = validator.validate(invalidVisibleOptions);
        assertFalse(violations.isEmpty(), "Invalid visible value should cause violations");

        // 测试无效的排序字段
        BrandListQueryOptions invalidOrderByOptions = new BrandListQueryOptions();
        invalidOrderByOptions.setPageNum(1);
        invalidOrderByOptions.setPageSize(10);
        invalidOrderByOptions.setVisible(1);
        invalidOrderByOptions.setOrderBy("invalid-field!@#");
        
        violations = validator.validate(invalidOrderByOptions);
        assertFalse(violations.isEmpty(), "Invalid orderBy field should cause violations");
    }
}