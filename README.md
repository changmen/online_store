# 🛒 在线商店系统 (Online Store)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

这是一个基于现代微服务架构的在线商店系统，采用Spring Cloud生态圈技术栈，支持商品管理、用户认证、购物车、订单处理等完整的电商功能。

## ✨ 功能特性

### 🔐 用户认证与权限
- JWT Token认证
- Spring Security安全框架
- 用户注册与登录
- 权限管理

### 📦 商品管理
- 商品分类管理
- 品牌管理
- 商品属性配置
- 商品详情管理
- 库存管理

### 👥 会员系统
- 会员注册
- 会员信息管理
- 会员等级体系

### 🏗️ 系统特性
- 微服务架构设计
- Nacos服务注册与配置中心
- Redis缓存支持
- 国际化支持
- 阿里云OSS文件存储
- 分页查询支持

## 🛠️ 技术栈

### 核心框架
- **JDK 17** - Java开发环境
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Cloud Alibaba 2022.0.0.0** - 阿里巴巴微服务套件

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis (Jedis 5.2.0)** - 缓存数据库
- **MyBatis 3.0.3** - ORM框架
- **PageHelper 2.1.0** - 分页插件

### 服务治理
- **Nacos 2.2.0** - 服务注册发现、配置中心
- **Actuator** - 应用监控

### 工具库
- **Lombok** - 简化Java代码
- **JJWT 0.11.5** - JWT处理
- **Apache Commons** - 工具类
- **Aliyun OSS 3.18.1** - 阿里云对象存储

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java        # 启动类
│   │   │   ├── controller/                        # 控制器层
│   │   │   │   ├── AttributeController.java       # 商品属性API
│   │   │   │   ├── BrandController.java           # 品牌管理API
│   │   │   │   ├── CategoryController.java        # 分类管理API
│   │   │   │   ├── ItemController.java            # 商品API
│   │   │   │   ├── ItemDetailController.java      # 商品详情API
│   │   │   │   └── MemberController.java          # 会员API
│   │   │   ├── service/                           # 业务逻辑层
│   │   │   ├── mapper/                            # 数据访问层
│   │   │   ├── entity/                            # 实体类
│   │   │   ├── dto/                               # 数据传输对象
│   │   │   ├── config/                            # 配置类
│   │   │   ├── security/                          # 安全配置
│   │   │   ├── utils/                             # 工具类
│   │   │   ├── enums/                             # 枚举类
│   │   │   ├── exceptions/                        # 异常处理
│   │   │   └── constants/                         # 常量定义
│   │   └── resources/
│   │       ├── application.yaml                  # 主配置文件
│   │       ├── application-local.yaml            # 本地环境配置
│   │       ├── bootstrap.yaml                    # 引导配置
│   │       ├── mapper/                            # MyBatis映射文件
│   │       ├── sql/                               # SQL脚本
│   │       └── i18n/                              # 国际化资源
│   └── test/                                      # 测试代码
├── scripts/                                       # 脚本文件
├── docker-compose.yaml                            # Docker编排文件
├── Dockerfile                                     # Docker镜像构建文件
├── pom.xml                                        # Maven配置文件
└── README.md                                      # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- **JDK 17+** - Java开发环境
- **Maven 3.6+** - 项目构建工具
- **MySQL 8.0+** - 数据库
- **Redis 6.0+** - 缓存服务
- **Docker & Docker Compose** (可选) - 容器化部署

### 1. 克隆项目

```bash
git clone <repository-url>
cd online_store
```

### 2. 数据库配置

#### 创建数据库
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 导入初始数据
```bash
# 执行sql目录下的建表脚本
mysql -u root -p online_store < src/main/resources/sql/
```

### 3. 配置文件

修改 `src/main/resources/application-local.yaml` 中的配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password

jwt:
  secret: your_jwt_secret_key
```

### 4. 启动服务

#### 方式一：使用Docker Compose (推荐)

```bash
# 启动MySQL和Redis
docker-compose --profile all up -d

# 启动应用
mvn spring-boot:run
```

#### 方式二：本地启动

```bash
# 确保MySQL和Redis服务已启动
# 启动应用，添加JVM参数
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

### 5. 验证部署

```bash
# 检查应用健康状态
curl http://localhost:8080/actuator/health

# 访问API文档 (如果配置了Swagger)
open http://localhost:8080/swagger-ui.html
```

## 🔧 开发指南

### 开发环境配置

1. **IDE推荐**：IntelliJ IDEA
2. **必需插件**：
   - Lombok Plugin
   - MyBatis Plugin
   - Spring Boot Assistant

### 代码规范

- 遵循Java编码规范
- 使用Lombok减少样板代码
- 统一异常处理
- API接口使用RESTful风格

### 数据库设计

- 使用下划线命名（snake_case）
- 主键统一使用`id`
- 创建时间字段：`created_at`
- 更新时间字段：`updated_at`

### API设计规范

```
GET    /api/items          # 获取商品列表
GET    /api/items/{id}     # 获取商品详情
POST   /api/items          # 创建商品
PUT    /api/items/{id}     # 更新商品
DELETE /api/items/{id}     # 删除商品
```

## 🐳 Docker部署

### 构建应用镜像

```bash
# 构建JAR包
mvn clean package -DskipTests

# 构建Docker镜像
docker build -t online-store:latest .
```

### 使用Docker Compose部署

```bash
# 启动所有服务
docker-compose --profile all up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

## 📝 环境变量配置

| 变量名 | 描述 | 默认值 |
|--------|------|--------|
| `SPRING_PROFILES_ACTIVE` | 激活的配置文件 | `local` |
| `ADMIN_USERNAME` | 管理员用户名 | `admin` |
| `ADMIN_PASSWORD` | 管理员密码 | `admin123` |
| `JWT_SECRET` | JWT密钥 | *必填* |
| `NACOS_ENABLED` | 是否启用Nacos | `false` |

## 🚦 API接口

### 认证接口
- `POST /auth/login` - 用户登录
- `POST /auth/register` - 用户注册
- `POST /auth/logout` - 用户登出

### 商品接口
- `GET /api/items` - 获取商品列表
- `GET /api/items/{id}` - 获取商品详情
- `POST /api/items` - 创建商品（需要管理员权限）
- `PUT /api/items/{id}` - 更新商品（需要管理员权限）

### 分类接口
- `GET /api/categories` - 获取分类列表
- `POST /api/categories` - 创建分类

### 品牌接口
- `GET /api/brands` - 获取品牌列表
- `POST /api/brands` - 创建品牌

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn integration-test

# 生成测试报告
mvn surefire-report:report
```

## 📊 监控与日志

### 应用监控
- Actuator端点：`http://localhost:8080/actuator`
- 健康检查：`http://localhost:8080/actuator/health`
- 应用信息：`http://localhost:8080/actuator/info`

### 日志配置
- 默认使用Logback
- 日志级别可通过配置文件调整
- 支持多环境日志配置

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目基于 Apache 2.0 许可证开源。详情请参阅 [LICENSE](LICENSE) 文件。

## 🙋‍♂️ 联系我们

如有问题或建议，请提交 [Issue](../../issues) 或联系项目维护者。

---

⭐ 如果这个项目对你有帮助，请给它一个星标！ 