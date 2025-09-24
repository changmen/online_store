# 📱 Online Store - 在线商城系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

这是一个基于Spring Cloud的现代化在线商城系统，提供完整的电商功能，包括商品管理、用户管理、购物车、订单处理、安全认证等核心功能。系统采用微服务架构设计，支持高并发、高可用的生产环境部署。

## ✨ 核心功能

- 🛍️ **商品管理**：商品分类、品牌管理、SKU管理、属性配置
- 👥 **用户管理**：用户注册登录、权限管理、个人信息管理
- 🛒 **购物车**：商品加购、数量调整、批量操作
- 📦 **订单管理**：订单创建、状态跟踪、支付集成
- 🔐 **安全认证**：JWT认证、Spring Security、用户权限控制
- 📊 **数据统计**：商品访问日志、用户行为分析
- 🌐 **国际化**：多语言支持
- ☁️ **服务发现**：Nacos集成（可选）
- 📁 **文件存储**：阿里云OSS集成

## 🛠️ 技术栈

### 后端框架
- **Java 17** - 基础运行环境
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Data Redis** - Redis集成
- **MyBatis 3.0.2** - ORM框架
- **PageHelper** - 分页插件

### 数据库与缓存
- **MySQL 8.2.0** - 主数据库
- **Redis 6.0+** - 缓存和会话存储
- **Jedis 5.2.0** - Redis客户端

### 服务注册与发现
- **Nacos 2.2.0** - 服务注册中心（可选）
- **Spring Cloud Alibaba** - 阿里云生态集成

### 安全与认证
- **JWT (jjwt 0.11.5)** - 令牌认证
- **Spring Boot Actuator** - 应用监控

### 工具库
- **Lombok** - 代码简化
- **Apache Commons Lang3** - 工具类
- **Jackson JSR310** - JSON处理
- **阿里云OSS** - 对象存储

## 📁 项目结构

```
online-store/
├── 📂 src/
│   ├── 📂 main/
│   │   ├── 📂 java/com/example/onlinestore/
│   │   │   ├── 🏠 OnlineStoreApplication.java    # 应用启动类
│   │   │   ├── 📦 bean/                          # Bean配置
│   │   │   ├── ⚙️ config/                        # 配置类
│   │   │   ├── 📊 constants/                     # 常量定义
│   │   │   ├── 🎮 controller/                    # REST控制器
│   │   │   ├── 📋 dto/                           # 数据传输对象
│   │   │   ├── 🗃️ entity/                        # 实体类
│   │   │   ├── 🔢 enums/                         # 枚举定义
│   │   │   ├── ❌ errors/                        # 错误处理
│   │   │   ├── 🚨 exceptions/                    # 异常定义
│   │   │   ├── 🎯 handler/                       # 处理器
│   │   │   ├── 🗺️ mapper/                        # MyBatis映射器
│   │   │   ├── 🔐 security/                      # 安全配置
│   │   │   ├── 🔧 service/                       # 业务服务
│   │   │   └── 🛠️ utils/                         # 工具类
│   │   └── 📂 resources/
│   │       ├── 📄 application.yaml              # 主配置文件
│   │       ├── 📄 application-local.yaml        # 本地环境配置
│   │       ├── 📄 bootstrap.yaml                # 引导配置
│   │       ├── 🌍 i18n/                         # 国际化资源
│   │       ├── 📋 mapper/                       # MyBatis XML映射
│   │       └── 🗃️ sql/                          # 数据库脚本
│   └── 📂 test/                                 # 测试代码
├── 📂 scripts/                                  # 脚本工具
│   ├── 📄 main.py                              # Git分析脚本
│   └── 📄 pyproject.toml                       # Python项目配置
├── 🐳 docker-compose.yaml                      # Docker编排文件
├── 🐳 Dockerfile                               # Docker镜像构建
├── 📋 pom.xml                                  # Maven配置
└── 📖 README.md                                # 项目文档
```

## 🚀 快速开始

### 环境要求

- **Java**: JDK 17 或更高版本
- **Maven**: 3.6 或更高版本  
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **Git**: 用于代码管理

### 本地开发环境搭建

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 数据库初始化
```sql
# 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 使用数据库
USE online_store;

# 执行SQL脚本（按以下顺序）
source src/main/resources/sql/member_table.sql;
source src/main/resources/sql/category_table.sql;
source src/main/resources/sql/brand_table.sql;
source src/main/resources/sql/attribute_table.sql;
source src/main/resources/sql/attribute_value_table.sql;
source src/main/resources/sql/item_table_table.sql;
source src/main/resources/sql/sku_table.sql;
source src/main/resources/sql/item_attribute_relation_table.sql;
source src/main/resources/sql/item_access_log_table.sql;
```

#### 3. 配置环境变量
```bash
# 创建 .env 文件或设置环境变量
export JWT_SECRET="your-secret-key-here"
export SPRING_PROFILES_ACTIVE="local"
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="admin123"
```

#### 4. 启动依赖服务
```bash
# 使用 Docker Compose 启动 MySQL 和 Redis
docker-compose up -d mysql redis

# 或者手动启动服务
# MySQL: 端口 3306，密码 123456
# Redis: 端口 6379，无密码
```

#### 5. 运行应用
```bash
# 编译项目
mvn clean compile

# 启动应用（添加JVM参数）
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"

# 或者直接使用Java运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

#### 6. 验证部署
访问 `http://localhost:8080` 验证应用是否正常启动。

## 🐳 Docker 部署

### 使用 Docker Compose（推荐）
```bash
# 启动所有服务
docker-compose --profile all up -d

# 仅启动数据库服务
docker-compose --profile without-redis up -d

# 查看服务状态
docker-compose ps

# 停止服务
docker-compose down
```

### 单独构建应用镜像
```bash
# 构建应用镜像
docker build -t online-store:latest .

# 运行应用容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=local \
  -e JWT_SECRET=your-secret-key \
  online-store:latest
```

## 📚 API 文档

### 认证相关
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/profile` - 获取用户信息

### 商品管理
- `GET /api/items` - 获取商品列表
- `GET /api/items/{id}` - 获取商品详情
- `POST /api/items` - 创建商品（管理员）
- `PUT /api/items/{id}` - 更新商品（管理员）
- `DELETE /api/items/{id}` - 删除商品（管理员）

### 分类管理
- `GET /api/categories` - 获取分类列表
- `GET /api/categories/{id}` - 获取分类详情
- `POST /api/categories` - 创建分类（管理员）

### 品牌管理
- `GET /api/brands` - 获取品牌列表
- `GET /api/brands/{id}` - 获取品牌详情
- `POST /api/brands` - 创建品牌（管理员）

### 购物车
- `GET /api/cart` - 获取购物车
- `POST /api/cart/items` - 添加商品到购物车
- `PUT /api/cart/items/{id}` - 更新购物车商品数量
- `DELETE /api/cart/items/{id}` - 移除购物车商品

> 💡 **提示**: 具体的API参数和响应格式请参考控制器代码或使用Swagger文档（如已配置）。

## ⚙️ 配置说明

### 应用配置 (application.yaml)
```yaml
server:
  port: 8080                    # 服务端口

spring:
  profiles:
    active: local               # 活动配置文件
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
    username: root
    password: 123456
  data:
    redis:
      host: localhost
      port: 6379
      database: 0

jwt:
  secret: ${JWT_SECRET}         # JWT密钥（必须设置环境变量）
  expiration: 86400             # Token过期时间（秒）
```

### 环境变量配置
| 变量名 | 描述 | 默认值 | 必需 |
|--------|------|--------|------|
| `JWT_SECRET` | JWT签名密钥 | - | ✅ |
| `SPRING_PROFILES_ACTIVE` | Spring配置文件 | local | ❌ |
| `ADMIN_USERNAME` | 管理员用户名 | admin | ❌ |
| `ADMIN_PASSWORD` | 管理员密码 | admin123 | ❌ |
| `NACOS_ENABLED` | 是否启用Nacos | false | ❌ |

## 🔧 开发指南

### 代码规范
- 使用 **Lombok** 简化实体类代码
- 遵循 **RESTful API** 设计原则
- 使用 **MyBatis** 进行数据访问
- 统一异常处理和错误响应格式
- 使用 **DTO** 模式进行数据传输

### 数据库设计
- 使用 **UTF8MB4** 字符集支持emoji
- 遵循数据库命名规范（下划线分隔）
- 合理设计索引提升查询性能
- 使用外键约束保证数据一致性

### 安全考虑
- JWT令牌认证
- 密码BCrypt加密
- CORS跨域配置
- SQL注入防护
- XSS攻击防护

## 🛠️ 故障排除

### 常见问题

**1. 应用启动失败 - IllegalAccessError**
```bash
# 解决方案：添加JVM参数
--add-opens java.base/java.lang=ALL-UNNAMED
```

**2. 数据库连接失败**
- 检查MySQL服务是否启动
- 验证数据库用户名密码
- 确认数据库名称是否正确
- 检查防火墙设置

**3. Redis连接失败**
- 检查Redis服务是否启动
- 验证Redis配置参数
- 检查网络连接

**4. JWT Token无效**
- 确认 `JWT_SECRET` 环境变量已设置
- 检查Token是否过期
- 验证Token格式是否正确

### 日志查看
```bash
# 查看应用日志
tail -f logs/spring.log

# 查看Docker容器日志
docker logs -f online-store
```

## 📊 性能优化

- **数据库优化**：合理使用索引，避免N+1查询
- **缓存策略**：Redis缓存热点数据
- **分页查询**：使用PageHelper进行分页
- **连接池**：优化数据库连接池配置
- **异步处理**：使用Spring异步注解处理耗时操作

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目使用 MIT 许可证。详情请见 [LICENSE](LICENSE) 文件。

## 📞 联系方式

- 项目维护者：[您的姓名](mailto:your.email@example.com)
- 项目主页：[GitHub仓库地址](https://github.com/yourname/online-store)
- 问题反馈：[Issues页面](https://github.com/yourname/online-store/issues)

---

💡 **提示**: 如果您在使用过程中遇到任何问题，请查看[故障排除](#-故障排除)部分或提交Issue。 