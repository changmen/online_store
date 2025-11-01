# 🛍️ 在线商城系统 (Online Store)

> 一个功能完善的企业级在线商城平台，基于Spring Cloud微服务架构构建

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2022.0.4-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)

## 📋 项目简介

本项目是一个现代化的电子商务平台，采用微服务架构设计，提供完整的商品管理、用户管理、订单处理等核心功能。系统具有高可用性、可扩展性和高性能的特点，适合中小型企业快速搭建自己的电商平台。

## 🔧 技术栈

- JDK 17
- Spring Cloud 2022.0.4
- Spring Boot 3.1.5
- MyBatis 3.0.2
- MySQL 8.0
- Redis (Jedis 4.3.1)

## ✨ 核心功能

- 📏 **商品管理**: 商品分类、品牌、属性管理
- 👥 **用户系统**: 用户注册、登录、权限管理
- 📋 **订单处理**: 购物车、订单生成、支付流程
- 🔒 **安全认证**: JWT Token、角色权限控制
- 🚀 **高性能**: Redis缓存、数据库优化
- 📊 **监控日志**: 用户行为跟踪、系统监控

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

## 📋 环境要求

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

## 📖 API文档

启动应用后，可通过以下地址访问 API 文档：
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: `http://localhost:8080/v3/api-docs`

## 📝 贡献指南

1. Fork 本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

如果您有任何问题或建议，请随时联系：

- 提交 [Issues](https://github.com/your-username/online-store/issues)
- 发送邮件到: your-email@example.com

---

<div align="center">
  <p>⭐ 如果这个项目对您有帮助，请不要忘记给一个 Star ⭐</p>
  <p>Made with ❤️ by <a href="#">您的名字</a></p>
</div> 