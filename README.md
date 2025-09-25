# 🛒 云端商城 (CloudMart)

### 基于 Spring Cloud 微服务架构的现代化电商平台

一个功能完整、可扩展的企业级在线商城系统，采用微服务架构设计，支持商品管理、库存管理、会员系统、订单处理等核心电商业务功能。

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)

## ✨ 核心特性

- 🔧 **微服务架构**: 基于 Spring Cloud 的分布式架构设计
- 🛡️ **安全认证**: JWT + Spring Security 安全框架
- 📦 **商品管理**: 多规格商品、品牌分类、属性管理
- 👥 **会员系统**: 用户注册、登录、权限管理
- 📊 **数据持久化**: MyBatis + MySQL 数据存储
- ⚡ **缓存优化**: Redis 缓存提升性能
- 🐳 **容器化部署**: Docker + Docker Compose 支持
- 📈 **监控运维**: Spring Boot Actuator 健康检查

---

## 🛠️ 技术架构

- JDK 17
- Spring Cloud 2022.0.4
- Spring Boot 3.1.5
- MyBatis 3.0.2
- MySQL 8.0
- Redis (Jedis 4.3.1)

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

## 📝 环境要求

- JDK 17或更高版本
- Maven 3.6或更高版本
- MySQL 8.0
- Redis 6.0或更高版本

## 🚀 快速开始

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