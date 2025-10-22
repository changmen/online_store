# Online Store - 在线商店系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![JDK](https://img.shields.io/badge/JDK-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

这是一个基于 Spring Cloud 微服务架构的现代化在线商店系统，提供完整的电商基础功能，包括商品管理、品牌管理、分类管理、会员管理等核心模块。

## 📑 目录

- [核心功能](#核心功能)
- [技术栈](#技术栈)
- [项目架构](#项目架构)
- [快速开始](#快速开始)
  - [环境要求](#环境要求)
  - [使用 Docker Compose（推荐）](#使用-docker-compose推荐)
  - [本地部署](#本地部署)
- [配置说明](#配置说明)
- [API 文档](#api-文档)
- [数据库设计](#数据库设计)
- [开发指南](#开发指南)
- [常见问题](#常见问题)

## 🚀 核心功能

### 商品管理
- ✅ 商品的增删改查
- ✅ 商品详情管理（SKU、属性等）
- ✅ 商品访问日志记录
- ✅ 商品分页查询与搜索

### 分类与品牌
- ✅ 多级商品分类管理
- ✅ 品牌管理（创建、编辑、删除）
- ✅ 品牌与分类关联

### 会员系统
- ✅ 会员注册与登录
- ✅ JWT 令牌认证
- ✅ Spring Security 安全防护
- ✅ 会员信息管理

### 属性系统
- ✅ 商品属性定义
- ✅ 属性值管理
- ✅ 商品与属性关联

## 🛠 技术栈

### 后端框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0
- **Spring Security**: 集成于 Spring Boot 3.4.3

### 数据持久化
- **MyBatis**: 3.0.3
- **MyBatis Spring Boot Starter**: 3.0.2
- **PageHelper**: 2.1.0（分页插件）
- **MySQL**: 8.2.0+

### 缓存与存储
- **Redis**: 5.2.0（Jedis 客户端）
- **Spring Data Redis**: 集成于 Spring Boot
- **Aliyun OSS**: 3.18.1（对象存储）

### 服务注册与配置
- **Nacos**: 2.2.0（服务注册与配置中心）

### 工具库
- **Lombok**: 1.18.36（简化代码）
- **JWT**: 0.11.5（JSON Web Token）
- **Apache Commons Lang3**: 3.17.0
- **Apache Commons Collections**: 3.2.2
- **CGLib**: 3.3.0（动态代理）

### 构建工具
- **Maven**: 3.6+
- **Maven Compiler Plugin**: 3.14.0

## 🏗 项目架构

```
online_store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用启动类
│   │   │   ├── bean/                          # Bean 配置
│   │   │   ├── config/                        # 配置类（Redis、Security 等）
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # REST API 控制器
│   │   │   │   ├── AttributeController.java   # 属性管理接口
│   │   │   │   ├── BrandController.java       # 品牌管理接口
│   │   │   │   ├── CategoryController.java    # 分类管理接口
│   │   │   │   ├── ItemController.java        # 商品管理接口
│   │   │   │   ├── ItemDetailController.java  # 商品详情接口
│   │   │   │   └── MemberController.java      # 会员管理接口
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── enums/                         # 枚举类型
│   │   │   ├── exceptions/                    # 自定义异常
│   │   │   ├── handler/                       # 全局异常处理器
│   │   │   ├── mapper/                        # MyBatis Mapper 接口
│   │   │   ├── security/                      # 安全配置
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yaml               # 主配置文件
│   │       ├── mapper/                        # MyBatis XML 映射文件
│   │       └── sql/                           # 数据库建表脚本
│   │           ├── attribute_table.sql
│   │           ├── attribute_value_table.sql
│   │           ├── brand_table.sql
│   │           ├── category_table.sql
│   │           ├── item_access_log_table.sql
│   │           ├── item_attribute_relation_table.sql
│   │           ├── item_table_table.sql
│   │           ├── member_table.sql
│   │           └── sku_table.sql
│   └── test/                                  # 单元测试
├── scripts/                                   # 脚本文件
├── docker-compose.yaml                        # Docker Compose 配置
├── Dockerfile                                 # Docker 镜像构建文件
├── pom.xml                                    # Maven 项目配置
└── README.md                                  # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **Docker** (可选): 用于容器化部署

### 使用 Docker Compose（推荐）

使用 Docker Compose 可以快速启动 MySQL 和 Redis 服务：

```bash
# 启动所有服务（MySQL + Redis）
docker-compose --profile all up -d

# 或者只启动 MySQL
docker-compose --profile without-redis up -d

# 停止服务
docker-compose down
```

**注意**：请确保在运行前修改 `docker-compose.yaml` 中的卷映射路径。

### 本地部署

#### 1. 克隆项目

```bash
git clone <repository-url>
cd online_store
```

#### 2. 启动依赖服务

**启动 MySQL：**

```bash
# 使用 Docker
docker run -d \
  --name mysql-online-store \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -e TZ=Asia/Shanghai \
  mysql:latest
```

**启动 Redis：**

```bash
# 使用 Docker
docker run -d \
  --name redis-online-store \
  -p 6379:6379 \
  redis:latest
```

#### 3. 初始化数据库

```bash
# 连接到 MySQL
mysql -h localhost -u root -p123456

# 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 使用数据库
USE online_store;

# 执行建表脚本（按顺序执行 src/main/resources/sql/ 目录下的所有 SQL 文件）
source src/main/resources/sql/member_table.sql;
source src/main/resources/sql/category_table.sql;
source src/main/resources/sql/brand_table.sql;
source src/main/resources/sql/attribute_table.sql;
source src/main/resources/sql/attribute_value_table.sql;
source src/main/resources/sql/item_table_table.sql;
source src/main/resources/sql/sku_table.sql;
source src/main/resources/sql/item_attribute_relation_table.sql;
source src/main/resources/sql/item_access_log_table.sql;
```

#### 4. 配置应用

编辑 `src/main/resources/application.yaml`，根据实际环境修改配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
  data:
    redis:
      host: localhost
      port: 6379
      password: # 如果 Redis 设置了密码，请在此填写

jwt:
  secret: your-secret-key-here  # 请设置一个强密钥
  expiration: 86400
```

**环境变量配置（可选）：**

```bash
export JWT_SECRET=your-secret-key-here
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
export SPRING_PROFILES_ACTIVE=local
```

#### 5. 编译项目

```bash
mvn clean install -DskipTests
```

#### 6. 启动应用

```bash
# 方式一：使用 Maven
mvn spring-boot:run

# 方式二：使用 Java 命令（需要添加 JVM 参数）
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

#### 7. 验证服务

应用启动后，访问：

```bash
# 健康检查
curl http://localhost:8080/actuator/health

# API 测试（需要认证）
curl -u admin:admin123 http://localhost:8080/api/categories
```

## ⚙️ 配置说明

### 核心配置项

| 配置项 | 说明 | 默认值 |
|-------|------|-------|
| `server.port` | 应用服务端口 | `8080` |
| `spring.datasource.url` | MySQL 数据库连接地址 | `jdbc:mysql://localhost:3306/online_store` |
| `spring.datasource.username` | 数据库用户名 | `root` |
| `spring.datasource.password` | 数据库密码 | `123456` |
| `spring.data.redis.host` | Redis 主机地址 | `localhost` |
| `spring.data.redis.port` | Redis 端口 | `6379` |
| `jwt.secret` | JWT 密钥（**必须配置**） | 环境变量 `JWT_SECRET` |
| `jwt.expiration` | JWT 过期时间（秒） | `86400`（24小时） |
| `spring.security.user.name` | 默认管理员用户名 | `admin` |
| `spring.security.user.password` | 默认管理员密码 | `admin123` |

### Nacos 配置（可选）

如需启用 Nacos 服务注册与发现：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        enabled: true
        register-enabled: true
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
```

设置环境变量：
```bash
export NACOS_ENABLED=true
```

## 📚 API 文档

### 认证

所有 API 请求需要进行 Basic Auth 认证或使用 JWT Token。

**Basic Auth：**
```
Username: admin
Password: admin123
```

### 主要接口

#### 商品管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/items` | 获取商品列表（支持分页） |
| GET | `/api/items/{id}` | 获取商品详情 |
| POST | `/api/items` | 创建商品 |
| PUT | `/api/items/{id}` | 更新商品 |
| DELETE | `/api/items/{id}` | 删除商品 |

#### 品牌管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/brands` | 获取品牌列表 |
| GET | `/api/brands/{id}` | 获取品牌详情 |
| POST | `/api/brands` | 创建品牌 |
| PUT | `/api/brands/{id}` | 更新品牌 |
| DELETE | `/api/brands/{id}` | 删除品牌 |

#### 分类管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/categories` | 获取分类列表 |
| GET | `/api/categories/{id}` | 获取分类详情 |
| POST | `/api/categories` | 创建分类 |
| PUT | `/api/categories/{id}` | 更新分类 |
| DELETE | `/api/categories/{id}` | 删除分类 |

#### 会员管理

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/members/register` | 会员注册 |
| POST | `/api/members/login` | 会员登录 |
| GET | `/api/members/{id}` | 获取会员信息 |
| PUT | `/api/members/{id}` | 更新会员信息 |

#### 属性管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/attributes` | 获取属性列表 |
| GET | `/api/attributes/{id}` | 获取属性详情 |
| POST | `/api/attributes` | 创建属性 |
| PUT | `/api/attributes/{id}` | 更新属性 |
| DELETE | `/api/attributes/{id}` | 删除属性 |

## 🗄️ 数据库设计

### 核心数据表

| 表名 | 说明 | SQL 脚本 |
|------|------|----------|
| `member` | 会员表 | `member_table.sql` |
| `category` | 商品分类表 | `category_table.sql` |
| `brand` | 品牌表 | `brand_table.sql` |
| `attribute` | 属性表 | `attribute_table.sql` |
| `attribute_value` | 属性值表 | `attribute_value_table.sql` |
| `item` | 商品表 | `item_table_table.sql` |
| `sku` | 商品SKU表 | `sku_table.sql` |
| `item_attribute_relation` | 商品属性关联表 | `item_attribute_relation_table.sql` |
| `item_access_log` | 商品访问日志表 | `item_access_log_table.sql` |

所有建表脚本位于 `src/main/resources/sql/` 目录下。

## 👨‍💻 开发指南

### 代码规范

- 遵循阿里巴巴 Java 开发手册
- 使用 Lombok 简化实体类代码
- Controller 层只负责请求转发和参数校验
- Service 层负责核心业务逻辑
- Mapper 层负责数据库操作

### 分支管理

```bash
# 创建功能分支
git checkout -b feature/your-feature-name

# 创建修复分支
git checkout -b fix/your-bug-fix
```

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=YourTestClass
```

### 打包部署

```bash
# 打包（跳过测试）
mvn clean package -DskipTests

# 打包并运行测试
mvn clean package

# 生成的 JAR 文件位于 target/ 目录
ls target/*.jar
```

## ❓ 常见问题

### Q1: 启动时报错 "JWT_SECRET not configured"

**解决方案：**

在启动前设置环境变量：
```bash
export JWT_SECRET=your-secret-key-here
```

或在 `application.yaml` 中直接配置：
```yaml
jwt:
  secret: your-secret-key-here
```

### Q2: 连接数据库失败

**解决方案：**

1. 确认 MySQL 服务已启动
2. 检查数据库连接配置（主机、端口、用户名、密码）
3. 确认数据库 `online_store` 已创建
4. 检查防火墙设置

### Q3: 运行时报错 "InaccessibleObjectException"

**解决方案：**

启动应用时添加 JVM 参数：
```bash
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

或在 Maven 启动时，确保 `pom.xml` 中的编译插件已正确配置。

### Q4: Redis 连接失败

**解决方案：**

1. 确认 Redis 服务已启动：`redis-cli ping`
2. 检查 Redis 配置（主机、端口、密码）
3. 如果 Redis 设置了密码，需要在配置文件中添加

### Q5: 如何修改默认端口？

**解决方案：**

在 `application.yaml` 中修改：
```yaml
server:
  port: 8888  # 修改为你想要的端口
```

## 📄 许可证

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📧 联系方式

如有问题，请通过以下方式联系：

- 提交 Issue
- 发送邮件至：[your-email@example.com]

---

**注意：** 本项目仅供学习和参考使用。 