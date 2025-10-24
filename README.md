# 🛒 Online Store

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A modern, scalable online store application built with Spring Cloud microservices architecture. This enterprise-grade e-commerce platform provides comprehensive product management, user authentication, and secure transaction processing capabilities.

## 🚀 Features

### Core Business Features
- 🛍️ **Product Management**: Complete product catalog with categories, brands, and attributes
- 👥 **User Management**: Member registration, authentication, and profile management
- 🏷️ **Category & Brand Management**: Hierarchical product organization
- 📋 **Product Attributes**: Flexible attribute system for product variants
- 🔐 **Security**: JWT-based authentication with Spring Security
- 📊 **RESTful APIs**: Well-documented REST endpoints for all operations

### Technical Features
- 🏗️ **Microservices Architecture**: Built with Spring Cloud
- 🔄 **Service Discovery**: Nacos integration for service registration
- 💾 **Data Persistence**: MyBatis with MySQL database
- ⚡ **Caching**: Redis integration for improved performance
- 🐳 **Containerization**: Docker and Docker Compose support
- 📈 **Monitoring**: Spring Boot Actuator for health checks
- 🧪 **Testing**: Comprehensive unit and integration tests

## 🛠️ Technology Stack

### Backend Technologies
- **Java**: 17+
- **Spring Cloud**: 2024.0.0
- **Spring Boot**: 3.4.3
- **Spring Security**: JWT Authentication
- **MyBatis**: 3.0.3 (Data Access Layer)
- **MySQL**: 8.2.0 (Primary Database)
- **Redis**: 5.2.0 (Caching & Session Storage)
- **Nacos**: 2.2.0 (Service Discovery & Configuration)
- **Maven**: Build & Dependency Management

### Additional Libraries
- **Lombok**: Code Generation
- **PageHelper**: Pagination Support
- **Jackson**: JSON Processing
- **Commons Lang3**: Utility Functions
- **JJWT**: JWT Token Processing
- **Aliyun OSS**: File Storage (Optional)

## 📁 Project Structure

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # Main Application
│   │   │   ├── controller/                    # REST Controllers
│   │   │   │   ├── AttributeController.java
│   │   │   │   ├── BrandController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ItemController.java
│   │   │   │   ├── ItemDetailController.java
│   │   │   │   └── MemberController.java
│   │   │   ├── service/                       # Business Logic
│   │   │   ├── mapper/                        # Data Access Layer
│   │   │   ├── entity/                        # Database Entities
│   │   │   ├── dto/                          # Data Transfer Objects
│   │   │   ├── config/                       # Configuration Classes
│   │   │   ├── security/                     # Security Configuration
│   │   │   ├── utils/                        # Utility Classes
│   │   │   ├── exceptions/                   # Custom Exceptions
│   │   │   └── enums/                        # Enumeration Types
│   │   └── resources/
│   │       ├── application.yml               # Application Configuration
│   │       ├── bootstrap.yml                 # Bootstrap Configuration
│   │       └── mapper/                       # MyBatis Mapper XMLs
│   └── test/                                 # Unit & Integration Tests
├── scripts/                                  # Utility Scripts
├── docker-compose.yaml                       # Docker Services
├── Dockerfile                                # Application Container
├── pom.xml                                   # Maven Dependencies
└── README.md                                 # This File
```

## 🚦 Prerequisites

Before running the application, ensure you have the following installed:

- **Java Development Kit (JDK)**: 17 or higher
- **Apache Maven**: 3.6+ (for building)
- **MySQL**: 8.0+ (primary database)
- **Redis**: 6.0+ (caching and sessions)
- **Docker & Docker Compose**: (optional, for containerized deployment)

## 🔧 Quick Start

### Method 1: Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd online_store
   ```

2. **Database Setup**
   ```sql
   # Connect to MySQL and create database
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # Create a dedicated user (recommended)
   CREATE USER 'store_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON online_store.* TO 'store_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Configuration**
   - Copy `application.yml.example` to `application.yml` (if available)
   - Update database connection settings:
     ```yaml
     spring:
       datasource:
         url: jdbc:mysql://localhost:3306/online_store
         username: store_user
         password: your_password
       redis:
         host: localhost
         port: 6379
     ```

4. **Start Required Services**
   ```bash
   # Start MySQL (if not running)
   sudo systemctl start mysql
   
   # Start Redis (if not running)
   sudo systemctl start redis
   ```

5. **Build and Run**
   ```bash
   # Clean and compile
   mvn clean compile
   
   # Run with JVM arguments (required for Java 17+)
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
   
   # Alternative: Run compiled JAR
   mvn clean package
   java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
   ```

6. **Verify Installation**
   - Application: http://localhost:8080
   - Health Check: http://localhost:8080/actuator/health
   - API Documentation: http://localhost:8080/swagger-ui.html (if configured)

### Method 2: Docker Deployment

1. **Using Docker Compose** (Recommended for development)
   ```bash
   # Start all services (MySQL + Redis + Application)
   docker-compose --profile all up -d
   
   # Start only database services
   docker-compose --profile without-redis up -d
   
   # View logs
   docker-compose logs -f
   ```

2. **Build and Run Application Container**
   ```bash
   # Build application image
   docker build -t online-store:latest .
   
   # Run with external MySQL/Redis
   docker run -d \
     --name online-store-app \
     -p 8080:8080 \
     -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/online_store \
     -e SPRING_REDIS_HOST=host.docker.internal \
     online-store:latest
   ```

## 🔌 API Endpoints

The application provides RESTful APIs for all major operations:

### Product Management
- `GET /api/items` - List all products
- `GET /api/items/{id}` - Get product details
- `POST /api/items` - Create new product
- `PUT /api/items/{id}` - Update product
- `DELETE /api/items/{id}` - Delete product

### Category Management
- `GET /api/categories` - List all categories
- `POST /api/categories` - Create category
- `PUT /api/categories/{id}` - Update category

### Brand Management
- `GET /api/brands` - List all brands
- `POST /api/brands` - Create brand
- `PUT /api/brands/{id}` - Update brand

### User Management
- `POST /api/members/register` - User registration
- `POST /api/members/login` - User authentication
- `GET /api/members/profile` - Get user profile
- `PUT /api/members/profile` - Update profile

### Attribute Management
- `GET /api/attributes` - List product attributes
- `POST /api/attributes` - Create attribute

> 📚 **API Documentation**: Complete API documentation is available via Swagger UI at `/swagger-ui.html` when the application is running.

## 🧪 Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Run integration tests only
mvn test -Dtest="*IT"

# Run specific test class
mvn test -Dtest="ProductControllerTest"
```

### Test Categories
- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions
- **API Tests**: Test REST endpoint functionality
- **Database Tests**: Test data persistence layer

## 🚀 Deployment

### Production Deployment

1. **Environment Configuration**
   - Set up production database with proper security
   - Configure Redis cluster for high availability
   - Set up Nacos server for service discovery
   - Configure proper logging levels

2. **Application Configuration**
   ```yaml
   # application-prod.yml
   spring:
     profiles:
       active: prod
     datasource:
       url: jdbc:mysql://prod-mysql:3306/online_store
     redis:
       cluster:
         nodes: redis-node1:6379,redis-node2:6379,redis-node3:6379
   ```

3. **Build Production Image**
   ```bash
   mvn clean package -Pprod
   docker build -t online-store:prod .
   ```

### Monitoring & Health Checks

- **Health Endpoint**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Info**: `/actuator/info`
- **Environment**: `/actuator/env`

## 🛠️ Troubleshooting

### Common Issues

**1. Application fails to start**
```bash
# Check if required services are running
sudo systemctl status mysql redis

# Verify database connectivity
mysql -u store_user -p -h localhost online_store

# Check Redis connectivity
redis-cli ping
```

**2. "Illegal reflective access" warnings**
- **Solution**: Add JVM argument `--add-opens java.base/java.lang=ALL-UNNAMED`

**3. Database connection errors**
- Verify MySQL is running: `sudo systemctl status mysql`
- Check connection parameters in `application.yml`
- Ensure database and user exist
- Verify firewall settings

**4. Redis connection issues**
- Check Redis status: `sudo systemctl status redis`
- Verify Redis configuration: `redis-cli info`
- Check network connectivity

**5. Port already in use**
```bash
# Find process using port 8080
sudo lsof -i :8080

# Kill process if needed
sudo kill -9 <PID>

# Or change port in application.yml
server:
  port: 8081
```

### Log Analysis
```bash
# View application logs
tail -f logs/application.log

# View startup logs
mvn spring-boot:run | grep ERROR

# Check Docker container logs
docker logs online-store-app
```

## 🤝 Contributing

We welcome contributions to the Online Store project! Please follow these guidelines:

### Development Workflow

1. **Fork the repository**
   ```bash
   git fork <repository-url>
   git clone <your-fork-url>
   cd online_store
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes**
   - Follow Java coding conventions
   - Add unit tests for new functionality
   - Update documentation as needed
   - Ensure all tests pass

4. **Submit a Pull Request**
   - Provide clear description of changes
   - Reference any related issues
   - Ensure CI/CD pipeline passes

### Code Standards
- **Java Style**: Follow Google Java Style Guide
- **Testing**: Maintain >80% code coverage
- **Documentation**: Update README and API docs
- **Commits**: Use conventional commit messages

### Testing Guidelines
```bash
# Before submitting PR
mvn clean test
mvn checkstyle:check
mvn spotbugs:check
```

## 📋 Changelog

### [1.0.0] - 2024-10-24
#### Added
- Initial release with core e-commerce functionality
- Product management with categories and brands
- User authentication and member management
- RESTful API endpoints
- Docker support
- Spring Security integration
- Redis caching layer
- MyBatis data persistence

## 🔗 Related Projects

- **Frontend Application**: [online-store-frontend](link-to-frontend)
- **Admin Dashboard**: [online-store-admin](link-to-admin)
- **Mobile App**: [online-store-mobile](link-to-mobile)
- **Microservices**: [online-store-microservices](link-to-microservices)

## 📚 Additional Resources

- **API Documentation**: [Swagger/OpenAPI Docs](http://localhost:8080/swagger-ui.html)
- **Architecture Guide**: [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)
- **Database Schema**: [docs/DATABASE.md](docs/DATABASE.md)
- **Deployment Guide**: [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md)
- **Security Guide**: [docs/SECURITY.md](docs/SECURITY.md)

## ❓ FAQ

**Q: Can I use this project for commercial purposes?**
A: Yes, this project is licensed under MIT License.

**Q: How do I add new product attributes?**
A: Use the Attribute Management API or extend the attribute entity model.

**Q: Is this production-ready?**
A: This is a demo/learning project. For production use, additional security, monitoring, and performance optimizations are recommended.

**Q: How do I integrate with payment systems?**
A: Payment integration is not included. You'll need to integrate with providers like Stripe, PayPal, or Alipay.

**Q: Can I run this on different databases?**
A: Currently optimized for MySQL, but can be adapted for PostgreSQL or other databases with minor configuration changes.

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/your-org/online-store/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-org/online-store/discussions)
- **Wiki**: [Project Wiki](https://github.com/your-org/online-store/wiki)
- **Email**: support@yourcompany.com

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- MyBatis community for the persistence framework
- Redis team for the caching solution
- All contributors who helped improve this project

---

**Made with ❤️ by the Online Store Team**

> ⭐ If you find this project helpful, please consider giving it a star on GitHub! 