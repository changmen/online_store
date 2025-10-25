# Online Store 🛒

A modern e-commerce platform built with Spring Cloud microservices architecture, providing comprehensive product management, user authentication, and shopping functionality.

## ✨ Features

- **Product Management**: Complete CRUD operations for products, categories, brands, and attributes
- **User Authentication**: JWT-based security with Spring Security
- **Multi-tenant Support**: Configurable environment settings with Nacos integration
- **Data Persistence**: MyBatis integration with MySQL database
- **Caching**: Redis integration for improved performance
- **Internationalization**: Multi-language support with i18n
- **RESTful APIs**: Comprehensive REST endpoints for all operations
- **Docker Support**: Containerized deployment with Docker Compose

## 🏗️ Architecture

This application follows a layered architecture pattern:

- **Controller Layer**: REST API endpoints
- **Service Layer**: Business logic implementation
- **Data Access Layer**: MyBatis mappers and database operations
- **Security Layer**: JWT authentication and authorization
- **Configuration Layer**: Spring Cloud configuration management

## 🛠️ Technology Stack

### Core Framework
- **Java 17** - Programming language
- **Spring Boot 3.4.3** - Application framework
- **Spring Cloud 2024.0.0** - Microservices framework
- **Spring Security** - Authentication and authorization
- **Spring Data Redis** - Redis integration

### Database & Persistence
- **MySQL 8.2.0** - Primary database
- **MyBatis 3.0.3** - ORM framework
- **Redis 5.2.0** - Caching and session storage
- **PageHelper 2.1.0** - Pagination support

### Additional Libraries
- **JWT (jjwt) 0.11.5** - JSON Web Token implementation
- **Lombok 1.18.36** - Code generation
- **Apache Commons Lang3 3.17.0** - Utility functions
- **Nacos 2.2.0** - Service discovery and configuration
- **Aliyun OSS 3.18.1** - Object storage service

## 📁 Project Structure

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java     # Main application class
│   │   │   ├── controller/                     # REST API controllers
│   │   │   │   ├── AttributeController.java
│   │   │   │   ├── BrandController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ItemController.java
│   │   │   │   ├── ItemDetailController.java
│   │   │   │   └── MemberController.java
│   │   │   ├── service/                        # Business logic layer
│   │   │   ├── mapper/                         # MyBatis mappers
│   │   │   ├── entity/                         # Database entities
│   │   │   ├── dto/                           # Data transfer objects
│   │   │   ├── config/                        # Configuration classes
│   │   │   ├── security/                      # Security configuration
│   │   │   ├── utils/                         # Utility classes
│   │   │   ├── enums/                         # Enumeration classes
│   │   │   └── exceptions/                    # Custom exceptions
│   │   └── resources/
│   │       ├── application.yaml               # Main configuration
│   │       ├── application-local.yaml         # Local environment config
│   │       ├── bootstrap.yaml                 # Bootstrap configuration
│   │       ├── mapper/                        # MyBatis XML mappings
│   │       ├── sql/                          # Database schema scripts
│   │       └── i18n/                         # Internationalization files
│   └── test/                                  # Test classes
├── scripts/                                   # Python data generation scripts
├── docker-compose.yaml                       # Docker services configuration
├── Dockerfile                                 # Application containerization
├── pom.xml                                   # Maven dependencies
└── README.md                                 # This file
```

## 🚀 Quick Start

### Prerequisites

- **Java 17** or higher
- **Maven 3.6** or higher
- **MySQL 8.0** or higher
- **Redis 6.0** or higher
- **Docker & Docker Compose** (optional, for containerized setup)

### Option 1: Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd online_store
   ```

2. **Start infrastructure services**
   ```bash
   # Start MySQL and Redis using Docker Compose
   docker-compose --profile all up -d
   ```

3. **Initialize the database**
   ```sql
   # Connect to MySQL and create database
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # Run SQL scripts to create tables
   # Scripts are located in src/main/resources/sql/
   ```

4. **Configure application**
   - Copy `application-local.yaml` and customize database/Redis settings if needed
   - Set required environment variables:
     ```bash
     export JWT_SECRET=your-secret-key-here
     export ADMIN_USERNAME=admin
     export ADMIN_PASSWORD=your-secure-password
     ```

5. **Run the application**
   ```bash
   # Add required JVM arguments and start
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
   ```

### Option 2: Docker Setup

1. **Build and run with Docker**
   ```bash
   # Build the application
   docker build -t online-store .
   
   # Start all services including the application
   docker-compose --profile all up -d
   ```

### Verification

Once the application is running, you can access:

- **Application**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **API Documentation**: Available through the REST endpoints

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|----------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `local` |
| `JWT_SECRET` | JWT signing secret | Required |
| `ADMIN_USERNAME` | Admin username | `admin` |
| `ADMIN_PASSWORD` | Admin password | `admin123` |
| `NACOS_ENABLED` | Enable Nacos service discovery | `false` |

### Database Configuration

The application uses MySQL with the following default settings:
- **Host**: localhost:3306
- **Database**: online_store
- **Username**: root
- **Password**: 123456

### Redis Configuration

Redis is used for caching and session storage:
- **Host**: localhost:6379
- **Database**: 0
- **Connection Pool**: Jedis with max 8 connections

## 📊 API Endpoints

The application provides REST APIs for:

### Product Management
- `GET/POST /api/items` - Item operations
- `GET/POST /api/categories` - Category management
- `GET/POST /api/brands` - Brand management
- `GET/POST /api/attributes` - Product attributes

### User Management
- `GET/POST /api/members` - Member operations
- Authentication endpoints (secured with JWT)

### Item Details
- `GET/POST /api/item-details` - Detailed product information

## 🧪 Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Generate test coverage report
mvn jacoco:report
```

## 📝 Data Generation

The project includes Python scripts for generating test data:

```bash
cd scripts
python main.py
```

## 🐳 Docker Support

The project includes Docker configuration for easy deployment:

- **Dockerfile**: Application containerization
- **docker-compose.yaml**: Multi-service setup with MySQL and Redis

```bash
# Start infrastructure only
docker-compose --profile without-redis up -d

# Start all services
docker-compose --profile all up -d
```

## 🔒 Security

- JWT-based authentication with configurable expiration
- Spring Security integration for endpoint protection
- Password encryption and secure session management
- Environment-based configuration for sensitive data

## 🌐 Internationalization

The application supports multiple languages through Spring's i18n framework. Language files are located in `src/main/resources/i18n/`.

## 🚀 Deployment

### Production Deployment

1. Build the application:
   ```bash
   mvn clean package -DskipTests
   ```

2. Configure production environment variables

3. Deploy using Docker or traditional application server

### Health Monitoring

Spring Actuator endpoints are available for monitoring:
- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For questions and support, please contact the development team or create an issue in the repository. 