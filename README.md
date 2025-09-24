# 🛒 在线商店系统 (Online Store)

> 一个基于Spring Cloud微服务架构的现代化在线商店系统，支持用户管理、商品管理、订单处理、支付集成等完整的电商功能。

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-7.0+-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## ✨ 主要功能

- 🔐 **用户认证与授权**：基于JWT的安全认证系统
- 👤 **用户管理**：用户注册、登录、个人信息管理
- 📦 **商品管理**：商品展示、分类、搜索、库存管理
- 🛍️ **购物车系统**：商品添加、移除、数量调整
- 📋 **订单管理**：订单创建、状态追踪、历史查询
- 💳 **支付集成**：支持多种支付方式
- 🔄 **服务发现**：基于Nacos的微服务注册与发现
- 📊 **监控与健康检查**：Spring Boot Actuator集成
- 📁 **文件存储**：阿里云OSS对象存储集成

## 🛠 技术栈

### 后端技术
- **运行环境**: JDK 17
- **框架**: Spring Boot 3.4.3
- **微服务**: Spring Cloud 2024.0.0
- **服务发现**: Alibaba Nacos 2.2.0
- **数据库**: MySQL 8.2.0
- **缓存**: Redis 7.0+ (Jedis 5.2.0)
- **持久层**: MyBatis 3.0.3 + PageHelper 2.1.0
- **安全认证**: Spring Security + JWT
- **对象存储**: 阿里云OSS

### 开发工具
- **构建工具**: Maven 3.6+
- **容器化**: Docker + Docker Compose
- **代码简化**: Lombok
- **工具库**: Apache Commons, CGLib

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 主启动类
│   │   │   ├── bean/                          # Bean配置
│   │   │   ├── config/                        # 配置类
│   │   │   ├── controller/                    # REST控制器
│   │   │   ├── dto/                          # 数据传输对象
│   │   │   ├── entity/                       # JPA实体类
│   │   │   ├── enums/                        # 枚举类
│   │   │   ├── mapper/                       # MyBatis映射器
│   │   │   ├── security/                     # 安全配置
│   │   │   ├── service/                      # 业务逻辑层
│   │   │   ├── utils/                        # 工具类
│   │   │   ├── exceptions/                   # 异常处理
│   │   │   └── handler/                      # 异常处理器
│   │   └── resources/
│   │       ├── application.yaml              # 主配置文件
│   │       ├── application-local.yaml        # 本地环境配置
│   │       ├── bootstrap.yaml                # 启动配置
│   │       └── mapper/                       # MyBatis SQL映射
│   └── test/                                 # 测试代码
├── scripts/                                  # Python脚本工具
├── docker-compose.yaml                       # Docker编排文件
├── Dockerfile                                # Docker镜像构建文件
├── pom.xml                                   # Maven依赖配置
└── README.md                                 # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本  
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **Docker** (可选): 20.10+ 和 Docker Compose 2.0+

### 方式一：本地运行

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online-store
```

#### 2. 数据库初始化
```sql
-- 连接MySQL并创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户（可选）
CREATE USER 'online_store'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON online_store.* TO 'online_store'@'%';
FLUSH PRIVILEGES;
```

#### 3. 配置环境变量
在项目根目录创建 `.env` 文件：
```properties
# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_NAME=online_store
DB_USERNAME=root
DB_PASSWORD=123456

# Redis配置
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# JWT配置
JWT_SECRET=your-jwt-secret-key-here-should-be-at-least-32-characters

# 管理员账号
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123

# Nacos配置（可选）
NACOS_ENABLED=false
NACOS_SERVER_ADDR=127.0.0.1:8848
```

#### 4. 编译与运行
```bash
# 安装依赖
mvn clean install

# 运行应用（添加JVM参数解决Java 17兼容性问题）
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"

# 或者直接运行jar包
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

#### 5. 访问应用
- **应用地址**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **API文档**: http://localhost:8080/swagger-ui.html (如果集成了Swagger)

### 方式二：Docker 部署

#### 1. 使用 Docker Compose（推荐）
```bash
# 启动所有服务（MySQL + Redis + 应用）
docker-compose --profile all up -d

# 或者只启动数据库服务
docker-compose --profile without-redis up -d  # 只启动MySQL
docker-compose up mysql redis -d              # 启动MySQL和Redis
```

#### 2. 手动构建和运行
```bash
# 构建应用镜像
docker build -t online-store .

# 运行应用容器
docker run -d \
  --name online-store-app \
  -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e REDIS_HOST=host.docker.internal \
  -e JWT_SECRET=your-jwt-secret-here \
  online-store
```

### 方式三：开发环境配置

如果使用IDE（如IntelliJ IDEA）进行开发：

1. **导入项目**: 使用IDE导入Maven项目
2. **配置JVM参数**: 在运行配置中添加 `--add-opens java.base/java.lang=ALL-UNNAMED`
3. **设置环境变量**: 在IDE的运行配置中指定上述环境变量
4. **激活开发配置**: 设置 `spring.profiles.active=local`

## 📚 API 文档

### 主要接口端点

#### 用户认证
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/profile` - 获取用户信息

#### 商品管理
- `GET /api/products` - 获取商品列表
- `GET /api/products/{id}` - 获取商品详情
- `POST /api/products` - 创建商品
- `PUT /api/products/{id}` - 更新商品
- `DELETE /api/products/{id}` - 删除商品

#### 购物车
- `GET /api/cart` - 获取购物车
- `POST /api/cart/items` - 添加商品到购物车
- `PUT /api/cart/items/{id}` - 更新购物车商品数量
- `DELETE /api/cart/items/{id}` - 从购物车移除商品

#### 订单管理
- `GET /api/orders` - 获取订单列表
- `GET /api/orders/{id}` - 获取订单详情
- `POST /api/orders` - 创建订单
- `PUT /api/orders/{id}/status` - 更新订单状态

### 请求示例

#### 用户登录
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

#### 获取商品列表
```bash
curl -X GET "http://localhost:8080/api/products?page=0&size=10" \
  -H "Authorization: Bearer <your-jwt-token>"
```

## ⚙️ 配置说明

### 主要配置参数

| 参数 | 默认值 | 描述 |
|------|---------|------|
| `DB_HOST` | localhost | 数据库主机地址 |
| `DB_PORT` | 3306 | 数据库端口 |
| `DB_NAME` | online_store | 数据库名 |
| `REDIS_HOST` | localhost | Redis主机地址 |
| `JWT_SECRET` | 无 | JWT签名密钥（必填） |
| `NACOS_ENABLED` | false | 是否启用Nacos服务发现 |
| `ADMIN_USERNAME` | admin | 默认管理员用户名 |
| `SPRING_PROFILES_ACTIVE` | local | 激活的配置文件 |

### 环境配置文件

- `application.yaml` - 主配置文件
- `application-local.yaml` - 本地开发环境
- `bootstrap.yaml` - 启动配置（Nacos配置中心）

## 🔧 故障排除

### 常见问题

#### 1. 启动失败："Illegal reflective access"
**解决方案**: 添加JVM参数
```bash
--add-opens java.base/java.lang=ALL-UNNAMED
```

#### 2. 数据库连接失败
**检查清单**:
- 确认MySQL服务正在运行
- 检查数据库连接参数
- 验证用户名和密码
- 确认数据库 `online_store` 已创建

#### 3. Redis连接问题
**检查清单**:
- 确认Redis服务正在运行
- 检查Redis配置参数
- 测试连接: `redis-cli ping`

#### 4. JWT配置错误
**解决方案**: 确保 `JWT_SECRET` 环境变量已设置且长度不少于32字符

### 日志查看

```bash
# 查看应用日志
tail -f logs/spring.log

# Docker日志
docker logs -f online-store-app

# Docker Compose日志
docker-compose logs -f
```

### 性能监控

访问 Spring Boot Actuator 端点：
- 健康检查: http://localhost:8080/actuator/health
- 指标信息: http://localhost:8080/actuator/metrics
- 环境信息: http://localhost:8080/actuator/env

## 🧪 测试

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 生成测试报告
mvn surefire-report:report
```

### 测试覆盖率

```bash
# 生成覆盖率报告
mvn jacoco:report

# 查看报告
open target/site/jacoco/index.html
```

## 📈 部署指南

### 生产环境配置

1. **创建生产配置文件** `application-prod.yaml`
2. **配置数据库连接池**
3. **设置日志级别和输出路径**
4. **配置SSL证书**（如需要）
5. **设置监控和告警**

### Docker生产部署

```yaml
# docker-compose.prod.yaml
version: '3.8'
services:
  app:
    image: online-store:latest
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=prod-mysql
      - REDIS_HOST=prod-redis
    ports:
      - "8080:8080"
    restart: unless-stopped
```

## 🤝 贡献指南

我们欢迎所有形式的贡献！

### 如何贡献

1. **Fork** 本仓库
2. **创建特性分支** (`git checkout -b feature/AmazingFeature`)
3. **提交修改** (`git commit -m 'Add some AmazingFeature'`)
4. **推送到分支** (`git push origin feature/AmazingFeature`)
5. **提交Pull Request**

### 代码规范

- 遵循 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- 编写单元测试
- 提供清晰的提交信息
- 更新相关文档

### 问题反馈

如果您发现问题或有建议，请在 [Issues](issues) 页面提交。

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 👥 维护者

- [@your-username](https://github.com/your-username) - 项目维护者

## 📞 联系方式

- **项目主页**: https://github.com/your-username/online-store
- **问题反馈**: https://github.com/your-username/online-store/issues
- **邮箱**: your-email@example.com

---

⭐ 如果这个项目对您有帮助，请给我们一个星标！ 