# 🛒 智能在线商城系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2022.0.4-blue.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)

> 基于Spring Cloud微服务架构的现代化电商平台，提供完整的商品管理、用户管理、订单处理等核心功能

## 🚀 核心功能

- 📋 **商品管理**: 商品分类、品牌管理、属性配置
- 👥 **用户系统**: 会员注册、登录认证、权限控制
- 📊 **分布式架构**: 基于Spring Cloud的微服务设计
- 🔒 **安全防护**: JWT令牌认证 + Spring Security
- 📦 **数据存储**: MySQL + Redis缓存优化
- 🔌 **服务治理**: Nacos注册中心与配置中心

## 📚 文档导航

- 📚 [API 接口文档](docs/API.md) - 详细的 API 接口说明和示例
- ⚙️ [环境配置指南](docs/SETUP.md) - 开发环境搭建和配置说明
- 📊 [数据库设计](src/main/resources/sql/init.sql) - 完整的数据库表结构和初始化数据

## 🏢 项目架构

```
src/main/java/com/example/onlinestore/
├── bean/          # 业务对象定义
├── config/        # 配置类（Security、Redis、MyBatis等）
├── controller/    # REST API 控制器
├── dto/           # 数据传输对象（请求/响应）
├── entity/        # 数据库实体类
├── enums/         # 枚举类定义
├── exceptions/    # 自定义异常
├── handler/       # 全局异常处理器
├── mapper/        # MyBatis 数据访问层
├── security/      # 安全配置（JWT、过滤器）
├── service/       # 业务逻辑层
└── utils/         # 工具类
```

### 核心模块

- **用户管理**: 会员注册、登录认证、JWT令牌管理
- **商品管理**: 商品CRUD、分类管理、品牌管理、SKU管理
- **属性管理**: 商品属性定义、属性值管理、关联关系
- **数据统计**: 商品访问日志、用户行为分析

## 🛠️ 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 基础架构 | JDK | 17 |
| 微服务 | Spring Cloud | 2022.0.4 |
| Web框架 | Spring Boot | 3.1.5 |
| ORM框架 | MyBatis | 3.0.2 |
| 数据库 | MySQL | 8.0 |
| 缓存 | Redis | 6.0+ (Jedis 4.3.1) |
| 注册中心 | Nacos | 2.2.0 |
| 安全框架 | Spring Security + JWT | - |

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

## ⚙️ 运行要求

在运行项目之前，请确保你的环境满足以下要求：

- ☀️ **Java**: JDK 17或更高版本
- 📺 **构建工具**: Maven 3.6或更高版本
- 📊 **数据库**: MySQL 8.0
- 🔄 **缓存**: Redis 6.0或更高版本

## 🚀 快速开始

### 1️⃣ 环境准备
确保 MySQL 和 Redis 服务已启动并正常运行。

### 2️⃣ 数据库初始化
创建数据库：
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3️⃣ 配置修改
修改 `application.yml` 中的数据库和 Redis 配置信息。

### 4️⃣ 启动应用
添加 JVM 参数：`--add-opens java.base/java.lang=ALL-UNNAMED`

运行应用程序：
```bash
mvn spring-boot:run
``` 