# Course Management System

A secure full-stack Course Management System built using **Java, Spring Boot, Spring Security, MySQL, Redis, and JavaScript**.

The application provides REST APIs for managing courses, students, trainers, platforms, subscriptions, enrollments, and users, with authentication, authorization, API protection, caching, validation, exception handling, database migrations, and automated testing.

---

## 🚀 Key Features

* User authentication and authorization
* JWT-based security
* Access token and refresh token support
* Token versioning and token revocation
* Role-Based Access Control (RBAC)
* Custom Spring Security filters
* BCrypt password encryption
* DTO-based request and response handling
* Bean Validation
* Centralized exception handling
* Standardized API error responses
* Redis caching
* API rate limiting
* Flyway database migrations
* Swagger/OpenAPI documentation
* RESTful APIs
* Unit and integration testing
* Responsive frontend using HTML, CSS, and JavaScript

---

## 🛠️ Technologies Used

### Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* REST APIs
* Maven
* JWT
* BCrypt

### Database & Data Management

* MySQL
* Redis
* Flyway

### API Documentation & Testing

* Swagger / OpenAPI
* Postman
* JUnit
* Mockito

### Frontend

* HTML5
* CSS3
* JavaScript
* Responsive Web Design

### Development Tools

* IntelliJ IDEA
* Visual Studio Code
* Git
* GitHub

---

## 🔐 Security

The application implements multiple security mechanisms to protect APIs and user data.

### JWT Authentication

Users authenticate using their credentials and receive JWT-based authentication tokens.

The application supports:

* Access tokens
* Refresh tokens
* Token expiration
* Token versioning
* Token revocation
* JWT ID (`jti`) tracking

### Role-Based Access Control

Different user roles can access different protected resources.

Spring Security is used to authenticate requests and authorize users based on their assigned roles.

### Password Security

User passwords are stored using **BCrypt hashing** rather than plain text.

### Custom Security Filters

Custom filters are used for:

* JWT authentication
* Request logging
* API rate limiting

---

## ⚡ Performance & API Protection

### Redis Caching

Redis is used to cache frequently requested data and reduce unnecessary database queries.

Example:

```text
Client
   ↓
Course API
   ↓
Redis Cache
   ↓
Database
```

When cached data is available, the application can return the cached response instead of querying MySQL.

### API Rate Limiting

Rate limiting is implemented to control excessive API requests.

The application uses **Bucket4j and Redis** to help prevent request abuse and protect API resources.

---

## 🗄️ Database & Migration

The application uses **MySQL** for persistent data storage.

Database schema changes are managed using **Flyway** migrations.

Example migration structure:

```text
src/main/resources/db/migration/

├── V1__initial_schema.sql
└── V2__add_description_to_platforms.sql
```

Flyway maintains database version history and applies migrations in the correct order.

---

## 📚 Main Modules

The system contains REST API modules for:

* Authentication
* User Management
* Course Management
* Student Management
* Trainer Management
* Platform Management
* Subscription Management
* Enrollment Management

---

## 🏗️ Application Architecture

The backend follows a layered architecture:

```text
Client
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

### Controller Layer

Handles HTTP requests and responses.

### Service Layer

Contains business logic and application rules.

### Repository Layer

Handles database operations using Spring Data JPA.

### Model Layer

Contains the application's domain entities and enums.

### DTO Layer

Separates API request/response models from database entities.

### Security Layer

Handles JWT authentication, authorization, and security filters.

### Exception Layer

Provides centralized exception handling and consistent API error responses.

---

## 📁 Project Structure

```text
course-management-system/
│
├── backend/
│   │
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/spring/course/management/system/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── dto/
│   │   │   │       ├── exception/
│   │   │   │       ├── model/
│   │   │   │       ├── rate/
│   │   │   │       ├── repository/
│   │   │   │       ├── security/
│   │   │   │       └── service/
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── db/migration/
│   │   │       └── application-example.properties
│   │   │
│   │   └── test/
│   │
│   ├── pom.xml
│   └── mvnw
│
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── script.js
│
└── .gitignore
```

---

## 🧪 Testing

The backend includes tests covering different application layers, including:

* Controller tests
* Service tests
* Repository tests
* Security/JWT tests
* Integration tests

The project uses **JUnit and Mockito** for automated testing.

---

## 📖 API Documentation

Swagger/OpenAPI is integrated into the backend to document and explore the REST APIs.

Once the backend is running, the API documentation can be accessed through the application's Swagger endpoint.

---

## ⚙️ Local Setup

### Prerequisites

Make sure the following are installed:

* Java 21
* MySQL
* Redis
* Git
* Maven or Maven Wrapper

### 1. Clone the Repository

```bash
git clone https://github.com/chandru-venkatesan/course-management-system.git
```

```bash
cd course-management-system
```

### 2. Configure the Backend

Navigate to:

```text
backend/src/main/resources/
```

Create your local:

```text
application.properties
```

Use `application-example.properties` as a reference.

Add your own local MySQL credentials and JWT secret.

**Do not commit your real `application.properties` file or credentials to GitHub.**

### 3. Create the Database

Create the MySQL database:

```sql
CREATE DATABASE course_management_system;
```

Flyway will manage the required schema migrations when the application starts.

### 4. Start Redis

Make sure your Redis server is running locally.

The default configuration uses:

```text
Host: localhost
Port: 6379
```

### 5. Run the Backend

From the `backend` directory:

Windows:

```cmd
mvnw.cmd spring-boot:run
```

or, if Maven is installed:

```bash
mvn spring-boot:run
```

### 6. Run the Frontend

Open:

```text
frontend/index.html
```

using a browser or a local development server.

---

## 🔄 Application Flow

A typical secured API request follows this flow:

```text
Frontend / Postman
        ↓
HTTP Request
        ↓
Spring Security Filters
        ↓
JWT Authentication
        ↓
Authorization / RBAC
        ↓
Controller
        ↓
Service
        ↓
Redis Cache
        ↓
Repository
        ↓
MySQL
        ↓
Response
        ↓
Client
```

---

## 🔒 Configuration & Secrets

Sensitive configuration is intentionally excluded from the repository.

The following type of local configuration should not be committed:

```text
application.properties
```

Use:

```text
application-example.properties
```

as the template for local configuration.

Never expose:

* Database passwords
* JWT secrets
* API keys
* Production credentials

---

## 🎯 Project Highlights

This project demonstrates practical implementation of:

* Java object-oriented programming
* Spring Boot application development
* REST API development
* Spring Security
* JWT authentication
* Role-based authorization
* Database integration using JPA/Hibernate
* MySQL
* Redis caching
* API rate limiting
* Flyway migrations
* DTO validation
* Global exception handling
* API documentation
* Unit testing
* Integration testing
* Frontend-backend integration
* Git and GitHub version control

---

## 👨‍💻 Author

**Chandru Venkatesan**

Java Full Stack Developer

* GitHub: https://github.com/chandru-venkatesan
* LinkedIn: https://www.linkedin.com/in/chandru-venkatesan

---

## 📌 Project Status

The project is actively maintained and can be extended with additional features and improvements.
