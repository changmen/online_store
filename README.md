# Online Store

这是一个基于 Spring Cloud 的在线商店项目，实现了商品管理、会员管理、品牌管理、分类管理等核心功能。

## 📋 目录

- [技术栈](#技术栈)
- [项目功能](#项目功能)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [Docker 部署](#docker-部署)
- [API 文档](#api-文档)

## 🛠 技术栈

### 核心框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0

### 数据层
- **MyBatis**: 3.0.2
- **PageHelper**: 2.1.0
- **MySQL**: 8.2.0
- **Redis**: Jedis 5.2.0

### 安全认证
- **Spring Security**: 集成 Spring Boot 3.4.3
- **JWT**: 0.11.5

### 其他组件
- **Nacos**: 2.2.0 (服务注册与配置中心)
- **Aliyun OSS**: 3.18.1 (对象存储)
- **Lombok**: 1.18.36
- **Commons Lang3**: 3.17.0

## ✨ 项目功能

- **商品管理**: 商品信息、SKU、属性、详情管理
- **品牌管理**: 品牌信息维护
- **分类管理**: 商品分类体系
- **会员管理**: 会员信息维护
- **安全认证**: 基于 JWT 的用户认证授权
- **访问日志**: 商品访问记录统计

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java  # 主启动类
│   │   │   ├── bean/                        # Bean 定义
│   │   │   ├── config/                      # 配置类
│   │   │   ├── constants/                   # 常量定义
│   │   │   ├── controller/                  # REST 控制器
│   │   │   │   ├── AttributeController.java
│   │   │   │   ├── BrandController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ItemController.java
│   │   │   │   ├── ItemDetailController.java
│   │   │   │   └── MemberController.java
│   │   │   ├── dto/                         # 数据传输对象
│   │   │   ├── entity/                      # 实体类
│   │   │   ├── enums/                       # 枚举类
│   │   │   ├── exceptions/                  # 异常定义
│   │   │   ├── handler/                     # 异常处理器
│   │   │   ├── mapper/                      # MyBatis Mapper
│   │   │   ├── security/                    # 安全配置
│   │   │   ├── service/                     # 业务逻辑层
│   │   │   └── utils/                       # 工具类
│   │   └── resources/
│   │       ├── application.yaml             # 主配置文件
│   │       ├── application-local.yaml       # 本地配置
│   │       ├── bootstrap.yaml               # 引导配置
│   │       ├── i18n/                        # 国际化文件
│   │       ├── mapper/                      # MyBatis XML 映射文件
│   │       └── sql/                         # 数据库建表脚本
│   └── test/                                # 测试代码
├── scripts/                                 # 脚本工具
├── docker-compose.yaml                      # Docker Compose 配置
├── Dockerfile                               # Docker 镜像构建文件
└── pom.xml                                  # Maven 配置文件
```

## 💻 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **Nacos**: 2.2.0 (可选，默认关闭)

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd online_store
```

### 2. 初始化数据库

```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE online_store;

-- 执行建表脚本 (位于 src/main/resources/sql/ 目录下)
-- 依次执行以下脚本：
source src/main/resources/sql/brand_table.sql;
source src/main/resources/sql/category_table.sql;
source src/main/resources/sql/member_table.sql;
source src/main/resources/sql/item_table_table.sql;
source src/main/resources/sql/sku_table.sql;
source src/main/resources/sql/attribute_table.sql;
source src/main/resources/sql/attribute_value_table.sql;
source src/main/resources/sql/item_attribute_relation_table.sql;
source src/main/resources/sql/item_access_log_table.sql;
```

### 3. 配置环境变量

创建 `.env` 文件或配置环境变量：

```bash
# JWT 密钥 (必需)
export JWT_SECRET=your-secret-key-here

# 管理员账号 (可选，默认 admin/admin123)
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123

# 环境配置 (可选，默认 local)
export SPRING_PROFILES_ACTIVE=local

# Nacos 配置 (可选，默认关闭)
export NACOS_ENABLED=false
```

### 4. 修改配置文件

编辑 `src/main/resources/application-local.yaml`，根据实际情况修改数据库和 Redis 配置：

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
      password:
```

### 5. 启动应用

#### 方式一：使用 Maven

```bash
# 编译项目
mvn clean package

# 运行应用 (需要添加 JVM 参数)
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

#### 方式二：使用 Java 命令

```bash
# 编译打包
mvn clean package

# 运行 JAR 文件
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 6. 验证启动

应用启动后，访问：
- 应用端口：http://localhost:8080
- Actuator 健康检查：http://localhost:8080/actuator/health

## ⚙️ 配置说明

### 配置文件说明

- `application.yaml`: 主配置文件，定义通用配置
- `application-local.yaml`: 本地开发环境配置
- `bootstrap.yaml`: Spring Cloud 引导配置，用于 Nacos 配置中心

### 重要配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `server.port` | 应用端口 | 8080 |
| `spring.profiles.active` | 激活的配置文件 | local |
| `spring.cloud.nacos.discovery.enabled` | 是否启用 Nacos 服务发现 | false |
| `jwt.secret` | JWT 签名密钥 | 环境变量 |
| `jwt.expiration` | JWT 过期时间(秒) | 86400 |

## 🐳 Docker 部署

### 使用 Docker Compose 启动依赖服务

```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 仅启动 MySQL
docker-compose --profile without-redis up -d
```

### 配置说明

- MySQL 默认端口：3306
- MySQL root 密码：123456
- Redis 默认端口：6379
- 数据持久化目录：
  - MySQL: `~/work/database/docker-compose/data/mysql`
  - Redis: `/tmp/docker-compose/data/redis`

## 📚 API 文档

### 主要 API 端点

#### 品牌管理
- `GET /brands` - 获取品牌列表
- `POST /brands` - 创建品牌
- `PUT /brands/{id}` - 更新品牌
- `DELETE /brands/{id}` - 删除品牌

#### 分类管理
- `GET /categories` - 获取分类列表
- `POST /categories` - 创建分类

#### 商品管理
- `GET /items` - 获取商品列表
- `POST /items` - 创建商品

#### 会员管理
- `GET /members` - 获取会员列表
- `POST /members` - 创建会员

#### 属性管理
- `GET /attributes` - 获取属性列表
- `POST /attributes` - 创建属性

> 注：所有 API 请求需要携带有效的 JWT Token 进行认证

## 📝 注意事项

1. **JVM 参数**: 由于 MyBatis 的反射机制，启动时需要添加 `--add-opens java.base/java.lang=ALL-UNNAMED` 参数
2. **JWT 密钥**: 生产环境务必使用强密钥，并通过环境变量配置
3. **数据库编码**: 确保数据库使用 `utf8mb4` 编码以支持完整的 Unicode 字符
4. **Nacos 配置**: 默认关闭 Nacos，如需使用请设置 `NACOS_ENABLED=true` 并配置 Nacos 服务地址

## 📄 License

This project is licensed under the MIT License. 