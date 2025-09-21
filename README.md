# 🛒 Online Store - 在线商店系统

一个基于 Spring Boot 和 Spring Cloud 构建的现代化在线商店系统，提供完整的电商功能，包括商品管理、会员系统、购物车、订单处理等核心模块。

## ✨ 主要特性

- 🔐 **安全认证** - 基于 JWT 的用户认证和授权系统
- 👥 **会员管理** - 完整的用户注册、登录、信息管理功能
- 📦 **商品管理** - 支持多规格商品、属性管理、品牌分类
- 🛍️ **购物功能** - 购物车、商品详情、商品搜索和过滤
- 🏷️ **分类管理** - 灵活的商品分类和品牌管理系统
- 📊 **访问统计** - 商品访问日志和数据分析
- 🌐 **国际化** - 支持多语言（中文/英文）
- ☁️ **云原生** - 支持 Nacos 服务发现和配置管理
- 🚀 **高性能** - Redis 缓存、连接池优化
- 📱 **RESTful API** - 标准化的 REST 接口设计

## 🛠️ 技术栈

### 后端框架
- **Java 17** - 编程语言
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **Spring Cloud Alibaba** - 阿里云微服务套件

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis 5.2.0** - 缓存数据库
- **MyBatis 3.0.2** - ORM 框架
- **PageHelper** - 分页插件

### 服务治理
- **Nacos 2.2.0** - 服务发现与配置管理
- **Jedis** - Redis 客户端

### 其他工具
- **JWT** - JSON Web Token 认证
- **Lombok** - 代码简化工具
- **Jackson** - JSON 处理
- **Aliyun OSS** - 对象存储服务
- **Maven** - 项目构建工具

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java     # 应用程序入口
│   ├── bean/                          # 数据传输对象
│   ├── config/                        # 配置类
│   ├── controller/                    # REST 控制器
│   ├── dto/                          # 数据传输对象
│   ├── entity/                       # 数据库实体
│   ├── enums/                        # 枚举类
│   ├── mapper/                       # MyBatis 映射器
│   ├── service/                      # 业务服务层
│   ├── security/                     # 安全相关
│   ├── utils/                        # 工具类
│   └── exceptions/                   # 异常处理
├── src/main/resources/
│   ├── mapper/                       # MyBatis XML 映射文件
│   ├── sql/                         # 数据库脚本
│   ├── i18n/                        # 国际化资源
│   └── application.yaml             # 应用配置
├── scripts/                         # 脚本文件
├── docker-compose.yaml              # Docker 编排文件
└── pom.xml                         # Maven 配置
```

## 🚀 快速开始

### 环境要求

- **JDK 17** 或更高版本
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis 6.0+**
- **Docker** (可选，用于容器化部署)

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

-- 导入数据库结构
source src/main/resources/sql/*.sql
```

#### 3. 配置文件
复制并修改配置文件：
```bash
cp src/main/resources/application-local.yaml.example src/main/resources/application-local.yaml
```

修改数据库和 Redis 连接配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password
```

#### 4. 启动应用
```bash
# 使用 Maven 启动
mvn clean spring-boot:run

# 或者编译后启动
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### Docker 容器化部署

#### 1. 启动基础服务
```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 仅启动 MySQL
docker-compose --profile without-redis up -d mysql
```

#### 2. 构建应用镜像
```bash
# 构建 JAR 包
mvn clean package -DskipTests

# 构建 Docker 镜像（需要先创建 Dockerfile）
docker build -t online-store:latest .
```

### 环境变量配置

可以通过环境变量覆盖默认配置：

```bash
export SPRING_PROFILES_ACTIVE=prod
export JWT_SECRET=your-secret-key
export MYSQL_URL=jdbc:mysql://mysql:3306/online_store
export REDIS_HOST=redis
export NACOS_ENABLED=true
```

## 📋 核心功能模块

### 🏪 商品管理 (Item Management)
- **商品信息管理** - 商品基本信息、详情、图片等
- **规格管理** - SKU 规格、属性值、库存管理
- **分类管理** - 多级商品分类，支持层级结构
- **品牌管理** - 品牌信息维护和关联
- **属性管理** - 商品属性定义和值设定

**主要接口：**
```
GET    /api/items              # 获取商品列表
POST   /api/items              # 创建商品
GET    /api/items/{id}         # 获取商品详情
PUT    /api/items/{id}         # 更新商品信息
DELETE /api/items/{id}         # 删除商品
```

### 👥 会员系统 (Member System)
- **用户注册登录** - 支持手机号/邮箱注册
- **用户信息管理** - 个人资料、头像、偏好设置
- **安全认证** - JWT Token 认证机制
- **权限控制** - 基于角色的访问控制

**主要接口：**
```
POST   /api/members/register   # 用户注册
POST   /api/members/login      # 用户登录
GET    /api/members/profile    # 获取用户信息
PUT    /api/members/profile    # 更新用户信息
```

### 🛒 购物功能 (Shopping Features)
- **商品浏览** - 支持分页、排序、筛选
- **商品搜索** - 基于关键词的商品搜索
- **购物车管理** - 添加、删除、修改购物车商品
- **商品详情** - 展示完整的商品信息和规格

**主要接口：**
```
GET    /api/items/search       # 商品搜索
GET    /api/items/detail/{id}  # 商品详情
POST   /api/cart/items         # 添加购物车
PUT    /api/cart/items         # 更新购物车
```

### 🏷️ 分类品牌 (Category & Brand)
- **分类管理** - 支持多级分类结构
- **品牌管理** - 品牌信息和Logo管理
- **关联管理** - 商品与分类、品牌的关联

**主要接口：**
```
GET    /api/categories         # 获取分类列表
GET    /api/brands             # 获取品牌列表
POST   /api/categories         # 创建分类
POST   /api/brands             # 创建品牌
```

### 📊 数据统计 (Analytics)
- **访问日志** - 记录商品访问情况
- **用户行为** - 跟踪用户浏览和操作行为
- **数据分析** - 提供基础的数据统计功能

## 🔌 API 文档

### 接口规范
- **Base URL**: `http://localhost:8080/api`
- **认证方式**: JWT Token (Header: `Authorization: Bearer <token>`)
- **请求格式**: JSON
- **响应格式**: JSON

### 通用响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2024-01-01T12:00:00Z"
}
```

### 分页响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [],
    "totalElements": 100,
    "totalPages": 10,
    "currentPage": 1,
    "pageSize": 10
  }
}
```

### 错误处理
```json
{
  "code": 400,
  "message": "参数错误",
  "error": "详细错误信息",
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## 📱 前端集成

系统提供完整的 RESTful API，可以与任何前端框架集成：

- **Vue.js** - 推荐使用 Vue 3 + TypeScript
- **React** - 支持 React 18 + TypeScript
- **Angular** - 支持 Angular 15+
- **微信小程序** - 支持原生小程序开发
- **移动端App** - 支持 React Native、Flutter 等

## 🔧 开发指南

### 代码规范
- 使用 **Java 17** 语法特性
- 遵循 **Google Java Style Guide**
- 使用 **Lombok** 简化代码
- 统一异常处理和错误码

### 数据库设计
```sql
-- 主要数据表
items              -- 商品主表
skus               -- 商品SKU表
categories         -- 商品分类表
brands             -- 品牌表
members            -- 会员表
attributes         -- 属性表
attribute_values   -- 属性值表
item_access_logs   -- 访问日志表
```

### 开发环境配置
1. **IDE 推荐**: IntelliJ IDEA 或 VS Code
2. **插件安装**:
   - Lombok Plugin
   - MyBatis Plugin
   - Spring Boot Helper
3. **代码格式化**: 导入项目 code style 配置

### 本地调试
```bash
# 启动调试模式
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# 或者在 IDE 中直接运行 OnlineStoreApplication.main()
```

### 单元测试
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=MemberServiceTest

# 生成测试覆盖率报告
mvn jacoco:report
```

## 🚀 生产环境部署

### 方式一：传统部署

#### 1. 环境准备
```bash
# 安装 JDK 17
sudo apt update
sudo apt install openjdk-17-jdk

# 安装 MySQL
sudo apt install mysql-server

# 安装 Redis
sudo apt install redis-server
```

#### 2. 应用部署
```bash
# 克隆代码
git clone <repository-url>
cd online_store

# 编译打包
mvn clean package -DskipTests

# 部署到服务器
scp target/online-store-1.0-SNAPSHOT.jar user@server:/opt/online-store/

# 启动服务
nohup java -jar /opt/online-store/online-store-1.0-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --server.port=8080 > /var/log/online-store.log 2>&1 &
```

### 方式二：Docker 部署

#### 1. 创建 Dockerfile
```dockerfile
# 创建 Dockerfile
cat > Dockerfile << 'EOF'
FROM openjdk:17-jre-slim

WORKDIR /app

# 复制 JAR 文件
COPY target/online-store-1.0-SNAPSHOT.jar app.jar

# 创建非 root 用户
RUN groupadd -r appuser && useradd -r -g appuser appuser
RUN chown -R appuser:appuser /app
USER appuser

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF
```

#### 2. 构建和运行
```bash
# 构建镜像
docker build -t online-store:latest .

# 运行容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e MYSQL_URL=jdbc:mysql://mysql:3306/online_store \
  -e REDIS_HOST=redis \
  online-store:latest
```

### 方式三：Docker Compose 完整部署

#### 1. 更新 docker-compose.yaml
```yaml
# 完整的 docker-compose.yaml 示例
version: "3.8"
services:
  mysql:
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: "123456"
      MYSQL_DATABASE: "online_store"
      TZ: 'Asia/Shanghai'
    volumes:
      - mysql_data:/var/lib/mysql
      - ./src/main/resources/sql:/docker-entrypoint-initdb.d
    networks:
      - online-store-network

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - online-store-network

  online-store:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      MYSQL_URL: jdbc:mysql://mysql:3306/online_store
      REDIS_HOST: redis
      JWT_SECRET: your-production-secret
    depends_on:
      - mysql
      - redis
    networks:
      - online-store-network
    restart: unless-stopped

volumes:
  mysql_data:
  redis_data:

networks:
  online-store-network:
    driver: bridge
```

#### 2. 一键部署
```bash
# 编译应用
mvn clean package -DskipTests

# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f online-store

# 停止服务
docker-compose down
```

### 方式四：Kubernetes 部署

创建 Kubernetes 部署配置：

```yaml
# k8s-deployment.yaml
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
        - name: MYSQL_URL
          value: "jdbc:mysql://mysql-service:3306/online_store"
        - name: REDIS_HOST
          value: "redis-service"
---
apiVersion: v1
kind: Service
metadata:
  name: online-store-service
spec:
  selector:
    app: online-store
  ports:
  - port: 80
    targetPort: 8080
  type: LoadBalancer
```

部署到 K8s：
```bash
kubectl apply -f k8s-deployment.yaml
```

## 🔍 监控和运维

### 健康检查
```bash
# 应用健康检查
curl http://localhost:8080/actuator/health

# 系统信息
curl http://localhost:8080/actuator/info

# 运行指标
curl http://localhost:8080/actuator/metrics
```

### 日志管理
```bash
# 查看应用日志
tail -f /var/log/online-store.log

# 使用 Docker 查看日志
docker logs -f online-store

# 查看错误日志
grep "ERROR" /var/log/online-store.log
```

### 性能调优
```bash
# JVM 参数优化
java -jar online-store.jar \
  -Xms2g -Xmx4g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+UseStringDeduplication
```

## 🔎 问题排查

### 常见问题

**1. 应用启动失败**
```bash
# 检查端口占用
netstat -tlnp | grep 8080

# 检查数据库连接
mysql -h localhost -u root -p -e "SELECT 1"

# 检查 Redis 连接
redis-cli ping
```

**2. 数据库连接问题**
- 棄查数据库服务是否启动
- 检查用户名和密码
- 检查防火墙设置
- 检查数据库是否存在

**3. Redis 连接问题**
- 检查 Redis 服务状态
- 检查连接池配置
- 检查内存使用情况

**4. JWT 认证问题**
- 检查 JWT_SECRET 环境变量
- 检查 token 过期时间
- 检查请求头格式

### 性能优化建议

1. **数据库优化**
   - 添加适当的索引
   - 优化查询语句
   - 使用连接池

2. **缓存优化**
   - 合理配置 Redis 过期时间
   - 使用缓存预热
   - 监控缓存命中率

3. **应用优化**
   - 使用异步处理
   - 优化分页查询
   - 启用 GZIP 压缩

## 🤝 贡献指南

欢迎各种形式的贡献！请遵循以下步骤：

### 参与方式
1. **代码贡献** - 提交 Pull Request
2. **Bug 报告** - 创建 Issue
3. **功能建议** - 提出 Feature Request
4. **文档完善** - 完善项目文档

### 开发流程
1. **Fork 项目**
```bash
git clone https://github.com/your-username/online_store.git
cd online_store
```

2. **创建功能分支**
```bash
git checkout -b feature/your-feature-name
```

3. **开发和测试**
```bash
# 编写代码
# 编写测试
mvn test
```

4. **提交代码**
```bash
git add .
git commit -m "feat: add your feature description"
git push origin feature/your-feature-name
```

5. **创建 Pull Request**
   - 描述修改内容
   - 关联相关 Issue
   - 等待代码审查

### 代码规范
- 遵循项目的代码风格
- 编写单元测试
- 添加适当的注释
- 更新相关文档

### Commit 消息规范
使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
feat: 新功能
fix: bug修复
docs: 文档更新
style: 代码格式修改
refactor: 代码重构
test: 测试相关
chore: 构建过程或辅助工具的变动
```

## 📄 许可证

本项目采用 [MIT License](LICENSE) 许可证。

```
MIT License

Copyright (c) 2024 Online Store Project

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 📞 联系信息

- **项目维护者**: Online Store Team
- **邮箱**: support@onlinestore.com
- **项目首页**: [GitHub Repository](https://github.com/your-org/online_store)
- **问题反馈**: [GitHub Issues](https://github.com/your-org/online_store/issues)
- **讨论区**: [GitHub Discussions](https://github.com/your-org/online_store/discussions)

## 🎆 更新日志

### v1.0.0 (2024-01-01)
- ✨ 初始版本发布
- ✨ 完整的商品管理功能
- ✨ 会员系统和 JWT 认证
- ✨ 购物车和订单功能
- ✨ 分类和品牌管理
- ✨ 多语言支持
- ✨ Docker 容器化支持

---

❤️ **感谢使用 Online Store！** 如果这个项目对您有帮助，请给我们一个 ⭐️ Star！ 