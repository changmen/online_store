# 🛒 Online Store - 在线商店系统

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue)
![Redis](https://img.shields.io/badge/Redis-6.0+-red)
![License](https://img.shields.io/badge/License-MIT-yellow)

一个基于Spring Cloud微服务架构的现代化在线商店系统，集成了商品管理、用户管理、订单处理等核心电商功能。

</div>

## ✨ 功能特性

- 🛍️ **商品管理**：支持商品分类、品牌管理、商品属性配置
- 👥 **用户系统**：用户注册、登录、权限管理，基于JWT的安全认证
- 🏷️ **商品展示**：多规格商品(SKU)管理，商品详情展示
- 📊 **数据分析**：商品访问日志记录和分析
- 🔍 **搜索功能**：基于分类和属性的商品搜索
- 🐳 **容器化部署**：支持Docker和Docker Compose一键部署
- ☁️ **微服务架构**：基于Nacos的服务发现和配置管理
- 🔄 **缓存系统**：Redis缓存提升系统性能

## 🛠️ 技术栈

### 后端技术
- **JDK 17** - Java开发环境
- **Spring Boot 3.4.3** - 应用开发框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全认证框架
- **MyBatis 3.0.3** - 数据持久层框架
- **PageHelper 2.1.0** - MyBatis分页插件

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis 6.0+** - 缓存数据库
- **Jedis 5.2.0** - Redis Java客户端

### 服务治理
- **Nacos 2.2.0** - 服务发现与配置管理
- **Spring Cloud Alibaba** - 阿里云微服务组件

### 工具库
- **JWT (JJWT 0.11.5)** - JSON Web Token
- **Lombok** - Java代码简化
- **Apache Commons Lang3** - 工具类库
- **Jackson** - JSON处理

## 📚 项目结构

```
online-store/
├── 📦 src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用主入口
│   │   │   ├── 🎮 controller/                # Web控制器层
│   │   │   │   ├── AttributeController.java   # 属性管理接口
│   │   │   │   ├── BrandController.java       # 品牌管理接口
│   │   │   │   ├── CategoryController.java    # 分类管理接口
│   │   │   │   ├── ItemController.java        # 商品管理接口
│   │   │   │   ├── ItemDetailController.java  # 商品详情接口
│   │   │   │   └── MemberController.java      # 用户管理接口
│   │   │   ├── ⚙️ service/                    # 业务逻辑层
│   │   │   ├── 🗾 mapper/                     # 数据访问层
│   │   │   ├── 🏢 entity/                     # 实体类
│   │   │   │   ├── AttributeEntity.java       # 属性实体
│   │   │   │   ├── BrandEntity.java           # 品牌实体
│   │   │   │   ├── CategoryEntity.java        # 分类实体
│   │   │   │   ├── ItemEntity.java            # 商品实体
│   │   │   │   ├── MemberEntity.java          # 用户实体
│   │   │   │   └── SkuEntity.java             # SKU实体
│   │   │   ├── 📎 dto/                       # 数据传输对象
│   │   │   ├── 🔐 security/                  # 安全配置
│   │   │   ├── ⚙️ config/                    # 配置类
│   │   │   ├── 🚫 exceptions/               # 异常处理
│   │   │   ├── ✨ enums/                     # 枚举类
│   │   │   └── 🛠️ utils/                     # 工具类
│   │   └── resources/
│   │       ├── application.yaml           # 主配置文件
│   │       ├── bootstrap.yaml             # 启动配置
│   │       └── mapper/                   # MyBatis XML映射文件
│   └── test/                           # 测试代码
├── 🚀 scripts/                        # 脚本目录
│   ├── main.py                       # 数据生成脚本
│   └── README.md                     # 脚本说明
├── 🐳 docker-compose.yaml             # Docker编排文件
├── 🐳 Dockerfile                      # Docker构建文件
├── 📦 pom.xml                         # Maven配置文件
└── 📄 README.md                       # 项目说明文档
```

## 📦 核心模块

### 🛍️ 商品管理模块
- **商品信息管理**：商品基本信息、价格、库存管理
- **SKU管理**：多规格商品管理，支持不同属性组合
- **分类管理**：分级分类系统，支持无限级分类
- **品牌管理**：品牌信息管理和展示
- **属性管理**：商品属性定义和属性值管理

### 👥 用户管理模块
- **用户注册/登录**：支持邮箱、手机号注册
- **JWT认证**：无状态认证，支持Token刷新
- **权限管理**：基于Spring Security的权限控制
- **用户信息**：用户资料管理和更新

### 📊 数据统计模块
- **访问日志**：商品访问情况记录和统计
- **热门商品**：基于访问量的热门商品排行
- **用户行为**：用户访问路径和行为分析

## ⚙️ 环境要求

### 基本要求
- **JDK 17+** - Java开发环境
- **Maven 3.6+** - 项目构建工具
- **Git** - 版本控制工具

### 数据库要求
- **MySQL 8.0+** - 主数据库
- **Redis 6.0+** - 缓存数据库

### 可选组件
- **Nacos 2.2.0+** - 服务注册与配置中心（生产环境推荐）
- **Docker & Docker Compose** - 容器化部署

## 🚀 快速开始

### 方式一：本地开发环境

#### 1. 克隆仓库
```bash
git clone https://github.com/your-username/online-store.git
cd online-store
```

#### 2. 环境准备
启动MySQL服务并创建数据库：
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

启动Redis服务：
```bash
# Ubuntu/Debian
sudo systemctl start redis-server

# macOS
brew services start redis

# Windows
redis-server
```

#### 3. 配置修改
修改 `src/main/resources/application.yaml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root        # 修改为你的MySQL用户名
    password: 123456      # 修改为你的MySQL密码
  data:
    redis:
      host: localhost     # Redis主机地址
      port: 6379          # Redis端口
      password:           # Redis密码（如果有）
```

#### 4. 设置环境变量
```bash
# 设置JWT密钥
export JWT_SECRET="your-secret-key-here-should-be-at-least-256-bits"

# 或者在application.yaml中配置
jwt:
  secret: your-secret-key-here-should-be-at-least-256-bits
```

#### 5. 构建和运行
```bash
# 清理和编译
mvn clean compile

# 运行应用 
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

应用启动成功后，访问 http://localhost:8080

### 方式二：Docker Compose 一键部署

```bash
# 启动所有服务（MySQL + Redis + 应用）
docker-compose --profile all up -d

# 或者只启动数据库服务
docker-compose --profile without-redis up -d  # 只启动MySQL
docker-compose --profile all up mysql redis    # 启动MySQL和Redis
```

## 📚 API 文档

### 认证相关 API

| 方法 | 路径 | 说明 | 请求体 |
|------|------|------|--------|
| POST | `/api/auth/login` | 用户登录 | `{"username": "admin", "password": "admin123"}` |
| POST | `/api/auth/register` | 用户注册 | `{"username": "user", "password": "pass", "email": "user@example.com"}` |
| POST | `/api/auth/refresh` | 刷新Token | `{"refreshToken": "..."}` |

### 商品管理 API

| 方法 | 路径 | 说明 | 权限要求 |
|------|------|------|----------|
| GET | `/api/items` | 获取商品列表 | - |
| GET | `/api/items/{id}` | 获取商品详情 | - |
| POST | `/api/items` | 创建商品 | ADMIN |
| PUT | `/api/items/{id}` | 更新商品 | ADMIN |
| DELETE | `/api/items/{id}` | 删除商品 | ADMIN |

### 分类管理 API

| 方法 | 路径 | 说明 | 权限要求 |
|------|------|------|----------|
| GET | `/api/categories` | 获取分类列表 | - |
| POST | `/api/categories` | 创建分类 | ADMIN |
| PUT | `/api/categories/{id}` | 更新分类 | ADMIN |
| DELETE | `/api/categories/{id}` | 删除分类 | ADMIN |

### 品牌管理 API

| 方法 | 路径 | 说明 | 权限要求 |
|------|------|------|----------|
| GET | `/api/brands` | 获取品牌列表 | - |
| POST | `/api/brands` | 创建品牌 | ADMIN |
| PUT | `/api/brands/{id}` | 更新品牌 | ADMIN |
| DELETE | `/api/brands/{id}` | 删除品牌 | ADMIN |

### 用户管理 API

| 方法 | 路径 | 说明 | 权限要求 |
|------|------|------|----------|
| GET | `/api/members/profile` | 获取用户信息 | USER |
| PUT | `/api/members/profile` | 更新用户信息 | USER |
| GET | `/api/members` | 获取用户列表 | ADMIN |

### 响应格式

所有 API 返回都遵循统一格式：
```json
{
  "code": 200,
  "message": "success",
  "data": { 
    // 具体数据
  },
  "timestamp": "2024-01-01T12:00:00Z"
}
```

### 请求示例

登录获取Token：
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

使用Token访问受保护的API：
```bash
curl -X GET http://localhost:8080/api/items \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🛠️ 开发指南

### 代码规范
- 使用 **Google Java Style Guide**
- 所有公共方法必须添加Javadoc注释
- 使用Lombok减少样板代码
- 统一使用`@RestController`和`@RequestMapping`注解

### 数据库设计
- 表名和字段名使用下划线命名法
- 所有表必须包含 `id`、`created_time`、`updated_time` 字段
- 使用逻辑删除，增加 `deleted` 字段
- 外键约束命名格式：`fk_{table}_{column}`

### 分支管理
```bash
# 主分支
main        # 生产环境代码
develop     # 开发主分支

# 功能分支
feature/feature-name    # 新功能开发
hotfix/bug-fix         # 紧急修复
release/v1.0.0         # 版本发布
```

### 本地开发设置

1. **IDE 推荐配置**
   - IntelliJ IDEA + Lombok插件
   - Enable Annotation Processing
   - 安装 MyBatis插件用于XML和Java跳转

2. **代码格式化**
   ```bash
   # 安装代码格式化工具
   mvn com.spotify.fmt:fmt-maven-plugin:format
   ```

3. **数据库初始化**
   运行 `scripts/` 目录下的Python脚本生成测试数据：
   ```bash
   cd scripts
   pip install -r requirements.txt
   python main.py
   ```

### 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 生成测试报告
mvn jacoco:report
```

## 🐳 Docker 部署

### 构建镜像

```bash
# 构建Spring Boot应用镜像
docker build -t online-store:latest .

# 或者使用Maven构建
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=online-store:latest
```

### 使用Docker Compose

```bash
# 完整部署（包含数据库）
docker-compose --profile all up -d

# 查看运行状态
docker-compose ps

# 查看日志
docker-compose logs -f online-store

# 停止服务
docker-compose down
```

### 环境变量配置

在生产环境中，可以通过环境变量进行配置：

```bash
# 数据库配置
export DB_HOST=mysql-server
export DB_PORT=3306
export DB_NAME=online_store
export DB_USERNAME=root
export DB_PASSWORD=your_password

# Redis配置
export REDIS_HOST=redis-server
export REDIS_PORT=6379
export REDIS_PASSWORD=your_redis_password

# JWT配置
export JWT_SECRET=your-256-bit-secret-key

# Nacos配置
export NACOS_ENABLED=true
export NACOS_SERVER_ADDR=nacos-server:8848
```

### 生产环境部署建议

1. **使用外部数据库**：不要在生产环境中使用Docker容器运行MySQL
2. **配置负载均衡**：使用Nginx或HAProxy作为反向代理
3. **启用HTTPS**：配置SSL证书保障安全
4. **监控告警**：集成Prometheus + Grafana进行监控

## 🤝 贡献指南

欢迎任何形式的贡献！请遵循以下步骤：

### 贡献流程

1. **Fork 项目**
   ```bash
   # 点击 GitHub 页面上的 Fork 按钮
   git clone https://github.com/your-username/online-store.git
   cd online-store
   ```

2. **创建功能分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **进行修改**
   - 遵循代码规范
   - 添加必要的测试
   - 更新相关文档

4. **提交修改**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   git push origin feature/your-feature-name
   ```

5. **创建 Pull Request**
   - 在 GitHub 上创建Byte Pull Request
   - 详细描述修改内容
   - 等待 Code Review

### 代码提交规范

使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

- `feat`: 新功能
- `fix`: 修复 bug
- `docs`: 文档更新
- `style`: 代码格式化
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

示例：
```
feat: add user avatar upload functionality
fix: resolve JWT token expiration issue  
docs: update API documentation for v2.0
```

### Issue 提交

在提交 Issue 时，请包含：

- **问题描述**：清晰描述问题
- **复现步骤**：如何复现该问题
- **期望行为**：你期望发生什么
- **实际行为**：实际发生了什么
- **环境信息**：Java版本、操作系统等

### 开发环境搭建

为了保证代码质量，请配置以下工具：

```bash
# 安装 pre-commit hooks
pip install pre-commit
pre-commit install

# 代码格式化检查
mvn spotless:check
mvn spotless:apply

# 静态代码分析
mvn pmd:check
mvn checkstyle:check
```

## 📄 更新日志

### v1.0.0 (2024-01-01)
- ✨ 新增用户注册、登录功能
- ✨ 新增商品管理功能
- ✨ 新增分类和品牌管理
- ✨ 新增 JWT 认证机制
- ✨ 集成 Redis 缓存
- ✨ 支持 Docker 部署

### v0.9.0 (2023-12-15)
- 🔧 初始化项目结构
- 🔧 配置 Spring Boot 和 MyBatis
- 🔧 数据库表结构设计

## 📝 许可证

该项目采用 [MIT 许可证](LICENSE)。

## 📞 联系信息

- **项目主页**：[https://github.com/your-username/online-store](https://github.com/your-username/online-store)
- **问题反馈**：[GitHub Issues](https://github.com/your-username/online-store/issues)
- **功能请求**：[GitHub Discussions](https://github.com/your-username/online-store/discussions)

## ❤️ 特别鸣谢

感谢以下开源项目的贡献：

- [Spring Boot](https://spring.io/projects/spring-boot) - 应用开发框架
- [Spring Cloud](https://spring.io/projects/spring-cloud) - 微服务解决方案
- [MyBatis](https://mybatis.org/) - 数据持久层框架
- [Nacos](https://nacos.io/) - 服务发现与配置管理

---

<div align="center">

**如果这个项目对你有帮助，请给个 ⭐ Star 支持一下！**

由 ❤️ 和 ☕ 制作

</div> 