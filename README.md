# 🛒 Online Store - 在线商店管理系统

> 一个基于Spring Boot + Spring Cloud的现代化电商管理系统，提供完整的商品管理、会员管理、品牌管理等功能。

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 目录

- [功能特性](#-功能特性)
- [技术栈](#-技术栈)
- [项目架构](#-项目架构)
- [快速开始](#-快速开始)
- [环境配置](#-环境配置)
- [部署指南](#-部署指南)
- [API文档](#-api文档)
- [开发指南](#-开发指南)
- [许可证](#-许可证)

## ✨ 功能特性

### 核心功能
- 🏪 **商品管理** - 商品信息维护、商品详情管理、库存管理
- 🏷️ **品牌管理** - 品牌信息维护、品牌商品关联
- 📂 **分类管理** - 商品分类体系、层级分类管理
- 👥 **会员管理** - 用户注册登录、会员信息管理
- 🔧 **属性管理** - 商品属性定义、属性值管理

### 技术特性
- 🔐 **安全认证** - 基于JWT的用户认证与授权
- 📊 **数据持久化** - MyBatis + MySQL数据库操作
- ⚡ **缓存支持** - Redis缓存提升性能
- 🌐 **微服务架构** - Spring Cloud生态系统
- 📄 **分页查询** - PageHelper分页插件
- 🔧 **配置管理** - Nacos配置中心(可选)
- 🐳 **容器化部署** - Docker & Docker Compose支持

## 🛠 技术栈

### 后端技术
- **核心框架**: Spring Boot 3.4.3
- **微服务**: Spring Cloud 2024.0.0
- **数据访问**: MyBatis 3.0.3 + PageHelper 2.1.0
- **数据库**: MySQL 8.2.0
- **缓存**: Redis + Jedis 5.2.0
- **安全**: Spring Security + JWT 0.11.5
- **配置中心**: Nacos 2.2.0 (可选)
- **对象存储**: 阿里云OSS 3.18.1

### 开发工具
- **Java版本**: JDK 17
- **构建工具**: Maven 3.6+
- **代码简化**: Lombok 1.18.36
- **工具类**: Apache Commons Lang3 3.17.0
- **容器化**: Docker & Docker Compose

## 🏗 项目架构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java      # 应用启动类
│   ├── bean/                           # 配置Bean
│   ├── config/                         # 配置类
│   │   ├── SecurityConfig.java         # 安全配置
│   │   ├── RedisConfig.java           # Redis配置
│   │   └── ...
│   ├── controller/                     # 控制器层
│   │   ├── AttributeController.java    # 属性管理
│   │   ├── BrandController.java        # 品牌管理
│   │   ├── CategoryController.java     # 分类管理
│   │   ├── ItemController.java         # 商品管理
│   │   ├── ItemDetailController.java   # 商品详情
│   │   └── MemberController.java       # 会员管理
│   ├── service/                        # 业务逻辑层
│   ├── mapper/                         # 数据访问层
│   ├── entity/                         # 实体类
│   ├── dto/                           # 数据传输对象
│   ├── enums/                         # 枚举类
│   ├── security/                      # 安全相关
│   ├── utils/                         # 工具类
│   └── exceptions/                    # 异常处理
├── src/main/resources/
│   ├── application.yaml               # 主配置文件
│   ├── bootstrap.yaml                 # 启动配置
│   ├── mapper/                        # MyBatis映射文件
│   ├── sql/                          # 数据库脚本
│   └── i18n/                         # 国际化资源
├── scripts/                           # 脚本工具
├── docker-compose.yaml               # Docker编排文件
├── Dockerfile                        # Docker镜像构建
└── pom.xml                          # Maven配置
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本  
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **Docker**: 20.0+ (可选，用于容器化部署)

### 本地开发

1. **克隆项目**
```bash
git clone <repository-url>
cd online_store
```

2. **数据库初始化**
```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行初始化脚本 (位于 src/main/resources/sql/ 目录)
USE online_store;
source src/main/resources/sql/init.sql;
```

3. **配置环境变量**
```bash
# 设置JWT密钥
export JWT_SECRET="your-secret-key-here"

# 可选：设置管理员账号
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="your-password"
```

4. **启动依赖服务**
```bash
# 使用Docker Compose启动MySQL和Redis
docker-compose --profile all up -d

# 或手动启动MySQL和Redis服务
```

5. **运行应用**
```bash
# Maven方式
mvn clean spring-boot:run

# 或构建后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

6. **验证服务**
```bash
# 检查应用状态
curl http://localhost:8080/actuator/health

# 访问应用
open http://localhost:8080
```

## ⚙️ 环境配置

### 配置文件说明

| 配置文件 | 用途 | 环境 |
|---------|------|------|
| `application.yaml` | 主配置文件 | 所有环境 |
| `application-local.yaml` | 本地开发配置 | 本地开发 |
| `bootstrap.yaml` | 启动期配置 | 所有环境 |

### 关键配置项

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
    username: root
    password: 123456

# Redis配置
  data:
    redis:
      host: localhost
      port: 6379
      database: 0

# JWT配置
jwt:
  secret: ${JWT_SECRET}  # 必须设置环境变量
  expiration: 86400      # 24小时

# Nacos配置 (可选)
spring:
  cloud:
    nacos:
      discovery:
        enabled: ${NACOS_ENABLED:false}
```

### 环境变量

| 变量名 | 必需 | 默认值 | 说明 |
|--------|------|--------|------|
| `JWT_SECRET` | ✅ | - | JWT签名密钥 |
| `ADMIN_USERNAME` | ❌ | admin | 管理员用户名 |
| `ADMIN_PASSWORD` | ❌ | admin123 | 管理员密码 |
| `NACOS_ENABLED` | ❌ | false | 是否启用Nacos |
| `SPRING_PROFILES_ACTIVE` | ❌ | local | 激活的配置文件 |

## 🐳 部署指南

### Docker 部署

1. **使用Docker Compose (推荐)**
```bash
# 启动所有服务
docker-compose --profile all up -d

# 仅启动数据库服务
docker-compose --profile without-redis up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

2. **手动Docker部署**
```bash
# 构建应用镜像
docker build -t online-store:latest .

# 启动MySQL
docker run -d --name mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -e MYSQL_DATABASE=online_store \
  -p 3306:3306 mysql:8.0

# 启动Redis
docker run -d --name redis -p 6379:6379 redis:latest

# 启动应用
docker run -d --name online-store \
  -e JWT_SECRET=your-secret-key \
  -p 8080:8080 \
  --link mysql:mysql \
  --link redis:redis \
  online-store:latest
```

### 生产环境部署

1. **系统要求**
   - CPU: 2核心或以上
   - 内存: 4GB或以上
   - 存储: 20GB或以上
   - 操作系统: Linux (推荐Ubuntu 20.04+)

2. **部署步骤**
```bash
# 1. 安装JDK 17
sudo apt update
sudo apt install openjdk-17-jdk

# 2. 安装MySQL 8.0
sudo apt install mysql-server-8.0

# 3. 安装Redis
sudo apt install redis-server

# 4. 部署应用
mvn clean package -DskipTests
sudo mkdir -p /opt/online-store
sudo cp target/online-store-1.0-SNAPSHOT.jar /opt/online-store/

# 5. 创建systemd服务
sudo tee /etc/systemd/system/online-store.service > /dev/null <<EOF
[Unit]
Description=Online Store Application
After=network.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=/opt/online-store
ExecStart=/usr/bin/java --add-opens java.base/java.lang=ALL-UNNAMED -jar online-store-1.0-SNAPSHOT.jar
Restart=always
RestartSec=10
Environment=JWT_SECRET=your-production-secret
Environment=SPRING_PROFILES_ACTIVE=prod

[Install]
WantedBy=multi-user.target
EOF

# 6. 启动服务
sudo systemctl daemon-reload
sudo systemctl enable online-store
sudo systemctl start online-store
```

## 📚 API文档

### 接口概览

| 模块 | 基础路径 | 说明 |
|------|----------|------|
| 商品管理 | `/api/items` | 商品CRUD操作 |
| 商品详情 | `/api/item-details` | 商品详细信息管理 |
| 品牌管理 | `/api/brands` | 品牌信息管理 |
| 分类管理 | `/api/categories` | 商品分类管理 |
| 属性管理 | `/api/attributes` | 商品属性管理 |
| 会员管理 | `/api/members` | 用户会员管理 |

### 认证机制

```http
# 登录获取Token
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

# 使用Token访问受保护接口
GET /api/items
Authorization: Bearer <your-jwt-token>
```

### 示例请求

```bash
# 获取商品列表
curl -X GET "http://localhost:8080/api/items?page=1&size=10" \
  -H "Authorization: Bearer <token>"

# 创建新商品
curl -X POST "http://localhost:8080/api/items" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "name": "测试商品",
    "price": 99.99,
    "description": "这是一个测试商品"
  }'

# 获取品牌列表
curl -X GET "http://localhost:8080/api/brands" \
  -H "Authorization: Bearer <token>"
```

### 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "items": [...],
    "total": 100,
    "page": 1,
    "size": 10
  },
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## 👨‍💻 开发指南

### 代码规范

- 使用 **Java 17** 新特性
- 遵循 **Spring Boot** 最佳实践
- 使用 **Lombok** 减少样板代码
- 统一异常处理和响应格式
- 完善的单元测试覆盖

### 项目结构说明

```
控制器层 (Controller) -> 业务逻辑层 (Service) -> 数据访问层 (Mapper) -> 数据库
                   ↓
               DTO/Entity 数据传输
```

### 开发流程

1. **创建分支**
```bash
git checkout -b feature/your-feature-name
```

2. **开发新功能**
   - 创建Entity实体类
   - 编写Mapper接口和XML
   - 实现Service业务逻辑
   - 开发Controller接口
   - 编写单元测试

3. **测试验证**
```bash
# 运行测试
mvn test

# 检查代码覆盖率
mvn jacoco:report
```

4. **提交代码**
```bash
git add .
git commit -m "feat: 添加xxx功能"
git push origin feature/your-feature-name
```

### 常用命令

```bash
# 清理构建
mvn clean

# 编译项目
mvn compile

# 运行测试
mvn test

# 打包应用
mvn package

# 跳过测试打包
mvn package -DskipTests

# 运行应用
mvn spring-boot:run

# 生成项目报告
mvn site
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

### 提交信息规范

- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建/工具相关

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE) - 查看 `LICENSE` 文件了解详情

## 📞 支持与反馈

- 🐛 **Bug报告**: [GitHub Issues](https://github.com/your-repo/online-store/issues)
- 💡 **功能建议**: [GitHub Discussions](https://github.com/your-repo/online-store/discussions)
- 📧 **邮件联系**: your-email@example.com

---

<div align="center">
  <p>如果这个项目对你有帮助，请给个 ⭐️ Star 支持一下！</p>
  <p>Made with ❤️ by [Your Name]</p>
</div>