# Hospital Management System

A comprehensive microservices-based hospital management system built with Spring Boot, React, and modern cloud technologies.

## 🏗️ Architecture Overview

This is a monorepo containing all microservices and frontend application for a hospital management system.

### Tech Stack

- **Backend**: Spring Boot (Java 17), Maven
- **Frontend**: React, Tailwind CSS, Ant Design, Apollo Client
- **API**: GraphQL (main) + REST (external APIs)
- **Inter-Service Communication**:
  - **gRPC** (synchronous service-to-service)
  - **Kafka** (asynchronous event-driven)
  - **REST** (external/client-facing)
- **Authentication**: JWT + Basic Auth + Session-based
- **Database**: PostgreSQL (Neon)
- **Service Discovery**: Eureka
- **API Gateway**: Spring Cloud Gateway
- **Email**: Mailgun
- **Storage**: Cloudinary
- **Containerization**: Docker & Docker Compose
- **Deployment**: Render (Backend), Vercel (Frontend)

## 📁 Project Structure

```
hospital-management-system/
├── backend/
│   ├── auth-service/              # Authentication & Authorization
│   ├── patient-service/            # Patient management
│   ├── doctor-service/             # Doctor management
│   ├── appointment-service/        # Appointment scheduling
│   ├── billing-service/            # Billing & invoicing
│   ├── notification-service/       # Email notifications
│   ├── discovery-service/          # Eureka service registry
│   ├── gateway-service/            # API Gateway
│   ├── graphql-api-gateway/        # GraphQL unified API
│   └── common-libs/                # Shared DTOs, Enums, Utilities
├── frontend/
│   └── react-graphql-app/          # React frontend application
├── docker-compose.yml              # Docker orchestration
└── README.md                       # This file
```

## 👥 User Roles

- **Admin**: Full system access, manages users, doctors, invoices
- **Doctor**: Manages appointments, patient notes, schedule
- **Patient**: Books appointments, views history, invoices
- **Receptionist**: Handles registrations, appointment scheduling

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Node.js 18+
- Docker & Docker Compose
- Maven 3.8+

### Local Development

1. **Start Infrastructure Services**:
   ```bash
   docker-compose up -d kafka zookeeper postgres
   ```

2. **Start Discovery Service**:
   ```bash
   cd backend/discovery-service
   mvn spring-boot:run
   ```

3. **Start Gateway Service**:
   ```bash
   cd backend/gateway-service
   mvn spring-boot:run
   ```

4. **Start Microservices** (in separate terminals):
   ```bash
   # Auth Service
   cd backend/auth-service && mvn spring-boot:run

   # Patient Service
   cd backend/patient-service && mvn spring-boot:run

   # Doctor Service
   cd backend/doctor-service && mvn spring-boot:run

   # Appointment Service
   cd backend/appointment-service && mvn spring-boot:run

   # Billing Service
   cd backend/billing-service && mvn spring-boot:run

   # Notification Service
   cd backend/notification-service && mvn spring-boot:run

   # GraphQL Gateway
   cd backend/graphql-api-gateway && mvn spring-boot:run
   ```

5. **Start Frontend**:
   ```bash
   cd frontend/react-graphql-app
   npm install
   npm start
   ```

## 📚 Documentation

- [2-Week Build Roadmap](./docs/2_WEEK_BUILD_ROADMAP.md) - **Complete step-by-step guide to build and deploy the project**
- [Free Tier Guide](./docs/FREE_TIER_GUIDE.md) - **🆓 Deploy using 100% free services (perfect for small-scale)**
- [Quick Deploy Free](./docs/QUICK_DEPLOY_FREE.md) - **⚡ 30-minute quick deployment guide**
- [Build Guide](./docs/BUILD_GUIDE.md) - **Detailed instructions for each phase**
- [Deployment Checklist](./docs/DEPLOYMENT_CHECKLIST.md) - **Pre and post-deployment checklist**
- [Learning Guide](./docs/LEARNING_GUIDE.md) - Comprehensive guide to understand all concepts
- [Communication Patterns](./docs/COMMUNICATION_PATTERNS.md) - When to use gRPC, Kafka, or REST
- [React Optimization](./docs/REACT_OPTIMIZATION.md) - **React performance optimization techniques**
- [Flow Documentation](./docs/FLOW_DOCUMENTATION.md) - Navigation flows for each functionality
- [API Documentation](./docs/API_DOCUMENTATION.md) - API endpoints and GraphQL schemas
- [Quick Start Guide](./docs/QUICK_START.md) - Setup and running instructions

## 🔐 Authentication Methods

The system supports three authentication methods:

1. **JWT (JSON Web Tokens)**: Stateless authentication for API access
2. **Basic Authentication**: HTTP Basic Auth for service-to-service communication
3. **Session-based**: Traditional session management for web applications

## 🧪 Testing

Run all tests:
```bash
# Backend tests
cd backend && mvn test

# Frontend tests
cd frontend/react-graphql-app && npm test
```

## 📦 Deployment

Comprehensive deployment guides available:

- [Deployment Guide](./docs/DEPLOYMENT.md) - **Complete deployment instructions for local, Docker, and cloud**
- [2-Week Build Roadmap](./docs/2_WEEK_BUILD_ROADMAP.md) - Step-by-step build guide
- [Build Guide](./docs/BUILD_GUIDE.md) - Detailed build instructions

### Quick Deploy

**Local:**
```bash
docker-compose up -d
```

**Production:**
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## 🤝 Contributing

This is a personal project for learning microservices architecture.