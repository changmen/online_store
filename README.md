# 🛒 Online Store

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

一个基于Spring Cloud微服务架构的现代化在线商店系统，提供完整的电商功能包括用户管理、商品管理、订单处理、支付集成等。

## ✨ 功能特性

- 🔐 **安全认证**: JWT Token + Spring Security
- 👥 **用户管理**: 用户注册、登录、权限管理
- 🛍️ **商品管理**: 商品展示、分类管理、库存管理
- 🛒 **购物车**: 购物车增删改查、持久化存储
- 📦 **订单系统**: 订单创建、状态跟踪、订单历史
- 💳 **支付集成**: 多种支付方式支持
- 🏪 **商家管理**: 商家入驻、商品发布、订单管理
- 📊 **数据分析**: 销售统计、用户行为分析
- 🌐 **国际化**: 多语言支持
- ☁️ **云原生**: Docker容器化部署
- 🔧 **配置中心**: Nacos配置管理
- 📈 **监控**: Spring Boot Actuator健康检查

## 🛠️ 技术栈

### 后端技术
- **框架**: Spring Boot 3.4.3, Spring Cloud 2024.0.0
- **数据库**: MySQL 8.2.0, Redis (Jedis 5.2.0)
- **ORM**: MyBatis 3.0.3, MyBatis Spring Boot Starter 3.0.2
- **安全**: Spring Security, JWT (JJWT 0.11.5)
- **服务发现**: Nacos 2.2.0
- **分页**: PageHelper 2.1.0
- **文件存储**: 阿里云OSS 3.18.1
- **工具库**: Lombok 1.18.36, Apache Commons Lang3 3.17.0

### 开发工具
- **构建工具**: Maven 3.14.0
- **Java版本**: JDK 17
- **容器化**: Docker, Docker Compose
- **代码增强**: Lombok, CGLIB 3.3.0

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # 应用启动类
│   │   │   ├── bean/                           # Bean配置
│   │   │   ├── config/                         # 配置类
│   │   │   ├── controller/                     # REST控制器
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── mapper/                        # MyBatis映射器
│   │   │   ├── security/                      # 安全配置
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yaml               # 应用配置
│   │       ├── application-local.yaml         # 本地环境配置
│   │       ├── bootstrap.yaml                 # 引导配置
│   │       ├── i18n/                         # 国际化资源
│   │       ├── mapper/                       # MyBatis XML映射
│   │       └── sql/                          # SQL脚本
│   └── test/                                  # 测试代码
├── scripts/                                   # 部署脚本
├── docker-compose.yaml                        # Docker编排文件
├── Dockerfile                                 # Docker镜像构建文件
├── pom.xml                                    # Maven配置
└── README.md                                  # 项目说明
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **Docker**: 可选，用于容器化部署

### 本地开发

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online-store
```

#### 2. 数据库初始化
```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行初始化脚本
source src/main/resources/sql/init.sql
```

#### 3. 配置环境变量
```bash
# 设置JWT密钥
export JWT_SECRET=your-secret-key

# 可选：设置管理员账户
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123

# 可选：启用Nacos
export NACOS_ENABLED=true
```

#### 4. 启动依赖服务
```bash
# 使用Docker Compose启动MySQL和Redis
docker-compose --profile all up -d

# 或者单独启动
docker-compose --profile without-redis up -d  # 仅MySQL
```

#### 5. 运行应用
```bash
# 方式1：使用Maven
mvn spring-boot:run

# 方式2：编译后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar

# 方式3：IDE运行
# 在IDE中运行OnlineStoreApplication.java，添加VM参数：
# --add-opens java.base/java.lang=ALL-UNNAMED
```

#### 6. 验证部署
```bash
# 健康检查
curl http://localhost:8080/actuator/health

# API测试
curl http://localhost:8080/api/products
```

### Docker部署

#### 构建镜像
```bash
docker build -t online-store:latest .
```

#### 使用Docker Compose部署
```bash
docker-compose up -d
```

## 📚 API文档

### 认证相关
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/refresh` - 刷新Token

### 用户管理
- `GET /api/users/profile` - 获取用户信息
- `PUT /api/users/profile` - 更新用户信息
- `GET /api/users/orders` - 获取用户订单

### 商品管理
- `GET /api/products` - 获取商品列表
- `GET /api/products/{id}` - 获取商品详情
- `GET /api/categories` - 获取商品分类

### 购物车
- `GET /api/cart` - 获取购物车
- `POST /api/cart/items` - 添加商品到购物车
- `PUT /api/cart/items/{id}` - 更新购物车商品
- `DELETE /api/cart/items/{id}` - 删除购物车商品

### 订单管理
- `POST /api/orders` - 创建订单
- `GET /api/orders/{id}` - 获取订单详情
- `PUT /api/orders/{id}/status` - 更新订单状态

*完整API文档请参考：`http://localhost:8080/swagger-ui.html`*

## 🏗️ 配置说明

### 应用配置 (application.yaml)
```yaml
server:
  port: 8080                    # 服务端口

spring:
  profiles:
    active: local               # 激活的配置文件
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
    username: root
    password: 123456
  data:
    redis:
      host: localhost
      port: 6379

jwt:
  secret: ${JWT_SECRET}         # JWT密钥（环境变量）
  expiration: 86400             # Token过期时间（秒）
```

### 环境变量
- `JWT_SECRET`: JWT签名密钥（必需）
- `SPRING_PROFILES_ACTIVE`: 激活的Spring Profile
- `ADMIN_USERNAME`: 管理员用户名
- `ADMIN_PASSWORD`: 管理员密码
- `NACOS_ENABLED`: 是否启用Nacos服务发现

## 🔧 开发指南

### 代码规范
- 使用Lombok减少样板代码
- 统一的异常处理机制
- RESTful API设计
- 分层架构：Controller -> Service -> Mapper

### 测试
```bash
# 运行所有测试
mvn test

# 运行特定测试
mvn test -Dtest=UserServiceTest
```

### 打包部署
```bash
# 生产环境打包
mvn clean package -Pprod

# 跳过测试打包
mvn clean package -DskipTests
```

## 🐛 故障排除

### 常见问题

1. **启动失败 - 数据库连接**
   ```
   检查MySQL服务是否启动
   确认数据库连接配置正确
   验证数据库用户权限
   ```

2. **JWT相关错误**
   ```
   确保设置了JWT_SECRET环境变量
   检查Token是否过期
   ```

3. **Redis连接失败**
   ```
   检查Redis服务状态
   确认端口和连接配置
   ```

4. **端口占用**
   ```bash
   # 查看端口占用
   lsof -i :8080
   
   # 修改端口
   export SERVER_PORT=8081
   ```

### 日志查看
```bash
# 应用日志
tail -f logs/application.log

# Docker容器日志
docker-compose logs -f online-store
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

- 项目维护者: [Your Name]
- 邮箱: [your.email@example.com]
- 项目链接: [https://github.com/yourusername/online-store]

## 🙏 致谢

感谢以下开源项目和社区的支持：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [MyBatis](https://mybatis.org/)
- [Redis](https://redis.io/)
- [MySQL](https://www.mysql.com/) 