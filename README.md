# 🔐 SSecure

A complete authentication system built with **Spring Boot** that demonstrates secure authentication using **JWT**, **Spring Security**, **OAuth2**, and **Email Verification**.

---

## ✨ Features

- 🔑 User Registration & Login
- 🔐 JWT Authentication
- 🍪 HTTP Only Cookie-based Authentication
- 📧 Email Verification using OTP
- 🔄 Forgot Password & Reset Password
- 🔒 BCrypt Password Encryption
- 🌐 Google OAuth2 Login
- 🐙 GitHub OAuth2 Login
- 🚫 Provider Restriction (Google users can't login locally)
- ⚠️ Global Exception Handling
- 👤 User Profile API
- 🛡️ Spring Security Route Protection

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT
- OAuth2
- MySQL
- Thymeleaf
- JavaMailSender
- Lombok
- Maven

---

## 📂 Project Structure

```
src
 ├── configuration
 ├── controller
 ├── entities
 ├── filter
 ├── io
 ├── repo
 ├── service
 ├── util
 └── resources
```

---

## 🚀 Getting Started

1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/Authify.git
```

2. Configure MySQL

3. Configure OAuth Credentials

4. Configure Mail Credentials

5. Configure JWT Secret

6. Run the project

---

## 🔒 Security

Sensitive credentials such as:

- Gmail App Password
- JWT Secret
- Google Client Secret
- GitHub Client Secret

are **not included** in this repository.

---

## 📸 Screenshots

## 📸 Screenshots

### Login

![Login](src/main/java/com/project2/mailjwt/Screenshots/Login.png)

---

### Register

![Register](src/main/java/com/project2/mailjwt/Screenshots/Register.png)

---

### Dashboard

![Dashboard](src/main/java/com/project2/mailjwt/Screenshots/Dashboard.png)

---

### Verify Account

![Verify Account](src/main/java/com/project2/mailjwt/Screenshots/VerifyAccount.png)

---

### Forgot Password

![Forgot Password](src/main/java/com/project2/mailjwt/Screenshots/ForgotPassword.png)

---

### Reset Password

![Reset Password](src/main/java/com/project2/mailjwt/Screenshots/ResetPassword.png)

---

### Welcome Email

![Welcome Email](src/main/java/com/project2/mailjwt/Screenshots/WelcomeEmail.png)

---

## 👨‍💻 Author

**Sujal Sethi**

If you like this project, consider giving it a ⭐.
