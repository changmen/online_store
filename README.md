# 在线商店系统 (Online Store)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.2-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)

基于 Spring Boot 和 Spring Cloud 构建的现代化在线商店系统，提供完整的电商功能，包括用户管理、商品管理、订单处理、支付集成等核心功能。

## ✨ 核心功能

- 🔐 **用户认证与授权** - 基于 JWT 的安全认证系统
- 🛍️ **商品管理** - 商品分类、库存管理、价格策略
- 🛒 **购物车系统** - 灵活的购物车管理
- 📦 **订单处理** - 完整的订单生命周期管理
- 💳 **支付集成** - 多种支付方式支持
- 👤 **用户管理** - 用户注册、登录、个人信息管理
- 📊 **数据统计** - 销售数据分析和报表

## 🛠️ 技术栈

### 后端技术
- **Java 17** - 编程语言
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Data Redis** - Redis 数据访问
- **MyBatis 3.0.3** - ORM 框架
- **MySQL 8.2** - 关系型数据库
- **Redis 5.2** - 缓存和会话存储
- **JWT** - 身份认证
- **Maven** - 项目构建工具

### 基础设施
- **Docker & Docker Compose** - 容器化部署
- **Nacos** - 服务发现与配置管理
- **Actuator** - 应用监控

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 主启动类
│   │   │   ├── bean/                          # Bean 配置
│   │   │   ├── config/                        # 配置类
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # REST 控制器
│   │   │   ├── dto/                          # 数据传输对象
│   │   │   ├── entity/                       # 实体类
│   │   │   ├── enums/                        # 枚举类
│   │   │   ├── exceptions/                   # 自定义异常
│   │   │   ├── handler/                      # 异常处理器
│   │   │   ├── mapper/                       # MyBatis 映射器
│   │   │   ├── security/                     # 安全配置
│   │   │   ├── service/                      # 业务逻辑层
│   │   │   └── utils/                        # 工具类
│   │   └── resources/
│   │       ├── application.yaml              # 应用配置
│   │       └── mapper/                       # MyBatis XML 映射文件
│   └── test/                                 # 测试代码
├── scripts/                                  # 脚本文件
├── docker-compose.yaml                       # Docker 编排文件
├── Dockerfile                                # Docker 镜像构建文件
├── pom.xml                                   # Maven 配置文件
└── README.md                                 # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- **JDK 17+** - Java 开发环境
- **Maven 3.6+** - 项目构建工具
- **MySQL 8.0+** - 数据库
- **Redis 6.0+** - 缓存服务
- **Docker & Docker Compose** (可选) - 容器化部署

### 📋 安装步骤

#### 方式一：使用 Docker Compose (推荐)

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd online_store
   ```

2. **启动基础服务**
   ```bash
   # 启动 MySQL 和 Redis
   docker-compose --profile all up -d
   
   # 或只启动 MySQL (如果你有独立的 Redis)
   docker-compose --profile without-redis up -d
   ```

3. **初始化数据库**
   ```bash
   # 创建数据库
   mysql -u root -p
   ```
   ```sql
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   USE online_store;
   
   # 执行数据库脚本（按顺序执行）
   SOURCE src/main/resources/sql/category_table.sql;
   SOURCE src/main/resources/sql/brand_table.sql;
   SOURCE src/main/resources/sql/attribute_table.sql;
   SOURCE src/main/resources/sql/attribute_value_table.sql;
   SOURCE src/main/resources/sql/item_table_table.sql;
   SOURCE src/main/resources/sql/sku_table.sql;
   SOURCE src/main/resources/sql/item_attribute_relation_table.sql;
   SOURCE src/main/resources/sql/member_table.sql;
   SOURCE src/main/resources/sql/item_access_log_table.sql;
   ```

4. **配置环境变量**
   ```bash
   export JWT_SECRET="your-jwt-secret-key-here"
   export SPRING_PROFILES_ACTIVE=local
   ```

5. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

#### 方式二：本地安装

1. **安装并启动 MySQL**
   ```bash
   # Ubuntu/Debian
   sudo apt update
   sudo apt install mysql-server
   sudo systemctl start mysql
   
   # 创建数据库
   mysql -u root -p
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **安装并启动 Redis**
   ```bash
   # Ubuntu/Debian
   sudo apt install redis-server
   sudo systemctl start redis
   ```

3. **配置应用**
   - 修改 `src/main/resources/application.yaml` 中的数据库和 Redis 连接信息
   - 设置环境变量：`export JWT_SECRET="your-secret-key"`

4. **运行应用**
   ```bash
   # 添加 JVM 参数以支持反射
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
   ```

### 🔧 配置说明

#### 环境变量

| 变量名 | 描述 | 默认值 | 必需 |
|--------|------|--------|------|
| `JWT_SECRET` | JWT 签名密钥 | - | ✅ |
| `SPRING_PROFILES_ACTIVE` | Spring 配置文件 | `local` | ❌ |
| `ADMIN_USERNAME` | 管理员用户名 | `admin` | ❌ |
| `ADMIN_PASSWORD` | 管理员密码 | `admin123` | ❌ |
| `MYSQL_PASSWORD` | MySQL 数据库密码 | `123456` | ❌ |
| `NACOS_ENABLED` | 是否启用 Nacos | `false` | ❌ |
| `NACOS_SERVER_ADDR` | Nacos 服务器地址 | `localhost:8848` | ❌ |
| `NACOS_NAMESPACE` | Nacos 命名空间 | - | ❌ |

#### 数据库配置

默认数据库连接信息：
- **主机**: localhost:3306
- **数据库**: online_store
- **用户名**: root
- **密码**: 123456

如需修改，请编辑 `application.yaml` 文件。

#### 数据库架构

系统包含以下主要数据表：

- `category` - 商品分类表
- `brand` - 品牌表
- `attribute` - 属性表
- `attribute_value` - 属性值表
- `item` - 商品主表
- `sku` - SKU表（库存单位）
- `item_attribute_relation` - 商品属性关联表
- `member` - 用户表
- `item_access_log` - 商品访问日志表

所有数据表创建脚本位于 `src/main/resources/sql/` 目录下。

## 🔍 API 文档

启动应用后，访问以下地址查看 API 文档：

- **应用主页**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **应用信息**: http://localhost:8080/actuator/info

### 主要 API 端点

#### 用户管理 API
- `POST /api/members/register` - 用户注册
- `POST /api/members/login` - 用户登录
- `GET /api/members/profile` - 获取用户信息

#### 商品管理 API
- `GET /api/items` - 获取商品列表
- `POST /api/items` - 创建商品
- `GET /api/items/{id}` - 获取商品详情
- `PUT /api/items/{id}` - 更新商品信息
- `DELETE /api/items/{id}` - 删除商品

#### 分类品牌 API
- `GET /api/categories` - 获取商品分类
- `POST /api/categories` - 创建分类
- `GET /api/brands` - 获取品牌列表
- `POST /api/brands` - 创建品牌

#### 属性管理 API
- `GET /api/attributes` - 获取属性列表
- `POST /api/attributes` - 创建属性
- `PUT /api/attributes/{id}` - 更新属性
- `DELETE /api/attributes/{id}` - 删除属性

## 🧪 测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=MessageSourceTest

# 跳过测试构建
mvn clean package -DskipTests

# 生成测试报告
mvn surefire-report:report

# 运行集成测试
mvn verify
```

### 测试覆盖率

```bash
# 生成测试覆盖率报告
mvn jacoco:report

# 查看覆盖率报告
open target/site/jacoco/index.html
```

## 📦 部署

### Docker 部署

1. **构建镜像**
   ```bash
   docker build -t online-store:latest .
   ```

2. **运行容器**
   ```bash
   docker run -d \
     --name online-store \
     -p 8080:8080 \
     -e JWT_SECRET="your-secret-key" \
     online-store:latest
   ```

### 生产环境部署

1. **构建项目**
   ```bash
   mvn clean package -DskipTests
   ```

2. **运行 JAR 文件**
   ```bash
   java -jar target/online-store-1.0-SNAPSHOT.jar \
     --add-opens java.base/java.lang=ALL-UNNAMED
   ```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📝 开发规范

- 遵循 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- 提交信息使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范
- 所有 API 都需要有对应的单元测试
- 新功能需要更新相应的文档
- 数据库表结构变更需要提供对应的 SQL 脚本
- 国际化文本放在 `src/main/resources/i18n/` 目录下
- 所有业务异常使用 `BizException` 统一处理

### 代码结构说明

- `bean/` - 业务对象和数据传输对象
- `config/` - 配置类（Redis、MyBatis、Security等）
- `controller/` - REST API 控制器
- `dto/` - 数据传输对象和请求响应类
- `entity/` - 数据库实体类
- `enums/` - 枚举类型定义
- `mapper/` - MyBatis 数据访问层
- `service/` - 业务逻辑层
- `security/` - 安全相关类（JWT等）
- `utils/` - 工具类

## ❓ 常见问题

**Q: 启动时出现 "java.lang.IllegalAccessError" 错误？**
A: 请确保添加了 JVM 参数：`--add-opens java.base/java.lang=ALL-UNNAMED`

**Q: 无法连接数据库？**
A: 请检查 MySQL 服务是否启动，数据库是否已创建，连接信息是否正确。

**Q: Redis 连接失败？**
A: 请确保 Redis 服务正在运行，并检查连接配置。

**Q: JWT 相关错误？**
A: 请确保设置了 `JWT_SECRET` 环境变量。

## 📄 许可证

本项目基于 [MIT 许可证](LICENSE) 开源。

## 📧 联系方式

如有问题或建议，请通过以下方式联系：

- 创建 [Issue](../../issues)
- 发送邮件到：[your-email@example.com]

---

⭐ 如果这个项目对你有帮助，请给个 Star！ 