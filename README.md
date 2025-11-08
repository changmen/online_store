# Online Store

这是一个基于Spring Cloud的在线商店项目。

## 技术栈

- JDK 17
- Spring Cloud 2024.0.0
- Spring Boot 3.4.3
- MyBatis Spring Boot 3.0.2
- MyBatis 3.0.3
- MySQL 8.2.0
- Redis（Jedis 5.2.0）
- Nacos 2.2.0
- Spring Security + JWT
- Lombok


## 项目结构

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── onlinestore/
│   │   │               ├── OnlineStoreApplication.java
│   │   │               ├── controller/
│   │   │               ├── service/
│   │   │               ├── mapper/
│   │   │               └── entity/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── mapper/
│   └── test/
├── pom.xml
└── README.md
```

## 运行要求

- JDK 17或更高版本
- Maven 3.6或更高版本
- MySQL 8.0
- Redis 6.0或更高版本

## 如何运行

1. 确保MySQL和Redis服务已启动
2. 创建数据库：
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
3. 修改`application.yml`中的数据库和Redis配置
4. 添加vm参数：`--add-opens java.base/java.lang=ALL-UNNAMED` 
5. 运行应用程序：
```bash
mvn spring-boot:run
```

### 使用 Docker Compose 启动依赖
如果本地未安装 MySQL/Redis，可使用项目中的 `docker-compose.yaml` 快速启动：
```bash
# 启动 MySQL + Redis
docker compose --profile all up -d

# 仅启动 MySQL
docker compose --profile without-redis up -d
```

### 打包与以可执行 JAR 运行
```bash
mvn clean package -DskipTests
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```