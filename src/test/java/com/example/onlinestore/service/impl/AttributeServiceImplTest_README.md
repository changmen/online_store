# AttributeServiceImpl 单元测试说明

## 概述
本测试文件为 `AttributeServiceImpl` 提供了全面的单元测试覆盖，使用 JUnit 5 和 Mockito 框架。

## 测试框架和依赖
- **JUnit 5**: 测试框架
- **Mockito**: Mock 框架，用于模拟依赖
- **Spring Boot Test**: Spring Boot 测试支持

## 测试覆盖的方法

### 1. createAttribute (创建属性)
- ✅ `testCreateAttribute_Success` - 成功创建属性
- ✅ `testCreateAttribute_DuplicateName` - 属性名称重复时抛出异常
- ✅ `testCreateAttribute_InsertFailed` - 插入失败时抛出异常

### 2. updateAttribute (更新属性)
- ✅ `testUpdateAttribute_Success` - 成功更新属性
- ✅ `testUpdateAttribute_NoChanges` - 没有字段变化时不执行更新
- ✅ `testUpdateAttribute_NotFound` - 属性不存在时抛出异常
- ✅ `testUpdateAttribute_UpdateFailed` - 更新失败时抛出异常

### 3. deleteAttribute (删除属性)
- ✅ `testDeleteAttribute_Success` - 成功删除属性和关联的属性值
- ✅ `testDeleteAttribute_NotFound` - 属性不存在时抛出异常
- ✅ `testDeleteAttribute_ReferencedByItem` - 被商品引用时不能删除
- ✅ `testDeleteAttribute_DeleteFailed` - 删除失败时抛出异常
- ✅ `testDeleteAttribute_NoAttributeValues` - 没有属性值时的删除逻辑
- ✅ `testDeleteAttribute_DeleteValuesFailed` - 删除属性值失败时抛出异常

### 4. getAttributeById (根据ID获取属性)
- ✅ `testGetAttributeById_Success` - 成功获取属性
- ✅ `testGetAttributeById_NotFound` - 属性不存在时抛出异常

### 5. getAttributeByIdWithValues (获取属性及其值)
- ✅ `testGetAttributeByIdWithValues_SingleSelect` - 获取单选/多选属性及其值
- ✅ `testGetAttributeByIdWithValues_InputType` - INPUT 类型不返回属性值

### 6. findAllAttributeValuesByAttributeId (查询属性值列表)
- ✅ `testFindAllAttributeValuesByAttributeId_Success` - 成功查询属性值
- ✅ `testFindAllAttributeValuesByAttributeId_InputType` - INPUT 类型返回空列表

### 7. getAttributeValueById (根据ID获取属性值)
- ✅ `testGetAttributeValueById_Success` - 成功获取属性值
- ✅ `testGetAttributeValueById_NotFound` - 属性值不存在时抛出异常

### 8. ensureItemAttributes (确保商品属性关联)
- ✅ `testEnsureItemAttributes_NewAttributes` - 添加新的属性关联
- ✅ `testEnsureItemAttributes_UpdateExisting` - 更新已存在的属性关联
- ✅ `testEnsureItemAttributes_NoNewRelations` - 没有新关联时不执行插入
- ✅ `testEnsureItemAttributes_DeleteFailed` - 删除旧关联失败时抛出异常
- ✅ `testEnsureItemAttributes_InsertFailed` - 插入新关联失败时抛出异常

## 测试数据准备

### 默认测试数据
在 `@BeforeEach` 中初始化以下测试数据：
- **CreateAttributeRequest**: 创建"颜色"属性的请求
- **UpdateAttributeRequest**: 更新为"尺寸"属性的请求
- **AttributeEntity**: ID为1的"颜色"属性实体

### Mock 行为
所有的 Mapper 依赖都通过 Mockito 进行 Mock：
- `AttributeMapper`: 属性数据访问
- `AttributeValueMapper`: 属性值数据访问
- `ItemAttributeRelationMapper`: 商品属性关联数据访问

## 运行测试

### 使用 Maven
```bash
mvn test -Dtest=AttributeServiceImplTest
```

### 使用 Maven Wrapper
```bash
./mvnw test -Dtest=AttributeServiceImplTest
```

### 在 IDE 中运行
直接在 IDE 中右键点击测试类或单个测试方法，选择"Run"或"Debug"。

## 测试覆盖率
本测试套件提供了以下覆盖：
- ✅ 所有公共方法的正常流程
- ✅ 所有异常情况和边界条件
- ✅ 业务逻辑的分支覆盖
- ✅ 数据库操作失败的场景
- ✅ 事务回滚的场景

## 注意事项
1. 所有测试使用 Mock 对象，不依赖真实数据库
2. 测试遵循 AAA 模式（Arrange-Act-Assert）
3. 每个测试方法都是独立的，互不影响
4. 使用 `ArgumentCaptor` 验证传递给 Mapper 的参数
5. 测试覆盖了所有异常路径和错误码

## 后续改进建议
1. 可以添加参数化测试来测试不同的输入组合
2. 可以添加集成测试来验证与数据库的交互
3. 可以使用 TestContainers 进行真实数据库测试
