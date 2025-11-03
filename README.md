# 📦 Online Store - 在线商店系统

这是一个基于 Spring Cloud 和 Spring Boot 的现代化在线商店后端系统，采用微服务架构设计，支持商品管理、用户管理、订单处理等核心电商功能。

## ✨ 特性

- 🔐 基于 JWT 的安全认证和授权
- 🏗️ 微服务架构，支持 Nacos 服务注册与发现
- 💾 Redis 缓存支持，提升系统性能
- 📝 完整的 RESTful API 设计
- 🔧 灵活的配置管理
- 🐳 Docker 容器化部署支持
- 📊 分页查询支持（PageHelper）
- 📦 阿里云 OSS 文件存储集成

## 🛠️ 技术栈

### 核心框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0
- **Spring Security**: 安全认证框架

### 数据存储
- **MySQL**: 8.2.0 - 主数据库
- **Redis**: 5.2.0 (Jedis) - 缓存
- **MyBatis**: 3.0.3 - ORM 框架
- **PageHelper**: 2.1.0 - 分页插件

### 服务治理
- **Nacos**: 2.2.0 - 服务注册与配置中心

### 其他组件
- **JWT**: 0.11.5 - 身份验证
- **Lombok**: 1.18.36 - 简化代码
- **Aliyun OSS**: 3.18.1 - 对象存储
- **Apache Commons**: 工具库

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用启动类
│   │   │   ├── bean/                           # Bean 配置
│   │   │   ├── config/                         # 配置类
│   │   │   ├── constants/                      # 常量定义
│   │   │   ├── controller/                     # REST 控制器
│   │   │   │   ├── AttributeController.java
│   │   │   │   ├── BrandController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ItemController.java
│   │   │   │   ├── ItemDetailController.java
│   │   │   │   └── MemberController.java
│   │   │   ├── dto/                            # 数据传输对象
│   │   │   ├── entity/                         # 实体类
│   │   │   ├── enums/                          # 枚举类
│   │   │   ├── errors/                         # 错误处理
│   │   │   ├── exceptions/                     # 自定义异常
│   │   │   ├── handler/                        # 处理器
│   │   │   ├── mapper/                         # MyBatis Mapper
│   │   │   ├── security/                       # 安全配置
│   │   │   ├── service/                        # 业务逻辑层
│   │   │   └── utils/                          # 工具类
│   │   └── resources/
│   │       ├── application.yaml                # 主配置文件
│   │       └── mapper/                         # MyBatis XML 映射文件
│   └── test/                                   # 测试代码
├── scripts/                                    # 脚本工具
├── docker-compose.yaml                         # Docker Compose 配置
├── Dockerfile                                  # Docker 镜像构建文件
├── pom.xml                                     # Maven 项目配置
└── README.md                                   # 项目文档
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本

### 本地开发

#### 1. 启动依赖服务（使用 Docker Compose）

```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 仅启动 MySQL
docker-compose --profile without-redis up -d
```

#### 2. 创建数据库

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. 配置环境变量（可选）

创建 `.env` 文件或设置系统环境变量：

```bash
export JWT_SECRET=your-secret-key-here
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
export SPRING_PROFILES_ACTIVE=local
export NACOS_ENABLED=false
```

#### 4. 修改配置文件

编辑 `src/main/resources/application.yaml`，根据实际情况修改以下配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
  data:
    redis:
      host: localhost
      port: 6379
```

#### 5. 运行应用程序

**方式一：使用 Maven**

```bash
# 添加 JVM 参数运行
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

**方式二：IDE 运行**

在 IDE 中配置 VM 参数：
```
--add-opens java.base/java.lang=ALL-UNNAMED
```

然后运行 `OnlineStoreApplication.java`

**方式三：打包后运行**

```bash
# 打包
mvn clean package -DskipTests

# 运行
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

#### 6. 验证服务

应用启动后，访问：
- **健康检查**: http://localhost:8080/actuator/health
- **API 接口**: http://localhost:8080/

默认用户名和密码：
- 用户名: `admin`
- 密码: `admin123`

## 🐳 Docker 部署

### 构建镜像

```bash
docker build -t online-store:latest .
```

### 运行容器

```bash
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e JWT_SECRET=your-secret-key \
  -e SPRING_PROFILES_ACTIVE=prod \
  online-store:latest
```

## 📝 API 文档

主要 API 端点：

### 商品管理
- `GET /items` - 获取商品列表
- `GET /items/{id}` - 获取商品详情
- `POST /items` - 创建商品
- `PUT /items/{id}` - 更新商品
- `DELETE /items/{id}` - 删除商品

### 品牌管理
- `GET /brands` - 获取品牌列表
- `GET /brands/{id}` - 获取品牌详情
- `POST /brands` - 创建品牌

### 分类管理
- `GET /categories` - 获取分类列表
- `GET /categories/{id}` - 获取分类详情

### 用户管理
- `GET /members` - 获取用户列表
- `GET /members/{id}` - 获取用户详情

### 属性管理
- `GET /attributes` - 获取属性列表

## ⚙️ 配置说明

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `JWT_SECRET` | JWT 密钥（必需） | - |
| `ADMIN_USERNAME` | 管理员用户名 | admin |
| `ADMIN_PASSWORD` | 管理员密码 | admin123 |
| `SPRING_PROFILES_ACTIVE` | 激活的配置文件 | local |
| `NACOS_ENABLED` | 是否启用 Nacos | false |

### 配置文件说明

- `application.yaml` - 主配置文件
- `bootstrap.yaml` - Bootstrap 配置（如果使用 Nacos）

## 🧪 测试

```bash
# 运行所有测试
mvn test

# 运行测试并生成报告
mvn test jacoco:report
```

## 📦 打包部署

```bash
# 清理并打包
mvn clean package

# 跳过测试打包
mvn clean package -DskipTests
```

生成的 JAR 文件位于 `target/online-store-1.0-SNAPSHOT.jar`

## 🔧 开发工具

项目包含 `scripts/` 目录，提供一些实用脚本：

```bash
cd scripts
# 查看脚本说明
cat README.md
```

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证。详见 LICENSE 文件。

## 📞 联系方式

如有问题或建议，请提交 Issue 或 Pull Request。

---

**Happy Coding! 🎉** 