# 🛒 Online Store

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![MySQL](https://img.shields.io/badge/MySQL-8.2.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-Latest-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive, enterprise-grade online store application built with modern Spring ecosystem technologies. This project demonstrates a complete e-commerce solution with microservices architecture support, featuring product management, user authentication, and scalable backend services.

## ✨ Key Features

### 🏪 E-commerce Core
- **Product Management**: Categories, brands, attributes, and SKU management
- **Inventory Control**: Real-time stock tracking and management
- **Member System**: User registration, authentication, and profile management
- **Search & Filter**: Advanced product searching and filtering capabilities

### 🔒 Security & Authentication
- **JWT Authentication**: Secure token-based authentication system
- **Spring Security**: Comprehensive security configuration
- **Role-based Access**: Fine-grained permission control
- **Password Encryption**: Secure password handling

### 🏗️ Architecture & Performance
- **Microservices Ready**: Spring Cloud integration with Nacos discovery
- **Caching Layer**: Redis integration for high-performance caching
- **Database Optimization**: MyBatis with connection pooling
- **RESTful APIs**: Well-designed REST endpoints
- **Internationalization**: Multi-language support

### 📊 Monitoring & Observability
- **Health Checks**: Spring Boot Actuator integration
- **Logging**: Comprehensive logging configuration
- **Error Handling**: Global exception handling
- **Performance Monitoring**: Built-in metrics and monitoring

## 🛠️ Technology Stack

### Backend Framework
- **Java 17**: Latest LTS version with modern language features
- **Spring Boot 3.4.3**: Production-ready application framework
- **Spring Cloud 2024.0.0**: Microservices infrastructure
- **Spring Security**: Authentication and authorization
- **Spring AOP**: Aspect-oriented programming support

### Database & Persistence
- **MySQL 8.2.0**: Primary relational database
- **MyBatis 3.0.3**: SQL mapping framework
- **PageHelper 2.1.0**: Pagination support
- **HikariCP**: High-performance connection pooling

### Caching & Performance
- **Redis**: In-memory data structure store
- **Jedis 5.2.0**: Redis Java client
- **Spring Data Redis**: Redis integration

### Service Discovery & Configuration
- **Nacos 2.2.0**: Service discovery and configuration management
- **Spring Cloud Alibaba**: Cloud-native application support

### Security & Authentication
- **JWT (JSON Web Tokens)**: Stateless authentication
- **JJWT 0.11.5**: Java JWT library
- **Spring Security**: Comprehensive security framework

### Development & Build Tools
- **Maven**: Project management and build automation
- **Lombok**: Boilerplate code reduction
- **Apache Commons**: Utility libraries
- **Docker**: Containerization support

### Cloud & Storage
- **Aliyun OSS**: Object storage service integration

## 📁 Project Structure

```
online-store/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/example/onlinestore/
│   │   │   ├── 📄 OnlineStoreApplication.java      # Application entry point
│   │   │   ├── 📁 bean/                            # Configuration beans
│   │   │   ├── 📁 config/                          # Configuration classes
│   │   │   ├── 📁 constants/                       # Application constants
│   │   │   ├── 📁 controller/                      # REST controllers
│   │   │   │   ├── 📄 AttributeController.java     # Product attributes API
│   │   │   │   ├── 📄 BrandController.java         # Brand management API
│   │   │   │   ├── 📄 CategoryController.java      # Category management API
│   │   │   │   ├── 📄 ItemController.java          # Product management API
│   │   │   │   ├── 📄 ItemDetailController.java    # Product details API
│   │   │   │   └── 📄 MemberController.java        # User management API
│   │   │   ├── 📁 dto/                             # Data transfer objects
│   │   │   ├── 📁 entity/                          # Database entities
│   │   │   │   ├── 📄 AttributeEntity.java
│   │   │   │   ├── 📄 BrandEntity.java
│   │   │   │   ├── 📄 CategoryEntity.java
│   │   │   │   ├── 📄 ItemEntity.java
│   │   │   │   ├── 📄 MemberEntity.java
│   │   │   │   └── 📄 SkuEntity.java
│   │   │   ├── 📁 enums/                           # Enumeration classes
│   │   │   ├── 📁 exceptions/                      # Custom exceptions
│   │   │   ├── 📁 handler/                         # Exception handlers
│   │   │   ├── 📁 mapper/                          # MyBatis mappers
│   │   │   ├── 📁 security/                        # Security configuration
│   │   │   ├── 📁 service/                         # Business logic services
│   │   │   └── 📁 utils/                           # Utility classes
│   │   └── 📁 resources/
│   │       ├── 📄 application.yaml                # Main configuration
│   │       ├── 📄 application-local.yaml          # Local environment config
│   │       ├── 📄 bootstrap.yaml                  # Bootstrap configuration
│   │       ├── 📁 i18n/                           # Internationalization files
│   │       ├── 📁 mapper/                          # MyBatis XML mappers
│   │       └── 📁 sql/                             # Database schema scripts
│   └── 📁 test/                                    # Test classes
├── 📁 scripts/                                     # Utility scripts
├── 📄 docker-compose.yaml                         # Docker services
├── 📄 Dockerfile                                   # Application container
├── 📄 pom.xml                                      # Maven configuration
└── 📄 README.md                                    # This file
```

## 🚀 Quick Start

### Prerequisites

- **Java 17+**: [Download Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
- **Maven 3.6+**: [Download Maven](https://maven.apache.org/download.cgi)
- **MySQL 8.0+**: [Download MySQL](https://dev.mysql.com/downloads/mysql/)
- **Redis 6.0+**: [Download Redis](https://redis.io/download)
- **Docker** (Optional): [Download Docker](https://www.docker.com/get-started)

### Option 1: Docker Compose (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd online_store
   ```

2. **Start infrastructure services**
   ```bash
   # Start MySQL and Redis
   docker-compose --profile all up -d
   
   # Or start only MySQL (if you have Redis installed locally)
   docker-compose --profile without-redis up -d
   ```

3. **Initialize the database**
   ```sql
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

4. **Set environment variables**
   ```bash
   export JWT_SECRET="your-super-secret-jwt-key-here-make-it-long-and-random"
   export ADMIN_USERNAME="admin"
   export ADMIN_PASSWORD="your-secure-password"
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

### Option 2: Manual Setup

1. **Install and start MySQL**
   ```bash
   # Create database
   mysql -u root -p
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **Install and start Redis**
   ```bash
   redis-server
   ```

3. **Configure application**
   - Copy `src/main/resources/application-local.yaml.example` to `application-local.yaml`
   - Update database and Redis connection settings
   - Set JWT secret and admin credentials

4. **Build and run**
   ```bash
   mvn clean install
   mvn spring-boot:run --add-opens java.base/java.lang=ALL-UNNAMED
   ```

### 🔧 Configuration

#### Environment Variables

| Variable | Description | Default |
|----------|-------------|--------|
| `JWT_SECRET` | JWT signing secret (required) | - |
| `ADMIN_USERNAME` | Admin username | `admin` |
| `ADMIN_PASSWORD` | Admin password | `admin123` |
| `SPRING_PROFILES_ACTIVE` | Active profile | `local` |
| `NACOS_ENABLED` | Enable Nacos discovery | `false` |

#### Database Configuration

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

#### Redis Configuration

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
```

## 📡 API Documentation

### Authentication Endpoints

- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/refresh` - Refresh JWT token

### Product Management

- `GET /api/items` - List products with pagination
- `GET /api/items/{id}` - Get product details
- `POST /api/items` - Create new product (Admin)
- `PUT /api/items/{id}` - Update product (Admin)
- `DELETE /api/items/{id}` - Delete product (Admin)

### Category & Brand Management

- `GET /api/categories` - List categories
- `GET /api/brands` - List brands
- `POST /api/categories` - Create category (Admin)
- `POST /api/brands` - Create brand (Admin)

### Member Management

- `GET /api/members/profile` - Get user profile
- `PUT /api/members/profile` - Update user profile
- `GET /api/members` - List members (Admin)

Access the application at: `http://localhost:8080`

## 🧪 Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ProductServiceTest

# Run tests with coverage
mvn test jacoco:report
```

### Test Categories

- **Unit Tests**: Individual component testing
- **Integration Tests**: Database and service integration
- **API Tests**: REST endpoint testing
- **Security Tests**: Authentication and authorization testing

## 📊 Monitoring & Health Checks

### Actuator Endpoints

- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Application metrics
- `GET /actuator/env` - Environment properties

### Logging

Logs are configured to output in JSON format for structured logging:

```bash
# View application logs
tail -f logs/online-store.log

# View error logs
tail -f logs/error.log
```

## 🚀 Deployment

### Docker Deployment

1. **Build Docker image**
   ```bash
   docker build -t online-store:latest .
   ```

2. **Run with Docker**
   ```bash
   docker run -p 8080:8080 \
     -e JWT_SECRET="your-secret-key" \
     -e DB_HOST="your-db-host" \
     online-store:latest
   ```

### Production Configuration

- Set `spring.profiles.active=prod`
- Configure external configuration server (Nacos)
- Set up monitoring and alerting
- Configure SSL/TLS certificates
- Set up load balancing

## 🛠️ Development

### Development Workflow

1. **Fork and clone the repository**
2. **Create a feature branch**: `git checkout -b feature/your-feature-name`
3. **Make your changes and add tests**
4. **Run tests**: `mvn test`
5. **Check code style**: `mvn checkstyle:check`
6. **Commit changes**: `git commit -m "Add your feature"`
7. **Push to your fork**: `git push origin feature/your-feature-name`
8. **Create a Pull Request**

### Code Style

This project follows Google Java Style Guide. Use the provided checkstyle configuration:

```bash
mvn checkstyle:check
```

### IDE Setup

#### IntelliJ IDEA
- Install Lombok plugin
- Enable annotation processing
- Import code style settings

#### VS Code
- Install Java Extension Pack
- Install Lombok Annotations Support

## 🔧 Troubleshooting

### Common Issues

#### Application fails to start

```bash
# Check if ports are available
netstat -tulpn | grep :8080
netstat -tulpn | grep :3306
netstat -tulpn | grep :6379

# Check Java version
java -version
```

#### Database connection issues

```bash
# Test MySQL connection
mysql -h localhost -u root -p

# Check MySQL status
sudo systemctl status mysql
```

#### Redis connection issues

```bash
# Test Redis connection
redis-cli ping

# Check Redis status
sudo systemctl status redis
```

#### JWT Secret not set

```bash
# Set JWT secret environment variable
export JWT_SECRET="your-super-secret-jwt-key-here-make-it-long-and-random"
```

### Performance Tuning

- Adjust JVM heap size: `-Xmx2g -Xms1g`
- Configure connection pool sizes
- Enable Redis clustering for high availability
- Use MySQL read replicas for read-heavy workloads

## 📋 Roadmap

- [ ] Shopping Cart functionality
- [ ] Order Management system
- [ ] Payment gateway integration
- [ ] Email notification system
- [ ] Advanced search with Elasticsearch
- [ ] Mobile app API enhancements
- [ ] Real-time inventory updates
- [ ] Advanced analytics dashboard
- [ ] Multi-tenant support
- [ ] GraphQL API support

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

### Contributors

Thanks to all contributors who have helped make this project better!

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

- **Documentation**: [Wiki](https://github.com/your-org/online-store/wiki)
- **Issues**: [GitHub Issues](https://github.com/your-org/online-store/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-org/online-store/discussions)
- **Email**: support@yourcompany.com

## 🙏 Acknowledgments

- Spring Boot team for the amazing framework
- MyBatis team for the excellent ORM solution
- Redis team for the high-performance caching solution
- All open source contributors who made this project possible

---

**Made with ❤️ by [Your Team Name]** 