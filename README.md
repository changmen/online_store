# Online Store

基于 Spring Cloud 和 Spring Boot 3.x 的在线商店微服务项目，提供商品管理、会员管理、品牌管理等核心电商功能。

## 📋 目录

- [技术栈](#技术栈)
- [功能特性](#功能特性)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API文档](#api文档)
- [Docker部署](#docker部署)

## 🛠 技术栈

### 核心框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0

### 数据层
- **MySQL**: 8.2.0 (数据持久化)
- **Redis**: Jedis 5.2.0 (缓存)
- **MyBatis**: 3.0.2 (ORM框架)
- **PageHelper**: 2.1.0 (分页插件)

### 服务治理
- **Nacos**: 2.2.0 (服务注册与配置中心)

### 安全与认证
- **Spring Security**: 安全框架
- **JWT**: 0.11.5 (Token认证)

### 其他组件
- **Aliyun OSS**: 3.18.1 (对象存储)
- **Lombok**: 1.18.36 (简化代码)
- **Commons Lang3**: 3.17.0 (工具类)

## ✨ 功能特性

- 🛍️ **商品管理**: 商品信息、SKU管理、商品详情
- 🏷️ **分类管理**: 商品分类层级管理
- 🎨 **品牌管理**: 品牌信息维护
- 👤 **会员管理**: 会员注册、登录、信息管理
- 🔐 **安全认证**: JWT Token认证、Spring Security权限控制
- 📊 **访问日志**: 商品访问记录统计
- 🏗️ **属性管理**: 商品属性及属性值管理
- ☁️ **文件上传**: 基于阿里云OSS的文件存储

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用启动类
│   │   │   ├── controller/                     # REST控制器
│   │   │   │   ├── AttributeController.java
│   │   │   │   ├── BrandController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ItemController.java
│   │   │   │   ├── ItemDetailController.java
│   │   │   │   └── MemberController.java
│   │   │   ├── service/                        # 业务逻辑层
│   │   │   │   └── impl/                       # 服务实现
│   │   │   ├── mapper/                         # MyBatis Mapper接口
│   │   │   ├── entity/                         # 实体类
│   │   │   ├── dto/                            # 数据传输对象
│   │   │   ├── config/                         # 配置类
│   │   │   ├── security/                       # 安全配置
│   │   │   ├── utils/                          # 工具类
│   │   │   ├── enums/                          # 枚举类
│   │   │   ├── exceptions/                     # 异常处理
│   │   │   └── handler/                        # 全局处理器
│   │   └── resources/
│   │       ├── application.yaml                # 主配置文件
│   │       ├── application-local.yaml          # 本地环境配置
│   │       ├── bootstrap.yaml                  # 启动配置
│   │       ├── mapper/                         # MyBatis XML映射
│   │       ├── sql/                            # 数据库初始化脚本
│   │       └── i18n/                           # 国际化资源
│   └── test/                                   # 测试代码
├── scripts/                                     # Python脚本工具
├── docker-compose.yaml                          # Docker Compose配置
├── Dockerfile                                   # Docker镜像构建文件
└── pom.xml                                      # Maven项目配置
```

## 💻 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **Nacos**: 2.2.0 (可选，用于服务注册与配置)

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd online-store
```

### 2. 启动基础服务

#### 方式一：使用 Docker Compose (推荐)

```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 或仅启动 MySQL
docker-compose --profile without-redis up -d
```

#### 方式二：本地安装

确保 MySQL 和 Redis 服务已在本地启动并运行。

### 3. 初始化数据库

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 退出MySQL
exit;

# 导入数据表结构
mysql -u root -p online_store < src/main/resources/sql/category_table.sql
mysql -u root -p online_store < src/main/resources/sql/brand_table.sql
mysql -u root -p online_store < src/main/resources/sql/attribute_table.sql
mysql -u root -p online_store < src/main/resources/sql/attribute_value_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_table_table.sql
mysql -u root -p online_store < src/main/resources/sql/sku_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_attribute_relation_table.sql
mysql -u root -p online_store < src/main/resources/sql/member_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_access_log_table.sql
```

### 4. 配置应用

编辑 `src/main/resources/application-local.yaml` (或创建自定义配置文件)，修改数据库和Redis连接信息：

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
      password: your_redis_password  # 如果有密码
```

### 5. 配置环境变量

```bash
# 设置JWT密钥 (必需)
export JWT_SECRET="your-secret-key-here"

# 设置管理员账号 (可选，默认 admin/admin123)
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="admin123"

# 是否启用Nacos (可选，默认false)
export NACOS_ENABLED="false"
```

### 6. 编译和运行

```bash
# 编译项目
mvn clean package

# 运行应用 (添加必要的JVM参数)
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"

# 或使用java命令直接运行
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 7. 验证运行

应用启动后，访问：

- **应用地址**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health

## ⚙️ 配置说明

### 配置文件说明

- `bootstrap.yaml`: 启动阶段配置，Nacos配置中心连接信息
- `application.yaml`: 主配置文件，包含数据源、Redis、MyBatis等基础配置
- `application-local.yaml`: 本地开发环境配置

### 关键配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `server.port` | 应用端口 | 8080 |
| `spring.profiles.active` | 激活的配置文件 | local |
| `spring.cloud.nacos.discovery.enabled` | 是否启用Nacos服务发现 | false |
| `jwt.secret` | JWT签名密钥 | (环境变量) |
| `jwt.expiration` | Token过期时间(秒) | 86400 |

### 环境变量

| 变量名 | 说明 | 是否必需 |
|--------|------|----------|
| `JWT_SECRET` | JWT签名密钥 | 是 |
| `ADMIN_USERNAME` | 管理员用户名 | 否 (默认: admin) |
| `ADMIN_PASSWORD` | 管理员密码 | 否 (默认: admin123) |
| `NACOS_ENABLED` | 是否启用Nacos | 否 (默认: false) |
| `SPRING_PROFILES_ACTIVE` | 激活的配置环境 | 否 (默认: local) |

## 📚 API文档

### 主要接口模块

- **会员管理**: `/api/members`
- **商品管理**: `/api/items`
- **商品详情**: `/api/item-details`
- **品牌管理**: `/api/brands`
- **分类管理**: `/api/categories`
- **属性管理**: `/api/attributes`

## 🐳 Docker部署

### 构建Docker镜像

```bash
# 构建应用镜像
docker build -t online-store:latest .
```

### 使用Docker Compose部署完整环境

```bash
# 启动所有服务 (MySQL + Redis)
docker-compose --profile all up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

## 📝 开发说明

### JVM参数说明

由于项目使用了某些反射特性，需要在运行时添加以下JVM参数：

```bash
--add-opens java.base/java.lang=ALL-UNNAMED
```

### IDE配置

**IntelliJ IDEA**:
1. 打开 Run/Debug Configurations
2. 在 VM options 中添加: `--add-opens java.base/java.lang=ALL-UNNAMED`
3. 在 Environment variables 中添加: `JWT_SECRET=your-secret-key`

**Eclipse**:
1. Run Configurations → Arguments
2. VM arguments: `--add-opens java.base/java.lang=ALL-UNNAMED`
3. Environment: 添加 `JWT_SECRET` 变量

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

MIT License 