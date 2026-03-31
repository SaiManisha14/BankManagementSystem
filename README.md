# SecureBank Backend - Spring Boot Banking API

A comprehensive Spring Boot REST API for banking management system with JWT authentication, role-based access control, and complete banking operations.

## 🚀 Features

### 🔐 Authentication & Security
- **JWT Token Authentication** with role-based access control
- **Spring Security** configuration with custom filters
- **Password Encryption** using BCrypt
- **Role-based Authorization** (ADMIN/USER roles)
- **CORS Configuration** for frontend integration
- **Request/Response Logging** for debugging

### 🏦 Banking Operations
- **User Management**: CRUD operations for users
- **Account Management**: Create and manage bank accounts
- **Transaction Processing**: Deposits, withdrawals, and transfers
- **Balance Management**: Real-time balance updates
- **Transaction History**: Complete audit trail

### 📊 Database Management
- **MySQL Database** integration
- **JPA/Hibernate** for ORM
- **Database Migrations** with proper schema
- **Connection Pooling** for performance
- **Transaction Management** for data consistency

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Security**: Spring Security 6.x with JWT
- **Database**: MySQL 8.0+
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven
- **Documentation**: Swagger/OpenAPI 3
- **Testing**: JUnit 5, Mockito
- **Logging**: SLF4J with Logback

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## 🔧 Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd banking-backend
```

### 2. Database Setup
```sql
-- Create database
CREATE DATABASE bank_account_db;

-- Create user (optional)
CREATE USER 'bankuser'@'localhost' IDENTIFIED BY 'bankpass123';
GRANT ALL PRIVILEGES ON bank_account_db.* TO 'bankuser'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Application Properties
```properties
# src/main/resources/application.properties

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/bank_account_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
jwt.secret=mySecretKey
jwt.expiration=86400000

# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Logging
logging.level.com.banking=DEBUG
logging.level.org.springframework.security=DEBUG
```

### 4. Build and Run
```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run

# Or build JAR and run
mvn clean package
java -jar target/banking-backend-1.0.0.jar
```

### 5. Verify Installation
- Application runs on: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- Health Check: `http://localhost:8080/api/actuator/health`

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── banking/
│   │           ├── BankingApplication.java
│   │           ├── config/
│   │           │   ├── SecurityConfig.java
│   │           │   ├── JwtConfig.java
│   │           │   └── CorsConfig.java
│   │           ├── controller/
│   │           │   ├── AuthController.java
│   │           │   ├── UserController.java
│   │           │   ├── AccountController.java
│   │           │   └── TransactionController.java
│   │           ├── service/
│   │           │   ├── AuthService.java
│   │           │   ├── UserService.java
│   │           │   ├── AccountService.java
│   │           │   └── TransactionService.java
│   │           ├── repository/
│   │           │   ├── UserRepository.java
│   │           │   ├── AccountRepository.java
│   │           │   └── TransactionRepository.java
│   │           ├── model/
│   │           │   ├── User.java
│   │           │   ├── Account.java
│   │           │   ├── Transaction.java
│   │           │   └── Role.java
│   │           ├── dto/
│   │           │   ├── LoginRequest.java
│   │           │   ├── LoginResponse.java
│   │           │   ├── CreateUserRequest.java
│   │           │   ├── CreateAccountRequest.java
│   │           │   └── TransactionRequest.java
│   │           ├── security/
│   │           │   ├── JwtAuthenticationFilter.java
│   │           │   ├── JwtTokenProvider.java
│   │           │   └── CustomUserDetailsService.java
│   │           └── exception/
│   │               ├── GlobalExceptionHandler.java
│   │               ├── ResourceNotFoundException.java
│   │               └── InsufficientBalanceException.java
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       ├── application-prod.properties
│       └── data.sql
└── test/
    └── java/
        └── com/
            └── banking/
                ├── controller/
                ├── service/
                └── repository/
```

## 🔒 API Endpoints

### Authentication Endpoints
```http
POST /api/auth/login
POST /api/auth/register
POST /api/auth/refresh
```

### User Management (Admin Only)
```http
GET    /api/users              # Get all users
GET    /api/users/{id}         # Get user by ID
POST   /api/users              # Create new user
PUT    /api/users/{id}         # Update user
DELETE /api/users/{id}         # Delete user
```

### Account Management
```http
GET    /api/accounts           # Get all accounts (Admin)
GET    /api/accounts/{id}      # Get account by ID
GET    /api/accounts/user/{userId}  # Get user accounts
POST   /api/accounts           # Create new account
PUT    /api/accounts/{id}      # Update account
DELETE /api/accounts/{id}      # Delete account
```

### Transaction Management
```http
GET    /api/transactions       # Get all transactions (Admin)
GET    /api/transactions/{id}  # Get transaction by ID
GET    /api/transactions/account/{accountId}  # Get account transactions
POST   /api/transactions       # Create transaction
```

### Dashboard & Analytics
```http
GET    /api/dashboard/summary  # Get dashboard summary (Admin)
GET    /api/dashboard/user/{userId}  # Get user dashboard data
```

## 📝 API Documentation

### Authentication

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "admin",
  "email": "admin@securebank.com",
  "roles": ["ADMIN"],
  "expiresIn": 86400
}
```

#### Register
```http
POST /api/auth/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

### Account Operations

#### Create Account
```http
POST /api/accounts
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 1,
  "accountType": "SAVINGS",
  "balance": 1000.00
}
```

**Response:**
```json
{
  "id": 1,
  "accountNumber": "ACC1234567890",
  "accountType": "SAVINGS",
  "balance": 1000.00,
  "userId": 1,
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### Transaction Operations

#### Create Transaction
```http
POST /api/transactions
Authorization: Bearer <token>
Content-Type: application/json

{
  "accountId": 1,
  "transactionType": "DEPOSIT",
  "amount": 500.00,
  "description": "Salary deposit"
}
```

**Response:**
```json
{
  "id": 1,
  "accountId": 1,
  "transactionType": "DEPOSIT",
  "amount": 500.00,
  "balanceAfter": 1500.00,
  "description": "Salary deposit",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

#### Transfer Money
```http
POST /api/transactions
Authorization: Bearer <token>
Content-Type: application/json

{
  "accountId": 1,
  "transactionType": "TRANSFER",
  "amount": 200.00,
  "toAccountNumber": "ACC0987654321",
  "description": "Transfer to friend"
}
```

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE auth_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Accounts Table
```sql
CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_type ENUM('SAVINGS', 'CURRENT') NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    user_id BIGINT NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'CLOSED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES auth_users(id) ON DELETE CASCADE
);
```

### Transactions Table
```sql
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    balance_after DECIMAL(15,2) NOT NULL,
    to_account_id BIGINT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
    FOREIGN KEY (to_account_id) REFERENCES accounts(id) ON DELETE SET NULL
);
```

## 🔐 Security Configuration

### JWT Configuration
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
```

### CORS Configuration
```java
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

## 🧪 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run tests with coverage
mvn test jacoco:report
```

### Test Structure
```
src/test/java/
├── controller/
│   ├── AuthControllerTest.java
│   ├── UserControllerTest.java
│   ├── AccountControllerTest.java
│   └── TransactionControllerTest.java
├── service/
│   ├── UserServiceTest.java
│   ├── AccountServiceTest.java
│   └── TransactionServiceTest.java
└── repository/
    ├── UserRepositoryTest.java
    ├── AccountRepositoryTest.java
    └── TransactionRepositoryTest.java
```

## 📊 Monitoring & Logging

### Application Monitoring
```properties
# Actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always
```

### Logging Configuration
```xml
<!-- logback-spring.xml -->
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <logger name="com.banking" level="DEBUG"/>
    <logger name="org.springframework.security" level="DEBUG"/>
    
    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

## 🚀 Deployment

### Development Environment
```bash
# Run with dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production Environment
```bash
# Build production JAR
mvn clean package -Pprod

# Run production JAR
java -jar -Dspring.profiles.active=prod target/banking-backend-1.0.0.jar
```

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/banking-backend-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
# Build Docker image
docker build -t banking-backend .

# Run container
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod banking-backend
```

## 🔧 Configuration

### Environment Variables
```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=bank_account_db
DB_USERNAME=root
DB_PASSWORD=password

# JWT
JWT_SECRET=mySecretKey
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080
```

### Profile-specific Properties

#### application-dev.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bank_account_db_dev
logging.level.com.banking=DEBUG
spring.jpa.show-sql=true
```

#### application-prod.properties
```properties
spring.datasource.url=jdbc:mysql://prod-server:3306/bank_account_db
logging.level.com.banking=INFO
spring.jpa.show-sql=false
```

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Issues**
   ```bash
   # Check MySQL service
   sudo systemctl status mysql
   
   # Test connection
   mysql -u root -p -h localhost
   ```

2. **JWT Token Issues**
   - Verify JWT secret configuration
   - Check token expiration settings
   - Ensure proper token format in requests

3. **CORS Issues**
   - Verify CORS configuration
   - Check allowed origins
   - Ensure preflight requests are handled

4. **Port Already in Use**
   ```bash
   # Find process using port 8080
   lsof -i :8080
   
   # Kill process
   kill -9 <PID>
   ```

### Debug Mode
```properties
# Enable debug logging
logging.level.com.banking=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web=DEBUG
```

## 📈 Performance Optimization

### Database Optimization
```properties
# Connection pooling
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000

# JPA optimization
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

### Caching
```java
@EnableCaching
@Configuration
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users", "accounts");
    }
}
```

## 🔮 Future Enhancements

- **Microservices Architecture** with Spring Cloud
- **Event-Driven Architecture** with Apache Kafka
- **Redis Caching** for improved performance
- **API Rate Limiting** for security
- **Audit Logging** for compliance
- **Notification Service** for real-time alerts
- **Batch Processing** for bulk operations
- **GraphQL API** for flexible queries
- **OAuth2 Integration** for third-party auth
- **Kubernetes Deployment** for scalability

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Contributors

- **Backend Development**: Spring Boot Banking API
- **Database Design**: MySQL Schema Design
- **Security Implementation**: JWT & Spring Security
- **API Documentation**: REST API Design

## 📧 Contact

For support or questions:
- **Email**: backend-support@securebank.com
- **Documentation**: Check the inline code comments
- **Issues**: Create GitHub issues for bugs or feature requests

---

**© 2026 SecureBank Backend. All Rights Reserved.**

*Built with Spring Boot and modern Java technologies for secure and scalable banking operations.*
