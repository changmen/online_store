# Online Store

🛒 A modern e-commerce platform built with Spring Cloud architecture, providing RESTful APIs for online shopping functionality.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Docker Support](#docker-support)
- [Development](#development)
- [Security](#security)

## ✨ Features

- 🔐 **JWT-based Authentication** - Secure user authentication and authorization
- 🛍️ **Product Management** - Complete CRUD operations for items, categories, and brands
- 👤 **Member Management** - User registration and profile management
- 🏷️ **Attribute Management** - Product attributes and specifications
- 💾 **Redis Caching** - High-performance data caching
- 🔍 **Nacos Integration** - Service discovery and configuration management
- 📦 **MyBatis Pagination** - Efficient database query pagination
- ☁️ **Aliyun OSS** - Cloud storage integration
- 🐳 **Docker Support** - Containerized deployment

## 🛠 Tech Stack

### Backend Framework
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0

### Database & Cache
- **MySQL**: 8.2.0
- **MyBatis**: 3.0.3
- **MyBatis Spring Boot Starter**: 3.0.2
- **PageHelper**: 2.1.0 (Pagination)
- **Redis**: 5.2.0 (Jedis client)

### Security & Authentication
- **Spring Security**: Latest
- **JWT (JJWT)**: 0.11.5

### Service Discovery
- **Nacos**: 2.2.0

### Cloud Storage
- **Aliyun OSS**: 3.18.1

### Utilities
- **Lombok**: 1.18.36
- **Apache Commons Lang3**: 3.17.0
- **Commons Collections**: 3.2.2
- **CGLIB**: 3.3.0

## 📁 Project Structure

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # Main application class
│   │   │   ├── bean/                           # Domain entities
│   │   │   ├── config/                         # Configuration classes
│   │   │   ├── constants/                      # Application constants
│   │   │   ├── controller/                     # REST API controllers
│   │   │   │   ├── AttributeController.java
│   │   │   │   ├── BrandController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ItemController.java
│   │   │   │   ├── ItemDetailController.java
│   │   │   │   └── MemberController.java
│   │   │   ├── dto/                            # Data Transfer Objects
│   │   │   ├── entity/                         # Database entities
│   │   │   ├── enums/                          # Enumerations
│   │   │   ├── errors/                         # Error definitions
│   │   │   ├── exceptions/                     # Custom exceptions
│   │   │   ├── handler/                        # Exception handlers
│   │   │   ├── mapper/                         # MyBatis mappers
│   │   │   ├── security/                       # Security configurations
│   │   │   ├── service/                        # Business logic services
│   │   │   └── utils/                          # Utility classes
│   │   └── resources/
│   │       ├── application.yaml                # Application configuration
│   │       ├── bootstrap.yaml                  # Bootstrap configuration
│   │       └── mapper/                         # MyBatis XML mappers
│   └── test/                                   # Test cases
├── scripts/                                    # Utility scripts
├── docker-compose.yaml                         # Docker compose configuration
├── Dockerfile                                  # Docker image definition
├── pom.xml                                     # Maven configuration
└── README.md                                   # This file
```

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **JDK 17** or higher
- **Maven 3.6+** for dependency management
- **MySQL 8.0+** for database
- **Redis 6.0+** for caching
- **Docker & Docker Compose** (optional, for containerized deployment)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd online_store
```

### 2. Set Up Database

Create the MySQL database:

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure Environment Variables

Set the following environment variables (or configure in `application.yaml`):

```bash
export JWT_SECRET=your-secret-key-here
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
export SPRING_PROFILES_ACTIVE=local
```

### 4. Start Infrastructure Services

#### Option A: Using Docker Compose

```bash
# Start MySQL and Redis
docker-compose --profile all up -d

# Or start MySQL only
docker-compose --profile without-redis up -d
```

#### Option B: Manual Setup

Ensure MySQL and Redis services are running on your local machine.

### 5. Build and Run the Application

```bash
# Build the project
mvn clean install

# Run the application with required JVM parameters
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

The application will start on `http://localhost:8080`.

## ⚙️ Configuration

### Application Configuration

Edit `src/main/resources/application.yaml`:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store
    username: root
    password: 123456
  
  data:
    redis:
      host: localhost
      port: 6379
      password: 
      database: 0

jwt:
  secret: ${JWT_SECRET}
  expiration: 86400  # 24 hours in seconds
```

### Nacos Configuration (Optional)

To enable Nacos for service discovery:

```bash
export NACOS_ENABLED=true
```

## 📡 API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Available Endpoints

#### Items
- `GET /api/v1/items/{itemId}` - Get item by ID

#### Categories
- `GET /api/v1/categories/{categoryId}` - Get category by ID

#### Brands
- Brand management endpoints

#### Members
- Member registration and management

#### Attributes
- Product attribute management

#### Item Details
- Detailed product information

> **Note**: For complete API documentation, consider integrating Swagger/OpenAPI.

## 🐳 Docker Support

### Build Docker Image

```bash
docker build -t online-store:latest .
```

### Run with Docker Compose

```bash
# Start all services (MySQL + Redis + Application)
docker-compose --profile all up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

## 💻 Development

### Running Tests

```bash
mvn test
```

### Code Style

This project uses:
- Lombok for reducing boilerplate code
- Spring Boot best practices
- RESTful API design principles

### Building for Production

```bash
mvn clean package -DskipTests
```

The executable JAR will be created in the `target/` directory.

## 🔒 Security

- **JWT Authentication**: All API endpoints (except public ones) require JWT tokens
- **Spring Security**: Configured for endpoint protection
- **Password Encryption**: User passwords are encrypted
- **Environment Variables**: Sensitive data should be configured via environment variables

### Default Credentials

```
Username: admin
Password: admin123
```

> ⚠️ **Warning**: Change default credentials in production!

## 📝 License

This project is licensed under the MIT License.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📞 Support

For issues and questions, please create an issue in the repository.

---

Built with ❤️ using Spring Boot and Spring Cloud 