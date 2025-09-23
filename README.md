# 在线商店系统 (Online Store)

<div align="center">

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-brightgreen.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

</div>

## 📋 项目简介

这是一个基于 Spring Boot 3.x 和 Spring Cloud 2024.x 构建的现代化电商系统后端服务。该系统采用微服务架构设计，提供完整的商品管理、用户管理、订单处理等核心电商功能。

### ✨ 主要特性

- 🏗️ **现代化架构**: 采用 Spring Boot 3.x + Spring Cloud 微服务架构
- 🔐 **安全认证**: 集成 Spring Security + JWT 实现用户认证和授权
- 📊 **数据持久化**: 使用 MyBatis + MySQL 进行数据管理
- ⚡ **缓存支持**: Redis 缓存提升系统性能
- 🌐 **服务发现**: 支持 Nacos 注册中心和配置中心
- 📁 **文件存储**: 集成阿里云 OSS 对象存储服务
- 🌍 **国际化**: 支持多语言 (中文/英文)
- 📖 **API 文档**: RESTful API 设计，易于集成
- 🐳 **容器化**: 提供 Docker 容器化部署支持

## 🛠️ 技术栈

### 核心框架
- **Java**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: 用户认证和授权
- **Spring Cloud Alibaba**: 2022.0.0.0

### 数据层
- **MySQL**: 8.2.0 (主数据库)
- **Redis**: 6.0+ (缓存)
- **MyBatis**: 3.0.3 (ORM 框架)
- **PageHelper**: 2.1.0 (分页插件)

### 服务治理
- **Nacos**: 2.2.0 (服务注册与配置中心)
- **Jedis**: 5.2.0 (Redis 客户端)

### 工具库
- **JWT**: 0.11.5 (令牌认证)
- **Lombok**: 1.18.36 (代码简化)
- **Apache Commons**: 工具类库
- **Jackson**: JSON 序列化
- **Aliyun OSS**: 3.18.1 (文件存储)

## 🏗️ 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java      # 启动类
│   ├── bean/                           # 数据传输对象
│   │   ├── Item.java                   # 商品实体
│   │   ├── Member.java                 # 用户实体
│   │   ├── Category.java               # 分类实体
│   │   └── ...
│   ├── controller/                     # 控制器层
│   │   ├── ItemController.java         # 商品控制器
│   │   ├── MemberController.java       # 用户控制器
│   │   ├── CategoryController.java     # 分类控制器
│   │   └── ...
│   ├── service/                        # 业务逻辑层
│   │   ├── ItemService.java
│   │   ├── MemberService.java
│   │   └── ...
│   ├── mapper/                         # 数据访问层
│   │   ├── ItemMapper.java
│   │   ├── MemberMapper.java
│   │   └── ...
│   ├── entity/                         # 数据库实体
│   ├── dto/                           # 数据传输对象
│   ├── config/                        # 配置类
│   ├── security/                      # 安全配置
│   ├── utils/                         # 工具类
│   └── exceptions/                    # 异常处理
├── src/main/resources/
│   ├── mapper/                        # MyBatis 映射文件
│   ├── sql/                           # 数据库脚本
│   ├── i18n/                          # 国际化资源
│   ├── application.yaml               # 主配置文件
│   ├── application-local.yaml         # 本地配置
│   └── bootstrap.yaml                 # 引导配置
├── scripts/                           # 脚本工具
├── docker-compose.yaml                # Docker 编排文件
├── Dockerfile                         # Docker 镜像构建
└── pom.xml                           # Maven 项目配置
```

## 🚀 核心功能模块

### 👤 用户管理 (Member Management)
- 用户注册和登录
- JWT 身份认证
- 用户信息管理

### 🛍️ 商品管理 (Item Management)
- 商品信息 CRUD
- 商品分类管理
- 商品属性管理
- SKU 管理
- 品牌管理

### 📊 系统管理
- 访问日志记录
- 系统监控 (Actuator)
- 国际化支持

## 📚 API 文档

### 用户相关 API

#### 用户注册
```http
POST /api/v1/members/registry
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com",
  "phone": "13800138000"
}
```

#### 用户登录
```http
POST /api/v1/members/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

### 商品相关 API

#### 获取商品详情
```http
GET /api/v1/items/{itemId}
Authorization: Bearer {jwt_token}
```

#### 获取商品列表
```http
GET /api/v1/item-details/list
Authorization: Bearer {jwt_token}
```

### 分类管理 API

#### 获取分类列表
```http
GET /api/v1/categories
```

## 🔧 环境要求

### 开发环境
- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **IDE**: IntelliJ IDEA 或 Eclipse

### 生产环境
- **服务器**: Linux (推荐 Ubuntu 20.04+)
- **内存**: 至少 2GB RAM
- **存储**: 至少 10GB 可用空间
- **网络**: 支持外网访问

## ⚡ 快速开始

### 1. 环境准备

#### 使用 Docker Compose (推荐)
```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d
```

#### 手动安装依赖
- 安装并启动 MySQL 8.0+
- 安装并启动 Redis 6.0+

### 2. 数据库初始化

```bash
# 连接到 MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 使用数据库
USE online_store;

# 执行建表脚本 (按顺序执行 src/main/resources/sql/ 目录下的脚本)
source src/main/resources/sql/member_table.sql;
source src/main/resources/sql/category_table.sql;
source src/main/resources/sql/brand_table.sql;
source src/main/resources/sql/item_table_table.sql;
source src/main/resources/sql/sku_table.sql;
source src/main/resources/sql/attribute_table.sql;
source src/main/resources/sql/attribute_value_table.sql;
source src/main/resources/sql/item_attribute_relation_table.sql;
source src/main/resources/sql/item_access_log_table.sql;
```

### 3. 配置文件设置

创建 `src/main/resources/application-local.yaml` 并配置数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_mysql_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password  # 如果有密码
      database: 0

jwt:
  secret: your_jwt_secret_key_here_at_least_32_characters_long
```

### 4. 运行应用

```bash
# 使用 Maven 运行
mvn clean spring-boot:run

# 或者编译后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 5. 验证安装

应用启动后，访问以下地址验证：

- **健康检查**: http://localhost:8080/actuator/health
- **应用信息**: http://localhost:8080/actuator/info

## 🐳 Docker 部署

### 构建镜像
```bash
# 构建应用镜像
docker build -t online-store:latest .
```

### 使用 Docker Compose 部署
```bash
# 启动所有服务
docker-compose --profile all up -d

# 查看运行状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

## 🔒 安全配置

### JWT 配置
- 在生产环境中，请确保设置强密码的 `JWT_SECRET` 环境变量
- JWT 令牌默认有效期为 24 小时

### 数据库安全
- 使用强密码
- 限制数据库访问 IP
- 定期备份数据

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 生成测试报告
mvn surefire-report:report
```

## 📊 监控和日志

### 应用监控
- **健康检查**: `/actuator/health`
- **指标监控**: `/actuator/metrics`
- **环境信息**: `/actuator/env`

### 日志配置
应用使用 Logback 进行日志管理，日志级别可通过环境变量 `LOGGING_LEVEL` 设置。

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证。详细信息请查看 [LICENSE](LICENSE) 文件。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 [Issue](../../issues)
- 发送邮件至项目维护者

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者们！

---

⭐ 如果这个项目对你有帮助，请给我们一个 Star！