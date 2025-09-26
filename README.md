# 在线商店系统 (Online Store)

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

一个基于Spring Cloud微服务架构的现代化在线商店系统，提供完整的电商功能，包括商品管理、会员管理、分类管理、品牌管理等核心模块。

## ✨ 主要功能

- 🛍️ **商品管理** - 商品信息管理、SKU管理、商品属性配置
- 👥 **会员管理** - 用户注册、登录、会员信息管理
- 📁 **分类管理** - 商品分类的层级管理
- 🏷️ **品牌管理** - 品牌信息维护与管理
- 📊 **访问统计** - 商品访问日志记录与分析
- 🔐 **安全认证** - 基于JWT的用户认证与授权
- 📁 **文件存储** - 集成阿里云OSS对象存储服务
- 🌐 **国际化** - 多语言支持
- 🔄 **服务发现** - 支持Nacos服务注册与发现

## 🛠️ 技术栈

### 后端框架
- **JDK 17** - Java开发环境
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Data Redis** - Redis数据访问

### 数据层
- **MyBatis 3.0.3** - ORM框架
- **MyBatis Spring Boot Starter 3.0.2** - MyBatis集成
- **PageHelper 2.1.0** - 分页插件
- **MySQL 8.2.0** - 关系型数据库
- **Redis 5.2.0** - 缓存数据库

### 安全与认证
- **JWT 0.11.5** - JSON Web Token
- **Spring Security** - 安全框架

### 工具库
- **Lombok 1.18.36** - 代码生成工具
- **Commons Lang3 3.17.0** - 工具类库
- **Alibaba Cloud OSS 3.18.1** - 对象存储
- **Nacos 2.2.0** - 服务发现与配置管理

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java      # 应用启动类
│   │   │   ├── bean/                           # Bean配置
│   │   │   ├── config/                         # 配置类
│   │   │   ├── constants/                      # 常量定义
│   │   │   ├── controller/                     # REST控制器
│   │   │   │   ├── AttributeController.java    # 属性管理
│   │   │   │   ├── BrandController.java        # 品牌管理
│   │   │   │   ├── CategoryController.java     # 分类管理
│   │   │   │   ├── ItemController.java         # 商品管理
│   │   │   │   ├── ItemDetailController.java   # 商品详情
│   │   │   │   └── MemberController.java       # 会员管理
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── exceptions/                    # 异常处理
│   │   │   ├── mapper/                        # MyBatis映射器
│   │   │   ├── security/                      # 安全配置
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yaml              # 主配置文件
│   │       ├── application-local.yaml        # 本地环境配置
│   │       ├── bootstrap.yaml                # 启动配置
│   │       ├── i18n/                         # 国际化资源
│   │       ├── mapper/                       # MyBatis XML映射
│   │       └── sql/                          # 数据库初始化脚本
│   └── test/                                 # 测试代码
├── scripts/                                  # 脚本文件
├── docker-compose.yaml                       # Docker容器编排
├── Dockerfile                               # Docker镜像构建
├── pom.xml                                  # Maven项目配置
└── README.md                                # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- **JDK 17+** - Java开发环境
- **Maven 3.6+** - 项目构建工具
- **MySQL 8.0+** - 数据库服务
- **Redis 6.0+** - 缓存服务
- **Docker** (可选) - 容器化部署

### 本地开发环境搭建

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 启动基础服务

**方式一：使用Docker Compose (推荐)**
```bash
# 启动MySQL和Redis服务
docker-compose --profile all up -d
```

**方式二：手动安装**
- 安装并启动MySQL 8.0+
- 安装并启动Redis 6.0+

#### 3. 初始化数据库

```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行初始化脚本
-- 按顺序执行 src/main/resources/sql/ 目录下的SQL文件
```

#### 4. 配置环境变量

创建 `.env` 文件或设置环境变量：
```bash
# JWT密钥 (必须)
export JWT_SECRET=your-jwt-secret-key-here

# 可选配置
export SPRING_PROFILES_ACTIVE=local
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
export NACOS_ENABLED=false
```

#### 5. 编译和运行

```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run

# 或者打包后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

应用启动后，访问 http://localhost:8080

## 🐳 Docker部署

### 使用Docker Compose一键部署

```bash
# 构建并启动所有服务
docker-compose --profile all up --build

# 后台运行
docker-compose --profile all up -d --build
```

### 手动Docker部署

```bash
# 构建应用镜像
docker build -t online-store:latest .

# 运行容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e JWT_SECRET=your-secret-key \
  online-store:latest
```

## 📖 API文档

应用启动后，可以通过以下方式查看API文档：

- **Swagger UI**: http://localhost:8080/swagger-ui.html (如果已配置)
- **Actuator**: http://localhost:8080/actuator (监控端点)

### 主要API端点

| 模块 | 端点 | 描述 |
|------|------|------|
| 商品管理 | `/api/items` | 商品CRUD操作 |
| 商品详情 | `/api/item-details` | 商品详细信息 |
| 分类管理 | `/api/categories` | 商品分类管理 |
| 品牌管理 | `/api/brands` | 品牌信息管理 |
| 属性管理 | `/api/attributes` | 商品属性管理 |
| 会员管理 | `/api/members` | 用户会员功能 |

## ⚙️ 配置说明

### 环境配置

项目支持多环境配置：
- `application.yaml` - 主配置文件
- `application-local.yaml` - 本地开发环境
- `application-dev.yaml` - 开发环境
- `application-prod.yaml` - 生产环境

### 关键配置项

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
      database: 0

# JWT配置
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400  # 24小时

# Nacos配置 (可选)
  cloud:
    nacos:
      discovery:
        enabled: ${NACOS_ENABLED:false}
```

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 生成测试报告
mvn surefire-report:report
```

## 📝 开发指南

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok减少样板代码
- 统一异常处理机制
- RESTful API设计原则

### 数据库设计
- 使用下划线命名法
- 统一字段类型和长度
- 合理设计索引
- 数据库版本控制

## 🔧 故障排除

### 常见问题

1. **应用启动失败**
   - 检查JDK版本是否为17+
   - 确认MySQL和Redis服务是否启动
   - 验证数据库连接配置

2. **JWT认证失败**
   - 确保设置了JWT_SECRET环境变量
   - 检查token是否过期

3. **数据库连接问题**
   - 检查数据库URL、用户名、密码
   - 确认数据库online_store已创建
   - 验证网络连接

### 日志查看

```bash
# 查看应用日志
tail -f logs/online-store.log

# 查看特定级别日志
grep ERROR logs/online-store.log
```

## 🤝 贡献指南

1. Fork本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 📞 联系我们

- 项目维护者: [维护者姓名]
- 邮箱: [联系邮箱]
- 项目地址: [项目仓库地址]

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者们！

---

**注意**: 生产环境部署前，请务必修改默认密码和密钥配置，确保系统安全。 