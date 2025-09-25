# 🛒 Online Store

一个现代化的在线商店系统，基于 Spring Cloud 微服务架构构建，提供完整的电商平台功能。

## ✨ 项目特性

- 🏗️ **微服务架构**：基于 Spring Cloud 构建的分布式系统
- 🔐 **安全认证**：集成 Spring Security + JWT 的认证授权体系
- 📊 **服务治理**：支持 Nacos 服务注册与发现
- 💾 **数据持久化**：MySQL + MyBatis 数据访问层
- ⚡ **高性能缓存**：Redis 缓存支持
- 🐳 **容器化部署**：Docker + Docker Compose 支持
- 📄 **分页查询**：PageHelper 分页插件
- 📁 **文件存储**：阿里云 OSS 对象存储集成

## 🛠️ 技术栈

### 核心框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: 用于安全认证
- **MyBatis**: 3.0.3 + PageHelper 2.1.0

### 数据存储
- **MySQL**: 8.2.0 (主数据库)
- **Redis**: 集群缓存 (Jedis 5.2.0)

### 服务治理
- **Nacos**: 2.2.0 (服务注册发现 + 配置中心)
- **Spring Cloud Alibaba**: 2022.0.0.0

### 工具库
- **Lombok**: 1.18.36 (代码简化)
- **JWT**: 0.11.5 (JSON Web Token)
- **Apache Commons**: 工具类库
- **阿里云 OSS**: 3.18.1 (对象存储)

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/    # 主要代码
│   │   │   ├── OnlineStoreApplication.java  # 启动类
│   │   │   ├── controller/                  # 控制层
│   │   │   ├── service/                     # 业务逻辑层
│   │   │   ├── mapper/                      # 数据访问层
│   │   │   ├── entity/                      # 实体类
│   │   │   ├── config/                      # 配置类
│   │   │   └── common/                      # 通用组件
│   │   └── resources/
│   │       ├── application.yaml             # 主配置文件
│   │       ├── application-local.yaml       # 本地环境配置
│   │       ├── bootstrap.yaml               # 引导配置
│   │       └── mapper/                      # MyBatis SQL映射
│   └── test/                                # 测试代码
├── scripts/                                 # 脚本工具
├── docker-compose.yaml                      # Docker编排配置
├── Dockerfile                               # Docker镜像构建
├── pom.xml                                  # Maven依赖配置
└── README.md                                # 项目文档
```

## 🚀 快速开始

### 环境要求

- ☕ **JDK 17** 或更高版本
- 📦 **Maven 3.6** 或更高版本
- 🗄️ **MySQL 8.0** 或更高版本
- 🔴 **Redis 6.0** 或更高版本
- 🐳 **Docker** (可选，用于容器化部署)

### 方式一：本地运行

#### 1. 环境准备

**启动 MySQL 服务并创建数据库：**
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**启动 Redis 服务：**
```bash
# 使用默认配置启动
redis-server
```

#### 2. 配置文件

根据你的环境修改 `src/main/resources/application.yaml` 中的配置：

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  
  # Redis配置
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password  # 如果有密码

# JWT密钥配置（生产环境请使用更安全的密钥）
jwt:
  secret: your-jwt-secret-key
```

#### 3. 运行应用

```bash
# 克隆项目
git clone <repository-url>
cd online-store

# 编译项目
mvn clean compile

# 运行应用（添加必要的JVM参数）
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"

# 或者打包后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 方式二：Docker 部署

#### 1. 使用 Docker Compose（推荐）

```bash
# 启动所有服务（MySQL + Redis）
docker-compose --profile all up -d

# 仅启动 MySQL
docker-compose --profile without-redis up -d

# 查看服务状态
docker-compose ps
```

#### 2. 构建应用镜像

```bash
# 构建应用 Docker 镜像
docker build -t online-store:latest .

# 运行应用容器
docker run -d --name online-store-app \
  --network host \
  -e SPRING_PROFILES_ACTIVE=local \
  -e JWT_SECRET=your-secret-key \
  online-store:latest
```

### 3. 验证部署

访问以下端点验证应用是否正常运行：

- 🏠 **应用首页**: http://localhost:8080
- 💓 **健康检查**: http://localhost:8080/actuator/health
- 📊 **应用信息**: http://localhost:8080/actuator/info

## 🔧 配置说明

### 环境配置

项目支持多环境配置，通过 `SPRING_PROFILES_ACTIVE` 环境变量控制：

- `local`: 本地开发环境
- `dev`: 开发环境  
- `prod`: 生产环境

### Nacos 配置中心

如需启用 Nacos 服务治理，设置以下环境变量：

```bash
export NACOS_ENABLED=true
export NACOS_SERVER_ADDR=your-nacos-server:8848
```

### JWT 配置

**重要**：生产环境请务必设置安全的 JWT 密钥：

```bash
export JWT_SECRET=your-very-secure-secret-key-here
```

## 📚 API 文档

应用启动后，你可以通过以下方式访问 API：

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/refresh` - 刷新token

### 商品管理
- `GET /api/products` - 获取商品列表
- `GET /api/products/{id}` - 获取商品详情
- `POST /api/products` - 创建商品
- `PUT /api/products/{id}` - 更新商品
- `DELETE /api/products/{id}` - 删除商品

## 🧪 开发指南

### 代码规范

- 使用 **Lombok** 简化实体类代码
- 遵循 **RESTful** API 设计规范
- 使用 **MyBatis** 进行数据库操作
- 统一异常处理和日志记录

### 测试

```bash
# 运行所有测试
mvn test

# 运行集成测试
mvn integration-test

# 生成测试覆盖率报告
mvn jacoco:report
```

## 🚀 部署

### 生产环境部署清单

- [ ] 配置生产数据库连接
- [ ] 设置安全的 JWT 密钥
- [ ] 配置 Redis 集群
- [ ] 启用 HTTPS
- [ ] 配置日志聚合
- [ ] 设置监控和告警
- [ ] 配置备份策略

### 性能优化建议

- 使用连接池优化数据库连接
- 合理设置 Redis 缓存策略
- 启用 Gzip 压缩
- 配置 CDN 加速静态资源

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目基于 MIT 许可证开源 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 支持

如果你在使用过程中遇到问题，请通过以下方式获取帮助：

- 📋 [提交 Issue](../../issues)
- 💬 [讨论区](../../discussions)
- 📧 联系维护者

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！ 