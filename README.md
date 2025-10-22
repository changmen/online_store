# Online Store

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

A modern e-commerce platform built with Spring Cloud microservices architecture, featuring product management, user authentication, and comprehensive RESTful APIs.

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
  - [Using Docker Compose](#using-docker-compose)
  - [Manual Setup](#manual-setup)
- [API Endpoints](#-api-endpoints)
- [Configuration](#-configuration)
- [Database Schema](#-database-schema)
- [Development](#-development)
- [Deployment](#-deployment)
- [Troubleshooting](#-troubleshooting)

## ✨ Features

- **Product Management**: Categories, brands, items, SKUs, and attributes
- **User Management**: Member registration, authentication with JWT
- **Security**: Spring Security integration with JWT token-based authentication
- **Caching**: Redis integration for high-performance data caching
- **Pagination**: Built-in PageHelper for efficient data pagination
- **File Storage**: Aliyun OSS integration for file uploads
- **Service Discovery**: Nacos support for microservices (optional)
- **Monitoring**: Spring Boot Actuator for health checks and metrics
- **RESTful APIs**: Well-designed REST endpoints for all operations

## 🛠 Technology Stack

### Core Framework
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Security**: JWT-based authentication

### Data & Persistence
- **MySQL**: 8.2.0 (Database)
- **MyBatis**: 3.0.3 (ORM Framework)
- **PageHelper**: 2.1.0 (Pagination)
- **Redis**: Jedis 5.2.0 (Caching)

### Microservices & Cloud
- **Nacos**: 2.2.0 (Service Discovery & Configuration)
- **Spring Cloud Alibaba**: 2022.0.0.0

### Utilities
- **Lombok**: 1.18.36 (Code Generation)
- **JWT**: 0.11.5 (Authentication Tokens)
- **Apache Commons**: Lang3 & Collections
- **Aliyun OSS**: 3.18.1 (Object Storage)

## 📁 Project Structure

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # Application entry point
│   │   │   ├── controller/                     # REST API controllers
│   │   │   │   ├── AttributeController.java
│   │   │   │   ├── BrandController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ItemController.java
│   │   │   │   ├── ItemDetailController.java
│   │   │   │   └── MemberController.java
│   │   │   ├── service/                        # Business logic layer
│   │   │   ├── mapper/                         # MyBatis data access layer
│   │   │   ├── entity/                         # Database entities
│   │   │   ├── dto/                            # Data transfer objects
│   │   │   ├── config/                         # Configuration classes
│   │   │   ├── security/                       # Security & JWT filters
│   │   │   ├── exceptions/                     # Custom exceptions
│   │   │   ├── enums/                          # Enumeration types
│   │   │   ├── utils/                          # Utility classes
│   │   │   └── constants/                      # Application constants
│   │   └── resources/
│   │       ├── application.yaml                # Main configuration
│   │       ├── mapper/                         # MyBatis XML mappers
│   │       └── sql/                            # Database schema scripts
│   │           ├── member_table.sql
│   │           ├── item_table_table.sql
│   │           ├── category_table.sql
│   │           ├── brand_table.sql
│   │           ├── attribute_table.sql
│   │           └── ...
│   └── test/                                   # Unit and integration tests
├── scripts/                                    # Utility scripts
├── docker-compose.yaml                         # Docker Compose configuration
├── Dockerfile                                  # Docker image definition
├── pom.xml                                     # Maven dependencies
└── README.md                                   # This file
```

## 📦 Prerequisites

Before running this application, ensure you have the following installed:

- **JDK 17** or higher
- **Maven 3.6+** for dependency management
- **MySQL 8.0+** for the database
- **Redis 6.0+** for caching
- **Docker & Docker Compose** (optional, for containerized setup)

## 🚀 Getting Started

### Using Docker Compose

The fastest way to get started is using Docker Compose to run MySQL and Redis:

```bash
# Start MySQL and Redis
docker-compose --profile all up -d

# Verify services are running
docker-compose ps
```

### Manual Setup

#### 1. Clone the Repository

```bash
git clone <repository-url>
cd online-store
```

#### 2. Set Up Database

```sql
-- Connect to MySQL
mysql -u root -p

-- Create database
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE online_store;

-- Execute schema scripts
source src/main/resources/sql/member_table.sql;
source src/main/resources/sql/item_table_table.sql;
source src/main/resources/sql/category_table.sql;
source src/main/resources/sql/brand_table.sql;
source src/main/resources/sql/attribute_table.sql;
source src/main/resources/sql/attribute_value_table.sql;
source src/main/resources/sql/item_attribute_relation_table.sql;
source src/main/resources/sql/sku_table.sql;
source src/main/resources/sql/item_access_log_table.sql;
```

#### 3. Configure Application

Update `src/main/resources/application.yaml` with your settings:

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
      password: your_redis_password  # Leave empty if no password

jwt:
  secret: your_jwt_secret_key_here  # IMPORTANT: Set this environment variable
```

Or use environment variables:

```bash
export JWT_SECRET="your-secret-key-min-256-bits"
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="admin123"
```

#### 4. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Or with VM arguments
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

#### 5. Verify Installation

The application will start on `http://localhost:8080`. Check the health endpoint:

```bash
curl http://localhost:8080/actuator/health
```

## 📡 API Endpoints

### Member Management
- `POST /members` - Register new member
- `GET /members/{id}` - Get member details
- `PUT /members/{id}` - Update member information

### Category Management
- `GET /categories` - List all categories
- `POST /categories` - Create new category
- `PUT /categories/{id}` - Update category

### Brand Management
- `GET /brands` - List all brands
- `POST /brands` - Create new brand
- `GET /brands/{id}` - Get brand details
- `PUT /brands/{id}` - Update brand

### Item Management
- `GET /items` - List items with pagination
- `POST /items` - Create new item
- `GET /items/{id}` - Get item details
- `PUT /items/{id}` - Update item

### Attribute Management
- `GET /attributes` - List all attributes
- `POST /attributes` - Create new attribute
- `PUT /attributes/{id}` - Update attribute

## ⚙️ Configuration

### Profiles

The application supports different profiles:

- `local` - Local development (default)
- `dev` - Development environment
- `prod` - Production environment

Set the active profile:

```bash
export SPRING_PROFILES_ACTIVE=dev
```

### Nacos Configuration

To enable Nacos service discovery:

```bash
export NACOS_ENABLED=true
```

Update Nacos server address in `application.yaml` if needed.

## 🗄️ Database Schema

Key database tables:

- **member** - User account information
- **item_table** - Product items
- **category** - Product categories
- **brand** - Product brands
- **attribute** - Product attributes
- **attribute_value** - Attribute values
- **sku** - Stock keeping units
- **item_attribute_relation** - Item-Attribute relationships
- **item_access_log** - Item access tracking

All SQL schema files are located in `src/main/resources/sql/`.

## 💻 Development

### Running Tests

```bash
mvn test
```

### Code Style

This project uses Lombok to reduce boilerplate code. Make sure Lombok plugin is installed in your IDE.

### Hot Reload

For development, add Spring Boot DevTools dependency for automatic restart.

## 🐳 Deployment

### Using Docker

```bash
# Build Docker image
docker build -t online-store:latest .

# Run container
docker run -d \
  -p 8080:8080 \
  -e JWT_SECRET="your-secret" \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/online_store" \
  online-store:latest
```

### Production Checklist

- [ ] Set strong `JWT_SECRET` environment variable
- [ ] Configure proper database credentials
- [ ] Enable HTTPS/TLS
- [ ] Set up database backups
- [ ] Configure Redis password
- [ ] Review and set security configurations
- [ ] Enable application monitoring
- [ ] Set up logging aggregation

## 🔧 Troubleshooting

### Common Issues

**Issue**: `java.lang.IllegalAccessError` related to reflection

**Solution**: Add JVM argument: `--add-opens java.base/java.lang=ALL-UNNAMED`

---

**Issue**: Cannot connect to MySQL

**Solution**: 
- Verify MySQL is running: `mysql -u root -p`
- Check connection string in `application.yaml`
- Ensure database `online_store` exists

---

**Issue**: JWT token errors

**Solution**: Ensure `JWT_SECRET` environment variable is set and is at least 256 bits

---

**Issue**: Redis connection refused

**Solution**: 
- Verify Redis is running: `redis-cli ping`
- Check Redis host and port in configuration

---

**Issue**: Port 8080 already in use

**Solution**: Change server port in `application.yaml` or kill the process using port 8080

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

## 📄 License

This project is licensed under the MIT License.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

**For more information, please contact the development team.** 