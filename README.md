# 🛒 Online Store - 在线商城系统

一个基于 Spring Cloud 微服务架构的现代化在线商城系统，提供完整的电商业务功能，包括商品管理、用户认证、购物车、订单处理等核心模块。

## ✨ 功能特性

### 核心功能
- 🔐 **用户认证与授权** - 基于 JWT 的安全认证机制
- 📦 **商品管理** - 商品分类、品牌、属性、SKU 管理
- 🛒 **购物车功能** - 支持商品添加、修改、删除
- 📊 **商品分析** - 商品访问日志记录与分析
- 🏷️ **多级分类** - 支持商品多级分类体系
- 🔍 **属性筛选** - 灵活的商品属性配置与筛选

### 技术特性
- 🏗️ **微服务架构** - 基于 Spring Cloud 的分布式系统设计
- 🚀 **高性能缓存** - Redis 缓存提升系统响应速度
- 📱 **RESTful API** - 标准化的 API 接口设计
- 🐳 **容器化部署** - 支持 Docker 容器化部署
- 🌐 **国际化支持** - 多语言国际化配置
- 📈 **监控与健康检查** - Spring Boot Actuator 监控

## 🛠️ 技术栈

### 后端技术
- **核心框架**: Spring Boot 3.4.3, Spring Cloud 2024.0.0
- **数据库**: MySQL 8.2.0 + MyBatis 3.0.3
- **缓存**: Redis + Jedis 5.2.0
- **安全**: Spring Security + JWT (jjwt 0.11.5)
- **服务发现**: Nacos 2.2.0
- **分页**: PageHelper 2.1.0
- **对象存储**: 阿里云 OSS 3.18.1
- **工具库**: Lombok, Apache Commons Lang3, CGLib

### 开发环境
- **JDK**: 17+
- **构建工具**: Maven 3.6+
- **容器**: Docker & Docker Compose
- **数据库**: MySQL 8.0+
- **缓存**: Redis 6.0+

## 📁 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # 主启动类
│   │   │   ├── bean/                          # Bean 配置
│   │   │   ├── config/                        # 配置类
│   │   │   ├── constants/                     # 常量定义
│   │   │   ├── controller/                    # REST 控制器
│   │   │   ├── dto/                          # 数据传输对象
│   │   │   ├── entity/                       # 实体类
│   │   │   ├── enums/                        # 枚举类
│   │   │   ├── exceptions/                   # 异常处理
│   │   │   ├── handler/                      # 处理器
│   │   │   ├── mapper/                       # MyBatis 映射器
│   │   │   ├── security/                     # 安全配置
│   │   │   ├── service/                      # 业务逻辑层
│   │   │   └── utils/                        # 工具类
│   │   └── resources/
│   │       ├── application.yaml              # 主配置文件
│   │       ├── application-local.yaml        # 本地环境配置
│   │       ├── bootstrap.yaml                # 启动配置
│   │       ├── i18n/                         # 国际化资源
│   │       ├── mapper/                       # MyBatis XML 映射
│   │       └── sql/                          # 数据库初始化脚本
│   └── test/                                  # 测试代码
├── scripts/                                   # 构建脚本
├── docker-compose.yaml                        # Docker Compose 配置
├── Dockerfile                                 # Docker 镜像构建
└── pom.xml                                    # Maven 依赖配置
```

## 🚀 快速开始

### 环境要求

确保您的开发环境满足以下要求：

- **Java**: JDK 17 或更高版本
- **Maven**: 3.6 或更高版本  
- **MySQL**: 8.0 或更高版本
- **Redis**: 6.0 或更高版本
- **Docker**: (可选) 用于容器化部署

### 1. 克隆项目

```bash
git clone <repository-url>
cd online_store
```

### 2. 数据库初始化

#### 创建数据库
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 执行初始化脚本
```bash
# 进入 MySQL 命令行
mysql -u root -p online_store

# 执行所有初始化脚本
source src/main/resources/sql/member_table.sql;
source src/main/resources/sql/category_table.sql;
source src/main/resources/sql/brand_table.sql;
source src/main/resources/sql/attribute_table.sql;
source src/main/resources/sql/attribute_value_table.sql;
source src/main/resources/sql/item_table_table.sql;
source src/main/resources/sql/sku_table.sql;
source src/main/resources/sql/item_attribute_relation_table.sql;
source src/main/resources/sql/item_access_log_table.sql;
```

### 3. 配置文件设置

#### 环境变量配置
```bash
# 设置必要的环境变量
export JWT_SECRET="your-super-secret-jwt-key-here"
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="admin123"
export SPRING_PROFILES_ACTIVE="local"
```

#### 修改配置文件 (可选)
根据您的环境，修改 `src/main/resources/application-local.yaml` 中的数据库和 Redis 连接配置。

### 4. 启动服务

#### 使用 Maven 启动
```bash
# 添加 JVM 参数并启动
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

#### 使用 Docker Compose 启动依赖服务
```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d

# 仅启动 MySQL
docker-compose --profile without-redis up -d
```

### 5. 验证部署

```bash
# 检查应用健康状态
curl http://localhost:8080/actuator/health

# 访问应用主页
curl http://localhost:8080
```

## 🐳 Docker 部署

### 构建镜像
```bash
# 构建应用镜像
docker build -t online-store:latest .
```

### 完整部署
```bash
# 启动所有服务
docker-compose --profile all up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

## 📚 API 文档

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/logout` - 用户登出

### 商品管理
- `GET /api/items` - 获取商品列表
- `GET /api/items/{id}` - 获取商品详情
- `POST /api/items` - 创建商品
- `PUT /api/items/{id}` - 更新商品
- `DELETE /api/items/{id}` - 删除商品

### 分类管理
- `GET /api/categories` - 获取分类列表
- `GET /api/categories/{id}` - 获取分类详情

### 品牌管理
- `GET /api/brands` - 获取品牌列表
- `GET /api/brands/{id}` - 获取品牌详情

## ⚙️ 配置说明

### 核心配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `server.port` | 应用端口 | 8080 |
| `spring.datasource.url` | 数据库连接 | localhost:3306/online_store |
| `spring.data.redis.host` | Redis 主机 | localhost |
| `jwt.secret` | JWT 密钥 | 环境变量 |
| `jwt.expiration` | JWT 过期时间(秒) | 86400 |

### 环境配置

- **local**: 本地开发环境
- **dev**: 开发环境
- **prod**: 生产环境

## 🔧 开发指南

### 代码规范
- 使用 Lombok 减少样板代码
- 遵循 RESTful API 设计规范
- 统一异常处理和返回格式
- 使用 MyBatis 进行数据库操作

### 测试
```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn verify
```

## 🚨 故障排除

### 常见问题

1. **启动失败**: 检查 JWT_SECRET 环境变量是否设置
2. **数据库连接失败**: 确认 MySQL 服务运行状态和连接配置
3. **Redis 连接失败**: 检查 Redis 服务状态
4. **端口冲突**: 修改 application.yaml 中的端口配置

### 日志查看
```bash
# 查看应用日志
tail -f logs/online-store.log

# Docker 环境日志
docker-compose logs -f online-store
```

## 📝 更新日志

### v1.0.0 (当前版本)
- ✅ 基础的商品管理功能
- ✅ 用户认证与授权
- ✅ Redis 缓存集成
- ✅ Docker 容器化支持
- ✅ 国际化支持
- ✅ API 监控与健康检查

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系我们

如有问题或建议，请提交 Issue 或联系开发团队。

---

⭐ 如果这个项目对您有帮助，请给我们一个 Star！