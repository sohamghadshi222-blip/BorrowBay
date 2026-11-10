# BorrowBay

**BorrowBay** is a peer-to-peer rental marketplace concept that helps people rent items they need and list items they own for others to rent.

## Project overview

This repository contains a Java Spring Boot application with a browser-based frontend served from Spring Boot's `static` resources.

### Technology stack

- Java 17
- Spring Boot 3
- Spring MVC (REST controllers)
- Spring Data JPA / Hibernate
- Spring Security
- MySQL
- HTML, CSS and JavaScript

## Current application areas

The source includes account registration and login endpoints, product listing management, booking endpoints, user/profile-related pages, rental/history pages, and frontend pages for cart and payment-related flows. The presence of a frontend page does not necessarily mean a production payment gateway is integrated.

## Getting started

### Prerequisites

- JDK 17 or later
- Maven 3.6+
- MySQL 8 or compatible MySQL server

### 1. Clone the repository

```bash
git clone https://github.com/YOUR-USERNAME/borrowbay.git
cd borrowbay
```

### 2. Create the database

Open MySQL and run:

```sql
CREATE DATABASE borrowbay;
```

### 3. Configure database credentials

Set these environment variables before starting the application. Do not commit real passwords.

**Windows PowerShell:**

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/borrowbay"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
$env:PORT="8081"
```

**macOS / Linux:**

```bash
export DB_URL="jdbc:mysql://localhost:3306/borrowbay"
export DB_USERNAME="root"
export DB_PASSWORD="your_mysql_password"
export PORT="8081"
```

You can use `.env.example` as a reference, but Spring Boot does not automatically load a `.env` file without an additional tool or library.

### 4. Run the application

```bash
mvn spring-boot:run
```

Open the application at:

```text
http://localhost:8081/start.html
```

The frontend pages are served from `src/main/resources/static/`.

## Repository structure

```text
BorrowBay/
├── src/
│   └── main/
│       ├── java/com/borrowbay/borrowbay/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── entity/
│       │   ├── repository/
│       │   ├── service/
│       │   └── BorrowbayApplication.java
│       └── resources/
│           ├── static/
│           └── application.properties
├── screenshots/
├── .env.example
├── .gitignore
├── pom.xml
└── README.md
```

## Important security and deployment notes

This is a development/portfolio project and should not be deployed publicly as-is. Review and harden authentication and authorization before deployment. In particular, the current `SecurityConfig` permits every request and disables CSRF protection. Restrict access to protected endpoints, validate ownership of products/bookings, and review session/token handling. Also use schema migrations and production-safe database settings before deployment.

## Screenshots

Add screenshots of the running application to the `screenshots/` directory and embed them here, for example:

```md
![BorrowBay home page](screenshots/homepage.png)
```

## License

No license has been selected yet. All rights are reserved by default unless a license is added to this repository.
