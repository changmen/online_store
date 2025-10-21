# Online Store

一个功能完整的在线商店系统，基于Spring Cloud微服务架构构建，提供商品管理、用户管理、订单处理等核心电商功能。

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.2-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)

## ✨ 主要特性

- 🛍️ **商品管理**: 支持商品分类、品牌、属性管理
- 👥 **用户系统**: 完整的用户注册、登录、权限管理
- 🔐 **安全认证**: 基于JWT的身份认证和Spring Security安全框架
- 📊 **数据分析**: 商品访问日志记录和分析
- 🚀 **微服务架构**: 基于Spring Cloud和Nacos的服务发现
- 💾 **缓存支持**: Redis缓存提升系统性能
- 🐳 **容器化部署**: 支持Docker和Docker Compose部署
- 📱 **RESTful API**: 完整的REST API接口设计

## 🛠️ 技术栈

### 后端框架
- **JDK 17** - Java开发环境
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **MyBatis 3.0.3** - ORM框架
- **PageHelper 2.1.0** - 分页插件

### 数据存储
- **MySQL 8.2** - 主数据库
- **Redis** - 缓存数据库
- **Jedis 5.2.0** - Redis Java客户端

### 服务治理
- **Nacos 2.2.0** - 服务发现与配置中心
- **Spring Cloud Alibaba** - 阿里云微服务套件

### 其他组件
- **JWT** - JSON Web Token认证
- **Lombok** - 代码简化工具
- **Aliyun OSS** - 对象存储服务
- **Jackson** - JSON处理
- **Apache Commons** - 工具类库

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java    # 主启动类
│   ├── bean/                          # Bean配置类
│   ├── config/                        # 配置类
│   ├── constants/                     # 常量定义
│   ├── controller/                    # 控制器层
│   │   ├── AttributeController.java   # 属性管理
│   │   ├── BrandController.java       # 品牌管理
│   │   ├── CategoryController.java    # 分类管理
│   │   ├── ItemController.java        # 商品管理
│   │   ├── ItemDetailController.java  # 商品详情
│   │   └── MemberController.java      # 用户管理
│   ├── dto/                           # 数据传输对象
│   ├── entity/                        # 实体类
│   ├── enums/                         # 枚举类
│   ├── exceptions/                    # 异常处理
│   ├── handler/                       # 处理器
│   ├── mapper/                        # 数据访问层
│   ├── security/                      # 安全配置
│   ├── service/                       # 业务服务层
│   └── utils/                         # 工具类
├── src/main/resources/
│   ├── application.yaml               # 应用配置
│   └── mapper/                        # MyBatis映射文件
├── scripts/                           # 脚本工具
├── docker-compose.yaml               # Docker编排文件
├── Dockerfile                         # Docker镜像构建文件
└── pom.xml                           # Maven依赖配置
```

## 🚀 快速开始

### 环境要求

- ☕ **JDK 17+** - Java开发环境
- 📦 **Maven 3.6+** - 项目构建工具
- 🗄️ **MySQL 8.0+** - 数据库
- 🔥 **Redis 6.0+** - 缓存服务
- 🐳 **Docker** (可选) - 容器化部署

### 本地开发环境搭建

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd online_store
   ```

2. **启动基础服务**
   ```bash
   # 使用Docker Compose启动MySQL和Redis
   docker-compose --profile all up -d
   
   # 或手动启动MySQL和Redis服务
   ```

3. **创建数据库**
   ```sql
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

4. **配置应用**
   
   编辑 `src/main/resources/application.yaml`，根据实际环境修改配置：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/online_store
       username: root
       password: your_password
     data:
       redis:
         host: localhost
         port: 6379
   ```

5. **设置环境变量**
   ```bash
   export JWT_SECRET=your_jwt_secret_key_here
   export ADMIN_USERNAME=admin
   export ADMIN_PASSWORD=your_admin_password
   ```

6. **启动应用**
   ```bash
   # 方式一：使用Maven
   mvn spring-boot:run
   
   # 方式二：先编译再运行
   mvn clean package -DskipTests
   java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
   ```

7. **验证启动**
   
   访问 http://localhost:8080 确认应用正常启动

## 🐳 Docker部署

### 使用Docker Compose (推荐)

```bash
# 启动所有服务（应用+MySQL+Redis）
docker-compose --profile all up -d

# 仅启动MySQL和Redis
docker-compose --profile without-redis up -d  # 仅MySQL
docker-compose --profile all up -d mysql redis  # MySQL + Redis
```

### 单独使用Docker

```bash
# 构建应用镜像
docker build -t online-store .

# 运行容器
docker run -d -p 8080:8080 \
  -e JWT_SECRET=your_secret \
  -e ADMIN_USERNAME=admin \
  -e ADMIN_PASSWORD=admin123 \
  online-store
```

## 📋 核心功能模块

| 模块 | 功能描述 | 主要接口 |
|------|----------|----------|
| 👤 用户管理 | 用户注册、登录、权限管理 | `/api/members/**` |
| 🏷️ 分类管理 | 商品分类的增删改查 | `/api/categories/**` |
| 🏢 品牌管理 | 品牌信息管理 | `/api/brands/**` |
| 🛍️ 商品管理 | 商品信息、SKU管理 | `/api/items/**` |
| 🔧 属性管理 | 商品属性和属性值管理 | `/api/attributes/**` |
| 📊 访问统计 | 商品访问日志记录 | `/api/item-details/**` |

## 🔐 安全配置

系统采用多层安全机制：

- **JWT认证**: 无状态的用户身份验证
- **Spring Security**: 方法级权限控制
- **密码加密**: BCrypt密码哈希
- **CORS配置**: 跨域请求安全控制

默认管理员账户:
- 用户名: `admin` (可通过环境变量 `ADMIN_USERNAME` 修改)
- 密码: `admin123` (可通过环境变量 `ADMIN_PASSWORD` 修改)

## 🧪 测试

```bash
# 运行所有测试
mvn test

# 跳过测试进行打包
mvn clean package -DskipTests

# 运行特定测试类
mvn test -Dtest=YourTestClass
```

## 📈 监控和管理

应用集成了Spring Boot Actuator，提供以下监控端点：

- 健康检查: `GET /actuator/health`
- 应用信息: `GET /actuator/info`
- 指标数据: `GET /actuator/metrics`

## 🌐 API文档

### 核心API端点

| 分类 | 方法 | 端点 | 描述 |
|------|------|------|------|
| 认证 | POST | `/api/auth/login` | 用户登录 |
| 认证 | POST | `/api/auth/register` | 用户注册 |
| 商品 | GET | `/api/items` | 获取商品列表 |
| 商品 | GET | `/api/items/{id}` | 获取商品详情 |
| 分类 | GET | `/api/categories` | 获取分类列表 |
| 品牌 | GET | `/api/brands` | 获取品牌列表 |

### 请求示例

```bash
# 获取商品列表
curl -X GET "http://localhost:8080/api/items?page=1&size=10"

# 用户登录
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## 🛠️ 开发指南

### 代码规范

- 遵循阿里巴巴Java开发手册
- 使用Lombok减少样板代码
- 统一的异常处理机制
- RESTful API设计原则

### 数据库规范

- 表名使用下划线命名法
- 主键统一使用`id`字段
- 时间字段统一使用`create_time`和`update_time`
- 软删除使用`deleted`字段标识

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📝 更新日志

### v1.0-SNAPSHOT
- ✨ 初始版本发布
- 🛍️ 实现基础商品管理功能
- 👥 完成用户认证系统
- 🔐 集成JWT安全认证
- 🐳 支持Docker容器化部署

## 📄 许可证

该项目基于 MIT 许可证开源 - 查看 [LICENSE](LICENSE) 文件了解详情

## 💬 支持

如果您在使用过程中遇到问题，请通过以下方式获取帮助：

- 📋 [创建 Issue](../../issues)
- 📧 发送邮件至: support@example.com
- 💬 加入讨论群: [链接]

---

⭐ 如果这个项目对您有帮助，请给我们一个星标! 