# 在线商店系统 (Online Store)

一个基于 Spring Boot 3.x 和 Spring Cloud 的现代化电商系统，提供完整的商品管理、品牌管理、分类管理和会员管理功能。

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)

## ✨ Features

- 🛍️ **商品管理**：商品信息管理、SKU管理、商品属性配置
- 🏷️ **品牌管理**：品牌信息维护、品牌与商品关联
- 📂 **分类管理**：商品分类层级管理
- 👥 **会员管理**：用户注册、登录、个人信息管理
- 🔐 **安全认证**：基于JWT的安全认证机制
- 📊 **数据统计**：商品访问日志、行为分析
- 🚀 **云原生**：支持Docker容器化部署
- 📈 **监控健康检查**：Spring Boot Actuator集成

## 🛠️ Tech Stack

### Core Framework
- **Java 17** - 现代化的Java开发平台
- **Spring Boot 3.4.3** - 简化Spring应用开发
- **Spring Cloud 2024.0.0** - 微服务架构支持
- **Spring Security** - 安全认证框架

### Data Storage
- **MySQL 8.2.0** - 关系型数据库
- **Redis (Jedis 5.2.0)** - 缓存和会话存储
- **MyBatis 3.0.3** - 持久层框架
- **PageHelper 2.1.0** - 分页插件

### Service Discovery & Configuration
- **Nacos 2.2.0** - 服务注册发现和配置管理
- **Spring Cloud Alibaba** - 阿里云生态集成

### Utilities
- **Lombok** - 简化Java代码
- **JWT (jjwt 0.11.5)** - JSON Web Token认证
- **Apache Commons Lang3** - 通用工具类
- **Aliyun OSS** - 阿里云对象存储

## 📁 Project Structure

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # 启动类
│   │   │   ├── controller/                     # 控制器层
│   │   │   │   ├── ItemController.java        # 商品管理
│   │   │   │   ├── BrandController.java       # 品牌管理
│   │   │   │   ├── CategoryController.java    # 分类管理
│   │   │   │   ├── MemberController.java      # 会员管理
│   │   │   │   ├── AttributeController.java   # 属性管理
│   │   │   │   └── ItemDetailController.java  # 商品详情
│   │   │   ├── service/                       # 业务服务层
│   │   │   ├── mapper/                        # 数据访问层
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── config/                        # 配置类
│   │   │   ├── security/                      # 安全配置
│   │   │   ├── utils/                         # 工具类
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── exceptions/                    # 异常处理
│   │   │   └── constants/                     # 常量定义
│   │   └── resources/
│   │       ├── application.yml                # 主配置文件
│   │       └── mapper/                        # MyBatis映射文件
│   └── test/                                  # 测试代码
├── scripts/                                   # 脚本文件
├── docker-compose.yaml                        # Docker编排文件
├── Dockerfile                                 # Docker镜像构建
├── pom.xml                                    # Maven依赖配置
└── README.md                                  # 项目说明文档
```

## ⚡ Quick Start

### Prerequisites

- ☕ **JDK 17** 或更高版本
- 📦 **Maven 3.6+** 或 **Gradle 7.0+**
- 🗄️ **MySQL 8.0+**
- 🚀 **Redis 6.0+**
- 🐳 **Docker & Docker Compose** (可选)

### Local Development Setup

#### Option 1: Using Docker Compose (Recommended)

1. **启动基础服务**
   ```bash
   # 启动 MySQL 和 Redis
   docker-compose --profile all up -d
   
   # 或仅启动 MySQL
   docker-compose --profile without-redis up -d
   ```

2. **创建数据库**
   ```sql
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **配置应用**
   - 复制 `src/main/resources/application-example.yml` 为 `application.yml`
   - 根据实际环境修改数据库和Redis连接配置

4. **启动应用**
   ```bash
   # 使用Maven
   mvn spring-boot:run
   
   # 或使用Java直接运行
   mvn clean package
   java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
   ```

#### Option 2: Manual Installation

1. **安装并启动 MySQL**
   ```bash
   # 创建数据库
   mysql -u root -p
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **安装并启动 Redis**
   ```bash
   # Ubuntu/Debian
   sudo apt-get install redis-server
   sudo systemctl start redis-server
   
   # macOS
   brew install redis
   brew services start redis
   ```

3. **配置和启动应用**（同上述步骤3-4）

### 🔧 Configuration

主要配置项位于 `src/main/resources/application.yml`：

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    
# Redis配置
  redis:
    host: localhost
    port: 6379
    password: # Redis密码（如果有）
    
# Nacos配置（如果使用）
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
```

## 🚀 Deployment

### Docker Deployment

1. **构建镜像**
   ```bash
   docker build -t online-store:latest .
   ```

2. **运行容器**
   ```bash
   docker run -d \
     --name online-store \
     -p 8080:8080 \
     -e SPRING_PROFILES_ACTIVE=prod \
     online-store:latest
   ```

## 📖 API Documentation

应用启动后，可以通过以下方式访问：

- **应用首页**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **API接口**: 详见各Controller类的接口定义

### Main APIs

| 模块 | 接口路径 | 说明 |
|------|----------|------|
| 商品管理 | `/api/items/*` | 商品CRUD操作 |
| 品牌管理 | `/api/brands/*` | 品牌信息管理 |
| 分类管理 | `/api/categories/*` | 商品分类管理 |
| 会员管理 | `/api/members/*` | 用户信息管理 |
| 属性管理 | `/api/attributes/*` | 商品属性配置 |

## 🧪 Testing

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=ItemControllerTest

# 生成测试报告
mvn surefire-report:report
```

## 🔍 Monitoring & Operations

### Health Check
- **端点**: `/actuator/health`
- **详细信息**: `/actuator/info`
- **指标监控**: `/actuator/metrics`

### Logging Configuration
项目使用 Logback 进行日志管理，日志级别可在配置文件中调整。

## 🤝 Contributing

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📝 Changelog

详见 [CHANGELOG.md](CHANGELOG.md)

## 📄 License

本项目采用 [MIT License](LICENSE) 许可证。

## 📞 Contact

如有问题或建议，请通过以下方式联系：

- 📧 Email: [your-email@example.com]
- 🐛 Issue: [GitHub Issues](https://github.com/your-username/online-store/issues)

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！ 