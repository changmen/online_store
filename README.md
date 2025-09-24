# Online Store 在线商店系统

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)
![JDK](https://img.shields.io/badge/JDK-17-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)
![Redis](https://img.shields.io/badge/Redis-Jedis%205.2.0-red.svg)

基于Spring Boot + Spring Cloud的现代化在线商店系统，提供完整的电商业务功能，包括商品管理、品牌管理、用户管理、属性管理等核心模块。

## ✨ Core Features

### 🛍️ 商品管理
- 商品CRUD操作
- 商品分类管理
- 商品状态管理（上架/下架）
- 商品图片管理（主图+子图）
- 商品搜索与分页

### 🏷️ 品牌管理
- 品牌信息管理（名称、描述、Logo、故事）
- 品牌排序和可见性控制
- 品牌名称唯一性校验

### 👥 用户管理
- 用户注册/登录
- JWT Token认证
- 用户信息管理
- Spring Security集成

### 🔧 属性管理
- 商品属性定义
- 属性值管理
- 商品属性关联

### 📊 访问日志
- 商品访问记录
- 用户行为追踪

## 🛠️ Technology Stack

### 后端框架
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: JWT认证
- **Spring Data Redis**: 缓存支持
- **Spring Validation**: 参数校验
- **Spring AOP**: 切面编程

### 数据存储
- **MySQL**: 8.2.0 - 主数据库
- **Redis**: Jedis 5.2.0 - 缓存和会话
- **MyBatis**: 3.0.3 - ORM框架
- **PageHelper**: 2.1.0 - 分页插件

### 服务治理
- **Nacos**: 配置中心和服务发现
- **Spring Cloud Alibaba**: 2022.0.0.0

### 工具库
- **Lombok**: 1.18.36 - 代码简化
- **Apache Commons**: 工具类
- **JJWT**: 0.11.5 - JWT处理
- **Aliyun OSS**: 3.18.1 - 对象存储
- **Jackson**: JSON处理

## 📁 Project Structure

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java     # 应用启动类
│   ├── bean/                           # 业务对象
│   │   ├── Attribute.java
│   │   ├── Brand.java
│   │   ├── Category.java
│   │   ├── Item.java
│   │   ├── Member.java
│   │   └── Sku.java
│   ├── config/                         # 配置类
│   │   ├── SecurityConfig.java         # 安全配置
│   │   ├── RedisConfig.java           # Redis配置
│   │   ├── MyBatisConfig.java         # MyBatis配置
│   │   └── OssConfig.java             # OSS配置
│   ├── controller/                     # 控制器层
│   │   ├── AttributeController.java
│   │   ├── BrandController.java
│   │   ├── CategoryController.java
│   │   ├── ItemController.java
│   │   └── MemberController.java
│   ├── dto/                           # 数据传输对象
│   │   ├── Request/                   # 请求对象
│   │   ├── Response/                  # 响应对象
│   │   └── converter/                 # 转换器
│   ├── entity/                        # 数据库实体
│   ├── enums/                         # 枚举类
│   ├── service/                       # 业务逻辑层
│   │   └── impl/                      # 实现类
│   ├── mapper/                        # 数据访问层
│   ├── security/                      # 安全相关
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtTokenUtil.java
│   ├── exceptions/                    # 异常处理
│   └── utils/                         # 工具类
├── src/main/resources/
│   ├── application.yaml               # 主配置文件
│   ├── application-local.yaml         # 本地环境配置
│   ├── bootstrap.yaml                 # 启动配置
│   ├── mapper/                        # MyBatis映射文件
│   ├── sql/                          # 数据库脚本
│   └── i18n/                         # 国际化文件
├── scripts/                           # 脚本文件
│   └── main.py                       # Git提交分析脚本
├── docker-compose.yaml               # Docker编排
├── Dockerfile                        # Docker镜像
└── pom.xml                          # Maven配置
```

## 🚀 Quick Start

### 环境要求
- **JDK**: 17或更高版本
- **Maven**: 3.6或更高版本  
- **MySQL**: 8.0或更高版本
- **Redis**: 6.0或更高版本

### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

### 2. 数据库初始化
```sql
# 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 使用数据库
USE online_store;

# 执行建表脚本
source src/main/resources/sql/member_table.sql;
source src/main/resources/sql/brand_table.sql;
source src/main/resources/sql/item_table_table.sql;
source src/main/resources/sql/category_table.sql;
source src/main/resources/sql/attribute_table.sql;
source src/main/resources/sql/attribute_value_table.sql;
source src/main/resources/sql/item_attribute_relation_table.sql;
source src/main/resources/sql/item_access_log_table.sql;
source src/main/resources/sql/sku_table.sql;
```

### 3. 配置文件

#### 环境变量配置
```bash
# 数据库配置
export MYSQL_PASSWORD=your_password

# JWT配置
export JWT_SECRET=your_jwt_secret_key

# 管理员账户
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123

# Nacos配置（可选）
export NACOS_ENABLED=false
export NACOS_SERVER_ADDR=localhost:8848
export NACOS_NAMESPACE=your_namespace
```

#### 本地开发配置
修改 `src/main/resources/application-local.yaml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: ${MYSQL_PASSWORD:123456}
  data:
    redis:
      host: localhost
      port: 6379
      password: 
      database: 0
```

### 4. 运行应用

#### 方式一：Maven启动
```bash
# 编译项目
mvn clean compile

# 启动应用（添加JVM参数）
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

#### 方式二：Docker启动
```bash
# 启动MySQL和Redis
docker-compose up -d mysql redis

# 构建应用镜像
docker build -t online-store .

# 运行应用容器
docker run -p 8080:8080 \
  -e MYSQL_PASSWORD=123456 \
  -e JWT_SECRET=your_secret_key \
  online-store
```

### 5. 验证部署
```bash
# 健康检查
curl http://localhost:8080/actuator/health

# 测试API
curl http://localhost:8080/api/v1/categories/1
```

## 📡 API Endpoints

### 用户管理
- `POST /api/v1/members/registry` - 用户注册
- `POST /api/v1/members/login` - 用户登录

### 商品管理
- `GET /api/v1/items/{itemId}` - 获取商品详情
- `GET /api/v1/items` - 商品列表查询
- `POST /api/v1/items` - 创建商品
- `PUT /api/v1/items/{itemId}` - 更新商品

### 品牌管理
- `GET /api/v1/brands` - 品牌列表
- `GET /api/v1/brands/{brandId}` - 品牌详情
- `POST /api/v1/brands` - 添加品牌
- `PUT /api/v1/brands/{brandId}` - 更新品牌
- `DELETE /api/v1/brands/{brandId}` - 删除品牌

### 分类管理
- `GET /api/v1/categories/{categoryId}` - 获取分类信息

### 属性管理
- `GET /api/v1/attributes/{attributeId}` - 获取属性
- `POST /api/v1/attributes` - 创建属性
- `PUT /api/v1/attributes/{attributeId}` - 更新属性

> 📖 详细API文档请查看各Controller类中的接口定义

## 🔧 Configuration

### 多环境配置
- `application.yaml` - 基础配置
- `application-local.yaml` - 本地开发环境
- `bootstrap.yaml` - 启动配置（Nacos集成）

### 关键配置项
```yaml
# 服务端口
server:
  port: 8080

# 数据库连接
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/online_store
    username: root
    password: ${MYSQL_PASSWORD}

# Redis配置
  data:
    redis:
      host: localhost
      port: 6379
      database: 0

# MyBatis配置
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.onlinestore.model
  configuration:
    map-underscore-to-camel-case: true

# JWT配置
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400  # 24小时
```

## 🧪 Testing

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify

# 生成测试报告
mvn surefire-report:report
```

## 📦 Deployment

### Docker部署
```bash
# 构建镜像
docker build -t online-store:latest .

# 运行容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e MYSQL_PASSWORD=your_password \
  -e JWT_SECRET=your_secret \
  online-store:latest
```

### 生产环境注意事项
1. **数据库**: 配置主从复制和读写分离
2. **Redis**: 配置集群模式
3. **监控**: 集成Prometheus + Grafana
4. **日志**: 配置ELK日志收集
5. **安全**: 配置HTTPS和防火墙规则

## 🔍 Monitoring & Operations

### 健康检查
- 应用健康状态: `http://localhost:8080/actuator/health`
- 应用信息: `http://localhost:8080/actuator/info`
- 指标监控: `http://localhost:8080/actuator/metrics`

### 日志配置
日志级别可通过环境变量控制：
```bash
export LOGGING_LEVEL_ROOT=INFO
export LOGGING_LEVEL_COM_EXAMPLE_ONLINESTORE=DEBUG
```

## 🤝 Contributing

1. Fork 项目
2. 创建功能分支: `git checkout -b feature/AmazingFeature`
3. 提交更改: `git commit -m 'Add some AmazingFeature'`
4. 推送到分支: `git push origin feature/AmazingFeature`
5. 开启 Pull Request

### 代码规范
- 遵循Java编码规范
- 使用Lombok简化代码
- 添加必要的注释和文档
- 确保测试覆盖率

## 📄 License

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 🔗 Related Links

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring Cloud 官方文档](https://spring.io/projects/spring-cloud)
- [MyBatis 官方文档](https://mybatis.org/mybatis-3/)
- [Nacos 官方文档](https://nacos.io/)

---

**技术支持**: 如有问题请提交 Issue 或联系开发团队 