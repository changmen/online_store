# 🛒 在线商店系统 (Online Store)

> 基于 Spring Cloud 微服务架构的现代化电商平台

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2022.0.4-blue.svg)](https://spring.io/projects/spring-cloud)
[![MyBatis](https://img.shields.io/badge/MyBatis-3.0.2-red.svg)](https://mybatis.org/)

## 📋 目录

- [🔧 技术栈](#技术栈)
- [📁 项目结构](#项目结构)
- [⚡ 运行要求](#运行要求)
- [🚀 快速开始](#快速开始)

## 🔧 技术栈

### 核心框架
- **JDK 17** - Java开发环境
- **Spring Cloud 2022.0.4** - 微服务框架
- **Spring Boot 3.1.5** - 应用框架
- **MyBatis 3.0.2** - 持久层框架

### 数据存储
- **MySQL 8.0** - 关系型数据库
- **Redis (Jedis 4.3.1)** - 缓存数据库

## 📁 项目结构

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

## ⚡ 运行要求

- JDK 17或更高版本
- Maven 3.6或更高版本
- MySQL 8.0
- Redis 6.0或更高版本

## 🚀 快速开始

### 1. 环境准备
确保以下服务已启动：
- MySQL 8.0
- Redis 6.0+

### 2. 数据库初始化
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 配置修改
编辑 `src/main/resources/application.yaml` 文件，配置数据库和Redis连接信息。

### 4. 启动应用
```bash
# 添加JVM参数并启动
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

### 5. 验证部署
访问 `http://localhost:8080` 确认应用正常运行。 