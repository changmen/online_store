# Online Store

A modern e-commerce platform built with Spring Cloud microservices architecture, featuring product management, member system, and comprehensive security controls.

## 🚀 Features

- **Product Management**: Items, SKUs, categories, brands, and attributes
- **Member System**: User registration, authentication, and profile management
- **Security**: JWT-based authentication with Spring Security
- **Caching**: Redis integration for high-performance data access
- **Service Discovery**: Nacos integration for microservices (optional)
- **Data Persistence**: MyBatis with MySQL database
- **File Storage**: Aliyun OSS integration for file uploads
- **API Documentation**: RESTful API design
- **Containerization**: Docker support for easy deployment

## 📋 Technology Stack

### Core Framework
- **JDK**: 17
- **Spring Boot**: 3.4.3
- **Spring Cloud**: 2024.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0

### Data & Persistence
- **MySQL**: 8.2.0
- **MyBatis Spring Boot**: 3.0.2
- **PageHelper**: 2.1.0 (pagination support)

### Cache & Messaging
- **Redis**: Jedis 5.2.0
- **Spring Data Redis**

### Security
- **Spring Security**
- **JWT**: 0.11.5 (jjwt)

### Service Discovery
- **Nacos**: 2.2.0 (config & discovery)

### Utilities
- **Lombok**: 1.18.36
- **Apache Commons Lang3**: 3.17.0
- **Commons Collections**: 3.2.2
- **Aliyun OSS SDK**: 3.18.1

## 📁 Project Structure

```
online-store/
├── src/
│   ├── main/
│   │   ├── java/com/example/onlinestore/
│   │   │   ├── OnlineStoreApplication.java    # Main application entry
│   │   │   ├── controller/                    # REST API controllers
│   │   │   │   ├── AttributeController.java
│   │   │   │   ├── BrandController.java
│   │   │   │   ├── CategoryController.java
│   │   │   │   ├── ItemController.java
│   │   │   │   ├── ItemDetailController.java
│   │   │   │   └── MemberController.java
│   │   │   ├── service/                       # Business logic layer
│   │   │   ├── mapper/                        # MyBatis mappers
│   │   │   ├── entity/                        # Database entities
│   │   │   ├── dto/                           # Data transfer objects
│   │   │   ├── config/                        # Configuration classes
│   │   │   ├── security/                      # Security components
│   │   │   ├── utils/                         # Utility classes
│   │   │   ├── enums/                         # Enumerations
│   │   │   ├── exceptions/                    # Custom exceptions
│   │   │   ├── handler/                       # Exception handlers
│   │   │   └── constants/                     # Constants
│   │   └── resources/
│   │       ├── application.yaml               # Main configuration
│   │       ├── mapper/                        # MyBatis XML mappers
│   │       └── sql/                           # Database schema scripts
│   │           ├── attribute_table.sql
│   │           ├── attribute_value_table.sql
│   │           ├── brand_table.sql
│   │           ├── category_table.sql
│   │           ├── item_table_table.sql
│   │           ├── item_attribute_relation_table.sql
│   │           ├── item_access_log_table.sql
│   │           ├── member_table.sql
│   │           └── sku_table.sql
│   └── test/
├── scripts/                                   # Utility scripts
├── pom.xml                                    # Maven configuration
├── Dockerfile                                 # Docker image definition
├── docker-compose.yaml                        # Docker compose setup
└── README.md
```

## 🛠️ Prerequisites

- **JDK**: 17 or higher
- **Maven**: 3.6 or higher
- **MySQL**: 8.0 or higher
- **Redis**: 6.0 or higher
- **Docker** (optional, for containerized deployment)

## 🚦 Quick Start

### Option 1: Using Docker Compose (Recommended)

1. **Start MySQL and Redis services**:
   ```bash
   # Start both MySQL and Redis
   docker-compose --profile all up -d
   
   # Or start MySQL only
   docker-compose --profile without-redis up -d
   ```

2. **Initialize the database**:
   ```bash
   # Connect to MySQL
   mysql -h localhost -u root -p123456
   
   # Create database
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # Import table schemas
   USE online_store;
   source src/main/resources/sql/attribute_table.sql;
   source src/main/resources/sql/attribute_value_table.sql;
   source src/main/resources/sql/brand_table.sql;
   source src/main/resources/sql/category_table.sql;
   source src/main/resources/sql/item_table_table.sql;
   source src/main/resources/sql/item_attribute_relation_table.sql;
   source src/main/resources/sql/item_access_log_table.sql;
   source src/main/resources/sql/member_table.sql;
   source src/main/resources/sql/sku_table.sql;
   ```

3. **Configure application settings**:
   
   Edit `src/main/resources/application.yaml` or set environment variables:
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
   
   jwt:
     secret: your-secret-key-here  # Required: Set via JWT_SECRET env var
   ```

4. **Run the application**:
   ```bash
   # Using Maven
   mvn clean spring-boot:run
   
   # Or build and run JAR
   mvn clean package
   java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/online-store-1.0-SNAPSHOT.jar
   ```

### Option 2: Manual Setup

1. **Install and start MySQL**:
   ```bash
   # Create database
   mysql -u root -p
   CREATE DATABASE online_store DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **Install and start Redis**:
   ```bash
   redis-server
   ```

3. **Configure application** (see step 3 above)

4. **Run the application** (see step 4 above)

## ⚙️ Configuration

### Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `JWT_SECRET` | Secret key for JWT token generation | - | ✅ |
| `ADMIN_USERNAME` | Admin username | `admin` | ❌ |
| `ADMIN_PASSWORD` | Admin password | `admin123` | ❌ |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `local` | ❌ |
| `NACOS_ENABLED` | Enable Nacos service discovery | `false` | ❌ |

### Application Profiles

The application supports multiple profiles for different environments:
- `local`: Local development (default)
- `dev`: Development environment
- `prod`: Production environment

Set the active profile via:
```bash
export SPRING_PROFILES_ACTIVE=dev
# or
java -Dspring.profiles.active=dev -jar online-store.jar
```

## 🔧 API Endpoints

The application runs on `http://localhost:8080` by default.

### Member Management
- `POST /members` - Register new member
- `GET /members/{id}` - Get member details
- `PUT /members/{id}` - Update member information

### Product Management
- `GET /items` - List all items
- `GET /items/{id}` - Get item details
- `POST /items` - Create new item
- `PUT /items/{id}` - Update item

### Category Management
- `GET /categories` - List all categories
- `POST /categories` - Create category

### Brand Management
- `GET /brands` - List all brands
- `POST /brands` - Create brand
- `GET /brands/{id}` - Get brand details

### Attribute Management
- `GET /attributes` - List all attributes
- `POST /attributes` - Create attribute

## 🐳 Docker Deployment

### Build Docker Image
```bash
docker build -t online-store:latest .
```

### Run with Docker Compose
```bash
# Start all services (app + MySQL + Redis)
docker-compose --profile all up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn clean test jacoco:report
```

## 📝 Database Schema

The application uses the following main tables:
- **member**: User/member information
- **item**: Product items
- **sku**: Stock keeping units
- **category**: Product categories
- **brand**: Product brands
- **attribute**: Product attributes
- **attribute_value**: Attribute values
- **item_attribute_relation**: Item-attribute relationships
- **item_access_log**: Item access tracking

All SQL schema files are located in `src/main/resources/sql/`.

## 🔒 Security

The application uses:
- **JWT (JSON Web Tokens)** for stateless authentication
- **Spring Security** for authorization and endpoint protection
- Password encryption using BCrypt
- Role-based access control (RBAC)

**Important**: Set the `JWT_SECRET` environment variable before running in production!

## 📚 Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [MyBatis Documentation](https://mybatis.org/mybatis-3/)
- [Nacos Documentation](https://nacos.io/docs/latest/what-is-nacos/)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License.

## 💬 Support

For issues and questions, please open an issue in the repository. 