# AttributeController API 文档

## 概述

`AttributeController` 是在线商店系统中负责管理商品属性的 REST API 控制器。它提供了属性的创建、查询和更新功能，支持多种属性类型和输入控件类型，为商品管理提供灵活的属性配置能力。

## 类信息

- **包路径**: `com.example.onlinestore.controller`
- **基础路径**: `/api/v1/attributes`
- **框架**: Spring Boot REST Controller
- **依赖注入**: 使用 `@Autowired` 注入 `AttributeService`

## API 端点

### 1. 创建属性

**端点**: `POST /api/v1/attributes`

**描述**: 创建新的商品属性

**请求参数**:
```json
{
    "name": "属性名称",
    "inputType": "INPUT|SINGLE_SELECT|MULTI_SELECT",
    "required": 0|1,
    "searchable": 0|1,
    "sortScore": 100,
    "visible": 0|1,
    "attributeType": "SKU|SALE|OTHER"
}
```

**参数说明**:
- `name` (必填): 属性名称，用于前端展示
- `inputType` (必填): 输入控件类型
  - `INPUT`: 文本输入框
  - `SINGLE_SELECT`: 单选下拉框
  - `MULTI_SELECT`: 多选列表
- `required` (必填): 是否必填 (0-非必填, 1-必填)
- `searchable` (必填): 是否可搜索 (0-不可搜索, 1-可搜索)
- `sortScore` (必填): 排序权重，数值越大排序越靠前
- `visible` (必填): 是否可见 (0-不可见, 1-可见)
- `attributeType` (必填): 属性类型
  - `SKU`: SKU 属性
  - `SALE`: 销售属性
  - `OTHER`: 其他属性

**响应示例**:
```json
{
    "success": true,
    "message": "操作成功",
    "data": {
        "id": 1,
        "name": "颜色",
        "sortScore": 100,
        "visible": 1,
        "attributeType": "SKU",
        "inputType": "SINGLE_SELECT",
        "required": 1,
        "searchable": 1,
        "values": []
    }
}
```

### 2. 获取属性详情

**端点**: `GET /api/v1/attributes/{attributeId}`

**描述**: 根据属性ID获取属性详细信息

**路径参数**:
- `attributeId` (Long): 属性唯一标识符

**响应示例**:
```json
{
    "success": true,
    "message": "操作成功",
    "data": {
        "id": 1,
        "name": "颜色",
        "sortScore": 100,
        "visible": 1,
        "attributeType": "SKU",
        "inputType": "SINGLE_SELECT",
        "required": 1,
        "searchable": 1,
        "values": [
            {
                "id": 1,
                "value": "红色"
            },
            {
                "id": 2,
                "value": "蓝色"
            }
        ]
    }
}
```

### 3. 更新属性

**端点**: `PUT /api/v1/attributes/{attributeId}`

**描述**: 更新指定属性的信息

**路径参数**:
- `attributeId` (Long): 属性唯一标识符

**请求参数**:
```json
{
    "name": "新属性名称",
    "sortScore": 200,
    "visible": 1,
    "attributeType": "SALE",
    "inputType": "MULTI_SELECT",
    "required": 0,
    "searchable": 1
}
```

**参数说明**: 所有参数都是可选的，只需传入要更新的字段

**响应示例**:
```json
{
    "success": true,
    "message": "操作成功"
}
```

## 数据模型

### 属性实体 (Attribute)

| 字段 | 类型 | 描述 |
|------|------|------|
| id | Long | 属性唯一标识符 |
| name | String | 属性名称 |
| sortScore | Integer | 排序权重分值 |
| visible | Integer | 可见性标识 (0-不可见, 1-可见) |
| attributeType | AttributeType | 属性类型枚举 |
| inputType | AttributeInputType | 输入控件类型枚举 |
| required | Integer | 必填标识 (0-非必填, 1-必填) |
| searchable | Integer | 可搜索标识 (0-不可搜索, 1-可搜索) |
| values | List<AttributeValue> | 属性值列表 |

### 枚举类型

#### AttributeType (属性类型)
- `SKU`: SKU 属性
- `SALE`: 销售属性  
- `OTHER`: 其他属性

#### AttributeInputType (输入控件类型)
- `INPUT`: 文本输入框
- `SINGLE_SELECT`: 单选下拉框
- `MULTI_SELECT`: 多选列表

## 验证规则

### 创建属性请求验证
- 所有必填字段不能为空
- `inputType` 只能是 `INPUT`、`SINGLE_SELECT`、`MULTI_SELECT` 之一
- `required`、`searchable`、`visible` 只能是 0 或 1
- `attributeType` 只能是 `SKU`、`SALE`、`OTHER` 之一

## 错误处理

API 遵循统一的响应格式，当发生错误时会返回相应的错误信息：

```json
{
    "success": false,
    "message": "错误描述",
    "data": null
}
```

常见错误情况：
- 400 Bad Request: 请求参数验证失败
- 404 Not Found: 属性不存在
- 500 Internal Server Error: 服务器内部错误

## 使用示例

### cURL 示例

#### 创建属性
```bash
curl -X POST http://localhost:8080/api/v1/attributes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "尺寸",
    "inputType": "SINGLE_SELECT",
    "required": 1,
    "searchable": 1,
    "sortScore": 100,
    "visible": 1,
    "attributeType": "SKU"
  }'
```

#### 获取属性
```bash
curl -X GET http://localhost:8080/api/v1/attributes/1
```

#### 更新属性
```bash
curl -X PUT http://localhost:8080/api/v1/attributes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "商品尺寸",
    "sortScore": 200
  }'
```

## 业务场景

1. **商品 SKU 管理**: 为商品定义规格属性，如颜色、尺寸等
2. **销售属性配置**: 设置影响销售的属性，如品牌、材质等
3. **搜索筛选**: 配置可搜索的属性，支持商品筛选功能
4. **前端展示**: 控制属性在前端的显示顺序和可见性

## 依赖关系

- **服务层**: 依赖 `AttributeService` 处理业务逻辑
- **数据传输**: 使用 DTO 类进行数据传输和验证
  - `CreateAttributeRequest`: 创建属性请求
  - `UpdateAttributeRequest`: 更新属性请求
  - `AttributeResponse`: 属性响应数据
- **实体类**: 操作 `Attribute` 实体进行数据持久化

## 注意事项

1. 所有 API 都使用 JSON 格式进行数据交换
2. 创建和更新操作都会进行参数验证
3. 属性 ID 必须存在，否则会返回 404 错误
4. 排序权重建议使用 100 的倍数，便于后续插入新属性
5. 属性类型和输入类型一旦创建建议谨慎修改，可能影响已关联的商品数据

## 扩展功能

当前控制器提供了基础的 CRUD 操作，后续可以考虑扩展：
- 批量操作接口
- 属性值管理接口
- 属性复制功能
- 属性使用统计
- 软删除功能