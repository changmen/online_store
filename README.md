# 🛒 Online Store - 在线商店系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-orange.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)
[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)

这是一个基于Spring Cloud微服务架构的现代化在线商店系统，提供完整的电商功能，包括商品管理、用户管理、订单处理等核心业务模块。

## ✨ 功能特性

### 🔐 用户与权限管理
- JWT 用户认证与授权
- Spring Security 安全框架集成
- 用户注册、登录、个人信息管理

### 📦 商品管理系统
- **商品管理**: 商品的增删改查、状态管理
- **品牌管理**: 品牌信息维护与管理
- **类别管理**: 商品分类的层级管理
- **属性管理**: 商品属性与属性值的动态配置
- **SKU管理**: 商品规格库存单位管理
- **商品详情**: 丰富的商品详情展示

### 🔍 高级功能
- 商品访问日志记录
- 分页查询与条件筛选
- 国际化支持（中英文）
- 阿里云OSS文件存储集成
- Redis 缓存优化
- MyBatis 数据持久化

### 🏗️ 技术架构
- 微服务架构设计
- RESTful API 设计
- 统一异常处理
- 参数校验与数据绑定
- 全局响应格式统一

## 🛠️ 技术栈

### 核心框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: 安全认证
- **Spring Cloud Alibaba**: 2022.0.0.0

### 数据存储
- **MySQL**: 8.2.0 - 主数据库
- **Redis**: Latest - 缓存与会话存储
- **MyBatis**: 3.0.3 - ORM框架
- **PageHelper**: 2.1.0 - 分页插件

### 工具库
- **JWT**: 0.11.5 - 身份认证
- **Lombok**: 1.18.36 - 代码简化
- **Commons Lang3**: 3.17.0 - 工具类
- **Jackson**: JSON处理
- **Aliyun OSS**: 3.18.1 - 文件存储

### 服务治理
- **Nacos**: 服务注册与发现、配置管理
- **Actuator**: 应用监控与管理

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java     # 应用启动类
│   ├── bean/                          # 业务实体类
│   │   ├── Item.java                  # 商品实体
│   │   ├── Member.java                # 会员实体
│   │   ├── Brand.java                 # 品牌实体
│   │   └── ...
│   ├── controller/                    # REST控制器
│   │   ├── ItemController.java        # 商品控制器
│   │   ├── MemberController.java      # 会员控制器
│   │   └── ...
│   ├── service/                       # 业务服务层
│   │   ├── ItemService.java          # 商品服务
│   │   ├── MemberService.java         # 会员服务
│   │   └── impl/                      # 服务实现
│   ├── mapper/                        # MyBatis映射器
│   ├── entity/                        # 数据库实体
│   ├── dto/                          # 数据传输对象
│   ├── config/                       # 配置类
│   ├── security/                     # 安全相关
│   ├── utils/                        # 工具类
│   ├── enums/                        # 枚举类
│   ├── exceptions/                   # 异常处理
│   └── handler/                      # 全局处理器
├── src/main/resources/
│   ├── application.yaml              # 主配置文件
│   ├── application-local.yaml        # 本地环境配置
│   ├── bootstrap.yaml                # 引导配置
│   ├── mapper/                       # MyBatis映射文件
│   ├── sql/                         # 数据库脚本
│   └── i18n/                        # 国际化资源
├── scripts/                          # 脚本工具
├── docker-compose.yaml               # Docker编排文件
├── Dockerfile                        # Docker镜像构建
└── pom.xml                          # Maven项目配置
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **Docker**: (可选) 用于容器化部署

### 🐳 Docker 快速启动 (推荐)

```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 等待服务启动后，运行应用
mvn spring-boot:run
```

### 🔧 手动安装

#### 1. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE online_store;

-- 执行初始化脚本 (位于 src/main/resources/sql/ 目录)
```

#### 2. 配置文件修改

复制并修改配置文件：

```bash
# 创建本地配置文件
cp src/main/resources/application-local.yaml.example src/main/resources/application-local.yaml
```

修改 `application-local.yaml` 中的数据库和Redis连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password

jwt:
  secret: your_jwt_secret_key_here
```

#### 3. 运行应用

```bash
# 编译项目
mvn clean compile

# 运行应用 (需要添加JVM参数)
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"

# 或直接运行JAR包
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

应用启动后，访问 http://localhost:8080

## 📚 API文档

### 认证相关

#### 用户注册
```http
POST /api/members/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "email": "user@example.com",
  "phone": "13800138000"
}
```

#### 用户登录
```http
POST /api/members/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

### 商品管理

#### 获取商品列表
```http
GET /api/items?page=1&size=10&keyword=手机&categoryId=1
Authorization: Bearer <jwt_token>
```

#### 获取商品详情
```http
GET /api/items/{itemId}/detail
Authorization: Bearer <jwt_token>
```

#### 创建商品
```http
POST /api/items
Content-Type: application/json
Authorization: Bearer <jwt_token>

{
  "name": "商品名称",
  "description": "商品描述",
  "price": 99.99,
  "categoryId": 1,
  "brandId": 1,
  "attributes": [
    {
      "attributeId": 1,
      "valueId": 1
    }
  ]
}
```

### 品牌管理

#### 获取品牌列表
```http
GET /api/brands?page=1&size=10
Authorization: Bearer <jwt_token>
```

### 类别管理

#### 获取类别树
```http
GET /api/categories/tree
Authorization: Bearer <jwt_token>
```

## 🐋 Docker部署

### 构建镜像

```bash
# 构建应用镜像
docker build -t online-store:latest .
```

### 使用 Docker Compose

```bash
# 启动完整环境 (MySQL + Redis + 应用)
docker-compose --profile all up -d

# 仅启动数据库服务
docker-compose --profile without-redis up -d

# 查看服务状态
docker-compose ps

# 停止服务
docker-compose down
```

## ⚙️ 配置说明

### 环境变量

| 变量名 | 描述 | 默认值 |
|--------|------|--------|
| `SPRING_PROFILES_ACTIVE` | 活动配置文件 | `local` |
| `JWT_SECRET` | JWT密钥 | **必须设置** |
| `ADMIN_USERNAME` | 管理员用户名 | `admin` |
| `ADMIN_PASSWORD` | 管理员密码 | `admin123` |
| `NACOS_ENABLED` | 是否启用Nacos | `false` |

### 配置文件说明

- `application.yaml`: 主配置文件
- `application-local.yaml`: 本地开发环境配置
- `bootstrap.yaml`: 引导配置，用于Nacos配置中心

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 生成测试报告
mvn surefire-report:report
```

## 📊 监控与运维

应用集成了Spring Boot Actuator，提供丰富的监控端点：

- **健康检查**: `GET http://localhost:8080/actuator/health`
- **应用信息**: `GET http://localhost:8080/actuator/info`
- **指标监控**: `GET http://localhost:8080/actuator/metrics`
- **环境信息**: `GET http://localhost:8080/actuator/env`

## 🔧 开发指南

### 代码规范

- 遵循阿里巴巴Java开发手册
- 使用Lombok减少样板代码
- 统一的异常处理机制
- RESTful API设计原则

### 数据库规范

- 表名使用下划线命名法
- 字段名使用下划线命名法
- 必须字段：`id`, `create_time`, `update_time`
- 软删除字段：`deleted`

### Git提交规范

```
feat: 新功能
fix: Bug修复
docs: 文档更新
style: 代码格式修改
refactor: 代码重构
test: 测试用例
chore: 构建过程或辅助工具的变动
```

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目使用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

- 项目地址: [GitHub Repository](https://github.com/your-username/online-store)
- 问题反馈: [Issues](https://github.com/your-username/online-store/issues)
- 邮箱: your-email@example.com

## 🙏 致谢

感谢以下开源项目的支持：

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Cloud](https://spring.io/projects/spring-cloud)
- [MyBatis](https://mybatis.org/)
- [MySQL](https://www.mysql.com/)
- [Redis](https://redis.io/)

---

⭐ 如果这个项目对你有帮助，请给我们一个Star！ 