# 📦 Online Store

A modern e-commerce backend system based on Spring Cloud and Spring Boot, featuring microservices architecture with support for product management, user management, order processing, and other core e-commerce functionalities.

## ✨ Features

- 🔐 JWT-based security authentication and authorization
- 🏗️ Microservices architecture with Nacos service registration and discovery
- 💾 Redis caching support for enhanced performance
- 📝 Complete RESTful API design
- 🔧 Flexible configuration management
- 🐳 Docker containerization deployment support
- 📊 Pagination query support (PageHelper)
- 📦 Aliyun OSS file storage integration

## 🛠️ Tech Stack

### Core Frameworks
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0
- **Spring Security**: 安全认证框架

### Data Storage
- **MySQL**: 8.2.0 - Main database
- **Redis**: 5.2.0 (Jedis) - Cache
- **MyBatis**: 3.0.3 - ORM framework
- **PageHelper**: 2.1.0 - Pagination plugin

### Service Governance
- **Nacos**: 2.2.0 - Service registration and configuration center

### Other Components
- **JWT**: 0.11.5 - Authentication
- **Lombok**: 1.18.36 - Code simplification
- **Aliyun OSS**: 3.18.1 - Object storage
- **Apache Commons**: Utility library

## 📁 Project Structure

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # Application entry point
│   │   │   ├── bean/                           # Bean configuration
│   │   │   ├── config/                         # Configuration classes
│   │   │   ├── constants/                      # Constant definitions
│   │   │   ├── controller/                     # REST controllers
│   │   │   │   ├── AttributeController.java
│   │   │   │   ├── BrandController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ItemController.java
│   │   │   │   ├── ItemDetailController.java
│   │   │   │   └── MemberController.java
│   │   │   ├── dto/                            # Data transfer objects
│   │   │   ├── entity/                         # Entity classes
│   │   │   ├── enums/                          # Enum classes
│   │   │   ├── errors/                         # Error handling
│   │   │   ├── exceptions/                     # Custom exceptions
│   │   │   ├── handler/                        # Handlers
│   │   │   ├── mapper/                         # MyBatis Mapper
│   │   │   ├── security/                       # Security configuration
│   │   │   ├── service/                        # Business logic layer
│   │   │   └── utils/                          # Utility classes
│   │   └── resources/
│   │       ├── application.yaml                # Main configuration file
│   │       └── mapper/                         # MyBatis XML mapping files
│   └── test/                                   # Test code
├── scripts/                                    # Script tools
├── docker-compose.yaml                         # Docker Compose configuration
├── Dockerfile                                  # Docker image build file
├── pom.xml                                     # Maven project configuration
└── README.md                                   # Project documentation
```

## 🚀 Quick Start

### Requirements

- **JDK**: 17 or higher
- **Maven**: 3.6 or higher
- **MySQL**: 8.0 or higher
- **Redis**: 6.0 or higher

### Local Development

#### 1. Start dependent services (using Docker Compose)

```bash
# Start MySQL and Redis
docker-compose --profile all up -d

# Start MySQL only
docker-compose --profile without-redis up -d
```

#### 2. Create database

```sql
CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. Configure environment variables (optional)

Create a `.env` file or set system environment variables:

```bash
export JWT_SECRET=your-secret-key-here
export ADMIN_USERNAME=admin
export ADMIN_PASSWORD=admin123
export SPRING_PROFILES_ACTIVE=local
export NACOS_ENABLED=false
```

#### 4. Modify configuration file

Edit `src/main/resources/application.yaml` and update the following configuration:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/online_store?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
  data:
    redis:
      host: localhost
      port: 6379
```

#### 5. Run the application

**Option 1: Using Maven**

```bash
# Run with JVM arguments
mvn spring-boot:run -Dspring-boot.run.jvmArguments="--add-opens java.base/java.lang=ALL-UNNAMED"
```

**Option 2: Run in IDE**

Configure VM arguments in your IDE:
```
--add-opens java.base/java.lang=ALL-UNNAMED
```

Then run `OnlineStoreApplication.java`

**Option 3: Run packaged JAR**

```bash
# Package
mvn clean package -DskipTests

# Run
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
```

#### 6. Verify the service

After the application starts, visit:
- **Health check**: http://localhost:8080/actuator/health
- **API endpoints**: http://localhost:8080/

Default credentials:
- Username: `admin`
- Password: `admin123`

## 🐳 Docker Deployment

### Build image

```bash
docker build -t online-store:latest .
```

### Run container

```bash
docker run -d \
  --name online-store \
  -p 8080:8080 \
  -e JWT_SECRET=your-secret-key \
  -e SPRING_PROFILES_ACTIVE=prod \
  online-store:latest
```

## 📝 API Documentation

Main API endpoints:

### Product Management
- `GET /items` - Get product list
- `GET /items/{id}` - Get product details
- `POST /items` - Create product
- `PUT /items/{id}` - Update product
- `DELETE /items/{id}` - Delete product

### Brand Management
- `GET /brands` - Get brand list
- `GET /brands/{id}` - Get brand details
- `POST /brands` - Create brand

### Category Management
- `GET /categories` - Get category list
- `GET /categories/{id}` - Get category details

### User Management
- `GET /members` - Get member list
- `GET /members/{id}` - Get member details

### Attribute Management
- `GET /attributes` - Get attribute list

## ⚙️ Configuration

### Environment Variables

| Variable | Description | Default Value |
|--------|------|--------|
| `JWT_SECRET` | JWT secret key (required) | - |
| `ADMIN_USERNAME` | Admin username | admin |
| `ADMIN_PASSWORD` | Admin password | admin123 |
| `SPRING_PROFILES_ACTIVE` | Active profile | local |
| `NACOS_ENABLED` | Enable Nacos | false |

### Configuration Files

- `application.yaml` - Main configuration file
- `bootstrap.yaml` - Bootstrap configuration (if using Nacos)

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run tests and generate report
mvn test jacoco:report
```

## 📦 Build & Deployment

```bash
# Clean and package
mvn clean package

# Package without tests
mvn clean package -DskipTests
```

The generated JAR file is located at `target/online-store-1.0-SNAPSHOT.jar`

## 🔧 Development Tools

The project includes a `scripts/` directory with utility scripts:

```bash
cd scripts
# View script documentation
cat README.md
```

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License. See the LICENSE file for details.

## 📞 Contact

For questions or suggestions, please submit an Issue or Pull Request.

---

**Happy Coding! 🎉** 