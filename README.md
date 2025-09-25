# 🛒 Online Store - 在线商店系统

一个基于Spring Cloud微服务架构的现代化在线商店系统，提供完整的电商功能，包括商品管理、用户管理、品牌管理等核心模块。

## ✨ 功能特性

- 🔐 **用户认证与授权** - 基于JWT的用户注册、登录系统
- 📦 **商品管理** - 商品信息管理、商品详情查看、商品分类管理
- 🏷️ **品牌管理** - 品牌信息CRUD操作，支持分页查询
- 🔧 **属性管理** - 商品属性配置与管理
- 👥 **会员系统** - 会员注册、登录、信息管理
- 📊 **访问日志** - 商品访问记录与统计分析
- ☁️ **微服务架构** - 基于Spring Cloud的分布式系统设计
- 🔄 **服务发现** - 集成Nacos实现服务注册与发现
- 📁 **文件存储** - 集成阿里云OSS对象存储服务

## 🛠️ 技术栈

### 核心框架
- **JDK 17** - Java开发环境
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全认证框架
- **Spring Cloud Alibaba** - 阿里云微服务解决方案

### 数据存储
- **MySQL 8.2.0** - 主数据库
- **Redis (Jedis 5.2.0)** - 缓存数据库
- **MyBatis 3.0.3** - 持久层框架
- **PageHelper 2.1.0** - 分页插件

### 中间件与工具
- **Nacos 2.2.0** - 服务注册发现与配置管理
- **JWT (jjwt 0.11.5)** - 身份认证令牌
- **Aliyun OSS 3.18.1** - 对象存储服务
- **Lombok 1.18.36** - 代码简化工具
- **Docker & Docker Compose** - 容器化部署

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # 主启动类
│   │   │   ├── controller/                     # 控制器层
│   │   │   │   ├── BrandController.java        # 品牌管理API
│   │   │   │   ├── ItemController.java         # 商品基础API
│   │   │   │   ├── ItemDetailController.java   # 商品详情API
│   │   │   │   ├── CategoryController.java     # 分类管理API
│   │   │   │   ├── AttributeController.java    # 属性管理API
│   │   │   │   └── MemberController.java       # 用户管理API
│   │   │   ├── service/                        # 业务逻辑层
│   │   │   ├── mapper/                         # 数据访问层
│   │   │   ├── entity/                         # 实体类
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── security/                      # 安全配置
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yml                # 应用配置
│   │       └── mapper/                        # MyBatis映射文件
│   └── test/                                  # 测试代码
├── scripts/                                   # 脚本工具
├── docker-compose.yaml                       # Docker编排文件
├── Dockerfile                                 # Docker镜像构建文件
├── pom.xml                                    # Maven配置文件
└── README.md                                  # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- **JDK 17+** - Java开发环境
- **Maven 3.6+** - 构建工具
- **MySQL 8.0+** - 数据库
- **Redis 6.0+** - 缓存服务
- **Docker & Docker Compose** (可选) - 容器化部署

### 本地开发环境搭建

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online-store
```

#### 2. 数据库初始化
```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户并授权（可选）
CREATE USER 'online_store'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON online_store.* TO 'online_store'@'localhost';
FLUSH PRIVILEGES;
```

#### 3. 配置文件
修改 `src/main/resources/application.yml` 中的配置：

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
    
  # Redis配置
  redis:
    host: localhost
    port: 6379
    password: # 如果有密码请填写
    
# Nacos配置
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
```

#### 4. 启动应用
```bash
# 使用Maven启动
mvn spring-boot:run

# 或者先编译再运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 🐳 Docker部署

#### 使用Docker Compose一键启动
```bash
# 启动所有服务（包括MySQL和Redis）
docker-compose --profile all up -d

# 仅启动MySQL（如果你有独立的Redis服务）
docker-compose --profile without-redis up -d
```

#### 单独构建应用镜像
```bash
# 构建应用镜像
docker build -t online-store:latest .

# 运行容器
docker run -d --name online-store -p 8080:8080 online-store:latest
```

## 📚 API文档

应用启动后，可以通过以下地址访问：

- **应用地址**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **应用信息**: http://localhost:8080/actuator/info

### 主要API端点

#### 用户管理 (`/api/v1/members`)
- `POST /api/v1/members/registry` - 用户注册
- `POST /api/v1/members/login` - 用户登录

#### 商品管理 (`/api/v1/items`)
- `GET /api/v1/items/{itemId}` - 获取商品基本信息
- `GET /api/v1/items/{itemId}/detail` - 获取商品详细信息

#### 品牌管理 (`/api/v1/brands`)
- `GET /api/v1/brands` - 获取品牌列表（支持分页）
- `GET /api/v1/brands/{brandId}` - 获取品牌详情
- `POST /api/v1/brands` - 创建品牌
- `PUT /api/v1/brands/{brandId}` - 更新品牌
- `DELETE /api/v1/brands/{brandId}` - 删除品牌

#### 分类管理 (`/api/v1/categories`)
- `GET /api/v1/categories/{categoryId}` - 获取分类信息

#### 属性管理 (`/api/v1/attributes`)
- `GET /api/v1/attributes/{attributeId}` - 获取属性信息
- `POST /api/v1/attributes` - 创建属性
- `PUT /api/v1/attributes/{attributeId}` - 更新属性

### API请求示例

#### 用户注册
```bash
curl -X POST http://localhost:8080/api/v1/members/registry \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'
```

#### 获取品牌列表
```bash
curl -X GET "http://localhost:8080/api/v1/brands?pageNum=1&pageSize=10&visible=1"
```

## 🔧 配置说明

### 核心配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `server.port` | 应用端口 | 8080 |
| `spring.datasource.url` | 数据库连接地址 | - |
| `spring.redis.host` | Redis主机地址 | localhost |
| `spring.cloud.nacos.discovery.server-addr` | Nacos服务地址 | localhost:8848 |
| `async-record-access-log` | 异步记录访问日志 | true |

### 环境变量支持

可以通过环境变量覆盖配置：

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/online_store
export SPRING_REDIS_HOST=redis
export SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=nacos:8848
```

## 🧪 测试

### 运行单元测试
```bash
mvn test
```

### 运行集成测试
```bash
mvn verify
```

### 代码覆盖率
```bash
mvn jacoco:report
```

## 📈 监控与运维

### 应用监控
项目集成了Spring Boot Actuator，提供以下监控端点：

- `/actuator/health` - 健康检查
- `/actuator/info` - 应用信息
- `/actuator/metrics` - 应用指标
- `/actuator/prometheus` - Prometheus指标（如果启用）

### 日志管理
- 应用日志默认输出到控制台
- 支持通过配置文件调整日志级别
- 集成访问日志记录功能

### 性能优化
- Redis缓存提升数据访问性能
- 数据库连接池优化
- 分页查询减少内存占用

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 [MIT License](LICENSE) 许可证。

## 🆘 支持与帮助

如果在使用过程中遇到问题，请：

1. 查看 [Issues](../../issues) 中是否有类似问题
2. 创建新的 Issue 描述问题
3. 联系维护团队

## 🚧 待办事项

- [ ] 添加商品搜索功能
- [ ] 实现订单管理系统
- [ ] 集成支付网关
- [ ] 添加购物车功能
- [ ] 完善API文档（Swagger集成）
- [ ] 增加单元测试覆盖率
- [ ] 性能测试与优化
- [ ] 国际化支持 