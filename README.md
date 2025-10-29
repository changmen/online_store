# 在线商店系统 (Online Store)

一个基于Spring Boot和Spring Cloud技术栈构建的现代化电商平台，提供完整的商品管理、用户管理、库存管理等核心功能。

## ✨ 主要特性

- 🛍️ **商品管理**: 支持商品信息、SKU、属性、品牌等全方位管理
- 👥 **用户管理**: 完整的会员体系和权限管理
- 🏷️ **分类管理**: 灵活的商品分类体系
- 🔍 **属性系统**: 可配置的商品属性和属性值
- 🛡️ **安全认证**: 基于JWT的身份认证和Spring Security权限控制
- 📊 **访问日志**: 完整的用户行为追踪
- 🚀 **微服务架构**: 支持Nacos服务发现和配置管理
- 📁 **文件管理**: 集成阿里云OSS对象存储

## 🛠️ 技术栈

### 核心框架
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: 身份认证和授权
- **Spring Cloud Alibaba**: 2022.0.0.0

### 数据库与缓存
- **MySQL**: 8.2.0 (主数据库)
- **Redis**: 缓存和会话管理
- **MyBatis**: 3.0.3 (ORM框架)
- **PageHelper**: 2.1.0 (分页插件)

### 服务发现与配置
- **Nacos**: 2.2.0 (服务注册发现和配置管理)

### 其他组件
- **JWT**: 0.11.5 (Token认证)
- **Lombok**: 1.18.36 (代码简化)
- **Apache Commons**: 工具类库
- **阿里云OSS**: 3.18.1 (对象存储)
- **Jedis**: 5.2.0 (Redis客户端)

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── OnlineStoreApplication.java     # 应用启动类
│   ├── bean/                          # Bean配置
│   ├── config/                        # 配置类
│   ├── constants/                     # 常量定义
│   ├── controller/                    # REST控制器
│   │   ├── AttributeController.java   # 属性管理
│   │   ├── BrandController.java       # 品牌管理
│   │   ├── CategoryController.java    # 分类管理
│   │   ├── ItemController.java        # 商品管理
│   │   ├── ItemDetailController.java  # 商品详情
│   │   └── MemberController.java      # 会员管理
│   ├── dto/                          # 数据传输对象
│   ├── entity/                       # 实体类
│   ├── enums/                        # 枚举类
│   ├── exceptions/                   # 异常处理
│   ├── mapper/                       # MyBatis映射器
│   ├── security/                     # 安全配置
│   ├── service/                      # 业务逻辑层
│   └── utils/                        # 工具类
├── src/main/resources/
│   ├── application.yaml              # 应用配置
│   ├── application-local.yaml        # 本地环境配置
│   ├── bootstrap.yaml                # 启动配置
│   ├── mapper/                       # MyBatis XML映射文件
│   ├── sql/                          # 数据库脚本
│   └── i18n/                         # 国际化资源
├── scripts/                          # 脚本文件
├── docker-compose.yaml               # Docker编排文件
├── Dockerfile                        # Docker镜像构建文件
└── pom.xml                          # Maven依赖配置
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17或更高版本
- **Maven**: 3.6或更高版本
- **MySQL**: 8.0或更高版本
- **Redis**: 6.0或更高版本
- **Docker** (可选): 用于容器化部署

### 安装步骤

#### 方式一：本地开发环境

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd online-store
   ```

2. **准备数据库**
   ```sql
   # 创建数据库
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # 执行初始化脚本（如果有）
   # source src/main/resources/sql/init.sql
   ```

3. **配置应用**
   - 复制 `application-local.yaml` 并根据需要修改数据库连接信息
   - 设置必要的环境变量：
     ```bash
     export JWT_SECRET=your-jwt-secret-key
     export SPRING_PROFILES_ACTIVE=local
     ```

4. **启动应用**
   ```bash
   # 方式1：使用Maven
   mvn clean spring-boot:run
   
   # 方式2：编译后运行
   mvn clean package
   java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
   ```

#### 方式二：Docker容器化部署

1. **启动基础服务**
   ```bash
   # 启动MySQL和Redis
   docker-compose --profile all up -d
   
   # 仅启动MySQL
   docker-compose --profile without-redis up -d
   ```

2. **构建并运行应用**
   ```bash
   # 构建Docker镜像
   docker build -t online-store .
   
   # 运行容器
   docker run -d -p 8080:8080 --name online-store-app online-store
   ```

### 访问应用

- **应用地址**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **默认管理员账号**: admin/admin123

## 🔧 配置说明

### 环境变量

| 变量名 | 描述 | 默认值 |
|--------|------|--------|
| `SPRING_PROFILES_ACTIVE` | 激活的配置文件 | `local` |
| `JWT_SECRET` | JWT密钥 | 必填 |
| `ADMIN_USERNAME` | 管理员用户名 | `admin` |
| `ADMIN_PASSWORD` | 管理员密码 | `admin123` |
| `NACOS_ENABLED` | 是否启用Nacos | `false` |

### 数据库配置

默认数据库连接信息（可在 `application-local.yaml` 中修改）：
- **地址**: localhost:3306
- **数据库**: online_store
- **用户名**: root
- **密码**: 123456

### Redis配置

默认Redis连接信息：
- **地址**: localhost:6379
- **数据库**: 0
- **密码**: 无

## 📚 API文档

### 主要接口

- **商品管理**: `/api/items`
- **品牌管理**: `/api/brands`
- **分类管理**: `/api/categories`
- **属性管理**: `/api/attributes`
- **会员管理**: `/api/members`

> 建议集成Swagger或OpenAPI文档以获得完整的API说明

## 🧪 测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=YourTestClass
```

## 📦 部署

### 生产环境部署

1. **设置生产环境配置**
   ```bash
   export SPRING_PROFILES_ACTIVE=prod
   export JWT_SECRET=your-production-jwt-secret
   ```

2. **构建生产包**
   ```bash
   mvn clean package -Pprod
   ```

3. **运行应用**
   ```bash
   java -Xms512m -Xmx1024m --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
   ```

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 [MIT License](LICENSE) 许可证。

## 📞 支持

如有问题或建议，请提交 [Issue](../../issues) 或联系项目维护者。 