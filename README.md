# 🧹 Clean Queen - Service Management System

<div align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.13-brightgreen?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Maven-3.9-red?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven">
  <img src="https://img.shields.io/badge/JWT-Security-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT">
</div>

<div align="center">
  <h3>🏢 Enterprise-Grade Cleaning Service Management Platform</h3>
  <p>A comprehensive Spring Boot application for managing cleaning services, customer requests, project assignments, and supervisor coordination.</p>
</div>

---

## 📋 Table of Contents

- [🌟 Features](#-features)
- [🏗️ Architecture](#️-architecture)
- [🚀 Quick Start](#-quick-start)
- [📚 API Documentation](#-api-documentation)
- [🔧 Configuration](#-configuration)
- [🧪 Testing](#-testing)
- [🚀 Deployment](#-deployment)
- [📊 Monitoring](#-monitoring)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)

---

## 🌟 Features

### 👥 **User Management**
- 🔐 **Multi-role Authentication** (Customer, Supervisor, Admin)
- 📧 **Email OTP Verification** with async processing
- 🔑 **JWT-based Security** with refresh tokens
- 👤 **Profile Management** with validation

### 📋 **Service Management**
- 🏠 **Cleaning Request Creation** with detailed requirements
- 📊 **Project Management** with status tracking
- 👷 **Supervisor Assignment** and coordination
- ⭐ **Review & Rating System** for quality assurance

### 🛡️ **Enterprise Security**
- 🔒 **Input Validation & Sanitization** (XSS protection)
- 🚫 **SQL Injection Prevention**
- 📝 **Comprehensive Exception Handling**
- 🏥 **Health Monitoring** with Spring Boot Actuator

### 🚀 **Performance Features**
- ⚡ **Async Email Processing** (5-20 thread pool)
- 🏊 **Database Connection Pooling** (HikariCP)
- 📦 **Hibernate Batch Processing** (20x performance boost)
- 📊 **Production Monitoring** endpoints

---

## 🏗️ Architecture

### 🧱 **Technology Stack**
```
Frontend:     RESTful APIs (Swagger/OpenAPI documented)
Backend:      Spring Boot 3.3.13 with Spring Security
Database:     MySQL 8.0+ with JPA/Hibernate
Security:     JWT Authentication + BCrypt password hashing
Monitoring:   Spring Boot Actuator + Custom metrics
Build:        Maven 3.9.x
Runtime:      Java 17 LTS (OpenJDK)
```

### 📐 **Project Structure**
```
src/main/java/com/enterprise/cleanqueen/
├── 🎮 controller/          # REST API endpoints
├── 📊 service/             # Business logic layer
├── 🗄️ repository/          # Data access layer
├── 📋 entity/              # JPA entities
├── 📄 dto/                 # Data transfer objects
├── ⚙️ config/              # Spring configurations
├── 🚨 exception/           # Global exception handling
├── 🔧 util/                # Utility classes
└── 📊 enums/               # Application enumerations
```

### 🔄 **API Endpoints Overview**
| Module | Endpoint | Description |
|--------|----------|-------------|
| 🔐 **Auth** | `/api/auth/*` | Registration, login, OTP verification |
| 👤 **Users** | `/api/users/*` | Profile management, user operations |
| 📋 **Requests** | `/api/requests/*` | Cleaning service requests |
| 📊 **Projects** | `/api/projects/*` | Project management & tracking |
| 👷 **Admin** | `/api/admin/*` | Administrative operations |
| ⭐ **Reviews** | `/api/reviews/*` | Customer feedback & ratings |
| 🏥 **Health** | `/api/actuator/*` | Monitoring & health checks |

---

## 🚀 Quick Start

### 📋 Prerequisites
```bash
☕ Java 17+ (OpenJDK recommended)
🗄️ MySQL 8.0+
🔧 Maven 3.6+
📧 Gmail account (for email functionality)
```

### 1️⃣ **Clone Repository**
```bash
git clone https://github.com/Sonal-deX/clean-queen-server.git
cd clean-queen-server
```

### 2️⃣ **Database Setup**
```sql
-- Create database
CREATE DATABASE cleanqueen_db;

-- Create user (optional)
CREATE USER 'cleanqueen_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON cleanqueen_db.* TO 'cleanqueen_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3️⃣ **Environment Configuration**
Create `.env` file in project root:
```bash
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/cleanqueen_db?createDatabaseIfNotExist=true&useSSL=false
DB_USERNAME=root
DB_PASSWORD=your_mysql_password

# JWT Security
JWT_SECRET=your-super-secure-256-bit-secret-key-here-make-it-long-and-random
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Email Configuration (Gmail)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Admin Configuration
ADMIN_EMAIL=admin@yourcompany.com

# OTP Configuration
OTP_EXPIRATION_MINUTES=5
OTP_LENGTH=6
```

### 4️⃣ **Build & Run**
```bash
# Install dependencies & build
mvn clean install

# Run the application
mvn spring-boot:run

# Alternative: Run JAR directly
java -jar target/cleanqueen-0.0.1-SNAPSHOT.jar
```

### 5️⃣ **Verify Installation**
```bash
# Health check
curl http://localhost:8080/api/actuator/health

# API documentation
open http://localhost:8080/api/swagger-ui.html
```

---

## 📚 API Documentation

### 🌐 **Interactive Documentation**
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

### 🔑 **Authentication Examples**

#### Register New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "SecurePass123",
    "role": "CUSTOMER",
    "phoneNumber": "+1234567890"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123"
  }'
```

#### Create Cleaning Request
```bash
curl -X POST http://localhost:8080/api/requests \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "John Doe",
    "phoneNumber": "+1234567890",
    "description": "Office deep cleaning required for 50-person workspace"
  }'
```

---

## 🔧 Configuration

### 🗄️ **Database Configuration**
```properties
# Development
spring.datasource.url=jdbc:mysql://localhost:3306/cleanqueen_db
spring.jpa.hibernate.ddl-auto=update

# Production
spring.datasource.url=${DB_URL}
spring.jpa.hibernate.ddl-auto=validate
```

### 🔒 **Security Configuration**
- JWT tokens expire in 24 hours (configurable)
- Refresh tokens expire in 7 days
- BCrypt password hashing with strength 10
- CORS enabled for frontend integration

### 📧 **Email Configuration**
- Async email processing with custom thread pool
- Gmail SMTP integration
- OTP generation and validation
- Email templates for different scenarios

---

## 🧪 Testing

### 🔬 **Run Tests**
```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=AuthControllerTest

# Integration tests
mvn verify
```

### 📊 **Test Coverage**
```bash
# Generate coverage report
mvn jacoco:report

# View report
open target/site/jacoco/index.html
```

### 🧪 **Test Database**
Uses H2 in-memory database for testing:
```properties
# test/resources/application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## 🚀 Deployment

### 🐳 **Docker Deployment**
```dockerfile
# Build JAR
mvn clean package -DskipTests

# Build Docker image
docker build -t cleanqueen:latest .

# Run with Docker Compose
docker-compose up -d
```

### ☁️ **Cloud Deployment**

#### **Heroku**
```bash
# Install Heroku CLI
heroku create your-app-name
heroku addons:create cleardb:ignite

# Deploy
git push heroku main
```

#### **AWS Elastic Beanstalk**
```bash
# Create application bundle
mvn clean package

# Deploy JAR file through EB console
# Configure environment variables in EB dashboard
```

### 🌍 **Environment Variables**
Set these in your hosting platform:
```bash
DB_URL=your_production_database_url
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password
JWT_SECRET=your_production_jwt_secret
MAIL_USERNAME=your_production_email
MAIL_PASSWORD=your_production_email_password
```

---

## 📊 Monitoring

### 🏥 **Health Endpoints**
```bash
# Application health
GET /api/actuator/health

# Application info
GET /api/actuator/info

# Metrics
GET /api/actuator/metrics
```

### 📈 **Performance Monitoring**
- **HikariCP**: Connection pool monitoring
- **JVM Metrics**: Memory, GC, thread monitoring
- **Custom Metrics**: Business logic monitoring
- **Logging**: Structured logging with correlation IDs

### 🚨 **Alerting**
Configure alerts for:
- High response times (>2s)
- Database connection issues
- Memory usage >80%
- Failed authentication attempts >10/min

---

## 🛡️ Security Features

### 🔒 **Input Validation**
- **Email validation** with regex patterns
- **Password strength** requirements
- **Phone number** format validation
- **XSS protection** via input sanitization
- **SQL injection** prevention with parameterized queries

### 🚫 **API Security**
- **Unknown JSON properties** rejected (400 error)
- **Malformed requests** handled gracefully
- **Rate limiting** ready (can be added)
- **HTTPS enforcement** in production

### 🔑 **Authentication Flow**
1. User registers → Email OTP sent
2. OTP verification → Account activated
3. Login → JWT + Refresh token issued
4. API access → JWT validation
5. Token refresh → New JWT issued

---

## 🤝 Contributing

### 📝 **Development Guidelines**
1. **Fork** the repository
2. **Create** feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** changes (`git commit -m 'Add amazing feature'`)
4. **Push** to branch (`git push origin feature/amazing-feature`)
5. **Open** Pull Request

### 🎯 **Code Standards**
- Follow **Spring Boot best practices**
- Use **meaningful variable names**
- Add **comprehensive JavaDoc**
- Include **unit tests** for new features
- Maintain **80%+ test coverage**

### 🧪 **Pull Request Checklist**
- [ ] Tests pass locally
- [ ] Code follows project style
- [ ] Documentation updated
- [ ] No security vulnerabilities
- [ ] Performance impact considered

---

## 📞 Contact & Support

### 👨‍💻 **Developer**
- **Name**: Sonal Attanayake
- **GitHub**: [@Sonal-deX](https://github.com/Sonal-deX)
- **Project**: [clean-queen-server](https://github.com/Sonal-deX/clean-queen-server)

### 🐛 **Issues & Bugs**
- Report issues: [GitHub Issues](https://github.com/Sonal-deX/clean-queen-server/issues)
- Security issues: Email privately

### 📖 **Documentation**
- API Docs: `/swagger-ui.html`
- Hosting Guide: `HOSTING_TECHNOLOGY_VERSIONS.md`
- Security Guide: `API_VALIDATION_SECURITY.md`

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **Spring Boot Team** for the excellent framework
- **MySQL** for reliable database solutions
- **JWT.io** for authentication standards
- **Swagger** for API documentation tools

---

<div align="center">
  <h3>⭐ If this project helped you, please give it a star! ⭐</h3>
  <p>Made with ❤️ for the cleaning service industry</p>
</div>

---

## 📊 Project Stats

![GitHub repo size](https://img.shields.io/github/repo-size/Sonal-deX/clean-queen-server)
![GitHub contributors](https://img.shields.io/github/contributors/Sonal-deX/clean-queen-server)
![GitHub last commit](https://img.shields.io/github/last-commit/Sonal-deX/clean-queen-server)
![GitHub issues](https://img.shields.io/github/issues/Sonal-deX/clean-queen-server)
![GitHub pull requests](https://img.shields.io/github/issues-pr/Sonal-deX/clean-queen-server)
