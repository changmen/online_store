# Online Store 在线商店系统

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.2-blue)
![Redis](https://img.shields.io/badge/Redis-Latest-red)

基于Spring Cloud微服务架构的现代化在线商店系统，支持商品管理、用户认证、品牌管理等核心电商功能。

</div>

## ✨ 核心特性

- 🔐 **JWT身份认证** - 基于Spring Security的无状态认证
- 🛍️ **商品管理** - 完整的商品、品牌、分类管理体系
- 👥 **用户系统** - 用户注册、登录、权限管理
- 📊 **访问统计** - 商品访问日志记录与分析
- 🔄 **分布式架构** - 支持Nacos服务发现与配置管理
- 📱 **RESTful API** - 标准化的API接口设计
- 🏷️ **商品属性** - 灵活的商品属性管理系统
- 🔍 **分页查询** - 基于PageHelper的高效分页

## 🏗️ 技术栈

### 后端技术
- **Java 17** - 编程语言
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **MyBatis 3.0.3** - 持久层框架
- **PageHelper 2.1.0** - 分页插件

### 数据存储
- **MySQL 8.2** - 关系型数据库
- **Redis** - 缓存数据库
- **Jedis 5.2.0** - Redis Java客户端

### 服务治理
- **Nacos 2.2.0** - 服务发现与配置管理
- **Spring Cloud Alibaba 2022.0.0.0** - 阿里云微服务组件

### 工具库
- **JWT 0.11.5** - JSON Web Token
- **Lombok 1.18.36** - 代码生成工具
- **Apache Commons** - 通用工具库
- **Aliyun OSS 3.18.1** - 阿里云对象存储

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java    # 应用启动类
│   ├── bean/                          # 实体类
│   ├── config/                        # 配置类
│   ├── controller/                    # REST控制器
│   │   ├── BrandController.java       # 品牌管理
│   │   ├── CategoryController.java    # 分类管理
│   │   ├── ItemController.java        # 商品管理
│   │   ├── MemberController.java      # 用户管理
│   │   └── AttributeController.java   # 属性管理
│   ├── dto/                          # 数据传输对象
│   ├── entity/                       # 数据实体
│   ├── enums/                        # 枚举类型
│   ├── mapper/                       # MyBatis映射器
│   ├── security/                     # 安全配置
│   ├── service/                      # 业务逻辑层
│   └── utils/                        # 工具类
├── src/main/resources/
│   ├── application.yaml              # 主配置文件
│   ├── application-local.yaml        # 本地环境配置
│   ├── bootstrap.yaml               # 启动配置
│   ├── mapper/                      # MyBatis XML映射文件
│   └── i18n/                        # 国际化资源
├── scripts/                         # 脚本工具
│   └── main.py                      # 测试数据生成工具
├── docker-compose.yaml              # Docker编排文件
├── Dockerfile                       # Docker镜像构建文件
└── pom.xml                         # Maven依赖配置
```

## 🚀 快速开始

### 环境要求

- **JDK 17** 或更高版本
- **Maven 3.6+** 构建工具
- **MySQL 8.0+** 数据库
- **Redis 6.0+** 缓存服务
- **Docker** (可选，用于容器化部署)

### 方式一：本地开发环境

1. **克隆项目**
```bash
git clone <repository-url>
cd online_store
```

2. **数据库初始化**
```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE online_store;
```

3. **环境变量配置**
```bash
# 设置必要的环境变量
export JWT_SECRET="your-secret-key-here"
export MYSQL_PASSWORD="123456"
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="admin123"
```

4. **启动应用**
```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run

# 或者添加JVM参数
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

### 方式二：Docker Compose部署

1. **启动所有服务**
```bash
# 启动MySQL和Redis
docker-compose --profile all up -d

# 或仅启动MySQL
docker-compose --profile without-redis up -d
```

2. **构建并运行应用**
```bash
# 构建Docker镜像
docker build -t online-store .

# 运行应用容器
docker run -p 8080:8080 --network host online-store
```

## 📋 API文档

应用启动后，可通过以下端点访问：

### 用户管理 API
- `POST /api/v1/members/registry` - 用户注册
- `POST /api/v1/members/login` - 用户登录

### 商品管理 API  
- `GET /api/v1/items/{itemId}` - 获取商品详情
- `GET /api/v1/items/{itemId}/detail` - 获取商品详细信息

### 品牌管理 API
- `GET /api/v1/brands` - 品牌列表（支持分页）
- `GET /api/v1/brands/{brandId}` - 获取品牌详情
- `POST /api/v1/brands` - 创建品牌
- `PUT /api/v1/brands/{brandId}` - 更新品牌
- `DELETE /api/v1/brands/{brandId}` - 删除品牌

### 分类管理 API
- `GET /api/v1/categories/{categoryId}` - 获取分类详情

### 属性管理 API
- `POST /api/v1/attributes` - 创建属性
- `GET /api/v1/attributes/{attributeId}` - 获取属性详情
- `PUT /api/v1/attributes/{attributeId}` - 更新属性

### 系统监控
- `GET /actuator/health` - 健康检查
- `GET /actuator/info` - 应用信息

## ⚙️ 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
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

### Nacos配置（可选）
```yaml
spring:
  cloud:
    nacos:
      discovery:
        enabled: ${NACOS_ENABLED:false}
        server-addr: ${NACOS_SERVER_ADDR:localhost:8848}
```

## 🔧 开发工具

### 测试数据生成
项目提供了Python脚本用于生成测试数据：

```bash
cd scripts
python main.py
```

### 代码生成
项目使用Lombok减少样板代码，主要注解：
- `@Data` - 自动生成getter/setter
- `@Builder` - 建造者模式
- `@NoArgsConstructor/@AllArgsConstructor` - 构造函数
- `@Slf4j` - 日志记录

## 🚦 运行状态检查

启动成功后，访问以下URL验证服务状态：

```bash
# 健康检查
curl http://localhost:8080/actuator/health

# 应用信息
curl http://localhost:8080/actuator/info
```

## 🐛 常见问题

### 1. JVM启动参数问题
如果遇到模块访问问题，添加以下JVM参数：
```bash
--add-opens java.base/java.lang=ALL-UNNAMED
```

### 2. 数据库连接问题
- 确保MySQL服务已启动
- 检查数据库用户名密码是否正确
- 确认数据库`online_store`已创建

### 3. Redis连接问题
- 确保Redis服务已启动
- 检查Redis连接配置

### 4. JWT密钥未设置
确保设置了`JWT_SECRET`环境变量：
```bash
export JWT_SECRET="your-very-secure-secret-key"
```

## 📈 性能监控

项目集成了Spring Boot Actuator，提供以下监控端点：
- `/actuator/metrics` - 应用指标
- `/actuator/health` - 健康状态
- `/actuator/info` - 应用信息

## 🔐 安全说明

- 默认用户名：`admin`，密码：`admin123`
- JWT Token有效期：24小时
- 密码使用BCrypt加密存储
- 支持基于角色的访问控制

## 📝 开发计划

- [ ] 订单管理系统
- [ ] 购物车功能
- [ ] 支付集成
- [ ] 库存管理
- [ ] 优惠券系统
- [ ] 评价系统
- [ ] 消息推送
- [ ] 数据统计分析

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

---

<div align="center">

**[⬆ 回到顶部](#online-store-在线商店系统)**

</div> 