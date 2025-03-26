package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.User;
import com.example.onlinestore.dto.LoginRequest;
import com.example.onlinestore.dto.LoginResponse;
import com.example.onlinestore.dto.PageResponse;
import com.example.onlinestore.dto.UserPageRequest;
import com.example.onlinestore.dto.UserVO;
import com.example.onlinestore.entity.UserEntity;
import com.example.onlinestore.mapper.UserMapper;
import com.example.onlinestore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final ObjectMapper objectMapper;

    public UserServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Value("${admin.auth.username}")
    private String adminUsername;

    @Value("${admin.auth.password}")
    private String adminPassword;

    @Value("${service.user.base-url}")
    private String userServiceBaseUrl;

    private static final String AUTH_PATH = "/auth";
    private static final String TOKEN_PREFIX = "token:";
    private static final long TOKEN_EXPIRE_DAYS = 1;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        // 先检查是否是管理员用户
        if (adminUsername.equals(request.getUsername())) {
            // 如果是管理员，验证密码
            if (adminPassword.equals(request.getPassword())) {
                logger.info("管理员快速登录");
                return createLoginResponse(request.getUsername());
            } else {
                logger.warn("管理员密码错误");
                throw new IllegalArgumentException(messageSource.getMessage(
                    "error.invalid.credentials", null, LocaleContextHolder.getLocale()));
            }
        }

        // 非管理员用户，调用user-service进行认证
        String authUrl = UriComponentsBuilder.fromHttpUrl(userServiceBaseUrl)
            .path(AUTH_PATH)
            .toUriString();
        Boolean isAuthenticated = restTemplate.postForObject(authUrl, request, Boolean.class);
        
        if (isAuthenticated == null || !isAuthenticated) {
            throw new IllegalArgumentException(messageSource.getMessage(
                "error.invalid.credentials", null, LocaleContextHolder.getLocale()));
        }

        return createLoginResponse(request.getUsername());
    }

    private LoginResponse createLoginResponse(String username) {
        // 生成token
        String token = UUID.randomUUID().toString();
        LocalDateTime expireTime = LocalDateTime.now().plusDays(TOKEN_EXPIRE_DAYS);

        // 查找或创建用户
        UserEntity user = userMapper.findByUsername(username);
        if (user == null) {
            // 用户不存在，创建新用户
            user = new UserEntity();
            user.setUsername(username);
            user.setToken(token);
            user.setTokenExpireTime(expireTime);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.insertUser(user);
            logger.info("创建新用户: {}", username);
        } else {
            // 更新现有用户的token
            user.setToken(token);
            user.setTokenExpireTime(expireTime);
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.updateUserToken(user);
            logger.info("更新用户token: {}", username);
        }

        try {
            // 将用户信息转换为JSON并保存到Redis
            String redisKey = TOKEN_PREFIX + token;
            String userJson = objectMapper.writeValueAsString(user);
            redisTemplate.opsForValue().set(redisKey, userJson, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
            logger.info("用户信息已缓存到Redis: {}", username);
        } catch (Exception e) {
            logger.error("缓存用户信息失败", e);
            // 继续处理，因为这不是致命错误
        }

        // 返回响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpireTime(expireTime);
        return response;
    }

    private UserVO convertToVO(UserEntity user) {
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setCreatedAt(user.getCreatedAt());
        vo.setUpdatedAt(user.getUpdatedAt());
        return vo;
    }

    @Override
    public PageResponse<UserVO> listUsers(UserPageRequest request) {
        // 计算分页参数
        int offset = (request.getPageNum() - 1) * request.getPageSize();
        int limit = request.getPageSize();

        // 查询数据
        List<UserEntity> users = userMapper.findAllWithPagination(offset, limit);
        long total = userMapper.countTotal();

        // 转换为VO
        List<UserVO> userVOs = users.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        // 构建响应
        PageResponse<UserVO> response = new PageResponse<>();
        response.setItems(userVOs);
        response.setTotal(total);
        response.setPageNum(request.getPageNum());
        response.setPageSize(request.getPageSize());

        return response;
    }

    @Override
    public User getUserByToken(String token) {
        try {
            String redisKey = TOKEN_PREFIX + token;
            String userJson = redisTemplate.opsForValue().get(redisKey);
            if (userJson == null) {
                logger.warn("无效的token: {}", token);
                return null;
            }
            UserEntity userEntity =  objectMapper.readValue(userJson, UserEntity.class);
            return convert_user(userEntity);
        } catch (Exception e) {
            logger.error("从Redis获取用户信息失败", e);
            return null;
        }
    }

    private User convert_user(UserEntity userEntity) {
        BeanCopier copier = BeanCopier.create(UserEntity.class, User.class, false);
        User user = new User();
        copier.copy(userEntity, user, null);
        return user;
    }
} 