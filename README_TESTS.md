# AddressServiceImpl 单元测试说明

## 测试文件结构

```
src/test/java/com/example/onlinestore/service/impl/
├── AddressServiceImplTest.java          # 主测试类 (21个测试方法)
└── AddressServiceImplTestPart2.java     # 补充测试类 (7个测试方法)
```

## 运行测试

### 使用 Maven
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=AddressServiceImplTest
mvn test -Dtest=AddressServiceImplTestPart2

# 运行测试并生成报告
mvn test jacoco:report
```

### 使用 IDE
- **IntelliJ IDEA**: 右键点击测试类 → Run 'AddressServiceImplTest'
- **Eclipse**: 右键点击测试类 → Run As → JUnit Test
- **VS Code**: 使用 Java Test Runner 扩展

## 测试覆盖范围

### 1. addAddress 方法测试 (4个用例)
- ✅ 添加默认地址 - 清除旧默认并插入新地址
- ✅ 添加非默认地址 - 直接插入不清除默认状态
- ✅ isDefault为null - 正常插入不执行清除默认操作
- ✅ 事务注解验证 - 方法应该支持事务管理

### 2. getAddress 方法测试 (4个用例)
- ✅ 获取存在的地址 - 返回正确的地址实体
- ✅ 获取不存在的地址 - 返回null
- ✅ null ID查询 - 正常处理并返回mapper结果
- ✅ 直接委托验证 - 应该直接返回Mapper结果

### 3. getUserAddresses 方法测试 (4个用例)
- ✅ 用户有地址记录 - 返回完整地址列表
- ✅ 用户无地址记录 - 返回空列表
- ✅ null用户ID - 正常处理并返回mapper结果
- ✅ 直接委托验证 - 应该直接返回Mapper结果

### 4. updateAddress 方法测试 (4个用例)
- ✅ 更新为默认地址 - 清除旧默认、更新地址、同步订单信息
- ✅ 更新非默认地址 - 直接更新地址并同步订单信息
- ✅ 有关联订单的地址 - 正确同步相关订单的收货信息
- ✅ 不存在的地址 - 正常执行更新和订单同步逻辑

### 5. deleteAddress 方法测试 (5个用例)
- ✅ 删除有关联订单的地址 - 更新订单收货信息为"已删除"
- ✅ 删除无关联订单的地址 - 直接删除地址记录
- ✅ 删除不存在的地址 - 静默处理不执行任何操作
- ✅ 地址匹配算法测试 - 正确匹配订单和地址
- ✅ null ID删除 - 正常处理并返回mapper结果

### 6. setDefaultAddress 方法测试 (7个用例)
- ✅ 设置存在的地址为默认 - 清除旧默认、设置新默认、同步订单
- ✅ 设置不存在的地址为默认 - 清除旧默认、设置操作、不执行订单同步
- ✅ 同步相关订单 - 正确更新匹配订单的收货信息
- ✅ 无相关订单 - 正常执行设置操作但不进行订单更新
- ✅ null参数处理 - 正常处理并委托给mapper
- ✅ 事务注解验证 - 方法应该支持事务管理
- ✅ 地址格式化验证 - 通过订单同步验证formatAddress方法

## 私有方法间接测试

### updateRelatedOrders 方法
- 通过 `updateAddress` 和 `setDefaultAddress` 测试验证
- 验证订单查询条件的正确性
- 验证订单更新逻辑的准确性

### formatAddress 方法
- 通过订单同步操作间接测试
- 验证地址格式化的完整性 (省 市 区 详细地址)
- 验证空值处理的安全性

### matchAddress 方法
- 通过 `deleteAddress` 和 `updateRelatedOrders` 间接测试
- 验证地址匹配算法的准确性 (姓名、电话、地址三重匹配)
- 验证字符串比较的安全性

## Mock 对象配置

### AddressMapper Mock
- `insertAddress` - 模拟插入操作并设置ID
- `findById` - 条件返回地址实体或null
- `findByUserId` - 返回用户地址列表或空列表
- `updateAddress` - 模拟更新操作
- `deleteAddress` - 模拟删除操作
- `clearDefaultAddress` - 模拟清除默认地址
- `setDefaultAddress` - 模拟设置默认地址

### OrderMapper Mock
- `findByCondition` - 返回匹配条件的订单列表
- `updateOrder` - 模拟订单更新操作

## 测试数据设计

### 地址实体测试数据
- **标准地址**: 完整的省市区详细地址信息
- **默认地址**: isDefault = true
- **非默认地址**: isDefault = false/null

### 订单实体测试数据
- **待付款订单**: status = 0 (可更新的订单状态)
- **匹配订单**: 收货信息与地址匹配
- **不匹配订单**: 收货信息与地址不匹配

## 断言策略

### 返回值验证
- 地址ID的正确返回
- 地址实体的完整性
- 集合数据的准确性

### 交互验证
- Mock方法的调用次数 (`times()`)
- 方法参数的正确性 (`ArgumentCaptor`)
- 调用顺序的验证 (`inOrder()`)

### 状态验证
- 对象状态的变更
- 业务规则的执行
- 数据一致性的保证

## 预期测试覆盖率

- **行覆盖率**: ≥ 95%
- **分支覆盖率**: ≥ 90%
- **方法覆盖率**: 100%

## 注意事项

1. **事务测试**: 实际的事务行为需要在集成测试中验证
2. **并发测试**: 可以添加并发访问的测试场景
3. **性能测试**: 可以添加大数据量的性能测试
4. **集成测试**: 建议添加与数据库的集成测试

## 扩展建议

### 1. 添加集成测试
```java
@SpringBootTest
@AutoConfigureTestDatabase
class AddressServiceImplIntegrationTest {
    // 真实数据库测试
}
```

### 2. 添加并发测试
```java
@Test
void updateAddress_ConcurrentAccess_ShouldHandleCorrectly() {
    // 并发访问测试
}
```

### 3. 添加性能测试
```java
@Test
void getUserAddresses_LargeDataSet_ShouldPerformWell() {
    // 大数据量性能测试
}
```

## 验证脚本

运行验证脚本确认测试完整性：
```bash
./verify_tests.sh
```

## 总结

本测试套件为 AddressServiceImpl 提供了全面的单元测试覆盖，符合设计文档要求，确保了代码质量和业务逻辑的正确性。测试设计遵循了最佳实践，包括合理的Mock使用、完整的断言验证和清晰的测试组织结构。