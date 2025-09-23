# 📡 API 接口文档

## 🔗 基础信息

- **基础URL**: `http://localhost:8080`
- **API版本**: `v1`
- **请求格式**: `application/json`
- **响应格式**: `application/json`
- **字符编码**: `UTF-8`

## 🔐 认证方式

### JWT Token 认证
除了登录和注册接口外，所有API都需要在请求头中携带JWT token：

```http
Authorization: Bearer <your-jwt-token>
```

## 📋 通用响应格式

```json
{
  "code": 0,           // 状态码：0-成功，非0-失败
  "message": "success", // 响应消息
  "data": {},          // 响应数据
  "timestamp": 1695456789000  // 时间戳
}
```

## 👥 会员管理 API

### 1. 会员注册
- **接口地址**: `POST /api/v1/members/registry`
- **请求参数**:
```json
{
  "username": "testuser",      // 用户名，必填，4-20字符
  "password": "password123",   // 密码，必填，6-20字符
  "email": "test@example.com", // 邮箱，必填
  "phone": "13800138000",      // 手机号，可选
  "nickname": "测试用户",      // 昵称，可选
  "gender": "MALE"             // 性别：MALE/FEMALE/UNKNOWN，可选
}
```
- **响应示例**:
```json
{
  "code": 0,
  "message": "注册成功",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "nickname": "测试用户",
    "status": "ACTIVE",
    "createdAt": "2023-09-23T10:30:00"
  }
}
```

### 2. 会员登录
- **接口地址**: `POST /api/v1/members/login`
- **请求参数**:
```json
{
  "username": "testuser",    // 用户名或邮箱
  "password": "password123"  // 密码
}
```
- **响应示例**:
```json
{
  "code": 0,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "user": {
      "id": 1,
      "username": "testuser",
      "nickname": "测试用户",
      "email": "test@example.com"
    }
  }
}
```

## 🏷️ 品牌管理 API

### 1. 获取品牌列表
- **接口地址**: `GET /api/v1/brands`
- **请求参数**:
```
page: 1           // 页码，默认1
size: 20          // 每页数量，默认20
keyword: ""       // 搜索关键词，可选
status: "ACTIVE"  // 状态筛选，可选
```
- **响应示例**:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "苹果",
        "logoUrl": "https://example.com/apple-logo.png",
        "description": "Apple Inc. - 创新科技领导者",
        "status": "ACTIVE",
        "sortScore": 100,
        "createdAt": "2023-09-23T10:30:00"
      }
    ],
    "page": 1,
    "size": 20,
    "total": 50,
    "pages": 3
  }
}
```

### 2. 获取品牌详情
- **接口地址**: `GET /api/v1/brands/{id}`
- **路径参数**:
  - `id`: 品牌ID
- **响应示例**:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "name": "苹果",
    "logoUrl": "https://example.com/apple-logo.png",
    "description": "Apple Inc. - 创新科技领导者",
    "status": "ACTIVE",
    "sortScore": 100,
    "createdAt": "2023-09-23T10:30:00"
  }
}
```

## 📂 分类管理 API

### 1. 获取分类列表
- **接口地址**: `GET /api/v1/categories`
- **请求参数**:
```
parentId: 0      // 父分类ID，0表示获取顶级分类
level: 1         // 分类层级，可选
status: "ACTIVE" // 状态筛选，可选
```
- **响应示例**:
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "parentId": 0,
      "name": "服装鞋帽",
      "iconUrl": "https://example.com/category-icon.png",
      "description": "各类服装、鞋子、帽子等时尚单品",
      "status": "ACTIVE",
      "level": 1,
      "sortScore": 100,
      "children": [
        {
          "id": 4,
          "parentId": 1,
          "name": "男装",
          "level": 2,
          "sortScore": 50
        }
      ]
    }
  ]
}
```

## 🛍️ 商品管理 API

### 1. 获取商品列表
- **接口地址**: `GET /api/v1/items`
- **请求参数**:
```
page: 1            // 页码，默认1
size: 20           // 每页数量，默认20
categoryId: ""     // 分类ID筛选，可选
brandId: ""        // 品牌ID筛选，可选
keyword: ""        // 搜索关键词，可选
status: "ON_SALE"  // 状态筛选，可选
sortBy: "createdAt" // 排序字段，可选
sortOrder: "desc"  // 排序方向：asc/desc，可选
```
- **响应示例**:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "iPhone 15 Pro",
        "description": "全新A17 Pro芯片，钛金属设计",
        "mainImageUrl": "https://example.com/iphone15pro.jpg",
        "subImageUrls": ["https://example.com/img1.jpg"],
        "brandId": 1,
        "brandName": "苹果",
        "categoryId": 6,
        "categoryName": "手机通讯",
        "status": "ON_SALE",
        "sortScore": 100,
        "createdAt": "2023-09-23T10:30:00"
      }
    ],
    "page": 1,
    "size": 20,
    "total": 100,
    "pages": 5
  }
}
```

### 2. 获取商品详情
- **接口地址**: `GET /api/v1/items/{id}`
- **路径参数**:
  - `id`: 商品ID
- **响应示例**:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "id": 1,
    "name": "iPhone 15 Pro",
    "description": "全新A17 Pro芯片，钛金属设计",
    "mainImageUrl": "https://example.com/iphone15pro.jpg",
    "subImageUrls": ["https://example.com/img1.jpg"],
    "brand": {
      "id": 1,
      "name": "苹果"
    },
    "category": {
      "id": 6,
      "name": "手机通讯"
    },
    "attributes": [
      {
        "attributeId": 1,
        "attributeName": "颜色",
        "attributeValueId": 1,
        "attributeValueName": "深空黑"
      }
    ],
    "skus": [
      {
        "id": 1,
        "skuCode": "IPH15PRO-128G-BLACK",
        "skuName": "iPhone 15 Pro 128GB 深空黑",
        "price": 7999.00,
        "originalPrice": 8999.00,
        "stock": 100,
        "skuImageUrl": "https://example.com/sku-image.jpg"
      }
    ],
    "status": "ON_SALE",
    "createdAt": "2023-09-23T10:30:00"
  }
}
```

### 3. 创建商品
- **接口地址**: `POST /api/v1/items`
- **请求参数**:
```json
{
  "name": "iPhone 15 Pro",
  "description": "全新A17 Pro芯片，钛金属设计",
  "mainImageUrl": "https://example.com/iphone15pro.jpg",
  "subImageUrls": ["https://example.com/img1.jpg"],
  "brandId": 1,
  "categoryId": 6,
  "status": "ON_SALE",
  "sortScore": 100,
  "attributes": [
    {
      "attributeId": 1,
      "attributeValueId": 1
    }
  ],
  "skus": [
    {
      "skuCode": "IPH15PRO-128G-BLACK",
      "skuName": "iPhone 15 Pro 128GB 深空黑",
      "price": 7999.00,
      "originalPrice": 8999.00,
      "stock": 100,
      "lowStockThreshold": 10
    }
  ]
}
```

### 4. 更新商品
- **接口地址**: `PUT /api/v1/items/{id}`
- **路径参数**:
  - `id`: 商品ID
- **请求参数**: 同创建商品接口

## 🎯 属性管理 API

### 1. 获取属性列表
- **接口地址**: `GET /api/v1/attributes`
- **请求参数**:
```
type: ""          // 属性类型筛选，可选：SPECIFICATION/SELLING_POINT
searchType: ""    // 搜索类型筛选，可选
```
- **响应示例**:
```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "颜色",
      "type": "SPECIFICATION",
      "inputType": "SELECT",
      "searchType": "NOT_SEARCH",
      "relatedStatus": 1,
      "sortScore": 100,
      "values": [
        {
          "id": 1,
          "value": "黑色",
          "sortScore": 100
        }
      ]
    }
  ]
}
```

### 2. 创建属性
- **接口地址**: `POST /api/v1/attributes`
- **请求参数**:
```json
{
  "name": "颜色",
  "type": "SPECIFICATION",
  "inputType": "SELECT",
  "searchType": "NOT_SEARCH",
  "relatedStatus": 1,
  "sortScore": 100,
  "values": ["黑色", "白色", "红色"]
}
```

## 📊 错误码说明

| 错误码 | 说明 | 备注 |
|--------|------|------|
| 0 | 成功 | - |
| 10001 | 参数错误 | 请求参数校验失败 |
| 10002 | 资源不存在 | 请求的资源不存在 |
| 10003 | 权限不足 | 没有访问权限 |
| 20001 | 用户名已存在 | 注册时用户名重复 |
| 20002 | 邮箱已存在 | 注册时邮箱重复 |
| 20003 | 用户名或密码错误 | 登录失败 |
| 20004 | 账户已被禁用 | 用户状态为INACTIVE |
| 30001 | JWT Token无效 | Token格式错误或已过期 |
| 30002 | JWT Token已过期 | Token超过有效期 |
| 40001 | 商品名称已存在 | 创建商品时名称重复 |
| 40002 | 商品不存在 | 商品ID不存在 |
| 50001 | 系统内部错误 | 服务器内部异常 |

## 🔍 接口测试示例

### 使用curl测试

```bash
# 1. 用户注册
curl -X POST http://localhost:8080/api/v1/members/registry \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "nickname": "测试用户"
  }'

# 2. 用户登录
curl -X POST http://localhost:8080/api/v1/members/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'

# 3. 获取商品列表（需要先登录获取token）
curl -X GET "http://localhost:8080/api/v1/items?page=1&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# 4. 获取商品详情
curl -X GET http://localhost:8080/api/v1/items/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 使用Postman测试

1. **设置环境变量**:
   - `base_url`: `http://localhost:8080`
   - `token`: 登录后获取的JWT token

2. **创建Collection**:
   - 导入上述API接口
   - 在Collection级别设置Authorization为Bearer Token
   - 使用环境变量 `{{token}}`

3. **测试流程**:
   - 先调用注册接口创建用户
   - 调用登录接口获取token
   - 使用token访问其他需要认证的接口

---

💡 **提示**: 
- 所有时间字段均采用ISO8601格式
- 分页从1开始计算
- 建议在生产环境中启用HTTPS
- API接口支持跨域访问（CORS）