# 在线商店系统 (Online Store)

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.2-orange.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)

这是一个基于Spring Boot和Spring Cloud的现代化在线商店系统，提供完整的电商功能，包括商品管理、用户管理、分类管理等核心功能。

## ✨ 核心特性

- 🛍️ **商品管理**：支持商品信息、SKU、品牌、分类管理
- 👥 **用户系统**：完整的用户注册、登录、权限管理
- 🔐 **安全认证**：基于JWT的安全认证机制
- 📊 **数据访问**：使用MyBatis进行数据持久化
- 🚀 **缓存支持**：集成Redis缓存提升性能
- ☁️ **微服务**：支持Nacos服务发现与配置管理
- 🐳 **容器化**：提供Docker部署支持
- 📦 **文件存储**：支持阿里云OSS对象存储

## 🛠️ 技术栈

### 后端框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: 用于安全认证
- **MyBatis**: 3.0.3 (数据持久化)
- **PageHelper**: 2.1.0 (分页插件)

### 数据库与缓存
- **MySQL**: 8.2.0
- **Redis**: 通过Jedis 5.2.0客户端

### 第三方服务
- **Nacos**: 2.2.0 (服务发现与配置管理)
- **JWT**: 0.11.5 (认证令牌)
- **阿里云OSS**: 3.18.1 (对象存储)
- **Lombok**: 1.18.36 (代码简化)

### 工具与部署
- **Maven**: 项目构建工具
- **Docker**: 容器化部署
- **Docker Compose**: 本地开发环境

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # 应用启动类
│   │   │   ├── controller/                     # REST API控制器
│   │   │   │   ├── AttributeController.java    # 属性管理
│   │   │   │   ├── BrandController.java        # 品牌管理
│   │   │   │   ├── CategoryController.java     # 分类管理
│   │   │   │   ├── ItemController.java         # 商品管理
│   │   │   │   ├── ItemDetailController.java   # 商品详情
│   │   │   │   └── MemberController.java       # 用户管理
│   │   │   ├── entity/                         # 数据实体
│   │   │   ├── service/                        # 业务逻辑层
│   │   │   ├── mapper/                         # 数据访问层
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── config/                        # 配置类
│   │   │   ├── security/                      # 安全配置
│   │   │   ├── utils/                         # 工具类
│   │   │   ├── enums/                         # 枚举类
│   │   │   └── exceptions/                    # 异常处理
│   │   └── resources/
│   │       ├── application.yaml              # 应用配置
│   │       └── mapper/                       # MyBatis映射文件
│   └── test/                                 # 测试代码
├── scripts/                                  # 脚本文件
├── Dockerfile                                # Docker镜像构建
├── docker-compose.yaml                      # 本地开发环境
├── pom.xml                                   # Maven配置
└── README.md                                 # 项目文档
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17或更高版本
- **Maven**: 3.6或更高版本
- **MySQL**: 8.0或更高版本
- **Redis**: 6.0或更高版本

### 本地开发

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 启动基础服务
使用Docker Compose快速启动MySQL和Redis：
```bash
# 启动所有服务
docker-compose --profile all up -d

# 或仅启动MySQL
docker-compose --profile without-redis up -d
```

#### 3. 初始化数据库
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 4. 配置环境变量
创建环境变量或修改 `application.yaml`：
```bash
export JWT_SECRET=your-jwt-secret-key
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
```

#### 5. 启动应用
```bash
# 使用Maven启动
mvn spring-boot:run

# 或者编译后运行
mvn clean package
java -jar target/online-store-1.0-SNAPSHOT.jar
```

#### 6. 验证启动
访问 http://localhost:8080 确认应用正常启动。

### 🐳 Docker部署

#### 构建镜像
```bash
docker build -t online-store:latest .
```

#### 运行容器
```bash
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e JWT_SECRET=your-jwt-secret \
  -e SPRING_PROFILES_ACTIVE=prod \
  online-store:latest
```

## 🔧 配置说明

### 应用配置
主要配置文件：`src/main/resources/application.yaml`

#### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
    username: root
    password: 123456
```

#### Redis配置
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
```

#### Nacos配置（可选）
```yaml
spring:
  cloud:
    nacos:
      discovery:
        enabled: true
        server-addr: localhost:8848
```

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `JWT_SECRET` | JWT签名密钥 | 必须设置 |
| `ADMIN_USERNAME` | 管理员用户名 | admin |
| `ADMIN_PASSWORD` | 管理员密码 | admin123 |
| `SPRING_PROFILES_ACTIVE` | 激活的配置文件 | local |
| `NACOS_ENABLED` | 是否启用Nacos | false |

## 📚 API文档

### 核心接口

#### 商品管理
- `GET /api/items` - 获取商品列表
- `POST /api/items` - 创建商品
- `PUT /api/items/{id}` - 更新商品
- `DELETE /api/items/{id}` - 删除商品

#### 用户管理
- `POST /api/members/register` - 用户注册
- `POST /api/members/login` - 用户登录
- `GET /api/members/profile` - 获取用户信息

#### 分类管理
- `GET /api/categories` - 获取分类列表
- `POST /api/categories` - 创建分类

## 🧪 测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=ItemControllerTest

# 生成测试报告
mvn surefire-report:report
```

## 📈 监控与健康检查

应用集成了Spring Boot Actuator，提供健康检查和监控端点：

- 健康检查：`GET /actuator/health`
- 应用信息：`GET /actuator/info`
- 指标监控：`GET /actuator/metrics`

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🔗 相关链接

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring Cloud 官方文档](https://spring.io/projects/spring-cloud)
- [MyBatis 官方文档](https://mybatis.org/mybatis-3/)
- [Nacos 官方文档](https://nacos.io/)

## ❓ 常见问题

### Q: 启动时出现数据库连接错误？
A: 请确保MySQL服务已启动，数据库已创建，并检查 `application.yaml` 中的连接配置。

### Q: JWT Token验证失败？
A: 请确保已正确设置 `JWT_SECRET` 环境变量。

### Q: 如何启用Nacos服务发现？
A: 设置环境变量 `NACOS_ENABLED=true` 并确保Nacos服务器正在运行。 