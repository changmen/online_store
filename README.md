# 在线商店系统 (Online Store)

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)
![Status](https://img.shields.io/badge/Status-开发中-green)

</div>

## 📋 项目简介

这是一个基于 Spring Boot 和 Spring Cloud 的现代化在线商店系统，采用微服务架构设计，提供完整的电商功能。系统支持商品管理、用户管理、品牌管理、分类管理等核心功能，并集成了 JWT 身份认证、Redis 缓存、OSS 文件存储等企业级特性。

## ✨ 主要特性

### 🛍️ 商品管理
- 商品信息管理（增删改查）
- 商品详情展示
- SKU 规格管理
- 商品属性与属性值管理
- 商品访问日志记录

### 👥 用户管理
- 用户注册与登录
- JWT Token 身份认证
- 用户信息管理
- Spring Security 安全框架

### 🏷️ 品牌与分类
- 品牌信息管理
- 商品分类管理
- 品牌与分类关联

### 🚀 技术特性
- RESTful API 设计
- Redis 缓存优化
- 分页查询支持
- 统一异常处理
- 国际化支持（i18n）
- Docker 容器化支持
- 阿里云 OSS 文件存储

## 🛠️ 技术栈

### 后端框架
- **Java 17** - 编程语言
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Data Redis** - Redis 集成

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis 5.2.0** - 缓存数据库
- **MyBatis 3.0.3** - ORM 框架
- **PageHelper 2.1.0** - 分页插件

### 服务治理
- **Nacos 2.2.0** - 服务注册与配置中心
- **Spring Cloud Alibaba** - 阿里云微服务组件

### 工具库
- **JWT 0.11.5** - Token 认证
- **Jackson** - JSON 处理
- **Lombok** - 代码简化
- **Apache Commons** - 工具类库
- **Aliyun OSS** - 对象存储服务

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── bean/                    # 业务实体类
│   │   ├── Item.java           # 商品实体
│   │   ├── Member.java         # 用户实体
│   │   ├── Brand.java          # 品牌实体
│   │   └── ...
│   ├── controller/             # 控制器层
│   │   ├── ItemController.java # 商品控制器
│   │   ├── MemberController.java # 用户控制器
│   │   └── ...
│   ├── service/                # 服务层
│   │   ├── impl/              # 服务实现
│   │   ├── ItemService.java   # 商品服务
│   │   └── ...
│   ├── mapper/                 # 数据访问层
│   ├── dto/                    # 数据传输对象
│   ├── entity/                 # 数据库实体
│   ├── config/                 # 配置类
│   ├── security/               # 安全相关
│   ├── utils/                  # 工具类
│   └── OnlineStoreApplication.java # 启动类
├── src/main/resources/
│   ├── mapper/                 # MyBatis 映射文件
│   ├── sql/                    # 数据库脚本
│   ├── i18n/                   # 国际化资源
│   └── application.yaml        # 配置文件
├── docker-compose.yaml         # Docker Compose 配置
├── Dockerfile                  # Docker 镜像构建
└── pom.xml                     # Maven 配置
```

## 🚀 快速开始

### 环境要求

- **JDK 17** 或更高版本
- **Maven 3.6+** 构建工具
- **MySQL 8.0+** 数据库
- **Redis 6.0+** 缓存服务
- **Docker** (可选)

### 方式一：本地运行

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd online_store
   ```

2. **准备数据库**
   ```sql
   # 创建数据库
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # 执行建表脚本
   # 运行 src/main/resources/sql/ 目录下的所有 SQL 文件
   ```

3. **配置环境变量**
   ```bash
   export JWT_SECRET=your-jwt-secret-key-here
   export SPRING_PROFILES_ACTIVE=local
   ```

4. **修改配置文件**
   
   编辑 `src/main/resources/application-local.yaml`，配置数据库和 Redis 连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/online_store
       username: your-username
       password: your-password
     data:
       redis:
         host: localhost
         port: 6379
   ```

5. **启动应用**
   ```bash
   # 添加 JVM 参数
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
   ```

### 方式二：Docker 运行

1. **启动依赖服务**
   ```bash
   # 启动 MySQL 和 Redis
   docker-compose --profile all up -d
   ```

2. **构建并运行应用**
   ```bash
   # 构建镜像
   docker build -t online-store .
   
   # 运行应用
   docker run -p 8080:8080 --network online_store_default online-store
   ```

### 访问应用

- **应用地址**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **API 文档**: 查看各 Controller 类了解可用接口

## 📊 API 接口

### 用户相关
- `POST /api/v1/members/registry` - 用户注册
- `POST /api/v1/members/login` - 用户登录

### 商品相关
- `GET /api/v1/items/{itemId}` - 获取商品信息
- `GET /api/v1/items/{itemId}/detail` - 获取商品详情

### 品牌相关
- `GET /api/v1/brands` - 品牌列表
- `POST /api/v1/brands` - 创建品牌
- `PUT /api/v1/brands/{brandId}` - 更新品牌
- `DELETE /api/v1/brands/{brandId}` - 删除品牌

### 属性相关
- `POST /api/v1/attributes` - 创建属性
- `GET /api/v1/attributes/{attributeId}` - 获取属性
- `PUT /api/v1/attributes/{attributeId}` - 更新属性

## 🔧 配置说明

### 环境配置

系统支持多环境配置，通过 `SPRING_PROFILES_ACTIVE` 环境变量切换：

- `local` - 本地开发环境
- `dev` - 开发环境  
- `test` - 测试环境
- `prod` - 生产环境

### 主要配置项

```yaml
# JWT 配置
jwt:
  secret: ${JWT_SECRET}           # JWT 密钥
  expiration: 86400               # Token 过期时间（秒）

# Nacos 配置
spring:
  cloud:
    nacos:
      discovery:
        enabled: ${NACOS_ENABLED:false}  # 是否启用 Nacos

# 缓存配置
async-record-access-log: true     # 是否异步记录访问日志
```

## 🧪 测试

```bash
# 运行所有测试
mvn test

# 运行特定测试
mvn test -Dtest=MessageSourceTest
```

## 📦 部署

### Docker 部署

```bash
# 构建镜像
docker build -t online-store:latest .

# 运行容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JWT_SECRET=your-production-secret \
  online-store:latest
```

### 生产环境部署建议

1. 使用外部 MySQL 和 Redis 服务
2. 配置适当的 JVM 参数
3. 启用 HTTPS
4. 配置日志收集
5. 设置监控和告警

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目基于 MIT 许可证开源 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 📧 Email: your-email@example.com
- 🐛 Issues: [GitHub Issues](https://github.com/your-username/online-store/issues)
- 📖 Wiki: [项目文档](https://github.com/your-username/online-store/wiki)

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者！

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！ 