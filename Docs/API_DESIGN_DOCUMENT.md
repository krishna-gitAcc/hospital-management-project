# API Design Document - Hospital Management System

## Overview
This document outlines the API contracts for the Hospital Management System. The system provides both REST and GraphQL APIs, with REST primarily for external integrations and GraphQL for the frontend application.

**Base URLs**:
- API Gateway: `http://localhost:8080` (development)
- GraphQL Endpoint: `http://localhost:8080/graphql`

**Authentication**: All endpoints (except public auth endpoints) require authentication via:
- JWT Bearer Token (recommended)
- Basic Authentication
- Session-based Authentication

---

## Service Architecture

### Microservices and Their Endpoints

1. **auth-service**: Authentication and user management
2. **patient-service**: Patient profile management
3. **doctor-service**: Doctor profile and availability management
4. **appointment-service**: Appointment scheduling
5. **billing-service**: Invoice and billing management
6. **notification-service**: Email notifications (internal)
7. **gateway-service**: API Gateway routing
8. **graphql-api-gateway**: Unified GraphQL API

---

## REST API Endpoints

### 1. Authentication Service (`/api/auth`)

#### Register User
```http
POST /api/auth/register?role={USER_ROLE}
Content-Type: application/json

Request Body:
{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe"
}

Response 200 OK:
{
  "userId": "uuid",
  "email": "user@example.com",
  "role": "PATIENT",
  "token": "jwt-token",
  "refreshToken": "refresh-token"
}
```

**Query Parameters**:
- `role`: PATIENT | DOCTOR | ADMIN | RECEPTIONIST (default: PATIENT)

---

#### Login (JWT)
```http
POST /api/auth/login/jwt
Content-Type: application/json

Request Body:
{
  "email": "user@example.com",
  "password": "password123"
}

Response 200 OK:
{
  "userId": "uuid",
  "email": "user@example.com",
  "role": "PATIENT",
  "token": "jwt-token",
  "refreshToken": "refresh-token"
}
```

---

#### Login (Basic Auth)
```http
POST /api/auth/login/basic
Authorization: Basic {base64(email:password)}
Content-Type: application/json

Response 200 OK:
{
  "userId": "uuid",
  "email": "user@example.com",
  "role": "PATIENT",
  "token": "jwt-token",
  "refreshToken": "refresh-token"
}
```

---

#### Login (Session-based)
```http
POST /api/auth/login/session
Content-Type: application/json

Request Body:
{
  "email": "user@example.com",
  "password": "password123"
}

Response 200 OK:
{
  "userId": "uuid",
  "email": "user@example.com",
  "role": "PATIENT",
  "token": "session-token",
  "refreshToken": null
}
```

---

#### Refresh Token
```http
POST /api/auth/refresh
Content-Type: application/json

Request Body:
{
  "refreshToken": "refresh-token"
}

Response 200 OK:
{
  "token": "new-jwt-token",
  "refreshToken": "new-refresh-token"
}
```

---

#### Logout
```http
POST /api/auth/logout
Authorization: Bearer {token}

Response 200 OK
```

---

### 2. Patient Service (`/api/patients`)

#### Create Patient
```http
POST /api/patients
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "userId": "uuid",
  "name": "John Doe",
  "age": 30,
  "gender": "MALE",
  "contact": "1234567890",
  "address": "123 Main St",
  "dateOfBirth": "1993-01-01",
  "bloodGroup": "O+",
  "medicalHistory": "None"
}

Response 201 Created:
{
  "id": "uuid",
  "userId": "uuid",
  "name": "John Doe",
  ...
}
```

---

#### Get Patient by ID
```http
GET /api/patients/{id}
Authorization: Bearer {token}

Response 200 OK:
{
  "id": "uuid",
  "userId": "uuid",
  "name": "John Doe",
  ...
}
```

---

#### Get Patient by User ID
```http
GET /api/patients/user/{userId}
Authorization: Bearer {token}

Response 200 OK:
{
  "id": "uuid",
  "userId": "uuid",
  "name": "John Doe",
  ...
}
```

---

#### Get All Patients
```http
GET /api/patients
Authorization: Bearer {token}

Response 200 OK:
[
  {
    "id": "uuid",
    "userId": "uuid",
    "name": "John Doe",
    ...
  },
  ...
]
```

**Query Parameters** (optional):
- `page`: Page number (default: 0)
- `size`: Page size (default: 20)
- `sort`: Sort field and direction (e.g., `name,asc`)

---

#### Update Patient
```http
PUT /api/patients/{id}
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "name": "Jane Doe",
  "age": 31,
  ...
}

Response 200 OK:
{
  "id": "uuid",
  "name": "Jane Doe",
  ...
}
```

---

#### Delete Patient
```http
DELETE /api/patients/{id}
Authorization: Bearer {token}

Response 204 No Content
```

---

### 3. Doctor Service (`/api/doctors`)

#### Create Doctor
```http
POST /api/doctors
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "userId": "uuid",
  "name": "Dr. Smith",
  "specialization": "Cardiology",
  "contact": "1234567890",
  "qualification": "MD",
  "experience": 10,
  "availableFrom": "09:00",
  "availableTo": "17:00",
  "availableDays": ["MONDAY", "TUESDAY", "WEDNESDAY"],
  "consultationFee": 500.00
}

Response 201 Created:
{
  "id": "uuid",
  "userId": "uuid",
  "name": "Dr. Smith",
  ...
}
```

---

#### Get Doctor by ID
```http
GET /api/doctors/{id}
Authorization: Bearer {token}

Response 200 OK:
{
  "id": "uuid",
  "userId": "uuid",
  "name": "Dr. Smith",
  ...
}
```

---

#### Get All Doctors
```http
GET /api/doctors
Authorization: Bearer {token}

Response 200 OK:
[
  {
    "id": "uuid",
    "name": "Dr. Smith",
    ...
  },
  ...
]
```

---

#### Get Doctors by Specialization
```http
GET /api/doctors?specialization=Cardiology
Authorization: Bearer {token}

Response 200 OK:
[
  {
    "id": "uuid",
    "name": "Dr. Smith",
    "specialization": "Cardiology",
    ...
  },
  ...
]
```

**Query Parameters**:
- `specialization`: Filter by specialization
- `page`: Page number
- `size`: Page size
- `sort`: Sort field

---

#### Update Doctor
```http
PUT /api/doctors/{id}
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "specialization": "Neurology",
  "consultationFee": 600.00,
  ...
}

Response 200 OK:
{
  "id": "uuid",
  "specialization": "Neurology",
  ...
}
```

---

#### Update Doctor Availability
```http
PUT /api/doctors/{id}/availability
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "availableFrom": "10:00",
  "availableTo": "18:00",
  "availableDays": ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY"]
}

Response 200 OK
```

---

### 4. Appointment Service (`/api/appointments`)

#### Create Appointment
```http
POST /api/appointments
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
  "patientId": "uuid",
  "doctorId": "uuid",
  "appointmentDate": "2024-01-15T10:00:00",
  "reason": "Regular checkup",
  "notes": "First visit",
  "bookedBy": "uuid"
}

Response 201 Created:
{
  "id": "uuid",
  "patientId": "uuid",
  "doctorId": "uuid",
  "appointmentDate": "2024-01-15T10:00:00",
  "status": "BOOKED",
  ...
}
```

**Validation**:
- Patient must exist (gRPC call to patient-service)
- Doctor must exist and be available (gRPC call to doctor-service)
- Appointment date must be in future
- Doctor must be available at requested time

---

#### Get Appointment by ID
```http
GET /api/appointments/{id}
Authorization: Bearer {token}

Response 200 OK:
{
  "id": "uuid",
  "patientId": "uuid",
  "doctorId": "uuid",
  "appointmentDate": "2024-01-15T10:00:00",
  "status": "BOOKED",
  ...
}
```

---

#### Get Appointments by Patient
```http
GET /api/appointments/patient/{patientId}
Authorization: Bearer {token}

Response 200 OK:
[
  {
    "id": "uuid",
    "patientId": "uuid",
    "doctorId": "uuid",
    "appointmentDate": "2024-01-15T10:00:00",
    "status": "BOOKED",
    ...
  },
  ...
]
```

---

#### Get Appointments by Doctor
```http
GET /api/appointments/doctor/{doctorId}
Authorization: Bearer {token}

Response 200 OK:
[
  {
    "id": "uuid",
    "patientId": "uuid",
    "doctorId": "uuid",
    "appointmentDate": "2024-01-15T10:00:00",
    "status": "BOOKED",
    ...
  },
  ...
]
```

**Query Parameters**:
- `status`: Filter by status (BOOKED, CONFIRMED, etc.)
- `startDate`: Filter from date
- `endDate`: Filter to date

---

#### Update Appointment Status
```http
PUT /api/appointments/{id}/status?status=CONFIRMED
Authorization: Bearer {token}

Response 200 OK:
{
  "id": "uuid",
  "status": "CONFIRMED",
  ...
}
```

**Status Values**:
- `BOOKED` - Initial booking
- `CONFIRMED` - Confirmed by doctor
- `CANCELLED` - Cancelled
- `COMPLETED` - Completed
- `RESCHEDULED` - Rescheduled

---

### 5. Billing Service (`/api/billing`)

#### Get Invoice by ID
```http
GET /api/billing/invoices/{id}
Authorization: Bearer {token}

Response 200 OK:
{
  "id": "uuid",
  "appointmentId": "uuid",
  "amount": 500.00,
  "status": "PENDING",
  "issuedAt": "2024-01-15T10:00:00",
  ...
}
```

---

#### Get Invoices by Appointment
```http
GET /api/billing/invoices/appointment/{appointmentId}
Authorization: Bearer {token}

Response 200 OK:
[
  {
    "id": "uuid",
    "appointmentId": "uuid",
    "amount": 500.00,
    "status": "PENDING",
    ...
  },
  ...
]
```

---

#### Update Invoice Status
```http
PUT /api/billing/invoices/{id}/status?status=PAID
Authorization: Bearer {token}

Response 200 OK:
{
  "id": "uuid",
  "status": "PAID",
  "paidAt": "2024-01-15T11:00:00",
  ...
}
```

**Status Values**:
- `PENDING` - Pending payment
- `PAID` - Payment received
- `OVERDUE` - Payment overdue
- `CANCELLED` - Invoice cancelled

---

## GraphQL API Schema

### Schema Definition

```graphql
type Query {
  # Patient queries
  patient(id: ID!): Patient
  patients(page: Int, size: Int): [Patient]

  # Doctor queries
  doctor(id: ID!): Doctor
  doctors(specialization: String, page: Int, size: Int): [Doctor]

  # Appointment queries
  appointment(id: ID!): Appointment
  appointments(patientId: ID, doctorId: ID, status: AppointmentStatus): [Appointment]
}

type Mutation {
  # Patient mutations
  createPatient(input: PatientInput!): Patient
  updatePatient(id: ID!, input: PatientInput!): Patient

  # Doctor mutations
  createDoctor(input: DoctorInput!): Doctor
  updateDoctor(id: ID!, input: DoctorInput!): Doctor

  # Appointment mutations
  createAppointment(input: AppointmentInput!): Appointment
  updateAppointmentStatus(id: ID!, status: AppointmentStatus!): Appointment
}
```

### Types

#### Patient Type
```graphql
type Patient {
  id: ID!
  userId: ID!
  name: String!
  age: Int
  gender: Gender
  contact: String
  address: String
  dateOfBirth: String
  bloodGroup: String
  medicalHistory: String
  appointments: [Appointment]
}
```

#### Doctor Type
```graphql
type Doctor {
  id: ID!
  userId: ID!
  name: String!
  specialization: String!
  contact: String
  qualification: String
  experience: Int
  availableFrom: String
  availableTo: String
  availableDays: [String]
  consultationFee: Float
  appointments: [Appointment]
}
```

#### Appointment Type
```graphql
type Appointment {
  id: ID!
  patientId: ID!
  doctorId: ID!
  patient: Patient
  doctor: Doctor
  appointmentDate: String!
  status: AppointmentStatus!
  reason: String
  notes: String
  bookedBy: ID!
  invoice: Invoice
}
```

#### Invoice Type
```graphql
type Invoice {
  id: ID!
  appointmentId: ID!
  appointment: Appointment
  amount: Float!
  status: InvoiceStatus!
  issuedAt: String!
  paidAt: String
  issuedBy: ID!
}
```

### Input Types

#### PatientInput
```graphql
input PatientInput {
  userId: ID!
  name: String!
  age: Int
  gender: Gender
  contact: String
  address: String
  dateOfBirth: String
  bloodGroup: String
  medicalHistory: String
}
```

#### DoctorInput
```graphql
input DoctorInput {
  userId: ID!
  name: String!
  specialization: String!
  contact: String
  qualification: String
  experience: Int
  availableFrom: String
  availableTo: String
  availableDays: [String]
  consultationFee: Float
}
```

#### AppointmentInput
```graphql
input AppointmentInput {
  patientId: ID!
  doctorId: ID!
  appointmentDate: String!
  reason: String
  notes: String
  bookedBy: ID!
}
```

### Enums

```graphql
enum Gender {
  MALE
  FEMALE
  OTHER
}

enum AppointmentStatus {
  BOOKED
  CONFIRMED
  CANCELLED
  COMPLETED
  RESCHEDULED
}

enum InvoiceStatus {
  PENDING
  PAID
  OVERDUE
  CANCELLED
}
```

### GraphQL Query Examples

#### Get Patient with Appointments
```graphql
query GetPatient($id: ID!) {
  patient(id: $id) {
    id
    name
    age
    gender
    appointments {
      id
      appointmentDate
      status
      doctor {
        name
        specialization
      }
    }
  }
}
```

#### Get Doctors by Specialization
```graphql
query GetDoctors($specialization: String) {
  doctors(specialization: $specialization) {
    id
    name
    specialization
    availableFrom
    availableTo
    consultationFee
  }
}
```

#### Get Appointments for Patient
```graphql
query GetPatientAppointments($patientId: ID!) {
  appointments(patientId: $patientId) {
    id
    appointmentDate
    status
    doctor {
      name
      specialization
      contact
    }
  }
}
```

### GraphQL Mutation Examples

#### Create Patient
```graphql
mutation CreatePatient($input: PatientInput!) {
  createPatient(input: $input) {
    id
    name
    age
  }
}
```

#### Create Appointment
```graphql
mutation CreateAppointment($input: AppointmentInput!) {
  createAppointment(input: $input) {
    id
    appointmentDate
    status
    patient {
      name
    }
    doctor {
      name
      specialization
    }
  }
}
```

#### Update Appointment Status
```graphql
mutation UpdateAppointmentStatus($id: ID!, $status: AppointmentStatus!) {
  updateAppointmentStatus(id: $id, status: $status) {
    id
    status
  }
}
```

---

## Error Response Format

### Standard Error Response
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/patients",
  "details": [
    {
      "field": "name",
      "message": "Name is required"
    }
  ]
}
```

### HTTP Status Codes

- `200 OK` - Successful GET, PUT, PATCH requests
- `201 Created` - Successful POST request (resource created)
- `204 No Content` - Successful DELETE request
- `400 Bad Request` - Validation error, invalid request
- `401 Unauthorized` - Missing or invalid authentication
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource conflict (e.g., duplicate email)
- `500 Internal Server Error` - Server error

---

## Inter-Service Communication

### gRPC Services

#### Patient Service (gRPC)
- `GetPatient(id)` - Get patient by ID
- `CreatePatient(patient)` - Create patient
- `UpdatePatient(id, patient)` - Update patient

#### Doctor Service (gRPC)
- `GetDoctor(id)` - Get doctor by ID
- `CheckAvailability(doctorId, date, time)` - Check doctor availability
- `GetDoctorsBySpecialization(specialization)` - Get doctors by specialization

### Kafka Events

#### Topic: `appointment-events`
**Event Types**:
- `APPOINTMENT_BOOKED` - When appointment is created
- `APPOINTMENT_CONFIRMED` - When appointment is confirmed
- `APPOINTMENT_CANCELLED` - When appointment is cancelled
- `APPOINTMENT_COMPLETED` - When appointment is completed

**Event Payload**:
```json
{
  "appointmentId": "uuid",
  "patientId": "uuid",
  "doctorId": "uuid",
  "appointmentDate": "2024-01-15T10:00:00",
  "status": "BOOKED",
  "eventType": "APPOINTMENT_BOOKED",
  "bookedBy": "uuid",
  "timestamp": "2024-01-15T09:00:00"
}
```

#### Topic: `billing-events`
**Event Types**:
- `INVOICE_GENERATED` - When invoice is created
- `PAYMENT_RECEIVED` - When payment is received

**Event Payload**:
```json
{
  "invoiceId": "uuid",
  "appointmentId": "uuid",
  "patientId": "uuid",
  "amount": 500.00,
  "status": "PENDING",
  "eventType": "INVOICE_GENERATED",
  "timestamp": "2024-01-15T09:00:00"
}
```

---

## Rate Limiting

**Recommended Limits**:
- Public endpoints (auth): 100 requests/minute
- Authenticated endpoints: 1000 requests/minute
- GraphQL: 100 queries/minute per user

---

## Pagination

List endpoints support pagination via query parameters:
```
GET /api/patients?page=0&size=20&sort=name,asc
```

**Response Format**:
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5,
  "first": true,
  "last": false
}
```

---

## Versioning

Current API version: **v1**

Future versions will be specified in the URL:
- `/api/v1/patients`
- `/api/v2/patients`

---

## Notes

1. All timestamps are in ISO 8601 format (e.g., `2024-01-15T10:00:00`)
2. All UUIDs are in standard UUID v4 format
3. Date formats: `YYYY-MM-DD` for dates, `HH:mm:ss` for times
4. Authentication tokens expire after 24 hours (refresh token: 7 days)
5. CORS is enabled for frontend origin
6. API Gateway handles routing and authentication validation

