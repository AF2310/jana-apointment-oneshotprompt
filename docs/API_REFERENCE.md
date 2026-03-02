# Appointment Booking System - Complete API Reference

**API Version**: 1.0  
**Base URL**: http://localhost:8080/api  
**Response Format**: JSON  
**Authentication**: Session-based (admin endpoints)

---

## Table of Contents
1. [Appointment Endpoints](#appointment-endpoints)
2. [Admin Endpoints](#admin-endpoints)
3. [Error Handling](#error-handling)
4. [Data Models](#data-models)
5. [Example Workflows](#example-workflows)
6. [Rate Limiting](#rate-limiting)
7. [HTTP Status Codes](#http-status-codes)

---

## Appointment Endpoints

### 1. Get Available Appointment Dates

**Endpoint**: `GET /appointments/available-dates`

**Description**: Retrieve list of available dates for appointments within the configured range (default: next 40 days).

**Request Parameters**:
- None

**Response**:
```json
{
  "success": true,
  "message": "Available dates retrieved successfully",
  "data": [
    "2026-03-05",
    "2026-03-06",
    "2026-03-07",
    "2026-03-09",
    "2026-03-10"
  ],
  "statusCode": 200
}
```

**Excludes**: Weekends, holidays, dates with full capacity

**Example cURL**:
```bash
curl -X GET http://localhost:8080/api/appointments/available-dates
```

**JavaScript Fetch**:
```javascript
fetch('/api/appointments/available-dates')
  .then(response => response.json())
  .then(data => console.log(data.data));
```

---

### 2. Get Available Time Slots

**Endpoint**: `GET /appointments/available-slots`

**Description**: Retrieve available time slots for a specific date.

**Request Parameters**:
- `date` (required, format: YYYY-MM-DD): Appointment date

**Response**:
```json
{
  "success": true,
  "message": "Time slots retrieved successfully",
  "data": [
    {
      "slotId": 1,
      "startTime": "09:00",
      "endTime": "09:15",
      "displayText": "09:00 - 09:15"
    },
    {
      "slotId": 2,
      "startTime": "09:15",
      "endTime": "09:30",
      "displayText": "09:15 - 09:30"
    }
  ],
  "statusCode": 200
}
```

**Parameters Detail**:
- Slots are 15-minute intervals (default, configurable)
- Working hours: 9:00 AM - 6:00 PM
- Excludes break times (lunch 12:00-13:00, maintenance 15:00-15:30)
- Only shows slots without bookings

**Example cURL**:
```bash
curl -X GET "http://localhost:8080/api/appointments/available-slots?date=2026-03-10"
```

**Error Response** (invalid date):
```json
{
  "success": false,
  "message": "Invalid date format. Use YYYY-MM-DD",
  "statusCode": 400
}
```

---

### 3. Book Appointment

**Endpoint**: `POST /appointments/book`

**Description**: Create new appointment booking for user.

**Request Headers**:
- `Content-Type: application/json`

**Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "5551234567",
  "make": "Toyota",
  "model": "Camry",
  "year": 2022,
  "color": "Silver",
  "licensePlate": "ABC123",
  "appointmentDate": "2026-03-10",
  "appointmentTime": "10:00",
  "notes": "Oil change and tire rotation"
}
```

**Response** (Success - 200):
```json
{
  "success": true,
  "message": "Appointment booked successfully",
  "data": {
    "appointmentId": 1,
    "userId": 1,
    "carId": 1,
    "appointmentDate": "2026-03-10",
    "appointmentTime": "10:00",
    "appointmentEndTime": "10:15",
    "statusId": 1,
    "statusName": "BOOKED",
    "notes": "Oil change and tire rotation",
    "createdAt": "2026-03-02T14:30:45",
    "isFuture": true,
    "isEditable": true
  },
  "statusCode": 200
}
```

**Validation Rules**:
- firstName: 2-50 chars, letters/spaces/hyphens only
- email: Valid email format
- phone: 10-15 digits
- licensePlate: 3-20 chars, alphanumeric/hyphens
- appointmentDate: Must be >= today, <= 40 days from now
- appointmentTime: Must be during working hours, not break time

**Error Responses**:

Invalid email format (400):
```json
{
  "success": false,
  "message": "Invalid email format",
  "statusCode": 400
}
```

Date in past (400):
```json
{
  "success": false,
  "message": "Cannot book appointments in the past",
  "statusCode": 400
}
```

Time slot already booked (409):
```json
{
  "success": false,
  "message": "Selected time slot is no longer available",
  "statusCode": 409
}
```

**Example cURL**:
```bash
curl -X POST http://localhost:8080/api/appointments/book \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "5551234567",
    "make": "Toyota",
    "model": "Camry",
    "year": 2022,
    "color": "Silver",
    "licensePlate": "ABC123",
    "appointmentDate": "2026-03-10",
    "appointmentTime": "10:00",
    "notes": "Oil change"
  }'
```

---

### 4. Get Upcoming Appointments

**Endpoint**: `GET /appointments/upcoming`

**Description**: Retrieve upcoming appointments for a user.

**Request Parameters**:
- `userId` (required): User ID
- `limit` (optional, default: 10): Max number of appointments

**Response**:
```json
{
  "success": true,
  "message": "Upcoming appointments retrieved",
  "data": [
    {
      "appointmentId": 1,
      "userId": 1,
      "carId": 1,
      "appointmentDate": "2026-03-10",
      "appointmentTime": "10:00",
      "appointmentEndTime": "10:15",
      "statusId": 1,
      "statusName": "BOOKED",
      "notes": "Oil change",
      "createdAt": "2026-03-02T14:30:45",
      "isFuture": true,
      "isEditable": true
    },
    {
      "appointmentId": 2,
      "userId": 1,
      "carId": 1,
      "appointmentDate": "2026-03-15",
      "appointmentTime": "14:00",
      "appointmentEndTime": "14:15",
      "statusId": 1,
      "statusName": "BOOKED",
      "notes": "Tire rotation",
      "createdAt": "2026-03-02T15:00:00",
      "isFuture": true,
      "isEditable": true
    }
  ],
  "statusCode": 200
}
```

**Example cURL**:
```bash
curl -X GET "http://localhost:8080/api/appointments/upcoming?userId=1&limit=5"
```

---

### 5. Get Appointment History

**Endpoint**: `GET /appointments/history`

**Description**: Retrieve past/completed appointments for a user.

**Request Parameters**:
- `userId` (required): User ID
- `limit` (optional, default: 20): Max number of appointments

**Response**:
```json
{
  "success": true,
  "message": "Appointment history retrieved",
  "data": [
    {
      "appointmentId": 10,
      "userId": 1,
      "carId": 1,
      "appointmentDate": "2026-02-15",
      "appointmentTime": "10:00",
      "appointmentEndTime": "10:15",
      "statusId": 2,
      "statusName": "COMPLETED",
      "notes": "Annual maintenance",
      "createdAt": "2026-02-10T10:00:00",
      "isFuture": false,
      "isEditable": false
    }
  ],
  "statusCode": 200
}
```

**Example cURL**:
```bash
curl -X GET "http://localhost:8080/api/appointments/history?userId=1"
```

---

### 6. Update Appointment

**Endpoint**: `PUT /appointments/{appointmentId}`

**Description**: Modify existing appointment (reschedule/edit notes).

**Path Parameters**:
- `appointmentId` (required): Appointment ID

**Request Body**:
```json
{
  "appointmentDate": "2026-03-12",
  "appointmentTime": "14:00",
  "notes": "Oil change and tire rotation"
}
```

**Response**:
```json
{
  "success": true,
  "message": "Appointment updated successfully",
  "data": {
    "appointmentId": 1,
    "userId": 1,
    "carId": 1,
    "appointmentDate": "2026-03-12",
    "appointmentTime": "14:00",
    "appointmentEndTime": "14:15",
    "statusId": 1,
    "statusName": "BOOKED",
    "notes": "Oil change and tire rotation",
    "updatedAt": "2026-03-02T14:45:00",
    "isEditable": true
  },
  "statusCode": 200
}
```

**Validation Rules**:
- Can only update future appointments
- Can only update if status is BOOKED or PENDING
- New date/time must be available

**Errors**:

Cannot reschedule (400):
```json
{
  "success": false,
  "message": "Cannot reschedule appointments in the past or with status COMPLETED",
  "statusCode": 400
}
```

**Example cURL**:
```bash
curl -X PUT http://localhost:8080/api/appointments/1 \
  -H "Content-Type: application/json" \
  -d '{
    "appointmentDate": "2026-03-12",
    "appointmentTime": "14:00",
    "notes": "Oil change and rotation"
  }'
```

---

### 7. Cancel Appointment

**Endpoint**: `DELETE /appointments/{appointmentId}`

**Description**: Cancel existing appointment.

**Path Parameters**:
- `appointmentId` (required): Appointment ID

**Response**:
```json
{
  "success": true,
  "message": "Appointment cancelled successfully",
  "data": null,
  "statusCode": 200
}
```

**Rules**:
- Can only cancel future appointments
- Status changed to CANCELED
- Cancellation email sent to user

**Error** (Cannot cancel):
```json
{
  "success": false,
  "message": "Cannot cancel past or completed appointments",
  "statusCode": 400
}
```

**Example cURL**:
```bash
curl -X DELETE http://localhost:8080/api/appointments/1
```

---

## Admin Endpoints

### 1. Admin Login

**Endpoint**: `POST /admin/login`

**Description**: Authenticate admin user and create session.

**Request Headers**:
- `Content-Type: application/json`

**Request Body**:
```json
{
  "username": "admin",
  "password": "<admin-password-from-env>"
}
```

**Response** (Success):
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "adminId": 1,
    "username": "admin",
    "fullName": "Administrator",
    "email": "admin@example.com",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "lastLogin": "2026-03-02T10:00:00"
  },
  "statusCode": 200
}
```

**Error** (Invalid credentials):
```json
{
  "success": false,
  "message": "Invalid username or password",
  "statusCode": 401
}
```

**Example cURL**:
```bash
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "<admin-password-from-env>"
  }'
```

---

### 2. Get Appointments for Calendar

**Endpoint**: `GET /admin/appointments/calendar`

**Description**: Retrieve appointments for calendar date range view.

**Request Parameters**:
- `startDate` (required, format: YYYY-MM-DD): Start date
- `endDate` (required, format: YYYY-MM-DD): End date

**Response**:
```json
{
  "success": true,
  "message": "Calendar appointments retrieved",
  "data": {
    "2026-03-10": [
      {
        "appointmentId": 1,
        "userId": 1,
        "userName": "John Doe",
        "appointmentTime": "10:00",
        "appointmentEndTime": "10:15",
        "statusId": 1,
        "statusName": "BOOKED",
        "vehicleDisplay": "2022 Toyota Camry"
      },
      {
        "appointmentId": 2,
        "userId": 2,
        "userName": "Jane Smith",
        "appointmentTime": "10:15",
        "appointmentEndTime": "10:30",
        "statusId": 1,
        "statusName": "BOOKED",
        "vehicleDisplay": "2023 Honda Civic"
      }
    ],
    "2026-03-11": [
      {
        "appointmentId": 3,
        "userId": 3,
        "userName": "Bob Johnson",
        "appointmentTime": "14:00",
        "appointmentEndTime": "14:15",
        "statusId": 2,
        "statusName": "COMPLETED",
        "vehicleDisplay": "2021 Ford F-150"
      }
    ]
  },
  "statusCode": 200
}
```

**Requires**: Admin authentication

**Example cURL**:
```bash
curl -X GET "http://localhost:8080/api/admin/appointments/calendar?startDate=2026-03-01&endDate=2026-03-31" \
  -H "Authorization: Bearer admin-token"
```

---

### 3. Get All Appointments (List View)

**Endpoint**: `GET /admin/appointments`

**Description**: Retrieve all appointments with filtering and pagination.

**Request Parameters**:
- `page` (optional, default: 0): Page number
- `size` (optional, default: 20): Page size
- `status` (optional): Filter by status (BOOKED, COMPLETED, CANCELED)
- `date` (optional, format: YYYY-MM-DD): Filter by date
- `userId` (optional): Filter by user ID

**Response**:
```json
{
  "success": true,
  "message": "Appointments retrieved",
  "data": {
    "content": [
      {
        "appointmentId": 1,
        "userId": 1,
        "userName": "John Doe",
        "appointmentDate": "2026-03-10",
        "appointmentTime": "10:00",
        "statusId": 1,
        "statusName": "BOOKED",
        "vehicleDisplay": "2022 Toyota Camry",
        "notes": "Oil change",
        "createdAt": "2026-03-02T14:30:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "totalElements": 150,
      "totalPages": 8
    }
  },
  "statusCode": 200
}
```

**Requires**: Admin authentication

**Example cURL**:
```bash
curl -X GET "http://localhost:8080/api/admin/appointments?page=0&size=20&status=BOOKED" \
  -H "Authorization: Bearer admin-token"
```

---

### 4. Update Appointment Status

**Endpoint**: `PUT /admin/appointments/{appointmentId}/status`

**Description**: Change appointment status (BOOKED → COMPLETED, CANCELED, etc.).

**Path Parameters**:
- `appointmentId` (required): Appointment ID

**Request Body**:
```json
{
  "statusName": "COMPLETED"
}
```

**Response**:
```json
{
  "success": true,
  "message": "Appointment status updated",
  "data": {
    "appointmentId": 1,
    "statusId": 2,
    "statusName": "COMPLETED",
    "previousStatus": "BOOKED",
    "updatedAt": "2026-03-02T15:00:00",
    "updatedByAdminId": 1
  },
  "statusCode": 200
}
```

**Valid Status Transitions**:
- BOOKED → COMPLETED, CANCELED, RESCHEDULED, NO_SHOW
- PENDING → BOOKED, CANCELED
- COMPLETED → (read-only)
- CANCELED → (read-only)

**Requires**: Admin authentication

**Example cURL**:
```bash
curl -X PUT http://localhost:8080/api/admin/appointments/1/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer admin-token" \
  -d '{"statusName": "COMPLETED"}'
```

---

### 5. Delete Appointment

**Endpoint**: `DELETE /admin/appointments/{appointmentId}`

**Description**: Delete appointment record.

**Path Parameters**:
- `appointmentId` (required): Appointment ID

**Response**:
```json
{
  "success": true,
  "message": "Appointment deleted successfully",
  "statusCode": 200
}
```

**Rules**:
- Only admins can delete
- Soft-delete or hard-delete (configurable)
- Audit logged

**Requires**: Admin authentication

**Example cURL**:
```bash
curl -X DELETE http://localhost:8080/api/admin/appointments/1 \
  -H "Authorization: Bearer admin-token"
```

---

### 6. Update Application Setting

**Endpoint**: `PUT /admin/settings/{settingKey}`

**Description**: Update system configuration setting.

**Path Parameters**:
- `settingKey` (required): Setting key (e.g., "default-range-days")

**Request Body**:
```json
{
  "settingValue": "50"
}
```

**Response**:
```json
{
  "success": true,
  "message": "Setting updated successfully",
  "data": {
    "settingKey": "default-range-days",
    "settingValue": "50",
    "settingType": "INT",
    "previousValue": "40",
    "updatedAt": "2026-03-02T15:05:00"
  },
  "statusCode": 200
}
```

**Available Settings**:
- `default-range-days`: Days ahead to allow bookings (INT)
- `default-slot-duration-minutes`: Appointment duration (INT)
- `max-appointments-per-day`: Daily capacity (INT)
- `allow-same-day-booking`: Enable same-day booking (BOOLEAN)
- `email-reminders-enabled`: Send reminder emails (BOOLEAN)
- `working-hours-start`: Daily work start (STRING)
- `working-hours-end`: Daily work end (STRING)

**Requires**: Admin authentication

**Example cURL**:
```bash
curl -X PUT http://localhost:8080/api/admin/settings/default-range-days \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer admin-token" \
  -d '{"settingValue": "50"}'
```

---

### 7. Get Configurable Settings

**Endpoint**: `GET /admin/settings?configurable=true`

**Description**: Retrieve all settings editable via admin panel.

**Request Parameters**:
- `configurable` (optional): Filter only user-configurable settings

**Response**:
```json
{
  "success": true,
  "message": "Settings retrieved",
  "data": [
    {
      "settingKey": "default-range-days",
      "settingValue": "40",
      "settingType": "INT",
      "isConfigurable": true,
      "description": "Number of days ahead to allow appointments"
    },
    {
      "settingKey": "default-slot-duration-minutes",
      "settingValue": "15",
      "settingType": "INT",
      "isConfigurable": true,
      "description": "Duration of each appointment slot in minutes"
    }
  ],
  "statusCode": 200
}
```

**Requires**: Admin authentication

**Example cURL**:
```bash
curl -X GET "http://localhost:8080/api/admin/settings?configurable=true" \
  -H "Authorization: Bearer admin-token"
```

---

### 8. Get Dashboard Statistics

**Endpoint**: `GET /admin/dashboard/stats`

**Description**: Retrieve summary statistics for admin dashboard.

**Response**:
```json
{
  "success": true,
  "message": "Dashboard statistics retrieved",
  "data": {
    "totalAppointments": 342,
    "todayAppointments": 12,
    "weekAppointments": 78,
    "monthAppointments": 245,
    "pendingAppointments": 5,
    "completedAppointments": 320,
    "canceledAppointments": 17,
    "totalUsers": 156,
    "activeUsers": 138,
    "averageRating": 4.7,
    "systemHealth": "Operational",
    "lastBackup": "2026-03-02T02:00:00",
    "databaseSize": "245 MB",
    "uptime": "99.95%"
  },
  "statusCode": 200
}
```

**Requires**: Admin authentication

**Example cURL**:
```bash
curl -X GET http://localhost:8080/api/admin/dashboard/stats \
  -H "Authorization: Bearer admin-token"
```

---

## Error Handling

### Error Response Format

All error responses follow this standard format:

```json
{
  "success": false,
  "message": "Human-readable error message",
  "statusCode": 400,
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format"
    }
  ]
}
```

### Common Error Messages

| Status Code | Message | Cause |
|-------------|---------|-------|
| 400 | Invalid request data | Malformed JSON or missing required fields |
| 400 | Email already exists | User with email already registered |
| 400 | Invalid email format | Email doesn't match pattern |
| 400 | Invalid phone number | Phone doesn't meet length requirements |
| 400 | Date in past | Selected date is before today |
| 400 | Time slot unavailable | Slot already booked or in break time |
| 401 | Unauthorized | Admin not authenticated |
| 401 | Invalid credentials | Wrong username or password |
| 403 | Forbidden | User doesn't have permission |
| 404 | Resource not found | Appointment/user doesn't exist |
| 409 | Conflict | Data conflict (e.g., duplicate license plate) |
| 429 | Rate limit exceeded | Too many requests |
| 500 | Internal server error | Unexpected server error |

---

## Data Models

### Appointment Model
```json
{
  "appointmentId": 1,
  "userId": 1,
  "carId": 1,
  "appointmentDate": "2026-03-10",
  "appointmentTime": "10:00",
  "appointmentEndTime": "10:15",
  "statusId": 1,
  "statusName": "BOOKED",
  "notes": "Oil change",
  "createdAt": "2026-03-02T14:30:00",
  "updatedAt": "2026-03-02T14:30:00",
  "isFuture": true,
  "isEditable": true
}
```

### User Model
```json
{
  "userId": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "5551234567",
  "createdAt": "2026-03-02T10:00:00",
  "updatedAt": "2026-03-02T10:00:00"
}
```

### TimeSlot Model
```json
{
  "slotId": 1,
  "startTime": "09:00",
  "endTime": "09:15",
  "displayText": "09:00 - 09:15",
  "isActive": true
}
```

### AppointmentStatus Model
```json
{
  "statusId": 1,
  "statusName": "BOOKED",
  "description": "Appointment confirmed by user",
  "displayOrder": 1,
  "isActive": true
}
```

---

## Example Workflows

### Workflow 1: Complete User Booking

```javascript
// Step 1: Get available dates
const datesResponse = await fetch('/api/appointments/available-dates');
const availableDates = await datesResponse.json();

// Step 2: Select a date and get time slots
const selectedDate = '2026-03-10';
const slotsResponse = await fetch(`/api/appointments/available-slots?date=${selectedDate}`);
const availableSlots = await slotsResponse.json();

// Step 3: Book appointment
const bookingResponse = await fetch('/api/appointments/book', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    firstName: 'John',
    lastName: 'Doe',
    email: 'john@example.com',
    phone: '5551234567',
    make: 'Toyota',
    model: 'Camry',
    year: 2022,
    color: 'Silver',
    licensePlate: 'ABC123',
    appointmentDate: selectedDate,
    appointmentTime: '10:00',
    notes: 'Oil change'
  })
});

const booking = await bookingResponse.json();
if (booking.success) {
  console.log('Appointment confirmed:', booking.data.appointmentId);
}
```

### Workflow 2: Admin Updates Status

```javascript
// Admin logs in
const loginResponse = await fetch('/api/admin/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    username: 'admin',
    password: '<admin-password-from-env>'
  })
});

const admin = await loginResponse.json();
const token = admin.data.token;

// Admin updates appointment status
const updateResponse = await fetch('/api/admin/appointments/1/status', {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify({
    statusName: 'COMPLETED'
  })
});

const result = await updateResponse.json();
console.log('Status updated:', result.data.statusName);
```

---

## Rate Limiting

**Default Limits**:
- Public endpoints: 100 requests per minute per IP
- Admin endpoints: 1000 requests per minute per admin
- Booking endpoint: 10 requests per minute per email

**Response Headers** (rate limit info):
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 87
X-RateLimit-Reset: 1646232847
```

**Rate Limit Exceeded** (429):
```json
{
  "success": false,
  "message": "Rate limit exceeded. Try again in 42 seconds.",
  "statusCode": 429
}
```

---

## HTTP Status Codes

| Code | Meaning | Use Case |
|------|---------|----------|
| 200 | OK | Successful request |
| 201 | Created | Resource created (used in some POST) |
| 204 | No Content | Success with no response body |
| 400 | Bad Request | Invalid input data |
| 401 | Unauthorized | Authentication required/failed |
| 403 | Forbidden | Authenticated but not authorized |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Data conflict |
| 429 | Too Many Requests | Rate limit exceeded |
| 500 | Internal Server Error | Server error |
| 503 | Service Unavailable | Database or service down |

---

## CORS Configuration

**Allowed Origins** (Production):
- https://booking.example.com
- https://app.example.com

**Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS

**Allowed Headers**: Content-Type, Authorization

**Preflight Cache**: 3600 seconds

---

## Changelog

### Version 1.0 (March 2, 2026)
- Initial API release
- 15 total endpoints
- Full appointment booking workflow
- Admin management features
- Session-based authentication

---

**API Documentation Version**: 1.0  
**Last Updated**: March 2, 2026  
**Next Review**: After v1.1 release
