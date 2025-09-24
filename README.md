# 📦 Online Store

一个基于Spring Boot 3.x + Spring Cloud + Nacos的现代化在线商店系统，支持微服务架构，提供完整的电商业务功能。

## ✨ 主要特性

- 🛒 **完整电商功能**：商品管理、订单处理、用户认证、支付集成
- 🔐 **安全认证**：JWT Token + Spring Security
- 🌐 **微服务架构**：Nacos服务发现与配置管理
- 📊 **数据持久化**：MyBatis + MySQL + Redis缓存
- 🐳 **容器化部署**：Docker + Docker Compose支持
- 📈 **监控运维**：Spring Boot Actuator健康检查
- 🔧 **分页支持**：PageHelper分页插件
- 🌍 **国际化**：多语言支持

## 🛠 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| **JDK** | 17 | Java开发环境 |
| **Spring Boot** | 3.4.3 | 核心框架 |
| **Spring Cloud** | 2024.0.0 | 微服务框架 |
| **Spring Cloud Alibaba** | 2022.0.0.0 | 阿里云组件 |
| **Nacos** | 2.2.0 | 服务发现与配置中心 |
| **MyBatis** | 3.0.3 | ORM框架 |
| **PageHelper** | 2.1.0 | 分页插件 |
| **MySQL** | 8.2.0 | 关系型数据库 |
| **Redis** | Jedis 5.2.0 | 缓存数据库 |
| **JWT** | 0.11.5 | 认证令牌 |
| **Spring Security** | - | 安全框架 |
| **Docker** | - | 容器化技术 |

## 📁 项目结构

```
online-store/
├── 📄 README.md                    # 项目说明文档
├── 📄 pom.xml                       # Maven配置文件
├── 🐳 Dockerfile                    # Docker镜像构建文件
├── 🐳 docker-compose.yaml          # Docker Compose配置
├── 📁 scripts/                     # 辅助脚本目录
│   ├── 📄 main.py                  # 测试数据生成脚本
│   └── 📄 README.md                # 脚本说明文档
└── 📁 src/
    ├── 📁 main/
    │   ├── 📁 java/com/example/onlinestore/
    │   │   ├── 📄 OnlineStoreApplication.java  # 启动类
    │   │   ├── 📁 bean/              # Bean配置
    │   │   ├── 📁 config/            # 配置类
    │   │   ├── 📁 controller/        # 控制器层
    │   │   ├── 📁 dto/               # 数据传输对象
    │   │   ├── 📁 entity/            # 实体类
    │   │   ├── 📁 mapper/            # 数据访问层
    │   │   ├── 📁 service/           # 业务逻辑层
    │   │   ├── 📁 security/          # 安全认证
    │   │   ├── 📁 utils/             # 工具类
    │   │   ├── 📁 enums/             # 枚举定义
    │   │   ├── 📁 constants/         # 常量定义
    │   │   ├── 📁 exceptions/        # 异常处理
    │   │   └── 📁 handler/           # 处理器
    │   └── 📁 resources/
    │       ├── 📄 application.yaml        # 主配置文件
    │       ├── 📄 application-local.yaml  # 本地环境配置
    │       ├── 📄 bootstrap.yaml          # 启动配置
    │       ├── 📁 mapper/                  # MyBatis映射文件
    │       ├── 📁 sql/                     # SQL脚本
    │       └── 📁 i18n/                    # 国际化资源
    └── 📁 test/                      # 测试代码
```

## 🚀 快速开始

### 📋 环境要求

- **JDK 17+** - Java开发环境
- **Maven 3.6+** - 项目构建工具
- **MySQL 8.0+** - 数据库
- **Redis 6.0+** - 缓存服务
- **Docker** (可选) - 容器化部署

### 🐳 Docker快速启动 (推荐)

1. **启动基础服务**：
```bash
# 启动MySQL和Redis
docker-compose --profile all up -d

# 或者只启动MySQL
docker-compose --profile without-redis up -d
```

2. **数据库初始化**：
```sql
-- 连接到MySQL (用户名: root, 密码: 123456)
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **运行应用**：
```bash
mvn spring-boot:run
```

### 🔧 手动安装配置

1. **克隆项目**：
```bash
git clone <repository-url>
cd online-store
```

2. **数据库配置**：
```sql
# 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 执行初始化SQL脚本 (位于 src/main/resources/sql/)
```

3. **配置文件修改**：
编辑 `src/main/resources/application.yaml`，根据实际环境修改数据库和Redis连接信息：
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

4. **环境变量配置**：
```bash
# JWT密钥 (必须设置)
export JWT_SECRET=your-secret-key-here

# 可选配置
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
export SPRING_PROFILES_ACTIVE=local
export NACOS_ENABLED=false
```

5. **运行应用**：
```bash
# 方式1: 使用Maven
mvn clean spring-boot:run

# 方式2: 打包后运行
mvn clean package
java -jar --add-opens java.base/java.lang=ALL-UNNAMED target/online-store-1.0-SNAPSHOT.jar
```

## 📖 API文档

应用启动后，可以通过以下地址访问：

- **应用主页**: http://localhost:8080
- **健康检查**: http://localhost:8080/actuator/health
- **应用信息**: http://localhost:8080/actuator/info

### 🔐 默认账户

- **用户名**: admin
- **密码**: admin123

## 🔧 开发配置

### 📊 生成测试数据

```bash
cd scripts
python main.py
```

### 🏗 构建Docker镜像

```bash
docker build -t online-store .
```

### 🔍 日志配置

项目使用Spring Boot默认日志配置，支持不同环境的日志级别设置。

## 🌍 部署

### 📦 生产环境部署

1. **构建生产包**：
```bash
mvn clean package -Pprod
```

2. **环境变量配置**：
```bash
export SPRING_PROFILES_ACTIVE=prod
export JWT_SECRET=your-production-secret
export MYSQL_URL=jdbc:mysql://prod-mysql:3306/online_store
export REDIS_HOST=prod-redis
```

3. **使用Docker部署**：
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## 🤝 贡献

欢迎提交Issue和Pull Request来帮助改进项目。

## 📄 许可证

本项目采用MIT许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 📧 邮箱: [your-email@example.com]
- 🐛 问题反馈: [GitHub Issues]

---

⭐ 如果这个项目对你有帮助，请给个Star支持一下！ 