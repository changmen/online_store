# Online Store

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-brightgreen.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

A modern, cloud-native e-commerce platform built with Spring Cloud microservices architecture, featuring product management, user authentication, and comprehensive RESTful APIs.

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Docker Support](#-docker-support)
- [Development](#-development)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

- 🛒 **Product Management**: Categories, brands, items, SKUs, and attributes
- 👤 **User Management**: Member registration, authentication with JWT
- 🔐 **Security**: Spring Security integration with JWT token-based authentication
- 📊 **Caching**: Redis-based caching for improved performance
- 🔍 **Search & Filter**: Advanced product search and filtering capabilities
- 📈 **Analytics**: Item access logging and tracking
- 🌐 **Cloud Native**: Nacos service discovery and configuration management (optional)
- 📦 **File Storage**: Aliyun OSS integration for media management
- 🎯 **RESTful APIs**: Well-designed REST endpoints with validation

## 🛠 Tech Stack

### Core Framework
- **Java**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0

### Persistence & Caching
- **MySQL**: 8.2.0 (Database)
- **MyBatis**: 3.0.2 (ORM)
- **Redis**: 5.2.0 (Jedis client)
- **PageHelper**: 2.1.0 (Pagination)

### Security & Authentication
- **Spring Security**: 3.4.3
- **JWT (JJWT)**: 0.11.5

### Service Discovery
- **Nacos**: 2.2.0 (Optional, disabled by default)

### Cloud Storage
- **Aliyun OSS**: 3.18.1

### Utilities
- **Lombok**: 1.18.36
- **Apache Commons Lang3**: 3.17.0
- **Apache Commons Collections**: 3.2.2
- **CGLIB**: 3.3.0

## 🏗 Architecture

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # Application entry point
│   │   │   ├── bean/                          # Configuration beans
│   │   │   ├── config/                        # Spring configurations
│   │   │   ├── constants/                     # Application constants
│   │   │   ├── controller/                    # REST controllers
│   │   │   │   ├── AttributeController.java
│   │   │   │   ├── BrandController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ItemController.java
│   │   │   │   ├── ItemDetailController.java
│   │   │   │   └── MemberController.java
│   │   │   ├── dto/                          # Data Transfer Objects
│   │   │   ├── entity/                       # JPA entities
│   │   │   ├── enums/                        # Enumerations
│   │   │   ├── errors/                       # Error handling
│   │   │   ├── exceptions/                   # Custom exceptions
│   │   │   ├── handler/                      # Global exception handlers
│   │   │   ├── mapper/                       # MyBatis mappers
│   │   │   ├── security/                     # Security configurations
│   │   │   ├── service/                      # Business logic services
│   │   │   └── utils/                        # Utility classes
│   │   └── resources/
│   │       ├── application.yaml              # Main configuration
│   │       ├── bootstrap.yaml                # Bootstrap configuration
│   │       ├── mapper/                       # MyBatis XML mappers
│   │       └── sql/                          # Database schemas
│   └── test/                                 # Test cases
├── scripts/                                   # Utility scripts
├── docker-compose.yaml                        # Docker Compose setup
├── Dockerfile                                 # Docker image definition
└── pom.xml                                    # Maven dependencies
```

## 📦 Prerequisites

- **JDK**: 17 or higher ([Download](https://adoptium.net/))
- **Maven**: 3.6 or higher ([Download](https://maven.apache.org/download.cgi))
- **MySQL**: 8.0 or higher ([Download](https://dev.mysql.com/downloads/mysql/))
- **Redis**: 6.0 or higher ([Download](https://redis.io/download))
- **Nacos**: 2.2.0 (Optional, for service discovery) ([Download](https://nacos.io/))

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd online_store
```

### 2. Database Setup

#### Create Database

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### Initialize Tables

Execute the SQL scripts located in `src/main/resources/sql/` to create the required tables:

```bash
# Using MySQL CLI
mysql -u root -p online_store < src/main/resources/sql/member_table.sql
mysql -u root -p online_store < src/main/resources/sql/category_table.sql
mysql -u root -p online_store < src/main/resources/sql/brand_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_table_table.sql
mysql -u root -p online_store < src/main/resources/sql/sku_table.sql
mysql -u root -p online_store < src/main/resources/sql/attribute_table.sql
mysql -u root -p online_store < src/main/resources/sql/attribute_value_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_attribute_relation_table.sql
mysql -u root -p online_store < src/main/resources/sql/item_access_log_table.sql
```

### 3. Configure Application

Update `src/main/resources/application.yaml` with your database and Redis credentials:

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
  secret: your_secret_key_here  # Change this to a secure random string
```

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

#### Using Maven

```bash
mvn spring-boot:run --add-opens java.base/java.lang=ALL-UNNAMED
```

#### Using JAR

```bash
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

### 6. Verify Installation

The application should be running at `http://localhost:8080`

Check the health endpoint:
```bash
curl http://localhost:8080/actuator/health
```

## ⚙️ Configuration

### Environment Variables

You can override configuration using environment variables:

| Variable | Description | Default |
|----------|-------------|----------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `local` |
| `ADMIN_USERNAME` | Admin username | `admin` |
| `ADMIN_PASSWORD` | Admin password | `admin123` |
| `JWT_SECRET` | JWT signing secret | *Required* |
| `NACOS_ENABLED` | Enable Nacos integration | `false` |
| `NACOS_SERVER_ADDR` | Nacos server address | `localhost:8848` |
| `NACOS_NAMESPACE` | Nacos namespace | `` |

### Profiles

The application supports different profiles for various environments:
- `local` - Local development (default)
- `dev` - Development environment
- `prod` - Production environment

Activate a profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 📚 API Documentation

### Authentication

The application uses JWT-based authentication. Include the token in the Authorization header:

```bash
Authorization: Bearer <your_jwt_token>
```

### Main Endpoints

#### Member Management
- `POST /members` - Register new member
- `GET /members/{id}` - Get member details
- `PUT /members/{id}` - Update member information

#### Product Catalog
- `GET /categories` - List all categories
- `GET /brands` - List all brands
- `GET /items` - List items with pagination
- `GET /items/{id}` - Get item details
- `POST /items` - Create new item

#### Product Attributes
- `GET /attributes` - List product attributes
- `POST /attributes` - Create new attribute

### Example Request

```bash
# Register a new member
curl -X POST http://localhost:8080/members \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "nickName": "johnd",
    "password": "securePassword123",
    "phone": "13800138000",
    "gender": "male",
    "age": 25
  }'
```

## 🗄 Database Schema

The application uses the following main tables:

- **member** - User accounts and profiles
- **category** - Product categories
- **brand** - Product brands
- **item** - Product items
- **sku** - Stock keeping units
- **attribute** - Product attributes
- **attribute_value** - Attribute values
- **item_attribute_relation** - Item-attribute mappings
- **item_access_log** - Item access tracking

Detailed schemas are available in `src/main/resources/sql/`

## 🐳 Docker Support

### Using Docker Compose

Start MySQL and Redis services:

```bash
# Start all services (MySQL + Redis)
docker-compose --profile all up -d

# Start only MySQL
docker-compose --profile without-redis up -d

# Stop services
docker-compose down
```

### Building Application Docker Image

```bash
# Build the image
docker build -t online-store:latest .

# Run the container
docker run -d -p 8080:8080 --name online-store online-store:latest
```

## 💻 Development

### Code Style

The project uses:
- Lombok for reducing boilerplate code
- MyBatis for SQL mapping
- Spring conventions for naming and structure

### Running Tests

```bash
mvn test
```

### Building for Production

```bash
mvn clean package -Pprod
```

### Hot Reload

For development, you can use Spring Boot DevTools (if added):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

## 🔧 Troubleshooting

### Common Issues

#### 1. Application fails to start with "IllegalAccessError"

**Solution**: Add JVM argument `--add-opens java.base/java.lang=ALL-UNNAMED`

#### 2. Database connection refused

**Solution**: Verify MySQL is running and credentials are correct in `application.yaml`

```bash
# Check MySQL status
sudo systemctl status mysql

# Or using Docker
docker ps | grep mysql
```

#### 3. Redis connection timeout

**Solution**: Ensure Redis is running and accessible

```bash
# Test Redis connection
redis-cli ping
# Should return: PONG
```

#### 4. JWT secret not configured

**Solution**: Set the `JWT_SECRET` environment variable:

```bash
export JWT_SECRET="your-very-secure-secret-key-here"
```

#### 5. Port 8080 already in use

**Solution**: Change the port in `application.yaml` or stop the conflicting service:

```yaml
server:
  port: 8081  # Use a different port
```

### Logs

Check application logs for detailed error messages:

```bash
# If running with Maven
tail -f logs/spring.log

# If running as JAR
java -jar target/online-store-1.0-SNAPSHOT.jar > app.log 2>&1
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding conventions
- Write unit tests for new features
- Update documentation for API changes
- Ensure all tests pass before submitting PR

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 📧 Contact

For questions or support, please open an issue in the repository.

---

**Happy Coding! 🚀** 