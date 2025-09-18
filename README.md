# 🛍️ Online Store - 在线商店系统

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MyBatis](https://img.shields.io/badge/MyBatis-3.0.2-red.svg)](https://mybatis.org/mybatis-3/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

一个功能完整的现代化在线商店系统，基于Spring Boot 3.x和Spring Cloud微服务架构构建，提供商品管理、用户管理、订单处理、评论系统等完整的电商功能。

## ✨ 主要特性

- 🏗️ **现代化架构**: 基于Spring Boot 3.x + Spring Cloud 2024 构建
- 🔐 **安全认证**: 完整的用户认证和权限管理系统
- 📦 **商品管理**: 支持商品分类、属性、库存管理
- 🛒 **购物车**: 完整的购物车和订单处理流程
- 💬 **评论系统**: 支持商品评论和评分功能
- 📊 **统计分析**: 商品访问统计、用户行为分析
- 🔍 **搜索功能**: 支持商品搜索和筛选
- 🏃‍♂️ **高性能**: Redis缓存优化，支持高并发访问
- 🐳 **容器化**: 提供Docker Compose一键部署
- 📈 **监控**: 集成Actuator监控和健康检查
- 🌐 **国际化**: 支持中英文国际化
- ☁️ **微服务**: 集成Nacos服务发现和配置中心

## 🛠️ 技术栈

### 后端技术
- **Java 17** - 主要开发语言
- **Spring Boot 3.4.3** - 主框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Cloud Alibaba 2022.0.0.0** - 阿里云微服务套件
- **MyBatis 3.0.2** - ORM框架
- **MySQL 8.2.0** - 主数据库
- **Redis 5.2.0** - 缓存和会话存储
- **Nacos 2.2.0** - 服务发现和配置中心

### 中间件和工具
- **Jedis** - Redis客户端
- **HikariCP** - 数据库连接池
- **Jackson** - JSON序列化
- **Validation** - 参数校验
- **AOP** - 面向切面编程
- **Actuator** - 应用监控
- **阿里云OSS** - 对象存储服务
- **Apache HttpClient** - HTTP客户端

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java          # 应用入口
│   ├── actuator/                           # 监控端点
│   │   ├── ConfigInfoEndpoint.java         # 配置信息端点
│   │   ├── DatabaseInfoEndpoint.java       # 数据库信息端点
│   │   └── SystemInfoEndpoint.java         # 系统信息端点
│   ├── annotation/                         # 自定义注解
│   │   ├── RequireAdmin.java              # 管理员权限注解
│   │   └── ValidateParams.java            # 参数校验注解
│   ├── aspect/                            # 切面
│   │   ├── AdminAuthAspect.java           # 管理员权限切面
│   │   └── ValidationAspect.java          # 参数校验切面
│   ├── bean/                              # 业务实体
│   │   ├── Category.java                  # 商品分类
│   │   ├── Item.java                      # 商品
│   │   ├── User.java                      # 用户
│   │   └── ...
│   ├── cache/                             # 缓存管理
│   │   ├── CacheManager.java              # 缓存管理器接口
│   │   ├── LocalCacheManager.java         # 本地缓存实现
│   │   └── RedisCacheManager.java         # Redis缓存实现
│   ├── config/                            # 配置类
│   │   ├── CacheConfig.java               # 缓存配置
│   │   ├── RedisConfig.java               # Redis配置
│   │   ├── MyBatisConfig.java             # MyBatis配置
│   │   └── ...
│   ├── controller/                        # 控制器
│   │   ├── AuthController.java            # 认证控制器
│   │   ├── ItemController.java            # 商品控制器
│   │   ├── OrderController.java           # 订单控制器
│   │   ├── UserController.java            # 用户控制器
│   │   └── ...
│   ├── dto/                               # 数据传输对象
│   ├── entity/                            # 数据库实体
│   ├── enums/                             # 枚举类
│   ├── exception/                         # 异常类
│   ├── handler/                           # 异常处理器
│   ├── hook/                              # 钩子系统
│   ├── interceptor/                       # 拦截器
│   ├── mapper/                            # MyBatis映射器
│   ├── service/                           # 业务服务
│   ├── util/                              # 工具类
│   └── validator/                         # 自定义校验器
├── src/main/resources/
│   ├── application.yml                     # 主配置文件
│   ├── application-local.yml               # 本地环境配置
│   ├── bootstrap.yml                       # 引导配置
│   ├── db/                                # 数据库脚本
│   │   ├── schema.sql                     # 数据库表结构
│   │   └── migration/                     # 数据库迁移脚本
│   ├── i18n/                              # 国际化资源
│   └── mapper/                            # MyBatis XML映射文件
├── src/test/                              # 测试代码
├── scripts/                               # 脚本工具
├── docker-compose.yaml                    # Docker编排文件
├── pom.xml                                # Maven配置
└── README.md                              # 项目说明
```

## 🚀 快速开始

### 环境要求

- **JDK 17+** (推荐使用OpenJDK 17)
- **Maven 3.8+**
- **MySQL 8.0+**
- **Redis 6.0+**
- **Docker & Docker Compose** (可选，用于容器化部署)

### 🐳 Docker 快速部署（推荐）

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd online_store
   ```

2. **启动基础服务**
   ```bash
   # 启动MySQL和Redis
   docker-compose --profile all up -d
   ```

3. **初始化数据库**
   ```bash
   # 连接MySQL并创建数据库
   mysql -h localhost -P 3306 -u root -p123456 -e "CREATE DATABASE IF NOT EXISTS online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
   
   # 导入数据库结构
   mysql -h localhost -P 3306 -u root -p123456 online_store < src/main/resources/db/schema.sql
   ```

4. **运行应用**
   ```bash
   # 编译并运行
   mvn clean compile
   mvn spring-boot:run
   ```

### 🖥️ 手动部署

1. **安装依赖服务**
   ```bash
   # Ubuntu/Debian
   sudo apt update
   sudo apt install mysql-server redis-server
   
   # CentOS/RHEL
   sudo yum install mysql-server redis
   ```

2. **配置MySQL**
   ```sql
   # 创建数据库
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # 创建用户（可选）
   CREATE USER 'store_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON online_store.* TO 'store_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **配置Redis**
   ```bash
   # 启动Redis服务
   sudo systemctl start redis
   sudo systemctl enable redis
   ```

4. **配置应用**
   ```bash
   # 复制配置文件
   cp src/main/resources/application.yml src/main/resources/application-local.yml
   
   # 编辑配置文件，修改数据库和Redis连接信息
   vim src/main/resources/application-local.yml
   ```

5. **运行应用**
   ```bash
   # 添加JVM参数并运行
   export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"
   mvn spring-boot:run
   ```

## 🔧 配置说明

### 环境配置

应用支持多环境配置，通过 `SPRING_PROFILES_ACTIVE` 环境变量控制：

- `local` - 本地开发环境（默认）
- `dev` - 开发环境
- `test` - 测试环境
- `prod` - 生产环境

### 主要配置项

```yaml
# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
    username: root
    password: 123456

# Redis配置
  data:
    redis:
      host: localhost
      port: 6379
      database: 0

# Nacos配置（可选）
  cloud:
    nacos:
      discovery:
        enabled: false  # 设置为true启用服务发现

# 管理员账户
admin:
  auth:
    username: admin
    password: password
```

## 📚 API 文档

应用启动后，可以通过以下地址访问相关信息：

- **应用首页**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **应用信息**: http://localhost:8080/actuator/info
- **所有端点**: http://localhost:8080/actuator

### 主要API端点

| 模块 | 端点 | 描述 |
|------|------|------|
| 认证 | `/api/auth/**` | 用户登录、注册、token管理 |
| 用户 | `/api/users/**` | 用户信息管理 |
| 商品 | `/api/items/**` | 商品CRUD操作 |
| 分类 | `/api/categories/**` | 商品分类管理 |
| 订单 | `/api/orders/**` | 订单处理 |
| 评论 | `/api/comments/**` | 商品评论系统 |
| 统计 | `/api/statistics/**` | 数据统计分析 |

## 🧪 测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserControllerTest

# 生成测试报告
mvn surefire-report:report
```

## 📊 监控和运维

### 健康检查

应用集成了Spring Boot Actuator，提供完整的监控和管理功能：

```bash
# 检查应用健康状态
curl http://localhost:8080/actuator/health

# 查看应用信息
curl http://localhost:8080/actuator/info

# 查看环境变量
curl http://localhost:8080/actuator/env

# 查看JVM指标
curl http://localhost:8080/actuator/metrics
```

### 日志管理

应用使用Logback进行日志管理，日志级别可通过配置文件调整：

```yaml
logging:
  level:
    com.example.onlinestore: DEBUG
    org.springframework: INFO
  file:
    name: logs/online-store.log
```

## 🔒 安全配置

### 管理员权限

系统支持基于注解的权限控制：

```java
@RequireAdmin
@GetMapping("/admin/users")
public ResponseEntity<List<User>> getAllUsers() {
    // 只有管理员可以访问
}
```

### 参数校验

使用自定义校验注解确保数据安全：

```java
@ValidateParams
@PostMapping("/items")
public ResponseEntity<Item> createItem(@Valid @RequestBody ItemDTO item) {
    // 自动参数校验
}
```

## 🐛 故障排除

### 常见问题

1. **启动失败 - Java版本问题**
   ```bash
   # 检查Java版本
   java -version
   # 确保使用JDK 17+
   ```

2. **数据库连接失败**
   ```bash
   # 检查MySQL服务状态
   sudo systemctl status mysql
   # 检查端口是否开放
   netstat -tlnp | grep 3306
   ```

3. **Redis连接失败**
   ```bash
   # 检查Redis服务状态
   sudo systemctl status redis
   # 测试Redis连接
   redis-cli ping
   ```

4. **JVM模块访问问题**
   ```bash
   # 添加JVM启动参数
   export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"
   ```

### 调试技巧

```bash
# 启用调试模式
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# 查看详细日志
mvn spring-boot:run -Dlogging.level.com.example.onlinestore=DEBUG
```

## 📈 性能优化

### Redis缓存策略

系统实现了多级缓存架构：

- **本地缓存**: 热点数据本地缓存
- **Redis缓存**: 分布式缓存共享
- **数据库**: 持久化存储

### 数据库优化

- 合理的索引设计
- 连接池优化
- 慢查询监控

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📝 许可证

本项目基于 MIT 许可证开源 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 📧 Email: developer@example.com
- 🐛 Issues: [GitHub Issues](https://github.com/your-repo/online-store/issues)
- 📖 文档: [项目Wiki](https://github.com/your-repo/online-store/wiki)

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者和开源社区。 