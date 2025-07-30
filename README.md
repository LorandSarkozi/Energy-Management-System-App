# ⚡ Energy Management System (EMS)

The **Energy Management System (EMS)** is a full-stack, microservices-based application designed to monitor, simulate, and interact with energy consumption data in real-time. Built with Java Spring Boot and deployed using Docker, the system integrates user and device management, real-time WebSocket chat, energy measurement ingestion via RabbitMQ, and a responsive frontend UI. A custom Java simulator reads energy data from CSV files and pushes it to the system for live visualization.

---

## 🎥 Demo Videos

- **🧭 Application Tutorial**

  https://github.com/user-attachments/assets/9e92d0f9-9aaf-42ff-ba0d-7e778c705bdf
  
  _This demo walks through launching the app via Docker, navigating the UI, managing users and devices, and using the real-time chat functionality._

- **🔌 Device Simulation**  

  https://github.com/user-attachments/assets/6f760e99-c280-4e52-8231-8a7f9bdae21e

  _This demo illustrates how the device simulator reads CSV energy data and sends hourly consumption to the backend via RabbitMQ._

### Microservices:

1. **User Management (spring-demo)**  
   CRUD operations for user accounts and authentication.

2. **Device Management (spring-demo-devices)**  
   CRUD operations for energy-consuming devices linked to users.

3. **Measurement Service (measurements-demo)**  
   Receives hourly consumption data from the simulator via RabbitMQ.

4. **Chat Service (chat-demo)**  
   Enables real-time user messaging through WebSockets.

5. **Simulator App (device-simulator)**  
   Reads energy usage from CSV and simulates device output.

6. **Frontend (EnergyManagementSystem)**  
   React-based user interface for dashboard interaction, chart visualization, and chat.

---

## 🛠️ Technologies Used

- **Java Spring Boot** – Core framework for building the backend microservices:
  - `spring-demo` (User CRUD)
  - `spring-demo-devices` (Device CRUD)
  - `measurements-demo` (Measurement service)
  - `chat-demo` (WebSocket chat service)

- **RabbitMQ** – Message broker used in:
  - `measurements-demo` to receive energy consumption data
  - `device-simulator` to send hourly consumption data

- **WebSocket (Spring Messaging)** – Enables real-time communication in:
  - `chat-demo` service for instant user-to-user messaging

- **Java (Plain)** – Used for the simulator:
  - `device-simulator` application that reads from CSV and sends data via RabbitMQ

- **CSV File Handling** – Input format for:
  - `device-simulator` to simulate hourly device energy consumption

- **Angular** – Frontend framework powering:
  - `EnergyManagementSystem` for interactive dashboard, device/user control, and chart visualization

- **Docker & Docker Compose** – Containerization and orchestration for all services, used to:
  - Run the full system seamlessly via `docker-compose.yml`

- **Maven** – Build and dependency management for:
  - All Java-based microservices and the simulator app
 
---

## ⚙️ How to Use

### Requirements

- **Docker & Docker Compose**  
  To run all services easily across environments.

- **Java 17+ & Maven (for development only)**  
  If you wish to build or extend the microservices or simulator manually.

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/your-user/energy-management-system.git
   cd energy-management-system
   ```

2. Start the application using Docker Compose:
   ```bash
   docker-compose up --build
   ```

3. Navigate to `http://localhost:3000` to access the frontend UI.

4. To simulate devices:
   - Navigate to `simulator-app/device-simulator`
   - Provide a valid `energy_data.csv` file
   - Run:
     ```bash
     mvn clean install
     java -jar target/device-simulator.jar
     ```

---

## 📂 Project Structure

```
├── main/
│   ├── README.md
│   └── docker-compose.yml
│
├── simulator-app/
│   └── device-simulator/            # Simulator reads CSV and sends data via RabbitMQ
│
├── microservices/
│   ├── spring-demo/                 # User CRUD
│   ├── spring-demo-devices/        # Device CRUD
│   ├── measurements-demo/          # Receives data from simulator
│   └── chat-demo/                  # WebSocket chat service
│
├── frontend/
│   └── EnergyManagementSystem/     # React frontend application
│
├── documents/
│   ├── requirements/               # Functional and technical project requirements
│   └── documentation/             # Design and architecture documentation
```

---

## 📊 Features

- 🔐 **User Management** – Register, authenticate, and manage roles.
- 🔧 **Device Configuration** – Add and assign devices to users.
- 📡 **Energy Measurement** – Receive hourly data from simulated sources.
- 📈 **Live Dashboard** – View energy charts and device-specific metrics.
- 💬 **Real-Time Chat** – Communicate instantly between users via WebSockets.
- 🐳 **Dockerized Deployment** – One command to launch the full system.

---

## 🚀 Performance

- Handles multiple simultaneous users and devices.
- Fully asynchronous message flow via RabbitMQ ensures decoupling and scalability.
- Real-time communication with WebSockets for instant messaging.
- Designed for extensibility: more sensors or device types can be integrated with ease.

---

## 📜 License

This project is provided for **academic and educational use only**.  
For research collaborations or production deployment, please contact:

📩 **lorand.sarkozi25@yahoo.com**
