# Online Store - 在线商店系统

一个基于 Spring Cloud 微服务架构的企业级在线商店系统，展示现代 Java 微服务开发的最佳实践。

## 📋 目录

- [核心功能](#核心功能)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
  - [本地开发环境部署](#本地开发环境部署)
  - [Docker 容器化部署](#docker-容器化部署)
- [配置说明](#配置说明)
- [API 文档](#api-文档)
- [常见问题](#常见问题)
- [开发指南](#开发指南)

## 核心功能

| 功能模块 | 功能描述 |
|---------|----------|
| 🛍️ 商品管理 | 商品的增删改查、SKU 管理、商品属性管理、商品分类 |
| 🏷️ 品牌管理 | 品牌信息的维护与查询 |
| 👥 会员系统 | 会员注册、登录认证（JWT）、会员信息管理 |
| 📁 分类管理 | 商品分类的层级管理 |
| 🏗️ 属性管理 | 商品属性与属性值的定义与关联 |
| 🔐 安全控制 | 基于 Spring Security 和 JWT 的认证授权机制 |

## 技术栈

| 技术类别 | 技术/框架 | 版本 |
|---------|----------|------|
| 核心框架 | Spring Boot | 3.4.3 |
| 微服务框架 | Spring Cloud | 2024.0.0 |
| 安全框架 | Spring Security | 随 Spring Boot 版本 |
| 持久层框架 | MyBatis | 3.0.3 |
| 分页插件 | PageHelper | 2.1.0 |
| 数据库 | MySQL | 8.2.0 |
| 缓存 | Redis (Jedis) | 5.2.0 |
| 服务发现与配置 | Nacos | 2.2.0 |
| 对象存储 | Alibaba OSS | 3.18.1 |
| 开发工具 | Lombok | 1.18.36 |
| 认证工具 | JWT (jjwt) | 0.11.5 |
| 构建工具 | Maven | 3.6+ |
| Java 版本 | JDK | 17 |

## 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用启动类
│   │   │   ├── controller/                    # 控制层
│   │   │   │   ├── ItemController.java        # 商品控制器
│   │   │   │   ├── BrandController.java       # 品牌控制器
│   │   │   │   ├── CategoryController.java    # 分类控制器
│   │   │   │   ├── AttributeController.java   # 属性控制器
│   │   │   │   ├── MemberController.java      # 会员控制器
│   │   │   │   └── ItemDetailController.java  # 商品详情控制器
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   ├── mapper/                        # MyBatis 映射器
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── config/                        # 配置类
│   │   │   ├── security/                      # 安全相关
│   │   │   ├── utils/                         # 工具类
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── exceptions/                    # 异常定义
│   │   │   └── handler/                       # 异常处理器
│   │   └── resources/
│   │       ├── application.yaml               # 主配置文件
│   │       ├── application-local.yaml         # 本地环境配置
│   │       ├── bootstrap.yaml                 # Bootstrap 配置
│   │       ├── mapper/                        # MyBatis XML 映射文件
│   │       ├── sql/                           # 数据库初始化脚本
│   │       │   ├── item_table_table.sql       # 商品表
│   │       │   ├── sku_table.sql              # SKU 表
│   │       │   ├── brand_table.sql            # 品牌表
│   │       │   ├── category_table.sql         # 分类表
│   │       │   ├── attribute_table.sql        # 属性表
│   │       │   ├── attribute_value_table.sql  # 属性值表
│   │       │   ├── item_attribute_relation_table.sql # 商品属性关联表
│   │       │   ├── member_table.sql           # 会员表
│   │       │   └── item_access_log_table.sql  # 商品访问日志表
│   │       └── i18n/                          # 国际化资源文件
│   └── test/                                  # 测试代码
├── scripts/                                   # 脚本文件
├── docker-compose.yaml                        # Docker Compose 配置
├── Dockerfile                                 # Docker 镜像构建文件
├── pom.xml                                    # Maven 配置文件
└── README.md                                  # 项目说明文档
```

## 环境要求

| 组件 | 版本要求 | 必需性 | 说明 |
|------|---------|--------|------|
| JDK | 17+ | ✅ 必需 | 需配置 JAVA_HOME 环境变量 |
| Maven | 3.6+ | ✅ 必需 | 用于项目构建 |
| MySQL | 8.0+ | ✅ 必需 | 数据持久化存储 |
| Redis | 6.0+ | ✅ 必需 | 缓存与会话管理 |
| Nacos | 2.x | ⭕ 可选 | 服务发现与配置中心，本地开发可不启用 |
| Docker | 最新版 | ⭕ 可选 | 用于容器化部署 |

## 快速开始

### 本地开发环境部署

#### 1. 克隆项目

```bash
git clone <repository-url>
cd online-store
```

#### 2. 启动基础服务

**启动 MySQL**

```bash
# 启动 MySQL 服务
sudo systemctl start mysql  # Linux
# 或使用 Docker
docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:8.2
```

**创建数据库**

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**启动 Redis**

```bash
# 启动 Redis 服务
sudo systemctl start redis  # Linux
# 或使用 Docker
docker run -d --name redis -p 6379:6379 redis:latest
```

#### 3. 配置应用

编辑 `src/main/resources/application-local.yaml` 文件，配置以下参数：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_mysql_password  # 修改为你的 MySQL 密码
  data:
    redis:
      host: localhost
      port: 6379
      password:  # 如果 Redis 设置了密码，在这里填写

jwt:
  secret: your_jwt_secret_key_here  # 设置 JWT 密钥（建议 32 位以上随机字符串）
```

**重要配置项说明：**
- 如需使用 OSS 文件上传功能，需要配置 Alibaba OSS 相关参数
- `jwt.secret` 必须设置，建议使用强随机字符串
- 生产环境请修改默认的用户名和密码

#### 4. 初始化数据库

执行 `src/main/resources/sql/` 目录下的所有 SQL 脚本：

```bash
# 进入 MySQL 命令行
mysql -u root -p online_store

# 依次执行以下脚本
source src/main/resources/sql/brand_table.sql
source src/main/resources/sql/category_table.sql
source src/main/resources/sql/attribute_table.sql
source src/main/resources/sql/attribute_value_table.sql
source src/main/resources/sql/item_table_table.sql
source src/main/resources/sql/sku_table.sql
source src/main/resources/sql/item_attribute_relation_table.sql
source src/main/resources/sql/member_table.sql
source src/main/resources/sql/item_access_log_table.sql
```

#### 5. 添加 JVM 参数

由于项目使用了 CGLIB 等技术，需要添加 JVM 参数：

```bash
--add-opens java.base/java.lang=ALL-UNNAMED
```

**IDE 配置方式：**

- **IntelliJ IDEA**: Run → Edit Configurations → VM options → 添加上述参数
- **Eclipse**: Run → Run Configurations → Arguments → VM arguments → 添加上述参数

#### 6. 启动应用

**使用 Maven 命令：**

```bash
# 设置环境变量并启动
export JWT_SECRET="your_jwt_secret_key_here"
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

**或通过 IDE 运行：**

运行主类：`com.example.onlinestore.OnlineStoreApplication`

#### 7. 验证启动

应用默认运行在 **8080** 端口，访问以下地址验证：

```bash
# 健康检查
curl http://localhost:8080/actuator/health

# 预期返回
{"status":"UP"}
```

### Docker 容器化部署

#### 1. 前置准备

确保已安装 Docker 和 Docker Compose：

```bash
docker --version
docker-compose --version
```

#### 2. 配置环境变量

编辑 `docker-compose.yaml` 文件，根据需要调整环境变量：

```yaml
environment:
  MYSQL_ROOT_PASSWORD: "123456"  # MySQL 密码
  TZ: 'Asia/Shanghai'             # 时区设置
```

#### 3. 启动基础服务

使用 Docker Compose 启动 MySQL 和 Redis：

```bash
# 启动所有服务（MySQL + Redis）
docker-compose --profile all up -d

# 或只启动 MySQL（如果已有 Redis）
docker-compose --profile without-redis up -d
```

**各容器说明：**
- `mysql`: MySQL 8.x 数据库，端口 3306
- `redis`: Redis 缓存服务，端口 6379

#### 4. 初始化数据库

等待 MySQL 容器启动完成后，执行初始化脚本：

```bash
# 复制 SQL 脚本到容器
docker cp src/main/resources/sql mysql:/tmp/sql

# 进入容器执行脚本
docker exec -it mysql bash
mysql -u root -p123456 -e "CREATE DATABASE IF NOT EXISTS online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
cd /tmp/sql
for file in *.sql; do mysql -u root -p123456 online_store < "$file"; done
exit
```

#### 5. 构建并启动应用

```bash
# 构建应用镜像
docker build -t online-store:latest .

# 启动应用容器
docker run -d --name online-store \
  -p 8080:8080 \
  -e JWT_SECRET="your_jwt_secret_key_here" \
  -e SPRING_PROFILES_ACTIVE=local \
  --link mysql:mysql \
  --link redis:redis \
  online-store:latest
```

#### 6. 查看服务状态

```bash
# 查看所有容器状态
docker-compose ps

# 查看应用日志
docker logs -f online-store

# 查看 MySQL 日志
docker-compose logs -f mysql

# 查看 Redis 日志
docker-compose logs -f redis
```

## 配置说明

### 核心配置参数

| 配置项 | 配置文件 | 说明 | 示例值 |
|-------|---------|------|--------|
| `server.port` | application.yaml | 应用服务端口 | 8080 |
| `spring.datasource.url` | application-local.yaml | 数据库连接地址 | jdbc:mysql://localhost:3306/online_store |
| `spring.datasource.username` | application-local.yaml | 数据库用户名 | root |
| `spring.datasource.password` | application-local.yaml | 数据库密码 | 123456 |
| `spring.data.redis.host` | application-local.yaml | Redis 主机地址 | localhost |
| `spring.data.redis.port` | application-local.yaml | Redis 端口 | 6379 |
| `jwt.secret` | application.yaml | JWT 密钥 | 环境变量配置 |
| `jwt.expiration` | application.yaml | JWT 过期时间（秒） | 86400 |
| `spring.cloud.nacos.config.server-addr` | bootstrap.yaml | Nacos 服务地址 | localhost:8848 |
| `spring.cloud.nacos.discovery.enabled` | application.yaml | 是否启用服务发现 | false（本地开发） |

### 配置文件说明

- **application.yaml**: 主配置文件，包含通用配置
- **application-local.yaml**: 本地开发环境专用配置
- **bootstrap.yaml**: Bootstrap 阶段配置，用于 Nacos 等配置中心

### 配置最佳实践

1. **敏感信息管理**：密码、密钥等敏感信息建议使用环境变量或配置中心管理
2. **环境隔离**：不同环境使用不同的 profile（local、dev、prod）
3. **生产环境**：
   - 修改默认的数据库密码
   - 使用强随机字符串作为 JWT 密钥
   - 调整数据库连接池参数以适应负载
   - 启用 HTTPS

## API 文档

### API 模块概览

| API 模块 | 基础路径 | 主要功能 | 认证要求 |
|---------|---------|---------|----------|
| 商品管理 | `/api/items` | 商品增删改查、详情查询 | ✅ 需要 |
| SKU 管理 | `/api/skus` | SKU 相关操作 | ✅ 需要 |
| 品牌管理 | `/api/brands` | 品牌信息管理 | ✅ 需要 |
| 分类管理 | `/api/categories` | 分类树查询与管理 | ✅ 需要 |
| 属性管理 | `/api/attributes` | 属性定义与关联 | ✅ 需要 |
| 会员管理 | `/api/members` | 注册、登录、信息查询 | ⭕ 部分需要 |

### 认证说明

系统使用 **JWT (JSON Web Token)** 进行身份认证：

1. **登录获取 Token**：通过会员登录接口获取 JWT Token
2. **携带 Token 访问**：在请求头中添加 Authorization 字段

```bash
# 请求示例
curl -X GET http://localhost:8080/api/items \
  -H "Authorization: Bearer {your_jwt_token}"
```

### 基础认证（开发测试）

系统也支持 Spring Security 基础认证（仅用于开发测试）：

- 用户名：`admin`（可通过环境变量 `ADMIN_USERNAME` 配置）
- 密码：`admin123`（可通过环境变量 `ADMIN_PASSWORD` 配置）

## 常见问题

### Q1: 启动时提示 "Illegal reflective access"

**原因**：JDK 17 的模块化限制

**解决方案**：添加 JVM 参数
```bash
--add-opens java.base/java.lang=ALL-UNNAMED
```

### Q2: 数据库连接失败

**排查步骤**：
1. 确认 MySQL 服务已启动：`systemctl status mysql` 或 `docker ps`
2. 确认数据库已创建：`SHOW DATABASES LIKE 'online_store';`
3. 检查配置文件中的连接信息（URL、用户名、密码）
4. 测试连接：`mysql -h localhost -u root -p online_store`

### Q3: Redis 连接超时

**排查步骤**：
1. 确认 Redis 服务已启动：`systemctl status redis` 或 `docker ps`
2. 检查 host 和 port 配置是否正确
3. 测试连接：`redis-cli -h localhost -p 6379 ping`
4. 如果设置了密码，确保配置文件中填写了正确的密码

### Q4: 404 错误

**排查步骤**：
1. 检查应用是否成功启动
2. 确认请求路径是否正确（注意大小写）
3. 检查是否需要认证（添加 Authorization 头）
4. 查看日志确认 Controller 是否正确映射

### Q5: JWT Token 验证失败

**可能原因**：
1. Token 已过期（默认 24 小时）
2. JWT 密钥配置不一致
3. Token 格式不正确（需要 "Bearer " 前缀）

**解决方案**：
1. 重新登录获取新 Token
2. 确认环境变量 `JWT_SECRET` 设置正确
3. 检查 Authorization 头格式：`Bearer {token}`

### Q6: Nacos 连接失败

**解决方案**：

本地开发环境默认已禁用 Nacos，如需启用：

```bash
# 方式1：修改配置文件
# 在 application.yaml 和bootstrap.yaml 中设置
NACOS_ENABLED: true

# 方式2：使用环境变量
export NACOS_ENABLED=true
export NACOS_SERVER_ADDR=localhost:8848

# 确保 Nacos 服务已启动
docker run -d --name nacos -p 8848:8848 -e MODE=standalone nacos/nacos-server:v2.2.0
```

### Q7: Maven 构建失败

**常见原因**：
1. 网络问题导致依赖下载失败
2. Maven 版本过低
3. JDK 版本不匹配

**解决方案**：
```bash
# 清理并重新构建
mvn clean install -U

# 使用阿里云镜像加速（编辑 ~/.m2/settings.xml）
```

## 开发指南

### 代码结构约定

- **Controller 层**：处理 HTTP 请求，参数验证，调用 Service
- **Service 层**：业务逻辑处理，事务管理
- **Mapper 层**：数据访问层，对应 MyBatis 接口
- **Entity 层**：数据库实体类，与表结构对应
- **DTO 层**：数据传输对象，用于前后端交互

### 数据库管理规范

1. **SQL 脚本管理**：所有建表语句放在 `src/main/resources/sql/` 目录
2. **命名规范**：表名使用下划线分隔，如 `item_table`
3. **字段规范**：使用下划线命名，MyBatis 自动转驼峰

### 国际化资源

项目支持国际化，资源文件位于 `src/main/resources/i18n/` 目录：

- 默认语言配置
- 多语言消息管理
- 错误提示信息国际化

### 提交代码前检查清单

- [ ] 代码符合项目编码规范
- [ ] 已添加必要的注释和文档
- [ ] 通过本地测试验证
- [ ] 检查是否有编译警告
- [ ] 更新相关 API 文档
- [ ] 数据库变更已创建对应 SQL 脚本

### 分支管理建议

- `main`: 生产环境代码
- `develop`: 开发环境代码
- `feature/*`: 功能开发分支
- `bugfix/*`: 问题修复分支
- `hotfix/*`: 紧急修复分支

---

## 📝 许可证

本项目仅用于学习和演示目的。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📧 联系方式

如有问题或建议，请通过 Issue 联系我们。
