# 在线商店系统 Online Store

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue)
![Redis](https://img.shields.io/badge/Redis-latest-red)

一个基于 Spring Cloud 微服务架构的现代化电商系统，提供完整的商品管理、用户管理、库存管理等电商核心功能。该系统采用前后端分离架构，支持高并发访问，具备良好的扩展性和维护性。

## ✨ 核心特性

- 🛍️ **商品管理**: 支持商品分类、品牌管理、属性配置、SKU管理
- 👥 **用户系统**: 用户注册登录、JWT认证、权限管理
- 🔐 **安全认证**: Spring Security + JWT token 认证机制
- 📊 **数据访问**: MyBatis-Plus + 分页查询支持
- 🗄️ **缓存系统**: Redis 缓存优化，提升系统性能
- 🌐 **微服务**: Nacos 服务注册发现（可选）
- 📱 **文件存储**: 阿里云 OSS 对象存储支持
- 🐳 **容器化**: Docker + Docker Compose 一键部署
- 📝 **API文档**: RESTful API 设计，完整的接口文档

## 🛠️ 技术栈

### 后端技术
- **核心框架**: Spring Boot 3.4.3
- **微服务**: Spring Cloud 2024.0.0
- **服务注册**: Nacos 2.2.0 (可选)
- **安全框架**: Spring Security + JWT
- **ORM框架**: MyBatis 3.0.3 + PageHelper
- **数据库**: MySQL 8.2.0
- **缓存**: Redis + Jedis 5.2.0
- **文件存储**: 阿里云 OSS 3.18.1
- **构建工具**: Maven 3.6+
- **JDK版本**: OpenJDK 17

### 工具依赖
- **JSON处理**: Jackson
- **代码简化**: Lombok
- **工具类**: Apache Commons Lang3
- **参数校验**: Spring Boot Validation
- **AOP支持**: Spring AOP

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── bean/              # 业务Bean对象
│   ├── config/            # 配置类
│   │   ├── SecurityConfig.java     # 安全配置
│   │   ├── RedisConfig.java        # Redis配置
│   │   ├── MyBatisConfig.java      # MyBatis配置
│   │   └── OssConfig.java          # OSS配置
│   ├── controller/        # REST控制器
│   │   ├── MemberController.java   # 用户管理
│   │   ├── ItemController.java     # 商品管理
│   │   ├── CategoryController.java # 分类管理
│   │   └── BrandController.java    # 品牌管理
│   ├── service/           # 业务服务层
│   ├── mapper/            # 数据访问层
│   ├── entity/            # 实体类
│   ├── dto/               # 数据传输对象
│   ├── security/          # 安全认证
│   ├── utils/             # 工具类
│   └── OnlineStoreApplication.java # 启动类
├── src/main/resources/
│   ├── mapper/            # MyBatis XML映射文件
│   ├── sql/               # 数据库初始化脚本
│   ├── i18n/              # 国际化资源文件
│   ├── application.yaml   # 主配置文件
│   └── bootstrap.yaml     # 引导配置文件
├── src/test/              # 测试代码
├── scripts/               # 工具脚本
├── docker-compose.yaml    # Docker编排文件
├── Dockerfile             # Docker镜像构建文件
└── pom.xml               # Maven项目配置
```

## 🚀 快速开始

### 环境要求

- **JDK**: OpenJDK 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **Docker**: 20.0+ (可选，用于容器化部署)

### 1. 克隆项目

```bash
git clone <repository-url>
cd online_store
```

### 2. 数据库准备

```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户(可选)
CREATE USER 'online_store'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON online_store.* TO 'online_store'@'%';
FLUSH PRIVILEGES;
```

### 3. 配置文件

复制并修改配置文件：

```bash
cp src/main/resources/application-local.yaml.example src/main/resources/application-local.yaml
```

编辑 `application.yaml` 配置数据库和Redis连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password

jwt:
  secret: your_jwt_secret_key_here
```

### 4. 初始化数据库

执行数据库初始化脚本：

```bash
# 在MySQL中执行初始化脚本
mysql -u your_username -p online_store < src/main/resources/sql/
```

### 5. 编译和运行

```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

应用启动后访问：http://localhost:8080

## 🐳 Docker 部署

### 使用 Docker Compose (推荐)

```bash
# 启动所有服务（MySQL + Redis + 应用）
docker-compose --profile all up -d

# 仅启动 MySQL 和 Redis
docker-compose --profile without-redis up -d

# 查看服务状态
docker-compose ps

# 停止服务
docker-compose down
```

### 单独构建应用镜像

```bash
# 构建应用镜像
docker build -t online-store:latest .

# 运行容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  online-store:latest
```

## 📚 API 文档

### 主要接口模块

| 模块 | 接口路径 | 描述 |
|------|----------|------|
| 用户管理 | `/api/members/*` | 用户注册、登录、信息管理 |
| 商品管理 | `/api/items/*` | 商品CRUD、搜索、详情 |
| 分类管理 | `/api/categories/*` | 商品分类管理 |
| 品牌管理 | `/api/brands/*` | 品牌信息管理 |
| 属性管理 | `/api/attributes/*` | 商品属性配置 |

### 示例接口

#### 用户注册
```http
POST /api/members/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com",
  "phone": "13800138000"
}
```

#### 用户登录
```http
POST /api/members/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

#### 商品列表查询
```http
GET /api/items?page=1&size=10&categoryId=1&keyword=手机
Authorization: Bearer <jwt_token>
```

## 🔧 开发指南

### 本地开发环境搭建

1. **IDE配置**: 推荐使用 IntelliJ IDEA 或 Visual Studio Code
2. **代码格式**: 使用项目根目录的 `.editorconfig` 配置
3. **Lombok支持**: 确保IDE安装Lombok插件

### 数据库迁移

```bash
# 使用Flyway进行数据库版本管理（如果配置）
mvn flyway:migrate
```

### 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn integration-test

# 生成测试报告
mvn site
```

## 🔒 安全配置

### JWT Token 配置

在 `application.yaml` 中配置JWT密钥：

```yaml
jwt:
  secret: ${JWT_SECRET:your-256-bit-secret-key}
  expiration: 86400  # 24小时，单位秒
```

### 环境变量

推荐使用环境变量管理敏感信息：

```bash
export JWT_SECRET="your-jwt-secret-key"
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="your-admin-password"
export SPRING_PROFILES_ACTIVE="local"
```

## 📈 性能优化

- **数据库连接池**: 使用HikariCP连接池
- **Redis缓存**: 缓存热点数据，减少数据库访问
- **分页查询**: 使用PageHelper进行高效分页
- **异步处理**: 使用`@Async`注解处理耗时操作

## 🐛 故障排除

### 常见问题

1. **启动失败**: 检查Java版本和Maven版本
2. **数据库连接失败**: 确认数据库服务状态和连接配置
3. **Redis连接失败**: 检查Redis服务和网络连接
4. **JWT认证失败**: 确认JWT密钥配置正确

### 日志查看

```bash
# 查看应用日志
tail -f logs/online-store.log

# Docker容器日志
docker logs -f online-store
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

- 项目维护者: [维护者姓名]
- 邮箱: [email@example.com]
- 问题反馈: [GitHub Issues](issues链接)

---

⭐ 如果这个项目对你有帮助，请给它一个星标！