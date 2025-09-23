# Online Store 在线商店系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.2-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)

一个基于Spring Boot和Spring Cloud的现代化在线商店系统，提供完整的电商功能，包括商品管理、用户管理、购物车、订单处理等。

## ✨ 功能特性

### 🛍️ 商品管理
- 商品分类管理
- 商品品牌管理
- 商品属性管理
- 商品详情展示
- 库存管理

### 👥 用户管理
- 用户注册/登录
- JWT身份认证
- 用户信息管理
- 权限控制

### 🔧 系统特性
- RESTful API设计
- Spring Security安全框架
- MyBatis数据持久化
- Redis缓存支持
- 分页查询支持
- Docker容器化部署
- Nacos服务发现（可选）

## 🛠️ 技术栈

### 后端技术
- **Java**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: 身份认证和授权
- **MyBatis**: 3.0.3 - 数据访问层
- **PageHelper**: 2.1.0 - 分页插件

### 数据库
- **MySQL**: 8.2.0 - 主数据库
- **Redis**: 最新版 - 缓存和会话存储
- **Jedis**: 5.2.0 - Redis客户端

### 工具库
- **JWT**: 0.11.5 - JSON Web Token
- **Lombok**: 1.18.36 - 代码简化
- **Apache Commons**: 工具类库
- **Aliyun OSS**: 3.18.1 - 文件存储

### 服务发现（可选）
- **Nacos**: 2.2.0 - 服务注册与发现
- **Spring Cloud Alibaba**: 2022.0.0.0

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # 启动类
│   │   │   ├── config/                         # 配置类
│   │   │   ├── controller/                     # 控制器层
│   │   │   │   ├── AttributeController.java    # 商品属性API
│   │   │   │   ├── BrandController.java        # 品牌管理API
│   │   │   │   ├── CategoryController.java     # 分类管理API
│   │   │   │   ├── ItemController.java         # 商品管理API
│   │   │   │   ├── ItemDetailController.java   # 商品详情API
│   │   │   │   └── MemberController.java       # 用户管理API
│   │   │   ├── service/                        # 业务逻辑层
│   │   │   ├── mapper/                         # 数据访问层
│   │   │   ├── entity/                         # 实体类
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   ├── security/                       # 安全配置
│   │   │   ├── utils/                          # 工具类
│   │   │   ├── exceptions/                     # 异常处理
│   │   │   └── enums/                          # 枚举类
│   │   └── resources/
│   │       ├── application.yaml               # 应用配置
│   │       └── mapper/                        # MyBatis映射文件
│   └── test/                                  # 测试代码
├── scripts/                                   # 脚本文件
│   └── main.py                               # 测试数据生成脚本
├── docker-compose.yaml                        # Docker编排文件
├── Dockerfile                                 # Docker镜像文件
├── pom.xml                                   # Maven配置
└── README.md                                 # 项目文档
```

## 🚀 快速开始

### 环境要求

- **JDK**: 17或更高版本
- **Maven**: 3.6或更高版本
- **MySQL**: 8.0或更高版本
- **Redis**: 6.0或更高版本
- **Docker**: 可选，用于容器化部署

### 本地开发环境搭建

#### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

#### 2. 数据库准备
```sql
-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户（可选）
CREATE USER 'online_store'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON online_store.* TO 'online_store'@'localhost';
FLUSH PRIVILEGES;
```

#### 3. 配置应用
编辑 `src/main/resources/application.yaml`，配置数据库和Redis连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password  # 如果Redis设置了密码

jwt:
  secret: your-secret-key-here
```

#### 4. 启动应用

**方式一：使用Maven**
```bash
# 添加JVM参数（如果需要）
export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"

# 启动应用
mvn spring-boot:run
```

**方式二：使用IDE**
- 在IDE中导入Maven项目
- 添加VM参数：`--add-opens java.base/java.lang=ALL-UNNAMED`
- 运行 `OnlineStoreApplication.java`

#### 5. 验证部署
访问 `http://localhost:8080` 确认服务正常启动。

### 🐳 Docker部署

#### 使用Docker Compose（推荐）

```bash
# 启动MySQL和Redis
docker-compose --profile all up -d

# 等待数据库启动后，运行应用
mvn spring-boot:run
```

#### 构建应用镜像

```bash
# 构建应用
mvn clean package -DskipTests

# 构建Docker镜像
docker build -t online-store:latest .

# 运行容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  online-store:latest
```

## 📚 API文档

### 认证相关
所有API请求需要在Header中包含JWT Token：
```
Authorization: Bearer <your-jwt-token>
```

### 主要API端点

| 模块 | 端点 | 方法 | 描述 |
|------|------|------|------|
| 用户管理 | `/api/members` | GET | 获取用户列表 |
| 商品管理 | `/api/items` | GET | 获取商品列表 |
| 商品详情 | `/api/item-details` | GET | 获取商品详情 |
| 分类管理 | `/api/categories` | GET | 获取分类列表 |
| 品牌管理 | `/api/brands` | GET | 获取品牌列表 |
| 属性管理 | `/api/attributes` | GET | 获取属性列表 |

### 示例请求

```bash
# 获取商品列表
curl -X GET "http://localhost:8080/api/items" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json"

# 获取用户信息
curl -X GET "http://localhost:8080/api/members" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json"
```

## ⚙️ 配置说明

### 环境变量

| 变量名 | 描述 | 默认值 |
|--------|------|--------|
| `SPRING_PROFILES_ACTIVE` | 激活的Spring配置文件 | `local` |
| `ADMIN_USERNAME` | 管理员用户名 | `admin` |
| `ADMIN_PASSWORD` | 管理员密码 | `admin123` |
| `JWT_SECRET` | JWT密钥 | 必须设置 |
| `NACOS_ENABLED` | 是否启用Nacos | `false` |

### Spring Profiles

- **local**: 本地开发环境
- **docker**: Docker容器环境
- **prod**: 生产环境

## 🔧 开发指南

### 测试数据生成
项目提供了Python脚本来生成测试数据：

```bash
cd scripts
python main.py
```

### 代码规范
- 使用Lombok减少样板代码
- 遵循RESTful API设计规范
- 使用统一的异常处理机制
- 所有数据库操作使用事务管理

### 添加新功能
1. 在`entity`包中定义实体类
2. 在`mapper`包中创建数据访问接口
3. 在`service`包中实现业务逻辑
4. 在`controller`包中创建API端点
5. 添加相应的DTO类进行数据传输

## 🚨 故障排除

### 常见问题

**1. 应用启动失败**
- 检查JDK版本是否为17或更高
- 确认数据库连接配置正确
- 检查Redis服务是否正常运行

**2. 数据库连接失败**
```
java.sql.SQLException: Access denied for user
```
- 检查数据库用户名和密码
- 确认数据库已创建
- 检查MySQL服务状态

**3. Redis连接失败**
```
redis.clients.jedis.exceptions.JedisConnectionException
```
- 检查Redis服务是否启动
- 确认Redis配置（host、port、password）

**4. JWT相关错误**
- 确保设置了`JWT_SECRET`环境变量
- 检查Token是否过期

### 日志查看
```bash
# 查看应用日志
tail -f logs/application.log

# 或使用Docker查看
docker logs -f online-store
```

## 🤝 贡献指南

1. Fork本项目
2. 创建功能分支：`git checkout -b feature/your-feature`
3. 提交更改：`git commit -am 'Add some feature'`
4. 推送到分支：`git push origin feature/your-feature`
5. 提交Pull Request

## 📄 许可证

本项目采用MIT许可证，详情请参见LICENSE文件。

## 📞 联系方式

如果您有任何问题或建议，请通过以下方式联系：

- 提交Issue
- 发送邮件
- 创建Pull Request

---

**感谢使用Online Store在线商店系统！** 🎉 