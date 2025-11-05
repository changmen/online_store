# 🛒 Online Store - 基于Spring Cloud的现代化电商平台

## 📖 项目简介

这是一个基于Spring Cloud微服务架构的现代化电商平台，采用最新的Java技术栈构建。项目提供完整的在线购物解决方案，包括商品管理、用户管理、订单处理等核心功能，适合作为企业级电商系统的基础架构或学习Spring Cloud微服务技术的实战项目。

本项目注重代码质量和架构设计，遵循最佳实践，为开发者提供了一个高质量的参考实现。

## ✨ 核心特性

- 🎯 **微服务架构**：基于Spring Cloud构建的分布式系统架构
- 🔐 **安全认证**：集成JWT token认证和授权机制
- 💾 **数据持久化**：使用MyBatis进行数据库操作，支持MySQL
- ⚡ **缓存优化**：集成Redis缓存提升系统性能
- 🔍 **商品搜索**：支持商品分类、品牌、属性等多维度搜索
- 👥 **用户管理**：完整的用户注册、登录、个人信息管理功能
- 🛍️ **商品管理**：支持商品、SKU、属性等电商核心实体管理
- 📊 **访问统计**：商品访问日志记录和统计分析
- 🌐 **国际化支持**：多语言配置支持
- 📱 **API友好**：RESTful API设计，支持前后端分离

## 🏗️ 技术架构

### 技术栈

- **核心框架**：Spring Cloud 2022.0.4、Spring Boot 3.1.5
- **开发语言**：Java 17
- **数据库**：MySQL 8.0
- **缓存**：Redis 6.0+ (Jedis 4.3.1)
- **ORM框架**：MyBatis 3.0.2
- **构建工具**：Maven 3.6+
- **安全框架**：Spring Security + JWT
- **文档工具**：Spring Boot Actuator

### 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # 应用启动类
│   │   │   ├── bean/                          # 业务对象
│   │   │   ├── config/                        # 配置类
│   │   │   ├── controller/                    # 控制器层
│   │   │   ├── dto/                          # 数据传输对象
│   │   │   ├── entity/                       # 实体类
│   │   │   ├── service/                      # 服务层
│   │   │   ├── mapper/                       # 数据访问层
│   │   │   ├── security/                     # 安全相关
│   │   │   ├── utils/                        # 工具类
│   │   │   ├── enums/                        # 枚举类
│   │   │   ├── exceptions/                   # 异常处理
│   │   │   └── handler/                      # 全局处理器
│   │   └── resources/
│   │       ├── application.yaml              # 应用配置
│   │       ├── mapper/                       # MyBatis映射文件
│   │       ├── sql/                          # 数据库脚本
│   │       └── i18n/                         # 国际化资源
│   └── test/                                 # 测试代码
├── scripts/                                  # 脚本工具
├── docker-compose.yaml                       # Docker编排文件
├── pom.xml                                   # Maven配置
└── README.md                                 # 项目文档
```

## 🚀 快速开始

### 环境要求

在开始之前，请确保您的开发环境满足以下要求：

- **JDK**：17或更高版本
- **Maven**：3.6或更高版本  
- **MySQL**：8.0或更高版本
- **Redis**：6.0或更高版本
- **IDE**：推荐使用IntelliJ IDEA或Eclipse

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd online_store
   ```

2. **启动基础服务**
   ```bash
   # 使用Docker Compose启动MySQL和Redis
   docker-compose up -d mysql redis
   ```

3. **初始化数据库**
   ```sql
   # 连接MySQL并创建数据库
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # 执行初始化脚本（位于src/main/resources/sql/目录）
   ```

4. **配置应用**
   - 复制`application-local.yaml.template`为`application-local.yaml`
   - 修改数据库和Redis连接配置
   - 根据需要调整其他配置项

### 运行应用

1. **编译项目**
   ```bash
   mvn clean compile
   ```

2. **启动应用**
   ```bash
   # 添加JVM参数以支持Java 17
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
   ```

3. **验证启动**
   - 访问健康检查端点：`http://localhost:8080/actuator/health`
   - 查看API文档：`http://localhost:8080/swagger-ui.html`（如果配置了Swagger）

## 📚 开发指南

### 本地开发

1. **开发环境配置**
   - 使用`application-local.yaml`配置本地环境
   - 配置IDE的代码格式化规则
   - 安装必要的IDE插件（如MyBatis插件）

2. **代码规范**
   - 遵循Google Java代码规范
   - 使用统一的包命名和类命名规范
   - 确保所有public方法都有适当的JavaDoc注释

### 测试运行

```bash
# 运行单元测试
mvn test

# 运行集成测试
mvn integration-test

# 生成测试报告
mvn surefire-report:report
```

### 部署说明

1. **生产环境配置**
   - 使用`application-prod.yaml`配置生产环境
   - 配置外部数据库和Redis连接
   - 设置适当的JVM参数和内存配置

2. **Docker部署**
   ```bash
   # 构建Docker镜像
   mvn spring-boot:build-image
   
   # 使用Docker Compose部署完整应用
   docker-compose up -d
   ```

## 🤝 贡献指南

我们欢迎社区贡献！请按照以下步骤参与项目：

1. Fork本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

请确保您的代码符合项目的编码规范，并包含适当的测试。

## 📄 许可证

本项目采用MIT许可证。详情请参见[LICENSE](LICENSE)文件。 