# Online Store

一个基于 Spring Cloud 和微服务架构的现代化在线商店系统，提供商品管理、品牌管理、分类管理、会员管理等核心电商功能。

## 📋 目录

- [技术栈](#技术栈)
- [核心功能](#核心功能)
- [项目结构](#项目结构)
- [运行要求](#运行要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API 文档](#api-文档)
- [数据库设计](#数据库设计)

## 🛠 技术栈

### 后端框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0
- **Spring Security**: 基于 Spring Boot 3.x

### 数据层
- **MyBatis**: 3.0.2
- **PageHelper**: 2.1.0 (分页插件)
- **MySQL**: 8.2.0
- **Redis**: Jedis 5.2.0

### 服务治理
- **Nacos**: 2.2.0 (服务注册与配置中心)

### 安全认证
- **JWT**: 0.11.5 (JSON Web Token)
- **Spring Security**: 用户认证与授权

### 其他核心依赖
- **Lombok**: 1.18.36 (简化 Java 代码)
- **Apache Commons Lang3**: 3.17.0
- **阿里云 OSS**: 3.18.1 (对象存储服务)

## ✨ 核心功能

- **商品管理**: 商品信息管理、SKU 管理、商品属性关联
- **品牌管理**: 品牌信息维护与查询
- **分类管理**: 商品分类层级管理
- **会员管理**: 用户注册、登录、信息维护
- **属性管理**: 商品属性与属性值管理
- **访问统计**: 商品访问日志记录
- **安全认证**: 基于 JWT 的身份认证机制
- **数据缓存**: Redis 缓存提升性能

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用入口
│   │   │   ├── bean/                          # JavaBean 配置
│   │   │   ├── config/                        # 配置类（Redis、Security 等）
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # REST API Controllers
│   │   │   │   ├── AttributeController.java   # Attribute management
│   │   │   │   ├── BrandController.java       # Brand management
│   │   │   │   ├── CategoryController.java    # Category management
│   │   │   │   ├── ItemController.java        # Item management
│   │   │   │   ├── ItemDetailController.java  # Item details
│   │   │   │   └── MemberController.java      # Member management
│   │   │   ├── dto/                           # Data Transfer Objects
│   │   │   ├── entity/                        # Entity classes
│   │   │   ├── enums/                         # Enum types
│   │   │   ├── exceptions/                    # Custom exceptions
│   │   │   ├── handler/                       # Exception handlers
│   │   │   ├── mapper/                        # MyBatis Mapper interfaces
│   │   │   ├── security/                      # Security configuration
│   │   │   ├── service/                       # Business logic layer
│   │   │   └── utils/                         # Utility classes
│   │   └── resources/
│   │       ├── application.yaml               # Main configuration file
│   │       ├── application-local.yaml         # Local environment config
│   │       ├── bootstrap.yaml                 # Bootstrap config (Nacos)
│   │       ├── i18n/                          # Internationalization resources
│   │       ├── mapper/                        # MyBatis XML mappers
│   │       └── sql/                           # Database table scripts
│   └── test/                                  # Test code
├── scripts/                                   # Python script tools
├── docker-compose.yaml                        # Docker Compose configuration
├── Dockerfile                                 # Docker image build file
├── pom.xml                                    # Maven project configuration
└── README.md                                  # Project documentation
```

## 📦 运行要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6+ 
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Nacos**: 2.2.0+ (可选，默认关闭)

## 🚀 快速开始

### 方式一：使用 Docker Compose（推荐）

1. **启动数据库服务**

```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 或仅启动 MySQL
docker-compose --profile without-redis up -d
```

2. **初始化数据库**

```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE online_store;

-- 执行建表脚本（位于 src/main/resources/sql/ 目录）
source src/main/resources/sql/attribute_table.sql;
source src/main/resources/sql/attribute_value_table.sql;
source src/main/resources/sql/brand_table.sql;
source src/main/resources/sql/category_table.sql;
source src/main/resources/sql/item_table_table.sql;
source src/main/resources/sql/sku_table.sql;
source src/main/resources/sql/item_attribute_relation_table.sql;
source src/main/resources/sql/item_access_log_table.sql;
source src/main/resources/sql/member_table.sql;
```

3. **配置环境变量**

```bash
# 设置 JWT 密钥（必需）
export JWT_SECRET="your-secret-key-here"

# 可选：配置 Nacos
export NACOS_ENABLED=true
```

4. **运行应用**

```bash
# 方式1: 使用 Maven
mvn spring-boot:run

# 方式2: 打包后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 方式二：本地环境运行

1. **确保 MySQL 和 Redis 已启动**

2. **修改配置文件**

编辑 `src/main/resources/application-local.yaml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password  # 如有密码
```

3. **执行数据库初始化脚本**（参考上述步骤）

4. **运行应用**

```bash
mvn spring-boot:run
```

5. **访问应用**

应用默认运行在 `http://localhost:8080`

## ⚙️ 配置说明

### 环境变量

| 变量名 | 说明 | 默认值 | 是否必需 |
|--------|------|--------|----------|
| `JWT_SECRET` | JWT 签名密钥 | 无 | ✅ |
| `SPRING_PROFILES_ACTIVE` | 运行环境 | `local` | ❌ |
| `NACOS_ENABLED` | 是否启用 Nacos | `false` | ❌ |
| `ADMIN_USERNAME` | 管理员用户名 | `admin` | ❌ |
| `ADMIN_PASSWORD` | 管理员密码 | `admin123` | ❌ |

### 配置文件说明

- **application.yaml**: 主配置文件，定义通用配置
- **application-local.yaml**: 本地开发环境配置
- **bootstrap.yaml**: Nacos 配置中心引导配置

### JVM 参数

运行应用时需要添加以下 JVM 参数以支持反射访问：

```bash
--add-opens java.base/java.lang=ALL-UNNAMED
```

## 📚 API 文档

### 主要接口模块

- **商品管理**: `/api/items`
- **品牌管理**: `/api/brands`
- **分类管理**: `/api/categories`
- **会员管理**: `/api/members`
- **属性管理**: `/api/attributes`

### 认证方式

所有 API 请求需要在 Header 中携带 JWT Token：

```
Authorization: Bearer <your-jwt-token>
```

或使用 Basic Auth（用户名/密码）进行基础认证。

## 🗄️ 数据库设计

项目包含以下核心数据表：

- **attribute**: 商品属性表
- **attribute_value**: 属性值表
- **brand**: 品牌表
- **category**: 商品分类表
- **item**: 商品基本信息表
- **sku**: 商品 SKU 表
- **item_attribute_relation**: 商品属性关联表
- **item_access_log**: 商品访问日志表
- **member**: 会员信息表

所有建表脚本位于 `src/main/resources/sql/` 目录。

## 🐛 常见问题

### 1. 启动时报错：JWT_SECRET 未配置

**解决方案**：设置环境变量 `JWT_SECRET`

```bash
export JWT_SECRET="your-secret-key"
```

### 2. MyBatis 映射文件找不到

**解决方案**：检查 `application.yaml` 中的配置：

```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml
```

### 3. Redis 连接失败

**解决方案**：确认 Redis 服务已启动，检查连接配置是否正确

```bash
# 测试 Redis 连接
redis-cli ping
```

## 📄 许可证

本项目仅供学习和参考使用。 