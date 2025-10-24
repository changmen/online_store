# Online Store

一个基于 Spring Cloud 微服务架构的现代化在线商店系统，集成了 Nacos 服务发现、Spring Security + JWT 认证、阿里云 OSS 存储等企业级功能。

## 📋 目录

- [技术栈](#技术栈)
- [主要特性](#主要特性)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [Docker 部署](#docker-部署)
- [开发指南](#开发指南)

## 🚀 技术栈

### 核心框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0

### 数据访问
- **MyBatis**: 3.0.3
- **MyBatis Spring Boot Starter**: 3.0.2
- **PageHelper**: 2.1.0 (分页插件)
- **MySQL Connector**: 8.2.0

### 缓存与存储
- **Redis**: Spring Data Redis + Jedis 5.2.0
- **Aliyun OSS**: 3.18.1

### 服务治理
- **Nacos**: 2.2.0 (服务发现与配置中心)

### 安全认证
- **Spring Security**: Spring Boot Starter Security
- **JWT**: JJWT 0.11.5

### 工具库
- **Lombok**: 1.18.36
- **Apache Commons Lang3**: 3.17.0
- **Commons Collections**: 3.2.2
- **CGLIB**: 3.3.0

## ✨ 主要特性

- ✅ **微服务架构**: 基于 Spring Cloud 的分布式系统架构
- ✅ **服务注册与发现**: 集成 Nacos 实现服务自动注册和发现
- ✅ **配置中心**: 使用 Nacos Config 实现配置集中管理和动态刷新
- ✅ **安全认证**: Spring Security + JWT 实现无状态认证
- ✅ **持久化**: MyBatis + MySQL 数据持久化方案
- ✅ **缓存**: Redis 缓存提升系统性能
- ✅ **对象存储**: 集成阿里云 OSS 处理文件上传
- ✅ **分页查询**: PageHelper 插件支持
- ✅ **参数校验**: Spring Validation 统一参数验证
- ✅ **AOP 支持**: 面向切面编程支持
- ✅ **监控**: Spring Boot Actuator 健康检查

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用启动类
│   │   │   ├── bean/                          # Bean 配置
│   │   │   ├── config/                        # 配置类
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # 控制器层
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── errors/                        # 错误处理
│   │   │   ├── exceptions/                    # 自定义异常
│   │   │   ├── handler/                       # 处理器
│   │   │   ├── mapper/                        # MyBatis Mapper
│   │   │   ├── security/                      # 安全配置
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yml                # 应用配置
│   │       ├── bootstrap.yml                  # Nacos 配置
│   │       └── mapper/                        # MyBatis XML
│   └── test/                                  # 测试代码
├── scripts/                                   # 脚本工具
├── docker-compose.yaml                        # Docker Compose 配置
├── Dockerfile                                 # Docker 镜像构建
├── pom.xml                                    # Maven 配置
└── README.md                                  # 项目说明
```

## 💻 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6+ 
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Nacos Server**: 2.2.0+ (可选，用于服务发现和配置管理)

## 🏃 快速开始

### 方式一：本地运行

#### 1. 准备数据库

启动 MySQL 服务并创建数据库：

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 2. 启动 Redis

```bash
redis-server
```

#### 3. 配置应用

修改 `src/main/resources/application.yml` 中的配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useSSL=false&serverTimezone=UTC
    username: your_username
    password: your_password
  
  redis:
    host: localhost
    port: 6379
```

#### 4. 启动应用

使用 Maven 运行：

```bash
mvn clean install
mvn spring-boot:run
```

或使用 Java 命令（需要添加 JVM 参数）：

```bash
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 方式二：使用 Docker Compose

#### 1. 启动所有服务（MySQL + Redis）

```bash
docker-compose --profile all up -d
```

#### 2. 仅启动 MySQL

```bash
docker-compose --profile without-redis up -d
```

#### 3. 查看服务状态

```bash
docker-compose ps
```

## ⚙️ 配置说明

### Nacos 配置

如果使用 Nacos，需要在 `bootstrap.yml` 中配置：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
        file-extension: yaml
```

### JWT 配置

在 Nacos 或 `application.yml` 中配置 JWT 密钥：

```yaml
jwt:
  secret: your-secret-key
  expiration: 86400000  # 24小时
```

### 阿里云 OSS 配置

```yaml
aliyun:
  oss:
    endpoint: your-endpoint
    accessKeyId: your-access-key
    accessKeySecret: your-secret-key
    bucketName: your-bucket
```

## 🐳 Docker 部署

### 构建镜像

```bash
docker build -t online-store:latest .
```

### 运行容器

```bash
docker run -d -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host:3306/online_store \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  online-store:latest
```

## 📖 开发指南

### 代码规范

- 使用 Lombok 简化代码
- 遵循 RESTful API 设计规范
- 统一异常处理
- 使用 DTO 进行数据传输

### API 测试

应用启动后，可以通过以下方式测试：

```bash
curl http://localhost:8080/actuator/health
```

### JVM 参数说明

由于使用了反射和某些高级特性，运行时需要添加以下 JVM 参数：

```
--add-opens java.base/java.lang=ALL-UNNAMED
```

这个参数允许应用访问 Java 内部 API，通常用于 CGLIB 和某些框架的反射操作。

## 📝 License

This project is licensed under the MIT License.

## 🤝 Contributing

Contributions, issues and feature requests are welcome!

---

**Happy Coding! 🎉** 