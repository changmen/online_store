# Online Store

一个基于 Spring Boot 和 Spring Cloud 的现代化在线商城系统，提供商品管理、用户管理、分类管理等核心电商功能。

## 📋 目录

- [特性](#特性)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API 文档](#api-文档)
- [Docker 部署](#docker-部署)

## ✨ 特性

- 🛍️ **商品管理**：支持商品信息、SKU、属性、品牌等完整的商品体系
- 👤 **用户管理**：基于 Spring Security 和 JWT 的用户认证与授权
- 📂 **分类管理**：多级商品分类管理
- 🔍 **商品检索**：支持商品浏览和详情查看
- 📊 **访问日志**：商品访问记录追踪
- 🗄️ **缓存支持**：集成 Redis 缓存提升性能
- 🌐 **微服务就绪**：支持 Nacos 服务注册与配置中心
- 📦 **OSS 集成**：支持阿里云 OSS 对象存储

## 🛠 技术栈

### 核心框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0

### 数据存储
- **MySQL**: 8.2.0 - 关系型数据库
- **Redis**: 5.2.0 (Jedis) - 缓存与会话管理
- **MyBatis**: 3.0.2 - ORM 框架
- **PageHelper**: 2.1.0 - 分页插件

### 安全认证
- **Spring Security** - 安全框架
- **JWT**: 0.11.5 - JSON Web Token 认证

### 服务治理
- **Nacos**: 2.2.0 - 服务注册与配置中心

### 工具库
- **Lombok**: 1.18.36 - 简化 Java 代码
- **Apache Commons Lang3**: 3.17.0
- **Apache Commons Collections**: 3.2.2
- **Aliyun OSS SDK**: 3.18.1 - 阿里云对象存储

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java   # 应用入口
│   │   │   ├── controller/                   # REST API 控制器
│   │   │   │   ├── AttributeController.java  # 属性管理
│   │   │   │   ├── BrandController.java      # 品牌管理
│   │   │   │   ├── CategoryController.java   # 分类管理
│   │   │   │   ├── ItemController.java       # 商品管理
│   │   │   │   ├── ItemDetailController.java # 商品详情
│   │   │   │   └── MemberController.java     # 会员管理
│   │   │   ├── service/                      # 业务逻辑层
│   │   │   ├── mapper/                       # MyBatis 数据访问层
│   │   │   ├── entity/                       # 实体类
│   │   │   ├── dto/                          # 数据传输对象
│   │   │   ├── config/                       # 配置类
│   │   │   ├── security/                     # 安全配置
│   │   │   ├── utils/                        # 工具类
│   │   │   ├── enums/                        # 枚举类
│   │   │   ├── exceptions/                   # 异常处理
│   │   │   └── handler/                      # 全局处理器
│   │   └── resources/
│   │       ├── application.yaml              # 应用配置
│   │       └── mapper/                       # MyBatis XML 映射文件
│   └── test/                                 # 测试代码
├── scripts/                                  # 脚本工具
├── docker-compose.yaml                       # Docker Compose 配置
├── Dockerfile                                # Docker 镜像构建文件
├── pom.xml                                   # Maven 项目配置
└── README.md                                 # 项目说明文档
```

## 💻 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6+ 
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Nacos**: 2.2.0+ (可选，用于服务注册与配置中心)

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd online_store
```

### 2. 数据库初始化

创建数据库：

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 配置应用

编辑 `src/main/resources/application.yaml`，修改以下配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password  # 如果 Redis 设置了密码

jwt:
  secret: your_jwt_secret_key
```

### 4. 启动服务

#### 方式一：使用 Maven

```bash
# 添加 JVM 参数启动
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

#### 方式二：打包后运行

```bash
# 打包
mvn clean package -DskipTests

# 运行 JAR 文件
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 5. 验证启动

应用默认运行在 `http://localhost:8080`

检查应用健康状态：
```bash
curl http://localhost:8080/actuator/health
```

## ⚙️ 配置说明

### 环境变量

支持通过环境变量覆盖配置：

| 环境变量 | 说明 | 默认值 |
|---------|------|-------|
| `SPRING_PROFILES_ACTIVE` | 激活的 Spring Profile | `local` |
| `ADMIN_USERNAME` | 管理员用户名 | `admin` |
| `ADMIN_PASSWORD` | 管理员密码 | `admin123` |
| `JWT_SECRET` | JWT 密钥 | 无默认值（必须配置） |
| `NACOS_ENABLED` | 是否启用 Nacos | `false` |

### Nacos 配置

如需启用 Nacos 服务注册与发现：

1. 启动 Nacos Server
2. 设置环境变量 `NACOS_ENABLED=true`
3. 在 `application.yaml` 中配置 Nacos 地址

## 📚 API 文档

### 主要 API 端点

#### 商品相关
- `GET /items` - 获取商品列表
- `GET /items/{id}` - 获取商品详情
- `POST /items` - 创建商品
- `PUT /items/{id}` - 更新商品
- `DELETE /items/{id}` - 删除商品

#### 分类相关
- `GET /categories` - 获取分类列表
- `GET /categories/{id}` - 获取分类详情

#### 品牌相关
- `GET /brands` - 获取品牌列表
- `POST /brands` - 创建品牌

#### 用户相关
- `POST /members/register` - 用户注册
- `POST /members/login` - 用户登录

> **注意**：需要认证的接口需要在请求头中携带 JWT Token：
> ```
> Authorization: Bearer <your-jwt-token>
> ```

## 🐳 Docker 部署

### 使用 Docker Compose 启动依赖服务

项目提供了 Docker Compose 配置文件，可快速启动 MySQL 和 Redis：

```bash
# 启动所有服务（MySQL + Redis）
docker-compose --profile all up -d

# 仅启动 MySQL
docker-compose --profile without-redis up -d

# 停止服务
docker-compose down
```

### 构建应用镜像

```bash
# 构建镜像
docker build -t online-store:latest .

# 运行容器
docker run -d -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JWT_SECRET=your_secret \
  --name online-store \
  online-store:latest
```

## 🔧 开发指南

### 代码规范

- 使用 Lombok 简化代码
- 遵循 RESTful API 设计规范
- 统一异常处理
- 使用 DTO 进行数据传输

### 分层架构

```
Controller -> Service -> Mapper -> Database
    ↓          ↓          ↓
   DTO       Entity     Entity
```

## 📝 License

This project is licensed under the MIT License.

## 👥 贡献

欢迎提交 Issue 和 Pull Request！

## 📧 联系方式

如有问题或建议，请通过 Issue 反馈。 