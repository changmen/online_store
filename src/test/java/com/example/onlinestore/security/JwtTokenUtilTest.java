package com.example.onlinestore.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtTokenUtil 单元测试类
 * 
 * 测试JWT工具类的各种功能，包括：
 * - Token生成
 * - Token解析
 * - Token验证
 * - 异常处理
 */
@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=mySecretKeyForJWTTokenThatIsAtLeast256BitsLongForTestingPurpose123456789",
    "jwt.expiration=86400"
})
class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;
    private UserDetails testUser;
    private final String testUsername = "testuser";
    private final String testPassword = "testpassword";

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        
        // 设置测试用的密钥和过期时间
        String testSecret = "mySecretKeyForJWTTokenThatIsAtLeast256BitsLongForTestingPurpose123456789";
        Long testExpiration = 86400L; // 24小时
        
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", testExpiration);
        
        // 创建测试用户
        testUser = new User(testUsername, testPassword, new ArrayList<>());
    }

    @Test
    @DisplayName("生成Token - 应该返回有效的JWT Token")
    void testGenerateToken_ShouldReturnValidToken() {
        // When
        String token = jwtTokenUtil.generateToken(testUser);
        
        // Then
        assertNotNull(token, "生成的token不应为null");
        assertFalse(token.isEmpty(), "生成的token不应为空字符串");
        assertTrue(token.contains("."), "JWT token应该包含点分隔符");
        
        // 验证token格式（JWT应该有3个部分，用.分隔）
        String[] tokenParts = token.split("\\.");
        assertEquals(3, tokenParts.length, "JWT token应该包含3个部分");
        
        // 验证能从生成的token中提取用户名
        String extractedUsername = jwtTokenUtil.extractUsername(token);
        assertEquals(testUsername, extractedUsername, "从token中提取的用户名应该正确");
    }

    @Test
    @DisplayName("提取用户名 - 应该返回正确的用户名")
    void testExtractUsername_ShouldReturnCorrectUsername() {
        // Given
        String token = jwtTokenUtil.generateToken(testUser);
        
        // When
        String username = jwtTokenUtil.extractUsername(token);
        
        // Then
        assertEquals(testUsername, username, "提取的用户名应该与原始用户名一致");
    }

    @Test
    @DisplayName("提取过期时间 - 应该返回未来的时间")
    void testExtractExpiration_ShouldReturnFutureDate() {
        // Given
        String token = jwtTokenUtil.generateToken(testUser);
        Date beforeGeneration = new Date(System.currentTimeMillis() - 1000); // 1秒前
        Date afterGeneration = new Date(System.currentTimeMillis() + 1000); // 1秒后
        
        // When
        Date expirationDate = jwtTokenUtil.extractExpiration(token);
        
        // Then
        assertNotNull(expirationDate, "过期时间不应为null");
        assertTrue(expirationDate.after(afterGeneration), "过期时间应该在未来");
        
        // 验证过期时间大约是24小时后（允许5分钟的误差）
        long timeDifference = expirationDate.getTime() - System.currentTimeMillis();
        long expectedDifference = 86400L * 1000; // 24小时转换为毫秒
        long tolerance = 5 * 60 * 1000; // 5分钟容差
        
        assertTrue(Math.abs(timeDifference - expectedDifference) < tolerance, 
                   "过期时间应该约为24小时后");
    }

    @Test
    @DisplayName("验证Token - 有效token应该返回true")
    void testValidateToken_WithValidToken_ShouldReturnTrue() {
        // Given
        String token = jwtTokenUtil.generateToken(testUser);
        
        // When
        Boolean isValid = jwtTokenUtil.validateToken(token, testUser);
        
        // Then
        assertTrue(isValid, "有效的token应该通过验证");
    }

    @Test
    @DisplayName("验证Token - 错误用户应该返回false")
    void testValidateToken_WithWrongUser_ShouldReturnFalse() {
        // Given
        String token = jwtTokenUtil.generateToken(testUser);
        UserDetails wrongUser = new User("wronguser", "password", new ArrayList<>());
        
        // When
        Boolean isValid = jwtTokenUtil.validateToken(token, wrongUser);
        
        // Then
        assertFalse(isValid, "使用错误用户验证token应该返回false");
    }

    @Test
    @DisplayName("验证Token - 过期token应该返回false")
    void testValidateToken_WithExpiredToken_ShouldReturnFalse() {
        // Given - 创建一个过期的token
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", -1L); // 设置为已过期
        String expiredToken = jwtTokenUtil.generateToken(testUser);
        
        // 恢复正常的过期时间
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", 86400L);
        
        // When
        Boolean isValid = jwtTokenUtil.validateToken(expiredToken, testUser);
        
        // Then
        assertFalse(isValid, "过期的token应该验证失败");
    }

    @Test
    @DisplayName("提取声明 - 应该返回正确的声明信息")
    void testExtractClaim_ShouldReturnCorrectClaim() {
        // Given
        String token = jwtTokenUtil.generateToken(testUser);
        Date beforeGeneration = new Date(System.currentTimeMillis() - 1000);
        Date afterGeneration = new Date(System.currentTimeMillis() + 1000);
        
        // When
        String subject = jwtTokenUtil.extractClaim(token, Claims::getSubject);
        Date issuedAt = jwtTokenUtil.extractClaim(token, Claims::getIssuedAt);
        
        // Then
        assertEquals(testUsername, subject, "subject声明应该是用户名");
        assertNotNull(issuedAt, "issuedAt声明不应为null");
        assertTrue(issuedAt.after(beforeGeneration) && issuedAt.before(afterGeneration),
                   "issuedAt应该在token生成的时间范围内");
    }

    @Test
    @DisplayName("无效Token - 应该抛出MalformedJwtException")
    void testExtractUsername_WithInvalidToken_ShouldThrowException() {
        // Given
        String invalidToken = "invalid.token.here";
        
        // When & Then
        assertThrows(MalformedJwtException.class, () -> {
            jwtTokenUtil.extractUsername(invalidToken);
        }, "解析无效token应该抛出MalformedJwtException");
    }

    @Test
    @DisplayName("畸形Token - 应该抛出异常")
    void testExtractUsername_WithMalformedToken_ShouldThrowException() {
        // Given
        String malformedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.malformed.signature";
        
        // When & Then
        assertThrows(Exception.class, () -> {
            jwtTokenUtil.extractUsername(malformedToken);
        }, "解析畸形token应该抛出异常");
    }

    @Test
    @DisplayName("篡改Token - 应该抛出SignatureException")
    void testValidateToken_WithTamperedToken_ShouldThrowException() {
        // Given
        String validToken = jwtTokenUtil.generateToken(testUser);
        // 篡改token的签名部分
        String[] parts = validToken.split("\\.");
        String tamperedToken = parts[0] + "." + parts[1] + "." + "tamperedSignature";
        
        // When & Then
        assertThrows(SignatureException.class, () -> {
            jwtTokenUtil.validateToken(tamperedToken, testUser);
        }, "验证被篡改的token应该抛出SignatureException");
    }

    @Test
    @DisplayName("不同用户 - 应该生成不同的Token")
    void testGenerateToken_WithDifferentUsers_ShouldGenerateDifferentTokens() {
        // Given
        UserDetails user1 = new User("user1", "password1", new ArrayList<>());
        UserDetails user2 = new User("user2", "password2", new ArrayList<>());
        
        // When
        String token1 = jwtTokenUtil.generateToken(user1);
        String token2 = jwtTokenUtil.generateToken(user2);
        
        // Then
        assertNotEquals(token1, token2, "不同用户应该生成不同的token");
        assertEquals("user1", jwtTokenUtil.extractUsername(token1), "token1应该包含user1的用户名");
        assertEquals("user2", jwtTokenUtil.extractUsername(token2), "token2应该包含user2的用户名");
    }

    @Test
    @DisplayName("多次生成 - 相同用户应该生成不同的Token")
    void testGenerateToken_MultipleCallsForSameUser_ShouldGenerateDifferentTokens() {
        // Given & When
        String token1 = jwtTokenUtil.generateToken(testUser);
        
        // 等待至少1毫秒确保时间戳不同
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("测试被中断");
        }
        
        String token2 = jwtTokenUtil.generateToken(testUser);
        
        // Then
        assertNotEquals(token1, token2, "相同用户的多次调用应该生成不同的token");
        
        // 但都应该包含相同的用户名
        String username1 = jwtTokenUtil.extractUsername(token1);
        String username2 = jwtTokenUtil.extractUsername(token2);
        assertEquals(username1, username2, "两个token应该包含相同的用户名");
        assertEquals(testUsername, username1, "用户名应该正确");
    }

    @Test
    @DisplayName("空用户名 - 应该正确处理边界情况")
    void testGenerateToken_WithEmptyUsername_ShouldHandleGracefully() {
        // Given
        UserDetails emptyUser = new User("", "password", new ArrayList<>());
        
        // When
        String token = jwtTokenUtil.generateToken(emptyUser);
        String extractedUsername = jwtTokenUtil.extractUsername(token);
        
        // Then
        assertNotNull(token, "即使用户名为空，也应该能生成token");
        assertEquals("", extractedUsername, "应该能正确提取空用户名");
    }
}