# Online Store

这是一个基于 Spring Cloud 的在线商店项目，提供完整的电商功能，包括用户管理、商品管理、订单管理、购物车、支付等核心模块。

## 功能特性

- 🔐 用户认证与授权（基于 JWT + Spring Security）
- 👤 用户管理（注册、登录、个人信息管理）
- 📦 商品管理（商品 CRUD、分类、库存管理）
- 🛒 购物车功能
- 📋 订单管理（创建订单、订单状态跟踪）
- 💳 支付集成
- 🖼️ 文件上传（集成阿里云 OSS）
- 📊 分页查询支持
- ⚡ Redis 缓存支持
- 🎯 统一异常处理
- 📝 参数校验

## 技术栈

### 核心框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0
- **Spring Security**: 基于 Spring Boot 3.4.3

### 数据层
- **MyBatis**: 3.0.2
- **MyBatis Spring**: 3.0.3
- **PageHelper**: 2.1.0（分页插件）
- **MySQL Connector**: 8.2.0
- **MySQL**: 8.0+

### 缓存与存储
- **Redis**: 6.0+
- **Jedis**: 5.2.0
- **Spring Data Redis**: 基于 Spring Boot 3.4.3
- **阿里云 OSS**: 3.18.1

### 服务治理
- **Nacos**: 2.2.0（配置中心 + 服务发现）

### 工具库
- **Lombok**: 1.18.36
- **Apache Commons Lang3**: 3.17.0
- **Apache Commons Collections**: 3.2.2
- **JJWT**: 0.11.5（JWT 令牌生成与验证）
- **CGLIB**: 3.3.0

### 容器化
- **Docker**: 支持 Docker Compose 快速部署

## 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 主启动类
│   │   │   ├── bean/                          # Bean 配置
│   │   │   ├── config/                        # 配置类（Redis、MyBatis、Security等）
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # 控制器层（API 接口）
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── entity/                        # 实体类（数据库表映射）
│   │   │   ├── enums/                         # 枚举类
│   │   │   ├── errors/                        # 错误定义
│   │   │   ├── exceptions/                    # 自定义异常
│   │   │   ├── handler/                       # 异常处理器
│   │   │   ├── mapper/                        # MyBatis Mapper 接口
│   │   │   ├── security/                      # 安全相关配置
│   │   │   ├── service/                       # 业务逻辑层
│   │   │   └── utils/                         # 工具类
│   │   └── resources/
│   │       ├── application.yml                # 应用配置
│   │       ├── bootstrap.yml                  # Nacos 配置
│   │       └── mapper/                        # MyBatis XML 映射文件
│   └── test/                                  # 测试代码
├── scripts/                                   # 脚本工具
├── docker-compose.yaml                        # Docker Compose 配置
├── Dockerfile                                 # Docker 镜像构建文件
├── pom.xml                                    # Maven 项目配置
└── README.md                                  # 项目说明文档
```

## 环境要求

### 开发环境
- **JDK**: 17 或更高版本
- **Maven**: 3.6 或更高版本
- **IDE**: IntelliJ IDEA 2023+ / Eclipse 2023+ / VS Code

### 运行环境
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Nacos**: 2.2.0+（可选，用于配置管理）

## 快速开始

### 方式一：使用 Docker Compose（推荐）

1. **启动 MySQL 和 Redis**

```bash
# 启动所有服务（MySQL + Redis）
docker-compose --profile all up -d

# 或仅启动 MySQL
docker-compose --profile without-redis up -d
```

2. **创建数据库**

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **修改配置文件**

编辑 `src/main/resources/application.yml`，配置数据库和 Redis 连接信息。

4. **运行应用**

```bash
mvn clean spring-boot:run
```

### 方式二：本地环境运行

1. **安装并启动 MySQL**

确保 MySQL 8.0+ 已安装并运行在 `localhost:3306`。

2. **安装并启动 Redis**

确保 Redis 6.0+ 已安装并运行在 `localhost:6379`。

3. **创建数据库**

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

4. **配置应用**

修改 `src/main/resources/application.yml` 中的配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

5. **编译并运行**

```bash
# 编译项目
mvn clean package

# 运行应用（添加必要的 JVM 参数）
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar

# 或使用 Maven 插件运行
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

### 验证运行

应用启动后，访问以下地址验证：

- **健康检查**: http://localhost:8080/actuator/health
- **API 文档**: （如有集成 Swagger，访问对应地址）

## 配置说明

### application.yml 主要配置项

```yaml
server:
  port: 8080                    # 应用端口

spring:
  application:
    name: online-store          # 应用名称
  
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
      timeout: 3000ms
  
  security:
    # JWT 密钥等安全配置

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.onlinestore.entity
  configuration:
    map-underscore-to-camel-case: true

pagehelper:
  helper-dialect: mysql
  reasonable: true
  support-methods-arguments: true
```

### Nacos 配置（可选）

如需使用 Nacos 配置中心，需配置 `bootstrap.yml`。

## 开发指南

### 编译项目

```bash
mvn clean compile
```

### 运行测试

```bash
mvn test
```

### 打包部署

```bash
# 打包为 JAR 文件
mvn clean package -DskipTests

# 生成的 JAR 位于 target/online-store-1.0-SNAPSHOT.jar
```

### Docker 部署

```bash
# 构建 Docker 镜像
docker build -t online-store:latest .

# 运行容器
docker run -d -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/online_store \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=123456 \
  -e SPRING_DATA_REDIS_HOST=host.docker.internal \
  online-store:latest
```

## 常见问题

### 1. 启动时报错 `InaccessibleObjectException`

**解决方案**：添加 JVM 参数
```bash
--add-opens java.base/java.lang=ALL-UNNAMED
```

### 2. 数据库连接失败

**检查项**：
- MySQL 服务是否启动
- 数据库名称、用户名、密码是否正确
- MySQL 版本是否为 8.0+
- 时区设置是否正确（`serverTimezone=Asia/Shanghai`）

### 3. Redis 连接超时

**检查项**：
- Redis 服务是否启动
- Redis 端口是否正确（默认 6379）
- 防火墙是否阻止连接

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进本项目。

## 许可证

本项目采用 MIT 许可证。详见 LICENSE 文件。 