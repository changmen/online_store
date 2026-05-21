# Online Store

基于 Spring Cloud 的在线商店后端服务项目。

## 技术栈

- JDK 17
- Spring Boot 3.4.3
- Spring Cloud 2024.0.0
- Spring Cloud Alibaba 2022.0.0.0
- MyBatis 3.0.2
- MySQL 8.2.0
- Redis (Jedis 5.2.0)
- Nacos 2.2.0（服务注册与配置中心）
- Aliyun OSS（对象存储）
- Apache Commons Lang3 3.17.0

## 功能模块

| 模块 | 说明 | API 路径 |
|------|------|----------|
| 商品 (Item) | 商品及 SKU 管理 | `/api/items` |
| 商品详情 (ItemDetail) | 商品详情页数据聚合 | `/api/items/{id}/detail` |
| 订单 (Order) | 订单创建、支付、发货、确认、取消 | `/api/orders` |
| 收货地址 (Address) | 地址增删改查、默认地址设置 | `/api/addresses` |
| 类目 (Category) | 商品类目及属性管理 | `/api/categories` |
| 评论 (Comment) | 商品评论及审核 | `/api/comments` |
| 用户 (User) | 用户管理及认证 | `/api/users` |
| 购物车 (Cart) | 购物车操作 | `/api/cart` |
| 统计 (Statistics) | 商品及用户统计 | `/api/stats` |
| 认证 (Auth) | 登录及 Token 管理 | `/api/auth` |
| 价格计算 | 商品价格计算 | `/api/price` |

## 项目结构

```
online_store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java   # 启动类
│   │   │   ├── actuator/                     # 自定义监控端点
│   │   │   ├── annotation/                   # 自定义注解（@RequireAdmin, @ValidateParams）
│   │   │   ├── aspect/                       # AOP 切面（权限校验、参数验证）
│   │   │   ├── bean/                         # 业务 Bean 对象
│   │   │   ├── cache/                        # 缓存管理（Redis + Local）
│   │   │   ├── config/                       # 配置类
│   │   │   ├── constants/                    # 常量定义
│   │   │   ├── context/                      # 上下文（UserContext）
│   │   │   ├── controller/                   # REST 控制器
│   │   │   ├── dto/                          # 数据传输对象
│   │   │   ├── entity/                       # 数据实体
│   │   │   ├── enums/                        # 枚举类
│   │   │   ├── exception/                    # 自定义异常
│   │   │   ├── handler/                      # 全局异常处理
│   │   │   ├── hook/                         # 评论 Hook 机制
│   │   │   ├── interceptor/                  # 认证拦截器
│   │   │   ├── mapper/                       # MyBatis Mapper 接口
│   │   │   └── service/                      # 服务层接口及实现
│   │   └── resources/
│   │       ├── application.yml               # 主配置文件
│   │       └── mapper/                       # MyBatis XML 映射文件
│   └── test/java/com/example/onlinestore/    # 单元测试
├── pom.xml
└── README.md
```

## 运行要求

- JDK 17 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0+
- Redis 6.0+
- Nacos 2.2.0（可选，用于服务注册与配置管理）

## 快速开始

1. 确保 MySQL、Redis 服务已启动
2. 创建数据库：
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
3. 修改 `application.yml` 中的数据库、Redis 和 Nacos 配置
4. 添加 VM 参数：`--add-opens java.base/java.lang=ALL-UNNAMED`
5. 启动应用：
```bash
mvn spring-boot:run
```

## Actuator 端点

项目提供了自定义监控端点：

- `/actuator/systemInfo` — 系统信息
- `/actuator/databaseInfo` — 数据库连接信息
- `/actuator/configInfo` — 配置信息
