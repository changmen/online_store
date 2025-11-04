# Online Store

一个基于 Spring Cloud 和微服务架构的现代化在线商店系统，提供商品管理、品牌管理、分类管理、会员管理等核心电商功能。

## 📋 Table of Contents

- [Tech Stack](#tech-stack)
- [Core Features](#core-features)
- [Project Structure](#project-structure)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Database Design](#database-design)

## 🛠 Tech Stack

### Backend Framework
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0
- **Spring Security**: 基于 Spring Boot 3.x

### Data Layer
- **MyBatis**: 3.0.2
- **PageHelper**: 2.1.0 (分页插件)
- **MySQL**: 8.2.0
- **Redis**: Jedis 5.2.0

### Service Governance
- **Nacos**: 2.2.0 (服务注册与配置中心)

### Security & Authentication
- **JWT**: 0.11.5 (JSON Web Token)
- **Spring Security**: 用户认证与授权

### Other Dependencies
- **Lombok**: 1.18.36 (简化 Java 代码)
- **Apache Commons Lang3**: 3.17.0
- **阿里云 OSS**: 3.18.1 (对象存储服务)

## ✨ Core Features

- **商品管理**: 商品信息管理、SKU 管理、商品属性关联
- **品牌管理**: 品牌信息维护与查询
- **分类管理**: 商品分类层级管理
- **会员管理**: 用户注册、登录、信息维护
- **属性管理**: 商品属性与属性值管理
- **访问统计**: 商品访问日志记录
- **安全认证**: 基于 JWT 的身份认证机制
- **数据缓存**: Redis 缓存提升性能

## 📁 Project Structure

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

## 📦 Requirements

- **JDK**: 17 or higher
- **Maven**: 3.6+ 
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Nacos**: 2.2.0+ (Optional, disabled by default)

## 🚀 Quick Start

### Option 1: Using Docker Compose (Recommended)

1. **Start Database Services**

```bash
# Start MySQL and Redis
docker-compose --profile all up -d

# Or start MySQL only
docker-compose --profile without-redis up -d
```

2. **Initialize Database**

```sql
-- Create database
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use database
USE online_store;

-- Execute table creation scripts (located in src/main/resources/sql/ directory)
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

3. **Configure Environment Variables**

```bash
# Set JWT secret (required)
export JWT_SECRET="your-secret-key-here"

# Optional: Configure Nacos
export NACOS_ENABLED=true
```

4. **Run Application**

```bash
# Method 1: Using Maven
mvn spring-boot:run

# Method 2: Package and run
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### Option 2: Local Environment

1. **Ensure MySQL and Redis are Running**

2. **Modify Configuration Files**

Edit `src/main/resources/application-local.yaml`:

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
      password: your_redis_password  # If password is set
```

3. **Execute Database Initialization Scripts** (refer to steps above)

4. **Run Application**

```bash
mvn spring-boot:run
```

5. **Access Application**

Application runs on `http://localhost:8080` by default

## ⚙️ Configuration

### Environment Variables

| Variable | Description | Default | Required |
|--------|------|--------|----------|
| `JWT_SECRET` | JWT signing secret | None | ✅ |
| `SPRING_PROFILES_ACTIVE` | Active profile | `local` | ❌ |
| `NACOS_ENABLED` | Enable Nacos | `false` | ❌ |
| `ADMIN_USERNAME` | Admin username | `admin` | ❌ |
| `ADMIN_PASSWORD` | Admin password | `admin123` | ❌ |

### Configuration Files

- **application.yaml**: Main configuration file, defines common settings
- **application-local.yaml**: Local development environment configuration
- **bootstrap.yaml**: Nacos config center bootstrap configuration

### JVM Parameters

When running the application, add the following JVM parameter to support reflection access:

```bash
--add-opens java.base/java.lang=ALL-UNNAMED
```

## 📚 API Documentation

### Main API Modules

- **Item Management**: `/api/items`
- **Brand Management**: `/api/brands`
- **Category Management**: `/api/categories`
- **Member Management**: `/api/members`
- **Attribute Management**: `/api/attributes`

### Authentication

All API requests require a JWT Token in the header:

```
Authorization: Bearer <your-jwt-token>
```

Or use Basic Auth (username/password) for basic authentication.

## 🗄️ Database Design

The project includes the following core tables:

- **attribute**: Product attributes table
- **attribute_value**: Attribute values table
- **brand**: Brands table
- **category**: Product categories table
- **item**: Product information table
- **sku**: Product SKU table
- **item_attribute_relation**: Product-attribute relation table
- **item_access_log**: Product access log table
- **member**: Member information table

All table creation scripts are located in the `src/main/resources/sql/` directory.

## 🐛 FAQ

### 1. Startup Error: JWT_SECRET not configured

**Solution**: Set environment variable `JWT_SECRET`

```bash
export JWT_SECRET="your-secret-key"
```

### 2. MyBatis Mapper Files Not Found

**Solution**: Check configuration in `application.yaml`:

```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml
```

### 3. Redis Connection Failed

**Solution**: Ensure Redis service is running and check connection configuration

```bash
# Test Redis connection
redis-cli ping
```

## 📄 License

This project is for learning and reference purposes only. 