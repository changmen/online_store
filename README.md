# 🛍️ Online Store - 在线商店系统

一个基于 Spring Boot 3.x 和 Spring Cloud 构建的现代化在线商店系统，支持用户管理、商品管理、订单处理等核心电商功能。

[![Java Version](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

## 📋 目录

- [项目介绍](#项目介绍)
- [技术栈](#技术栈)
- [核心功能](#核心功能)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API 文档](#api-文档)
- [部署方案](#部署方案)
- [开发指南](#开发指南)
- [许可证](#许可证)

## 🎯 项目介绍

本项目是一个企业级的在线商店系统，采用微服务架构设计，具备高可用、高并发、易扩展的特点。系统包含用户认证、商品管理、订单处理、支付集成等完整的电商业务流程。

### 特色亮点

- 🏗️ **微服务架构**：基于 Spring Cloud 构建的分布式系统
- 🔐 **安全认证**：集成 Spring Security + JWT 的安全框架
- 🚀 **高性能**：Redis 缓存 + MyBatis 数据访问优化
- 📦 **容器化**：支持 Docker 容器化部署
- 🌐 **服务发现**：集成 Nacos 服务注册与发现
- 📊 **监控完善**：Spring Boot Actuator 健康检查
- 🔧 **易于维护**：统一的异常处理和日志管理

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17+ | 编程语言 |
| Spring Boot | 3.4.3 | 应用框架 |
| Spring Cloud | 2024.0.0 | 微服务框架 |
| Spring Security | - | 安全框架 |
| MyBatis | 3.0.3 | ORM 框架 |
| MySQL | 8.2.0 | 关系型数据库 |
| Redis | - | 缓存数据库 |
| JWT | 0.11.5 | 身份认证 |
| Nacos | 2.2.0 | 服务注册发现 |
| Lombok | 1.18.36 | 代码简化 |
| PageHelper | 2.1.0 | 分页插件 |
| Aliyun OSS | 3.18.1 | 对象存储 |

### 开发工具

- **构建工具**：Maven 3.6+
- **容器化**：Docker & Docker Compose
- **IDE**：IntelliJ IDEA / Eclipse
- **版本控制**：Git

## ⚡ 核心功能

### 🔐 用户管理
- 用户注册、登录、登出
- JWT Token 认证
- 用户信息管理
- 权限控制

### 📦 商品管理
- 商品信息维护
- 商品分类管理
- 库存管理
- 商品搜索

### 🛒 订单系统
- 购物车管理
- 订单创建与处理
- 订单状态跟踪
- 订单历史查询

### 💳 支付集成
- 多种支付方式支持
- 支付状态管理
- 退款处理

### 📊 系统管理
- 系统监控
- 日志管理
- 配置管理
- 健康检查

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # 应用启动类
│   │   │   ├── bean/                           # Bean 配置
│   │   │   ├── config/                         # 配置类
│   │   │   ├── constants/                      # 常量定义
│   │   │   ├── controller/                     # 控制器层
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── exceptions/                    # 异常处理
│   │   │   ├── handler/                       # 处理器
│   │   │   ├── mapper/                        # 数据访问层
│   │   │   ├── security/                      # 安全配置
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yaml               # 主配置文件
│   │       ├── application-local.yaml         # 本地配置
│   │       ├── bootstrap.yaml                 # 启动配置
│   │       ├── mapper/                        # MyBatis 映射文件
│   │       ├── sql/                          # 数据库脚本
│   │       └── i18n/                         # 国际化资源
│   └── test/                                  # 测试代码
├── scripts/                                   # 脚本文件
├── docker-compose.yaml                        # Docker 编排文件
├── Dockerfile                                 # Docker 镜像文件
├── pom.xml                                    # Maven 配置
└── README.md                                  # 项目说明
```

## 🔧 环境要求

### 基础环境

- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **Git**: 用于代码管理

### 数据库环境

- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本

### 可选环境

- **Docker**: 支持容器化部署
- **Nacos**: 服务注册与发现（可选）

## 🚀 快速开始

### 方式一：本地运行

#### 1. 克隆项目

```bash
git clone <repository-url>
cd online_store
```

#### 2. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入初始化脚本（如果有）
USE online_store;
SOURCE src/main/resources/sql/init.sql;
```

#### 3. 配置环境变量

```bash
# 必需的环境变量
export JWT_SECRET=your-secret-key-here
export SPRING_PROFILES_ACTIVE=local

# 可选的环境变量
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
export NACOS_ENABLED=false
```

#### 4. 启动应用

```bash
# 添加 JVM 参数并启动
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

### 方式二：Docker 运行

#### 1. 启动基础服务

```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 或者仅启动 MySQL
docker-compose --profile without-redis up -d
```

#### 2. 构建和运行应用

```bash
# 构建应用镜像
docker build -t online-store .

# 运行应用容器
docker run -d \
  --name online-store-app \
  --network host \
  -e JWT_SECRET=your-secret-key \
  -e SPRING_PROFILES_ACTIVE=local \
  online-store
```

### 3. 验证部署

访问以下端点验证服务是否正常运行：

- **应用首页**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **API 文档**: http://localhost:8080/swagger-ui.html (如果已配置)

## ⚙️ 配置说明

### 环境配置

项目支持多环境配置，通过 `SPRING_PROFILES_ACTIVE` 环境变量切换：

- `local`: 本地开发环境
- `dev`: 开发环境
- `test`: 测试环境
- `prod`: 生产环境

### 关键配置项

| 配置项 | 环境变量 | 默认值 | 说明 |
|--------|----------|--------|------|
| 服务端口 | SERVER_PORT | 8080 | 应用服务端口 |
| 数据库连接 | DB_URL | localhost:3306/online_store | 数据库连接地址 |
| Redis 配置 | REDIS_HOST | localhost | Redis 服务地址 |
| JWT 密钥 | JWT_SECRET | 无 | JWT 签名密钥（必需） |
| 管理员账号 | ADMIN_USERNAME | admin | 默认管理员用户名 |
| 管理员密码 | ADMIN_PASSWORD | admin123 | 默认管理员密码 |
| Nacos 开关 | NACOS_ENABLED | false | 是否启用 Nacos |

### 数据库配置

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

### Redis 配置

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

## 📖 API 文档

### 认证相关

- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/profile` - 获取用户信息

### 商品相关

- `GET /api/products` - 获取商品列表
- `GET /api/products/{id}` - 获取商品详情
- `POST /api/products` - 创建商品
- `PUT /api/products/{id}` - 更新商品
- `DELETE /api/products/{id}` - 删除商品

### 订单相关

- `GET /api/orders` - 获取订单列表
- `GET /api/orders/{id}` - 获取订单详情
- `POST /api/orders` - 创建订单
- `PUT /api/orders/{id}/status` - 更新订单状态

> 📝 **注意**: 具体的 API 接口文档请参考项目中的 Swagger 文档或接口测试文件。

## 🐳 部署方案

### 开发环境部署

1. 使用 `application-local.yaml` 配置
2. 本地启动 MySQL 和 Redis 服务
3. 通过 IDE 或 Maven 命令启动应用

### 生产环境部署

#### Docker 部署

```bash
# 1. 构建生产镜像
docker build -t online-store:latest .

# 2. 使用 docker-compose 部署
docker-compose --profile all up -d

# 3. 启动应用
docker run -d \
  --name online-store-prod \
  --restart unless-stopped \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JWT_SECRET=your-prod-secret \
  online-store:latest
```

#### Kubernetes 部署

```yaml
# 参考 k8s/ 目录下的配置文件（如果提供）
apiVersion: apps/v1
kind: Deployment
metadata:
  name: online-store
spec:
  replicas: 3
  selector:
    matchLabels:
      app: online-store
  template:
    metadata:
      labels:
        app: online-store
    spec:
      containers:
      - name: online-store
        image: online-store:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: online-store-secret
              key: jwt-secret
```

## 👨‍💻 开发指南

### 代码规范

- 使用 Java 17+ 语法特性
- 遵循 Spring Boot 最佳实践
- 使用 Lombok 简化代码
- 统一异常处理机制
- 完善的单元测试覆盖

### 开发流程

1. **环境准备**
   ```bash
   # 安装依赖
   mvn clean install
  
   # 启动开发环境
   mvn spring-boot:run -Dspring.profiles.active=local
   ```

2. **数据库迁移**
   ```bash
   # 执行数据库脚本
   mysql -u root -p online_store < src/main/resources/sql/init.sql
   ```

3. **代码提交**
   ```bash
   # 代码格式化
   mvn spotless:apply
  
   # 运行测试
   mvn test
  
   # 提交代码
   git add .
   git commit -m "feat: add new feature"
   git push origin feature-branch
   ```

### 测试指南

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 生成测试报告
mvn surefire-report:report
```

### 监控和调试

- **应用监控**: http://localhost:8080/actuator
- **健康检查**: http://localhost:8080/actuator/health
- **性能指标**: http://localhost:8080/actuator/metrics
- **日志配置**: 查看 `application.yaml` 中的 logging 配置

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目基于 MIT 许可证开源。详细信息请查看 [LICENSE](LICENSE) 文件。

## 📞 支持

如果您在使用过程中遇到问题，可以通过以下方式获取帮助：

- 📧 邮件支持：your-email@example.com
- 🐛 问题反馈：[GitHub Issues](https://github.com/your-repo/issues)
- 📖 文档中心：[项目 Wiki](https://github.com/your-repo/wiki)

---

**⭐ 如果这个项目对您有帮助，请给我们一个 Star！** 