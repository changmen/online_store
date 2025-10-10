# 🛒 Online Store

一个功能完整的在线商店系统，基于Spring Cloud微服务架构构建，提供商品管理、用户管理、商品展示等核心电商功能。

## ✨ 主要功能

- 🏷️ **商品管理**：商品信息管理、SKU管理、商品属性配置
- 🏢 **品牌管理**：品牌信息维护和管理
- 📂 **类目管理**：商品分类体系管理
- 👤 **会员管理**：用户注册、登录、个人信息管理
- 🔍 **商品展示**：商品详情页、商品列表、搜索功能
- 📊 **访问统计**：商品访问日志记录和分析
- 🔐 **安全认证**：基于JWT的用户认证和授权
- ☁️ **文件存储**：集成阿里云OSS对象存储服务

## 🛠️ 技术栈

### 后端框架
- **JDK 17** - Java开发环境
- **Spring Boot 3.4.3** - 应用框架
- **Spring Cloud 2024.0.0** - 微服务架构
- **Spring Security** - 安全框架
- **MyBatis 3.0.3** - ORM持久层框架
- **PageHelper 2.1.0** - 分页插件

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis** - 缓存数据库
- **阿里云OSS 3.18.1** - 对象存储服务

### 服务发现与配置
- **Nacos 2.2.0** - 服务注册发现和配置中心
- **Spring Cloud Alibaba 2022.0.0.0** - 阿里云微服务组件

### 其他组件
- **JWT 0.11.5** - 身份认证令牌
- **Lombok 1.18.36** - 代码生成工具
- **Apache Commons** - 工具类库

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java      # 启动类
│   │   │   ├── controller/                      # 控制层
│   │   │   │   ├── AttributeController.java     # 商品属性API
│   │   │   │   ├── BrandController.java         # 品牌管理API
│   │   │   │   ├── CategoryController.java      # 类目管理API
│   │   │   │   ├── ItemController.java          # 商品管理API
│   │   │   │   ├── ItemDetailController.java    # 商品详情API
│   │   │   │   └── MemberController.java        # 会员管理API
│   │   │   ├── service/                         # 业务层
│   │   │   ├── mapper/                          # 数据访问层
│   │   │   ├── entity/                          # 实体类
│   │   │   ├── dto/                             # 数据传输对象
│   │   │   ├── config/                          # 配置类
│   │   │   ├── security/                        # 安全配置
│   │   │   ├── utils/                           # 工具类
│   │   │   ├── exceptions/                      # 异常处理
│   │   │   └── enums/                           # 枚举类
│   │   └── resources/
│   │       ├── application.yaml                # 主配置文件
│   │       ├── application-local.yaml          # 本地环境配置
│   │       ├── bootstrap.yaml                  # 引导配置
│   │       ├── mapper/                          # MyBatis映射文件
│   │       ├── sql/                             # 数据库初始化脚本
│   │       └── i18n/                            # 国际化资源
│   └── test/                                    # 测试代码
├── scripts/                                     # 脚本文件
├── docker-compose.yaml                          # Docker编排文件
├── Dockerfile                                   # Docker镜像构建文件
├── pom.xml                                      # Maven依赖配置
└── README.md                                    # 项目说明文档
```

## 🗄️ 数据库表结构

项目包含以下核心数据表：
- `item` - 商品信息表
- `sku` - 商品SKU表
- `brand` - 品牌信息表
- `category` - 商品类目表
- `attribute` - 商品属性表
- `attribute_value` - 属性值表
- `item_attribute_relation` - 商品属性关联表
- `member` - 会员信息表
- `item_access_log` - 商品访问日志表

## 🚀 环境要求

### 基础环境
- **JDK 17** 或更高版本
- **Maven 3.6** 或更高版本
- **MySQL 8.0** 或更高版本
- **Redis 6.0** 或更高版本

### 可选组件
- **Nacos** (如需启用服务发现)
- **Docker & Docker Compose** (容器化部署)
- **阿里云OSS** (文件存储服务)

## 🏃‍♂️ 快速开始

### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

### 2. 环境配置

#### 数据库初始化
```sql
# 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据表结构
# 执行 src/main/resources/sql/ 目录下的所有SQL文件
```

#### 配置文件
修改 `src/main/resources/application.yaml` 中的配置：
```yaml
# 数据库连接
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
    username: your_username
    password: your_password
 
  # Redis连接
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password

# JWT密钥 (生产环境请使用安全的密钥)
jwt:
  secret: your_jwt_secret_key
```

### 3. 运行应用

#### 本地开发运行
```bash
# 编译项目
mvn clean compile

# 启动应用
mvn spring-boot:run

# 或者添加JVM参数
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

#### Docker Compose 运行
```bash
# 启动MySQL和Redis服务
docker-compose up -d mysql redis

# 等待服务启动完成后，启动应用
mvn spring-boot:run
```

### 4. 验证部署

访问以下端点验证服务运行状态：
- 应用首页：http://localhost:8080
- 健康检查：http://localhost:8080/actuator/health
- API文档：根据具体实现访问相应端点

## 🔧 开发指南

### 环境变量配置

可通过环境变量覆盖默认配置：
```bash
export SPRING_PROFILES_ACTIVE=local          # 激活的配置文件
export ADMIN_USERNAME=admin                  # 管理员用户名
export ADMIN_PASSWORD=admin123               # 管理员密码
export JWT_SECRET=your_secret_key             # JWT密钥
export NACOS_ENABLED=false                   # 是否启用Nacos
```

### API接口

主要API端点：
- `GET /api/items` - 获取商品列表
- `GET /api/items/{id}` - 获取商品详情
- `POST /api/items` - 创建商品
- `PUT /api/items/{id}` - 更新商品
- `GET /api/brands` - 获取品牌列表
- `GET /api/categories` - 获取类目列表
- `POST /api/members/register` - 用户注册
- `POST /api/members/login` - 用户登录

### 开发注意事项

1. **代码规范**：项目使用Lombok简化代码，请确保IDE安装Lombok插件
2. **数据库操作**：使用MyBatis进行数据访问，SQL文件位于`resources/mapper/`目录
3. **安全认证**：API需要JWT令牌认证，请先登录获取token
4. **日志记录**：重要操作会记录访问日志，便于后续分析

## 📋 后续规划

- [ ] 购物车功能
- [ ] 订单管理系统
- [ ] 支付系统集成
- [ ] 商品搜索优化
- [ ] 推荐系统
- [ ] 移动端API
- [ ] 管理后台界面
- [ ] 性能监控和告警

## 🤝 贡献指南

1. Fork 项目仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 Issue
- 发送邮件
- 项目讨论区 