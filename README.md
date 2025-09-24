# 🛍️ Online Store - Spring Cloud Microservices E-commerce Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2022.0.4-blue.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)

> A modern online store platform based on Spring Cloud microservices architecture, built with the latest Java 17 and Spring Boot 3.x technology stack.

## Technology Stack

- JDK 17
- Spring Cloud 2022.0.4
- Spring Boot 3.1.5
- MyBatis 3.0.2
- MySQL 8.0
- Redis (Jedis 4.3.1)

## Project Structure

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

## Requirements

- JDK 17 or higher
- Maven 3.6 or higher
- MySQL 8.0
- Redis 6.0 or higher

## How to Run

1. Ensure MySQL and Redis services are running
2. Create database:
```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
3. Modify database and Redis configuration in `application.yml`
4. Add VM parameter: `--add-opens java.base/java.lang=ALL-UNNAMED`
5. Run the application:
```bash
mvn spring-boot:run
``` 