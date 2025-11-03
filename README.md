# Online Store（在线商店）

这是一个基于 Spring Cloud 的现代化在线商店项目，采用微服务架构设计，提供完整的商品管理、用户管理、认证授权等功能。

## 📋 目录

- [技术栈](#技术栈)
- [核心功能](#核心功能)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API文档](#api文档)
- [部署指南](#部署指南)

## 🛠 技术栈

### 核心框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0

### 中间件
- **数据库**: MySQL 8.2.0
- **缓存**: Redis (Jedis 5.2.0)
- **注册中心/配置中心**: Nacos 2.2.0

### 核心依赖
- **ORM框架**: MyBatis 3.0.2 + PageHelper 2.1.0
- **安全框架**: Spring Security + JWT (jjwt 0.11.5)
- **对象存储**: Aliyun OSS 3.18.1
- **工具库**: Lombok 1.18.36, Apache Commons Lang3 3.17.0

## ✨ 核心功能

- 🔐 **用户认证与授权**: 基于 JWT 的无状态认证机制
- 📦 **商品管理**: 商品分类、品牌、属性、详情管理
- 👥 **会员管理**: 用户注册、登录、信息管理
- 🏷️ **分类与品牌**: 多级分类体系、品牌管理
- 📊 **分页查询**: 集成 PageHelper 实现高效分页
- 💾 **Redis缓存**: 提升系统性能和响应速度
- 📁 **文件存储**: 集成阿里云 OSS 对象存储服务

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用入口
│   │   │   ├── controller/                     # 控制层
│   │   │   │   ├── AttributeController.java   # 属性管理
│   │   │   │   ├── BrandController.java       # 品牌管理
│   │   │   │   ├── CategoryController.java    # 分类管理
│   │   │   │   ├── ItemController.java        # 商品管理
│   │   │   │   ├── ItemDetailController.java  # 商品详情
│   │   │   │   └── MemberController.java      # 会员管理
│   │   │   ├── service/                        # 业务层
│   │   │   ├── mapper/                         # 数据访问层
│   │   │   ├── entity/                         # 实体类
│   │   │   ├── dto/                            # 数据传输对象
│   │   │   ├── config/                         # 配置类
│   │   │   ├── security/                       # 安全配置
│   │   │   ├── utils/                          # 工具类
│   │   │   ├── enums/                          # 枚举类
│   │   │   ├── exceptions/                     # 异常处理
│   │   │   └── handler/                        # 全局处理器
│   │   └── resources/
│   │       ├── application.yaml                # 应用配置
│   │       └── mapper/                         # MyBatis XML映射文件
│   └── test/                                   # 测试代码
├── scripts/                                    # 脚本文件
├── Dockerfile                                  # Docker镜像构建文件
├── docker-compose.yaml                         # Docker Compose配置
├── pom.xml                                     # Maven项目配置
└── README.md                                   # 项目说明文档
```

## 📋 环境要求

| 软件 | 版本要求 | 说明 |
|------|---------|------|
| JDK | 17+ | 必须 |
| Maven | 3.6+ | 必须 |
| MySQL | 8.0+ | 必须 |
| Redis | 6.0+ | 必须 |
| Nacos | 2.2.0+ | 可选（用于服务注册和配置中心） |

## 🚀 快速开始

### 方式一：本地运行

#### 1. 启动依赖服务

确保 MySQL 和 Redis 服务已启动。你可以使用 Docker Compose 快速启动：

```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 或只启动 MySQL（如果已有 Redis）
docker-compose --profile without-redis up -d
```

#### 2. 初始化数据库

创建数据库：

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

执行 SQL 脚本（如果有初始化脚本）：

```bash
# 导入数据库脚本
mysql -u root -p online_store < scripts/init.sql
```

#### 3. 配置环境变量

设置必要的环境变量：

```bash
# JWT密钥（必须）
export JWT_SECRET="your-secret-key-here"

# 数据库配置（可选，使用默认值则无需设置）
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/online_store"
export SPRING_DATASOURCE_USERNAME="root"
export SPRING_DATASOURCE_PASSWORD="123456"

# Redis配置（可选）
export SPRING_REDIS_HOST="localhost"
export SPRING_REDIS_PORT="6379"

# 管理员账号（可选）
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="admin123"
```

#### 4. 编译运行

```bash
# 清理并编译项目
mvn clean install

# 方式1：使用 Maven 运行
mvn spring-boot:run

# 方式2：运行打包后的 jar
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

#### 5. 验证服务

访问健康检查端点：

```bash
curl http://localhost:8080/actuator/health
```

### 方式二：Docker 部署

#### 1. 构建 Docker 镜像

```bash
docker build -t online-store:latest .
```

#### 2. 运行容器

```bash
docker run -d \
  -p 8080:8080 \
  -e JWT_SECRET="your-secret-key" \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/online_store" \
  --name online-store \
  online-store:latest
```

## ⚙️ 配置说明

### 核心配置项

应用配置文件位于 `src/main/resources/application.yaml`，支持以下环境变量覆盖：

| 配置项 | 环境变量 | 默认值 | 说明 |
|--------|---------|--------|------|
| 服务端口 | `SERVER_PORT` | 8080 | 应用服务端口 |
| 数据库地址 | `SPRING_DATASOURCE_URL` | localhost:3306/online_store | MySQL连接地址 |
| 数据库用户名 | `SPRING_DATASOURCE_USERNAME` | root | 数据库用户 |
| 数据库密码 | `SPRING_DATASOURCE_PASSWORD` | 123456 | 数据库密码 |
| Redis地址 | `SPRING_REDIS_HOST` | localhost | Redis服务器地址 |
| Redis端口 | `SPRING_REDIS_PORT` | 6379 | Redis端口 |
| JWT密钥 | `JWT_SECRET` | **必须设置** | JWT签名密钥 |
| JWT过期时间 | `JWT_EXPIRATION` | 86400 | Token过期时间（秒） |
| Nacos开关 | `NACOS_ENABLED` | false | 是否启用Nacos |
| 管理员账号 | `ADMIN_USERNAME` | admin | 管理员用户名 |
| 管理员密码 | `ADMIN_PASSWORD` | admin123 | 管理员密码 |

### 配置文件说明

- **application.yaml**: 主配置文件
- 支持 Spring Profiles 多环境配置（local/dev/prod）
- 使用 `SPRING_PROFILES_ACTIVE` 环境变量切换环境

## 📖 API文档

### 主要接口

| 模块 | 接口路径 | 说明 |
|------|---------|------|
| 商品管理 | `/api/items/**` | 商品CRUD操作 |
| 商品详情 | `/api/item-details/**` | 商品详细信息管理 |
| 分类管理 | `/api/categories/**` | 商品分类管理 |
| 品牌管理 | `/api/brands/**` | 品牌信息管理 |
| 属性管理 | `/api/attributes/**` | 商品属性管理 |
| 会员管理 | `/api/members/**` | 用户信息管理 |

### 认证说明

- 使用 JWT Token 进行身份验证
- 请求头需携带：`Authorization: Bearer <token>`
- 默认管理员账号：admin / admin123

## 🚢 部署指南

### 开发环境部署

```bash
# 设置开发环境
export SPRING_PROFILES_ACTIVE=local
mvn spring-boot:run
```

### 生产环境部署

1. **修改配置文件**

创建生产环境配置 `application-prod.yaml`

2. **编译打包**

```bash
mvn clean package -DskipTests
```

3. **启动服务**

```bash
java -jar \
  --add-opens java.base/java.lang=ALL-UNNAMED \
  -Dspring.profiles.active=prod \
  -Xms512m -Xmx1024m \
  target/online-store-1.0-SNAPSHOT.jar
```

4. **使用 systemd 管理服务**（推荐）

创建服务文件 `/etc/systemd/system/online-store.service`：

```ini
[Unit]
Description=Online Store Application
After=network.target

[Service]
Type=simple
User=app
WorkingDirectory=/opt/online-store
Environment="JWT_SECRET=your-secret-key"
ExecStart=/usr/bin/java --add-opens java.base/java.lang=ALL-UNNAMED -jar /opt/online-store/online-store.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启用并启动服务：

```bash
sudo systemctl daemon-reload
sudo systemctl enable online-store
sudo systemctl start online-store
sudo systemctl status online-store
```

## 🔧 常见问题

### 1. 启动时报错 `InaccessibleObjectException`

**解决方案**：添加 JVM 参数 `--add-opens java.base/java.lang=ALL-UNNAMED`

### 2. JWT Token 验证失败

**解决方案**：确保设置了 `JWT_SECRET` 环境变量

### 3. 数据库连接失败

**解决方案**：
- 检查 MySQL 服务是否启动
- 验证数据库连接配置是否正确
- 确认数据库 `online_store` 已创建

### 4. Redis 连接失败

**解决方案**：
- 检查 Redis 服务是否启动
- 验证 Redis 连接配置
- 如不需要 Redis，可临时注释相关配置

## 📄 许可证

本项目仅供学习和研究使用。

## 👥 联系方式

如有问题或建议，欢迎提交 Issue。 