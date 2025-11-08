# Online Store - 现代化电商平台

# Online Store - 现代化电商平台

<div align="center">

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

</div>

## 📖 项目介绍

Online Store 是一个基于 **Spring Boot 3.4.3** 和 **Spring Cloud 2024.0.0** 构建的现代化电商平台后端服务。该项目采用微服务架构设计，提供完整的商品管理、用户管理、分类管理、品牌管理等核心电商功能，支持高并发和可扩展性。

### 🎯 核心价值

- **现代化技术栈**：基于最新的 Spring Boot 3.x 和 Spring Cloud 2024.x
- **微服务架构**：支持服务拆分和独立部署
- **高性能缓存**：集成 Redis 提升查询性能
- **安全认证**：基于 JWT 的用户认证和授权机制
- **容器化支持**：提供 Docker 和 Docker Compose 部署方案
- **云原生就绪**：支持 Nacos 配置管理和服务发现

### 🚀 应用场景

- 中小型电商平台后端服务
- 微服务架构学习和实践项目
- Spring Boot 3.x 技术栈示例项目
- 企业级应用开发参考模板

## 🛠 技术栈

### 核心框架

| 技术 | 版本 | 说明 |
|------|------|------|
| **JDK** | 17+ | Java 开发工具包 |
| **Spring Boot** | 3.4.3 | 应用开发框架 |
| **Spring Cloud** | 2024.0.0 | 微服务框架 |
| **Spring Security** | 6.x | 安全认证框架 |
| **MyBatis** | 3.0.3 | ORM 数据访问框架 |

### 数据存储

| 技术 | 版本 | 说明 |
|------|------|------|
| **MySQL** | 8.2.0 | 关系型数据库 |
| **Redis** | 6.0+ | 内存数据库/缓存 |
| **Jedis** | 5.2.0 | Redis Java 客户端 |

### 中间件与工具

| 技术 | 版本 | 说明 |
|------|------|------|
| **Nacos** | 2.2.0 | 配置管理/服务发现 |
| **JWT** | 0.11.5 | JSON Web Token |
| **PageHelper** | 2.1.0 | MyBatis 分页插件 |
| **阿里云 OSS** | 3.18.1 | 对象存储服务 |


## ✨ 功能特性

### 🛍️ 业务功能模块

| 模块 | 功能描述 | 核心特性 |
|------|----------|----------|
| **商品管理** | 商品信息、SKU管理、库存控制 | 多规格商品、库存实时更新 |
| **分类管理** | 商品分类层级结构管理 | 树形分类结构、无限级分类 |
| **品牌管理** | 品牌信息维护和查询 | 品牌展示、品牌关联商品 |
| **属性管理** | 商品属性定义和属性值管理 | 动态属性、属性值组合 |
| **用户管理** | 会员注册、登录、信息管理 | JWT认证、用户权限管理 |
| **商品详情** | 商品详细信息展示服务 | 商品详情聚合、访问日志 |

### 🔧 技术支撑模块

| 模块 | 技术实现 | 作用 |
|------|----------|------|
| **安全认证** | Spring Security + JWT | 用户身份验证和授权 |
| **数据持久化** | MyBatis + MySQL | 数据存储和查询优化 |
| **缓存服务** | Redis + Jedis | 提升查询性能 |
| **配置管理** | Nacos Config | 动态配置管理 |
| **服务发现** | Nacos Discovery | 微服务注册发现 |
| **对象存储** | 阿里云OSS | 文件存储服务 |

## 🏗️ 系统架构

### 分层架构

```
┌─────────────────────────────────────────────────────┐
│                  Web Layer                      │
│              (Controller)                       │
├─────────────────────────────────────────────────────┤
│                Business Layer                   │
│            (Service & ServiceImpl)              │
├─────────────────────────────────────────────────────┤
│               Persistence Layer                 │
│            (Mapper & MyBatis)                   │
├─────────────────────────────────────────────────────┤
│                Database Layer                   │
│               (MySQL & Redis)                   │
└─────────────────────────────────────────────────────┘
```

### 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # 启动类
│   │   │   ├── bean/                           # 业务对象
│   │   │   ├── config/                         # 配置类
│   │   │   ├── controller/                     # 控制层
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── entity/                        # 数据库实体
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── mapper/                        # 数据访问层
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   ├── security/                      # 安全相关
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── mapper/                        # MyBatis映射文件
│   │       ├── sql/                          # 数据库脚本
│   │       ├── i18n/                         # 国际化文件
│   │       ├── application.yaml              # 主配置文件
│   │       ├── application-local.yaml        # 本地配置
│   │       └── bootstrap.yaml                # 启动配置
│   └── test/                                  # 测试代码
├── scripts/                                   # Python脚本
├── docker-compose.yaml                        # Docker编排文件
├── Dockerfile                                 # Docker构建文件
└── pom.xml                                   # Maven配置文件
```

## 🚀 快速开始

### 环境要求

| 工具 | 版本要求 | 说明 |
|------|----------|------|
| **JDK** | 17+ | 支持 Oracle JDK 或 OpenJDK |
| **Maven** | 3.6+ | 用于依赖管理和构建 |
| **MySQL** | 8.0+ | 数据存储 |
| **Redis** | 6.0+ | 缓存服务 |
| **Git** | 2.0+ | 版本控制 |

### 快速启动（推荐）

#### 方式一：使用 Docker Compose（推荐）

```bash
# 1. 克隆项目
git clone <repository-url>
cd online_store

# 2. 启动基础服务（MySQL + Redis）
docker-compose --profile all up -d

# 3. 设置环境变量
export JWT_SECRET=your-jwt-secret-key-here
export MYSQL_PASSWORD=123456

# 4. 初始化数据库
mysql -h localhost -u root -p123456 -e "CREATE DATABASE IF NOT EXISTS online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 5. 执行数据库脚本
for sql_file in src/main/resources/sql/*.sql; do
    mysql -h localhost -u root -p123456 online_store < "$sql_file"
done

# 6. 启动应用
mvn spring-boot:run
```

#### 方式二：本地环境启动

```bash
# 1. 确保 MySQL 和 Redis 服务已启动
sudo systemctl start mysql
sudo systemctl start redis

# 2. 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 3. 执行 SQL 脚本初始化表结构
for sql_file in src/main/resources/sql/*.sql; do
    mysql -u root -p online_store < "$sql_file"
done

# 4. 设置必需的环境变量
export JWT_SECRET=mySecretKey123456789
export MYSQL_PASSWORD=your_mysql_password

# 5. 启动应用
mvn clean spring-boot:run
```

### 验证启动

启动成功后，可以通过以下方式验证：

```bash
# 检查应用健康状态
curl http://localhost:8080/actuator/health

# 查看应用信息
curl http://localhost:8080/actuator/info
```

期望返回：
```json
{
  "status": "UP"
}
```

## 📚 API 文档

### 认证接口

#### 用户登录
```http
POST /api/members/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**响应示例：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiration": 86400
  }
}
```

### 商品接口

#### 获取商品列表
```http
GET /api/items?page=1&size=10&keyword=手机
Authorization: Bearer {token}
```

#### 获取商品详情
```http
GET /api/items/{itemId}
Authorization: Bearer {token}
```

### 错误码说明

| 错误码 | 说明 | 解决方案 |
|--------|------|----------|
| `200` | 成功 | - |
| `400` | 请求参数错误 | 检查请求参数格式 |
| `401` | 未授权 | 提供有效的JWT令牌 |
| `403` | 权限不足 | 检查用户权限 |
| `404` | 资源不存在 | 检查资源ID是否正确 |
| `500` | 服务器内部错误 | 查看服务器日志 |

## 🚢 部署指南

### Docker 部署

#### 构建镜像

```bash
# 构建应用镜像
docker build -t online-store:latest .

# 查看构建的镜像
docker images | grep online-store
```

#### Docker Compose 部署（推荐）

```bash
# 启动所有服务
docker-compose --profile all up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f online-store

# 停止服务
docker-compose down
```

### ⚙️ 配置说明

### 环境变量

| 变量名 | 默认值 | 说明 | 必需性 |
|--------|--------|------|--------|
| `SPRING_PROFILES_ACTIVE` | `local` | 激活的配置文件 | 建议设置 |
| `JWT_SECRET` | - | JWT签名密钥（生产环境必须设置） | **必需** |
| `MYSQL_PASSWORD` | `123456` | MySQL数据库密码 | 生产环境必需 |
| `ADMIN_USERNAME` | `admin` | 默认管理员用户名 | 可选 |
| `ADMIN_PASSWORD` | `admin123` | 默认管理员密码 | 生产环境必需 |
| `NACOS_ENABLED` | `false` | 是否启用Nacos服务 | 可选 |
| `SERVER_PORT` | `8080` | 应用服务端口 | 可选 |

### 数据库配置

#### MySQL 配置优化

```sql
-- 创建数据库
CREATE DATABASE online_store 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 创建应用用户（生产环境推荐）
CREATE USER 'online_store'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON online_store.* TO 'online_store'@'%';
FLUSH PRIVILEGES;
```

## 🔧 故障排除

### 常见启动问题

#### 1. 数据库连接失败

**错误信息：**
```
Communications link failure
```

**解决方案：**
```bash
# 检查 MySQL 服务状态
sudo systemctl status mysql

# 检查端口是否开放
telnet localhost 3306

# 检查数据库配置
mysql -u root -p -e "SHOW DATABASES;"
```

#### 2. Redis 连接失败

**错误信息：**
```
Connection refused: no further information
```

**解决方案：**
```bash
# 检查 Redis 服务状态
sudo systemctl status redis

# 测试 Redis 连接
redis-cli ping

# 检查 Redis 配置
redis-cli CONFIG GET bind
```

#### 3. JWT 密钥未配置

**错误信息：**
```
JWT secret cannot be null or empty
```

**解决方案：**
```bash
# 设置环境变量
export JWT_SECRET="your-secret-key-at-least-32-characters"

# 或在配置文件中设置
echo "jwt.secret=your-secret-key" >> application-local.yaml
```

## 🤝 贡献指南

我们欢迎所有形式的贡献，包括但不限于：

- 🐛 Bug 修复
- ✨ 新功能开发
- 📝 文档完善
- 🎨 代码优化
- 🧪 测试用例

### 开发流程

```bash
# 1. Fork 项目到你的 GitHub 账户

# 2. 克隆你的 Fork
git clone https://github.com/your-username/online_store.git
cd online_store

# 3. 创建功能分支
git checkout -b feature/your-feature-name

# 4. 进行开发并提交
git add .
git commit -m "feat: add your feature"

# 5. 推送到你的 Fork
git push origin feature/your-feature-name

# 6. 创建 Pull Request
```

### 提交规范

- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式化
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

## 📄 许可证

本项目采用 [MIT License](LICENSE) 开源协议。

## 📞 联系我们

- 项目主页：[GitHub Repository](https://github.com/your-username/online_store)
- 问题反馈：[Issues](https://github.com/your-username/online_store/issues)
- 讨论交流：[Discussions](https://github.com/your-username/online_store/discussions)

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给它一个星标！**

Made with ❤️ by [Your Name](https://github.com/your-username)

</div>