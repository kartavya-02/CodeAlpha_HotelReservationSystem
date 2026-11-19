# 🏨 Hotel Reservation System

A Java-based Hotel Reservation System developed using Object-Oriented Programming (OOP) and Java Swing. This application allows users to search available hotel rooms, make reservations, simulate payments, view booking details, and cancel reservations through a user-friendly graphical interface.

## ✨ Features

- Search available hotel rooms.
- Three room categories: Standard, Deluxe, and Suite.
- Book rooms with customer information.
- Automatic booking ID generation.
- Automatic calculation of total room charges.
- Simulated payment gateway.
- View complete reservation details.
- Cancel existing reservations.
- Check room availability based on booking dates.
- Store booking records using Java File Handling.
- Retain booking information after restarting the application.

## 🛠️ Technologies Used

- Java
- Java Swing
- Object-Oriented Programming (OOP)
- Java File Handling
- VS Code

## 📂 Project Structure

```text
CodeAlpha_HotelReservationSystem/
│
├── src/
│   ├── Main.java
│   ├── Hotel.java
│   ├── Room.java
│   ├── Customer.java
│   ├── Reservation.java
│   ├── Payment.java
│   └── FileManager.java
│
├── data/
│   └── bookings.txt
│
├── README.md
└── .gitignore
```

## 🚀 How to Run

**Prerequisites:** Java JDK 17 or later.

1. Clone or download this repository.
2. Open the project folder in VS Code.
3. Open the terminal in the project directory.
4. Compile the Java source files:

```bash
mkdir out
javac -d out src/*.java
```

5. Run the application:

```bash
java -cp out Main
```

## 🏨 Room Categories

| Category | Price per Night |
|---|---:|
| Standard | ₹1,500 |
| Deluxe | ₹2,500 |
| Suite | ₹4,000 |

## 💳 Payment System

The application includes a simulated payment process for demonstrating hotel booking functionality. No real financial transactions are performed.

## 💾 Data Storage

Booking information is stored locally using Java File Handling in the `data/bookings.txt` file.

## 🎯 Project Objective

The objective of this project is to demonstrate Java programming skills, Object-Oriented Programming concepts, GUI development, reservation management, and file handling through a practical hotel reservation application.

## 👨‍💻 Project Information

**Project:** CodeAlpha Hotel Reservation System  
**Language:** Java  
**Interface:** Java Swing  
**Storage:** File Handling  
**Project Type:** Internship Project – CodeAlpha
