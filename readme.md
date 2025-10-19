# Cel projektu: praktyczne opanowanie Spring Boot Security, JWT oraz architektury REST API.

# 🔐 Auth Service API

Auth Service API to backendowa aplikacja oparta o **Spring Boot**, służąca do **uwierzytelniania i autoryzacji użytkowników** przy użyciu **JWT (JSON Web Token)**.  
Projekt został stworzony w celu nauki i praktycznego zastosowania kluczowych mechanizmów bezpieczeństwa Springa.

---

## 🚀 Funkcjonalności

✅ Rejestracja nowych użytkowników (`/api/auth/register`)  
✅ Logowanie z generowaniem JWT (`/api/auth/login`)  
✅ Odświeżanie tokenu (`/api/auth/refresh`)  
✅ Wylogowanie (usunięcie ciasteczka JWT) (`/api/auth/logout`)  
✅ Walidacja tokenów przez `JwtAuthenticationFilter`  
✅ Obsługa tokenów zarówno w **nagłówku `Authorization`**, jak i w **ciasteczkach** (`HttpOnly cookie`)  
✅ Bezpieczne endpointy tylko dla zalogowanych użytkowników

---

## 🧩 Technologie

- **Java 21**
- **Spring Boot 3+**
- **Spring Security**
- **Spring Web**
- **Spring Data JPA**
- **JWT (jjwt)**
- **H2 Database (dla testów)**
- **Maven**

---