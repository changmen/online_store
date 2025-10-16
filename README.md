# 在线商店系统 (Online Store)

基于Spring Cloud的微服务架构在线商店系统，提供商品管理、用户管理等核心电商功能。

## 目录
- [项目简介](#项目简介)
- [核心功能](#核心功能)
- [技术架构](#技术架构)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [部署指南](#部署指南)
- [API文档](#api文档)
- [测试说明](#测试说明)
- [贡献指南](#贡献指南)

## 项目简介

在线商店系统是一个现代化的电商平台后端服务，采用微服务架构设计，具备高可用性、可扩展性和易维护性。系统提供了完整的电商核心功能，包括用户管理、商品管理、购物车、订单管理等模块。

## 核心功能

- **用户管理**
  - 用户注册与登录
  - JWT Token认证
  - 个人信息管理
  
- **商品管理**
  - 商品信息维护
  - 商品分类管理
  - 品牌管理
  - 商品属性管理
  
- **购物车功能**
  - 商品添加
  - 数量修改
  - 删除商品
  
- **订单管理**
  - 订单创建
  - 订单状态跟踪
  
## 技术架构

```mermaid
graph TD
    A[客户端] --> B[API网关]
    B --> C[用户服务]
    B --> D[商品服务]
    B --> E[订单服务]
    C --> F[(MySQL数据库)]
    D --> F
    E --> F
    C --> G[(Redis缓存)]
    D --> G
    style A fill:#FFE4C4,stroke:#333
    style B fill:#E0FFFF,stroke:#333
    style C fill:#98FB98,stroke:#333
    style D fill:#98FB98,stroke:#333
    style E fill:#98FB98,stroke:#333
    style F fill:#87CEEB,stroke:#333
    style G fill:#87CEEB,stroke:#333
```

### 技术栈

| 组件 | 技术 | 版本 |
|------|------|------|
| 核心框架 | Spring Boot | 3.4.3 |
| 微服务框架 | Spring Cloud | 2024.0.0 |
| 数据库 | MySQL | 8.2.0 |
| 缓存 | Redis | 5.2.0 |
| ORM框架 | MyBatis | 3.0.3 |
| 安全框架 | Spring Security + JWT | 0.11.5 |
| 配置中心 | Nacos | 2.2.0 |
| 对象存储 | 阿里云OSS | 3.18.1 |
| 日志框架 | SLF4J + Logback | - |
| 工具类 | Apache Commons | 3.17.0 |
| 代码简化 | Lombok | 1.18.36 |

## 项目结构

```
online-store/
├── scripts/                    # 脚本目录
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java  # 启动类
│   │   │   ├── bean/           # 实体类
│   │   │   ├── config/         # 配置类
│   │   │   ├── constants/      # 常量定义
│   │   │   ├── controller/     # 控制器层
│   │   │   ├── dto/            # 数据传输对象
│   │   │   ├── entity/         # 数据库实体
│   │   │   ├── enums/          # 枚举类
│   │   │   ├── errors/         # 错误码定义
│   │   │   ├── exceptions/     # 自定义异常
│   │   │   ├── handler/        # 全局异常处理
│   │   │   ├── mapper/         # MyBatis映射接口
│   │   │   ├── security/       # 安全相关
│   │   │   ├── service/        # 业务逻辑层
│   │   │   └── utils/          # 工具类
│   │   └── resources/
│   │       ├── application.yml      # 主配置文件
│   │       ├── application-local.yml # 本地环境配置
│   │       ├── mapper/         # MyBatis映射文件
│   │       ├── sql/            # SQL脚本
│   │       └── i18n/           # 国际化资源文件
│   └── test/                   # 测试代码
├── pom.xml                     # Maven配置文件
├── Dockerfile                  # Docker配置文件
├── docker-compose.yaml         # Docker Compose配置文件
└── README.md                   # 项目说明文件
```

## 环境要求

- JDK 17或更高版本
- Maven 3.6或更高版本
- MySQL 8.0或更高版本
- Redis 6.0或更高版本
- Docker (可选，用于容器化部署)

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd online-store
```

### 2. 数据库初始化

创建数据库：
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

执行建表脚本：
```sql
-- 用户表
CREATE TABLE member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(64) NOT NULL COMMENT '用户姓名',
    nick_name VARCHAR(64) COMMENT '用户昵称',
    password VARCHAR(255) NOT NULL COMMENT '用户密码',
    phone VARCHAR(20) COMMENT '用户手机号',
    gender VARCHAR(10) COMMENT '用户性别',
    age INT COMMENT '用户年龄',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 商品表
CREATE TABLE IF NOT EXISTS item (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品唯一标识ID',
  `brand_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联品牌ID',
  `category_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联类目ID',
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '商品名称',
  `description` VARCHAR(128) NOT NULL  DEFAULT ''  COMMENT '商品详细描述内容，存储在OSS的地址',
  `main_image_url` VARCHAR(128) NOT NULL DEFAULT ''  COMMENT '商品主图URL',
  `sub_image_urls` VARCHAR(2048) COMMENT '子图URL集合（多个用逗号分隔）',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ON_SALE' COMMENT '商品状态：ON_SALE-售卖中/OFF_SALE-已下架',
  `sort_score` INT NOT NULL DEFAULT 0 COMMENT '排序权重分（越大越靠前）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（ISO8601格式）',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间（ISO8601格式）',
  PRIMARY KEY (`id`),
  INDEX idx_brand (`brand_id`),
  INDEX idx_category_status (`category_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息表';
```

### 3. 环境配置

修改`src/main/resources/application-local.yml`中的数据库和Redis配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456  # 修改为你的MySQL密码
  data:
    redis:
      host: localhost
      port: 6379
      password:  # 如果Redis有密码，请填写
```

### 4. 编译并运行

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run -Dspring.profiles.active=local
```

或者使用Java命令运行：
```bash
# 打包项目
mvn clean package

# 运行jar包
java -jar target/online-store-1.0-SNAPSHOT.jar --spring.profiles.active=local
```

## 配置说明

### 主要配置文件

- `application.yml`: 主配置文件，包含通用配置
- `application-local.yml`: 本地开发环境配置

### 重要配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| server.port | 服务端口 | 8080 |
| spring.datasource.* | 数据库连接配置 | - |
| spring.data.redis.* | Redis连接配置 | - |
| jwt.secret | JWT密钥 | 从环境变量读取 |
| jwt.expiration | JWT过期时间(秒) | 86400 |

### 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| ADMIN_USERNAME | 管理员用户名 | admin |
| ADMIN_PASSWORD | 管理员密码 | admin123 |
| JWT_SECRET | JWT密钥 | 无默认值，必须设置 |
| MYSQL_PASSWORD | MySQL密码 | 123456 |

## 部署指南

### 本地部署

参考[快速开始](#快速开始)部分。

### 容器化部署

项目支持使用Docker和docker-compose进行容器化部署。

1. 构建Docker镜像：
```bash
docker build -t online-store .
```

2. 使用docker-compose启动服务：
```bash
docker-compose --profile all up -d
```

这将启动MySQL、Redis和在线商店应用三个容器。

如果只想启动应用容器（假设你已经有外部的MySQL和Redis服务）：
```bash
docker-compose --profile app-only up -d
```

### 多环境配置

通过Spring Profiles实现多环境配置：
- `local`: 本地开发环境
- `dev`: 开发环境
- `test`: 测试环境
- `prod`: 生产环境

## API文档

### 认证机制

除登录和注册接口外，其他接口都需要在HTTP Header中携带JWT Token：
```
Authorization: Bearer <token>
```

### 核心API接口

#### 用户相关接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户注册 | POST | `/api/v1/members/registry` | 注册新用户 |
| 用户登录 | POST | `/api/v1/members/login` | 用户登录获取Token |

#### 商品相关接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 获取商品详情 | GET | `/api/v1/items/{itemId}` | 根据ID获取商品详情 |

#### 分类相关接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 获取分类列表 | GET | `/api/v1/categories` | 获取所有分类 |
| 获取分类详情 | GET | `/api/v1/categories/{categoryId}` | 根据ID获取分类详情 |

#### 品牌相关接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 获取品牌列表 | GET | `/api/v1/brands` | 获取所有品牌 |
| 获取品牌详情 | GET | `/api/v1/brands/{brandId}` | 根据ID获取品牌详情 |

### API调用示例

用户注册：
```bash
curl -X POST http://localhost:8080/api/v1/members/registry \
  -H "Content-Type: application/json" \
  -d '{
    "name": "testuser",
    "password": "password123",
    "nickName": "测试用户",
    "phone": "13800138000"
  }'
```

用户登录：
```bash
curl -X POST http://localhost:8080/api/v1/members/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

获取商品详情（需要认证）：
```bash
curl -X GET http://localhost:8080/api/v1/items/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.xxxxxxx"
```

## 测试说明

### 单元测试

项目使用JUnit 5和Spring Boot Test进行单元测试。

运行所有测试：
```bash
mvn test
```

运行特定测试类：
```bash
mvn -Dtest=MemberServiceTest test
```

### 测试覆盖率

项目集成了JaCoCo代码覆盖率工具，可以通过以下命令生成覆盖率报告：
```bash
mvn test jacoco:report
```

报告生成位置：`target/site/jacoco/index.html`

## 贡献指南

我们欢迎任何形式的贡献！

### 提交Issue

如果您发现了bug或者有功能建议，请提交Issue：
1. 检查是否已存在相关Issue
2. 使用清晰明确的标题
3. 详细描述问题或建议
4. 提供复现步骤（如果是bug）

### 提交Pull Request

1. Fork项目并创建功能分支
2. 编写代码并通过所有测试
3. 添加必要的文档说明
4. 提交Pull Request并详细描述变更内容

### 代码规范

- 遵循Google Java Style Guide
- 添加必要的单元测试
- 保持代码简洁和可读性
- 添加适当的注释