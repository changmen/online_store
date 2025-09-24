# 在线商店 (Online Store)

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

一个基于 Spring Boot 和 Spring Cloud 的现代化在线商店项目，提供完整的电商功能，包括用户管理、商品管理、品牌管理、分类管理等核心业务功能。

## ✨ 功能特性

### 🔐 用户系统
- **用户注册/登录** - 支持用户注册和安全登录
- **JWT认证** - 基于JWT的无状态身份认证
- **Spring Security** - 企业级安全框架保护
- **密码加密** - BCrypt密码加密存储

### 📦 商品管理
- **商品浏览** - 商品详情查看和展示
- **分类管理** - 多级商品分类体系
- **品牌管理** - 品牌信息管理和展示
- **访问统计** - 商品访问日志记录和统计

### 🏗️ 系统架构
- **微服务架构** - 基于Spring Cloud的分布式架构
- **服务发现** - Nacos服务注册与发现
- **数据持久化** - MyBatis + MySQL数据存储
- **缓存系统** - Redis缓存提升性能
- **容器化部署** - Docker容器化支持

## 🛠️ 技术栈

### 核心框架
- **Java 17** - 现代Java开发平台
- **Spring Boot 3.4.3** - 企业级应用开发框架
- **Spring Cloud 2024.0.0** - 微服务架构框架
- **Spring Security** - 安全认证框架

### 数据层
- **MyBatis 3.0.3** - 持久层框架
- **MySQL 8.2.0** - 关系型数据库
- **Redis (Jedis 5.2.0)** - 内存数据库/缓存
- **HikariCP** - 高性能数据库连接池

### 微服务组件
- **Nacos 2.2.0** - 服务发现与配置管理
- **Spring Cloud Alibaba** - 阿里云微服务套件
- **Spring Cloud Gateway** - API网关

### 开发工具
- **Maven** - 项目构建工具
- **Lombok** - 代码简化工具
- **PageHelper** - MyBatis分页插件
- **JWT (JJWT 0.11.5)** - JSON Web Token
- **Aliyun OSS** - 阿里云对象存储

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/onlinestore/
│   │   │       ├── OnlineStoreApplication.java    # 应用启动类
│   │   │       ├── controller/                   # REST控制器
│   │   │       │   ├── MemberController.java     # 用户管理
│   │   │       │   ├── ItemController.java       # 商品管理
│   │   │       │   ├── BrandController.java      # 品牌管理
│   │   │       │   └── CategoryController.java   # 分类管理
│   │   │       ├── service/                      # 业务服务层
│   │   │       │   └── impl/                     # 服务实现
│   │   │       ├── mapper/                       # 数据访问层
│   │   │       ├── entity/                       # 数据库实体
│   │   │       ├── bean/                         # 业务对象
│   │   │       ├── dto/                          # 数据传输对象
│   │   │       ├── config/                       # 配置类
│   │   │       ├── security/                     # 安全配置
│   │   │       ├── constants/                    # 常量定义
│   │   │       └── exceptions/                   # 异常处理
│   │   └── resources/
│   │       ├── application.yaml                 # 应用配置
│   │       ├── application-local.yaml           # 本地环境配置
│   │       ├── bootstrap.yaml                   # 启动配置
│   │       ├── mapper/                           # MyBatis映射文件
│   │       ├── sql/                              # 数据库脚本
│   │       └── i18n/                             # 国际化资源
│   └── test/                                     # 测试代码
├── scripts/                                      # 脚本文件
│   └── main.py                                   # 数据生成脚本
├── docker-compose.yaml                           # Docker编排文件
├── Dockerfile                                    # Docker镜像构建文件
├── pom.xml                                       # Maven项目配置
└── README.md                                     # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- **JDK 17+** - Java开发环境
- **Maven 3.6+** - 项目构建工具
- **MySQL 8.0+** - 数据库服务
- **Redis 6.0+** - 缓存服务
- **Docker** (可选) - 容器化部署

### 1. 克隆项目

```bash
git clone <repository-url>
cd online_store
```

### 2. 数据库初始化

#### 创建数据库
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 导入数据库结构
```bash
# 执行SQL脚本初始化数据库结构和基础数据
mysql -u root -p online_store < src/main/resources/sql/init.sql
```

### 3. 配置文件

复制并修改配置文件：

```bash
cp src/main/resources/application-local.yaml.example src/main/resources/application-local.yaml
```

关键配置项：

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
      password: your_redis_password

jwt:
  secret: your_jwt_secret_key
```

### 4. 启动应用

#### 方式一：Maven启动
```bash
# 设置必要的JVM参数并启动
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

#### 方式二：IDE启动
在IDE中添加VM参数：`--add-opens java.base/java.lang=ALL-UNNAMED`

#### 方式三：Docker Compose启动
```bash
# 启动所有服务（MySQL + Redis + 应用）
docker-compose up -d

# 或者只启动数据库服务
docker-compose --profile without-redis up -d  # 启动MySQL
docker-compose --profile all up -d             # 启动MySQL + Redis
```

### 5. 验证启动

```bash
# 检查应用健康状态
curl http://localhost:8080/actuator/health

# 访问应用首页
curl http://localhost:8080/
```

## 📡 API 文档

### 认证接口

#### 用户注册
```http
POST /api/v1/members/registry
Content-Type: application/json

{
  "name": "username",
  "password": "password",
  "email": "user@example.com"
}
```

#### 用户登录
```http
POST /api/v1/members/login
Content-Type: application/json

{
  "name": "username",
  "password": "password"
}
```

### 商品接口

#### 获取商品详情
```http
GET /api/v1/items/{itemId}
Authorization: Bearer {jwt_token}
```

#### 获取品牌列表
```http
GET /api/v1/brands?pageNum=1&pageSize=10&visible=1
Authorization: Bearer {jwt_token}
```

#### 获取分类信息
```http
GET /api/v1/categories/{categoryId}
Authorization: Bearer {jwt_token}
```

### 响应格式

成功响应：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    // 响应数据
  }
}
```

错误响应：
```json
{
  "code": 400,
  "message": "错误信息",
  "data": null
}
```

## 🔧 开发指南

### 代码规范

- 使用 **Lombok** 简化POJO类代码
- 使用 **@Valid** 进行参数校验
- 统一异常处理机制
- RESTful API设计规范
- 分层架构：Controller -> Service -> Mapper

### 数据库设计

- 使用下划线命名法（snake_case）
- 主键统一使用 `id` 字段
- 创建时间字段：`created_time`
- 更新时间字段：`updated_time`
- 软删除字段：`deleted`

### 缓存策略

- 分类数据缓存（定时刷新）
- 用户会话缓存
- 商品访问统计缓存

## 🧪 测试

### 运行测试
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=MemberServiceTest
```

### 测试覆盖率
```bash
# 生成测试覆盖率报告
mvn clean test jacoco:report
```

## 🚢 部署指南

### Docker部署

#### 构建镜像
```bash
# 构建应用镜像
docker build -t online-store:latest .
```

#### 运行容器
```bash
# 运行应用容器
docker run -d -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e MYSQL_HOST=your_mysql_host \
  -e REDIS_HOST=your_redis_host \
  --name online-store \
  online-store:latest
```

### 生产环境配置

1. **数据库连接池配置**
2. **Redis集群配置**
3. **Nacos集群配置**
4. **日志配置优化**
5. **JVM参数调优**

## 🔍 监控与运维

### 应用监控

访问 Spring Boot Actuator 端点：

- 健康检查：`/actuator/health`
- 应用信息：`/actuator/info`
- 性能指标：`/actuator/metrics`
- 环境配置：`/actuator/env`

### 日志配置

应用使用 SLF4J + Logback 进行日志记录，支持：

- 按日期滚动的日志文件
- 不同级别的日志输出
- 结构化日志格式

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支：`git checkout -b feature/amazing-feature`
3. 提交更改：`git commit -m 'Add some amazing feature'`
4. 推送分支：`git push origin feature/amazing-feature`
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🆘 故障排除

### 常见问题

**Q: 启动时出现 "Access denied for user" 错误**
A: 检查数据库连接配置，确保用户名密码正确且具有足够权限。

**Q: Redis连接失败**
A: 确保Redis服务已启动，检查连接地址和端口配置。

**Q: JWT token验证失败**
A: 检查JWT密钥配置，确保客户端正确携带Authorization头。

**Q: Nacos服务注册失败**
A: 检查Nacos服务器状态和网络连接，确认配置文件中的Nacos地址正确。

### 获取帮助

- 提交 [Issue](https://github.com/your-repo/online-store/issues)
- 查看 [Wiki](https://github.com/your-repo/online-store/wiki)
- 联系维护者

---

⭐ 如果这个项目对您有帮助，请给个星标支持！