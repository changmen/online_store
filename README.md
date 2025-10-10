# 在线商店 (Online Store)

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)

一个基于 Spring Cloud 微服务架构的现代化在线商店系统，提供完整的电商功能，包括商品管理、用户管理、订单处理等核心业务模块。

## ✨ 主要特性

- 🛍️ **商品管理**: 支持商品分类、品牌管理、属性配置、SKU管理
- 👤 **用户系统**: 会员注册、登录、权限管理
- 🔐 **安全认证**: 基于JWT的安全认证和授权
- 📊 **数据统计**: 商品访问日志、用户行为分析
- 🚀 **高性能**: Redis缓存、数据库连接池优化
- 🌐 **微服务**: Spring Cloud生态，支持服务注册发现
- 🐳 **容器化**: Docker和Docker Compose支持
- 📚 **多语言**: 国际化支持

## 🛠️ 技术栈

### 后端技术
- **Java**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: 安全认证框架
- **MyBatis**: 3.0.3 (数据持久层)
- **PageHelper**: 2.1.0 (分页插件)

### 数据库
- **MySQL**: 8.2.0
- **Redis**: 6.0+ (缓存)

### 服务注册与发现
- **Nacos**: 2.2.0 (配置中心 & 服务注册)
- **Spring Cloud Alibaba**: 2022.0.0.0

### 其他组件
- **JWT**: 0.11.5 (Token认证)
- **Jedis**: 5.2.0 (Redis客户端)
- **Lombok**: 1.18.36 (代码简化)
- **Aliyun OSS**: 3.18.1 (文件存储)
- **Apache Commons**: 工具类库

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 主启动类
│   │   │   ├── bean/                          # 配置Bean
│   │   │   ├── config/                        # 配置类
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # REST控制器
│   │   │   │   ├── AttributeController.java   # 属性管理
│   │   │   │   ├── BrandController.java       # 品牌管理
│   │   │   │   ├── CategoryController.java    # 分类管理
│   │   │   │   ├── ItemController.java        # 商品管理
│   │   │   │   ├── ItemDetailController.java  # 商品详情
│   │   │   │   └── MemberController.java      # 会员管理
│   │   │   ├── dto/                          # 数据传输对象
│   │   │   ├── entity/                       # 实体类
│   │   │   ├── enums/                        # 枚举类
│   │   │   ├── exceptions/                   # 异常处理
│   │   │   ├── handler/                      # 处理器
│   │   │   ├── mapper/                       # MyBatis映射器
│   │   │   ├── security/                     # 安全配置
│   │   │   ├── service/                      # 业务逻辑层
│   │   │   └── utils/                        # 工具类
│   │   └── resources/
│   │       ├── application.yaml              # 主配置文件
│   │       ├── application-local.yaml        # 本地环境配置
│   │       ├── bootstrap.yaml                # 启动配置
│   │       ├── mapper/                       # MyBatis XML映射文件
│   │       ├── sql/                          # 数据库脚本
│   │       └── i18n/                         # 国际化文件
│   └── test/                                 # 测试代码
├── scripts/                                  # Python脚本工具
├── docker-compose.yaml                       # Docker编排文件
├── Dockerfile                                # Docker镜像构建文件
├── pom.xml                                   # Maven项目配置
└── README.md                                 # 项目说明文档
```

## ⚙️ 环境要求

### 必需软件
- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **Docker**: 20.0+ (可选，用于容器化部署)
- **Docker Compose**: 2.0+ (可选)

### 推荐环境
- **操作系统**: Linux / macOS / Windows 10+
- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **内存**: 8GB+
- **硬盘**: 10GB+ 可用空间

## 🚀 快速开始

### 方式一：传统部署

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 准备数据库
```sql
# 连接MySQL并创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入初始化脚本
source src/main/resources/sql/init.sql;
```

#### 3. 配置环境
```bash
# 设置环境变量
export JWT_SECRET=your_jwt_secret_key_here
export SPRING_PROFILES_ACTIVE=local

# 或者修改 application-local.yaml 中的配置
```

#### 4. 启动服务
```bash
# 确保MySQL和Redis服务已启动
sudo systemctl start mysql
sudo systemctl start redis

# 编译和运行应用
mvn clean compile
mvn spring-boot:run

# 或者使用jar包运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

#### 5. 验证部署
```bash
# 检查应用状态
curl http://localhost:8080/actuator/health

# 访问主页
curl http://localhost:8080/
```

### 方式二：Docker 部署

#### 1. 使用 Docker Compose（推荐）
```bash
# 启动所有服务（MySQL + Redis + 应用）
docker-compose --profile all up -d

# 仅启动数据库服务
docker-compose --profile without-redis up -d

# 查看服务状态
docker-compose ps
```

#### 2. 手动构建和运行
```bash
# 构建应用镜像
docker build -t online-store:latest .

# 运行容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=local \
  -e JWT_SECRET=your_jwt_secret \
  online-store:latest
```

#### 3. 数据初始化
```bash
# 进入MySQL容器执行初始化脚本
docker exec -i mysql_container mysql -uroot -p123456 online_store < src/main/resources/sql/init.sql
```

## 📚 API 文档

应用启动后，可通过以下接口访问系统功能：

### 基础信息
- **基础URL**: `http://localhost:8080`
- **API版本**: v1
- **认证方式**: JWT Token
- **数据格式**: JSON

### 主要接口

#### 👤 用户管理
```bash
# 会员注册
POST /api/members/register
Content-Type: application/json
{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com"
}

# 用户登录
POST /api/members/login
Content-Type: application/json
{
  "username": "testuser",
  "password": "password123"
}

# 获取用户信息
GET /api/members/profile
Authorization: Bearer <jwt_token>
```

#### 🛍️ 商品管理
```bash
# 获取商品列表
GET /api/items?page=1&size=10&category=electronics

# 获取商品详情
GET /api/items/{itemId}

# 获取商品详细信息
GET /api/items/{itemId}/details

# 搜索商品
GET /api/items/search?keyword=phone&brand=apple
```

#### 📂 分类管理
```bash
# 获取商品分类
GET /api/categories

# 获取分类下的商品
GET /api/categories/{categoryId}/items
```

#### 🏷️ 品牌管理
```bash
# 获取品牌列表
GET /api/brands

# 获取品牌详情
GET /api/brands/{brandId}

# 获取品牌下的商品
GET /api/brands/{brandId}/items
```

#### 🏷️ 属性管理
```bash
# 获取商品属性
GET /api/attributes

# 获取属性值
GET /api/attributes/{attributeId}/values
```

### 响应格式
所有API响应都遵循统一格式：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    // 具体数据
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

### 错误码
- `200`: 成功
- `400`: 请求参数错误
- `401`: 未授权
- `403`: 权限不足
- `404`: 资源不存在
- `500`: 服务器内部错误

### 示例测试
```bash
# 使用curl测试API
curl -X GET "http://localhost:8080/api/items" \
     -H "Content-Type: application/json"

# 使用认证Token
curl -X GET "http://localhost:8080/api/members/profile" \
     -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## ⚙️ 配置说明

### 环境变量
项目支持以下环境变量配置：

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `SPRING_PROFILES_ACTIVE` | `local` | 激活的Spring配置文件 |
| `JWT_SECRET` | **必需** | JWT签名密钥，建议使用64位随机字符串 |
| `ADMIN_USERNAME` | `admin` | 管理员用户名 |
| `ADMIN_PASSWORD` | `admin123` | 管理员密码 |
| `NACOS_ENABLED` | `false` | 是否启用Nacos服务注册发现 |

### 数据库配置
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

### Redis配置
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: 
      database: 0
      jedis:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
```

### JWT配置
```yaml
jwt:
  secret: ${JWT_SECRET}  # 从环境变量获取
  expiration: 86400      # Token过期时间（秒）
```

### 不同环境配置

#### 开发环境 (application-local.yaml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store_dev
  data:
    redis:
      host: localhost
      port: 6379
```

#### 生产环境配置建议
```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}
      password: ${REDIS_PASSWORD}
```

## 🛠️ 开发指南

### 代码规范
- 使用 **Java 17** 特性和语法
- 遵循 **Spring Boot** 最佳实践
- 使用 **Lombok** 简化代码
- 遵循 **RESTful API** 设计原则
- 使用 **MyBatis** 进行数据库操作

### 本地开发环境搭建
```bash
# 1. 克隆代码
git clone <repository-url>
cd online_store

# 2. 启动本地数据库服务
docker-compose --profile all up -d

# 3. 初始化数据库
mysql -h127.0.0.1 -P3306 -uroot -p123456 online_store < src/main/resources/sql/init.sql

# 4. 设置环境变量
export JWT_SECRET="your-secret-key-must-be-at-least-64-characters-long-for-security"
export SPRING_PROFILES_ACTIVE=local

# 5. 启动应用
mvn spring-boot:run
```

### 单元测试
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=MemberServiceTest

# 生成测试报告
mvn test jacoco:report
```

### 代码质量检查
```bash
# 使用SpotBugs进行静态分析
mvn spotbugs:check

# 使用Checkstyle检查代码风格
mvn checkstyle:check
```

### 数据库迁移
```bash
# 运行数据库迁移脚本
mysql -h127.0.0.1 -P3306 -uroot -p123456 online_store < src/main/resources/sql/migrations/V1__Initial_Setup.sql
```

## 📊 监控和日志

### 健康检查
应用提供了多个健康检查端点：
```bash
# 基本健康检查
curl http://localhost:8080/actuator/health

# 数据库连接状态
curl http://localhost:8080/actuator/health/db

# Redis连接状态
curl http://localhost:8080/actuator/health/redis

# 应用信息
curl http://localhost:8080/actuator/info

# 指标信息
curl http://localhost:8080/actuator/metrics
```

### 日志配置
日志文件位置：`logs/online-store.log`

不同日志级别：
- **ERROR**: 系统错误和异常
- **WARN**: 警告信息
- **INFO**: 一般信息
- **DEBUG**: 调试信息

## 🔧 故障排查

### 常见问题

#### 1. 应用无法启动
```bash
# 检查端口是否被占用
netstat -tlnp | grep 8080

# 检查Java版本
java -version

# 检查数据库连接
mysql -h127.0.0.1 -P3306 -uroot -p123456 -e "SELECT 1"
```

#### 2. 数据库连接失败
- 检查MySQL服务是否运行
- 验证数据库凭据
- 检查防火墙设置

#### 3. Redis连接问题
```bash
# 测试Redis连接
redis-cli -h localhost -p 6379 ping
```

#### 4. JWT Token错误
- 检查JWT_SECRET环境变量是否设置
- 确保密钥长度足够（至少64位）

### 性能优化
- 使用Redis缓存热点数据
- 数据库连接池优化
- 分页查询优化
- 数据库索引优化

## 🤝 贡献指南

### 提交代码
1. Fork 项目到你的GitHub账户
2. 创建特性分支: `git checkout -b feature/new-feature`
3. 提交修改: `git commit -am 'Add new feature'`
4. 推送分支: `git push origin feature/new-feature`
5. 创建 Pull Request

### 代码检查清单
- [ ] 代码符合项目规范
- [ ] 添加必要的注释
- [ ] 编写单元测试
- [ ] 更新相关文档
- [ ] 所有测试通过

### 报告问题
请在GitHub Issues中报告问题，并提供：
- 问题描述
- 重现步骤
- 预期结果
- 实际结果
- 环境信息

## 📝 开源许可

本项目采用 [MIT License](LICENSE) 许可证。

## 📞 联系方式

- **项目作者**: [Your Name]
- **邮箱**: your.email@example.com
- **项目主页**: https://github.com/yourusername/online_store

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！ 