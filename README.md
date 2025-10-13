# Online Store 在线商店系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

这是一个基于 Spring Cloud 微服务架构的现代化在线商店系统，提供完整的电商功能，包括商品管理、会员管理、分类管理、品牌管理等核心模块。

## 📋 目录

- [技术栈](#技术栈)
- [核心功能](#核心功能)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API文档](#api文档)
- [部署指南](#部署指南)

## 🚀 技术栈

### 后端框架
- **JDK 17** - Java开发工具包
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务架构
- **Spring Security** - 安全认证框架
- **Spring Cloud Alibaba** - 微服务组件

### 数据持久化
- **MyBatis 3.0.3** - ORM框架
- **MySQL 8.2.0** - 关系型数据库
- **Redis 5.2.0** (Jedis) - 缓存数据库
- **PageHelper 2.1.0** - 分页插件

### 服务治理
- **Nacos 2.2.0** - 服务注册与配置中心

### 其他组件
- **JWT 0.11.5** - Token认证
- **Lombok 1.18.36** - 简化Java代码
- **Aliyun OSS 3.18.1** - 对象存储服务
- **Apache Commons Lang3 3.17.0** - 工具类库

## ✨ 核心功能

- **商品管理**：商品信息、SKU管理、商品详情、库存管理
- **分类管理**：商品分类的增删改查
- **品牌管理**：品牌信息维护
- **属性管理**：商品属性定义与属性值管理
- **会员管理**：用户注册、登录、信息管理
- **安全认证**：基于JWT的身份认证与授权
- **访问日志**：商品访问记录统计

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用启动类
│   │   │   ├── bean/                          # Bean配置类
│   │   │   ├── config/                        # 配置类（Redis、MyBatis、Security等）
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # REST API控制器
│   │   │   │   ├── AttributeController.java   # 属性管理API
│   │   │   │   ├── BrandController.java       # 品牌管理API
│   │   │   │   ├── CategoryController.java    # 分类管理API
│   │   │   │   ├── ItemController.java        # 商品管理API
│   │   │   │   ├── ItemDetailController.java  # 商品详情API
│   │   │   │   └── MemberController.java      # 会员管理API
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── exceptions/                    # 自定义异常
│   │   │   ├── handler/                       # 异常处理器
│   │   │   ├── mapper/                        # MyBatis Mapper接口
│   │   │   ├── security/                      # 安全配置
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yaml               # 主配置文件
│   │       ├── application-local.yaml         # 本地环境配置
│   │       ├── bootstrap.yaml                 # 启动配置
│   │       ├── mapper/                        # MyBatis XML映射文件
│   │       ├── sql/                           # 数据库建表脚本
│   │       └── i18n/                          # 国际化资源
│   └── test/                                  # 测试代码
├── scripts/                                   # 辅助脚本
├── docker-compose.yaml                        # Docker Compose配置
├── Dockerfile                                 # Docker镜像构建文件
├── pom.xml                                    # Maven项目配置
└── README.md                                  # 项目说明文档
```

## 💻 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6+ 
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Nacos**: 2.2.0+ (可选，用于服务注册与配置中心)

## 🎯 快速开始

### 方式一：本地运行

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 启动数据库服务

**使用 Docker Compose（推荐）**
```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 或只启动 MySQL
docker-compose --profile without-redis up -d
```

**手动启动**
- 确保 MySQL 和 Redis 服务已在本地运行

#### 3. 初始化数据库

```bash
# 连接到MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 执行建表脚本
USE online_store;
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

#### 4. 配置应用

编辑 `src/main/resources/application.yaml` 或 `application-local.yaml`，修改以下配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password  # 修改为你的密码
  data:
    redis:
      host: localhost
      port: 6379
      password:  # 如果Redis设置了密码，在此填写

jwt:
  secret: your_secret_key_here  # 设置JWT密钥，建议使用强密码
```

#### 5. 编译并运行

```bash
# 编译项目
mvn clean install

# 运行应用（需要添加JVM参数）
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"

# 或使用IDE运行，VM参数设置为：
# --add-opens java.base/java.lang=ALL-UNNAMED
```

#### 6. 验证运行

应用启动后，访问：
- 应用地址：http://localhost:8080
- 健康检查：http://localhost:8080/actuator/health

### 方式二：Docker 部署

```bash
# 构建镜像
docker build -t online-store:latest .

# 运行容器
docker run -d -p 8080:8080 --name online-store online-store:latest
```

## ⚙️ 配置说明

### 环境变量

应用支持以下环境变量配置：

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `SPRING_PROFILES_ACTIVE` | 激活的配置文件 | `local` |
| `ADMIN_USERNAME` | 管理员用户名 | `admin` |
| `ADMIN_PASSWORD` | 管理员密码 | `admin123` |
| `JWT_SECRET` | JWT密钥 | **必须设置** |
| `NACOS_ENABLED` | 是否启用Nacos | `false` |

### 配置文件

- `application.yaml` - 主配置文件，包含通用配置
- `application-local.yaml` - 本地开发环境配置
- `bootstrap.yaml` - 启动配置，用于Nacos配置中心

### Nacos 配置（可选）

如需使用Nacos服务注册与配置中心：

1. 启动 Nacos Server
2. 在 `bootstrap.yaml` 中配置 Nacos 地址
3. 设置环境变量 `NACOS_ENABLED=true`

## 📚 API文档

### 认证

大部分API需要JWT Token认证，请在请求头中添加：
```
Authorization: Bearer <your-jwt-token>
```

### 主要API端点

- **会员管理**: `/api/members`
- **商品管理**: `/api/items`
- **商品详情**: `/api/item-details`
- **分类管理**: `/api/categories`
- **品牌管理**: `/api/brands`
- **属性管理**: `/api/attributes`

## 🚢 部署指南

### 生产环境部署建议

1. **数据库**
   - 使用专用的MySQL服务器或云数据库服务
   - 配置主从复制提高可用性
   - 定期备份数据

2. **缓存**
   - 使用Redis集群提高性能和可用性
   - 配置持久化策略

3. **应用**
   - 使用反向代理（如Nginx）
   - 配置HTTPS证书
   - 启用生产配置文件
   - 设置合适的JVM参数

4. **监控**
   - 使用Spring Boot Actuator监控应用状态
   - 配置日志聚合和分析工具
   - 设置告警机制

### 生产环境JVM参数示例

```bash
java -Xms2g -Xmx2g \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     --add-opens java.base/java.lang=ALL-UNNAMED \
     -jar online-store-1.0-SNAPSHOT.jar
```

## 🤝 贡献指南

欢迎贡献代码、报告问题或提出建议！

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 📧 联系方式

如有问题或建议，请提交 Issue 或 Pull Request。

---

**注意**: 首次运行前请确保已正确配置数据库连接和JWT密钥！ 