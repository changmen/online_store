# AddressServiceImpl 单元测试说明

## 测试覆盖范围

本测试套件为 `AddressServiceImpl` 提供了全面的单元测试覆盖，包括以下功能：

### 1. 创建地址 (createAddress)
- ✅ 成功创建普通地址
- ✅ 成功创建默认地址（会清除其他默认地址）
- ✅ 超过地址数量限制时抛出异常
- ✅ 数据库插入失败时抛出异常

### 2. 更新地址 (updateAddress)
- ✅ 成功更新地址信息
- ✅ 地址不存在时抛出异常
- ✅ 无权访问他人地址时抛出异常
- ✅ 数据库更新失败时抛出异常

### 3. 删除地址 (deleteAddress)
- ✅ 成功删除地址
- ✅ 地址不存在时抛出异常
- ✅ 无权访问他人地址时抛出异常
- ✅ 数据库删除失败时抛出异常

### 4. 根据ID获取地址 (getAddressById)
- ✅ 成功获取地址信息
- ✅ 地址不存在时抛出异常
- ✅ 无权访问他人地址时抛出异常

### 5. 获取用户所有地址 (getAddressesByMemberId)
- ✅ 成功获取用户地址列表
- ✅ 用户无地址时返回空列表

### 6. 获取默认地址 (getDefaultAddress)
- ✅ 成功获取默认地址
- ✅ 无默认地址时返回null

### 7. 设置默认地址 (setDefaultAddress)
- ✅ 成功设置默认地址
- ✅ 地址不存在时抛出异常
- ✅ 无权访问他人地址时抛出异常
- ✅ 数据库设置失败时抛出异常

## 运行测试

使用以下命令运行测试：

```bash
# 运行所有测试
mvn test

# 只运行 AddressServiceImpl 测试
mvn test -Dtest=AddressServiceImplTest

# 使用 Gradle（如果项目使用 Gradle）
./gradlew test --tests AddressServiceImplTest
```

## 测试统计

- **总测试方法数**: 22个
- **测试覆盖的功能点**: 7个主要功能
- **异常场景覆盖**: 13个
- **成功场景覆盖**: 9个

## 测试技术栈

- **测试框架**: JUnit 5
- **Mock框架**: Mockito
- **断言库**: JUnit 5 Assertions
- **测试注解**: 
  - `@ExtendWith(MockitoExtension.class)` - 启用Mockito支持
  - `@Mock` - 创建Mock对象
  - `@InjectMocks` - 注入Mock依赖
  - `@DisplayName` - 提供测试描述
  - `@BeforeEach` - 测试前准备

## 业务规则验证

测试验证了以下业务规则：

1. **地址数量限制**: 每个用户最多20个地址
2. **默认地址唯一性**: 每个用户只能有一个默认地址
3. **权限控制**: 用户只能操作自己的地址
4. **数据一致性**: 数据库操作失败时正确抛出异常
5. **参数校验**: 空值和无效参数的处理

所有测试用例都遵循AAA模式（Arrange-Act-Assert），确保测试的清晰性和可维护性。