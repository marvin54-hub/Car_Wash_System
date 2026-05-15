# 🚗 Witbank Elite Car Wash System

A full-stack web-based management system built with **Java Spring Boot** for **Witbank Elite Car Wash**. The system allows customers to book car wash services online and provides administrators with full control over bookings, inventory, notifications, and reporting.

---

## 📋 Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Team](#team)

---

## 📌 About the Project

The Witbank Elite Car Wash System is designed to streamline the car wash booking process for both customers and staff. Customers can visit the website, choose a service package, and submit a booking. The admin dashboard gives staff real-time visibility into all bookings, revenue, inventory levels, and notification logs — all in one place.

---

## ✅ Features

### 👤 Customer Side
- Browse available service packages (e.g. Express Shine, Elite Detail, Platinum Ceramic)
- Book a car wash by providing name, cellphone number, email, service type, and preferred date/time
- Receive an instant on-screen booking confirmation

### 🛠️ Admin Dashboard
- View all customer bookings with current status (Pending, Completed, Cancelled)
- Update or delete bookings
- Track total revenue and booking counts per service type
- View individual customer visit history and statistics

### 📦 Inventory Management
- Add, update, and delete inventory items (e.g. soap, wax, microfibre cloths)
- Automatic low-stock alerts when item quantities fall below a set threshold

### 📢 Notifications
- Send manual SMS or email notifications to customers directly from the dashboard
- Automatic notification logging for every dispatch action
- Email delivery configured via Gmail SMTP and EmailJS browser layer

### 📊 Reports
- Export a printable booking and revenue summary report
- Report includes total bookings, revenue breakdown, and service statistics

---

## 🛠️ Tech Stack

| Layer        | Technology                        |
|--------------|-----------------------------------|
| Backend      | Java 17, Spring Boot              |
| Frontend     | Thymeleaf, HTML5, CSS3, JavaScript|
| Database     | H2 (file-based persistent storage)|
| Email        | Gmail SMTP / EmailJS              |
| Build Tool   | Maven                             |
| IDE Support  | VS Code (`.vscode` config included)|

---

## 📁 Project Structure

```
Car_Wash_System/
├── src/
│   └── main/
│       ├── java/com/witbank/carwash/
│       │   ├── CarWashApplication.java        # App entry point
│       │   ├── controller/
│       │   │   ├── BookingController.java     # Handles customer bookings
│       │   │   └── AdminController.java       # Handles admin dashboard
│       │   ├── model/
│       │   │   ├── Booking.java               # Booking entity
│       │   │   ├── ServicePackage.java        # Service package entity
│       │   │   ├── InventoryItem.java         # Inventory item entity
│       │   │   └── NotificationLog.java       # Notification log entity
│       │   ├── repository/                    # Spring Data JPA repositories
│       │   └── service/
│       │       ├── BookingService.java        # Booking business logic
│       │       └── NotificationService.java   # Notification dispatch logic
│       └── resources/
│           ├── templates/
│           │   ├── index.html                 # Customer booking page
│           │   ├── admin.html                 # Admin dashboard
│           │   └── report_print.html          # Printable report page
│           ├── static/
│           │   ├── css/style.css
│           │   └── js/main.js
│           └── application.properties         # App configuration
├── data/
│   └── carwashdb.mv.db                        # H2 persistent database file
└── REFLECTIONS/                               # Team reflection documents
```

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

- [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- A Gmail account (for email notifications)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/car-wash-system.git
   cd car-wash-system
   ```

2. **Configure email settings** in `src/main/resources/application.properties`:
   ```properties
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password-here
   ```
   > Generate a Google App Password via: Google Account → Security → 2-Step Verification → App Passwords

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Open in your browser:**
   ```
   http://localhost:8080
   ```

---

## 💻 Usage

| Page | URL | Description |
|------|-----|-------------|
| Customer Booking | `http://localhost:8080/` | Book a car wash service |
| Admin Dashboard | `http://localhost:8080/admin/dashboard` | Manage bookings, inventory & notifications |
| Export Report | `http://localhost:8080/admin/export` | View printable summary report |
| H2 Database Console | `http://localhost:8080/h2-console` | Direct database access (dev use) |

---

## 👥 Team

| Name | Student Number |
|------|---------------|
| C.W MYA | 223842763 |
| M.M MATHEBULA | 221705815 |
| N.D HLUNGWANI | 222863201 |
| N.E RIKHOTSO | 221398637 |
| S.R MLWANE | 224567642 |

---

## 📄 License

This project was developed as an academic project at **Witbank**. All rights reserved by the project team.
