# Online Store 🛒

一个基于Spring Boot + Spring Cloud的现代化在线商店系统，提供完整的电商核心功能，包括商品管理、用户管理、分类管理、品牌管理等。

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## ✨ 特性

- 🏗️ **现代化架构**: 基于Spring Boot 3.x + Spring Cloud 2024的微服务架构
- 🔐 **安全认证**: 集成Spring Security + JWT，支持用户注册登录
- 📊 **数据持久化**: MyBatis + MySQL，支持分页查询和事务管理
- ⚡ **缓存支持**: Redis缓存，提升系统性能
- 🌐 **RESTful API**: 标准的REST API设计，支持前后端分离
- 📝 **数据校验**: 完整的请求参数校验和错误处理
- 🐳 **容器化部署**: 支持Docker和Docker Compose部署
- 🌍 **国际化**: 支持多语言国际化配置
- 📈 **监控支持**: 集成Spring Boot Actuator监控
- ☁️ **服务发现**: 支持Nacos服务注册与发现

## 🛠️ 技术栈

### 后端框架
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: 安全认证和授权
- **Spring Data Redis**: Redis缓存支持

### 数据库与ORM
- **MySQL**: 8.2.0 - 主数据库
- **MyBatis**: 3.0.3 - ORM框架
- **MyBatis Spring Boot Starter**: 3.0.2
- **PageHelper**: 2.1.0 - 分页插件

### 缓存与中间件
- **Redis**: 最新版 - 缓存数据库
- **Jedis**: 5.2.0 - Redis Java客户端
- **Nacos**: 2.2.0 - 服务注册发现和配置中心

### 工具库
- **JWT**: 0.11.5 - JSON Web Token认证
- **Lombok**: 1.18.36 - 简化Java代码
- **Apache Commons Lang3**: 3.17.0 - 工具类库
- **Aliyun OSS**: 3.18.1 - 阿里云对象存储
- **Jackson**: JSON序列化和反序列化

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── bean/                    # 业务实体类
│   │   ├── Item.java           # 商品实体
│   │   ├── Member.java         # 会员实体
│   │   ├── Category.java       # 分类实体
│   │   └── ...
│   ├── config/                  # 配置类
│   │   ├── SecurityConfig.java # 安全配置
│   │   ├── RedisConfig.java    # Redis配置
│   │   └── ...
│   ├── controller/              # 控制器层
│   │   ├── ItemController.java # 商品API
│   │   ├── MemberController.java # 用户API
│   │   └── ...
│   ├── dto/                     # 数据传输对象
│   │   ├── Request类           # 请求DTO
│   │   ├── Response类          # 响应DTO
│   │   └── converter/          # 对象转换器
│   ├── entity/                  # 数据库实体
│   ├── enums/                   # 枚举类
│   ├── mapper/                  # MyBatis映射器
│   ├── service/                 # 业务逻辑层
│   │   └── impl/               # 服务实现类
│   ├── security/                # 安全相关
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtTokenUtil.java
│   └── utils/                   # 工具类
├── src/main/resources/
│   ├── mapper/                  # MyBatis XML映射文件
│   ├── sql/                     # 数据库脚本
│   ├── i18n/                    # 国际化配置
│   ├── application.yaml         # 应用配置
│   └── bootstrap.yaml           # 启动配置
├── src/test/                    # 测试代码
├── scripts/                     # 脚本工具
├── docker-compose.yaml          # Docker编排文件
├── Dockerfile                   # Docker镜像构建文件
└── pom.xml                     # Maven项目配置
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17或更高版本
- **Maven**: 3.6或更高版本
- **MySQL**: 8.0或更高版本
- **Redis**: 6.0或更高版本

### 本地开发环境搭建

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 数据库初始化
```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE online_store;

-- 执行SQL脚本（按顺序执行src/main/resources/sql/目录下的所有.sql文件）
```

#### 3. 配置文件设置

复制并修改配置文件：
```bash
cp src/main/resources/application-local.yaml.example src/main/resources/application-local.yaml
```

修改 `application-local.yaml` 中的配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password  # 如果有密码

jwt:
  secret: your_jwt_secret_key_here  # 请设置一个安全的密钥
```

#### 4. 运行应用

**方式一：使用Maven**
```bash
# 编译项目
mvn clean compile

# 运行应用（需要添加JVM参数）
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

**方式二：使用IDE**
- 在IDE中添加VM参数：`--add-opens java.base/java.lang=ALL-UNNAMED`
- 运行 `OnlineStoreApplication.java`

**方式三：打包运行**
```bash
mvn clean package -DskipTests
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 使用Docker部署

#### 1. 使用Docker Compose（推荐）
```bash
# 启动所有服务（MySQL + Redis）
docker-compose --profile all up -d

# 或者只启动MySQL
docker-compose --profile without-redis up -d

# 查看服务状态
docker-compose ps
```

#### 2. 手动Docker部署
```bash
# 构建应用镜像
docker build -t online-store:latest .

# 运行应用容器
docker run -d --name online-store \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  online-store:latest
```

## 📋 API文档

应用启动后，可以通过以下方式访问API：

- **基础URL**: `http://localhost:8080`
- **健康检查**: `http://localhost:8080/actuator/health`

### 主要API端点

| 模块 | 方法 | 端点 | 描述 |
|------|------|------|------|
| 用户管理 | POST | `/api/v1/members/registry` | 用户注册 |
| 用户管理 | POST | `/api/v1/members/login` | 用户登录 |
| 商品管理 | GET | `/api/v1/items/{itemId}` | 获取商品详情 |
| 商品管理 | GET | `/api/v1/items/{itemId}/details` | 获取商品详细信息 |
| 分类管理 | GET | `/api/v1/categories/{categoryId}` | 获取分类详情 |
| 品牌管理 | GET | `/api/v1/brands` | 获取品牌列表 |
| 品牌管理 | GET | `/api/v1/brands/{brandId}` | 获取品牌详情 |
| 属性管理 | POST | `/api/v1/attributes` | 创建商品属性 |
| 属性管理 | GET | `/api/v1/attributes/{attributeId}` | 获取属性详情 |
| 属性管理 | PUT | `/api/v1/attributes/{attributeId}` | 更新属性信息 |

### API请求示例

**用户注册**
```bash
curl -X POST http://localhost:8080/api/v1/members/registry \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'
```

**用户登录**
```bash
curl -X POST http://localhost:8080/api/v1/members/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

## 🔧 开发指南

### 代码规范
- 使用Lombok减少样板代码
- 遵循RESTful API设计原则
- 统一异常处理和响应格式
- 完善的参数校验和错误提示

### 数据库规范
- 表名使用下划线命名
- 字段名使用驼峰命名（MyBatis自动转换）
- 所有表必须有创建时间和更新时间字段

### 安全规范
- 敏感信息不得硬编码，使用环境变量
- JWT Token有效期设置合理
- API接口需要适当的权限控制

## 🧪 测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=MessageSourceTest

# 跳过测试进行打包
mvn package -DskipTests
```

## 📊 监控和运维

### 应用监控
- **健康检查**: `/actuator/health`
- **应用信息**: `/actuator/info`
- **指标监控**: `/actuator/metrics`

### 日志管理
- 应用日志位置：`logs/`目录
- 日志级别可通过配置文件调整
- 支持结构化日志输出

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📝 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👥 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 [Issue](../../issues)
- 发送邮件到：[your-email@example.com]

---

⭐ 如果这个项目对您有帮助，请给它一个星标！ 