# IU Bookstore Management System

## 1. Project Overview

**IU Bookstore Management System** is a web-based bookstore management application developed for the **Web Application Development Final Project**.

The system is designed to support the essential operations of an academic bookstore, including:

- Book catalog browsing
- User registration and authentication
- Email verification
- Shopping cart management
- Order tracking
- Book reservation management
- User role management
- Administrative monitoring

The application is built using **Java Spring Boot** and follows a **server-side rendered web architecture** with **Thymeleaf**. It uses **MySQL** as the main relational database and **Spring Security** for authentication and authorization.

The system applies **role-based access control** to distinguish between customers, staff, managers, and administrators.

This project demonstrates important web development concepts such as **MVC architecture**, **relational database design**, **authentication**, **authorization**, **form validation**, **email verification**, **JDBC-based data access**, and **secure configuration management**.

---

## 2. Project Objectives

The main objectives of this project are:

- To build a complete bookstore management web application.
- To implement user authentication and role-based authorization.
- To design a relational database for bookstore operations.
- To support customer features such as catalog browsing, cart management, order viewing, and book reservations.
- To support manager features such as book, order, and reservation management.
- To support administrator features such as user management and audit log monitoring.
- To implement real email verification using Gmail SMTP.
- To demonstrate secure password hashing using BCrypt.
- To apply a layered architecture including controller, service, repository, model, and view layers.
- To provide a maintainable and extensible codebase for future improvement.

---

## 3. Technology Stack

| Category              | Technology                   |
| --------------------- | ---------------------------- |
| Programming Language  | Java 17                      |
| Backend Framework     | Spring Boot 3.3.5            |
| Web Framework         | Spring MVC                   |
| Template Engine       | Thymeleaf                    |
| Security Framework    | Spring Security              |
| Database Access       | Spring JDBC                  |
| Main Database         | MySQL                        |
| Development Database  | H2                           |
| Build Tool            | Maven                        |
| Frontend Technologies | HTML, CSS, JavaScript        |
| Email Service         | Spring Boot Mail, Gmail SMTP |
| Containerization      | Docker Compose               |
| Version Control       | Git and GitHub               |

---

## 4. System Architecture

The application follows a **layered server-side architecture**.

### 4.1 General Request Flow

```text
User
  ↓
Browser
  ↓
Spring Security Filter Chain
  ↓
Controller Layer
  ↓
Service Layer
  ↓
Repository Layer
  ↓
MySQL Database
  ↓
Repository Result
  ↓
Controller Model
  ↓
Thymeleaf Template
  ↓
HTML Response
```

### 4.2 Architecture Explanation

This architecture separates responsibilities clearly:

- **Controller Layer** handles HTTP requests and returns views.
- **Service Layer** contains business logic and reusable operations.
- **Repository Layer** communicates directly with the database.
- **View Layer** renders the user interface using Thymeleaf templates.
- **Spring Security** controls authentication and authorization.

This structure improves maintainability, readability, scalability, and separation of concerns.

---

## 5. Main Application Layers

### 5.1 Controller Layer

The controller layer receives HTTP requests, validates input data, communicates with services or repositories, and returns Thymeleaf views.

| Controller               | Responsibility                                                |
| ------------------------ | ------------------------------------------------------------- |
| `AuthController`         | Handles login, registration, and email verification           |
| `CatalogController`      | Handles book catalog and book detail pages                    |
| `CartController`         | Handles shopping cart operations                              |
| `OrderController`        | Handles customer orders                                       |
| `ReservationController`  | Handles customer reservations                                 |
| `ProfileController`      | Handles user profile and password management                  |
| `NotificationController` | Handles user notifications                                    |
| `ManagerController`      | Handles manager-level book, order, and reservation management |
| `AdminController`        | Handles user management and audit logs                        |
| `HomeController`         | Handles home page and dashboard navigation                    |

---

### 5.2 Service Layer

The service layer contains business-related logic and reusable operations.

| Service              | Responsibility                                       |
| -------------------- | ---------------------------------------------------- |
| `BookstoreService`   | Handles bookstore-related business operations        |
| `CurrentUserService` | Retrieves the currently authenticated user           |
| `EmailService`       | Sends email verification messages through Gmail SMTP |

---

### 5.3 Repository Layer

The repository layer communicates directly with the database using `JdbcTemplate`.

| Repository               | Responsibility                                                             |
| ------------------------ | -------------------------------------------------------------------------- |
| `UserRepository`         | Handles user lookup, registration, role assignment, and email verification |
| `CatalogRepository`      | Handles book, category, and author queries                                 |
| `OrderRepository`        | Handles order-related database operations                                  |
| `ReservationRepository`  | Handles reservation-related database operations                            |
| `NotificationRepository` | Handles notification records                                               |
| `AuditLogRepository`     | Handles audit log records                                                  |

---

### 5.4 View Layer

The view layer is implemented using Thymeleaf templates.

| Template                    | Purpose                                 |
| --------------------------- | --------------------------------------- |
| `home.html`                 | Landing page                            |
| `login.html`                | Login page                              |
| `register.html`             | Registration page                       |
| `catalog.html`              | Book catalog page                       |
| `book-details.html`         | Book detail page                        |
| `cart.html`                 | Shopping cart page                      |
| `orders.html`               | Customer order page                     |
| `reservations.html`         | Customer reservation page               |
| `profile.html`              | Profile management page                 |
| `dashboard.html`            | Dashboard for staff, manager, and admin |
| `manager-books.html`        | Book management page                    |
| `manager-orders.html`       | Order management page                   |
| `manager-reservations.html` | Reservation management page             |
| `admin-users.html`          | User management page                    |
| `audit.html`                | Audit log page                          |

---

## 6. Main Features

### 6.1 User Registration

The system allows new users to register an account. The registration form validates important information such as full name, email, phone number, password, and password confirmation.

Registration includes:

- Full name validation
- Email format validation
- Duplicate email checking
- Vietnamese phone number validation
- Password strength validation
- Password confirmation checking
- BCrypt password hashing
- Default customer role assignment
- Email verification before login

---

### 6.2 Email Verification

The system supports real email verification using Gmail SMTP.

#### 6.2.1 Email Verification Flow

```text
User registers a new account
  ↓
System creates the account with email_verified = false
  ↓
System generates a unique verification token
  ↓
System sends a verification link to the user's email address
  ↓
User clicks the verification link
  ↓
System validates the token
  ↓
System updates email_verified = true
  ↓
User can log in successfully
```

This feature improves security by ensuring that the registered email address is valid and accessible by the user.

---

### 6.3 Login and Authentication

The system uses Spring Security for authentication. Users log in using their email address and password.

Passwords are stored securely using **BCrypt hashing**.

A user cannot log in if:

- The email does not exist.
- The password is incorrect.
- The account status is inactive.
- The email has not been verified.

---

### 6.4 Role-Based Authorization

The system supports multiple roles.

| Role       | Main Permissions                                                       |
| ---------- | ---------------------------------------------------------------------- |
| `CUSTOMER` | Browse catalog, manage cart, place orders, reserve books, view profile |
| `STAFF`    | Access dashboard and operational pages                                 |
| `MANAGER`  | Manage books, categories, authors, orders, coupons, and reservations   |
| `ADMIN`    | Manage users and view audit logs                                       |

#### 6.4.1 Access Control Rules

| URL Pattern                       | Access Permission         |
| --------------------------------- | ------------------------- |
| `/login`                          | Public                    |
| `/register`                       | Public                    |
| `/verify-email`                   | Public                    |
| `/css/**`, `/js/**`, `/images/**` | Public                    |
| `/admin/**`                       | ADMIN only                |
| `/manager/**`                     | ADMIN and MANAGER         |
| `/dashboard`                      | ADMIN, MANAGER, and STAFF |
| Other pages                       | Authenticated users only  |

---

### 6.5 Book Catalog

Users can browse books in the catalog. Each book card displays essential information such as title, category, stock quantity, price, and action buttons.

Catalog functions include:

- View book list
- View book details
- Add book to cart
- Display stock availability
- Display book price
- Show book cover image

---

### 6.6 Shopping Cart

Authenticated customers can add books to their cart and proceed to checkout.

Cart functions include:

- Add book to cart
- View cart items
- Remove items from cart
- Checkout selected books

---

### 6.7 Orders

Customers can view their orders. Managers can monitor and update order-related information.

Order data includes:

- Order number
- Customer information
- Order status
- Payment status
- Shipment status
- Order items
- Subtotal
- Discount amount
- Grand total

---

### 6.8 Reservations

Customers can reserve books. Managers can review and process reservation requests.

Reservation data includes:

- User
- Book
- Quantity
- Reservation status
- Reserved date
- Expiration date
- Notification status

---

### 6.9 Book Management

Managers can manage catalog data.

Book management functions include:

- Add new books
- Edit existing books
- Delete books
- Manage categories
- Manage authors
- Export book data to CSV

---

### 6.10 User Management

Administrators can manage user accounts.

Admin functions include:

- View all users
- Update user status
- Update user role
- Monitor user-related activities

---

### 6.11 Notifications

The system supports notification records for users. Notifications may be used for reservation updates, system messages, or account-related information.

---

### 6.12 Audit Logs

Audit logs record important system activities. This feature supports accountability and administrative supervision.

---

## 7. Database Design

The database is designed using a relational model. It includes entities for users, roles, permissions, books, authors, categories, carts, orders, reservations, payments, shipments, notifications, reviews, inventory transactions, and audit logs.

---

### 7.1 Main Tables

| Table                      | Description                      |
| -------------------------- | -------------------------------- |
| `app_user`                 | Stores user account information  |
| `role`                     | Stores user roles                |
| `user_role`                | Maps users to roles              |
| `permission`               | Stores permission definitions    |
| `role_permission`          | Maps roles to permissions        |
| `address`                  | Stores user addresses            |
| `category`                 | Stores book categories           |
| `author`                   | Stores author information        |
| `book`                     | Stores book information          |
| `book_author`              | Maps books to authors            |
| `coupon`                   | Stores discount coupons          |
| `promotion`                | Stores promotions                |
| `cart`                     | Stores user shopping carts       |
| `cart_item`                | Stores cart items                |
| `wishlist`                 | Stores user wishlists            |
| `wishlist_item`            | Stores wishlist items            |
| `reservation`              | Stores book reservation requests |
| `customer_order`           | Stores order headers             |
| `order_item`               | Stores order details             |
| `payment`                  | Stores payment records           |
| `shipment`                 | Stores shipment records          |
| `inventory_transaction`    | Stores stock movement records    |
| `review`                   | Stores customer reviews          |
| `notification`             | Stores user notifications        |
| `audit_log`                | Stores system audit logs         |
| `email_verification_token` | Stores email verification tokens |

---

### 7.2 Email Verification Table

The email verification function uses the table `email_verification_token`.

```sql
CREATE TABLE IF NOT EXISTS email_verification_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at DATETIME NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_email_token_user
        FOREIGN KEY (user_id) REFERENCES app_user(id)
        ON DELETE CASCADE
);
```

---

### 7.3 Main Database Relationships

| Relationship                                     | Type         |
| ------------------------------------------------ | ------------ |
| `app_user` to `address`                          | One-to-many  |
| `app_user` to `cart`                             | One-to-many  |
| `app_user` to `customer_order`                   | One-to-many  |
| `app_user` to `reservation`                      | One-to-many  |
| `app_user` to `notification`                     | One-to-many  |
| `app_user` to `audit_log`                        | One-to-many  |
| `app_user` to `role` through `user_role`         | Many-to-many |
| `role` to `permission` through `role_permission` | Many-to-many |
| `book` to `author` through `book_author`         | Many-to-many |
| `cart` to `book` through `cart_item`             | Many-to-many |
| `wishlist` to `book` through `wishlist_item`     | Many-to-many |

---

## 8. Security Design

### 8.1 Password Hashing

User passwords are not stored in plain text. The system uses `BCryptPasswordEncoder` with strength `12`.

```java
new BCryptPasswordEncoder(12)
```

This increases the computational cost of password hashing and improves protection against brute-force attacks.

---

### 8.2 Account Verification

New users are created with:

```text
email_verified = false
```

They must verify their email before they can log in.

---

### 8.3 Public and Protected Routes

The application allows public access to:

- Login page
- Registration page
- Email verification link
- Static resources

Other pages require authentication or specific roles.

---

### 8.4 Sensitive Configuration

Sensitive information must not be committed to GitHub.

Examples of sensitive information:

```properties
spring.datasource.password
spring.mail.username
spring.mail.password
```

The Gmail password used in this project must be a **Gmail App Password**, not the normal Gmail account password.

---

## 9. Project Structure

```text
bookstore-project-web/
│
├── database/
│   ├── schema.sql
│   ├── seed-data.sql
│   └── ERD1.svg
│
├── docs/
│   ├── API.md
│   ├── DEPLOYMENT.md
│   └── DESIGN_NOTES.md
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       ├── BookstoreApplication.java
│       │       ├── config/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── model/
│       │       ├── repository/
│       │       └── service/
│       │
│       └── resources/
│           ├── static/
│           │   ├── css/
│           │   ├── js/
│           │   └── images/
│           │
│           ├── templates/
│           ├── application.properties
│           └── application-mysql.properties
│
├── docker-compose.yml
├── pom.xml
├── mvnw
└── README.md
```

---

## 10. Prerequisites

Before running the project, install the following software:

- Java Development Kit 17
- Maven
- MySQL Server or Docker Desktop
- MySQL Workbench
- Visual Studio Code or another Java IDE
- Git
- Gmail account with App Password enabled

---

## 11. Application Configuration

### 11.1 `application.properties`

```properties
spring.application.name=bookstore-management
spring.thymeleaf.cache=false
spring.profiles.active=mysql
server.error.whitelabel.enabled=false
logging.level.com=INFO
```

---

### 11.2 `application-mysql.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/bookstore_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=bookstore_user
spring.datasource.password=bookstore_pass
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.sql.init.mode=never

app.base-url=http://localhost:8080

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL@gmail.com
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

> Important: `spring.mail.password` must be a Gmail App Password, not the normal Gmail password.

---

## 12. Running the Database with Docker

The project can run MySQL using Docker Compose.

### 12.1 Start MySQL

```bash
docker compose up -d
```

### 12.2 Check Running Containers

```bash
docker ps
```

### 12.3 Database Connection Information

| Item     | Value                  |
| -------- | ---------------------- |
| Host     | `127.0.0.1`            |
| Port     | `3307`                 |
| Username | `root`                 |
| Database | `bookstore_management` |

In MySQL Workbench, connect using port `3307`.

---

## 13. Running the Spring Boot Application

From the project root folder, run:

```bash
mvn clean spring-boot:run
```

If the application starts successfully, the terminal should show:

```text
Tomcat started on port 8080
Started BookstoreApplication
```

Then open:

```text
http://localhost:8080
```

---

## 14. Demo Accounts

The system may include seeded demo accounts.

| Role     | Email               | Password       |
| -------- | ------------------- | -------------- |
| Admin    | `admin@test.com`    | `Admin123!`    |
| Manager  | `manager@test.com`  | `Manager123!`  |
| Staff    | `staff@test.com`    | `Staff123!`    |
| Customer | `customer@test.com` | `Customer123!` |

Additional customer accounts may include:

```text
customer1@test.com
customer2@test.com
customer3@test.com
...
customer24@test.com
```

Default password:

```text
Customer123!
```

---

## 15. How to Use the System

### 15.1 Customer Workflow

1. Open the application.
2. Register a new account.
3. Verify the email by clicking the verification link sent to Gmail.
4. Log in.
5. Browse the book catalog.
6. View book details.
7. Add books to cart.
8. Checkout.
9. View orders.
10. Reserve books.
11. Manage profile information.

---

### 15.2 Manager Workflow

1. Log in as manager.
2. Access the dashboard.
3. Manage books, categories, authors, and coupons.
4. View and update orders.
5. Review and process reservations.
6. Export book data when needed.

---

### 15.3 Admin Workflow

1. Log in as admin.
2. Access the dashboard.
3. Manage users.
4. Update user roles and status.
5. View audit logs.
6. Monitor system-level activities.

---

## 16. Email Verification Demonstration

To demonstrate the email verification function:

1. Register a new account using a real Gmail address.
2. The system creates the account with `email_verified = 0`.
3. The system sends a verification email.
4. Open Gmail and click the verification link.
5. The system validates the token.
6. The system updates `email_verified = 1`.
7. The user can log in successfully.

### 16.1 Check User Verification Status

```sql
USE bookstore_management;

SELECT id, email, email_verified
FROM app_user
ORDER BY id DESC;
```

### 16.2 Check Verification Tokens

```sql
SELECT id, user_id, token, expires_at, used, created_at
FROM email_verification_token
ORDER BY id DESC;
```

After successful verification:

```text
email_verified = 1
used = 1
```

---

## 17. Useful SQL Commands

### 17.1 Show All Tables

```sql
USE bookstore_management;
SHOW TABLES;
```

### 17.2 Check Recent Users

```sql
SELECT id, email, email_verified, created_at
FROM app_user
ORDER BY id DESC
LIMIT 10;
```

### 17.3 Check Verification Tokens

```sql
SELECT id, user_id, token, expires_at, used, created_at
FROM email_verification_token
ORDER BY id DESC
LIMIT 10;
```

### 17.4 Reuse a Gmail Address for Testing

```sql
UPDATE app_user
SET email = CONCAT('deleted_', id, '_', email)
WHERE email = 'your_email@gmail.com';
```

### 17.5 Use Gmail Aliases for Repeated Testing

```text
your_email+test1@gmail.com
your_email+test2@gmail.com
your_email+bookstore@gmail.com
```

All of these emails still arrive in the same Gmail inbox, but the system treats them as different email addresses.

---

## 18. Git and Collaboration Guidelines

### 18.1 Recommended Branch Structure

| Branch   | Purpose                                              |
| -------- | ---------------------------------------------------- |
| `main`   | Stable branch for final submission and demonstration |
| `dev`    | Integration branch for completed features            |
| `feat/*` | Feature branches for individual development tasks    |

### 18.2 Example Workflow

```bash
git checkout dev
git pull origin dev
git checkout -b feat/email-verification
```

### 18.3 Example Commit Messages

```bash
git commit -m "feat(auth): add email verification token flow"
git commit -m "feat(mail): send verification email through Gmail SMTP"
git commit -m "fix(security): allow public access to verify-email endpoint"
git commit -m "docs(readme): add setup and demo instructions"
```

### 18.4 Files Safe to Push

```text
src/main/java/com/controller/AuthController.java
src/main/java/com/repository/UserRepository.java
src/main/java/com/service/EmailService.java
src/main/java/com/config/SecurityConfig.java
src/main/resources/templates/
src/main/resources/static/
database/schema.sql
pom.xml
README.md
```

### 18.5 Files That Should Not Contain Real Secrets

```text
src/main/resources/application-mysql.properties
src/main/resources/application.properties
```

### 18.6 Recommended `.gitignore` Entries

```gitignore
src/main/resources/application-mysql.properties
.env
*.env
target/
```

A safe approach is to push an example configuration file such as:

```text
application-mysql.example.properties
```

and keep the real `application-mysql.properties` file local.

---

## 19. Troubleshooting

### 19.1 Application Cannot Connect to MySQL

Check whether Docker is running:

```bash
docker ps
```

Check database connection settings:

| Item     | Value                  |
| -------- | ---------------------- |
| Host     | `127.0.0.1`            |
| Port     | `3307`                 |
| Database | `bookstore_management` |

Check MySQL port:

```sql
SHOW VARIABLES LIKE 'port';
```

---

### 19.2 Browser Shows `ERR_CONNECTION_REFUSED`

This means the Spring Boot application is not running.

Run:

```bash
mvn spring-boot:run
```

Then open:

```text
http://localhost:8080/login
```

---

### 19.3 Email Already Registered

The system prevents duplicate email registration.

Solution 1: Use a Gmail alias.

```text
your_email+test1@gmail.com
```

Solution 2: Update the old email in the database.

```sql
UPDATE app_user
SET email = CONCAT('deleted_', id, '_', email)
WHERE email = 'your_email@gmail.com';
```

---

### 19.4 `JavaMailSender` Bean Not Found

Make sure `pom.xml` contains:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

Also make sure `application-mysql.properties` contains:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL@gmail.com
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

### 19.5 Gmail Authentication Failed

Possible causes:

- The normal Gmail password was used instead of an App Password.
- Two-Step Verification is not enabled.
- The App Password was copied incorrectly.
- Gmail blocked the SMTP request.

Solutions:

1. Enable Two-Step Verification.
2. Create a new Gmail App Password.
3. Replace `spring.mail.password`.
4. Restart the application.

---

## 20. Academic Relevance

This project demonstrates several important Web Application Development concepts:

- Client-server architecture
- MVC design pattern
- Server-side rendering with Thymeleaf
- Relational database design
- Foreign key relationships
- Authentication and authorization
- Password hashing with BCrypt
- Role-based access control
- Form validation with Jakarta Validation
- Email verification using SMTP
- JDBC-based data access
- Docker-based database deployment
- Layered architecture
- Separation of concerns
- Secure configuration management

---

## 21. Limitations and Future Improvements

Although the system implements the major functions of a bookstore management application, several improvements can be made in the future:

- Add online payment gateway integration.
- Add password reset through email.
- Add OTP-based email verification as an alternative to verification links.
- Add advanced search and filtering by author, category, price range, and publication year.
- Add pagination and sorting to all management tables.
- Add unit tests and integration tests.
- Add REST API endpoints for mobile or frontend framework integration.
- Add better reporting for inventory and sales.
- Add user activity analytics.
- Improve production deployment using environment variables.
- Improve error handling and logging.
- Add stronger CSRF protection configuration for production.

---

## 22. Conclusion

The **IU Bookstore Management System** is a complete academic web application that demonstrates the practical use of **Spring Boot, Thymeleaf, Spring Security, JDBC, MySQL, Docker, and Gmail SMTP**.

It implements important bookstore operations such as catalog browsing, cart management, order management, reservations, user management, role-based access control, audit logging, and email verification.

The project reflects key principles of web application development, including layered architecture, secure authentication, relational database modeling, server-side rendering, and maintainable code organization.

Therefore, the system is suitable for academic demonstration and can be extended into a more advanced bookstore or library management platform in the future.

---

## 23. Authors

| Name            | Student ID  | Role                    |
| --------------- | ----------- | ----------------------- |
| Nguyễn Thái Bảo | ITCSIU24013 | Developer / Contributor |
| Trần Khánh Bình | ITCSIU24015 | Developer / Contributor |

---

## 24. Course Information

| Item             | Description                                                              |
| ---------------- | ------------------------------------------------------------------------ |
| Course           | Web Application Development                                              |
| Project          | Final Project                                                            |
| Institution      | International University - Vietnam National University, Ho Chi Minh City |
| Topic            | Bookstore Management System                                              |
| Technology Stack | Spring Boot, Thymeleaf, Spring Security, JDBC, MySQL                     |
