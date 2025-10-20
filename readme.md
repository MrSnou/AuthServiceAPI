# Project goal: practical mastery of Spring Boot Security, JWT, and REST API architecture.

# 🔐 Auth Service API

The Auth Service API is a backend application based on **Spring Boot**, used to **authenticate and authorize users** using **JWT (JSON Web Token)**.
The project was created to teach and practically apply key Spring security mechanisms.

---

## 🚀 Features

✅ New user registration (`/api/auth/register`)
✅ Login with JWT generation (`/api/auth/login`)
✅ Token refresh (`/api/auth/refresh`)
✅ Logout (deleting the JWT cookie) (`/api/auth/logout`)
✅ Token validation via `JwtAuthenticationFilter`
✅ Token support in both the **Authorization* header and cookies (`HttpOnly cookie`)
✅ Secure endpoints only for logged-in users

---

## 🧩 Technologies

- **Java 21**
- **Spring Boot 3+**
- **Spring Security**
- **Spring Web**
- **Spring Data JPA**
- **JWT (jjwt)**
- **H2 Database (dla testów)**
- **Maven**

---
