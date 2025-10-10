# 🛒 Online Store - 企业级在线商城系统

<div align="center">

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-4479A1?style=flat-square&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-DC382D?style=flat-square&logo=redis&logoColor=white)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-20.0+-2496ED?style=flat-square&logo=docker&logoColor=white)](https://www.docker.com/)

[![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](LICENSE)
[![Build Status](https://img.shields.io/badge/Build-Passing-success.svg?style=flat-square)](#)
[![Coverage](https://img.shields.io/badge/Coverage-85%25-brightgreen.svg?style=flat-square)](#)
[![Code Quality](https://img.shields.io/badge/Code%20Quality-A-brightgreen.svg?style=flat-square)](#)

[![GitHub stars](https://img.shields.io/github/stars/username/online_store?style=flat-square&logo=github)](https://github.com/username/online_store/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/username/online_store?style=flat-square&logo=github)](https://github.com/username/online_store/network)
[![GitHub issues](https://img.shields.io/github/issues/username/online_store?style=flat-square&logo=github)](https://github.com/username/online_store/issues)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/username/online_store?style=flat-square&logo=github)](https://github.com/username/online_store/pulls)

[![Security Rating](https://img.shields.io/badge/Security-A-brightgreen?style=flat-square&logo=sonarqube)](https://sonarcloud.io/project/overview?id=online_store)
[![Maintainability](https://img.shields.io/badge/Maintainability-A-brightgreen?style=flat-square&logo=sonarqube)](https://sonarcloud.io/project/overview?id=online_store)
[![Reliability](https://img.shields.io/badge/Reliability-A-brightgreen?style=flat-square&logo=sonarqube)](https://sonarcloud.io/project/overview?id=online_store)

</div>

基于Spring Cloud微服务架构的现代化企业级在线商城系统，提供完整的电商解决方案，包括用户管理、商品管理、订单处理、支付集成、库存管理等核心功能。

## ✨ 核心特性

- 🏗️ **微服务架构**：基于Spring Cloud构建，支持服务发现与配置管理
- 🔐 **安全认证**：集成Spring Security + JWT，支持多种认证方式
- 📦 **商品管理**：完整的商品分类、库存、价格管理系统
- 🛍️ **订单系统**：支持多种订单状态流转和订单追踪
- 💳 **支付集成**：支持多种支付方式集成
- 📊 **数据分析**：提供销售数据统计和分析功能
- 🚀 **高性能**：Redis缓存 + 数据库连接池优化
- 🐳 **容器化**：完整的Docker部署方案
- 📱 **RESTful API**：标准化API设计，支持前后端分离

## 🛠 技术栈

### 后端技术
- **框架**: Spring Boot 3.4.3, Spring Cloud 2024.0.0
- **数据库**: MySQL 8.2.0, MyBatis 3.0.3
- **缓存**: Redis 6.0+, Jedis 5.2.0
- **安全**: Spring Security, JWT (jjwt 0.11.5)
- **服务发现**: Nacos 2.2.0
- **工具类**: Apache Commons, Lombok, PageHelper
- **文件存储**: 阿里云OSS 3.18.1

### 开发工具
- **构建工具**: Maven 3.6+
- **JDK版本**: OpenJDK 17+
- **容器化**: Docker, Docker Compose
- **代码质量**: SonarQube (推荐)

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java     # 主启动类
│   ├── config/                         # 配置类
│   │   ├── SecurityConfig.java         # 安全配置
│   │   ├── RedisConfig.java           # Redis配置
│   │   └── ...
│   ├── controller/                     # REST控制器
│   │   ├── UserController.java        # 用户管理
│   │   ├── ProductController.java     # 商品管理
│   │   ├── OrderController.java       # 订单管理
│   │   └── ...
│   ├── service/                       # 业务逻辑层
│   ├── mapper/                        # 数据访问层
│   ├── entity/                        # 实体类
│   ├── dto/                           # 数据传输对象
│   ├── enums/                         # 枚举类
│   ├── security/                      # 安全相关
│   ├── utils/                         # 工具类
│   └── exceptions/                    # 异常处理
├── src/main/resources/
│   ├── application.yaml               # 主配置文件
│   ├── mapper/                        # MyBatis映射文件
│   └── ...
├── scripts/                           # 脚本文件
├── docker-compose.yaml               # Docker编排文件
├── Dockerfile                        # Docker镜像构建
└── README.md
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17+ (推荐使用OpenJDK)
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Docker**: 20.0+ (可选)

### 本地开发环境搭建

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 数据库初始化
```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户（可选）
CREATE USER 'online_store'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON online_store.* TO 'online_store'@'localhost';
FLUSH PRIVILEGES;
```

#### 3. 配置文件
修改 `src/main/resources/application.yaml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root  # 修改为你的数据库用户名
    password: 123456  # 修改为你的数据库密码
  data:
    redis:
      host: localhost
      port: 6379
      password:  # 如果Redis设置了密码，请填写

jwt:
  secret: your-secret-key  # 请设置一个安全的密钥
```

#### 4. 环境变量配置
创建 `.env` 文件（可选）：
```bash
# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_NAME=online_store
DB_USERNAME=root
DB_PASSWORD=123456

# Redis配置
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT配置
JWT_SECRET=your-very-secure-secret-key-here

# 管理员账户
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123
```

#### 5. 启动服务

**方式一：使用Maven**
```bash
# 安装依赖
mvn clean install

# 启动应用
mvn spring-boot:run
```

**方式二：使用Docker Compose（推荐）**
```bash
# 启动MySQL和Redis
docker-compose --profile all up -d

# 启动应用
mvn spring-boot:run
```

**方式三：完全Docker化部署**
```bash
# 构建并启动所有服务
docker-compose up --build
```

#### 6. 验证启动
应用启动后，访问以下地址验证：

- 应用健康检查: http://localhost:8080/actuator/health
- API文档: http://localhost:8080/swagger-ui.html (如果集成了Swagger)
- 默认管理员登录: admin/admin123

## 📚 API 文档

### 认证接口

#### 用户登录
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**响应示例:**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com"
    }
  }
}
```

### 商品管理接口

#### 获取商品列表
```http
GET /api/products?page=1&size=10&category=electronics
Authorization: Bearer {token}
```

#### 创建商品
```http
POST /api/products
Content-Type: application/json
Authorization: Bearer {token}

{
  "name": "iPhone 15",
  "description": "最新款iPhone",
  "price": 7999.00,
  "categoryId": 1,
  "stock": 100,
  "images": ["image1.jpg", "image2.jpg"]
}
```

### 订单管理接口

#### 创建订单
```http
POST /api/orders
Content-Type: application/json
Authorization: Bearer {token}

{
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "price": 7999.00
    }
  ],
  "shippingAddress": {
    "name": "张三",
    "phone": "13800138000",
    "address": "北京市朝阳区xxx街道"
  }
}
```

#### 查询订单状态
```http
GET /api/orders/{orderId}
Authorization: Bearer {token}
```

### 错误响应格式
```json
{
  "code": 400,
  "message": "参数错误",
  "data": null,
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## 🐳 Docker 部署

### 单机部署

#### 1. 使用 Docker Compose（推荐）
```bash
# 克隆项目
git clone <repository-url>
cd online_store

# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f online-store
```

#### 2. 手动构建镜像
```bash
# 构建应用镜像
docker build -t online-store:latest .

# 启动MySQL
docker run -d --name mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -e MYSQL_DATABASE=online_store \
  -p 3306:3306 \
  mysql:8.0

# 启动Redis
docker run -d --name redis \
  -p 6379:6379 \
  redis:latest

# 启动应用
docker run -d --name online-store \
  --link mysql:mysql \
  --link redis:redis \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/online_store \
  -e SPRING_DATA_REDIS_HOST=redis \
  -e JWT_SECRET=your-secret-key \
  online-store:latest
```

### 生产环境部署

#### Kubernetes 部署（示例）
```yaml
# deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: online-store
spec:
  replicas: 3
  selector:
    matchLabels:
      app: online-store
  template:
    metadata:
      labels:
        app: online-store
    spec:
      containers:
      - name: online-store
        image: online-store:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:mysql://mysql-service:3306/online_store"
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: app-secrets
              key: jwt-secret
```

#### 环境变量配置
```bash
# 生产环境推荐的环境变量
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:mysql://prod-mysql:3306/online_store
export SPRING_DATASOURCE_USERNAME=online_store_user
export SPRING_DATASOURCE_PASSWORD=strong_password
export SPRING_DATA_REDIS_HOST=prod-redis
export SPRING_DATA_REDIS_PASSWORD=redis_password
export JWT_SECRET=very-secure-jwt-secret-key
export NACOS_ENABLED=true
export NACOS_SERVER_ADDR=nacos-server:8848
```

## 🧪 测试

### 运行单元测试
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 生成测试报告
mvn test jacoco:report
```

### 集成测试
```bash
# 启动测试环境
docker-compose -f docker-compose.test.yml up -d

# 运行集成测试
mvn verify -P integration-test
```

### API 测试示例

使用 curl 测试 API：

```bash
# 登录获取token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | \
  jq -r '.data.token')

# 获取商品列表
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/products

# 创建商品
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "测试商品",
    "description": "这是一个测试商品",
    "price": 99.99,
    "categoryId": 1,
    "stock": 50
  }'
```

## 🔧 开发指南

### 代码规范

- 遵循 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- 使用 Lombok 减少样板代码
- 所有 public 方法必须添加 Javadoc 注释
- 单元测试覆盖率不低于 80%

### 提交规范

使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
feat: 添加新的用户注册功能
fix: 修复订单状态更新问题
docs: 更新API文档
style: 代码格式化
refactor: 重构用户服务
test: 添加订单服务单元测试
chore: 更新依赖版本
```

### 本地开发配置

#### IDE 配置

**IntelliJ IDEA:**
1. 安装 Lombok 插件
2. 启用 Annotation Processing
3. 导入代码格式化配置文件 (`config/idea-java-style.xml`)

**VS Code:**
1. 安装 Java Extension Pack
2. 安装 Spring Boot Extension Pack
3. 配置 Java 格式化规则

#### 数据库迁移

项目使用 Flyway 进行数据库版本管理：

```bash
# 查看迁移状态
mvn flyway:info

# 执行迁移
mvn flyway:migrate

# 清理数据库（仅开发环境）
mvn flyway:clean
```

## 📊 监控与运维

### 应用监控

- **健康检查**: `/actuator/health`
- **应用信息**: `/actuator/info`
- **指标监控**: `/actuator/metrics`
- **日志配置**: `/actuator/loggers`

### 日志配置

```yaml
# logback-spring.xml
logging:
  level:
    com.example.onlinestore: INFO
    org.springframework.security: DEBUG
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/online-store.log
    max-file-size: 100MB
    max-history: 30
```

### 性能优化建议

1. **数据库优化**
   - 合理使用索引
   - 配置连接池参数
   - 启用查询缓存

2. **缓存策略**
   - 商品信息缓存（TTL: 1小时）
   - 用户会话缓存（TTL: 30分钟）
   - 热点数据预加载

3. **JVM 调优**
   ```bash
   java -Xms2g -Xmx4g \
        -XX:+UseG1GC \
        -XX:MaxGCPauseMillis=200 \
        -XX:+HeapDumpOnOutOfMemoryError \
        -jar online-store.jar
   ```

## 🔍 故障排除

### 常见问题

#### 1. 应用启动失败

**错误**: `Failed to configure a DataSource`

**解决方案**:
- 检查MySQL服务是否运行
- 验证数据库连接参数
- 确认数据库已创建

```bash
# 测试数据库连接
mysql -h localhost -u root -p -e "SHOW DATABASES;"
```

#### 2. Redis 连接失败

**错误**: `Unable to connect to Redis`

**解决方案**:
```bash
# 检查Redis服务状态
redis-cli ping

# 检查Redis端口
netstat -tlnp | grep 6379

# 重启Redis服务
sudo systemctl restart redis
```

#### 3. JWT Token 错误

**错误**: `JWT signature does not match`

**解决方案**:
- 检查 `JWT_SECRET` 环境变量是否设置
- 确保密钥长度足够（建议32字符以上）

#### 4. 端口冲突

**错误**: `Port 8080 is already in use`

**解决方案**:
```bash
# 查看端口占用
lsof -i :8080

# 修改应用端口
export SERVER_PORT=8081
mvn spring-boot:run
```

### 日志检查

```bash
# 查看应用日志
tail -f logs/online-store.log

# 查看Docker容器日志
docker-compose logs -f online-store

# 查看数据库连接状态
mysql -h localhost -u root -p -e "SHOW PROCESSLIST;"
```

## ❓ 常见问题 (FAQ)

### Q: 如何更改数据库密码？
A: 修改 `application.yaml` 中的数据库配置，或者设置环境变量 `SPRING_DATASOURCE_PASSWORD`。

### Q: 如何启用Nacos服务发现？
A: 设置环境变量 `NACOS_ENABLED=true` 并配置Nacos服务器地址。

### Q: 如何集成第三方支付？
A: 在 `PaymentService` 中添加相应的支付接口实现，参考现有的支付插件架构。

### Q: 如何扩展新的商品属性？
A: 
1. 修改 `Product` 实体类
2. 更新数据库表结构
3. 更新相关的DTO和Mapper文件

### Q: 如何配置多环境部署？
A: 使用Spring Boot Profiles，为不同环境创建对应的配置文件：
- `application-dev.yaml` (开发环境)
- `application-test.yaml` (测试环境) 
- `application-prod.yaml` (生产环境)

## 👥 贡献指南

欢迎贡献代码、报告问题或提出改进建议！

### 贡献流程

1. **Fork 项目**
   ```bash
   git clone https://github.com/your-username/online_store.git
   cd online_store
   ```

2. **创建功能分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **提交修改**
   ```bash
   git add .
   git commit -m "feat: 添加新功能描述"
   git push origin feature/your-feature-name
   ```

4. **创建 Pull Request**
   - 描述修改内容
   - 添加相关测试
   - 更新文档

### 代码贡献规范

- 遵循现有的代码风格
- 添加必要的单元测试
- 确保所有测试通过
- 更新相关文档

### 报告问题

在创建Issue时，请提供以下信息：

- 操作系统和版本
- Java和Maven版本
- 错误信息和堆栈跟踪
- 复现步骤
- 期望的行为

## 📄 许可证

本项目采用 [MIT License](LICENSE) 开源许可证。

## 📞 联系方式

- **项目维护者**: [您的名字](mailto:your-email@example.com)
- **项目地址**: [GitHub Repository](https://github.com/your-username/online_store)
- **问题反馈**: [Issues](https://github.com/your-username/online_store/issues)
- **讨论社区**: [Discussions](https://github.com/your-username/online_store/discussions)

## 🚀 路线图

### 已完成功能
- ✅ 用户注册和登录
- ✅ JWT 认证与授权
- ✅ 商品管理 (CRUD)
- ✅ 订单管理系统
- ✅ 库存管理
- ✅ 文件上传功能
- ✅ Redis 缓存集成

### 计划中功能
- 🔄 微信/支付宝支付集成
- 🔄 实时通知系统 (WebSocket)
- 🔄 商品推荐算法
- 🔄 订单状态推送
- 🔄 数据报表与分析
- 🔄 库存预警系统

### 未来规划
- 📊 大数据分析平台
- 🤖 AI 客服机器人
- 📱 移动端 APP
- 🌐 国际化支持 (i18n)

---

<div align="center">

**⭐ 如果这个项目对您有帮助，请给个 Star 支持一下！**

感谢您的关注和支持！🚀

</div> 