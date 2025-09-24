# 🛒 Online Store - 在线商店系统

一个基于 Spring Cloud 微服务架构的现代化在线商店项目，提供完整的电商功能和企业级安全特性。

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-latest-red.svg)](https://redis.io/)

## 📋 项目简介

本项目是一个功能完整的电商平台，包含商品管理、品牌管理、分类管理、用户认证、权限控制等核心功能。采用前后端分离架构，支持微服务部署，具备良好的扩展性和可维护性。

### 🎯 核心功能

- 👥 **用户管理**：用户注册、登录、JWT认证
- 🏷️ **商品管理**：商品信息、详情展示、SKU管理
- 🏪 **品牌管理**：品牌增删改查、分页查询
- 📂 **分类管理**：商品分类体系
- 🔒 **安全认证**：Spring Security + JWT
- 📊 **访问统计**：商品访问日志记录
- 🌐 **国际化**：多语言支持
- 📁 **文件存储**：阿里云OSS集成

## 🛠️ 技术栈

### 后端技术

| 技术 | 版本 | 描述 |
|------|------|------|
| **Java** | 17 | 编程语言 |
| **Spring Boot** | 3.4.3 | 应用框架 |
| **Spring Cloud** | 2024.0.0 | 微服务框架 |
| **Spring Security** | 6.x | 安全框架 |
| **Spring Data Redis** | 3.x | Redis集成 |
| **MyBatis** | 3.0.3 | ORM框架 |
| **PageHelper** | 2.1.0 | 分页插件 |
| **MySQL Connector** | 8.2.0 | MySQL驱动 |
| **Jedis** | 5.2.0 | Redis客户端 |
| **JWT** | 0.11.5 | Token认证 |
| **Lombok** | 1.18.36 | 代码简化 |
| **Aliyun OSS** | 3.18.1 | 对象存储 |

### 中间件 & 数据库

- **数据库**：MySQL 8.0+
- **缓存**：Redis 6.0+
- **服务发现**：Nacos（可选）
- **配置中心**：Nacos Config（可选）

## 📁 项目结构

```
online-store/
├── 📁 src/main/java/com/example/onlinestore/
│   ├── 📄 OnlineStoreApplication.java      # 应用启动类
│   ├── 📁 controller/                      # REST控制器
│   │   ├── 📄 MemberController.java        # 用户管理
│   │   ├── 📄 BrandController.java         # 品牌管理
│   │   ├── 📄 CategoryController.java      # 分类管理
│   │   ├── 📄 ItemController.java          # 商品管理
│   │   ├── 📄 ItemDetailController.java    # 商品详情
│   │   └── 📄 AttributeController.java     # 属性管理
│   ├── 📁 service/                         # 业务逻辑层
│   ├── 📁 mapper/                          # 数据访问层
│   ├── 📁 bean/                            # 实体类
│   ├── 📁 dto/                             # 数据传输对象
│   ├── 📁 config/                          # 配置类
│   └── 📁 utils/                           # 工具类
├── 📁 src/main/resources/
│   ├── 📄 application.yaml                # 主配置文件
│   ├── 📄 application-local.yaml          # 本地环境配置
│   ├── 📄 bootstrap.yaml                  # 引导配置
│   ├── 📁 mapper/                          # MyBatis映射文件
│   ├── 📁 sql/                             # 数据库脚本
│   └── 📁 i18n/                            # 国际化资源
├── 📁 scripts/                             # 脚本工具
├── 📄 docker-compose.yaml                 # Docker编排
├── 📄 Dockerfile                          # Docker镜像
└── 📄 pom.xml                             # Maven配置
```

## 🚀 快速开始

### 📋 系统要求

- ☕ **JDK 17+**
- 📦 **Maven 3.6+**
- 🗄️ **MySQL 8.0+**
- 🔄 **Redis 6.0+**
- 🐳 **Docker** (可选)

### 🔧 环境配置

#### 1. 数据库准备

```sql
-- 创建数据库
CREATE DATABASE online_store 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 创建用户(可选)
CREATE USER 'online_store'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON online_store.* TO 'online_store'@'%';
FLUSH PRIVILEGES;
```

#### 2. 环境变量配置

创建 `.env` 文件或设置系统环境变量：

```bash
# 数据库配置
MYSQL_PASSWORD=your_mysql_password

# JWT密钥(必须设置)
JWT_SECRET=your-256-bit-secret-key-here

# 管理员账号
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123

# 环境配置
SPRING_PROFILES_ACTIVE=local

# Nacos配置(可选)
NACOS_ENABLED=false
```

### 🏃‍♂️ 运行应用

#### 方式一：Maven运行

```bash
# 1. 克隆项目
git clone <repository-url>
cd online-store

# 2. 编译项目
mvn clean compile

# 3. 运行应用
mvn spring-boot:run

# 或者添加JVM参数
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

#### 方式二：Docker运行

```bash
# 1. 启动MySQL和Redis
docker-compose up mysql redis -d

# 2. 构建应用镜像
docker build -t online-store .

# 3. 运行应用容器
docker run -p 8080:8080 \
  -e JWT_SECRET=your-secret-key \
  -e MYSQL_PASSWORD=123456 \
  online-store
```

#### 方式三：打包运行

```bash
# 1. 打包应用
mvn clean package -DskipTests

# 2. 运行JAR文件
java --add-opens java.base/java.lang=ALL-UNNAMED \
     -DJWT_SECRET=your-secret-key \
     -jar target/online-store-1.0-SNAPSHOT.jar
```

### 🔍 验证安装

应用启动后，访问以下端点验证：

- **应用主页**：http://localhost:8080
- **健康检查**：http://localhost:8080/actuator/health
- **API文档**：http://localhost:8080/swagger-ui.html (如果启用)

## 📚 API接口

### 🔐 认证接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/v1/members/registry` | 用户注册 |
| POST | `/api/v1/members/login` | 用户登录 |

### 🛍️ 商品接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/v1/items/{id}` | 获取商品信息 |
| GET | `/api/v1/items/{id}/detail` | 获取商品详情 |

### 🏷️ 品牌接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/v1/brands` | 品牌列表 |
| GET | `/api/v1/brands/{id}` | 获取品牌 |
| POST | `/api/v1/brands` | 创建品牌 |
| PUT | `/api/v1/brands/{id}` | 更新品牌 |
| DELETE | `/api/v1/brands/{id}` | 删除品牌 |

### 📂 分类接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/v1/categories/{id}` | 获取分类 |

### 🏷️ 属性接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/v1/attributes/{id}` | 获取属性 |
| POST | `/api/v1/attributes` | 创建属性 |
| PUT | `/api/v1/attributes/{id}` | 更新属性 |

## ⚙️ 配置说明

### 🔧 application.yaml 主要配置

```yaml
server:
  port: 8080                    # 服务端口

spring:
  application:
    name: online-store          # 应用名称
  profiles:
    active: local               # 激活环境
  datasource:                   # 数据源配置
    url: jdbc:mysql://localhost:3306/online_store
    username: root
    password: ${MYSQL_PASSWORD:123456}
  data:
    redis:                      # Redis配置
      host: localhost
      port: 6379
      database: 0

jwt:
  secret: ${JWT_SECRET}         # JWT密钥(必须设置)
  expiration: 86400             # Token过期时间(秒)
```

### 🌐 环境配置

- **local**: 本地开发环境
- **dev**: 开发环境
- **prod**: 生产环境

## 🐳 Docker部署

### 📋 使用docker-compose

```bash
# 启动所有服务
docker-compose --profile all up -d

# 仅启动数据库服务
docker-compose --profile without-redis up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

## 🔧 开发指南

### 📝 代码规范

- 使用 **Lombok** 简化代码
- 遵循 **RESTful** API设计
- 统一异常处理和响应格式
- 使用 **@Valid** 进行参数校验

### 🧪 测试

```bash
# 运行所有测试
mvn test

# 跳过测试打包
mvn package -DskipTests

# 生成测试报告
mvn test jacoco:report
```

### 📊 监控

项目集成了 Spring Boot Actuator，提供以下监控端点：

- `/actuator/health` - 健康检查
- `/actuator/info` - 应用信息
- `/actuator/metrics` - 性能指标

## 🚨 故障排除

### 常见问题

1. **JWT_SECRET 未设置**
   ```
   错误：Could not resolve placeholder 'JWT_SECRET'
   解决：设置环境变量 JWT_SECRET
   ```

2. **数据库连接失败**
   ```
   错误：Connection refused
   解决：确保MySQL服务启动，检查连接配置
   ```

3. **Redis连接失败**
   ```
   错误：Unable to connect to Redis
   解决：确保Redis服务启动，检查连接配置
   ```

4. **端口被占用**
   ```
   错误：Port 8080 was already in use
   解决：修改server.port配置或杀死占用进程
   ```

### 📋 检查清单

- [ ] JDK 17+ 已安装
- [ ] Maven 3.6+ 已安装 
- [ ] MySQL 8.0+ 服务运行中
- [ ] Redis 6.0+ 服务运行中
- [ ] 数据库 `online_store` 已创建
- [ ] 环境变量 `JWT_SECRET` 已设置
- [ ] 防火墙允许 8080 端口访问

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 📧 Email: [your-email@example.com]
- 🐛 Issues: [项目Issues页面]
- 📖 Wiki: [项目Wiki页面]

---

⭐ 如果这个项目对你有帮助，请给个星标支持！ 