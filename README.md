# 在线商店系统 (Online Store)

🛍️ 一个基于Spring Cloud微服务架构的现代化在线商店系统，提供完整的电商功能，包括商品管理、品牌管理、分类管理、会员管理等核心模块。

## 🚀 项目特色

- **微服务架构**: 基于Spring Cloud构建的分布式系统
- **现代化技术栈**: Spring Boot 3.4+ + JDK 17 + MySQL 8.0
- **安全认证**: 集成Spring Security + JWT认证机制
- **缓存支持**: Redis缓存提升系统性能
- **服务发现**: 支持Nacos服务注册与发现
- **容器化部署**: 完整的Docker部署方案
- **国际化支持**: 多语言界面支持

## 🛠 技术栈

### 后端技术
- **Java**: JDK 17
- **框架**: Spring Boot 3.4.3 + Spring Cloud 2024.0.0
- **持久层**: MyBatis 3.0.3 + PageHelper 2.1.0
- **数据库**: MySQL 8.2.0
- **缓存**: Redis + Jedis 5.2.0
- **安全**: Spring Security + JWT (jjwt 0.11.5)
- **服务发现**: Nacos 2.2.0
- **对象存储**: 阿里云OSS 3.18.1
- **工具库**: Lombok, Apache Commons, CGLib

### 开发工具
- **构建工具**: Maven 3.6+
- **容器化**: Docker + Docker Compose
- **脚本支持**: Python 脚本工具

## 📋 核心功能

### 商品管理
- ✅ 商品信息管理 (CRUD)
- ✅ 商品分类管理
- ✅ 商品属性管理
- ✅ SKU管理
- ✅ 商品访问日志

### 品牌管理
- ✅ 品牌信息维护
- ✅ 品牌与商品关联

### 会员管理
- ✅ 会员注册/登录
- ✅ 会员信息管理
- ✅ JWT认证授权

### 系统功能
- ✅ 国际化支持 (i18n)
- ✅ 统一异常处理
- ✅ 请求日志记录
- ✅ 数据校验
- ✅ 缓存管理

## 🏗 项目架构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java      # 应用启动类
│   ├── bean/                           # Bean配置类
│   ├── config/                         # 配置类 (Security, Redis, etc.)
│   ├── constants/                      # 常量定义
│   ├── controller/                     # REST控制器
│   │   ├── AttributeController.java    # 属性管理
│   │   ├── BrandController.java        # 品牌管理
│   │   ├── CategoryController.java     # 分类管理
│   │   ├── ItemController.java         # 商品管理
│   │   ├── ItemDetailController.java   # 商品详情
│   │   └── MemberController.java       # 会员管理
│   ├── dto/                           # 数据传输对象
│   ├── entity/                        # 实体类
│   ├── enums/                         # 枚举类
│   ├── exceptions/                    # 自定义异常
│   ├── handler/                       # 异常处理器
│   ├── mapper/                        # MyBatis映射器
│   ├── security/                      # 安全配置
│   ├── service/                       # 业务逻辑层
│   └── utils/                         # 工具类
├── src/main/resources/
│   ├── application.yaml               # 主配置文件
│   ├── application-local.yaml         # 本地环境配置
│   ├── bootstrap.yaml                 # 启动配置
│   ├── i18n/                         # 国际化资源
│   ├── mapper/                       # MyBatis映射文件
│   └── sql/                          # 数据库表结构
├── scripts/                          # Python工具脚本
├── docker-compose.yaml               # Docker编排文件
├── Dockerfile                        # Docker镜像构建文件
└── pom.xml                          # Maven项目配置
```

## 🗄 数据库设计

系统包含以下核心数据表：

- **member**: 会员表 - 用户信息管理
- **brand**: 品牌表 - 品牌信息管理
- **category**: 分类表 - 商品分类管理
- **item**: 商品表 - 商品基本信息
- **sku**: SKU表 - 商品库存单位
- **attribute**: 属性表 - 商品属性定义
- **attribute_value**: 属性值表 - 属性值管理
- **item_attribute_relation**: 商品属性关联表
- **item_access_log**: 商品访问日志表

## 📦 环境要求

### 必需软件
- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本

### 可选软件
- **Docker**: 20.10 或更高版本
- **Docker Compose**: 1.29 或更高版本
- **Nacos**: 2.2.0 (如需服务发现)

## 🚀 快速开始

### 方式一：本地开发环境

#### 1. 环境准备

**克隆项目**
```bash
git clone <repository-url>
cd online_store
```

**启动必需服务**
```bash
# 启动MySQL和Redis (使用Docker Compose)
docker-compose up -d mysql redis
```

#### 2. 数据库初始化

**创建数据库**
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**执行SQL脚本**
```bash
# 进入MySQL容器
docker exec -it <mysql_container_id> mysql -uroot -p123456

# 或者直接执行SQL文件
mysql -h localhost -u root -p123456 online_store < src/main/resources/sql/attribute_table.sql
mysql -h localhost -u root -p123456 online_store < src/main/resources/sql/brand_table.sql
mysql -h localhost -u root -p123456 online_store < src/main/resources/sql/category_table.sql
mysql -h localhost -u root -p123456 online_store < src/main/resources/sql/item_table_table.sql
mysql -h localhost -u root -p123456 online_store < src/main/resources/sql/sku_table.sql
mysql -h localhost -u root -p123456 online_store < src/main/resources/sql/member_table.sql
mysql -h localhost -u root -p123456 online_store < src/main/resources/sql/attribute_value_table.sql
mysql -h localhost -u root -p123456 online_store < src/main/resources/sql/item_attribute_relation_table.sql
mysql -h localhost -u root -p123456 online_store < src/main/resources/sql/item_access_log_table.sql
```

#### 3. 配置文件设置

**配置环境变量**
```bash
# 设置JWT密钥
export JWT_SECRET="your-secret-key-here-at-least-256-bits-long"

# 可选：配置管理员账户
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="admin123"

# 可选：启用Nacos服务发现
export NACOS_ENABLED="false"
```

**修改配置文件**

根据需要修改 `src/main/resources/application-local.yaml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
  data:
    redis:
      host: localhost
      port: 6379
      password: # 如果有密码请填写
```

#### 4. 启动应用

```bash
# 编译项目
mvn clean compile

# 运行应用 (添加JVM参数以解决模块化问题)
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"

# 或者直接运行jar包
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

应用启动后，访问 http://localhost:8080

### 方式二：Docker 容器化部署

#### 1. 使用 Docker Compose 一键部署

**启动所有服务**
```bash
# 启动MySQL和Redis
docker-compose --profile all up -d

# 构建应用镜像
docker build -t online-store:latest .

# 运行应用容器
docker run -d \
  --name online-store-app \
  --network host \
  -e JWT_SECRET="your-secret-key-here-at-least-256-bits-long" \
  -e SPRING_PROFILES_ACTIVE="local" \
  online-store:latest \
  java --add-opens java.base/java.lang=ALL-UNNAMED -jar /app/online-store.jar
```

**完整的Docker Compose配置示例**

创建 `docker-compose.override.yaml`：
```yaml
version: "3"
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - JWT_SECRET=your-secret-key-here-at-least-256-bits-long
      - SPRING_PROFILES_ACTIVE=local
    depends_on:
      - mysql
      - redis
    command: [
      "java", 
      "--add-opens", "java.base/java.lang=ALL-UNNAMED",
      "-jar", "/app/online-store.jar"
    ]
```

然后运行：
```bash
docker-compose -f docker-compose.yaml -f docker-compose.override.yaml up -d
```

#### 2. 数据持久化配置

**创建数据目录**
```bash
# 创建MySQL数据目录
mkdir -p $HOME/work/database/docker-compose/data/mysql
mkdir -p $HOME/work/database/docker-compose/config/mysql

# 创建Redis数据目录
mkdir -p /tmp/docker-compose/data/redis
```

**MySQL配置文件**

创建 `$HOME/work/database/docker-compose/config/mysql/mysql.cnf`：
```ini
[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
default-time-zone='+08:00'
```

### 方式三：生产环境部署

#### 1. 环境变量配置

创建 `.env` 文件：
```bash
# 数据库配置
DB_HOST=your-mysql-host
DB_PORT=3306
DB_NAME=online_store
DB_USERNAME=your-username
DB_PASSWORD=your-password

# Redis配置
REDIS_HOST=your-redis-host
REDIS_PORT=6379
REDIS_PASSWORD=your-redis-password

# JWT配置
JWT_SECRET=your-very-secure-secret-key-256-bits

# 管理员账户
ADMIN_USERNAME=admin
ADMIN_PASSWORD=secure-admin-password

# Nacos配置 (可选)
NACOS_ENABLED=true
NACOS_SERVER_ADDR=your-nacos-server:8848
```

#### 2. 应用配置优化

创建生产环境配置 `application-prod.yaml`：
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}
      password: ${REDIS_PASSWORD}
      timeout: 3000ms
      jedis:
        pool:
          max-active: 20
          max-idle: 10
          min-idle: 2

logging:
  level:
    com.example.onlinestore: INFO
    org.springframework.security: WARN
  file:
    name: logs/online-store.log
```

#### 3. 启动生产服务

```bash
# 使用生产配置启动
java --add-opens java.base/java.lang=ALL-UNNAMED \
     -Dspring.profiles.active=prod \
     -Xms512m -Xmx2g \
     -jar online-store.jar
```

## 📚 API 文档

### 管理员登录

**默认账户**: admin / admin123 (可通过环境变量修改)

### 核心 API 接口

#### 会员管理

| 方法 | 路径 | 描述 | 请求参数 |
|------|------|------|----------|
| POST | `/api/v1/members/registry` | 会员注册 | MemberRegistryRequest |
| POST | `/api/v1/members/login` | 会员登录 | LoginRequest |

**注册请求示例**:
```json
{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com",
  "phone": "13800138000"
}
```

**登录请求示例**:
```json
{
  "username": "testuser",
  "password": "123456"
}
```

#### 品牌管理

| 方法 | 路径 | 描述 | 请求参数 |
|------|------|------|----------|
| GET | `/api/v1/brands` | 获取品牌列表 | pageNum, pageSize, visible |
| GET | `/api/v1/brands/{brandId}` | 获取品牌详情 | brandId |
| POST | `/api/v1/brands` | 创建品牌 | Brand |
| PUT | `/api/v1/brands/{brandId}` | 更新品牌 | brandId, Brand |
| DELETE | `/api/v1/brands/{brandId}` | 删除品牌 | brandId |

**品牌对象示例**:
```json
{
  "name": "苹果",
  "description": "苹果公司品牌",
  "logo": "https://example.com/apple-logo.png",
  "visible": 1
}
```

#### 商品管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/v1/items` | 获取商品列表 |
| GET | `/api/v1/items/{itemId}` | 获取商品详情 |
| POST | `/api/v1/items` | 创建商品 |
| PUT | `/api/v1/items/{itemId}` | 更新商品 |
| DELETE | `/api/v1/items/{itemId}` | 删除商品 |

#### 分类管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/v1/categories` | 获取分类列表 |
| POST | `/api/v1/categories` | 创建分类 |
| PUT | `/api/v1/categories/{categoryId}` | 更新分类 |
| DELETE | `/api/v1/categories/{categoryId}` | 删除分类 |

#### 属性管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/v1/attributes` | 获取属性列表 |
| POST | `/api/v1/attributes` | 创建属性 |
| PUT | `/api/v1/attributes/{attributeId}` | 更新属性 |
| DELETE | `/api/v1/attributes/{attributeId}` | 删除属性 |

### 认证与授权

系统使用JWT认证机制：

1. **获取Token**: 通过登录接口获取JWT Token
2. **使用Token**: 在请求头中添加 `Authorization: Bearer <token>`
3. **Token过期**: 默认24小时，过期后需要重新登录

### 统一返回格式

所有API都采用统一的返回格式：

**成功返回**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    // 具体数据
  },
  "timestamp": 1640995200000
}
```

**失败返回**:
```json
{
  "code": 400,
  "message": "错误信息",
  "data": null,
  "timestamp": 1640995200000
}
```

**分页返回**:
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "list": [
      // 数据列表
    ],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 10
  },
  "timestamp": 1640995200000
}
```

## 🔧 开发指南

### 项目结构详细说明

```
src/main/java/com/example/onlinestore/
├── bean/                          # 数据传输对象 (Bean类)
│   ├── Attribute.java             # 属性Bean
│   ├── Brand.java                 # 品牌Bean
│   ├── Category.java              # 分类Bean
│   └── Member.java                # 会员Bean
├── config/                        # 配置类
│   ├── RedisConfig.java           # Redis配置
│   ├── SecurityConfig.java        # Spring Security配置
│   ├── WebConfig.java             # Web配置
│   └── MyBatisConfig.java         # MyBatis配置
├── constants/                     # 常量定义
├── controller/                    # REST控制器层
├── dto/                          # 数据传输对象
│   ├── Request类                   # 请求DTO
│   ├── Response类                  # 返回DTO
│   └── QueryOptions类             # 查询参数DTO
├── entity/                       # 数据库实体类
├── enums/                        # 枚举类
├── exceptions/                   # 自定义异常
├── handler/                      # 全局异常处理器
├── mapper/                       # MyBatis数据访问层
├── security/                     # 安全相关类
│   ├── JwtAuthenticationFilter.java # JWT过滤器
│   └── JwtTokenUtil.java          # JWT工具类
├── service/                      # 业务逻辑层
│   ├── impl/                      # 服务实现类
│   └── 各种 Service 接口         # 服务接口
└── utils/                        # 工具类
    ├── DateUtil.java              # 日期工具
    ├── JsonUtil.java              # JSON工具
    └── StringUtil.java            # 字符串工具
```

### 开发规范

#### 代码规范
- 使用Lombok减少样板代码
- 使用`@Valid`进行参数校验
- 使用`@RestController`和`@RequestMapping`的RESTful风格
- 统一的异常处理机制

#### 数据库规范
- 使用`utf8mb4`字符集
- 表名使用下划线命名
- 必须有`created_at`和`updated_at`字段
- 使用透明主键设计

#### API设计规范
- RESTful API风格
- 统一的返回格式
- 使用HTTP状态码
- 遵循JSON:API规范

### 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 生成测试报告
mvn jacoco:report
```

### 构建和部署

```bash
# 清理和编译
mvn clean compile

# 打包
mvn clean package

# 跳过测试打包
mvn clean package -DskipTests

# 生成Docker镜像
mvn clean package docker:build
```

## 🔍 故障排查

### 常见问题

**1. 应用启动失败**
```bash
# 检查端口是否被占用
netstat -nlp | grep 8080

# 检查数据库连接
mysql -h localhost -u root -p123456 -e "SELECT 1"

# 检查Redis连接
redis-cli ping
```

**2. JWT配置问题**
```bash
# 确保JWT_SECRET环境变量设置
echo $JWT_SECRET

# JWT密钥至少256位
export JWT_SECRET="your-very-long-secret-key-that-is-at-least-256-bits-long"
```

**3. 数据库连接问题**
```bash
# 检查MySQL服务状态
docker ps | grep mysql

# 查看数据库日志
docker logs <mysql_container_id>

# 测试数据库连接
mysql -h localhost -P 3306 -u root -p123456 -e "SHOW DATABASES;"
```

**4. 内存不足**
```bash
# 调整JVM堆内存大小
java -Xms512m -Xmx2g --add-opens java.base/java.lang=ALL-UNNAMED -jar online-store.jar
```

### 日志查看

```bash
# 应用日志
tail -f logs/online-store.log

# Docker容器日志
docker logs -f online-store-app

# 系统资源使用情况
docker stats online-store-app
```

## 🎆 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交修改 (`git commit -m 'Add some amazing feature'`)
4. 推送分支 (`git push origin feature/amazing-feature`)
5. 提交Pull Request

## 📝 更新日志

### v1.0.0 (2024-12-25)
- ✅ 初始项目架构
- ✅ 会员管理功能
- ✅ 品牌管理功能
- ✅ 商品管理功能
- ✅ JWT认证机制
- ✅ Docker部署支持

## 📜 许可证

本项目采用 MIT 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

---

<div align="center">
  <b>📧 联系我们</b><br>
  如有问题或建议，请通过 <a href="mailto:support@example.com">support@example.com</a> 联系我们
</div> 