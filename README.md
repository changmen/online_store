# 🛒 Online Store - 在线商店系统

<div align="center">
  <h3>基于Spring Cloud微服务架构的现代化电商平台</h3>
  <img src="https://img.shields.io/badge/Java-17-orange" alt="Java Version">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen" alt="Spring Boot Version">
  <img src="https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue" alt="Spring Cloud Version">
  <img src="https://img.shields.io/badge/License-MIT-yellow" alt="License">
</div>

## 📖 项目介绍

Online Store 是一个功能完整的在线商店系统，采用现代化的微服务架构设计。系统提供了完整的电商功能，包括用户管理、商品管理、订单处理、支付集成等核心模块。

### ✨ 主要特性

- 🔐 **安全认证**: 基于JWT的用户认证和授权机制
- 🛍️ **商品管理**: 完整的商品分类、库存管理功能
- 🛒 **购物车**: 灵活的购物车操作和管理
- 📦 **订单系统**: 完整的订单生命周期管理
- 💳 **支付集成**: 支持多种支付方式
- 📊 **数据分析**: 实时的业务数据统计
- 🌐 **国际化**: 支持多语言切换
- ☁️ **云存储**: 集成阿里云OSS文件存储

## 🛠️ 技术栈

### 后端技术
- **Java 17** - 编程语言
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **MyBatis 3.0.3** - ORM框架
- **PageHelper 2.1.0** - 分页插件

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis 5.2.0** - 缓存数据库

### 服务治理
- **Nacos 2.2.0** - 服务注册与配置中心
- **Spring Cloud Alibaba 2022.0.0.0** - 阿里云微服务组件

### 工具库
- **JWT (JJWT 0.11.5)** - Token认证
- **Lombok 1.18.36** - 代码简化
- **Apache Commons Lang3 3.17.0** - 工具类库
- **Aliyun OSS 3.18.1** - 阿里云对象存储

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # 启动类
│   │   │   ├── bean/                          # Bean配置
│   │   │   ├── config/                        # 配置类
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # 控制器层
│   │   │   ├── dto/                          # 数据传输对象
│   │   │   ├── entity/                       # 实体类
│   │   │   ├── enums/                        # 枚举类
│   │   │   ├── exceptions/                   # 异常处理
│   │   │   ├── handler/                      # 处理器
│   │   │   ├── mapper/                       # 数据访问层
│   │   │   ├── security/                     # 安全配置
│   │   │   ├── service/                      # 业务逻辑层
│   │   │   └── utils/                        # 工具类
│   │   └── resources/
│   │       ├── application.yaml              # 主配置文件
│   │       ├── application-local.yaml        # 本地环境配置
│   │       ├── bootstrap.yaml                # 启动配置
│   │       ├── i18n/                        # 国际化资源
│   │       ├── mapper/                       # MyBatis映射文件
│   │       └── sql/                         # SQL脚本
│   └── test/                                 # 测试代码
├── scripts/                                  # 脚本文件
├── docker-compose.yaml                       # Docker编排文件
├── Dockerfile                               # Docker镜像构建文件
├── pom.xml                                  # Maven配置文件
└── README.md                                # 项目文档
```

## 🚀 快速开始

### 环境要求

- **JDK 17+** (推荐使用 OpenJDK 17)
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis 6.0+**
- **Nacos 2.2.0+** (可选，用于服务注册和配置管理)

### 🐳 Docker 快速部署

1. **克隆项目**
```bash
git clone <repository-url>
cd online-store
```

2. **启动基础服务**
```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 或者只启动 MySQL
docker-compose --profile without-redis up -d
```

3. **初始化数据库**
```sql
-- 连接到MySQL后执行
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入初始化脚本（如果有的话）
USE online_store;
source src/main/resources/sql/init.sql;
```

### 💻 本地开发部署

1. **配置数据库**
   - 创建 `online_store` 数据库
   - 执行 `src/main/resources/sql/` 目录下的SQL脚本

2. **配置 Redis**
   - 启动 Redis 服务 (默认端口 6379)

3. **修改配置文件**
   - 复制 `application-local.yaml` 并根据你的环境修改数据库和Redis连接信息

4. **启动应用**
```bash
# 使用Maven启动
mvn clean spring-boot:run -Dspring-boot.run.profiles=local

# 或者使用Java启动
mvn clean package
java -jar target/online-store-1.0-SNAPSHOT.jar --spring.profiles.active=local
```

5. **验证部署**
   - 访问健康检查端点: http://localhost:8080/actuator/health
   - 查看应用信息: http://localhost:8080/actuator/info

## 📚 API 文档

应用启动后，可通过以下方式查看API文档：

- **Health Check**: `GET /actuator/health`
- **Application Info**: `GET /actuator/info`
- **Metrics**: `GET /actuator/metrics`

> 💡 建议集成 Swagger/OpenAPI 3.0 来生成完整的API文档

## 🔧 开发指南

### 代码规范
- 使用 Lombok 简化实体类代码
- 遵循 RESTful API 设计规范
- 使用统一的异常处理机制
- 合理使用缓存策略

### 测试
```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 生成测试报告
mvn surefire-report:report
```

### 构建和打包
```bash
# 构建项目
mvn clean compile

# 打包应用
mvn clean package

# 跳过测试打包
mvn clean package -DskipTests
```

## 🐳 Docker 部署

### 构建镜像
```bash
# 构建应用镜像
docker build -t online-store:latest .

# 运行容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  online-store:latest
```

### 使用 Docker Compose
```bash
# 启动完整的应用栈
docker-compose --profile all up -d

# 查看服务状态
docker-compose ps

# 查看应用日志
docker-compose logs -f
```

## 🛡️ 安全配置

- 使用 JWT 进行用户认证
- 实现基于角色的访问控制 (RBAC)
- 数据库连接使用加密传输
- 敏感信息通过环境变量或配置中心管理

## 📊 监控和日志

- **应用监控**: 集成 Spring Boot Actuator
- **性能监控**: 建议集成 Micrometer + Prometheus
- **日志管理**: 使用 Logback 进行日志配置
- **链路追踪**: 可集成 Spring Cloud Sleuth

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📝 更新日志

- **v1.0.0** (2024-09-24)
  - 初始版本发布
  - 实现基础电商功能
  - 集成Spring Security和JWT认证
  - 支持Docker部署

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 📧 Email: your-email@example.com
- 🐛 Issues: [GitHub Issues](https://github.com/your-username/online-store/issues)
- 📖 Wiki: [项目Wiki](https://github.com/your-username/online-store/wiki)

---

<div align="center">
  <p>⭐ 如果这个项目对你有帮助，请给它一个星标！</p>
</div> 