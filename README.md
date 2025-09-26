# 在线商店系统 (Online Store)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)

一个功能完整的企业级在线商店系统，基于Spring Boot和微服务架构构建，支持商品管理、用户管理、订单处理等核心电商功能。

## ✨ 核心功能

- 🛍️ **商品管理**: 商品分类、品牌管理、属性配置、库存管理
- 👥 **用户管理**: 用户注册、登录、权限控制、JWT认证
- 📊 **数据分析**: 商品访问日志、用户行为分析
- 🔒 **安全控制**: Spring Security集成、JWT令牌认证
- 📦 **文件存储**: 阿里云OSS对象存储集成
- 🚀 **微服务架构**: Nacos服务发现与配置管理

## 🛠️ 技术栈

### 后端框架
- **JDK 17** - 现代Java开发平台
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务生态
- **Spring Security** - 安全框架
- **Spring Data Redis** - Redis集成

### 数据存储
- **MySQL 8.2.0** - 主数据库
- **Redis 5.2.0** - 缓存与会话存储
- **MyBatis 3.0.3** - ORM框架
- **PageHelper 2.1.0** - 分页插件

### 服务治理
- **Nacos 2.2.0** - 服务注册发现与配置管理
- **Spring Cloud Alibaba** - 阿里巴巴微服务套件

### 工具库
- **Lombok** - 代码简化
- **JWT (JJWT 0.11.5)** - 身份认证
- **Apache Commons Lang3** - 工具类库
- **Aliyun OSS** - 对象存储服务

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java      # 应用启动类
│   ├── bean/                           # Bean配置
│   ├── config/                         # 配置类
│   ├── constants/                      # 常量定义
│   ├── controller/                     # REST控制器
│   │   ├── AttributeController.java    # 商品属性API
│   │   ├── BrandController.java        # 品牌管理API
│   │   ├── CategoryController.java     # 分类管理API
│   │   ├── ItemController.java         # 商品管理API
│   │   ├── ItemDetailController.java   # 商品详情API
│   │   └── MemberController.java       # 用户管理API
│   ├── dto/                           # 数据传输对象
│   ├── entity/                        # 实体类
│   │   ├── AttributeEntity.java       # 属性实体
│   │   ├── BrandEntity.java           # 品牌实体
│   │   ├── CategoryEntity.java        # 分类实体
│   │   ├── ItemEntity.java            # 商品实体
│   │   ├── MemberEntity.java          # 用户实体
│   │   └── SkuEntity.java             # SKU实体
│   ├── enums/                         # 枚举类
│   ├── exceptions/                    # 异常处理
│   ├── mapper/                        # MyBatis映射器
│   ├── security/                      # 安全配置
│   ├── service/                       # 业务逻辑层
│   └── utils/                         # 工具类
├── src/main/resources/
│   ├── application.yaml               # 应用配置
│   ├── mapper/                        # MyBatis XML映射文件
│   └── sql/                          # 数据库建表脚本
├── scripts/                          # 脚本工具
├── docker-compose.yaml               # Docker容器编排
├── Dockerfile                        # Docker镜像构建
└── pom.xml                          # Maven依赖配置
```

## 🚀 快速开始

### 环境要求

- **JDK 17+** - Java开发环境
- **Maven 3.8+** - 项目构建工具
- **MySQL 8.0+** - 数据库服务
- **Redis 6.0+** - 缓存服务
- **Docker & Docker Compose** (可选) - 容器化部署

### 安装步骤

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 数据库初始化

**创建数据库:**
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**执行建表脚本:**
```bash
# 按顺序执行以下SQL文件
mysql -u root -p online_store < src/main/resources/sql/member_table.sql
mysql -u root -p online_store < src/main/resources/sql/category_table.sql
mysql -u root -p online_store < src/main/resources/sql/brand_table.sql
mysql -u root -p online_store < src/main/resources/sql/attribute_table.sql
mysql -u root -p online_store < src/main/resources/sql/attribute_value_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_table_table.sql
mysql -u root -p online_store < src/main/resources/sql/sku_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_attribute_relation_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_access_log_table.sql
```

#### 3. 配置应用

**环境变量配置:**
```bash
# 设置JWT密钥
export JWT_SECRET=your-jwt-secret-key-here

# 可选：配置管理员账户
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123

# 可选：启用Nacos服务发现
export NACOS_ENABLED=true
export SPRING_PROFILES_ACTIVE=prod
```

**修改数据库配置** (如需要):
编辑 `src/main/resources/application.yaml`：
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
```

#### 4. 构建和运行

**使用Maven运行:**
```bash
# 编译项目
mvn clean compile

# 运行应用 (需要添加JVM参数)
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

**或者打包运行:**
```bash
# 打包应用
mvn clean package -DskipTests

# 运行JAR文件
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### Docker部署 (推荐)

#### 使用Docker Compose快速启动

```bash
# 启动MySQL和Redis服务
docker-compose --profile all up -d

# 等待服务启动完成后，运行应用
mvn spring-boot:run
```

#### 自定义配置

根据需要修改 `docker-compose.yaml` 中的配置：
- MySQL数据卷路径
- Redis数据卷路径  
- 端口映射
- 环境变量

### 验证部署

访问以下端点验证应用是否正常运行：

- **应用健康检查**: http://localhost:8080/actuator/health
- **API文档**: http://localhost:8080/swagger-ui.html (如果配置了)
- **示例API**: http://localhost:8080/api/categories

## 📖 API文档

### 核心API端点

| 模块 | 端点 | 描述 |
|------|------|------|
| 用户管理 | `/api/members` | 用户注册、登录、资料管理 |
| 商品管理 | `/api/items` | 商品CRUD操作 |
| 分类管理 | `/api/categories` | 商品分类管理 |
| 品牌管理 | `/api/brands` | 品牌信息管理 |
| 属性管理 | `/api/attributes` | 商品属性配置 |
| 商品详情 | `/api/item-details` | 商品详细信息查询 |

### 认证说明

大部分API需要JWT令牌认证，请在请求头中添加：
```
Authorization: Bearer <your-jwt-token>
```

## 🔧 开发指南

### 代码规范

- 使用Lombok减少样板代码
- 遵循RESTful API设计原则
- 统一异常处理和响应格式
- 使用PageHelper进行分页查询

### 数据库设计

- 支持商品多属性配置
- 用户行为日志记录
- 完整的商品分类体系
- SKU库存管理

### 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify
```

## 🐛 常见问题

### Q: 启动时出现 "Illegal reflective access" 警告
**A:** 添加JVM参数 `--add-opens java.base/java.lang=ALL-UNNAMED`

### Q: 数据库连接失败
**A:** 检查MySQL服务状态和连接配置，确保数据库已创建

### Q: Redis连接超时
**A:** 确认Redis服务正在运行，检查防火墙和网络配置

### Q: JWT令牌验证失败
**A:** 确保设置了 `JWT_SECRET` 环境变量

## 📄 许可证

本项目采用 MIT 许可证，详情请参阅 [LICENSE](LICENSE) 文件。

## 🤝 贡献

欢迎提交Issue和Pull Request来改进这个项目！

## 📞 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 [GitHub Issue](../../issues)
- 发送邮件至项目维护者 