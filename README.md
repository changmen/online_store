# 🛒 企业级在线商城系统 | Enterprise Online Store Platform

> 基于 Spring Cloud 微服务架构的现代电商解决方案，集成商品管理、用户系统、安全认证等核心功能

## ✨ 项目特色

- 🏗️ **微服务架构**：采用Spring Cloud生态构建分布式系统
- 🔐 **安全认证**：集成Spring Security + JWT认证体系
- 📦 **商品管理**：完整的商品分类、品牌、属性管理系统
- 👤 **用户体系**：会员注册、登录、权限管理
- 🚀 **容器化部署**：支持Docker和Docker Compose一键部署
- ☁️ **云原生**：集成Nacos服务发现与配置中心
- 📊 **监控运维**：Spring Boot Actuator健康检查
- 🗃️ **对象存储**：集成阿里云OSS文件存储服务

## 🛠️ 技术栈

### 核心框架
- **JDK 17** - Java开发平台
- **Spring Boot 3.4.3** - 微服务基础框架
- **Spring Cloud 2024.0.0** - 微服务治理
- **Spring Security** - 安全认证框架

### 数据层
- **MyBatis 3.0.3** - ORM持久化框架
- **PageHelper 2.1.0** - 分页插件
- **MySQL 8.2.0** - 关系型数据库
- **Redis 5.2.0 (Jedis)** - 缓存数据库

### 微服务组件
- **Nacos 2.2.0** - 服务注册与配置中心
- **Spring Cloud Alibaba** - 阿里云微服务套件

### 工具库
- **Lombok 1.18.36** - Java代码简化
- **JWT 0.11.5** - Token认证
- **Apache Commons** - 通用工具库
- **Aliyun OSS 3.18.1** - 对象存储服务

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java    # 应用启动类
│   ├── controller/                    # REST API控制层
│   │   ├── AttributeController.java   # 商品属性管理
│   │   ├── BrandController.java       # 品牌管理
│   │   ├── CategoryController.java    # 商品分类管理
│   │   ├── ItemController.java        # 商品管理
│   │   ├── ItemDetailController.java  # 商品详情管理
│   │   └── MemberController.java      # 会员管理
│   ├── service/                       # 业务逻辑层
│   ├── mapper/                        # 数据访问层
│   ├── entity/                        # 实体类
│   ├── dto/                          # 数据传输对象
│   ├── config/                       # 配置类
│   ├── security/                     # 安全配置
│   ├── utils/                        # 工具类
│   ├── enums/                        # 枚举类
│   └── exceptions/                   # 异常处理
├── src/main/resources/
│   ├── application.yml               # 主配置文件
│   ├── bootstrap.yml                 # 启动配置
│   └── mapper/                       # MyBatis映射文件
├── scripts/                          # 数据库脚本
├── Dockerfile                        # Docker构建文件
├── docker-compose.yaml              # 容器编排配置
└── pom.xml                          # Maven项目配置
```

## 📋 环境要求

### 开发环境
- **JDK 17+** - Java开发环境
- **Maven 3.6+** - 项目构建工具
- **Git** - 版本控制
- **IDE** - IntelliJ IDEA / Eclipse

### 运行环境
- **MySQL 8.0+** - 主数据库
- **Redis 6.0+** - 缓存数据库
- **Nacos 2.2.0+** - 服务注册中心（可选）

### 容器环境（可选）
- **Docker** - 容器化部署
- **Docker Compose** - 容器编排

## 🚀 快速开始

### 方式一：本地开发环境

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd online_store
   ```

2. **启动基础服务**
   ```bash
   # 启动MySQL和Redis（使用Docker Compose）
   docker-compose up -d mysql redis
   ```

3. **创建数据库**
   ```sql
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

4. **配置应用**
   - 修改 `src/main/resources/application.yml` 中的数据库连接信息
   - 配置Redis连接参数
   - 配置Nacos服务地址（如需要）

5. **运行应用**
   ```bash
   # 添加JVM参数并启动
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
   ```

### 方式二：Docker容器化部署

```bash
# 构建应用镜像
docker build -t online-store:latest .

# 启动完整服务栈
docker-compose --profile all up -d
```

### 方式三：IDE开发

1. 导入Maven项目到IDE
2. 配置JVM参数：`--add-opens java.base/java.lang=ALL-UNNAMED`
3. 启动 `OnlineStoreApplication.java`

## 📱 API文档

应用启动后，可通过以下方式访问：

- **应用地址**：http://localhost:8080
- **健康检查**：http://localhost:8080/actuator/health
- **应用信息**：http://localhost:8080/actuator/info

### 主要API端点

| 模块 | 端点 | 描述 |
|------|------|------|
| 商品管理 | `/api/items` | 商品CRUD操作 |
| 品牌管理 | `/api/brands` | 品牌信息管理 |
| 分类管理 | `/api/categories` | 商品分类管理 |
| 会员管理 | `/api/members` | 用户账户管理 |
| 商品属性 | `/api/attributes` | 商品属性配置 |

## 🔧 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

### Redis配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    database: 0
```

### Nacos配置
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
      config:
        server-addr: 127.0.0.1:8848
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系我们

如有问题或建议，请通过以下方式联系：

- 📧 Email: [your-email@example.com]
- 🐛 Issues: [GitHub Issues](https://github.com/your-repo/issues)
- 📖 Wiki: [项目文档](https://github.com/your-repo/wiki)

---

⭐ 如果这个项目对你有帮助，请给我们一个星标！ 