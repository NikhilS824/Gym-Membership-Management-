# 🏋️ Gym Membership Management System

A modern, full-stack **Gym Membership Management System (GMMS)** designed to simplify and digitize gym operations such as member management, memberships, payments, attendance, trainers, expiry tracking, dashboards, and reports.

The system provides a secure and responsive interface for **Administrators and Staff** to efficiently manage day-to-day gym activities.

---

## 📌 Project Overview

Managing gym members, memberships, payments, attendance, and expiry dates manually can be time-consuming and error-prone.

The **Gym Membership Management System** provides a centralized platform where gym staff can manage all major gym operations from a single application.

### Key capabilities

* 👤 Member registration and management
* 🆔 Automatic Member ID generation
* 📋 Membership plan management
* 🔄 Membership renewal
* 💳 Payment management
* 📅 Attendance tracking
* 🏋️ Trainer management
* ⏰ Membership expiry alerts
* 📊 Dashboard and analytics
* 📈 Reports
* 🔍 Search, filtering, and pagination
* 🔐 Secure authentication and authorization
* 👥 Role-based access control
* 📱 Responsive user interface

---

## ✨ Features

### 🔐 Authentication & Security

* Secure login system
* JWT-based authentication
* BCrypt password hashing
* Role-based authorization
* ADMIN and STAFF roles
* Protected backend APIs
* Backend-side authorization
* Input validation and error handling

---

### 👤 Member Management

Staff and administrators can:

* Add new members
* View member details
* Update member information
* Search members
* Filter members
* View membership status
* Track membership history
* Automatically generate Member IDs

Example Member ID:

```text
GM00001
GM00002
GM00003
```

---

### 📋 Membership Management

The system supports multiple membership durations:

* Monthly
* Quarterly
* Half-Yearly
* Yearly
* Custom duration

Memberships are maintained separately from member records, allowing the system to track membership history and renewals efficiently.

### Renewal

When a membership is renewed, the system handles the existing membership period appropriately instead of unnecessarily losing remaining active time.

---

### 💳 Payment Management

The payment module allows gym staff to:

* Record membership payments
* Track payment amounts
* View payment history
* Associate payments with members and memberships
* Monitor membership-related transactions

---

### 📅 Attendance Management

The attendance module allows staff to:

* Check members in
* Track attendance records
* View attendance history
* Verify membership status before allowing check-in

Only members with an active membership can check in.

---

### ⚠️ Membership Verification

Members can be verified using their **Member ID**.

If a membership has expired, the system provides a clear warning to the staff instead of allowing normal attendance processing.

> Member ID is used as the primary member identification method.

---

### 🏋️ Trainer Management

Administrators and authorized staff can manage trainer information and associate trainers with gym operations.

---

### 📊 Dashboard

The dashboard provides an overview of important gym information, including:

* Total members
* Active memberships
* Expired memberships
* Recent payments
* Attendance information
* Membership statistics
* Other important operational metrics

---

### 📈 Reports & Analytics

The system provides useful reports and visual analytics to help gym management understand:

* Membership trends
* Attendance
* Payments
* Active vs expired memberships
* Gym activity

Charts and visualizations are provided through **Recharts**.

---

### 🔎 Search, Filter & Pagination

The application includes:

* Member search
* Filtering
* Pagination
* Membership status filtering
* Efficient data presentation

This makes it easier to manage a large number of gym members.

---

## 🛠️ Tech Stack

### Backend

| Technology        | Purpose                        |
| ----------------- | ------------------------------ |
| Java 17           | Backend programming language   |
| Spring Boot 3.2.4 | Backend framework              |
| Spring Web        | REST APIs                      |
| Spring Data JPA   | Database operations            |
| Hibernate         | ORM                            |
| Spring Security   | Authentication & authorization |
| JWT               | Token-based authentication     |
| BCrypt            | Password hashing               |
| Bean Validation   | Input validation               |
| Lombok            | Boilerplate reduction          |
| Maven             | Dependency management          |

### Frontend

| Technology       | Purpose              |
| ---------------- | -------------------- |
| React 19         | User interface       |
| Vite             | Frontend build tool  |
| JavaScript / JSX | Frontend development |
| React Router     | Client-side routing  |
| Axios            | API communication    |
| Tailwind CSS     | Styling              |
| Recharts         | Charts and analytics |
| Lucide React     | Icons                |

### Database

| Technology        | Purpose                 |
| ----------------- | ----------------------- |
| MySQL 8+          | Relational database     |
| MySQL Connector/J | Java-MySQL connectivity |



## 🔄 Application Workflow

```text
                    ┌───────────────────┐
                    │      User         │
                    └─────────┬─────────┘
                              │
                              ▼
                    ┌───────────────────┐
                    │ React Frontend    │
                    │   + Vite          │
                    └─────────┬─────────┘
                              │
                         REST API
                              │
                              ▼
                    ┌───────────────────┐
                    │ Spring Boot       │
                    │ Backend           │
                    └─────────┬─────────┘
                              │
                 ┌────────────┴────────────┐
                 │                         │
                 ▼                         ▼
        ┌─────────────────┐      ┌─────────────────┐
        │ Spring Security │      │ Service Layer   │
        │ JWT + BCrypt    │      │ Business Logic  │
        └─────────────────┘      └────────┬────────┘
                                          │
                                          ▼
                                ┌─────────────────┐
                                │ Spring Data JPA │
                                │    Hibernate    │
                                └────────┬────────┘
                                         │
                                         ▼
                                ┌─────────────────┐
                                │      MySQL      │
                                └─────────────────┘
```

---

## 🗄️ Database

The application uses **MySQL** as its relational database.

Create the database before starting the backend:

```sql
CREATE DATABASE gym_management;
```

The application connects to:

```text
localhost:3306/gym_management
```

Hibernate/JPA is configured to automatically update the database schema during development.

---

## ⚙️ Prerequisites

Make sure the following are installed:

* Java JDK 17+
* Maven
* Node.js
* npm
* MySQL 8+
* Git

Check the installations:

```bash
java -version
mvn -version
node -v
npm -v
mysql --version
git --version
```

---

# 🚀 Installation & Setup

## 1. Clone the Repository

```bash
git clone https://github.com/YOUR-USERNAME/gym-membership-management.git
```

Move into the project:

```bash
cd gym-membership-management
```

---

## 2. Configure MySQL

Start your MySQL server.

Create the database:

```sql
CREATE DATABASE gym_management;
```

---

## 3. Configure Backend

Open:

```text
backend/src/main/resources/application.properties
```

Configure your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gym_management
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

server.port=8080
```

> Replace `YOUR_PASSWORD` with your local MySQL password.

For production environments, credentials should be stored using environment variables or another secure configuration mechanism instead of committing them to GitHub.

---

## 4. Start the Backend

Open a terminal:

```bash
cd backend
```

Install/build the project:

```bash
mvn clean install
```

Run Spring Boot:

```bash
mvn spring-boot:run
```

The backend will run at:

```text
http://localhost:8080
```

---

## 5. Install Frontend Dependencies

Open another terminal:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

---

## 6. Start the Frontend

```bash
npm run dev
```

The frontend will normally be available at:

```text
http://localhost:5173
```

---

# 🔑 Authentication

The application uses:

```text
JWT Authentication
        +
Spring Security
        +
BCrypt Password Hashing
```

After successful login, the authenticated user receives a JWT token that is used for protected API requests.

The application supports two primary roles:

### ADMIN

Administrative users have access to management-level functionality such as:

* Member management
* Membership management
* Payment management
* Trainer management
* Reports
* Analytics
* System-level operations

### STAFF

Staff users can perform day-to-day gym operations according to their assigned permissions, such as:

* Member management
* Membership operations
* Attendance
* Payment operations
* Member verification

---

# 📡 API Structure

The backend follows a RESTful API architecture.

Main API areas include:

```text
/api/auth
/api/members
/api/memberships
/api/plans
/api/payments
/api/attendance
/api/trainers
/api/dashboard
/api/reports
```

> Exact endpoints may vary depending on the current implementation.

---

# 📱 Responsive Design

The frontend is designed to work across different screen sizes:

* 💻 Desktop
* 💻 Laptop
* 📱 Mobile
* 📟 Tablet

The interface uses Tailwind CSS for responsive styling and reusable UI components.

---

# 🛡️ Security

Security considerations implemented in the application include:

* JWT authentication
* BCrypt password hashing
* Role-based authorization
* Protected REST endpoints
* Input validation
* Centralized exception handling
* Backend authorization
* Secure authentication flow

Sensitive configuration such as database passwords should **not** be committed to the repository.

---

# 🚫 Identification Method

The system uses **Member IDs** for member identification and verification.

Example:

```text
GM00001
```

The project intentionally does **not** use QR codes or QR scanning.


# 🧪 Testing

Backend tests can be executed using:

```bash
mvn test
```

Frontend can be tested by running the development server:

```bash
npm run dev
```

API endpoints can also be tested using tools such as Postman or through the frontend application.

---

# 🐛 Error Handling

The backend includes centralized exception handling for common application errors such as:

* Invalid requests
* Validation errors
* Unauthorized requests
* Resource not found
* Duplicate records
* Authentication failures
* Database-related errors

---

# 📈 Future Improvements

Possible future improvements include:

* Email notifications for membership expiry
* Automated payment reminders
* Advanced financial reports
* Subscription analytics
* Export reports to PDF/Excel
* Cloud deployment
* Docker support
* Automated CI/CD pipeline
* Advanced staff permission management
* Member profile photos
* Workout and fitness tracking
* Mobile application

---

# 🤝 Contributing

Contributions are welcome.

1. Fork the repository.
2. Create a new branch:

```bash
git checkout -b feature/your-feature
```

3. Make your changes.
4. Commit your changes:

```bash
git add .
git commit -m "Add your feature"
```

5. Push the branch:

```bash
git push origin feature/your-feature
```

6. Open a Pull Request.

---

# 📄 License

This project is intended for educational and portfolio purposes.

You may modify and extend the project according to your requirements.

---

# 👨‍💻 Author

**Nikhil Suryawanshi**

IT Engineering Student
Interested in Full-Stack Development, Software Engineering, and AI/ML.

---

## ⭐ Support

If you find this project useful or interesting, consider giving the repository a ⭐ on GitHub.

---

**Built with ❤️ using Java, Spring Boot, React, and MySQL.**
