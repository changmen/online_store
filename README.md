# 🛍️ Online Store

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen?style=flat-square&logo=spring)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue?style=flat-square&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.2-blue?style=flat-square&logo=mysql)
![Redis](https://img.shields.io/badge/Redis-Latest-red?style=flat-square&logo=redis)

A modern, cloud-native online store application built with Spring Cloud microservices architecture. This project demonstrates enterprise-level e-commerce functionality with comprehensive features including user management, product catalog, shopping cart, order processing, and secure authentication.

## ✨ Features

- 🔐 **Authentication & Authorization**: JWT-based security with Spring Security
- 👤 **User Management**: Registration, login, profile management
- 📦 **Product Management**: CRUD operations for products and categories
- 🛒 **Shopping Cart**: Add, remove, update items with session management
- 💳 **Order Processing**: Complete order lifecycle management
- 🌐 **Microservices Ready**: Nacos service discovery and configuration
- 📊 **Database Integration**: MyBatis with MySQL for data persistence
- ⚡ **Caching**: Redis integration for improved performance
- 🌍 **Internationalization**: Multi-language support
- 📱 **RESTful API**: Well-designed REST endpoints
- 🔧 **Configuration Management**: Environment-specific configurations

## 🏗️ Architecture

### Technology Stack

| Component | Technology | Version |
|-----------|------------|----------|
| **Runtime** | Java JDK | 17 |
| **Framework** | Spring Boot | 3.4.3 |
| **Cloud** | Spring Cloud | 2024.0.0 |
| **Database** | MySQL | 8.2+ |
| **Cache** | Redis | Latest |
| **ORM** | MyBatis | 3.0.3 |
| **Security** | Spring Security + JWT | Latest |
| **Service Discovery** | Nacos | 2.2.0 |
| **Build Tool** | Maven | 3.6+ |
| **Containerization** | Docker | Latest |

### Project Structure

```
online-store/
├── 📁 src/main/java/com/example/onlinestore/
│   ├── 🚀 OnlineStoreApplication.java       # Application entry point
│   ├── 📁 controller/                       # REST controllers
│   ├── 📁 service/                         # Business logic layer
│   ├── 📁 mapper/                          # Data access layer
│   ├── 📁 entity/                          # JPA entities
│   ├── 📁 dto/                             # Data transfer objects
│   ├── 📁 config/                          # Configuration classes
│   ├── 📁 security/                        # Security configurations
│   ├── 📁 utils/                           # Utility classes
│   ├── 📁 enums/                           # Enumerations
│   └── 📁 exceptions/                      # Custom exceptions
├── 📁 src/main/resources/
│   ├── ⚙️ application.yaml                 # Main configuration
│   ├── ⚙️ application-local.yaml           # Local environment config
│   ├── ⚙️ bootstrap.yaml                   # Bootstrap configuration
│   ├── 📁 mapper/                          # MyBatis XML mappers
│   ├── 📁 sql/                             # Database scripts
│   └── 📁 i18n/                            # Internationalization files
├── 📁 scripts/                             # Utility scripts
├── 🐳 docker-compose.yaml                  # Docker services
├── 🐳 Dockerfile                          # Container configuration
└── 📋 pom.xml                             # Maven dependencies
```

## 🚀 Quick Start

### Prerequisites

- ☕ **Java JDK 17+**
- 🔧 **Maven 3.6+**
- 🐘 **MySQL 8.0+**
- 🔴 **Redis 6.0+**
- 🐳 **Docker & Docker Compose** (optional)

### Option 1: Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd online-store
   ```

2. **Set up the database**
   ```sql
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Configure environment variables**
   ```bash
   export JWT_SECRET=your-jwt-secret-key
   export SPRING_PROFILES_ACTIVE=local
   ```

4. **Update configuration**
   - Edit `src/main/resources/application-local.yaml`
   - Configure your MySQL and Redis connection details

5. **Run the application**
   ```bash
   # Option 1: Using Maven
   mvn clean spring-boot:run
   
   # Option 2: Using JVM arguments (if needed)
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
   ```

### Option 2: Docker Compose

1. **Start services with Docker Compose**
   ```bash
   # Start MySQL and Redis
   docker-compose --profile all up -d
   
   # Or start only MySQL
   docker-compose --profile without-redis up -d
   ```

2. **Run the application**
   ```bash
   mvn clean spring-boot:run
   ```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `JWT_SECRET` | JWT signing secret | **Required** |
| `SPRING_PROFILES_ACTIVE` | Active profile | `local` |
| `ADMIN_USERNAME` | Default admin username | `admin` |
| `ADMIN_PASSWORD` | Default admin password | `admin123` |
| `NACOS_ENABLED` | Enable Nacos discovery | `false` |

### Database Configuration

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

### Redis Configuration

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
```

## 📚 API Documentation

Once the application is running, you can access:

- **Application**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics

### Key Endpoints

- `POST /api/auth/login` - User authentication
- `POST /api/auth/register` - User registration
- `GET /api/products` - List products
- `POST /api/cart/add` - Add item to cart
- `POST /api/orders` - Create order

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Run integration tests
mvn verify
```

## 🚢 Deployment

### Building Docker Image

```bash
# Build the application
mvn clean package

# Build Docker image
docker build -t online-store:latest .

# Run container
docker run -p 8080:8080 online-store:latest
```

### Production Deployment

1. Set production environment variables
2. Configure external MySQL and Redis instances
3. Enable Nacos for service discovery
4. Use production-grade secrets management

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Troubleshooting

### Common Issues

**Issue**: Application fails to start with "JWT_SECRET not found"
```bash
# Solution: Set the JWT_SECRET environment variable
export JWT_SECRET=your-very-secure-secret-key-here
```

**Issue**: Database connection refused
```bash
# Solution: Ensure MySQL is running and accessible
docker-compose --profile without-redis up -d
# Or start MySQL service locally
```

**Issue**: Redis connection issues
```bash
# Solution: Start Redis service
docker-compose --profile all up -d
# Or install and start Redis locally
```

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Troubleshooting](#-troubleshooting) section
2. Review the application logs
3. Open an issue in the repository

---

<div align="center">
  <strong>Built with ❤️ using Spring Boot and Spring Cloud</strong>
</div> 