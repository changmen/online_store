# 🛒 在线商店系统 (Online Store)

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

一个基于 **Spring Cloud** 微服务架构的现代化在线商店系统，提供商品管理、用户管理、属性管理等完整的电商功能。

## 📋 目录

- [功能特性](#-功能特性)
- [技术栈](#-技术栈)
- [项目架构](#-项目架构)
- [快速开始](#-快速开始)
- [部署指南](#-部署指南)
- [API文档](#-api文档)
- [开发指南](#-开发指南)
- [贡献指南](#-贡献指南)

## ✨ 功能特性

### 🏪 核心业务功能
- **商品管理**：商品信息管理、SKU管理、商品分类
- **品牌管理**：品牌信息维护、品牌关联管理
- **属性管理**：商品属性定义、属性值管理
- **用户管理**：用户注册、登录、个人信息管理
- **访问日志**：商品访问记录、用户行为分析

### 🔧 技术特性
- **微服务架构**：基于Spring Cloud构建
- **安全认证**：JWT Token认证，Spring Security集成
- **数据持久化**：MyBatis + MySQL，支持分页查询
- **缓存机制**：Redis缓存，提升系统性能
- **服务发现**：Nacos注册中心（可选）
- **容器化部署**：Docker + Docker Compose支持
- **国际化支持**：多语言消息配置
- **文件存储**：阿里云OSS集成

## 🚀 技术栈

### 后端框架
- **Java 17** - 编程语言
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Data Redis** - Redis集成
- **MyBatis 3.0.3** - 持久层框架
- **PageHelper 2.1.0** - 分页插件

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis 6.0+** - 缓存数据库
- **阿里云OSS** - 对象存储服务

### 服务治理
- **Nacos 2.2.0** - 服务注册与配置中心
- **Spring Cloud Alibaba** - 阿里云微服务组件

### 工具库
- **Lombok** - 代码生成工具
- **Jackson JSR310** - JSON处理
- **JJWT 0.11.5** - JWT令牌处理
- **Apache Commons Lang3** - 工具类库

## 🏗️ 项目架构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java     # 应用启动类
│   ├── bean/                          # 业务对象
│   │   ├── Item.java                  # 商品信息
│   │   ├── Member.java                # 用户信息
│   │   ├── Category.java              # 商品分类
│   │   └── ...                        # 其他业务对象
│   ├── controller/                    # REST控制器
│   │   ├── ItemController.java        # 商品管理接口
│   │   ├── MemberController.java      # 用户管理接口
│   │   └── ...                        # 其他控制器
│   ├── service/                       # 业务服务层
│   │   ├── ItemService.java           # 商品业务逻辑
│   │   ├── MemberService.java         # 用户业务逻辑
│   │   └── impl/                      # 服务实现类
│   ├── mapper/                        # MyBatis映射器
│   │   ├── ItemMapper.java            # 商品数据访问
│   │   └── ...                        # 其他映射器
│   ├── entity/                        # 数据库实体
│   ├── dto/                          # 数据传输对象
│   ├── config/                       # 配置类
│   ├── security/                     # 安全配置
│   ├── utils/                        # 工具类
│   └── exceptions/                   # 异常处理
├── src/main/resources/
│   ├── mapper/                       # MyBatis XML映射文件
│   ├── sql/                          # 数据库脚本
│   ├── i18n/                         # 国际化资源
│   ├── application.yaml              # 应用配置
│   └── bootstrap.yaml                # 启动配置
├── src/test/                         # 单元测试
├── scripts/                          # 辅助脚本
├── docker-compose.yaml               # Docker编排文件
├── Dockerfile                        # Docker镜像构建
└── pom.xml                          # Maven配置
```

## 🚀 快速开始

### 环境要求

| 组件 | 版本要求 | 备注 |
|------|----------|------|
| JDK | 17+ | Oracle JDK 或 OpenJDK |
| Maven | 3.6+ | 项目构建工具 |
| MySQL | 8.0+ | 数据库服务 |
| Redis | 6.0+ | 缓存服务 |
| Docker | 20.0+ | 可选，用于容器化部署 |

### 1. 克隆项目

```bash
git clone <repository-url>
cd online_store
```

### 2. 数据库初始化

#### 2.1 创建数据库
```sql
CREATE DATABASE online_store 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

#### 2.2 执行数据库脚本
```bash
# 按顺序执行 src/main/resources/sql/ 目录下的所有 .sql 文件
mysql -u root -p online_store < src/main/resources/sql/member_table.sql
mysql -u root -p online_store < src/main/resources/sql/category_table.sql
mysql -u root -p online_store < src/main/resources/sql/brand_table.sql
mysql -u root -p online_store < src/main/resources/sql/attribute_table.sql
mysql -u root -p online_store < src/main/resources/sql/attribute_value_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_table_table.sql
mysql -u root -p online_store < src/main/resources/sql/sku_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_attribute_relation_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_access_log_table.sql
```

### 3. 配置文件设置

#### 3.1 数据库配置
编辑 `src/main/resources/application.yaml`：

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
      password: your_redis_password  # 如果Redis设置了密码
      database: 0
```

#### 3.2 JWT密钥配置
设置环境变量或在配置文件中指定JWT密钥：

```bash
export JWT_SECRET=your_jwt_secret_key_here
```

### 4. 启动应用

#### 方式一：Maven启动
```bash
# 清理并编译
mvn clean compile

# 启动应用（添加必要的JVM参数）
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

#### 方式二：JAR包启动
```bash
# 打包
mvn clean package -DskipTests

# 运行
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 5. 验证启动

访问以下端点验证应用是否正常启动：

- **健康检查**: http://localhost:8080/actuator/health
- **应用信息**: http://localhost:8080/actuator/info

## 🐳 部署指南

### Docker Compose 部署（推荐）

1. **启动基础服务**（MySQL + Redis）:
```bash
docker-compose --profile all up -d
```

2. **仅启动MySQL**:
```bash
docker-compose --profile without-redis up -d
```

3. **构建并启动应用**:
```bash
# 构建应用镜像
docker build -t online-store:latest .

# 启动完整服务栈
docker-compose up -d
```

### 生产环境部署

1. **环境变量配置**:
```bash
export SPRING_PROFILES_ACTIVE=prod
export MYSQL_HOST=your-mysql-host
export MYSQL_PORT=3306
export MYSQL_USERNAME=your-username
export MYSQL_PASSWORD=your-password
export REDIS_HOST=your-redis-host
export REDIS_PORT=6379
export JWT_SECRET=your-production-jwt-secret
export NACOS_ENABLED=true
export NACOS_SERVER_ADDR=your-nacos-server:8848
```

2. **启动应用**:
```bash
java -jar \
  --add-opens java.base/java.lang=ALL-UNNAMED \
  -Xms512m -Xmx2g \
  -Dspring.profiles.active=prod \
  target/online-store-1.0-SNAPSHOT.jar
```

## 📖 API文档

### 认证接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/members/register` | 用户注册 |
| POST | `/api/members/login` | 用户登录 |

### 商品管理接口

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| GET | `/api/items` | 获取商品列表 | 否 |
| GET | `/api/items/{id}` | 获取商品详情 | 否 |
| POST | `/api/items` | 创建商品 | 是 |
| PUT | `/api/items/{id}` | 更新商品 | 是 |
| DELETE | `/api/items/{id}` | 删除商品 | 是 |

### 分类管理接口

| 方法 | 路径 | 描述 | 认证要求 |
|------|------|------|----------|
| GET | `/api/categories` | 获取分类列表 | 否 |
| POST | `/api/categories` | 创建分类 | 是 |

### 请求示例

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

#### 获取商品列表
```bash
curl -X GET "http://localhost:8080/api/items?page=1&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🛠️ 开发指南

### 代码规范

- 遵循阿里巴巴Java开发手册
- 使用Lombok减少样板代码
- 统一的异常处理机制
- RESTful API设计原则

### 测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=MessageSourceTest

# 生成测试报告
mvn test jacoco:report
```

### 数据生成脚本

项目包含Python脚本用于生成测试数据：

```bash
cd scripts
pip install -r requirements.txt
python main.py
```

### 日志配置

- 开发环境：控制台输出，DEBUG级别
- 生产环境：文件输出，INFO级别
- 错误监控：集成异常处理和监控告警

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支: `git checkout -b feature/AmazingFeature`
3. 提交更改: `git commit -m 'Add some AmazingFeature'`
4. 推送到分支: `git push origin feature/AmazingFeature`
5. 提交Pull Request

### 提交规范

- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建工具或辅助工具的变动

## 📄 许可证

本项目基于 MIT 许可证开源 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 🆘 问题反馈

如果您在使用过程中遇到问题，请通过以下方式联系：

- 提交 [GitHub Issue](../../issues)
- 发送邮件至项目维护者

## 🔗 相关链接

- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Cloud 官方文档](https://docs.spring.io/spring-cloud/docs/current/reference/html/)
- [MyBatis 官方文档](https://mybatis.org/mybatis-3/)
- [MySQL 官方文档](https://dev.mysql.com/doc/)
- [Redis 官方文档](https://redis.io/documentation)

---

⭐ 如果这个项目对您有帮助，请给我们一个星标！ 