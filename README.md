# 在线商店 (Online Store)

一个功能完整的在线商店后端系统，基于Spring Boot 3和Spring Cloud微服务架构构建，提供商品管理、用户管理、分类管理等核心电商功能。

## ✨ 功能特性

- 🛍️ **商品管理**：支持商品创建、编辑、分类、SKU管理
- 👥 **用户管理**：用户注册、登录、JWT认证
- 🏷️ **分类管理**：多级商品分类体系
- 🏭 **品牌管理**：品牌信息维护
- 🔍 **属性管理**：商品属性与属性值管理
- 📊 **访问日志**：商品访问记录统计
- 🔒 **安全认证**：基于Spring Security + JWT的安全框架
- 📁 **文件存储**：集成阿里云OSS对象存储
- 🌐 **国际化**：多语言支持(中文/英文)

## 🛠️ 技术栈

### 核心框架
- **Java 17** - JDK版本
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Cloud Alibaba** - 阿里云微服务组件

### 数据存储
- **MySQL 8.2.0** - 主数据库
- **Redis 6.0+** - 缓存数据库
- **MyBatis 3.0.3** - ORM框架
- **PageHelper 2.1.0** - 分页插件

### 其他组件
- **Nacos 2.2.0** - 服务注册发现与配置管理
- **JWT 0.11.5** - 令牌认证
- **Lombok 1.18.36** - 代码简化
- **阿里云OSS 3.18.1** - 对象存储
- **Jedis 5.2.0** - Redis客户端

## 📁 项目结构

```
online_store/
├── src/main/java/com/example/onlinestore/
│   ├── bean/                    # 业务对象
│   ├── config/                  # 配置类
│   ├── controller/              # 控制器层
│   │   ├── AttributeController.java    # 属性管理
│   │   ├── BrandController.java        # 品牌管理
│   │   ├── CategoryController.java     # 分类管理
│   │   ├── ItemController.java         # 商品管理
│   │   ├── ItemDetailController.java   # 商品详情
│   │   └── MemberController.java       # 用户管理
│   ├── dto/                     # 数据传输对象
│   ├── entity/                  # 数据库实体
│   ├── enums/                   # 枚举类
│   ├── mapper/                  # MyBatis映射器
│   ├── security/                # 安全相关
│   ├── service/                 # 业务逻辑层
│   └── utils/                   # 工具类
├── src/main/resources/
│   ├── mapper/                  # MyBatis XML映射文件
│   ├── sql/                     # 数据库脚本
│   ├── i18n/                    # 国际化文件
│   └── application.yaml         # 配置文件
├── scripts/                     # 脚本工具
└── docker-compose.yaml          # Docker编排文件
```

## 🚀 快速开始

### 环境要求

- **JDK 17+**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis 6.0+**

### 1. 使用Docker快速启动依赖服务

```bash
# 启动MySQL和Redis
docker-compose --profile all up -d

# 或单独启动MySQL
docker-compose --profile without-redis up -d
```

### 2. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行表结构脚本
-- 依次执行 src/main/resources/sql/ 目录下的所有.sql文件
```

### 3. 配置应用

复制并修改配置文件：

```bash
# 复制本地配置文件模板
cp src/main/resources/application-local.yaml.example src/main/resources/application-local.yaml
```

关键配置项：

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

# JWT密钥（生产环境请使用复杂密钥）
jwt:
  secret: your-secret-key
```

### 4. 启动应用

```bash
# 方式1：Maven启动
mvn clean spring-boot:run

# 方式2：打包后启动
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 5. 验证启动

访问健康检查端点：
```bash
curl http://localhost:8080/actuator/health
```

## 📊 数据库表结构

主要数据表：

- `member` - 用户信息表
- `item` - 商品基本信息表
- `sku` - 商品SKU表
- `category` - 商品分类表
- `brand` - 品牌信息表
- `attribute` - 属性表
- `attribute_value` - 属性值表
- `item_attribute_relation` - 商品属性关联表
- `item_access_log` - 商品访问日志表

## 🔧 开发指南

### 环境变量

可通过环境变量覆盖默认配置：

```bash
export SPRING_PROFILES_ACTIVE=prod
export JWT_SECRET=your-production-secret
export MYSQL_HOST=your-mysql-host
export REDIS_HOST=your-redis-host
```

### 测试数据生成

```bash
# 使用scripts目录下的工具生成测试数据
cd scripts
python main.py
```

### API文档

应用启动后，主要API端点：

- **用户管理**: `/api/members/*`
- **商品管理**: `/api/items/*`
- **分类管理**: `/api/categories/*`
- **品牌管理**: `/api/brands/*`
- **属性管理**: `/api/attributes/*`

## 📝 配置文件说明

- `application.yaml` - 主配置文件
- `application-local.yaml` - 本地开发环境配置
- `bootstrap.yaml` - 启动配置（Nacos等）

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情 