# GlobalExceptionHandler 全局异常处理器

## 概述

`GlobalExceptionHandler` 是在线商城系统的全局异常处理器，基于 Spring Boot 的 `@RestControllerAdvice` 注解实现。它负责捕获和处理应用程序中的各种异常，提供统一的错误响应格式，增强系统的健壮性和用户体验。

## 文件位置

```
src/main/java/com/example/onlinestore/handler/GlobalExceptionHandler.java
```

## 主要功能

### 1. 统一异常处理
- 捕获应用程序中的各种异常
- 提供统一的错误响应格式
- 记录详细的错误日志
- 支持国际化错误消息

### 2. 异常类型覆盖
处理以下类型的异常：
- **系统异常**: `Exception`, `RuntimeException`
- **参数验证异常**: `MethodArgumentNotValidException`
- **业务异常**: `BizException`
- **约束验证异常**: `ConstraintViolationException`

## 异常处理详情

### 1. 系统异常处理

```java
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@ExceptionHandler({Exception.class, RuntimeException.class})
public Response<String> handleException(Exception e)
```

**处理范围**: 所有未被其他处理器捕获的异常
**HTTP状态码**: 500 (Internal Server Error)
**响应格式**: 
```json
{
  "success": false,
  "message": "INTERNAL_SERVER_ERROR",
  "data": null
}
```

### 2. 参数验证异常处理

```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(MethodArgumentNotValidException.class)
public Response<String> handleException(MethodArgumentNotValidException e)
```

**处理范围**: Spring MVC 参数验证失败异常
**HTTP状态码**: 400 (Bad Request)
**功能特点**:
- 提取第一个验证失败的字段信息
- 返回具体的字段名和错误消息
- 格式化错误信息：`Parameter:{field}, error:{message}`

**响应示例**:
```json
{
  "success": false,
  "message": "Parameter:username, error:用户名不能为空",
  "data": null
}
```

### 3. 业务异常处理

```java
@ResponseStatus(HttpStatus.CONFLICT)
@ExceptionHandler(BizException.class)
public Response<String> handleException(BizException e)
```

**处理范围**: 自定义业务异常
**HTTP状态码**: 409 (Conflict)
**功能特点**:
- 支持错误码和默认消息
- 支持国际化消息
- 支持参数化消息格式
- 完整的错误日志记录

**处理流程**:
1. 检查错误码是否为空
2. 尝试从 MessageSource 获取国际化消息
3. 如果国际化消息不存在，使用默认消息
4. 支持 MessageFormat 参数替换
5. 记录详细的错误日志

### 4. 约束验证异常处理

```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(ConstraintViolationException.class)
public Response<String> handleException(ConstraintViolationException e)
```

**处理范围**: Bean Validation 约束验证异常
**HTTP状态码**: 400 (Bad Request)
**功能特点**:
- 收集所有验证失败的约束
- 格式化为易读的错误消息
- 包含属性路径和具体错误信息

## 依赖组件

### 1. Response 类
- 统一的响应包装器
- 位置：`com.example.onlinestore.dto.Response`
- 提供成功和失败的响应构造方法

### 2. BizException 类
- 自定义业务异常
- 位置：`com.example.onlinestore.exceptions.BizException`
- 包含错误码和参数信息

### 3. ErrorCode 枚举
- 错误码定义
- 位置：`com.example.onlinestore.errors.ErrorCode`
- 包含错误码和默认消息

### 4. MessageSource
- Spring 国际化消息源
- 支持多语言错误消息
- 使用当前线程的 Locale 信息

## 使用示例

### 1. 抛出业务异常

```java
// 简单业务异常
throw new BizException(ErrorCode.MEMBER_NOT_FOUND);

// 带参数的业务异常
throw new BizException(ErrorCode.MEMBER_NOT_FOUND, "username123");
```

### 2. 参数验证

```java
public class UserDto {
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @Email(message = "邮箱格式不正确")
    private String email;
}
```

### 3. 控制器中的使用

```java
@RestController
public class UserController {
    
    @PostMapping("/users")
    public Response<User> createUser(@Valid @RequestBody UserDto userDto) {
        // 如果 userDto 验证失败，会被 GlobalExceptionHandler 捕获
        // 如果业务逻辑出错，抛出 BizException
        return userService.createUser(userDto);
    }
}
```

## 日志记录

异常处理器会记录不同级别的日志：

- **ERROR 级别**: 记录所有异常的详细信息
- **包含异常堆栈**: 便于问题排查
- **结构化日志**: 包含错误码、参数等关键信息

## 配置要求

### 1. Spring Boot 配置
确保启用了以下功能：
- Spring MVC
- Bean Validation
- MessageSource 配置

### 2. 消息国际化
在 `application.properties` 中配置：
```properties
spring.messages.basename=messages
spring.messages.encoding=UTF-8
```

### 3. 消息文件示例
`messages.properties`:
```properties
ErrorCode.Member.NotFound=会员不存在
ErrorCode.Member.Password.Incorrect=密码不正确
```

## 最佳实践

### 1. 异常抛出
- 使用具体的异常类型而不是通用异常
- 为业务异常提供有意义的错误码
- 在需要时提供参数信息

### 2. 错误消息
- 错误消息应该对用户友好
- 避免暴露敏感的系统信息
- 支持多语言环境

### 3. 日志记录
- 记录足够的上下文信息
- 使用适当的日志级别
- 避免记录敏感信息

## 扩展建议

1. **添加更多异常类型**: 根据业务需要添加特定异常处理器
2. **增强错误响应**: 添加错误码、时间戳等信息
3. **监控集成**: 与监控系统集成，及时发现系统问题
4. **错误统计**: 统计错误频率，优化系统稳定性

## 相关文件

- [`Response.java`](../dto/Response.java) - 统一响应类
- [`BizException.java`](../exceptions/BizException.java) - 业务异常类
- [`ErrorCode.java`](../errors/ErrorCode.java) - 错误码枚举

## 注意事项

1. 所有异常都会被记录日志，注意日志文件大小
2. 国际化消息配置文件需要与错误码保持同步
3. HTTP 状态码的选择应该符合 REST API 规范
4. 避免在异常处理中再次抛出异常