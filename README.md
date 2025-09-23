# 🛒 Online Store 在线商店系统

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)

一个基于 Spring Cloud 的现代化在线商店系统，提供完整的电商解决方案，包括商品管理、用户认证、品牌管理、分类管理等核心功能。

## ✨ 功能特性

### 🔐 用户管理
- 用户注册与登录
- JWT 令牌认证
- Spring Security 安全框架
- 用户信息管理

### 📦 商品管理
- 商品信息管理（增删改查）
- SKU 管理
- 商品属性与属性值管理
- 商品访问日志记录
- 商品详情管理

### 🏷️ 分类与品牌
- 商品分类管理
- 品牌管理
- 属性与分类关联

### 🔧 系统功能
- 国际化支持（中文/英文）
- 文件上传（阿里云 OSS）
- 分页查询
- 统一异常处理
- API 响应格式统一
- 数据库访问日志

## 🛠️ 技术栈

### 后端框架
- **JDK 17** - Java 开发环境
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全认证框架
- **Spring Cloud Alibaba** - 阿里巴巴微服务组件

### 数据层
- **MySQL 8.2.0** - 关系型数据库
- **MyBatis 3.0.3** - ORM 框架
- **PageHelper 2.1.0** - MyBatis 分页插件
- **Redis** - 缓存数据库
- **Jedis 5.2.0** - Redis Java 客户端

### 服务发现与配置
- **Nacos 2.2.0** - 服务注册与配置中心

### 安全认证
- **JWT (JJWT 0.11.5)** - JSON Web Token
- **Spring Security** - 安全框架

### 工具库
- **Lombok 1.18.36** - 代码生成工具
- **Apache Commons Lang3 3.17.0** - 工具类库
- **Jackson** - JSON 处理
- **阿里云 OSS 3.18.1** - 对象存储服务

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java     # 应用启动类
│   ├── bean/                          # 业务实体类
│   │   ├── Item.java                  # 商品实体
│   │   ├── Member.java                # 用户实体
│   │   ├── Brand.java                 # 品牌实体
│   │   └── ...
│   ├── config/                        # 配置类
│   │   ├── SecurityConfig.java        # 安全配置
│   │   ├── RedisConfig.java          # Redis配置
│   │   ├── MyBatisConfig.java        # MyBatis配置
│   │   └── ...
│   ├── controller/                    # 控制器层
│   │   ├── ItemController.java        # 商品控制器
│   │   ├── MemberController.java      # 用户控制器
│   │   ├── BrandController.java       # 品牌控制器
│   │   └── ...
│   ├── service/                       # 服务层
│   │   ├── ItemService.java           # 商品服务
│   │   ├── MemberService.java         # 用户服务
│   │   └── ...
│   ├── mapper/                        # 数据访问层
│   ├── dto/                          # 数据传输对象
│   ├── entity/                       # 数据库实体
│   ├── enums/                        # 枚举类
│   ├── security/                     # 安全相关
│   ├── utils/                        # 工具类
│   └── exceptions/                   # 异常处理
├── src/main/resources/
│   ├── sql/                          # 数据库脚本
│   ├── mapper/                       # MyBatis映射文件
│   ├── i18n/                         # 国际化资源
│   ├── application.yaml              # 应用配置
│   └── bootstrap.yaml                # 引导配置
├── scripts/                          # 脚本文件
├── docker-compose.yaml               # Docker编排文件
├── Dockerfile                        # Docker构建文件
└── pom.xml                          # Maven依赖配置
```

## 🚀 快速开始

### 环境要求

- **JDK 17** 或更高版本
- **Maven 3.6** 或更高版本  
- **MySQL 8.0** 或更高版本
- **Redis 6.0** 或更高版本

### 1. 克隆项目

```bash
git clone <repository-url>
cd online-store
```

### 2. 数据库初始化

#### 创建数据库
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 执行数据库脚本
运行 `src/main/resources/sql/` 目录下的所有 SQL 脚本来创建数据表：

```bash
# 按以下顺序执行SQL文件
src/main/resources/sql/
├── member_table.sql              # 用户表
├── brand_table.sql               # 品牌表
├── category_table.sql            # 分类表
├── attribute_table.sql           # 属性表
├── attribute_value_table.sql     # 属性值表
├── item_table_table.sql          # 商品表
├── sku_table.sql                 # SKU表
├── item_attribute_relation_table.sql  # 商品属性关联表
└── item_access_log_table.sql     # 商品访问日志表
```

### 3. 配置应用

#### 修改数据库配置
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
      password: your_redis_password  # 如果有密码
```

#### 设置环境变量
```bash
# JWT密钥（必须设置）
export JWT_SECRET=your-256-bit-secret

# 可选配置
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
export NACOS_ENABLED=false
```

### 4. 编译和运行

```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 5. 使用 Docker 运行环境

项目提供了 Docker Compose 配置文件，可以快速启动 MySQL 和 Redis：

```bash
# 启动全部服务（MySQL + Redis）
docker-compose --profile all up -d

# 仅启动MySQL
docker-compose --profile without-redis up -d

# 停止服务
docker-compose down
```

## 📡 API 接口

应用启动后，访问 `http://localhost:8080`

### 用户相关接口
- `POST /api/v1/members/registry` - 用户注册
- `POST /api/v1/members/login` - 用户登录

### 商品相关接口
- `GET /api/v1/items/{itemId}` - 获取商品详情

### 其他接口
- Spring Boot Actuator: `http://localhost:8080/actuator`

## 🔧 开发指南

### 代码规范
- 使用 Lombok 减少样板代码
- 统一使用 `Response<T>` 包装 API 响应
- 异常处理使用 `@ControllerAdvice` 全局处理
- 使用 `@Valid` 进行请求参数校验

### 数据库访问
- 使用 MyBatis 进行数据访问
- Mapper 接口与 XML 分离
- 支持分页查询（PageHelper）

### 安全认证
- JWT Token 认证
- Spring Security 权限控制
- 密码加密存储

## 🐛 故障排除

### 常见问题

1. **启动时出现 "JWT_SECRET not found" 错误**
   ```bash
   export JWT_SECRET=your-256-bit-secret
   ```

2. **数据库连接失败**
   - 检查 MySQL 服务是否启动
   - 确认数据库连接参数正确
   - 确保数据库已创建

3. **Redis 连接失败**
   - 检查 Redis 服务是否启动
   - 确认 Redis 连接参数正确

4. **Maven 编译失败**
   ```bash
   mvn clean install -U
   ```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 Issue
- 发送邮件
- 微信群讨论

---

⭐ 如果这个项目对你有帮助，请给它一个星标！ 