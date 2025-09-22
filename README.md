# 🛒 Online Store - 在线商城系统

基于 Spring Boot 3 + Spring Cloud 的现代化电商后端系统，提供完整的商品管理、用户管理、分类管理等功能。

## ✨ 主要功能

- 🛍️ **商品管理**：商品信息管理、SKU管理、库存管理
- 👥 **用户管理**：用户注册、登录认证、JWT令牌管理
- 📂 **分类管理**：商品分类、品牌管理
- 🏷️ **属性管理**：商品属性、属性值管理
- 📊 **访问日志**：商品访问统计和日志记录
- 🔒 **安全认证**：基于JWT的安全认证机制
- 🌐 **国际化**：支持中英文多语言

## 🛠 技术栈

### 核心框架
- **JDK 17** - Java开发环境
- **Spring Boot 3.4.3** - 基础框架
- **Spring Cloud 2024.0.0** - 微服务框架
- **Spring Security** - 安全框架
- **MyBatis 3.0.3** - ORM框架

### 数据存储
- **MySQL 8.2.0** - 关系型数据库
- **Redis 5.2.0** - 缓存数据库

### 其他组件
- **Nacos** - 服务发现与配置中心
- **JWT** - 身份认证
- **Lombok** - 代码简化
- **PageHelper** - 分页插件
- **Aliyun OSS** - 对象存储服务

## 📁 项目结构

```
online-store/
├── src/main/java/com/example/onlinestore/
│   ├── bean/              # 数据传输对象
│   ├── config/            # 配置类
│   ├── controller/        # REST控制器
│   │   ├── AttributeController.java    # 属性管理
│   │   ├── BrandController.java        # 品牌管理
│   │   ├── CategoryController.java     # 分类管理
│   │   ├── ItemController.java         # 商品管理
│   │   ├── ItemDetailController.java   # 商品详情
│   │   └── MemberController.java       # 用户管理
│   ├── dto/               # 数据传输对象
│   ├── entity/            # 数据库实体
│   ├── enums/             # 枚举类
│   ├── mapper/            # MyBatis映射器
│   ├── security/          # 安全相关
│   ├── service/           # 业务逻辑层
│   └── utils/             # 工具类
├── src/main/resources/
│   ├── mapper/            # MyBatis XML映射文件
│   ├── sql/               # 数据库初始化脚本
│   ├── i18n/              # 国际化资源文件
│   └── application*.yaml # 配置文件
├── scripts/               # Python脚本工具
├── docker-compose.yaml    # Docker编排文件
└── pom.xml               # Maven依赖配置
```

## 📋 系统要求

- **JDK 17+** - Java开发环境
- **Maven 3.6+** - 项目构建工具
- **MySQL 8.0+** - 数据库服务
- **Redis 6.0+** - 缓存服务

## 🚀 快速开始

### 方式一：Docker 启动（推荐）

1. **启动依赖服务**
```bash
# 启动 MySQL 和 Redis
docker-compose --profile all up -d
```

2. **初始化数据库**
```sql
# 连接MySQL并创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 执行初始化脚本
source src/main/resources/sql/*.sql
```

3. **配置环境变量**
```bash
export JWT_SECRET=your-jwt-secret-key-here
export SPRING_PROFILES_ACTIVE=local
```

4. **启动应用**
```bash
mvn spring-boot:run
```

### 方式二：本地服务启动

1. **启动MySQL和Redis服务**

2. **创建数据库**
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **修改配置文件**
   - 编辑 `src/main/resources/application-local.yaml`
   - 配置数据库连接信息
   - 配置Redis连接信息

4. **设置环境变量**
```bash
export JWT_SECRET=your-very-secure-secret-key-minimum-256-bits
```

5. **运行应用**
```bash
mvn clean spring-boot:run --add-opens java.base/java.lang=ALL-UNNAMED
```

## 📚 API文档

应用启动后，可以通过以下端点访问：

- **应用地址**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health

### 主要API端点

| 模块 | 端点 | 描述 |
|------|------|------|
| 用户管理 | `POST /api/v1/members/registry` | 用户注册 |
| 用户管理 | `POST /api/v1/members/login` | 用户登录 |
| 商品管理 | `GET /api/v1/items/{itemId}` | 获取商品详情 |
| 商品详情 | `GET /api/v1/item-details/{itemId}` | 获取商品详细信息 |
| 分类管理 | `GET /api/v1/categories` | 获取分类列表 |
| 品牌管理 | `GET /api/v1/brands` | 获取品牌列表 |
| 属性管理 | `GET /api/v1/attributes` | 获取属性列表 |

## 🔧 配置说明

### 环境变量

| 变量名 | 描述 | 默认值 | 必填 |
|--------|------|--------|------|
| `JWT_SECRET` | JWT签名密钥 | - | ✅ |
| `SPRING_PROFILES_ACTIVE` | 激活的配置文件 | `local` | ❌ |
| `ADMIN_USERNAME` | 管理员用户名 | `admin` | ❌ |
| `ADMIN_PASSWORD` | 管理员密码 | `admin123` | ❌ |
| `NACOS_ENABLED` | 是否启用Nacos | `false` | ❌ |

### 数据库配置

修改 `src/main/resources/application-local.yaml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### Redis配置

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password
      database: 0
```

## 🗃️ 数据库初始化

项目提供了完整的数据库初始化脚本，位于 `src/main/resources/sql/` 目录：

```bash
# 按顺序执行以下SQL文件
src/main/resources/sql/
├── member_table.sql           # 用户表
├── brand_table.sql            # 品牌表
├── category_table.sql         # 分类表
├── attribute_table.sql        # 属性表
├── attribute_value_table.sql  # 属性值表
├── item_table_table.sql       # 商品表
├── sku_table.sql             # SKU表
├── item_attribute_relation_table.sql  # 商品属性关联表
└── item_access_log_table.sql  # 商品访问日志表
```

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn integration-test

# 生成测试报告
mvn surefire-report:report
```

## 📦 构建部署

### 本地构建

```bash
# 清理并编译
mvn clean compile

# 打包
mvn clean package

# 跳过测试打包
mvn clean package -DskipTests
```

### Docker部署

```bash
# 构建镜像
docker build -t online-store:latest .

# 运行容器
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e JWT_SECRET=your-secret-key \
  online-store:latest
```

## 🛠️ 开发工具

### Python脚本工具

项目包含一些Python工具脚本，位于 `scripts/` 目录：

```bash
cd scripts
pip install -e .
python main.py --help
```

### 推荐IDE插件

- **Lombok Plugin** - 支持Lombok注解
- **MyBatis Plugin** - MyBatis映射文件支持
- **Maven Helper** - Maven依赖管理
- **Spring Boot Helper** - Spring Boot开发支持

## 🔍 故障排除

### 常见问题

1. **JWT_SECRET未设置**
   ```
   错误: Could not resolve placeholder 'JWT_SECRET'
   解决: 设置环境变量 export JWT_SECRET=your-secret-key
   ```

2. **数据库连接失败**
   ```
   错误: Communications link failure
   解决: 检查MySQL服务状态和连接配置
   ```

3. **Redis连接失败**
   ```
   错误: Unable to connect to Redis
   解决: 检查Redis服务状态和连接配置
   ```

4. **端口占用**
   ```
   错误: Port 8080 already in use
   解决: 修改server.port配置或停止占用进程
   ```

### 日志查看

```bash
# 查看应用日志
tail -f logs/online-store.log

# 查看错误日志
tail -f logs/error.log
```

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交变更 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 📧 Email: [your-email@example.com]
- 🐛 Issues: [项目Issues页面]
- 📖 文档: [项目Wiki页面]

---

⭐ 如果这个项目对您有帮助，请给我们一个星标！