# Online Store - 在线商店系统

这是一个基于 Spring Cloud 微服务架构的在线商店项目，提供完整的电商业务功能，包括商品管理、会员管理、品牌管理、属性管理等模块。

## 📋 目录

- [技术栈](#技术栈)
- [核心功能](#核心功能)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [Docker 部署](#docker-部署)
- [API 文档](#api-文档)

## 🚀 技术栈

### 后端框架
- **JDK 17** - Java 开发平台
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全认证框架
- **JWT 0.11.5** - Token 认证

### 服务治理
- **Nacos 2.2.0** - 服务注册发现与配置中心
- **Spring Cloud Alibaba 2022.0.0.0** - 阿里云微服务组件

### 持久层
- **MyBatis 3.0.3** - ORM 框架
- **PageHelper 2.1.0** - 分页插件
- **MySQL 8.2.0** - 关系型数据库
- **Redis (Jedis 5.2.0)** - 缓存数据库

### 工具组件
- **Lombok 1.18.36** - 代码简化工具
- **Apache Commons Lang3 3.17.0** - 工具类库
- **Aliyun OSS 3.18.1** - 阿里云对象存储
- **CGLIB 3.3.0** - 动态代理

## ✨ 核心功能

- **商品管理**：商品 CRUD、商品详情、商品分类
- **品牌管理**：品牌信息管理
- **会员管理**：用户注册、登录、信息维护
- **属性管理**：商品属性配置
- **分类管理**：商品分类体系
- **安全认证**：JWT Token 认证、Spring Security 权限控制
- **文件存储**：阿里云 OSS 文件上传
- **缓存支持**：Redis 缓存提升性能

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用主入口
│   │   │   ├── bean/                          # Bean 配置类
│   │   │   ├── config/                        # 配置类（Redis、Security、MyBatis等）
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
│   │   │   ├── exceptions/                    # 异常处理
│   │   │   ├── handler/                       # 全局处理器
│   │   │   ├── mapper/                        # MyBatis Mapper
│   │   │   ├── security/                      # 安全相关
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yaml               # 应用配置
│   │       ├── application-local.yaml         # 本地环境配置
│   │       ├── bootstrap.yaml                 # 启动配置
│   │       ├── i18n/                          # 国际化资源
│   │       ├── mapper/                        # MyBatis XML 映射
│   │       └── sql/                           # SQL 脚本
│   └── test/                                  # 测试代码
├── scripts/                                   # Python 脚本工具
├── docker-compose.yaml                        # Docker Compose 配置
├── Dockerfile                                 # Docker 镜像构建
├── pom.xml                                    # Maven 依赖配置
└── README.md                                  # 项目说明文档
```

## 💻 环境要求

- **JDK 17** 或更高版本
- **Maven 3.6+** 或更高版本
- **MySQL 8.0+**
- **Redis 6.0+**
- **Nacos 2.2.0+**（可选，默认禁用）

## 🎯 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd online-store
```

### 2. 启动依赖服务

#### 方式一：使用 Docker Compose（推荐）

```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 仅启动 MySQL
docker-compose --profile without-redis up -d
```

#### 方式二：手动安装

确保 MySQL 和 Redis 服务已启动并运行在默认端口。

### 3. 初始化数据库

```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行 SQL 脚本（位于 src/main/resources/sql/ 目录）
USE online_store;
source src/main/resources/sql/*.sql
```

### 4. 配置环境变量

创建环境变量或修改 `application.yaml` 配置：

```bash
# JWT 密钥（必须配置）
export JWT_SECRET=your_jwt_secret_key_here

# 数据库配置（可选，默认值见下方）
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/online_store
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=123456

# Redis 配置（可选）
export SPRING_REDIS_HOST=localhost
export SPRING_REDIS_PORT=6379

# 激活环境配置（可选）
export SPRING_PROFILES_ACTIVE=local

# Nacos 配置（可选，默认禁用）
export NACOS_ENABLED=false
export NACOS_SERVER_ADDR=localhost:8848
```

### 5. 编译并运行

```bash
# 编译项目
mvn clean package

# 运行应用
mvn spring-boot:run

# 或使用 Java 命令运行（需要添加 JVM 参数）
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 6. 验证服务

应用启动后，访问以下地址验证：

- **应用端口**：http://localhost:8080
- **健康检查**：http://localhost:8080/actuator/health
- **默认认证**：用户名 `admin` / 密码 `admin123`

## ⚙️ 配置说明

### 核心配置文件

| 文件 | 说明 |
|------|------|
| `application.yaml` | 主配置文件，包含数据源、Redis、MyBatis 等配置 |
| `application-local.yaml` | 本地开发环境配置 |
| `bootstrap.yaml` | 启动配置，包含 Nacos 配置中心设置 |

### 关键配置项

#### 数据库配置

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

#### Redis 配置

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
      jedis:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
```

#### JWT 配置

```yaml
jwt:
  secret: ${JWT_SECRET}  # 必须通过环境变量配置
  expiration: 86400      # Token 有效期（秒）
```

#### Nacos 配置（可选）

```yaml
spring:
  cloud:
    nacos:
      enabled: ${NACOS_ENABLED:false}
      discovery:
        enabled: ${NACOS_ENABLED:false}
        server-addr: ${NACOS_SERVER_ADDR:localhost:8848}
```

## 🐳 Docker 部署

### 构建镜像

```bash
docker build -t online-store:latest .
```

### 运行容器

```bash
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e JWT_SECRET=your_secret_key \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/online_store \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=123456 \
  online-store:latest
```

### 使用 Docker Compose

```bash
# 启动所有服务（MySQL + Redis + 应用）
docker-compose --profile all up -d
```

## 📚 API 文档

### 主要 API 端点

#### 商品管理
- `GET /items` - 获取商品列表
- `GET /items/{id}` - 获取商品详情
- `POST /items` - 创建商品
- `PUT /items/{id}` - 更新商品
- `DELETE /items/{id}` - 删除商品

#### 品牌管理
- `GET /brands` - 获取品牌列表
- `POST /brands` - 创建品牌
- `PUT /brands/{id}` - 更新品牌
- `DELETE /brands/{id}` - 删除品牌

#### 分类管理
- `GET /categories` - 获取分类列表
- `POST /categories` - 创建分类

#### 会员管理
- `GET /members` - 获取会员列表
- `POST /members/register` - 会员注册
- `POST /members/login` - 会员登录

#### 属性管理
- `GET /attributes` - 获取属性列表
- `POST /attributes` - 创建属性

### 认证说明

所有 API 请求需要在 Header 中携带 JWT Token：

```
Authorization: Bearer <your_jwt_token>
```

## 🛠️ 开发工具

### Scripts 目录

项目包含 Python 脚本工具（位于 `scripts/` 目录），使用 `uv` 作为包管理器：

```bash
cd scripts
uv run main.py
```

## 📝 注意事项

1. **JWT Secret 配置**：生产环境必须配置强密钥，建议使用 32 位以上随机字符串
2. **数据库字符集**：确保使用 `utf8mb4` 字符集支持 emoji 和特殊字符
3. **JVM 参数**：运行时需添加 `--add-opens java.base/java.lang=ALL-UNNAMED` 参数
4. **Nacos 配置**：如不使用 Nacos，保持 `NACOS_ENABLED=false` 即可
5. **端口占用**：确保 8080 端口未被占用

## 📄 许可证

本项目采用 MIT 许可证。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---

**最后更新**：2025-10-12 