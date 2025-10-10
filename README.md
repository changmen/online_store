# 🛒 Online Store - 基于Spring Cloud的电商平台

> 一个功能完整的分布式电商系统，采用Spring Cloud微服务架构设计

## 📖 项目介绍

这是一个基于Spring Cloud的在线商店项目，集成了现代化的技术栈，提供完整的电商业务功能。

## 🚀 技术栈

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

## 📋 运行要求

- JDK 17或更高版本
- Maven 3.6或更高版本
- MySQL 8.0
- Redis 6.0或更高版本

## 🏃‍♂️ 快速开始

1. 确保MySQL和Redis服务已启动
2. 创建数据库：
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
3. 修改`application.yml`中的数据库和Redis配置
4. 添加vm参数：`--add-opens java.base/java.lang=ALL-UNNAMED`
5. 运行应用程序：
```bash
mvn spring-boot:run
```

## 📚 API文档

启动应用后，可以通过以下地址访问API文档：
- Swagger UI: http://localhost:8080/swagger-ui.html

## 🤝 贡献指南

欢迎提交Issue和Pull Request来改进这个项目！

## 📄 许可证

本项目采用MIT许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。 