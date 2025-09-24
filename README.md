# 🛒 Online Store

一个基于Spring Cloud微服务架构的现代化在线商店系统，提供完整的电商功能包括用户管理、商品管理、订单处理、支付集成等。

## ✨ 功能特性

- 🔐 **用户认证与授权**：基于JWT的安全认证系统
- 🛍️ **商品管理**：商品分类、库存管理、价格管理
- 🛒 **购物车功能**：支持商品添加、删除、数量调整
- 📦 **订单管理**：订单创建、状态跟踪、历史记录
- 💳 **支付集成**：支持多种支付方式
- 📊 **数据统计**：销售统计、用户行为分析
- 🔄 **微服务架构**：基于Spring Cloud的分布式系统
- 📁 **文件存储**：集成阿里云OSS对象存储

## 🏗️ 技术栈

### 后端技术
- **Java 17** - 编程语言
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **MyBatis 3.0.3** - ORM框架
- **PageHelper 2.1.0** - 分页插件

### 数据库与缓存
- **MySQL 8.2.0** - 关系型数据库
- **Redis** - 缓存数据库
- **Jedis 5.2.0** - Redis客户端

### 服务治理
- **Nacos 2.2.0** - 服务注册与配置中心
- **Spring Cloud Alibaba 2022.0.0.0** - 阿里云微服务套件

### 第三方服务
- **JWT (JJWT 0.11.5)** - 身份认证
- **阿里云OSS 3.18.1** - 对象存储
- **Lombok 1.18.36** - 代码简化

### 工具与部署
- **Maven** - 项目构建工具
- **Docker** - 容器化部署
- **Docker Compose** - 容器编排

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用启动类
│   │   │   ├── bean/                          # 配置Bean
│   │   │   ├── config/                        # 配置类
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # REST控制器
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── exceptions/                    # 自定义异常
│   │   │   ├── handler/                       # 异常处理器
│   │   │   ├── mapper/                        # MyBatis映射器
│   │   │   ├── security/                      # 安全配置
│   │   │   ├── service/                       # 业务服务层
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yaml               # 主配置文件
│   │       ├── application-local.yaml         # 本地环境配置
│   │       ├── bootstrap.yaml                 # 引导配置
│   │       └── mapper/                        # MyBatis XML映射
│   └── test/                                  # 测试代码
├── scripts/                                   # 脚本文件
├── docker-compose.yaml                        # Docker容器编排
├── Dockerfile                                 # Docker镜像构建
├── pom.xml                                    # Maven配置
└── README.md                                  # 项目说明
```

## 🔧 环境要求

### 基础环境
- **JDK 17** 或更高版本
- **Maven 3.6+** 项目构建工具
- **Git** 版本控制

### 数据库环境
- **MySQL 8.0+** 主数据库
- **Redis 6.0+** 缓存数据库

### 可选环境
- **Docker** & **Docker Compose** 容器化部署
- **Nacos** 服务注册与配置中心（生产环境推荐）

## 🚀 快速开始

### 方式一：本地开发环境

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 准备数据库
```sql
# 连接MySQL并创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. 配置环境变量
```bash
# 设置必要的环境变量
export JWT_SECRET=your_jwt_secret_key_here
export SPRING_PROFILES_ACTIVE=local

# 可选：配置管理员账户
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
```

#### 4. 启动Redis服务
```bash
# 使用Docker启动Redis（推荐）
docker run -d -p 6379:6379 --name redis redis:latest

# 或使用本地Redis服务
redis-server
```

#### 5. 运行应用
```bash
# 使用Maven运行
mvn clean spring-boot:run

# 或者先打包再运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 方式二：Docker Compose（推荐）

#### 1. 使用Docker Compose启动所有服务
```bash
# 启动MySQL和Redis
docker-compose --profile all up -d

# 只启动MySQL（如果已有Redis服务）
docker-compose --profile without-redis up -d
```

#### 2. 配置环境变量并运行应用
```bash
export JWT_SECRET=your_jwt_secret_key_here
mvn clean spring-boot:run
```

## 🔧 配置说明

### 数据库配置
应用支持通过环境变量或配置文件进行数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

### Redis配置
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
```

### JWT配置
```yaml
jwt:
  secret: ${JWT_SECRET}  # 必须设置环境变量
  expiration: 86400      # 24小时过期
```

### Nacos配置（可选）
```yaml
spring:
  cloud:
    nacos:
      discovery:
        enabled: ${NACOS_ENABLED:false}
        register-enabled: ${NACOS_ENABLED:false}
```

## 📚 API文档

应用启动后，可以通过以下方式访问API：

- **应用地址**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **应用信息**: http://localhost:8080/actuator/info

### 主要API端点

| 功能模块 | 端点路径 | 描述 |
|---------|---------|-----|
| 用户管理 | `/api/users/*` | 用户注册、登录、信息管理 |
| 商品管理 | `/api/products/*` | 商品CRUD、分类管理 |
| 购物车 | `/api/cart/*` | 购物车操作 |
| 订单管理 | `/api/orders/*` | 订单创建、查询、状态更新 |
| 支付 | `/api/payments/*` | 支付处理 |

## 🐳 Docker部署

### 构建Docker镜像
```bash
# 构建应用镜像
docker build -t online-store:latest .

# 运行容器
docker run -d -p 8080:8080 \
  -e JWT_SECRET=your_secret_key \
  -e SPRING_PROFILES_ACTIVE=prod \
  --name online-store \
  online-store:latest
```

### 使用Docker Compose部署完整环境
```bash
# 部署包含MySQL和Redis的完整环境
docker-compose --profile all up -d
```

## 🧪 测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 生成测试报告
mvn surefire-report:report
```

## 📈 监控与日志

### Spring Boot Actuator端点
- `/actuator/health` - 健康检查
- `/actuator/info` - 应用信息
- `/actuator/metrics` - 应用指标
- `/actuator/env` - 环境信息

### 日志配置
应用使用SLF4J + Logback进行日志管理，日志级别可通过配置文件调整。

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 代码规范
- 遵循Java编码规范
- 使用Lombok减少样板代码
- 编写单元测试
- 添加必要的注释和文档

## 📝 版本历史

- **1.0-SNAPSHOT** - 初始版本
  - 基础用户管理功能
  - 商品管理系统
  - 订单处理流程
  - JWT认证机制

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 创建 [Issue](../../issues)
- 发送邮件到 [your-email@example.com]

## 🙏 致谢

感谢以下开源项目和社区：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [MyBatis](https://mybatis.org/)
- [Nacos](https://nacos.io/)
- [Alibaba Cloud](https://www.alibabacloud.com/)