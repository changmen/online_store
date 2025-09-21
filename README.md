# 🛒 Online Store - 现代化在线购物平台

> 基于 Spring Cloud 微服务架构的企业级电商解决方案

这是一个功能完整的在线商店项目，采用现代化的微服务架构设计，提供稳定、高效、可扩展的电商服务。

## ✨ 特性亮点

- 🏗️ **微服务架构**: 基于 Spring Cloud 的分布式系统设计
- 🔐 **安全认证**: JWT Token 认证机制
- 📊 **数据管理**: MyBatis + MySQL 高性能数据持久化
- ⚡ **缓存加速**: Redis 缓存提升响应速度
- 🛒 **商品管理**: 完整的商品、分类、品牌管理体系
- 👥 **用户系统**: 会员注册、登录、信息管理
- 🔍 **搜索功能**: 多维度商品搜索与筛选
- 📱 **现代化 API**: RESTful API 设计

## 🛠️ 技术栈

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

## ⚙️ 运行要求

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

## 📝 许可证

本项目采用 MIT 许可证。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 来帮助改进项目！

---

**❤️ 用心打造** | 如果认为项目有用，请给一个 ⭐ Star！