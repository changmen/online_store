# 🛒 Online Store - 在线商店系统

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

一个基于Spring Cloud微服务架构的现代化在线商店系统，提供完整的电商解决方案。该项目采用前后端分离架构，支持商品管理、用户管理、订单处理、库存管理等核心电商功能。

## ✨ 功能特性

### 🏪 商品管理
- 商品分类管理（多级分类）
- 商品信息管理（名称、描述、价格、库存等）
- 商品详情管理（详细描述、规格参数）
- 商品属性管理（颜色、尺寸等）
- 品牌管理

### 👥 用户管理
- 用户注册与登录
- 用户信息管理
- 权限控制（基于Spring Security + JWT）
- 会员等级管理

### 🛡️ 安全特性
- JWT身份认证
- Spring Security安全框架
- 接口权限控制
- 数据加密存储

### 🚀 技术特性
- 微服务架构（Spring Cloud）
- 服务注册与发现（Nacos）
- 分布式缓存（Redis）
- 数据持久化（MySQL + MyBatis）
- 文件存储（阿里云OSS）
- 分页查询支持

## 🛠️ 技术栈

### 后端框架
- **Java 17** - 开发语言
- **Spring Boot 3.4.3** - 基础框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Data Redis** - Redis集成

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis 6.0+** - 缓存数据库
- **MyBatis 3.0.3** - ORM框架
- **PageHelper 2.1.0** - 分页插件

### 服务治理
- **Nacos 2.2.0** - 服务注册发现与配置中心
- **Spring Cloud Alibaba** - 微服务生态

### 工具库
- **Lombok** - 代码简化
- **JWT** - 身份认证
- **Jackson** - JSON处理
- **Apache Commons** - 工具类库
- **阿里云OSS** - 对象存储

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 启动类
│   │   │   ├── controller/                    # 控制器层
│   │   │   │   ├── AttributeController.java  # 商品属性控制器
│   │   │   │   ├── BrandController.java       # 品牌控制器
│   │   │   │   ├── CategoryController.java    # 分类控制器
│   │   │   │   ├── ItemController.java        # 商品控制器
│   │   │   │   ├── ItemDetailController.java  # 商品详情控制器
│   │   │   │   └── MemberController.java      # 会员控制器
│   │   │   ├── service/                       # 服务层
│   │   │   ├── mapper/                        # 数据访问层
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── config/                        # 配置类
│   │   │   ├── security/                      # 安全相关
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── exceptions/                    # 异常处理
│   │   │   ├── utils/                         # 工具类
│   │   │   └── constants/                     # 常量定义
│   │   └── resources/
│   │       ├── application.yaml               # 主配置文件
│   │       └── mapper/                        # MyBatis映射文件
│   └── test/                                  # 测试代码
├── scripts/                                   # 脚本文件
│   ├── main.py                               # 测试数据生成脚本
│   └── README.md                             # 脚本说明
├── docker-compose.yaml                        # Docker编排文件
├── Dockerfile                                 # Docker镜像构建文件
├── pom.xml                                    # Maven配置文件
└── README.md                                  # 项目说明文档
```

## 📋 环境要求

### 开发环境
- **JDK 17+** - Java开发环境
- **Maven 3.6+** - 项目构建工具
- **MySQL 8.0+** - 数据库
- **Redis 6.0+** - 缓存服务
- **Git** - 版本控制工具

### 可选环境
- **Docker & Docker Compose** - 容器化部署
- **Nacos Server** - 服务注册中心（可选，默认关闭）
- **阿里云OSS** - 文件存储服务

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
docker-compose --profile all up -d

# 或者手动启动MySQL和Redis服务
```

3. **创建数据库**
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

4. **配置环境变量**
```bash
# 设置JWT密钥（必需）
export JWT_SECRET=your-secret-key-here

# 可选：设置管理员账号
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123

# 可选：启用Nacos
export NACOS_ENABLED=true
```

5. **修改配置文件**
编辑 `src/main/resources/application.yaml`，根据实际环境修改数据库和Redis连接信息。

6. **启动应用**
```bash
# 添加JVM参数并启动
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

### 方式二：Docker部署

1. **使用Docker Compose一键部署**
```bash
# 构建并启动所有服务
docker-compose --profile all up -d

# 只启动MySQL（不包含Redis）
docker-compose --profile without-redis up -d
```

2. **验证服务状态**
```bash
# 检查服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

## 🔧 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
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

### Nacos配置
```yaml
spring:
  cloud:
    nacos:
      discovery:
        enabled: ${NACOS_ENABLED:false}
```

### JWT配置
```yaml
jwt:
  secret: ${JWT_SECRET}  # 必须设置环境变量
  expiration: 86400      # 24小时过期
```

## 📋 接口文档

应用启动后，可以通过以下地址访问：

- **应用地址**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **应用信息**: http://localhost:8080/actuator/info

### 主要API端点

#### 用户管理
- `POST /api/members/register` - 用户注册
- `POST /api/members/login` - 用户登录
- `GET /api/members/profile` - 获取用户信息
- `PUT /api/members/profile` - 更新用户信息

#### 商品管理
- `GET /api/items` - 获取商品列表
- `GET /api/items/{id}` - 获取商品详情
- `POST /api/items` - 创建商品（需要管理员权限）
- `PUT /api/items/{id}` - 更新商品（需要管理员权限）
- `DELETE /api/items/{id}` - 删除商品（需要管理员权限）

#### 分类管理
- `GET /api/categories` - 获取商品分类列表
- `GET /api/categories/{id}` - 获取分类详情
- `POST /api/categories` - 创建分类（需要管理员权限）

#### 品牌管理
- `GET /api/brands` - 获取品牌列表
- `GET /api/brands/{id}` - 获取品牌详情
- `POST /api/brands` - 创建品牌（需要管理员权限）

### 认证方式

所有需要认证的接口都需要在请求头中添加JWT令牌：

```bash
Authorization: Bearer <your-jwt-token>
```

### 请求示例

```bash
# 用户登录
curl -X POST http://localhost:8080/api/members/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "your-username",
    "password": "your-password"
  }'

# 获取商品列表（支持分页）
curl -X GET "http://localhost:8080/api/items?page=1&size=10" \
  -H "Authorization: Bearer <your-jwt-token>"

# 创建新商品
curl -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin-jwt-token>" \
  -d '{
    "name": "商品名称",
    "price": 99.99,
    "description": "商品描述",
    "categoryId": 1,
    "brandId": 1
  }'
```

## 🧪 测试

### 运行单元测试
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 生成测试报告
mvn surefire-report:report
```

### 生成测试数据

项目提供了测试数据生成脚本，位于 `scripts/` 目录：

```bash
cd scripts
python main.py  # 生成测试数据
```

### 集成测试

```bash
# 启动集成测试环境
docker-compose --profile all up -d

# 等待服务启动后运行集成测试
mvn verify
```

## 🔧 开发指南

### 代码规范

- 使用Java 17的最新特性
- 遵循Spring Boot最佳实践
- 使用Lombok减少样板代码
- 使用MyBatis进行数据访问
- 统一异常处理和日志记录

### 项目层次结构

```
com.example.onlinestore/
├── controller/        # 控制器层 - 处理HTTP请求
├── service/          # 服务层 - 业务逻辑
├── mapper/           # 数据访问层 - MyBatis映射器
├── entity/           # 实体类 - 数据库表映射
├── dto/              # 数据传输对象
├── config/           # 配置类
├── security/         # 安全配置
├── exceptions/       # 异常定义
├── enums/            # 枚举类
├── utils/            # 工具类
└── constants/        # 常量定义
```

### 数据库设计

项目采用规范的数据库设计，主要表结构包括：

- `members` - 用户表
- `items` - 商品表
- `categories` - 分类表
- `brands` - 品牌表
- `item_details` - 商品详情表
- `attributes` - 商品属性表

### 添加新功能

1. **创建实体类** - 在 `entity/` 包下定义数据模型
2. **创建Mapper** - 在 `mapper/` 包下定义数据访问接口
3. **创建Service** - 在 `service/` 包下实现业务逻辑
4. **创建Controller** - 在 `controller/` 包下提供REST API
5. **创建DTO** - 在 `dto/` 包下定义数据传输对象
6. **编写测试** - 为新功能编写单元测试和集成测试

### 环境配置

#### 开发环境配置

```yaml
# application-dev.yaml
spring:
  profiles:
    active: dev
  datasource:
    url: jdbc:mysql://localhost:3306/online_store_dev
  data:
    redis:
      host: localhost
      port: 6379
      database: 1
logging:
  level:
    com.example.onlinestore: DEBUG
```

#### 生产环境配置

```yaml
# application-prod.yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
logging:
  level:
    root: WARN
    com.example.onlinestore: INFO
```

## 🐳 Docker部署

### 构建Docker镜像

```bash
# 构建应用镜像
docker build -t online-store:latest .

# 推送到镜像仓库
docker tag online-store:latest your-registry/online-store:latest
docker push your-registry/online-store:latest
```

### 生产环境部署

```bash
# 创建生产环境配置文件
cp docker-compose.yaml docker-compose.prod.yaml

# 修改生产环境配置
# 更新数据库密码、环境变量等

# 启动生产环境
docker-compose -f docker-compose.prod.yaml up -d
```

### 健康检查

```bash
# 检查应用健康状态
curl http://localhost:8080/actuator/health

# 查看应用信息
curl http://localhost:8080/actuator/info

# 查看应用指标
curl http://localhost:8080/actuator/metrics
```

## 📚 故障排除

### 常见问题

#### 1. 应用启动失败
```bash
# 检查Java版本
java --version

# 检查端口占用
netstat -tulpn | grep 8080

# 查看详细启动日志
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dlogging.level.root=DEBUG"
```

#### 2. 数据库连接问题
```bash
# 测试数据库连接
mysql -h localhost -u root -p

# 检查数据库权限
SHOW GRANTS FOR 'root'@'localhost';

# 检查数据库编码
SHOW VARIABLES LIKE 'character_set%';
```

#### 3. Redis连接问题
```bash
# 测试Redis连接
redis-cli ping

# 检查Redis配置
redis-cli CONFIG GET '*'
```

#### 4. JWT配置问题
确保设置了正确的JWT密钥：
```bash
export JWT_SECRET=your-very-long-secret-key-at-least-256-bits
```

### 日志配置

在 `application.yaml` 中配置日志级别：

```yaml
logging:
  level:
    com.example.onlinestore: DEBUG
    org.springframework.security: DEBUG
    org.mybatis: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
  file:
    name: logs/online-store.log
```

## 🤝 贡献指南

我们欢迎所有形式的贡献！

### 贡献流程

1. **Fork项目** - 在GitHub上fork本项目
2. **创建分支** - 创建你的功能分支 (`git checkout -b feature/AmazingFeature`)
3. **提交更改** - 提交你的更改 (`git commit -m 'Add some AmazingFeature'`)
4. **推送分支** - 推送到分支 (`git push origin feature/AmazingFeature`)
5. **创建PR** - 创建Pull Request

### 代码贡献规范

- 遵循现有的代码风格
- 为新功能编写测试
- 更新相关文档
- 确保所有测试通过
- 使用有意义的提交信息

### 报告问题

在提交issue时，请包含以下信息：

- 操作系统和版本
- Java版本
- 错误的详细描述
- 重现步骤
- 期望的行为
- 实际的行为

## 📄 许可证

本项目采用MIT许可证 - 查看 [LICENSE](LICENSE) 文件了解详细信息。

## 📞 联系方式

- 项目维护者：[Your Name]
- 邮箱：[your.email@example.com]
- 项目地址：[https://github.com/your-username/online-store]

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者们！

---

**Happy Coding! 🚀** 