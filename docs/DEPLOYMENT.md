# Deployment Guide

1. Create a MySQL database named `bookstore_management`.
2. Set environment variables or edit `application-mysql.properties`.
3. Run schema from `database/schema.sql`.
4. Build the application:

```bash
cd backend
./mvnw clean package
```

5. Run with MySQL profile:

```bash
SPRING_PROFILES_ACTIVE=mysql java -jar target/bookstore-1.0.0.jar
```

For Render/Railway, set the same profile and database URL through environment variables.
