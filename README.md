# 🛒 Online Store - 在线商店系统

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)

一个功能完整的现代化在线商店系统，基于Spring Cloud微服务架构构建，支持商品管理、用户管理、购物车、订单处理等核心电商功能。

## ✨ 功能特性

### 🛍️ 核心业务功能
- **商品管理**：商品分类、品牌管理、商品属性、SKU管理
- **库存管理**：实时库存跟踪、库存预警
- **用户系统**：用户注册、登录、个人资料管理
- **购物车**：添加商品、数量调整、批量操作
- **订单系统**：订单创建、状态跟踪、支付集成
- **访问统计**：商品访问日志记录和分析

### 🔧 技术特性
- **微服务架构**：基于Spring Cloud构建
- **服务发现**：集成Nacos服务注册与发现
- **安全认证**：Spring Security + JWT令牌认证
- **缓存系统**：Redis缓存提升性能
- **持久化**：MyBatis + MySQL数据持久化
- **文件存储**：阿里云OSS对象存储
- **容器化部署**：Docker + Docker Compose
- **多环境配置**：支持local、dev、prod等多环境
- **国际化支持**：多语言资源文件

## 🚀 技术栈

### 后端技术
- **Java 17** - 编程语言
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Data Redis** - Redis集成
- **MyBatis 3.0.3** - 持久层框架
- **PageHelper** - 分页插件

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis 5.2.0** - 缓存数据库
- **阿里云OSS** - 对象存储服务

### 中间件与工具
- **Nacos 2.2.0** - 服务注册与配置中心
- **JWT 0.11.5** - 令牌认证
- **Lombok** - 代码简化工具
- **Apache Commons** - 工具类库

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 启动类
│   │   │   ├── bean/                          # 配置Bean
│   │   │   ├── config/                        # 配置类
│   │   │   ├── controller/                    # 控制层
│   │   │   │   ├── AttributeController.java   # 属性管理
│   │   │   │   ├── BrandController.java       # 品牌管理
│   │   │   │   ├── CategoryController.java    # 分类管理
│   │   │   │   ├── ItemController.java        # 商品管理
│   │   │   │   ├── MemberController.java      # 用户管理
│   │   │   │   └── SkuController.java         # SKU管理
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── mapper/                        # 数据访问层
│   │   │   ├── security/                      # 安全配置
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yaml               # 主配置文件
│   │       ├── application-local.yaml         # 本地环境配置
│   │       ├── bootstrap.yaml                 # 引导配置
│   │       ├── i18n/                         # 国际化资源
│   │       ├── mapper/                       # MyBatis映射文件
│   │       └── sql/                          # 数据库初始化脚本
│   └── test/                                 # 测试代码
├── scripts/                                  # 脚本文件
├── docker-compose.yaml                       # Docker Compose配置
├── Dockerfile                               # Docker镜像构建文件
└── pom.xml                                  # Maven配置文件
```

## 🚀 快速开始

### 环境要求

- **JDK 17+**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis 6.0+**
- **Docker & Docker Compose** (可选)

### 方式一：本地开发环境

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 数据库准备
```bash
# 启动MySQL和Redis（使用Docker Compose）
docker-compose up -d mysql redis

# 或者手动启动MySQL服务，然后创建数据库
mysql -u root -p
```

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. 初始化数据库表
```bash
# 执行SQL初始化脚本
mysql -u root -p online_store < src/main/resources/sql/
```

#### 4. 配置环境变量
```bash
# 设置JWT密钥
export JWT_SECRET="your-jwt-secret-key-here"

# 或者在application-local.yaml中配置数据库和Redis连接信息
```

#### 5. 启动应用
```bash
# 使用Maven启动
mvn clean spring-boot:run

# 或者编译后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 方式二：Docker部署

```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps
```

## 🔧 配置说明

### 环境配置

- **本地开发环境**：`application-local.yaml`
- **生产环境**：通过环境变量或Nacos配置中心

### 关键配置项

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
    username: root
    password: 123456

# Redis配置
  data:
    redis:
      host: localhost
      port: 6379

# JWT配置
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400  # 24小时
```

## 📚 API文档

应用启动后，访问以下地址：

- **应用首页**：http://localhost:8080
- **健康检查**：http://localhost:8080/actuator/health
- **应用信息**：http://localhost:8080/actuator/info

### 主要API端点

| 模块 | 端点 | 描述 |
|------|------|------|
| 商品管理 | `/api/items` | 商品CRUD操作 |
| SKU管理 | `/api/skus` | SKU管理 |
| 分类管理 | `/api/categories` | 商品分类 |
| 品牌管理 | `/api/brands` | 品牌管理 |
| 用户管理 | `/api/members` | 用户相关操作 |
| 属性管理 | `/api/attributes` | 商品属性管理 |

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn integration-test

# 生成测试报告
mvn site
```

## 🚀 部署

### 生产环境部署建议

1. **使用外部数据库和Redis集群**
2. **配置SSL证书**
3. **设置负载均衡**
4. **配置监控和日志收集**
5. **启用Nacos配置中心**

### Docker生产部署

```bash
# 构建生产镜像
docker build -t online-store:latest .

# 使用生产配置启动
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JWT_SECRET=your-production-secret \
  online-store:latest
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 🙋‍♂️ 支持

如果您在使用过程中遇到问题，请：

1. 查看 [FAQ](docs/FAQ.md)
2. 搜索现有的 [Issues](../../issues)
3. 创建新的 Issue 描述问题

## 📈 路线图

- [ ] 支付系统集成
- [ ] 物流系统对接
- [ ] 移动端API优化
- [ ] 性能监控仪表板
- [ ] 多租户支持
- [ ] 消息队列集成
- [ ] 搜索引擎优化 