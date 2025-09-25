# 在线商店 (Online Store)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

一个基于Spring Cloud微服务架构的现代化在线商店系统，提供完整的电商功能，包括商品管理、用户认证、订单处理等核心业务功能。

## ✨ 主要特性

- 🏗️ **微服务架构** - 基于Spring Cloud的分布式系统设计
- 🔐 **安全认证** - 集成Spring Security + JWT的用户认证授权
- 📦 **商品管理** - 支持商品分类、品牌、属性等多维度管理
- 👥 **用户系统** - 完整的用户注册、登录、权限管理
- 🎯 **服务发现** - 支持Nacos服务注册与发现
- 📊 **数据持久化** - 基于MyBatis的数据访问层
- ⚡ **缓存优化** - Redis缓存提升系统性能
- 🌍 **国际化** - 支持多语言国际化
- 🐳 **容器化** - 提供Docker部署支持
- 📝 **代码生成** - 集成代码生成工具提升开发效率

## 🛠️ 技术栈

### 后端技术
- **JDK 17** - Java开发平台
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **MyBatis 3.0.3** - 数据访问框架
- **MySQL 8.2.0** - 关系型数据库
- **Redis 5.2.0** - 缓存数据库
- **Nacos 2.2.0** - 服务注册与配置中心
- **JWT** - 身份验证
- **PageHelper** - 分页插件
- **Lombok** - 代码简化工具

### 工具与部署
- **Maven** - 项目构建管理
- **Docker** - 容器化部署
- **Docker Compose** - 容器编排
- **Aliyun OSS** - 对象存储服务

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 启动类
│   │   │   ├── bean/                          # Bean配置
│   │   │   ├── config/                        # 配置类
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # 控制器层
│   │   │   ├── dto/                          # 数据传输对象
│   │   │   ├── entity/                       # 实体类
│   │   │   ├── enums/                        # 枚举类
│   │   │   ├── exceptions/                   # 异常处理
│   │   │   ├── handler/                      # 处理器
│   │   │   ├── mapper/                       # 数据访问层
│   │   │   ├── security/                     # 安全配置
│   │   │   ├── service/                      # 业务逻辑层
│   │   │   └── utils/                        # 工具类
│   │   └── resources/
│   │       ├── application.yaml              # 主配置文件
│   │       ├── application-local.yaml        # 本地环境配置
│   │       ├── bootstrap.yaml                # 引导配置
│   │       ├── i18n/                         # 国际化资源
│   │       ├── mapper/                       # MyBatis映射文件
│   │       └── sql/                          # 数据库脚本
│   └── test/                                  # 测试代码
├── scripts/                                   # 脚本工具
├── docker-compose.yaml                       # Docker编排文件
├── Dockerfile                                 # Docker镜像构建文件
├── pom.xml                                   # Maven配置文件
└── README.md                                 # 项目说明文档
```

## 🚀 快速开始

### 环境要求

- **JDK 17** 或更高版本
- **Maven 3.6** 或更高版本
- **MySQL 8.0** 或更高版本
- **Redis 6.0** 或更高版本
- **Docker** (可选，用于容器化部署)

### 1. 克隆项目

```bash
git clone <repository-url>
cd online_store
```

### 2. 数据库初始化

#### 创建数据库
```sql
CREATE DATABASE online_store 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

#### 导入数据表结构
```bash
# 导入SQL文件（位于 src/main/resources/sql/ 目录）
mysql -u root -p online_store < src/main/resources/sql/category_table.sql
mysql -u root -p online_store < src/main/resources/sql/brand_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_table_table.sql
# ... 导入其他SQL文件
```

### 3. 配置环境变量

创建 `.env` 文件或设置环境变量：

```bash
# 数据库配置
export DB_URL=jdbc:mysql://localhost:3306/online_store
export DB_USERNAME=root
export DB_PASSWORD=123456

# Redis配置
export REDIS_HOST=localhost
export REDIS_PORT=6379

# JWT密钥
export JWT_SECRET=your-secret-key

# 管理员账户
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
```

### 4. 编译和运行

#### 本地运行
```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run

# 或者使用Java直接运行
mvn package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

#### 使用Docker运行
```bash
# 启动数据库和Redis
docker-compose up -d mysql redis

# 构建应用镜像
docker build -t online-store .

# 运行应用容器
docker run -p 8080:8080 --env-file .env online-store
```


## 🔧 开发指南

### 代码结构说明

- **controller/**: REST API接口层，处理HTTP请求
- **service/**: 业务逻辑层，实现核心业务功能
- **mapper/**: 数据访问层，与MyBatis映射文件对应
- **entity/**: 数据库实体类
- **dto/**: 数据传输对象，用于API请求和响应
- **config/**: 配置类，包括数据库、Redis、安全等配置
- **security/**: 安全相关类，包括JWT处理等
- **utils/**: 工具类和助手方法

### 本地开发环境配置

1. **IDE配置**
   - 推荐使用IntelliJ IDEA或Eclipse
   - 安装Lombok插件
   - 配置代码格式化和检查规则

2. **数据库配置**
   ```bash
   # MySQL服务启动
   sudo systemctl start mysql
   
   # 创建数据库用户（可选）
   CREATE USER 'online_store'@'localhost' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON online_store.* TO 'online_store'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Redis配置**
   ```bash
   # Redis服务启动
   sudo systemctl start redis
   
   # 测试Redis连接
   redis-cli ping
   ```

### 环境配置详解

项目支持多环境配置：

- **local**: 本地开发环境
- **dev**: 开发环境
- **test**: 测试环境
- **prod**: 生产环境

通过设置`SPRING_PROFILES_ACTIVE`环境变量或启动参数来切换环境：

```bash
# 方式1: 环境变量
export SPRING_PROFILES_ACTIVE=local
mvn spring-boot:run

# 方式2: 启动参数
mvn spring-boot:run -Dspring.profiles.active=local
```

## 🐳 Docker 部署

### 单容器部署

```bash
# 构建应用镜像
docker build -t online-store:latest .

# 运行应用容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/online_store \
  -e REDIS_HOST=host.docker.internal \
  -e JWT_SECRET=your-production-secret \
  online-store:latest
```

### Docker Compose 部署

```bash
# 启动所有服务（包括MySQL和Redis）
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

### 容器环境变量

创建 `.env` 文件用于Docker Compose：

```env
# 数据库配置
MYSQL_ROOT_PASSWORD=123456
DB_URL=jdbc:mysql://mysql:3306/online_store
DB_USERNAME=root
DB_PASSWORD=123456

# Redis配置
REDIS_HOST=redis
REDIS_PORT=6379
REDIS_PASSWORD=

# 应用配置
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=your-super-secret-jwt-key-for-production
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123

# Nacos配置
NACOS_ENABLED=false
NACOS_SERVER_ADDR=localhost:8848
```

## 📊 API 文档

### 主要 API 端点

#### 用户认证
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/profile` - 获取用户信息

#### 商品管理
- `GET /api/items` - 获取商品列表
- `GET /api/items/{id}` - 获取商品详情
- `POST /api/items` - 创建商品
- `PUT /api/items/{id}` - 更新商品
- `DELETE /api/items/{id}` - 删除商品

#### 分类管理
- `GET /api/categories` - 获取分类列表
- `POST /api/categories` - 创建分类
- `PUT /api/categories/{id}` - 更新分类

#### 品牌管理
- `GET /api/brands` - 获取品牌列表
- `POST /api/brands` - 创建品牌
- `PUT /api/brands/{id}` - 更新品牌

### API 请求示例

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
curl -X GET "http://localhost:8080/api/items?page=1&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 响应格式

所有API响应都遵循RESTful规范，统一返回格式：

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

## 🔧 测试

### 单元测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 运行特定测试方法
mvn test -Dtest=UserServiceTest#testCreateUser
```

### 集成测试

```bash
# 执行集成测试
mvn verify

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

## 📈 性能优化

### 数据库优化
- 使用连接池管理数据库连接
- 为常用查询添加适当索引
- 使用PageHelper实现分页查询

### 缓存策略
- Redis缓存热点数据
- 实现缓存更新和失效机制
- 使用分布式锁防止缓存击穿

### 应用优化
- 使用@Async实现异步处理
- 优化SQL查询和N+1问题
- 合理配置JVM参数

## 🔍 问题排查

### 常见问题

1. **启动失败**
   - 检查数据库和Redis服务是否正常运行
   - 确认端口是否被占用
   - 检查配置文件是否正确

2. **数据库连接问题**
   ```bash
   # 测试数据库连接
   mysql -h localhost -u root -p
   
   # 检查数据库是否存在
   SHOW DATABASES;
   ```

3. **Redis连接问题**
   ```bash
   # 测试Redis连接
   redis-cli ping
   
   # 检查Redis配置
   redis-cli config get "*"
   ```

### 日志查看

```bash
# 查看应用日志
tail -f logs/spring.log

# 查看Docker容器日志
docker logs -f online-store
```

### 性能监控

- **Actuator端点**: http://localhost:8080/actuator
- **健康检查**: http://localhost:8080/actuator/health

## 🤝 贡献指南

欢迎参与在线商店项目的开发！请遵循以下步骤：

### 开发流程

1. **Fork 项目**
   ```bash
   # Fork 本仓库到您的GitHub账户
   # 然后克隆您的Fork
   git clone https://github.com/YOUR_USERNAME/online_store.git
   cd online_store
   ```

2. **创建功能分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **开发和测试**
   - 编写代码并确保遵循项目编码规范
   - 添加单元测试和集成测试
   - 确保所有测试通过

4. **提交更改**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   git push origin feature/your-feature-name
   ```

5. **创建Pull Request**
   - 在GitHub上创建Pull Request
   - 提供详细的功能描述和测试说明
   - 等待代码审查和合并

### 编码规范

1. **Java 编码标准**
   - 遵循Google Java Style Guide
   - 使用有意义的变量和方法名
   - 添加适当的注释和Javadoc

2. **提交信息规范**
   - 使用英文编写提交信息
   - 遵循Conventional Commits规范：
     - `feat:` 新功能
     - `fix:` Bug修复
     - `docs:` 文档更新
     - `style:` 代码格式调整
     - `refactor:` 代码重构
     - `test:` 测试相关
     - `chore:` 构建或辅助工具更改

3. **代码质量要求**
   - 保持测试覆盖率不低于80%
   - 确保所有测试通过
   - 遵循单一职责原则
   - 避免代码重复

### 问题报告

如果您发现了Bug或有功能建议，请通过GitHub Issues提交：

1. **Bug报告**
   - 提供详细的问题描述
   - 包含复现步骤
   - 提供错误日志和环境信息

2. **功能建议**
   - 详细描述建议的功能
   - 说明使用场景和价值
   - 如可能，提供设计思路

### 开发环境设置

推荐的开发工具和配置：

```bash
# IDE插件推荐
# - Lombok Plugin
# - MyBatis Plugin
# - Spring Boot Assistant
# - GitToolBox

# 代码检查工具
mvn checkstyle:check
mvn spotbugs:check
mvn pmd:check

# 代码格式化
mvn spring-javaformat:apply
```

## 📋 更新日志

### v1.0.0 (2024-01-01)
- ✨ 初始版本发布
- ✨ 实现用户认证和授权
- ✨ 实现商品管理功能
- ✨ 实现分类和品牌管理
- ✨ 集成Redis缓存
- ✨ 支持Docker部署
- ✨ 添加单元测试和集成测试

## 📜 相关资源

### 官方文档
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring Cloud 官方文档](https://spring.io/projects/spring-cloud)
- [MyBatis 官方文档](https://mybatis.org/mybatis-3/)
- [Redis 官方文档](https://redis.io/documentation)

### 学习资源
- [Spring Boot 入门教程](https://spring.io/guides/gs/spring-boot/)
- [RESTful API 设计指南](https://restfulapi.net/)
- [Docker 官方教程](https://docs.docker.com/get-started/)

### 开发工具
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Visual Studio Code](https://code.visualstudio.com/)
- [Postman](https://www.postman.com/) - API测试工具
- [DBeaver](https://dbeaver.io/) - 数据库管理工具

## ❓ 常见问题 (FAQ)

### Q: 如何修改默认端口？
A: 在 `application.yaml` 中修改 `server.port` 配置，或者设置环境变量 `SERVER_PORT`。

### Q: 如何启用Nacos服务发现？
A: 设置环境变量 `NACOS_ENABLED=true` 并配置 Nacos 服务器地址。

### Q: 如何配置外部数据库？
A: 修改 `application.yaml` 中的数据库连接配置，或者设置相应的环境变量。

### Q: 如何修改JWT密钥？
A: 设置环境变量 `JWT_SECRET` 为您的安全密钥，建议使用不少于32位的随机字符串。

### Q: 如何查看应用日志？
A: 日志文件默认位于 `logs/` 目录下，或者通过 `docker logs` 命令查看容器日志。

## 📧 联系方式

- **项目主页**: https://github.com/your-org/online_store
- **问题报告**: https://github.com/your-org/online_store/issues
- **讨论社区**: https://github.com/your-org/online_store/discussions
- **邮箱**: your-email@example.com

## 📜 许可证

本项目采用 MIT 许可证。详情请查看 [LICENSE](LICENSE) 文件。

---

<div align="center">

**如果这个项目对您有帮助，请给一个 ⭐ Star！**

[⬆️ 返回顶部](#在线商店-online-store)

</div> 