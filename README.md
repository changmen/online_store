# 在线商店 (Online Store)

<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

一个基于 Spring Cloud 微服务架构的现代化在线商店系统

</div>

## 📋 目录

- [项目简介](#项目简介)
- [核心功能](#核心功能)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
- [部署指南](#部署指南)
- [API 文档](#api-文档)
- [环境配置](#环境配置)
- [开发指南](#开发指南)

## 📖 项目简介

这是一个基于 Spring Cloud 微服务架构的在线商店系统，采用前后端分离的设计模式。项目集成了主流的技术栈，包括 Spring Security + JWT 认证、MyBatis-Plus 持久层框架、Redis 缓存、Nacos 服务注册与配置中心等。

## ✨ 核心功能

- 🛍️ **商品管理**：商品分类、品牌管理、商品属性、商品详情
- 👥 **用户管理**：用户注册、登录、JWT 认证授权
- 🔐 **安全认证**：基于 Spring Security + JWT 的安全认证体系
- 📦 **订单管理**：购物车、订单创建、订单查询
- 🎯 **分布式架构**：Nacos 服务注册与发现、配置中心
- 💾 **缓存支持**：Redis 缓存提升系统性能
- ☁️ **对象存储**：阿里云 OSS 文件存储

## 🛠 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 17 | Java 开发工具包 |
| Spring Boot | 3.4.3 | 应用开发框架 |
| Spring Cloud | 2024.0.0 | 微服务框架 |
| Spring Cloud Alibaba | 2022.0.0.0 | 阿里巴巴微服务组件 |
| Spring Security | - | 安全框架 |
| MyBatis | 3.0.3 | ORM 持久层框架 |
| MyBatis Spring Boot Starter | 3.0.2 | MyBatis 集成 |
| PageHelper | 2.1.0 | MyBatis 分页插件 |
| MySQL | 8.2.0 | 关系型数据库 |
| Redis | - | 分布式缓存 |
| Jedis | 5.2.0 | Redis Java 客户端 |
| Nacos | 2.2.0 | 服务注册与配置中心 |
| JWT | 0.11.5 | JSON Web Token |
| Lombok | 1.18.36 | Java 简化工具 |
| Aliyun OSS | 3.18.1 | 阿里云对象存储 |

### 基础设施

- **数据库**：MySQL 8.0+
- **缓存**：Redis 6.0+
- **服务注册中心**：Nacos 2.2.0+
- **构建工具**：Maven 3.6+
- **容器化**：Docker & Docker Compose

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用启动类
│   │   │   ├── bean/                          # JavaBean 配置
│   │   │   ├── config/                        # 配置类（Security、Redis、MyBatis等）
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # REST API 控制器
│   │   │   │   ├── AttributeController.java   # 属性管理
│   │   │   │   ├── BrandController.java       # 品牌管理
│   │   │   │   ├── CategoryController.java    # 分类管理
│   │   │   │   ├── ItemController.java        # 商品管理
│   │   │   │   ├── ItemDetailController.java  # 商品详情
│   │   │   │   └── MemberController.java      # 会员管理
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── exceptions/                    # 自定义异常
│   │   │   ├── handler/                       # 全局异常处理器
│   │   │   ├── mapper/                        # MyBatis Mapper 接口
│   │   │   ├── security/                      # 安全配置（JWT、过滤器等）
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yaml               # 应用配置文件
│   │       ├── bootstrap.yaml                 # Bootstrap 配置
│   │       └── mapper/                        # MyBatis XML 映射文件
│   └── test/                                  # 测试代码
├── scripts/                                   # 脚本文件
├── docker-compose.yaml                        # Docker Compose 配置
├── Dockerfile                                 # Docker 镜像构建文件
├── pom.xml                                    # Maven 项目配置
└── README.md                                  # 项目说明文档
```

## 🚀 快速开始

### 环境要求

在开始之前，请确保你的开发环境满足以下要求：

- ☕ JDK 17 或更高版本
- 📦 Maven 3.6 或更高版本
- 🗄️ MySQL 8.0 或更高版本
- 🔴 Redis 6.0 或更高版本
- 🌐 Nacos 2.2.0 或更高版本（可选，默认关闭）

### 方式一：本地运行

#### 1. 克隆项目

```bash
git clone <repository-url>
cd online-store
```

#### 2. 启动基础服务

**启动 MySQL 和 Redis（使用 Docker Compose）：**

```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 或者只启动 MySQL
docker-compose --profile without-redis up -d
```

**或手动启动服务：**

确保 MySQL 和 Redis 服务已在本地运行。

#### 3. 创建数据库

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 4. 配置应用

编辑 `src/main/resources/application.yaml` 文件，修改以下配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456  # 修改为你的 MySQL 密码
  data:
    redis:
      host: localhost
      port: 6379
      password:         # 如果有密码请填写
```

#### 5. 设置环境变量

```bash
# JWT 密钥（必需）
export JWT_SECRET="your-secret-key-here"

# 管理员账号（可选，默认 admin/admin123）
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="admin123"
```

#### 6. 编译并运行

```bash
# 清理并编译项目
mvn clean install

# 运行应用（添加必要的 JVM 参数）
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"

# 或者使用打包后的 jar 运行
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

#### 7. 验证服务

访问以下地址验证服务是否正常运行：

- 应用地址：http://localhost:8080
- 健康检查：http://localhost:8080/actuator/health

### 方式二：Docker 部署

```bash
# 构建镜像
docker build -t online-store:latest .

# 启动所有服务
docker-compose --profile all up -d
```

## 🌍 部署指南

### 生产环境配置

在生产环境中，建议通过环境变量或 Nacos 配置中心管理配置：

```bash
# 启用 Nacos
export NACOS_ENABLED=true
export SPRING_PROFILES_ACTIVE=prod

# 数据库配置
export DB_HOST=your-db-host
export DB_PORT=3306
export DB_NAME=online_store
export DB_USERNAME=your-username
export DB_PASSWORD=your-password

# Redis 配置
export REDIS_HOST=your-redis-host
export REDIS_PORT=6379
export REDIS_PASSWORD=your-redis-password

# JWT 配置
export JWT_SECRET=your-production-secret-key
export JWT_EXPIRATION=86400
```

## 📚 API 文档

### 主要 API 端点

#### 用户管理
- `POST /api/members/register` - 用户注册
- `POST /api/members/login` - 用户登录
- `GET /api/members/profile` - 获取用户信息

#### 商品管理
- `GET /api/categories` - 获取商品分类
- `GET /api/brands` - 获取品牌列表
- `GET /api/items` - 获取商品列表
- `GET /api/items/{id}` - 获取商品详情
- `GET /api/items/{id}/details` - 获取商品详细信息

#### 属性管理
- `GET /api/attributes` - 获取属性列表
- `POST /api/attributes` - 创建属性
- `PUT /api/attributes/{id}` - 更新属性
- `DELETE /api/attributes/{id}` - 删除属性

> 💡 详细的 API 文档请参考 Swagger UI（待集成）或 Postman Collection

## ⚙️ 环境配置

### 配置文件说明

项目支持多环境配置：

- `application.yaml` - 主配置文件
- `application-local.yaml` - 本地开发环境
- `application-dev.yaml` - 开发环境
- `application-prod.yaml` - 生产环境

### 重要配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `server.port` | 服务端口 | 8080 |
| `spring.profiles.active` | 激活的配置文件 | local |
| `spring.cloud.nacos.discovery.enabled` | 是否启用 Nacos | false |
| `jwt.secret` | JWT 密钥 | 环境变量 |
| `jwt.expiration` | JWT 过期时间（秒） | 86400 |

## 👨‍💻 开发指南

### 代码规范

- 遵循阿里巴巴 Java 开发手册
- 使用 Lombok 简化代码
- 统一使用 RESTful API 设计规范
- 所有 API 返回统一的响应格式

### 分支管理

- `main` - 主分支，保持稳定
- `develop` - 开发分支
- `feature/*` - 功能分支
- `hotfix/*` - 热修复分支

### 提交规范

```
feat: 新功能
fix: 修复问题
docs: 文档更新
style: 代码格式
refactor: 重构
test: 测试相关
chore: 构建/工具链
```

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 Issue
- 发送邮件
- 加入讨论组

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给它一个星标！**

</div> 