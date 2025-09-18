# Online Store

这是一个基于Spring Cloud的在线商店项目。

## 系统架构

### 整体架构图

```mermaid
graph TB
    Client[客户端] --> Gateway[API网关]
    Gateway --> AuthC[认证控制器]
    Gateway --> UserC[用户控制器]
    Gateway --> ItemC[商品控制器]
    Gateway --> OrderC[订单控制器]
    Gateway --> CommentC[评论控制器]
    Gateway --> CategoryC[分类控制器]
    
    AuthC --> AuthS[认证服务]
    UserC --> UserS[用户服务]
    ItemC --> ItemS[商品服务]
    ItemC --> ItemDetailS[商品详情服务]
    ItemC --> InventoryS[库存服务]
    OrderC --> OrderS[订单服务]
    OrderC --> CartS[购物车服务]
    CommentC --> CommentS[评论服务]
    CategoryC --> CategoryS[分类服务]
    CategoryC --> CategoryAttrS[分类属性服务]
    
    UserS --> CacheLayer[缓存层]
    ItemS --> CacheLayer
    OrderS --> CacheLayer
    CommentS --> CacheLayer
    
    CacheLayer --> LocalCache[本地缓存]
    CacheLayer --> Redis[Redis缓存]
    
    AuthS --> MySQL[(MySQL数据库)]
    UserS --> MySQL
    ItemS --> MySQL
    OrderS --> MySQL
    CommentS --> MySQL
    CategoryS --> MySQL
    
    ItemS --> OSS[对象存储OSS]
```

### 核心模块架构

```mermaid
graph LR
    subgraph "用户模块"
        UserController --> UserService
        UserController --> AddressController
        AddressController --> AddressService
    end
    
    subgraph "商品模块"
        ItemController --> ItemService
        ItemDetailController --> ItemDetailService
        ItemStatisticsController --> ItemAccessLogService
        CategoryController --> CategoryService
        CategoryAttributeController --> CategoryAttributeService
    end
    
    subgraph "订单模块"
        OrderController --> OrderService
        OrderController --> CartService
        PriceCalculatorController --> PriceCalculator
    end
    
    subgraph "评论模块"
        CommentController --> CommentService
        CommentService --> CommentHookManager
    end
```

### 数据层架构

```mermaid
graph TB
    Service[业务服务层] --> Mapper[MyBatis映射层]
    Mapper --> MySQL[(MySQL数据库)]
    
    subgraph "核心实体"
        UserEntity[用户实体]
        ItemEntity[商品实体]
        OrderEntity[订单实体]
        CommentEntity[评论实体]
        CategoryEntity[分类实体]
        SkuEntity[SKU实体]
    end
    
    subgraph "扩展实体"
        AddressEntity[地址实体]
        CartItemEntity[购物车项实体]
        InventoryEntity[库存实体]
        OrderItemEntity[订单项实体]
    end
```

## 技术栈

- JDK 17
- Spring Cloud 2022.0.4
- Spring Boot 3.1.5
- MyBatis 3.0.2
- MySQL 8.0
- Redis (Jedis 4.3.1)

## 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── onlinestore/
│   │   │               ├── OnlineStoreApplication.java
│   │   │               ├── controller/
│   │   │               ├── service/
│   │   │               ├── mapper/
│   │   │               └── entity/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── mapper/
│   └── test/
├── pom.xml
└── README.md
```

## 运行要求

- JDK 17或更高版本
- Maven 3.6或更高版本
- MySQL 8.0
- Redis 6.0或更高版本

## 如何运行

1. 确保MySQL和Redis服务已启动
2. 创建数据库：
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
3. 修改`application.yml`中的数据库和Redis配置
4. 添加vm参数：`--add-opens java.base/java.lang=ALL-UNNAMED` 
4. 运行应用程序：
```bash
mvn spring-boot:run
``` 