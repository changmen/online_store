# Online Store - 在线商店系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

一个基于 Spring Boot 和 Spring Cloud 的现代化在线商店系统，提供完整的电商核心功能，包括商品管理、用户管理、品牌管理、分类管理等模块。

## ✨ 功能特性

### 核心业务模块
- 🛍️ **商品管理** - 完整的商品信息管理，支持多规格SKU
- 👥 **用户管理** - 用户注册、登录、JWT身份验证
- 🏷️ **品牌管理** - 品牌信息的增删改查
- 📂 **分类管理** - 商品分类体系管理
- 🏪 **SKU管理** - 库存单位管理，支持属性值组合
- 📊 **属性管理** - 商品属性和属性值管理
- 📈 **访问日志** - 商品访问统计和分析

### 技术特性
- 🔐 **Spring Security** - 完整的安全框架集成
- 🎯 **JWT认证** - 无状态身份验证
- 🌐 **RESTful API** - 标准化API设计
- 📄 **分页查询** - 高效的数据分页处理
- 🔄 **参数校验** - 完整的请求参数验证
- 🐳 **Docker支持** - 容器化部署
- ☁️ **Nacos集成** - 服务注册与配置管理(可选)

## 🛠️ 技术栈

### 后端框架
- **JDK 17** - Java运行环境
- **Spring Boot 3.4.3** - 主框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **MyBatis 3.0.3** - ORM框架
- **PageHelper 2.1.0** - 分页插件

### 数据存储
- **MySQL 8.2.0** - 主数据库
- **Redis** - 缓存和会话存储
- **Jedis 5.2.0** - Redis客户端

### 工具库
- **JJWT 0.11.5** - JWT处理
- **Lombok 1.18.36** - 代码简化
- **Apache Commons Lang3** - 工具类库
- **Jackson** - JSON处理

### 服务治理(可选)
- **Nacos 2.2.0** - 服务注册与配置中心
- **Spring Cloud Alibaba** - 阿里云微服务套件

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # 主启动类
│   │   │   ├── controller/                     # REST控制器
│   │   │   │   ├── AttributeController.java    # 属性管理
│   │   │   │   ├── BrandController.java        # 品牌管理
│   │   │   │   ├── CategoryController.java     # 分类管理
│   │   │   │   ├── ItemController.java         # 商品管理
│   │   │   │   ├── ItemDetailController.java   # 商品详情
│   │   │   │   ├── MemberController.java       # 用户管理
│   │   │   │   └── SkuController.java          # SKU管理
│   │   │   ├── service/                        # 业务服务层
│   │   │   │   ├── impl/                       # 服务实现
│   │   │   │   ├── AttributeService.java
│   │   │   │   ├── BrandService.java
│   │   │   │   ├── CategoryService.java
│   │   │   │   ├── ItemService.java
│   │   │   │   ├── MemberService.java
│   │   │   │   └── SkuService.java
│   │   │   ├── mapper/                         # MyBatis映射器
│   │   │   ├── entity/                         # 数据实体
│   │   │   ├── bean/                           # 业务模型
│   │   │   ├── dto/                            # 数据传输对象
│   │   │   ├── enums/                          # 枚举类
│   │   │   ├── exceptions/                     # 异常处理
│   │   │   └── utils/                          # 工具类
│   │   └── resources/
│   │       ├── application.yaml                # 主配置文件
│   │       ├── application-local.yaml          # 本地环境配置
│   │       ├── bootstrap.yaml                  # 引导配置
│   │       ├── mapper/                         # MyBatis XML映射文件
│   │       ├── sql/                            # 数据库初始化脚本
│   │       └── i18n/                           # 国际化资源
│   └── test/                                   # 测试代码
├── scripts/                                    # 辅助脚本
├── docker-compose.yaml                         # Docker编排文件
├── Dockerfile                                  # Docker镜像构建文件
└── pom.xml                                     # Maven配置文件
```

## 🚀 快速开始

### 环境要求

- **JDK 17** 或更高版本
- **Maven 3.6+** 构建工具
- **MySQL 8.0+** 数据库
- **Redis 6.0+** 缓存服务

### 方式一：本地开发环境

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 启动数据库服务
```bash
# 使用Docker快速启动MySQL和Redis
docker-compose --profile all up -d

# 或手动启动MySQL和Redis服务
```

#### 3. 初始化数据库
```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行初始化脚本
source src/main/resources/sql/*.sql
```

#### 4. 配置环境变量
```bash
# 设置必要的环境变量
export MYSQL_PASSWORD=123456
export JWT_SECRET=your-jwt-secret-key
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
```

#### 5. 启动应用
```bash
# 使用Maven启动
mvn spring-boot:run

# 或编译后运行
mvn clean package
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 方式二：Docker部署

#### 1. 构建镜像
```bash
docker build -t online-store:latest .
```

#### 2. 启动完整环境
```bash
docker-compose --profile all up -d
```

### 验证部署

应用启动后，可以通过以下方式验证：

```bash
# 检查应用健康状态
curl http://localhost:8080/actuator/health

# 测试API接口
curl http://localhost:8080/api/v1/brands
```

## 📋 API 文档

### 认证相关
- `POST /api/v1/members/registry` - 用户注册

### 商品管理
- `GET /api/v1/items/{itemId}` - 获取商品详情
- `GET /api/v1/items/{itemId}/detail` - 获取商品详细信息

### 品牌管理
- `GET /api/v1/brands` - 获取品牌列表
- `GET /api/v1/brands/{brandId}` - 获取品牌详情
- `POST /api/v1/brands` - 创建品牌
- `PUT /api/v1/brands/{brandId}` - 更新品牌
- `DELETE /api/v1/brands/{brandId}` - 删除品牌

### 分类管理
- `GET /api/v1/categories/{categoryId}` - 获取分类详情

### 属性管理
- `GET /api/v1/attributes/{attributeId}` - 获取属性详情
- `POST /api/v1/attributes` - 创建属性
- `PUT /api/v1/attributes/{attributeId}` - 更新属性

## ⚙️ 配置说明

### 数据库配置
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
      database: 0
```

### JWT配置
```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400  # 24小时
```

### Nacos配置(可选)
```yaml
spring:
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
mvn integration-test

# 生成测试覆盖率报告
mvn jacoco:report
```

## 📈 监控

应用集成了Spring Boot Actuator，提供以下监控端点：

- `/actuator/health` - 健康检查
- `/actuator/info` - 应用信息
- `/actuator/metrics` - 应用指标
- `/actuator/env` - 环境变量

## 🔧 开发指南

### 代码规范
- 使用Lombok减少样板代码
- 统一的异常处理机制
- RESTful API设计规范
- 完整的参数校验

### 数据库设计
- 统一的主键策略(AUTO_INCREMENT)
- 标准的时间字段(created_at, updated_at)
- 软删除支持
- 适当的索引设计

## 🚀 部署

### 生产环境配置
```bash
# 设置生产环境变量
export SPRING_PROFILES_ACTIVE=prod
export MYSQL_HOST=your-mysql-host
export REDIS_HOST=your-redis-host
export JWT_SECRET=your-production-jwt-secret
```

### 性能优化建议
- 配置连接池参数
- 启用Redis缓存
- 配置JVM参数
- 使用CDN加速静态资源

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

该项目使用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，请提交 Issue 或联系项目维护者。

---

**Note**: 这是一个学习和演示项目，包含了现代Spring Boot应用的最佳实践。 