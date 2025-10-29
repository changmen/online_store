# 🛒 在线商店系统 (Online Store System)

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)

一个功能完整的现代企业级在线商店系统，基于Spring Cloud微服务架构构建，集成了商品管理、用户认证、订单处理等核心电商功能。

## ✨ 核心功能特性

### 🛍️ 商品管理
- **商品信息管理**: 商品的基本信息、详情、规格等全方位管理
- **品牌管理**: 品牌信息维护和品牌关联商品管理
- **分类管理**: 多级商品分类体系，支持灵活的分类组织
- **属性管理**: 动态商品属性配置，支持各种商品规格
- **SKU管理**: 库存单位管理，支持多规格商品

### 👥 用户系统
- **用户注册登录**: 完整的用户认证体系
- **JWT安全认证**: 基于JSON Web Token的安全认证机制
- **用户权限管理**: 细粒度的权限控制

### 📊 系统监控
- **访问日志**: 商品访问统计和用户行为分析
- **系统健康检查**: Spring Boot Actuator集成的系统监控

## 🏗️ 技术架构

### 核心技术栈
- **☕ Java 17**: 采用最新LTS版本，享受现代Java特性
- **🌱 Spring Boot 3.4.3**: 最新版本Spring Boot框架
- **☁️ Spring Cloud 2024.0.0**: 微服务架构支持
- **🔒 Spring Security**: 企业级安全框架
- **🏛️ MyBatis 3.0.3**: 持久层框架，支持灵活的SQL映射
- **🗄️ MySQL 8.2.0**: 关系型数据库，支持JSON字段等现代特性
- **⚡ Redis**: 高性能缓存和会话存储
- **📱 Nacos**: 服务发现和配置管理

### 核心依赖
```xml
<!-- 核心框架 -->
Spring Boot 3.4.3
Spring Cloud 2024.0.0
Spring Security (JWT认证)
MyBatis Spring Boot Starter 3.0.2

<!-- 数据存储 -->
MySQL Connector 8.2.0
Jedis 5.2.0
Spring Data Redis

<!-- 微服务组件 -->
Nacos Config & Discovery 2.2.0
Spring Cloud Alibaba 2022.0.0.0

<!-- 工具库 -->
Lombok 1.18.36
PageHelper 2.1.0
Apache Commons Lang3 3.17.0
Aliyun OSS SDK 3.18.1
```

## 📁 项目结构

```
online-store/
├── 📦 src/main/java/com/example/onlinestore/
│   ├── 🚀 OnlineStoreApplication.java     # 应用程序入口
│   ├── 🎮 controller/                     # REST API控制器
│   │   ├── AttributeController.java      # 属性管理接口
│   │   ├── BrandController.java          # 品牌管理接口
│   │   ├── CategoryController.java       # 分类管理接口
│   │   ├── ItemController.java           # 商品管理接口
│   │   ├── ItemDetailController.java     # 商品详情接口
│   │   └── MemberController.java         # 用户管理接口
│   ├── 🏢 service/                        # 业务逻辑层
│   ├── 🗃️ mapper/                         # 数据访问层
│   ├── 📊 entity/                         # 数据实体类
│   │   ├── AttributeEntity.java          # 属性实体
│   │   ├── BrandEntity.java              # 品牌实体
│   │   ├── CategoryEntity.java           # 分类实体
│   │   ├── ItemEntity.java               # 商品实体
│   │   ├── MemberEntity.java             # 用户实体
│   │   └── SkuEntity.java                # SKU实体
│   ├── 📝 dto/                            # 数据传输对象
│   ├── ⚙️ config/                         # 配置类
│   ├── 🔒 security/                       # 安全相关
│   ├── 🛠️ utils/                          # 工具类
│   ├── 🎯 enums/                          # 枚举类
│   ├── ⚠️ exceptions/                     # 异常处理
│   └── 🔧 handler/                        # 处理器
├── 📋 src/main/resources/
│   ├── application.yaml                  # 主配置文件
│   ├── application-local.yaml            # 本地环境配置
│   ├── bootstrap.yaml                    # 引导配置
│   ├── 🗺️ mapper/                         # MyBatis映射文件
│   ├── 💾 sql/                            # 数据库脚本
│   └── 🌐 i18n/                           # 国际化资源
├── 🐳 docker-compose.yaml                # Docker编排文件
├── 📦 Dockerfile                         # Docker镜像构建文件
├── 🔧 scripts/                           # 脚本工具
└── 📋 pom.xml                            # Maven项目配置
```

## 🚀 快速开始

### 环境要求

- ☕ **JDK 17+**: 确保安装Oracle JDK或OpenJDK 17及以上版本
- 🔨 **Maven 3.6+**: 用于项目构建和依赖管理
- 🗄️ **MySQL 8.0+**: 关系型数据库
- ⚡ **Redis 6.0+**: 缓存服务器
- 🐳 **Docker & Docker Compose** (可选): 用于容器化部署

### 本地开发环境搭建

#### 方式一：使用Docker Compose (推荐)

1. **启动基础服务**
   ```bash
   # 启动MySQL和Redis
   docker-compose --profile all up -d
   
   # 仅启动MySQL (如果已有Redis服务)
   docker-compose --profile without-redis up -d
   ```

2. **初始化数据库**
   ```bash
   # 连接到MySQL容器
   docker exec -it $(docker-compose ps -q mysql) mysql -uroot -p123456
   
   # 创建数据库
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

#### 方式二：本地安装

1. **安装并启动MySQL**
   ```bash
   # Ubuntu/Debian
   sudo apt-get install mysql-server
   sudo systemctl start mysql
   
   # macOS (使用Homebrew)
   brew install mysql
   brew services start mysql
   
   # 创建数据库
   mysql -u root -p
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **安装并启动Redis**
   ```bash
   # Ubuntu/Debian
   sudo apt-get install redis-server
   sudo systemctl start redis
   
   # macOS (使用Homebrew)
   brew install redis
   brew services start redis
   ```

### 🔧 应用配置

1. **配置数据库连接**
   
   编辑 `src/main/resources/application-local.yaml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/online_store?useSSL=false&serverTimezone=UTC
       username: your_username
       password: your_password
     
     redis:
       host: localhost
       port: 6379
       password: # 如果Redis设置了密码
   ```

2. **Nacos配置** (如果使用Nacos)
   
   编辑 `src/main/resources/bootstrap.yaml`:
   ```yaml
   spring:
     cloud:
       nacos:
         config:
           server-addr: localhost:8848
         discovery:
           server-addr: localhost:8848
   ```

### 🏃‍♂️ 运行应用

1. **编译项目**
   ```bash
   mvn clean compile
   ```

2. **运行应用**
   ```bash
   # 使用Maven插件运行
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   
   # 或者先打包再运行
   mvn clean package -DskipTests
   java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar --spring.profiles.active=local
   ```

3. **验证服务启动**
   ```bash
   # 检查应用健康状态
   curl http://localhost:8080/actuator/health
   
   # 预期返回：{"status":"UP"}
   ```

## 🐳 Docker部署

### 构建Docker镜像

```bash
# 构建应用镜像
docker build -t online-store:latest .

# 或使用Maven插件构建
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=online-store:latest
```

### 使用Docker Compose部署完整环境

```yaml
# docker-compose.prod.yaml
version: "3.8"
services:
  app:
    image: online-store:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/online_store
      - SPRING_REDIS_HOST=redis
    depends_on:
      - mysql
      - redis
    
  mysql:
    image: mysql:8.2
    environment:
      MYSQL_ROOT_PASSWORD: "your_secure_password"
      MYSQL_DATABASE: "online_store"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql:/docker-entrypoint-initdb.d
    
  redis:
    image: redis:7-alpine
    volumes:
      - redis_data:/data

volumes:
  mysql_data:
  redis_data:
```

```bash
# 部署生产环境
docker-compose -f docker-compose.prod.yaml up -d
```

## 🧪 测试

### 运行单元测试

```bash
# 运行所有测试
mvn test

# 运行指定测试类
mvn test -Dtest=ItemControllerTest

# 跳过测试构建
mvn clean package -DskipTests
```

### 集成测试

```bash
# 运行集成测试 (需要数据库)
mvn verify -Pintegration-test
```

## 📚 API文档

### 核心API端点

#### 🛍️ 商品管理API

| HTTP方法 | 端点 | 描述 | 认证 |
|---------|------|------|------|
| `GET` | `/api/items` | 获取商品列表 | ❌ |
| `GET` | `/api/items/{id}` | 获取商品详情 | ❌ |
| `POST` | `/api/items` | 创建商品 | ✅ |
| `PUT` | `/api/items/{id}` | 更新商品 | ✅ |
| `DELETE` | `/api/items/{id}` | 删除商品 | ✅ |

#### 🏷️ 品牌管理API

| HTTP方法 | 端点 | 描述 | 认证 |
|---------|------|------|------|
| `GET` | `/api/brands` | 获取品牌列表 | ❌ |
| `POST` | `/api/brands` | 创建品牌 | ✅ |
| `PUT` | `/api/brands/{id}` | 更新品牌 | ✅ |

#### 📂 分类管理API

| HTTP方法 | 端点 | 描述 | 认证 |
|---------|------|------|------|
| `GET` | `/api/categories` | 获取分类树 | ❌ |
| `POST` | `/api/categories` | 创建分类 | ✅ |

#### 👤 用户管理API

| HTTP方法 | 端点 | 描述 | 认证 |
|---------|------|------|------|
| `POST` | `/api/members/register` | 用户注册 | ❌ |
| `POST` | `/api/members/login` | 用户登录 | ❌ |
| `GET` | `/api/members/profile` | 获取用户信息 | ✅ |
| `PUT` | `/api/members/profile` | 更新用户信息 | ✅ |

### API请求示例

#### 用户注册
```bash
curl -X POST http://localhost:8080/api/members/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "phone": "13800138000"
  }'
```

#### 用户登录
```bash
curl -X POST http://localhost:8080/api/members/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

#### 创建商品 (需要JWT Token)
```bash
curl -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "最新款iPhone",
    "price": 9999.00,
    "brandId": 1,
    "categoryId": 1
  }'
```

## 🔧 配置说明

### 环境配置文件

- `application.yaml` - 默认配置
- `application-local.yaml` - 本地开发环境
- `application-dev.yaml` - 开发环境  
- `application-test.yaml` - 测试环境
- `application-prod.yaml` - 生产环境

### 关键配置项

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:password}
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  # Redis配置
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
  
  # JPA配置
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect

# JWT配置
jwt:
  secret: ${JWT_SECRET:your-secret-key-here}
  expiration: 86400 # 24小时

# 文件上传配置
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

# Actuator监控配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when-authorized
```

## 🔨 开发指南

### 代码规范

1. **Java代码规范**
   - 遵循阿里巴巴Java开发手册
   - 使用Lombok简化代码
   - 统一使用UTF-8编码

2. **Git提交规范**
   ```
   feat: 新功能
   fix: 修复bug
   docs: 文档更新
   style: 代码格式调整
   refactor: 代码重构
   test: 测试相关
   chore: 构建工具或辅助工具的变动
   ```

3. **数据库规范**
   - 表名使用小写+下划线
   - 字段名使用小写+下划线
   - 必须有主键和创建时间、更新时间字段
   - 所有表必须有注释

### 项目结构说明

```
src/main/java/com/example/onlinestore/
├── 📁 bean/          # 配置Bean和组件
├── 📁 config/        # 配置类 (Redis, Security, etc.)
├── 📁 constants/     # 常量定义
├── 📁 controller/    # REST API控制器
├── 📁 dto/          # 数据传输对象
├── 📁 entity/       # JPA实体类
├── 📁 enums/        # 枚举类
├── 📁 exceptions/   # 自定义异常
├── 📁 handler/      # 异常处理器
├── 📁 mapper/       # MyBatis映射器
├── 📁 security/     # 安全相关 (JWT, etc.)
├── 📁 service/      # 业务逻辑层
└── 📁 utils/        # 工具类
```

### 添加新功能的步骤

1. **创建实体类** (`entity/`)
   ```java
   @Entity
   @Table(name = "new_table")
   @Data
   public class NewEntity {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
       // ... 其他字段
   }
   ```

2. **创建数据访问层** (`mapper/`)
   ```java
   @Mapper
   public interface NewMapper {
       List<NewEntity> findAll();
       // ... 其他方法
   }
   ```

3. **创建业务逻辑层** (`service/`)
   ```java
   @Service
   public class NewService {
       @Autowired
       private NewMapper newMapper;
       // ... 业务方法
   }
   ```

4. **创建控制器** (`controller/`)
   ```java
   @RestController
   @RequestMapping("/api/news")
   public class NewController {
       @Autowired
       private NewService newService;
       // ... API端点
   }
   ```

5. **编写测试**
   ```java
   @SpringBootTest
   class NewServiceTest {
       // ... 测试方法
   }
   ```

## 🚨 故障排除

### 常见问题

1. **启动失败 - 端口被占用**
   ```bash
   # 查找占用8080端口的进程
   netstat -tuln | grep 8080
   # 或者修改端口
   server.port=8081
   ```

2. **数据库连接失败**
   ```bash
   # 检查MySQL服务状态
   systemctl status mysql
   # 测试数据库连接
   mysql -h localhost -u root -p online_store
   ```

3. **Redis连接失败**
   ```bash
   # 检查Redis服务状态
   systemctl status redis
   # 测试Redis连接
   redis-cli ping
   ```

4. **JWT Token无效**
   - 检查JWT密钥配置
   - 确认Token没有过期
   - 验证请求头格式: `Authorization: Bearer <token>`

### 日志配置

```yaml
# logback-spring.xml
logging:
  level:
    com.example.onlinestore: DEBUG
    org.springframework.security: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/online-store.log
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 支持

如果您在使用过程中遇到问题，可以通过以下方式获取帮助：

- 📧 邮箱: support@example.com
- 💬 问题反馈: [GitHub Issues](https://github.com/your-username/online-store/issues)
- 📖 文档: [项目Wiki](https://github.com/your-username/online-store/wiki)

---

⭐ 如果这个项目对您有帮助，请给它一个星标！ 