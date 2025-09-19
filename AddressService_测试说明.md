# AddressService 单元测试说明

## 概述
为 `AddressServiceImpl.java` 生成了完整的单元测试，包括单元测试和集成测试。

## 创建的文件

### 1. 核心业务类
- `src/main/java/com/example/onlinestore/bean/Address.java` - 地址实体Bean
- `src/main/java/com/example/onlinestore/entity/AddressEntity.java` - 地址数据库实体
- `src/main/java/com/example/onlinestore/service/AddressService.java` - 地址服务接口
- `src/main/java/com/example/onlinestore/service/impl/AddressServiceImpl.java` - 地址服务实现
- `src/main/java/com/example/onlinestore/mapper/AddressMapper.java` - 地址数据访问接口

### 2. 测试文件
- `src/test/java/com/example/onlinestore/service/impl/AddressServiceImplTest.java` - 单元测试（使用Mock）
- `src/test/java/com/example/onlinestore/service/impl/AddressServiceIntegrationTest.java` - 集成测试
- `src/test/resources/application-test.yml` - 测试环境配置

### 3. 错误码扩展
在 `ErrorCode.java` 中添加了地址相关的错误码：
- `ADDRESS_NOT_FOUND` - 地址不存在
- `ADDRESS_NOT_BELONG_TO_MEMBER` - 地址不属于指定用户
- `PARAMETER_ERROR` - 参数错误

## 测试覆盖范围

### AddressServiceImplTest (单元测试)
使用 Mockito 框架进行 Mock 测试，覆盖以下方法：

1. **createAddress**
   - ✅ 成功创建地址
   - ✅ 创建默认地址（自动清除其他默认地址）
   - ✅ 插入失败异常处理

2. **updateAddress**
   - ✅ 成功更新地址
   - ✅ 地址ID为空异常
   - ✅ 地址不存在异常
   - ✅ 更新为默认地址（自动清除其他默认地址）

3. **deleteAddress**
   - ✅ 成功删除地址
   - ✅ 地址不存在异常
   - ✅ 删除失败异常

4. **getAddressById**
   - ✅ 成功获取地址
   - ✅ 地址不存在异常

5. **getAddressesByMemberId**
   - ✅ 成功获取用户地址列表
   - ✅ 空列表情况

6. **getDefaultAddress**
   - ✅ 成功获取默认地址
   - ✅ 无默认地址情况（返回null）

7. **setDefaultAddress**
   - ✅ 成功设置默认地址
   - ✅ 地址不存在异常
   - ✅ 地址不属于指定用户异常

8. **getCurrentUserAddresses**
   - ✅ 成功获取当前登录用户地址

9. **getCurrentUserDefaultAddress**
   - ✅ 成功获取当前登录用户默认地址
   - ✅ 无默认地址情况

### AddressServiceIntegrationTest (集成测试)
测试完整的业务流程：
- ✅ 地址完整生命周期（创建→获取→更新→删除）
- ✅ 多地址默认地址管理

## 运行测试

### 方法1: 使用Maven命令
```bash
# 运行所有测试
mvn test

# 只运行AddressServiceImpl相关测试
mvn test -Dtest=AddressServiceImplTest

# 运行集成测试（需要数据库环境）
mvn test -Dtest=AddressServiceIntegrationTest
```

### 方法2: 使用IDE
在IDE中右键点击测试类或测试方法，选择"Run Test"

## 测试依赖

### 单元测试依赖
- JUnit 5
- Mockito
- Spring Boot Test

### 集成测试依赖
- 需要配置测试数据库（推荐使用H2内存数据库）
- 需要相应的MyBatis映射文件

## 注意事项

1. **单元测试** (`AddressServiceImplTest`) 可以独立运行，不需要数据库环境
2. **集成测试** (`AddressServiceIntegrationTest`) 需要完整的Spring Boot环境和数据库配置
3. 集成测试使用了 `@Transactional` 注解，确保测试后数据回滚
4. 测试数据使用了合理的中文场景数据

## 测试质量

- **代码覆盖率**: 覆盖了所有公共方法和主要分支逻辑
- **边界测试**: 包含了各种异常情况和边界条件
- **业务逻辑**: 验证了默认地址管理等核心业务逻辑
- **异常处理**: 测试了各种业务异常的正确抛出