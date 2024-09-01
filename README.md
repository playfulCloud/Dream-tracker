# 🌟 Dream Tracker

Welcome to the **Dream Tracker** project! This README provides all the crucial information needed to interact with the project.

## 🚀 Getting Started

### Prerequisites
- **Java** (version 11 or higher)
- **Maven**
- **Node.js** and **npm**
- **IntelliJ IDEA 2024.1**
- **Docker**
- **PostgreSQL** 

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/dream-tracker.git
   cd dream-tracker
    ```
## 📚 Project Structure

### Backend
- **Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Maven

### Frontend
- **Language:** TypeScript, JavaScript
- **Framework:** React - Next.js
- **Package Manager:** npm

### 🔐 Security Features

#### User Registration:

- **Email with verification**
- **Google OAuth (planned)**
- **Facebook OAuth (planned)**
- **GitHub OAuth (planned)**
  
#### User Authentication:


**JWT (JSON Web Token) as the primary authentication mechanism: Upon successful login, a JWT is issued to the user, which is then used for securing subsequent requests**.
- **Email and password**
- **Google OAuth (planned)**
- **GitHub OAuth (planned)**

  
#### Password Management:

- **Change credentials**
- **Password reset via email**

JWT plays a crucial role in securing user sessions by embedding user details within the token, which is validated on every request.

## 📊 Habit and Goal Tracking

### Habit Features
- Define action, frequency, duration,  goal, difficulty, and status
- Mark habits as done or undone
- Browse active and inactive habits

### Goal Features
- Define habits for goals
- Set completion criteria
- Track progress with charts

## 🖥️ Interface Features

- Create custom views for habit tracking
- Pick tracking componnets up to your liking to create your own expierience
  

## 📄 API Documentation

API documentation is available in the `docs/openapi.yaml` file. Use tools like Swagger to visualize and interact with the API.

## 🛠️ Development

### Running Tests
```bash
mvn test
```
### Building the Project
```bash
mvn clean install
```
### 🤝 Contributing
Check the LICENSE.

### 📧 Contact
For any inquiries, please contact playfulCloud@proton.me   <hr></hr> Happy Tracking! 🎉
