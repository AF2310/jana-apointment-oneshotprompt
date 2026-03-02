# Appointment Booking System - Complete Documentation

## Table of Contents
1. [System Overview](#system-overview)
2. [Architecture](#architecture)
3. [Features](#features)
4. [Installation & Setup](#installation--setup)
5. [Database Schema](#database-schema)
6. [API Documentation](#api-documentation)
7. [User Guide](#user-guide)
8. [Admin Guide](#admin-guide)
9. [Configuration](#configuration)
10. [Troubleshooting](#troubleshooting)

---

## System Overview

The Appointment Booking System is a comprehensive Java-based web application that enables users to book appointments and allows administrators to manage the booking system with full control over scheduling, settings, and analytics.

### Key Objectives
- **User-Friendly Booking**: Simple interface for customers to reserve appointments
- **Admin Control**: Complete administrative panel for scheduling management
- **Configurable Settings**: Flexible system that adapts to business needs
- **Data Security**: Secure authentication and input validation
- **Scalability**: Built with Spring Boot for production deployment

### Target Users
- **End Users**: Customers booking appointments for services
- **Administrators**: Staff managing appointments and system configuration

---

## Architecture

### System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        USER INTERFACE LAYER                      │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│  │  User Booking    │  │  Admin Login     │  │  Admin Dashboard │
│  │  Form (HTML/JS)  │  │  (HTML/JS)       │  │  (HTML/JS)       │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘
└─────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                    REST API CONTROLLER LAYER                     │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│  │ Appointment      │  │ Admin            │  │ User             │
│  │ Controller       │  │ Controller       │  │ Controller       │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘
└─────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                    BUSINESS LOGIC LAYER                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐  
│  │ Appointment  │  │ Admin        │  │ AppSetting / Holiday │
│  │ Service      │  │ Service      │  │ / Car Service        │
│  └──────────────┘  └──────────────┘  └──────────────────────┘
│  - Validation         - Auth            - Configuration       
│  - Availability       - Permissions     - Master Data         
│  - Scheduling         - Logging         - Maintenance         
└─────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                    DATA ACCESS LAYER (DAO)                       │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐       │
│  │ User     │  │ Appt     │  │ Admin    │  │ Settings │       │
│  │ DAO      │  │ DAO      │  │ DAO      │  │ DAO      │  ...  │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘       │
│  - CRUD Operations   - Queries   - Persistence              │
└─────────────────────────────────────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                       DATABASE LAYER                             │
│                                                                  │
│  MySQL Database: appointment_booking_db                         │
│  ┌─────────────┐  ┌──────────┐  ┌─────────────┐ ┌──────────┐  │
│  │ Users       │  │ Appts    │  │ AdminUsers  │ │ Settings │  │
│  │ CarDetails  │  │ Appt     │  │ WorkingHrs  │ │ Holidays │  │
│  │             │  │ Statuses │  │ BreakTimes  │ │ Logs     │  │
│  └─────────────┘  └──────────┘  └─────────────┘ └──────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### Package Structure

```
com.booking.appointment/
├── model/              # Data models/entities
│   ├── User.java
│   ├── Appointment.java
│   ├── CarDetails.java
│   ├── AdminUser.java
│   ├── AppointmentStatus.java
│   ├── AppSetting.java
│   ├── WorkingHours.java
│   ├── BreakTime.java
│   ├── Holiday.java
│   ├── TimeSlotTemplate.java
│   └── AuditLog.java
│
├── dao/                # Data Access Objects
│   ├── interfaces/
│   │   ├── BaseDAO.java
│   │   ├── UserDAO.java
│   │   ├── AppointmentDAO.java
│   │   ├── AdminUserDAO.java
│   │   ├── AppSettingDAO.java
│   │   ├── CarDetailsDAO.java
│   │   ├── HolidayDAO.java
│   │   └── ...
│   └── impl/
│       ├── UserDAOImpl.java
│       ├── AppointmentDAOImpl.java
│       ├── AppSettingDAOImpl.java
│       └── ...
│
├── service/            # Business Logic Services
│   ├── AppointmentService.java
│   ├── UserService.java
│   ├── AdminService.java
│   ├── AppSettingService.java
│   ├── CarDetailsService.java
│   ├── HolidayService.java
│   └── impl/
│       ├── AppointmentServiceImpl.java
│       ├── UserServiceImpl.java
│       └── ...
│
├── controller/         # REST API Controllers
│   ├── AppointmentController.java
│   ├── AdminController.java
│   └── UserController.java
│
├── config/             # Configuration Classes
│   ├── DatabaseConfig.java
│   ├── SecurityConfig.java
│   └── AppConfig.java
│
├── util/               # Utility Classes
│   ├── ValidationUtil.java
│   ├── DateTimeUtil.java
│   ├── ApiResponse.java
│   └── Constants.java
│
└── AppointmentBookingApplication.java  # Entry point
```

---

## Features

### User Features

#### 1. Appointment Booking
- Browse available dates within configurable range (default: 40 days)
- View available time slots for selected date
- Provide customer information (name, email, phone)
- Register vehicle information (make, model, year, license plate)
- Add special notes/requirements
- Receive confirmation

#### 2. Appointment Management
- View upcoming appointments
- View appointment history
- Reschedule appointments
- Cancel appointments
- Receive confirmation emails

#### 3. User Profile
- Create account automatically on first booking
- Update personal information
- Manage multiple vehicles
- View booking history

### Admin Features

#### 1. Appointment Management
- View all appointments (calendar and list views)
- Edit appointment details
- Update appointment status (Booked, Completed, Canceled, etc.)
- Delete appointments
- View appointment details with customer info

#### 2. Configuration Settings
- Appointment date range (default: 40 days)
- Time slot duration (default: 15 minutes)
- Maximum appointments per day
- Allow/disable same-day booking

#### 3. Business Hours Management
- Set working hours per day
- Define break times (lunch, maintenance)
- Manage holidays and closures
- Exclude weekends

#### 4. System Administration
- Admin user management
- Audit log tracking
- System statistics and reports
- Backup and maintenance

#### 5. Master Data Management
- Appointment statuses (BOOKED, COMPLETED, CANCELED, NO_SHOW, PENDING, RESCHEDULED)
- Extensible for adding new statuses
- Holiday calendar management
- Time slot templates

---

## Installation & Setup

### Prerequisites
- Java 11 or higher
- MySQL 8.0 or higher
- Maven 3.6+
- Git
- Web browser (Chrome, Firefox, Safari, Edge)

### Step 1: Database Setup

```bash
# Connect to MySQL
mysql -u root -p

# Create database
SOURCE /path/to/AppointmentBooking/sql/schema.sql
```

The script will:
- Create the `appointment_booking_db` database
- Create all necessary tables with indexes
- Insert default data (statuses, working hours, settings, admin user)
- Create views for easier querying

### Step 2: Configure Database Connection

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/appointment_booking_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### Step 3: Build the Application

```bash
cd AppointmentBooking
mvn clean install
```

### Step 4: Run the Application

```bash
# Using Maven
mvn spring-boot:run

# Or run the JAR
java -jar target/appointment-booking-1.0.0.jar
```

The application will start on `http://localhost:8080`

### Step 5: Access the System

- **User Booking**: http://localhost:8080/booking.html
- **Admin Login**: http://localhost:8080/admin/login
  - Default credentials: `admin` / `admin123`
- **Admin Dashboard**: http://localhost:8080/admin/dashboard

---

## Database Schema

### Tables Overview

#### 1. `users` Table
Stores customer information
```sql
- user_id (PK): Auto-increment primary key
- first_name: VARCHAR(50) - First name
- last_name: VARCHAR(50) - Last name
- email: VARCHAR(100) UNIQUE - Email address
- phone: VARCHAR(20) - Phone number
- created_at: TIMESTAMP - Record creation time
- updated_at: TIMESTAMP - Last modification time
```

**Indexes**: email, first_name, last_name

#### 2. `car_details` Table
Stores vehicle information
```sql
- car_id (PK): Auto-increment primary key
- user_id (FK): Reference to users table
- make: VARCHAR(50) - Vehicle make (manufacturer)
- model: VARCHAR(50) - Vehicle model
- year: INT - Vehicle year
- color: VARCHAR(30) - Vehicle color
- license_plate: VARCHAR(20) - License plate number
- vin: VARCHAR(50) UNIQUE - Vehicle Identification Number
- created_at: TIMESTAMP
- updated_at: TIMESTAMP
```

**Indexes**: user_id, license_plate, vin

#### 3. `appointments` Table
Core appointment records
```sql
- appointment_id (PK): Auto-increment primary key
- user_id (FK): Reference to users table
- car_id (FK): Reference to car_details table
- appointment_date: DATE - Appointment date
- appointment_time: TIME - Appointment start time
- end_time: TIME - Appointment end time
- status_id (FK): Reference to appointment_statuses table
- notes: VARCHAR(500) - Special notes
- created_at: TIMESTAMP
- updated_at: TIMESTAMP
```

**Constraints**: UNIQUE KEY on (appointment_date, appointment_time)
**Indexes**: user_id, car_id, appointment_date, status_id, datetime

#### 4. `appointment_statuses` Table (Master Data)
Available appointment statuses
```sql
- status_id (PK): Auto-increment primary key
- status_name: VARCHAR(50) UNIQUE - Status name
- status_description: VARCHAR(255) - Description
- display_order: INT - Display order in UI
- is_active: BOOLEAN - Active/inactive flag
- created_at: TIMESTAMP
```

**Default Statuses**:
- BOOKED: Appointment is confirmed
- COMPLETED: Service completed
- CANCELED: Appointment canceled
- NO_SHOW: Customer didn't arrive
- PENDING: Awaiting confirmation
- RESCHEDULED: Moved to different time

#### 5. `admin_users` Table
Administrator accounts
```sql
- admin_id (PK): Auto-increment primary key
- username: VARCHAR(50) UNIQUE - Login username
- password: VARCHAR(255) - SHA-256 hashed password
- email: VARCHAR(100) - Admin email
- full_name: VARCHAR(100) - Display name
- last_login:TIMESTAMP NULL - Last login time
- is_active: BOOLEAN - Account active status
- created_at: TIMESTAMP
- updated_at: TIMESTAMP
```

#### 6. `app_settings` Table
Configuration parameters
```sql
- setting_id (PK): Auto-increment primary key
- setting_key: VARCHAR(100) UNIQUE - Setting identifier
- setting_value: VARCHAR(500) - Setting value
- setting_type: VARCHAR(20) - Value type (INT, FLOAT, STRING, BOOLEAN, JSON)
- description: VARCHAR(255) - Setting description
- is_configurable: BOOLEAN - Allows admin change
- updated_by: INT (FK) - Admin who last updated
- created_at: TIMESTAMP
- updated_at: TIMESTAMP
```

**Key Settings**:
- `DEFAULT_APPOINTMENT_RANGE_DAYS` (INT): 40
- `DEFAULT_TIME_SLOT_DURATION_MINUTES` (INT): 15
- `MAX_APPOINTMENTS_PER_DAY` (INT): 32
- `ALLOW_SAME_DAY_BOOKING` (BOOLEAN): true

#### 7. `working_hours` Table
Business operating hours
```sql
- working_hour_id (PK): Auto-increment primary key
- day_of_week: INT UNIQUE - 0=Sunday to 6=Saturday
- start_time: TIME - Opening time
- end_time: TIME - Closing time
- is_working_day: BOOLEAN - Whether business is open
- created_at: TIMESTAMP
- updated_at: TIMESTAMP
```

**Default Configuration**:
- Sunday-Saturday: Sunday & Saturday closed
- Monday-Friday: 9 AM - 6 PM

#### 8. `break_times` Table
Break periods (lunch, maintenance)
```sql
- break_id (PK): Auto-increment primary key
- day_of_week: INT NULL - NULL means all days, 0-6 for specific days
- start_time: TIME - Break start
- end_time: TIME - Break end
- break_type: VARCHAR(50) - LUNCH, BREAK, MAINTENANCE
- is_active: BOOLEAN - Whether break is active
- created_at: TIMESTAMP
- updated_at: TIMESTAMP
```

**Default**: Lunch 12:00-13:00 all days

#### 9. `holidays` Table
Company holidays
```sql
- holiday_id (PK): Auto-increment primary key
- holiday_date: DATE UNIQUE - Holiday date
- holiday_name: VARCHAR(100) - Holiday name
- is_active: BOOLEAN - Whether holiday is active
- created_at: TIMESTAMP
- updated_at: TIMESTAMP
```

#### 10. `time_slot_templates` Table
Available appointment time slots
```sql
- slot_id (PK): Auto-increment primary key
- start_time: TIME - Slot start time
- end_time: TIME - Slot end time
- is_active: BOOLEAN - Whether slot is available
- created_at: TIMESTAMP
- updated_at: TIMESTAMP
```

**Note**: Automatically generated based on time slot duration

#### 11. `audit_logs` Table
Administrative action tracking
```sql
- log_id (PK): Auto-increment BIGINT primary key
- admin_id (FK): Admin who performed action
- action: VARCHAR(100) - Action performed
- entity_type: VARCHAR(50) - Type of entity affected
- entity_id: INT - ID of affected entity
- changes_before: VARCHAR(1000) - JSON before state
- changes_after: VARCHAR(1000) - JSON after state
- ip_address: VARCHAR(45) - IP address of requester
- created_at: TIMESTAMP - When action occurred
```

### Database Views

#### `available_time_slots_view`
Shows all active time slots sorted by time
```sql
SELECT slot_id, start_time, end_time, formatted_start, formatted_end
FROM time_slot_templates WHERE is_active = TRUE
```

#### `appointments_detail_view` 
Complete appointment details with user and car information
```sql
SELECT appointment_id, appointment_date, appointment_time, 
       first_name, last_name, email, make, model, year, license_plate,
       status_name, notes, created_at, updated_at
FROM appointments joined with users, car_details, appointment_statuses
```

### Query Optimization

Indexes created for performance:
- `idx_user_id` on appointments.user_id
- `idx_car_id` on appointments.car_id
- `idx_appointment_date` on appointments.appointment_date
- `idx_status_id` on appointments.status_id
- `idx_datetime` on appointments(appointment_date, appointment_time)
- `idx_email` on users.email
- `idx_license_plate` on car_details.license_plate
- `idx_vin` on car_details.vin

---

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Appointment Endpoints

#### 1. Get Available Dates
```
GET /appointments/available-dates

Response:
{
  "success": true,
  "message": "Available dates retrieved",
  "data": ["2026-03-05", "2026-03-06", ...],
  "statusCode": 200
}
```

#### 2. Get Available Time Slots
```
GET /appointments/available-slots?date=2026-03-05

Response:
{
  "success": true,
  "message": "Available time slots retrieved",
  "data": ["09:00", "09:15", "09:30", ...],
  "statusCode": 200
}
```

#### 3. Book New Appointment
```
POST /appointments/book

Request Body:
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "licensePlate": "ABC123",
  "appointmentDate": "2026-03-05",
  "appointmentTime": "10:00",
  "notes": "Oil change needed"
}

Response:
{
  "success": true,
  "message": "Appointment booked successfully",
  "data": {
    "appointmentId": 1,
    "userId": 5,
    "carId": 3,
    "appointmentDate": "2026-03-05",
    "appointmentTime": "10:00:00",
    "statusId": 1,
    "notes": "Oil change needed",
    "statusName": "BOOKED"
  },
  "statusCode": 200
}
```

#### 4. Get Upcoming Appointments
```
GET /appointments/upcoming?userId=5

Response:
{
  "success": true,
  "message": "Upcoming appointments retrieved",
  "data": [...array of appointments...],
  "statusCode": 200
}
```

#### 5. Update Appointment
```
PUT /appointments/{appointmentId}

Request Body:
{
  "appointmentDate": "2026-03-06",
  "appointmentTime": "14:00",
  "notes": "Updated notes"
}

Response:
{
  "success": true,
  "message": "Appointment updated successfully",
  "data": {...updated appointment...},
  "statusCode": 200
}
```

#### 6. Cancel Appointment
```
DELETE /appointments/{appointmentId}

Response:
{
  "success": true,
  "message": "Appointment cancelled successfully",
  "data": "",
  "statusCode": 200
}
```

### Admin Endpoints

#### 1. Admin Login
```
POST /admin/login

Request Body:
{
  "username": "admin",
  "password": "admin123"
}

Response:
{
  "success": true,
  "message": "Login successful",
  "data": {
    "adminId": 1,
    "username": "admin",
    "email": "admin@example.com",
    "fullName": "System Administrator",
    "isActive": true
  },
  "statusCode": 200
}
```

#### 2. Get All Appointments
```
GET /admin/appointments

Response:
{
  "success": true,
  "message": "All appointments retrieved",
  "data": [...array of all appointments...],
  "statusCode": 200
}
```

#### 3. Get Calendar Appointments
```
GET /admin/appointments/calendar?startDate=2026-03-01&endDate=2026-03-31

Response:
{
  "success": true,
  "message": "Calendar appointments retrieved",
  "data": [...appointments in the range...],
  "statusCode": 200
}
```

#### 4. Update Appointment Status
```
PUT /admin/appointments/{appointmentId}/status

Request Body:
{
  "statusName": "COMPLETED"
}

Response:
{
  "success": true,
  "message": "Status updated successfully",
  "data": "",
  "statusCode": 200
}
```

#### 5. Delete Appointment
```
DELETE /admin/appointments/{appointmentId}

Response:
{
  "success": true,
  "message": "Appointment deleted successfully",
  "data": "",
  "statusCode": 200
}
```

#### 6. Update Settings
```
PUT /admin/settings/{settingKey}

Request Body:
{
  "settingValue": "50"
}

Response:
{
  "success": true,
  "message": "Setting updated successfully",
  "data": "",
  "statusCode": 200
}
```

#### 7. Get Dashboard Statistics
```
GET /admin/dashboard/stats

Response:
{
  "success": true,
  "message": "Dashboard statistics retrieved",
  "data": {
    "totalAppointments": 145,
    "todayAppointments": 8,
    "pendingConfirmations": 3,
    "completedToday": 5
  },
  "statusCode": 200
}
```

---

## User Guide

### For Customers

#### Step 1: Access Booking System
1. Open browser and navigate to `http://localhost:8080/booking.html`
2. You should see the appointment booking form

#### Step 2: Fill Personal Information
1. Enter your first name
2. Enter your last name
3. Enter your email address
4. (Optional) Enter phone number

#### Step 3: Add Vehicle Information
1. Select vehicle make (e.g., Toyota, Honda)
2. Select vehicle model
3. Enter vehicle year
4. (Optional) Enter vehicle color
5. Enter license plate number

#### Step 4: Select Appointment Date and Time
1. Click on the "Preferred Date" field
2. Choose a date from the calendar (within 40 days)
3. Available time slots for that date will appear automatically
4. Click on your preferred time slot (shown as "09:00", "09:15", etc.)
5. The selected time will be highlighted in blue

#### Step 5: Add Special Notes
1. In the "Additional Notes" field, describe any special requirements
2. Example: "Oil change needed", "Tire rotation", etc.

#### Step 6: Confirm Booking
1. Review all information
2. Click the "Book Appointment" button
3. You will receive a success message
4. A confirmation email will be sent to your email address

#### Step 7: Manage Your Appointments
After booking, you can:
- View upcoming appointments at any time
- Reschedule appointments
- Cancel appointments if needed
- View past appointment history

### Contact Information
For questions or issues:
- Email: support@appointmentbooking.com
- Phone: 1-800-BOOKING
- Hours: Monday-Friday, 9 AM - 6 PM

---

## Admin Guide

### For Administrators

#### Initial Setup

##### 1. First-Time Login
- URL: `http://localhost:8080/admin/login`
- Default Credentials:
  - Username: `admin`
  - Password: `admin123`
- **⚠️ IMPORTANT**: Change the default password immediately after first login

##### 2. Configure System Settings
1. Log in to admin dashboard
2. Click "Settings" in the sidebar
3. Configure:
   - **Appointment Range**: Set number of days customers can book ahead (default: 40)
   - **Time Slot Duration**: Set duration of each slot in minutes (default: 15)
   - **Max Appointments Per Day**: Limit appointments per day
   - **Allow Same-Day Booking**: Enable/disable same-day bookings
4. Click "Save Settings"

##### 3. Set Working Hours
1. Click "Settings" → "Working Hours"
2. For each day of the week:
   - Click the day to edit
   - Set opening time (e.g., 09:00)
   - Set closing time (e.g., 18:00)
   - Mark as working day/holiday
3. Save changes

##### 4. Configure Break Times
1. Click "Settings" → "Break Times"
2. Add breaks (lunch, maintenance, etc.):
   - Select applicable days (or "All Days")
   - Set break start time (e.g., 12:00)
   - Set break end time (e.g., 13:00)
   - Select break type
3. Click "Add Break"
4. View and edit existing breaks in the list

##### 5. Manage Holidays
1. Click "Holidays"
2. To add a holiday:
   - Click "Add Holiday"
   - Select date
   - Enter holiday name (e.g., "Christmas")
   - Click "Add"
3. Holidays automatically block booking for that date

#### Daily Management

##### View Today's Appointments
1. Dashboard shows "Today's Appointments" card
2. Shows appointment count and time distribution
3. Click "View All Today" to see full list

##### Appointment Calendar View
1. Click "Calendar View" in sidebar
2. Navigate months using arrow buttons
3. Days with appointments show indicators
4. Click a date to view appointments for that day
5. Click an appointment to see details

##### Appointment List View
1. Click "All Appointments" in sidebar
2. Shows table of all appointments (paginated)
3. Columns show:
   - Customer name
   - Date & time
   - Vehicle information
   - Current status
   - Action buttons

##### Update Appointment Status
1. Find appointment in list
2. Click "Edit" button
3. Select new status from dropdown:
   - **BOOKED**: Confirmed appointment
   - **COMPLETED**: Service finished
   - **CANCELED**: Customer canceled
   - **NO_SHOW**: Customer didn't arrive
   - **PENDING**: Awaiting confirmation
   - **RESCHEDULED**: Moved to different time
4. Add notes if needed
5. Click "Save"

##### Reschedule Appointment
1. Click "Edit" on the appointment
2. Select new date
3. Select new time
4. Click "Save"
5. System prevents conflicts automatically

##### Cancel Appointment
1. Click "Delete" button on appointment
2. Confirm cancellation in dialog
3. Note: You can also update status to "CANCELED"

##### Add Notes to Appointment
1. Click "Edit" on the appointment
2. Click "Notes" section
3. Add or update notes
4. Click "Save"

#### Customer Management

##### Search for Customer
1. Click "Customers" in sidebar
2. Type name or email in search box
3. Click "Search" or press Enter
4. View customer details:
   - Contact information
   - Registered vehicles
   - Appointment history
   - Total appointments made

##### View Customer Details
1. Click on customer name in search results
2. See customer profile with:
   - Personal information
   - All registered vehicles
   - Complete appointment history
   - Total spend/value

##### View Customer Appointments
1. Search for customer
2. In customer profile, click "View All Appointments"
3. Shows past and upcoming appointments
4. Can filter by status or date range

#### Reporting & Analytics

##### Dashboard Statistics
- Total Appointments: All-time appointment count
- Today's Appointments: Appointments scheduled for today
- Pending Confirmations: Appointments awaiting confirmation
- System Status: Current system health indicator

##### Monthly Reports
1. Click "Reports" in sidebar
2. Select month and year
3. View statistics:
   - Total appointments booked
   - Breakdown by status
   - Peak booking times
   - Customer satisfaction metrics

##### Audit Logs
1. Click "Audit Logs" in sidebar
2. View all administrative actions
3. Filter by date, admin user, or action type
4. Shows who made changes and when
5. Important for compliance and troubleshooting

#### Maintenance Tasks

##### Backup Database
1. Use MySQL backup tools:
   ```bash
   mysqldump -u root -p appointment_booking_db > backup.sql
   ```
2. Store backup in secure location
3. Test restore procedures regularly

##### Clear Old Audit Logs
1. Click "System" → "Maintenance"
2. Select "Clear Audit Logs Older Than..."
3. Choose number of days (e.g., 90)
4. Click "Clear"

##### Database Optimization
1. Regularly run indexes
2. Monitor query performance
3. Check disk space
4. Archive old completed appointments if needed

---

## Configuration

### Application Properties

Edit `src/main/resources/application.properties` to customize:

```properties
# Server
server.port=8080                              # Server port

# Database
spring.datasource.url=jdbc:mysql://...       # Database URL
spring.datasource.username=root               # DB username
spring.datasource.password=                   # DB password

# Appointment Settings
appointment.default-range-days=40            # Days ahead to show
appointment.default-slot-duration-minutes=15 # Slot length
appointment.max-appointments-per-day=32      # Daily max
appointment.allow-same-day-booking=true      # Same day bookings

# Email Configuration
spring.mail.host=smtp.gmail.com              # Email server
spring.mail.username=your-email@gmail.com    # Email account
spring.mail.password=app-password            # Email password

# Logging
logging.level.com.booking.appointment=DEBUG  # Log level
logging.file.name=logs/appointment.log       # Log file
```

### Database Configuration

#### Connection Pooling
Adjust HikariCP settings in `application.properties`:
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
```

#### Performance Tuning
```properties
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true
spring.jpa.properties.hibernate.jdbc.fetch_size=100
```

---

## Troubleshooting

### Common Issues and Solutions

#### Issue: "Connection Refused" on Startup
**Problem**: Cannot connect to MySQL database

**Solutions**:
1. Verify MySQL is running:
   ```bash
   # Windows
   net start MySQL80
   
   # Linux
   sudo systemctl start mysql
   ```
2. Check database credentials in `application.properties`
3. Verify database and tables exist:
   ```mysql
   SHOW DATABASES;
   USE appointment_booking_db;
   SHOW TABLES;
   ```
4. Check MySQL port (default 3306):
   ```bash
   netstat -an | grep 3306
   ```

#### Issue: "Table doesn't exist"
**Problem**: SQL error about missing tables

**Solution**:
1. Re-run the schema creation script:
   ```bash
   mysql -u root -p < sql/schema.sql
   ```
2. Or manually create tables:
   ```mysql
   USE appointment_booking_db;
   SOURCE sql/schema.sql;
   ```

#### Issue: "Admin login fails"
**Problem**: Cannot log in with default credentials

**Solutions**:
1. Verify default admin exists:
   ```mysql
   SELECT * FROM admin_users WHERE username='admin';
   ```
2. Reset admin password:
   ```mysql
   UPDATE admin_users SET password='hashed_password' WHERE username='admin';
   ```
3. Check if admin account is active:
   ```mysql
   SELECT is_active FROM admin_users WHERE username='admin';
   ```

#### Issue: "Appointment time slots not showing"
**Problem**: When selecting date, no time slots appear

**Solutions**:
1. Verify time slots exist:
   ```mysql
   SELECT COUNT(*) FROM time_slot_templates;
   ```
2. If empty, regenerate time slots:
   ```mysql
   DELETE FROM time_slot_templates;
   -- Then run the INSERT statements from schema.sql
   ```
3. Check working hours for that day:
   ```mysql
   SELECT * FROM working_hours WHERE day_of_week=2;  -- Monday=1
   ```

#### Issue: "Emails not sending"
**Problem**: Confirmation emails not being sent

**Solutions**:
1. Check email configuration in `application.properties`
2. Verify SMTP credentials are correct
3. If using Gmail, enable "Less secure app access"
4. Check logs for email errors:
   ```bash
   tail -f logs/appointment-booking.log | grep -i email
   ```

#### Issue: "Slow appointment queries"
**Problem**: Appointment list takes long to load

**Solutions**:
1. Check indexes exist:
   ```mysql
   SHOW INDEX FROM appointments;
   ```
2. Optimize queries:
   ```mysql
   ANALYZE TABLE appointments;
   OPTIMIZE TABLE appointments;
   ```
3. Add missing indexes:
   ```mysql
   CREATE INDEX idx_datetime ON appointments(appointment_date, appointment_time);
   ```
4. Archive old appointments if table is very large

#### Issue: "Port 8080 already in use"
**Problem**: Another application using port 8080

**Solutions**:
1. Find process using port:
   ```bash
   # Windows
   netstat -ano | findstr :8080
   
   # Linux
   lsof -i :8080
   ```
2. Kill the process or use different port:
   ```properties
   server.port=8081  # In application.properties
   ```

#### Issue: "Maven build fails"
**Problem**: `mvn clean install` fails

**Solutions**:
1. Clear Maven cache:
   ```bash
   mvn clean
   ```
2. Update dependencies:
   ```bash
   mvn dependency:resolve-plugins
   ```
3. Check Java version:
   ```bash
   java -version  # Should be 11 or higher
   ```

### Debug Mode

Enable detailed debugging:

1. Set logging level to DEBUG:
   ```properties
   logging.level.com.booking.appointment=DEBUG
   logging.level.org.springframework.web=DEBUG
   logging.level.org.hibernate.SQL=DEBUG
   ```

2. View logs:
   ```bash
   tail -f logs/appointment-booking.log
   ```

3. Enable SQL logging in application:
   ```properties
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   ```

### Performance Monitoring

Monitor system performance:

```bash
# Check database size
mysql -u root -p
SELECT TABLE_NAME, ROUND(((data_length + index_length) / 1024 / 1024), 2) AS size_mb
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'appointment_booking_db';

# Check slow queries
SELECT * FROM mysql.slow_log;
```

---

## Support & Maintenance

### Regular Maintenance Tasks

**Daily**:
- Monitor system logs for errors
- Check database backup status
- Review today's appointments

**Weekly**:
- Review admin actions in audit logs
- Check system performance metrics
- Backup database

**Monthly**:
- Generate reports
- Review customer feedback
- Update documentation

**Quarterly**:
- Test disaster recovery procedures
- Review security policies
- Plan for improvements

### Future Enhancements

Potential features for future versions:
- SMS notifications for appointments
- Payment processing integration
- Customer ratings/reviews
- Recurring appointments
- Staff scheduling
- Multi-location support
- Mobile app
- Google Calendar integration
- Automated reminders

---

## Contact & Support

For issues, questions, or feature requests:
- **Email**: support@appointmentbooking.com
- **GitHub**: [Repository URL]
- **Issues**: GitHub Issues page
- **Documentation**: [Wiki URL]

---

**Document Version**: 1.0
**Last Updated**: March 2, 2026
**Maintained By**: Development Team
