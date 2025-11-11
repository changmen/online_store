# Online Store

[English](#english) | [中文](#chinese)

---

## <a id="english">English</a>

### Overview

A modern online store application built with Spring Cloud microservices architecture, providing a comprehensive e-commerce solution with product management, order processing, user management, and more.

### Tech Stack

**Backend Framework:**
- Java 17
- Spring Boot 3.4.3
- Spring Cloud 2024.0.0
- Spring Cloud Alibaba 2022.0.0.0

**Data & Cache:**
- MyBatis 3.0.2 (ORM)
- MySQL 8.2.0 (Database)
- Redis 5.2.0 with Jedis client (Cache)

**Service Discovery & Configuration:**
- Nacos 2.2.0 (Service Discovery & Config Center)

**Additional Libraries:**
- Spring AOP (Aspect-Oriented Programming)
- Spring Validation (Bean Validation)
- Apache Commons Lang3 3.17.0
- Apache Commons Collections 3.2.2
- Aliyun OSS SDK 3.15.1 (Object Storage)
- Apache HttpClient 4.5.14
- CGLIB 3.3.0 (Dynamic Proxy)

### Project Structure

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # Application entry point
│   │   │   ├── actuator/                       # Custom actuator endpoints
│   │   │   ├── annotation/                     # Custom annotations
│   │   │   ├── aspect/                         # AOP aspects
│   │   │   ├── bean/                           # Bean definitions
│   │   │   ├── cache/                          # Cache management
│   │   │   ├── config/                         # Configuration classes
│   │   │   ├── constants/                      # Constants
│   │   │   ├── context/                        # Application context utilities
│   │   │   ├── controller/                     # REST controllers
│   │   │   ├── dto/                            # Data Transfer Objects
│   │   │   ├── entity/                         # JPA entities
│   │   │   ├── enums/                          # Enum types
│   │   │   ├── exception/                      # Custom exceptions
│   │   │   ├── handler/                        # Exception handlers
│   │   │   ├── hook/                           # Application hooks
│   │   │   ├── interceptor/                    # Request interceptors
│   │   │   ├── mapper/                         # MyBatis mappers
│   │   │   ├── service/                        # Business logic
│   │   │   ├── util/                           # Utility classes
│   │   │   └── validator/                      # Custom validators
│   │   └── resources/
│   │       ├── application.yml                 # Application configuration
│   │       ├── bootstrap.yml                   # Bootstrap configuration
│   │       └── mapper/                         # MyBatis XML mappers
│   └── test/                                   # Test cases
├── scripts/                                    # Utility scripts
├── docker-compose.yaml                         # Docker Compose config
├── pom.xml                                     # Maven configuration
└── README.md                                   # This file
```

### Prerequisites

- **JDK:** 17 or higher
- **Maven:** 3.6 or higher
- **MySQL:** 8.0 or higher
- **Redis:** 6.0 or higher
- **Nacos:** 2.2.0 or higher (for service discovery and configuration)

### Quick Start

#### Option 1: Using Docker Compose (Recommended)

1. **Start MySQL and Redis:**
   ```bash
   # Start all services
   docker-compose --profile all up -d
   
   # Or start MySQL only
   docker-compose --profile without-redis up -d
   ```

2. **Create database:**
   ```sql
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Configure Nacos:**
   - Download and start Nacos server (2.2.0)
   - Access Nacos console at `http://localhost:8848/nacos`
   - Create configuration for the application

4. **Run the application:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
   ```

#### Option 2: Manual Setup

1. **Install and start services:**
   - Install MySQL 8.0+ and Redis 6.0+
   - Install and start Nacos 2.2.0+

2. **Create database:**
   ```sql
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Configure application:**
   - Update `src/main/resources/application.yml` with your MySQL and Redis settings
   - Update `src/main/resources/bootstrap.yml` with your Nacos server address

4. **Build and run:**
   ```bash
   # Build the project
   mvn clean package
   
   # Run the application
   java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
   ```

### Configuration

#### Database Configuration
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

#### Redis Configuration
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
```

#### Nacos Configuration
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
```

### API Documentation

Once the application is running, you can access:
- **Application:** `http://localhost:8080`
- **Actuator Health Check:** `http://localhost:8080/actuator/health`
- **Actuator Endpoints:** `http://localhost:8080/actuator`

### Development

#### Running Tests
```bash
mvn test
```

#### Code Style
The project follows standard Java coding conventions and Spring Boot best practices.

### Troubleshooting

**Common Issues:**

1. **Reflection warnings:** Add JVM argument `--add-opens java.base/java.lang=ALL-UNNAMED`
2. **Database connection failed:** Check MySQL is running and credentials are correct
3. **Redis connection failed:** Ensure Redis server is running on port 6379
4. **Nacos connection failed:** Verify Nacos server is running on port 8848

### License

This project is licensed under the MIT License.

---

## <a id="chinese">中文</a>

### 项目简介

基于 Spring Cloud 微服务架构构建的现代化在线商店应用，提供完整的电商解决方案，包括商品管理、订单处理、用户管理等功能。

### 技术栈

**后端框架：**
- Java 17
- Spring Boot 3.4.3
- Spring Cloud 2024.0.0
- Spring Cloud Alibaba 2022.0.0.0

**数据与缓存：**
- MyBatis 3.0.2（ORM 框架）
- MySQL 8.2.0（数据库）
- Redis 5.2.0 with Jedis 客户端（缓存）

**服务发现与配置：**
- Nacos 2.2.0（服务发现与配置中心）

**其他依赖：**
- Spring AOP（面向切面编程）
- Spring Validation（Bean 校验）
- Apache Commons Lang3 3.17.0
- Apache Commons Collections 3.2.2
- 阿里云 OSS SDK 3.15.1（对象存储）
- Apache HttpClient 4.5.14
- CGLIB 3.3.0（动态代理）

### 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用程序入口
│   │   │   ├── actuator/                       # 自定义监控端点
│   │   │   ├── annotation/                     # 自定义注解
│   │   │   ├── aspect/                         # AOP 切面
│   │   │   ├── bean/                           # Bean 定义
│   │   │   ├── cache/                          # 缓存管理
│   │   │   ├── config/                         # 配置类
│   │   │   ├── constants/                      # 常量定义
│   │   │   ├── context/                        # 应用上下文工具
│   │   │   ├── controller/                     # REST 控制器
│   │   │   ├── dto/                            # 数据传输对象
│   │   │   ├── entity/                         # 实体类
│   │   │   ├── enums/                          # 枚举类型
│   │   │   ├── exception/                      # 自定义异常
│   │   │   ├── handler/                        # 异常处理器
│   │   │   ├── hook/                           # 应用钩子
│   │   │   ├── interceptor/                    # 请求拦截器
│   │   │   ├── mapper/                         # MyBatis 映射器
│   │   │   ├── service/                        # 业务逻辑
│   │   │   ├── util/                           # 工具类
│   │   │   └── validator/                      # 自定义校验器
│   │   └── resources/
│   │       ├── application.yml                 # 应用配置
│   │       ├── bootstrap.yml                   # 启动配置
│   │       └── mapper/                         # MyBatis XML 映射文件
│   └── test/                                   # 测试用例
├── scripts/                                    # 工具脚本
├── docker-compose.yaml                         # Docker Compose 配置
├── pom.xml                                     # Maven 配置
└── README.md                                   # 本文件
```

### 运行要求

- **JDK：** 17 或更高版本
- **Maven：** 3.6 或更高版本
- **MySQL：** 8.0 或更高版本
- **Redis：** 6.0 或更高版本
- **Nacos：** 2.2.0 或更高版本（用于服务发现和配置管理）

### 快速开始

#### 方式一：使用 Docker Compose（推荐）

1. **启动 MySQL 和 Redis：**
   ```bash
   # 启动所有服务
   docker-compose --profile all up -d
   
   # 或仅启动 MySQL
   docker-compose --profile without-redis up -d
   ```

2. **创建数据库：**
   ```sql
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **配置 Nacos：**
   - 下载并启动 Nacos 服务器（2.2.0）
   - 访问 Nacos 控制台 `http://localhost:8848/nacos`
   - 为应用程序创建配置

4. **运行应用程序：**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
   ```

#### 方式二：手动配置

1. **安装并启动服务：**
   - 安装 MySQL 8.0+ 和 Redis 6.0+
   - 安装并启动 Nacos 2.2.0+

2. **创建数据库：**
   ```sql
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **配置应用程序：**
   - 修改 `src/main/resources/application.yml` 中的 MySQL 和 Redis 配置
   - 修改 `src/main/resources/bootstrap.yml` 中的 Nacos 服务器地址

4. **构建并运行：**
   ```bash
   # 构建项目
   mvn clean package
   
   # 运行应用程序
   java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
   ```

### 配置说明

#### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: 你的用户名
    password: 你的密码
```

#### Redis 配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
```

#### Nacos 配置
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
```

### API 文档

应用程序运行后，可以访问：
- **应用程序：** `http://localhost:8080`
- **健康检查：** `http://localhost:8080/actuator/health`
- **监控端点：** `http://localhost:8080/actuator`

### 开发指南

#### 运行测试
```bash
mvn test
```

#### 代码规范
项目遵循标准 Java 编码规范和 Spring Boot 最佳实践。

### 故障排除

**常见问题：**

1. **反射警告：** 添加 JVM 参数 `--add-opens java.base/java.lang=ALL-UNNAMED`
2. **数据库连接失败：** 检查 MySQL 是否运行以及凭据是否正确
3. **Redis 连接失败：** 确保 Redis 服务器在 6379 端口运行
4. **Nacos 连接失败：** 验证 Nacos 服务器在 8848 端口运行

### 许可证

本项目采用 MIT 许可证。 