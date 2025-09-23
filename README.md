# 🛒 Online Store - 在线商店系统

基于 Spring Boot 3.x 和 Spring Cloud 构建的现代化电商后端系统，采用微服务架构设计，提供完整的商品管理、用户管理、权限控制等核心功能。

## ✨ 核心特性

- 🔐 **JWT 身份认证** - 基于 Spring Security 的安全认证机制
- 📦 **商品管理** - 完整的商品、品牌、分类、属性管理体系
- 👥 **用户管理** - 用户注册、登录、个人信息管理
- 🏷️ **灵活属性系统** - 支持多种商品属性配置
- 📊 **访问日志** - 商品访问行为追踪
- 🌐 **国际化支持** - 多语言消息配置
- 🐳 **容器化部署** - Docker 和 Docker Compose 支持
- ☁️ **云原生** - 支持 Nacos 服务发现和配置中心
- 📦 **对象存储** - 集成阿里云 OSS 文件存储

## 🛠️ 技术栈

### 后端框架
- **Java 17** - 现代 Java 开发平台
- **Spring Boot 3.4.3** - 企业级应用开发框架
- **Spring Cloud 2024.0.0** - 微服务解决方案
- **Spring Security** - 安全认证与授权
- **Spring Data Redis** - Redis 数据访问

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis 5.2.0** - 内存数据库和缓存
- **MyBatis 3.0.2** - 持久层框架
- **PageHelper 2.1.0** - 分页插件

### 服务治理
- **Nacos 2.2.0** - 服务发现与配置管理
- **Spring Cloud Alibaba** - 阿里云微服务组件

### 工具库
- **Lombok** - 简化 Java 代码
- **JJWT 0.11.5** - JWT 令牌处理
- **Apache Commons** - 通用工具库
- **Jackson** - JSON 序列化
- **Aliyun OSS 3.18.1** - 阿里云对象存储

## 🏗️ 项目架构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── 📁 bean/           # 业务对象模型
│   ├── 📁 config/         # 配置类
│   ├── 📁 controller/     # REST API 控制器
│   ├── 📁 dto/           # 数据传输对象
│   ├── 📁 entity/        # 数据库实体
│   ├── 📁 enums/         # 枚举定义
│   ├── 📁 exceptions/    # 异常处理
│   ├── 📁 handler/       # 全局异常处理器
│   ├── 📁 mapper/        # MyBatis 映射器
│   ├── 📁 security/      # 安全认证组件
│   ├── 📁 service/       # 业务逻辑层
│   ├── 📁 utils/         # 工具类
│   └── 📄 OnlineStoreApplication.java
├── src/main/resources/
│   ├── 📁 i18n/          # 国际化消息
│   ├── 📁 mapper/        # MyBatis XML 映射文件
│   ├── 📁 sql/           # 数据库初始化脚本
│   ├── 📄 application.yaml
│   ├── 📄 application-local.yaml
│   └── 📄 bootstrap.yaml
├── 📁 scripts/           # Python 辅助脚本
├── 🐳 Dockerfile        # 容器镜像构建
├── 🐳 docker-compose.yaml # 容器编排
└── 📄 pom.xml           # Maven 项目配置
```

## 🗄️ 数据模型

系统包含以下核心数据模型：

- **Member** - 用户信息管理
- **Item** - 商品基础信息
- **Sku** - 商品库存单位
- **Category** - 商品分类
- **Brand** - 商品品牌
- **Attribute** - 商品属性定义
- **AttributeValue** - 属性值
- **ItemAttributeRelation** - 商品属性关联
- **ItemAccessLog** - 商品访问日志

## 🚀 快速开始

### 环境要求

- ☕ **JDK 17+** - Java 开发环境
- 🔧 **Maven 3.6+** - 项目构建工具
- 🐬 **MySQL 8.0+** - 数据库服务
- 🔴 **Redis 6.0+** - 缓存服务
- 🐳 **Docker** (可选) - 容器化部署

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

-- 执行初始化脚本（按顺序执行 src/main/resources/sql/ 目录下的所有 .sql 文件）
```

#### 3. 环境变量配置
创建 `.env` 文件或设置以下环境变量：
```bash
# JWT 密钥（必须设置）
export JWT_SECRET="your-jwt-secret-key-at-least-256-bits"

# 可选配置
export SPRING_PROFILES_ACTIVE=local
export NACOS_ENABLED=false
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
```

#### 4. 配置文件调整
根据本地环境修改 `src/main/resources/application-local.yaml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_mysql_username
    password: your_mysql_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password  # 如果Redis设置了密码
```

#### 5. 启动应用
```bash
# 方式一：使用 Maven
mvn clean spring-boot:run

# 方式二：打包后运行
mvn clean package -DskipTests
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 🐳 Docker 部署

#### 使用 Docker Compose（推荐）
```bash
# 启动完整环境（包含 MySQL 和 Redis）
docker-compose --profile all up -d

# 仅启动 MySQL
docker-compose --profile without-redis up -d
```

#### 手动 Docker 部署
```bash
# 构建镜像
docker build -t online-store:latest .

# 运行容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e JWT_SECRET="your-jwt-secret" \
  -e SPRING_PROFILES_ACTIVE=local \
  online-store:latest
```

## 🔧 API 接口

应用启动后，可以访问以下接口：

- **应用首页**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **API 文档**: 详见各 Controller 类的接口定义

### 主要接口模块

| 模块 | 控制器 | 功能描述 |
|------|--------|----------|
| 用户管理 | `MemberController` | 用户注册、登录、信息管理 |
| 商品管理 | `ItemController` | 商品CRUD操作 |
| 商品详情 | `ItemDetailController` | 商品详细信息查询 |
| 品牌管理 | `BrandController` | 品牌信息管理 |
| 分类管理 | `CategoryController` | 商品分类管理 |
| 属性管理 | `AttributeController` | 商品属性配置 |

## 🛡️ 安全配置

系统使用 JWT 进行身份认证：

1. **注册/登录** 获取 JWT Token
2. **请求头** 携带 `Authorization: Bearer <token>`
3. **Token 过期时间** 默认 24 小时（可配置）

## 📊 监控与运维

- **应用监控**: Spring Boot Actuator
- **日志管理**: 基于 Logback
- **访问统计**: 商品访问日志记录
- **健康检查**: `/actuator/health`

## 🤝 开发指南

### 代码规范
- 使用 Lombok 简化代码
- 统一异常处理机制
- RESTful API 设计
- 分层架构（Controller → Service → Mapper）

### 扩展功能
- 新增业务模块请参考现有 Controller 和 Service 结构
- 数据库变更请在 `sql/` 目录添加对应脚本
- 配置项统一在 `application.yaml` 中管理

## 📝 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 🙋‍♂️ 支持

如有问题或建议，请提交 Issue 或 Pull Request。 