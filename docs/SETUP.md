# 📋 环境配置指南

## 📦 开发环境要求

### 必需软件
- **Java**: JDK 17+ 
- **构建工具**: Maven 3.6+
- **数据库**: MySQL 8.0+
- **缓存**: Redis 6.0+
- **IDE**: IntelliJ IDEA 2023+ / Eclipse 2023+ / VS Code

### 可选软件
- **容器**: Docker 20.10+ & Docker Compose 2.0+
- **版本控制**: Git 2.30+

## 🚀 快速启动

### 方式一：Docker Compose 启动（推荐）

1. **启动基础服务**
```bash
# 启动 MySQL + Redis
docker-compose --profile all up -d

# 或者只启动 MySQL
docker-compose --profile without-redis up -d
```

2. **初始化数据库**
```bash
# 连接到 MySQL 容器
docker exec -it online_store_mysql_1 mysql -uroot -p123456

# 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入初始化脚本
USE online_store;
SOURCE /var/lib/mysql/init.sql;  # 需要将 init.sql 挂载到容器中
```

3. **配置环境变量**
```bash
# 复制环境变量模板
cp .env.example .env

# 编辑环境变量
vim .env
```

4. **启动应用**
```bash
mvn spring-boot:run
```

### 方式二：本地环境启动

1. **安装和启动 MySQL**
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql

# macOS (使用 Homebrew)
brew install mysql
brew services start mysql

# Windows
# 下载 MySQL 安装包并按照向导安装
```

2. **安装和启动 Redis**
```bash
# Ubuntu/Debian
sudo apt install redis-server
sudo systemctl start redis
sudo systemctl enable redis

# macOS (使用 Homebrew)
brew install redis
brew services start redis

# Windows
# 下载 Redis for Windows 或使用 WSL
```

3. **创建数据库和用户**
```sql
-- 连接到 MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户并授权（可选，生产环境推荐）
CREATE USER 'online_store'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON online_store.* TO 'online_store'@'localhost';
FLUSH PRIVILEGES;

-- 导入初始化脚本
USE online_store;
SOURCE src/main/resources/sql/init.sql;
```

4. **配置应用**
```bash
# 复制配置文件
cp src/main/resources/application-local.yaml.example src/main/resources/application-local.yaml

# 修改数据库连接信息
vim src/main/resources/application-local.yaml
```

5. **启动应用**
```bash
# 设置环境变量
export JWT_SECRET="your-256-bit-secret-key-here-must-be-long-enough"
export MYSQL_PASSWORD="your_mysql_password"

# 启动应用
mvn spring-boot:run
```

## 🔧 环境变量说明

### 必需环境变量
| 变量名 | 描述 | 示例值 |
|--------|------|--------|
| `JWT_SECRET` | JWT签名密钥（至少256位） | `your-very-long-secret-key-for-jwt-signing-must-be-at-least-256-bits` |
| `MYSQL_PASSWORD` | MySQL数据库密码 | `123456` |

### 可选环境变量
| 变量名 | 描述 | 默认值 |
|--------|------|--------|
| `SPRING_PROFILES_ACTIVE` | 激活的配置文件 | `local` |
| `ADMIN_USERNAME` | 管理员用户名 | `admin` |
| `ADMIN_PASSWORD` | 管理员密码 | `admin123` |
| `NACOS_ENABLED` | 是否启用Nacos | `false` |

## 📝 配置文件说明

### application.yaml
主配置文件，包含通用配置和生产环境配置。

### application-local.yaml
本地开发环境配置，包含：
- 本地数据库连接信息
- Redis连接配置
- JWT配置
- Jackson序列化配置

### bootstrap.yaml
引导配置文件，用于Nacos配置中心集成。

## 🗄️ 数据库配置

### 连接配置
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: ${MYSQL_PASSWORD:123456}
```

### MyBatis配置
```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.onlinestore.model
  configuration:
    map-underscore-to-camel-case: true
```

## 🔄 Redis配置

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: 
      database: 0
      jedis:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
          max-wait: -1ms
```

## 🔐 安全配置

### JWT配置
```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400  # 24小时（秒）
```

### Spring Security
- 登录接口：`/api/v1/members/login`
- 注册接口：`/api/v1/members/registry`
- 监控接口：`/actuator/**`
- 其他接口需要JWT认证

## 🐛 故障排除

### 常见问题

1. **MySQL连接失败**
   - 检查MySQL服务是否启动：`sudo systemctl status mysql`
   - 检查端口是否开放：`netstat -an | grep 3306`
   - 验证用户名密码是否正确

2. **Redis连接失败**
   - 检查Redis服务是否启动：`sudo systemctl status redis`
   - 检查端口是否开放：`netstat -an | grep 6379`

3. **JWT相关错误**
   - 确保JWT_SECRET环境变量已设置且长度足够（建议64字符以上）
   - 检查token是否在请求头中正确设置

4. **Maven编译失败**
   - 检查Java版本：`java -version`
   - 清理Maven缓存：`mvn clean`
   - 更新依赖：`mvn dependency:resolve`

5. **应用启动失败**
   - 检查端口8080是否被占用：`lsof -i :8080`
   - 查看详细日志：`mvn spring-boot:run -X`

### 日志查看
```bash
# 查看应用日志
tail -f logs/online-store.log

# 查看MySQL错误日志
sudo tail -f /var/log/mysql/error.log

# 查看Redis日志
sudo tail -f /var/log/redis/redis-server.log
```

## 📚 开发工具推荐

### IDE插件
- **IntelliJ IDEA**: 
  - Lombok Plugin
  - MyBatis Plugin
  - Spring Boot Assistant
  
- **VS Code**:
  - Extension Pack for Java
  - Spring Boot Extension Pack
  - Lombok Annotations Support

### 数据库工具
- **Navicat**: 专业的MySQL管理工具
- **DBeaver**: 免费的通用数据库工具
- **phpMyAdmin**: Web版MySQL管理工具

### API测试工具
- **Postman**: 专业的API测试工具
- **Insomnia**: 轻量级REST客户端
- **curl**: 命令行HTTP客户端

---

💡 **提示**: 建议使用Docker Compose方式进行开发，可以快速搭建一致的开发环境。