# 📱 在线商店系统 (Online Store)

> 基于 Spring Boot + MyBatis 构建的现代化电商平台，支持商品管理、用户管理、属性管理等核心功能。

## 🚀 项目简介

这是一个功能完整的在线商店后端系统，采用微服务架构设计，提供RESTful API接口，支持商品分类、品牌管理、用户认证、商品属性配置等电商核心业务功能。

## 🛠️ 技术栈

### 后端框架
- **Java**: JDK 17
- **Spring Boot**: 3.1.5
- **Spring Cloud**: 2022.0.4
- **Spring Security**: JWT认证

### 数据存储
- **数据库**: MySQL 8.0
- **缓存**: Redis (Jedis 4.3.1)
- **ORM**: MyBatis 3.0.2

### 开发工具
- **构建工具**: Maven
- **容器化**: Docker & Docker Compose

## 📁 项目结构

### 🏢 核心模块

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── 🔌 OnlineStoreApplication.java    # 应用主类
│   ├── 🌐 controller/                   # REST API 控制器
│   │   ├── ItemController.java         # 商品管理
│   │   ├── MemberController.java       # 用户管理
│   │   ├── CategoryController.java     # 分类管理
│   │   └── BrandController.java        # 品牌管理
│   ├── ⚙️ service/                      # 业务逻辑层
│   ├── 🗃️ mapper/                       # 数据访问层
│   ├── 📦 entity/                       # 数据实体
│   ├── 📦 dto/                          # 数据传输对象
│   ├── 🔒 security/                     # 安全认证
│   └── 🛠️ config/                       # 配置类
├── src/main/resources/
│   ├── application.yaml              # 主配置文件
│   ├── mapper/                       # MyBatis XML映射文件
│   └── sql/                          # 数据库初始化脚本
└── 🚀 docker-compose.yaml            # Docker 编排文件
```

## ✅ 运行环境要求

### 🔧 基础要求
- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0
- **Redis**: 6.0 或更高版本

## 🚀 快速开始

### 1️⃣ 环境准备

首先确保 MySQL 和 Redis 服务已启动：

```bash
# 使用 Docker Compose 一键启动所有依赖服务
docker-compose up -d
```

### 2️⃣ 数据库初始化

创建数据库：
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
### 3️⃣ 配置修改

修改 `src/main/resources/application-local.yaml` 中的数据库和 Redis 配置。

### 4️⃣ 运行应用

添加 JVM 参数并启动应用：
```bash
# 方式1：使用 Maven 运行
export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"
mvn spring-boot:run

# 方式2：编译后运行
mvn clean package -DskipTests
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-*.jar
```

### 5️⃣ 验证运行

访问以下地址验证服务是否正常启动：
- 🌐 **API 基础地址**: http://localhost:8080
- 📊 **健康检查**: http://localhost:8080/actuator/health

---

## 📚 更多信息

- 📄 **API 文档**: 查看 `controller` 包下的各个控制器
- 📋 **数据库脚本**: 查看 `src/main/resources/sql/` 目录
- ⚙️ **配置说明**: 查看 `src/main/resources/application*.yaml`
- 🐳 **Docker 部署**: 使用 `docker-compose.yaml` 一键部署 