# Online Store - 在线商店系统

一个基于Spring Boot + MyBatis的现代化在线商店系统，支持商品管理、用户管理、订单处理等核心电商功能。

## ✨ 核心功能

- 🔐 **用户管理** - 用户注册、登录、JWT认证
- 🛍️ **商品管理** - 商品CRUD、商品详情、商品搜索
- 🏷️ **分类管理** - 商品分类层级管理
- 🏢 **品牌管理** - 品牌信息管理和展示
- 📊 **属性管理** - 商品属性定义和管理
- 📈 **访问统计** - 商品访问日志记录
- 🔄 **缓存支持** - Redis缓存提升性能
- 🔧 **配置中心** - 支持Nacos配置管理
- 🐳 **容器化** - Docker支持

## 🛠️ 技术栈

### 后端框架
- **JDK 17** - Java运行环境
- **Spring Boot 3.4.3** - 主框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全认证
- **Spring Data Redis** - Redis集成

### 数据层
- **MyBatis 3.0.3** - ORM框架
- **PageHelper 2.1.0** - 分页插件
- **MySQL 8.2.0** - 关系型数据库
- **Redis (Jedis 5.2.0)** - 缓存数据库

### 工具库
- **JWT 0.11.5** - Token认证
- **Lombok 1.18.36** - 代码简化
- **Apache Commons** - 工具类库
- **Jackson** - JSON处理
- **Aliyun OSS 3.18.1** - 对象存储

### 服务治理
- **Nacos 2.2.0** - 配置中心和服务发现
- **Spring Cloud Alibaba** - 阿里云生态集成

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 启动类
│   │   │   ├── controller/                    # REST API控制器
│   │   │   │   ├── MemberController.java      # 用户管理
│   │   │   │   ├── ItemController.java        # 商品管理
│   │   │   │   ├── BrandController.java       # 品牌管理
│   │   │   │   ├── CategoryController.java    # 分类管理
│   │   │   │   └── AttributeController.java   # 属性管理
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   │   ├── impl/                      # 服务实现
│   │   │   │   ├── MemberService.java         # 用户服务
│   │   │   │   ├── ItemService.java           # 商品服务
│   │   │   │   └── CategoryService.java       # 分类服务
│   │   │   ├── mapper/                        # 数据访问层
│   │   │   ├── entity/                        # 数据实体
│   │   │   ├── bean/                          # 业务对象
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── config/                        # 配置类
│   │   │   └── exceptions/                    # 异常处理
│   │   └── resources/
│   │       ├── application.yaml               # 主配置文件
│   │       ├── application-local.yaml         # 本地环境配置
│   │       ├── bootstrap.yaml                 # 引导配置
│   │       ├── mapper/                        # MyBatis映射文件
│   │       ├── sql/                           # 数据库脚本
│   │       └── i18n/                          # 国际化资源
│   └── test/                                  # 测试代码
├── scripts/                                   # 部署脚本
├── docker-compose.yaml                        # Docker编排
├── Dockerfile                                 # Docker镜像构建
└── pom.xml                                    # Maven配置
```

## 🚀 快速开始

### 环境要求

- **JDK 17+** - Java开发环境
- **Maven 3.6+** - 构建工具
- **MySQL 8.0+** - 数据库
- **Redis 6.0+** - 缓存服务
- **Docker** (可选) - 容器化部署

### 方式一：本地开发运行

1. **克隆项目**
```bash
git clone <repository-url>
cd online-store
```

2. **启动基础服务**
```bash
# 使用Docker Compose启动MySQL和Redis
docker-compose up -d mysql redis
```

3. **创建数据库**
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

4. **执行数据库脚本**
```bash
# 在MySQL中执行src/main/resources/sql/目录下的SQL脚本
```

5. **配置环境变量**
```bash
# 设置必需的环境变量
export JWT_SECRET=your-jwt-secret-key
export MYSQL_PASSWORD=123456
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
```

6. **启动应用**
```bash
# 方式1：使用Maven
mvn spring-boot:run

# 方式2：编译后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 方式二：Docker部署

```bash
# 构建镜像
docker build -t online-store .

# 启动所有服务
docker-compose --profile all up -d
```

## 📋 API文档

应用启动后，可通过以下地址访问：

- **应用首页**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **应用信息**: http://localhost:8080/actuator/info

### 主要API端点

| 模块 | 端点 | 描述 |
|------|------|------|
| 用户管理 | `POST /api/v1/members/registry` | 用户注册 |
| 用户管理 | `POST /api/v1/members/login` | 用户登录 |
| 商品管理 | `GET /api/v1/items/{itemId}` | 获取商品详情 |
| 品牌管理 | `GET /api/v1/brands` | 获取品牌列表 |
| 分类管理 | `GET /api/v1/categories/{categoryId}` | 获取分类信息 |
| 属性管理 | `POST /api/v1/attributes` | 创建属性 |
| 属性管理 | `GET /api/v1/attributes/{attributeId}` | 获取属性详情 |

## ⚙️ 配置说明

### 数据库配置

修改 `src/main/resources/application-local.yaml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: ${MYSQL_PASSWORD:123456}
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
```

### JWT配置

```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400  # 24小时
```

## 🔧 开发指南

### 代码规范

- 使用 **Lombok** 简化实体类代码
- 遵循 **RESTful API** 设计规范
- 统一使用 **@Valid** 进行参数校验
- 使用 **@Transactional** 管理事务
- 统一异常处理和响应格式

### 数据库设计

- 统一使用 `created_at` 和 `updated_at` 字段
- 主键使用自增 `BIGINT` 类型
- 字符编码统一使用 `utf8mb4`
- 合理建立索引提升查询性能

### 缓存策略

- 商品详情使用Redis缓存，TTL为30分钟
- 分层缓存架构，支持缓存穿透保护
- 使用JSON序列化存储复杂对象

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn integration-test

# 生成测试报告
mvn test jacoco:report
```

## 📦 构建部署

```bash
# 本地构建
mvn clean package

# 跳过测试构建
mvn clean package -DskipTests

# 构建Docker镜像
docker build -t online-store:latest .
```

## 🔍 监控运维

- **应用监控**: Spring Boot Actuator
- **日志管理**: 支持分级日志输出
- **配置管理**: 支持Nacos动态配置
- **服务发现**: 支持Nacos服务注册发现

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 项目Issues: [GitHub Issues](../issues)
- 邮箱: [your-email@example.com](mailto:your-email@example.com)

---

⭐ **如果这个项目对你有帮助，请给个Star支持一下！** 