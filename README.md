# 🛒 Online Store - 基于Spring Cloud的微服务电商平台

这是一个功能完整的在线商店项目，采用Spring Cloud微服务架构构建，提供现代化的电商解决方案。

## ✨ 主要特性

- 🏗️ **微服务架构** - 采用Spring Cloud构建的分布式系统
- 🔐 **安全认证** - 完整的用户认证和授权体系
- 💾 **数据持久化** - 基于MyBatis和MySQL的数据层
- ⚡ **缓存支持** - Redis缓存提升系统性能
- 🎯 **RESTful API** - 标准化的API接口设计
- 🚀 **高可用性** - 支持集群部署和负载均衡

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