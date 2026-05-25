package com.example.onlinestore.aspect;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参数验证切面
 * 
 * 该切面用于处理带有 @ValidateParams 注解的方法的参数验证。
 * 它会对方法的所有参数进行 JSR-303 验证，如果发现验证错误，
 * 将返回 400 Bad Request 响应，并包含本地化的错误消息。
 * 
 * 使用示例：
 * <pre>
 * {@code
 * @ValidateParams
 * public ResponseEntity<?> someMethod(@Valid SomeRequest request) {
 *     // 方法实现
 * }
 * }
 * </pre>
 */
@Aspect
@Component
public class ValidationAspect {
    private static final Logger logger = LoggerFactory.getLogger(ValidationAspect.class);

    private final Validator validator;

    private final MessageSource messageSource;

    public ValidationAspect(Validator validator, MessageSource messageSource) {
        this.validator = validator;
        this.messageSource = messageSource;
    }

    /**
     * 验证方法参数的切面方法
     * 
     * @param joinPoint 切点
     * @return 如果验证通过，返回原方法的执行结果；如果验证失败，返回错误响应
     * @throws Throwable 如果原方法执行时抛出异常
     */
    @Around("@annotation(com.example.onlinestore.annotation.ValidateParams)")
    public Object validateParameters(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        
        // 遍历所有参数进行校验
        for (Object arg : args) {
            if (arg != null) {
                Set<ConstraintViolation<Object>> violations = validator.validate(arg);
                if (!violations.isEmpty()) {
                    String errorMessages = violations.stream()
                        .map(violation -> messageSource.getMessage(
                            violation.getMessage(), 
                            null, 
                            LocaleContextHolder.getLocale()))
                        .collect(Collectors.joining(", "));
                    
                    logger.warn("参数验证失败: {}", errorMessages);
                    return ResponseEntity.badRequest().body(errorMessages);
                }
            }
        }
        
        // 校验通过，继续执行原方法
        return joinPoint.proceed();
    }
} 