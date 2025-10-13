# Online Store 🛒

基于Spring Boot 3.4.3 + Spring Cloud的现代化在线商店系统，提供完整的电商核心功能。

## ✨ 功能特性

### 🛍️ 商品管理
- 商品信息管理（商品详情、规格、库存）
- 商品分类管理（层级分类结构）
- 品牌管理
- 商品属性管理（动态属性配置）
- SKU管理（多规格商品支持）

### 👥 用户管理  
- 会员注册、登录
- JWT认证授权
- 用户信息管理

### 📊 数据统计
- 商品访问日志记录
- 用户行为分析

### 🔧 系统特性
- RESTful API设计
- Spring Security安全框架
- Redis缓存支持
- MyBatis持久层框架
- 分页查询支持
- 阿里云OSS文件存储集成
- Nacos服务发现（可选）
- Docker容器化部署

## 🛠️ 技术栈

### 后端技术
- **Java**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: JWT认证
- **MyBatis**: 3.0.3 + PageHelper分页
- **数据库**: MySQL 8.2.0
- **缓存**: Redis + Jedis 5.2.0
- **服务发现**: Nacos 2.2.0
- **文件存储**: 阿里云OSS 3.18.1

### 开发工具
- **构建工具**: Maven 3.6+
- **容器化**: Docker + Docker Compose
- **API文档**: Spring Boot Actuator

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # 启动类
│   │   │   ├── controller/                     # REST控制器
│   │   │   │   ├── ItemController.java         # 商品管理
│   │   │   │   ├── CategoryController.java     # 分类管理
│   │   │   │   ├── BrandController.java        # 品牌管理
│   │   │   │   ├── MemberController.java       # 会员管理
│   │   │   │   └── AttributeController.java    # 属性管理
│   │   │   ├── service/                        # 业务逻辑层
│   │   │   ├── mapper/                         # 数据访问层
│   │   │   ├── entity/                         # 实体类
│   │   │   │   ├── ItemEntity.java             # 商品实体
│   │   │   │   ├── CategoryEntity.java         # 分类实体
│   │   │   │   ├── BrandEntity.java            # 品牌实体
│   │   │   │   ├── MemberEntity.java           # 会员实体
│   │   │   │   └── SkuEntity.java              # SKU实体
│   │   │   ├── dto/                            # 数据传输对象
│   │   │   ├── config/                         # 配置类
│   │   │   ├── security/                       # 安全配置
│   │   │   ├── utils/                          # 工具类
│   │   │   └── exceptions/                     # 异常处理
│   │   └── resources/
│   │       ├── application.yaml               # 主配置文件
│   │       └── mapper/                         # MyBatis映射文件
│   └── test/                                   # 测试代码
├── scripts/                                    # 工具脚本
│   ├── main.py                                 # Git提交分析脚本
│   └── output/                                 # 脚本输出
├── docker-compose.yaml                         # Docker编排文件
├── Dockerfile                                  # Docker镜像构建
└── pom.xml                                     # Maven依赖配置
```

## 🚀 快速开始

### 环境要求
- **JDK**: 17或更高版本
- **Maven**: 3.6或更高版本  
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **Docker**: 20.0+ (可选)

### 1. 克隆项目
```bash
git clone <repository-url>
cd online_store
```

### 2. 数据库初始化
```sql
# 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 创建用户（可选）
CREATE USER 'online_store'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON online_store.* TO 'online_store'@'localhost';
FLUSH PRIVILEGES;
```

### 3. 配置文件
修改 `src/main/resources/application.yaml` 中的配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password

jwt:
  secret: your_jwt_secret_key
```

### 4. 运行方式

#### 方式一：直接运行
```bash
# 启动MySQL和Redis服务
sudo systemctl start mysql redis

# 编译并运行
mvn clean compile
mvn spring-boot:run
```

#### 方式二：使用Docker Compose
```bash
# 启动数据库服务
docker-compose --profile all up -d

# 运行应用
mvn spring-boot:run
```

#### 方式三：完全容器化
```bash
# 构建镜像
docker build -t online-store .

# 启动所有服务
docker-compose --profile all up -d
```

### 5. 验证运行
- 应用地址: http://localhost:8080
- 健康检查: http://localhost:8080/actuator/health
- 默认管理员账号: admin/admin123

## 📚 API文档

### 认证相关
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册

### 商品管理
- `GET /api/items` - 获取商品列表
- `GET /api/items/{id}` - 获取商品详情
- `POST /api/items` - 创建商品
- `PUT /api/items/{id}` - 更新商品
- `DELETE /api/items/{id}` - 删除商品

### 分类管理  
- `GET /api/categories` - 获取分类列表
- `POST /api/categories` - 创建分类

### 品牌管理
- `GET /api/brands` - 获取品牌列表
- `POST /api/brands` - 创建品牌

## 🔧 开发指南

### 代码规范
- 使用Lombok减少样板代码
- 遵循RESTful API设计原则
- 统一异常处理和响应格式
- 使用分页查询优化性能

### 数据库设计
- 支持商品多SKU
- 动态属性配置
- 访问日志记录
- 索引优化

### 缓存策略
- Redis缓存热点数据
- 合理设置过期时间
- 缓存穿透保护

## 🚢 部署指南

### 生产环境配置
1. 设置环境变量：
```bash
export SPRING_PROFILES_ACTIVE=prod
export JWT_SECRET=your_production_secret
export MYSQL_PASSWORD=your_production_password
```

2. 使用外部配置中心（Nacos）：
```bash
export NACOS_ENABLED=true
```

3. 启用HTTPS和安全配置

### 监控和运维
- 使用Spring Boot Actuator进行应用监控
- 集成日志收集系统
- 配置告警规则

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支: `git checkout -b feature/AmazingFeature`
3. 提交更改: `git commit -m 'Add some AmazingFeature'`
4. 推送分支: `git push origin feature/AmazingFeature`
5. 提交Pull Request

## 📄 许可证

本项目采用 MIT 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

## 📞 联系方式

如有问题或建议，请提交Issue或联系项目维护者。

---

⭐ 如果这个项目对您有帮助，请给它一个星标！ 