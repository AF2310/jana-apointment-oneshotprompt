# Appointment Booking System - Quick Reference Guide

## 📋 Documentation Index

| Document | Content | Best For |
|----------|---------|----------|
| **README.md** | System overview, installation, setup | First-time setup, understanding architecture |
| **API_REFERENCE.md** | Complete API endpoints, examples, data models | API users, testing API endpoints |
| **IMPLEMENTATION_ROADMAP.md** | 6-week development plan, detailed tasks | Development team planning |
| **DEPLOYMENT_TESTING.md** | Deployment procedures, testing strategies | DevOps, QA automation |
| **ADVANCED_CONFIG.md** | Database tuning, security, monitoring, HA | Operations team |
| **PROJECT_SUMMARY.md** | Executive overview, progress tracking | Project managers, stakeholders |

---

## 🚀 Quick Start

### For Developers
```bash
# 1. Set up Java environment
java -version  # Ensure Java 11+

# 2. Clone and navigate
cd AppointmentBooking

# 3. Build project
mvn clean install

# 4. Set up database
mysql -u root -p < sql/schema.sql

# 5. Run application
mvn spring-boot:run

# 6. Access application
# User: http://localhost:8080/
# Admin: http://localhost:8080/admin/login (admin/admin123)
```

### For DevOps
```bash
# 1. Build Docker image
docker build -t appointment-booking:1.0.0 .

# 2. Run with Docker Compose
docker-compose up -d

# 3. Verify deployment
docker ps
curl http://localhost:8080/health
```

---

## 📁 Project Structure at a Glance

```
AppointmentBooking/
├── src/main/java/com/booking/appointment/
│   ├── model/               # 10 entity classes
│   ├── dao/                 # 11 DAO interfaces + 3 implementations
│   ├── service/             # 6 service interfaces
│   ├── controller/          # 2 REST controllers (15 endpoints)
│   ├── util/                # Validation, date/time utilities
│   └── config/              # Database configuration
├── src/main/resources/
│   ├── application.properties
│   └── templates/           # 3 HTML templates
├── sql/
│   └── schema.sql           # Database setup (11 tables)
└── docs/
    ├── README.md            # 600+ lines
    ├── API_REFERENCE.md     # 800+ lines
    ├── DEPLOYMENT_TESTING.md # 500+ lines
    ├── ADVANCED_CONFIG.md    # 600+ lines
    ├── IMPLEMENTATION_ROADMAP.md # 700+ lines
    └── PROJECT_SUMMARY.md   # This index
```

---

## 🔑 Key Files & Classes

### Core Entities (model/)
- **User.java** - Customer account
- **Appointment.java** - Booking record
- **CarDetails.java** - Vehicle info
- **AppointmentStatus.java** - Status master data
- **AdminUser.java** - Admin account
- **AppSetting.java** - Configuration
- **WorkingHours.java** - Business hours
- **BreakTime.java** - Break periods
- **Holiday.java** - Closure dates
- **TimeSlotTemplate.java** - Available slots

### Data Access (dao/)
- **UserDAO/UserDAOImpl** - User queries
- **AppointmentDAO/AppointmentDAOImpl** - Booking queries
- **AppSettingDAO/AppSettingDAOImpl** - Config queries
- **[7 other DAOs]** - Holiday, BreakTime, etc.

### Business Logic (service/)
- **AppointmentService** - Booking logic
- **UserService** - User management
- **AdminService** - Admin operations
- **AppSettingService** - Configuration access
- **CarDetailsService** - Vehicle management
- **HolidayService** - Holiday management

### REST API (controller/)
- **AppointmentController** - 7 booking endpoints
- **AdminController** - 8 admin endpoints

### Utilities (util/)
- **ValidationUtil** - Email, phone, password validation
- **DateTimeUtil** - Date/time operations
- **ApiResponse<T>** - Response wrapper

### Web Templates (resources/templates/)
- **booking.html** - User booking interface
- **admin-login.html** - Admin login
- **admin-dashboard.html** - Admin dashboard

### Configuration (resources/)
- **application.properties** - All settings
- **DatabaseConfig.java** - Connection management

### Database (sql/)
- **schema.sql** - Tables, indexes, seed data

---

## 📊 Database Quick Reference

### 11 Core Tables
1. **users** - Customer accounts
2. **car_details** - Vehicle information
3. **appointments** - Booking records
4. **appointment_statuses** - Status definitions
5. **admin_users** - Admin accounts
6. **app_settings** - Configuration
7. **working_hours** - Business hours (Mon-Sun)
8. **break_times** - Break periods
9. **holidays** - Closure dates
10. **time_slot_templates** - Available slots
11. **audit_logs** - Activity tracking

### Key Relationships
```
users (1) ──────────── (M) appointments
users (1) ──────────── (M) car_details
car_details (1) ──────── (M) appointments
appointment_statuses (1) ── (M) appointments
admin_users (1) ──────── (M) audit_logs
```

### Sample Queries
```sql
-- Get available slots for date
SELECT * FROM time_slot_templates 
WHERE is_active = true 
ORDER BY start_time;

-- Get today's appointments
SELECT a.* FROM appointments a
WHERE DATE(a.appointment_date) = CURDATE()
AND a.status_id != 3;  -- Exclude CANCELED

-- Check if time slot is available
SELECT COUNT(*) as conflicts FROM appointments
WHERE appointment_date = '2026-03-10'
AND appointment_time = '10:00'
AND status_id != 3;  -- Exclude CANCELED
```

---

## 🔗 REST API Quick Reference

### Public Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | /api/appointments/available-dates | Get open dates (next 40 days) |
| GET | /api/appointments/available-slots?date=... | Get open times for date |
| POST | /api/appointments/book | Create appointment |
| PUT | /api/appointments/{id} | Reschedule appointment |
| DELETE | /api/appointments/{id} | Cancel appointment |
| GET | /api/appointments/upcoming?userId=... | User's upcoming appointments |
| GET | /api/appointments/history?userId=... | User's past appointments |

### Admin Endpoints (Require Login)

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | /api/admin/login | Admin authentication |
| GET | /api/admin/appointments?page=... | List all appointments |
| GET | /api/admin/appointments/calendar | Calendar view |
| PUT | /api/admin/appointments/{id}/status | Update status |
| DELETE | /api/admin/appointments/{id} | Delete appointment |
| GET | /api/admin/settings | Get all settings |
| PUT | /api/admin/settings/{key} | Update setting |
| GET | /api/admin/dashboard/stats | Dashboard statistics |

### Example: Book Appointment
```bash
curl -X POST http://localhost:8080/api/appointments/book \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phone": "5551234567",
    "make": "Toyota",
    "model": "Camry",
    "year": 2022,
    "licensePlate": "ABC123",
    "appointmentDate": "2026-03-10",
    "appointmentTime": "10:00",
    "notes": "Oil change"
  }'
```

---

## 🛠️ Configuration Quick Reference

### application.properties Key Settings

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/appointment_booking_db
spring.datasource.username=root
spring.datasource.password=

# Appointment business rules
appointment.default-range-days=40              # Configurable booking range
appointment.default-slot-duration-minutes=15   # Slot length
appointment.max-appointments-per-day=32        # Daily capacity
appointment.allow-same-day-booking=true        # Same-day booking allowed

# Working hours (stored in database)
# Email notifications (SMTP configured)
# Logging levels (DEBUG for development)
```

### Change Business Settings via Admin API
```bash
# Update appointment range to 50 days
curl -X PUT http://localhost:8080/api/admin/settings/default-range-days \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer admin-token" \
  -d '{"settingValue": "50"}'
```

---

## 🧪 Testing Quick Reference

### Unit Test Structure
```java
@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTest {
    @Mock private AppointmentDAO appointmentDAO;
    @InjectMocks private AppointmentService service;
    
    @Test
    public void testBookAppointment() { ... }
}
```

### API Test with cURL
```bash
# Test available dates
curl http://localhost:8080/api/appointments/available-dates

# Test booking
curl -X POST http://localhost:8080/api/appointments/book -d '...'

# Check response
# Status 200 = Success
# Status 400 = Invalid input
# Status 409 = Conflict (time already booked)
```

### Test User Credentials
```
Email: test@example.com
Admin Username: admin
Admin Password: admin123
```

---

## 🔐 Security Checklist

- ✅ Parameterized SQL queries (prevents SQL injection)
- ✅ Input validation on all fields
- ✅ SHA-256 password hashing
- ✅ Session-based admin authentication
- ✅ CORS configuration
- ✅ Rate limiting (designed)
- ✅ Error handling (no stack traces in API)

### Enable HTTPS
```properties
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=changeit
server.ssl.enabled=true
```

---

## 📈 Performance Tuning Quick Tips

### Database
```sql
-- Create indexes
CREATE INDEX idx_appointments_date ON appointments(appointment_date);
CREATE INDEX idx_users_email ON users(email);

-- Check slow queries
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;
```

### Application
```bash
# Increase heap size
java -Xmx512m -Xms256m -jar app.jar

# Enable caching
spring.cache.type=redis
```

### Expected Performance
- Response time: < 200ms (p95)
- Throughput: > 100 req/sec
- Uptime: > 99.5%

---

## 🐳 Docker Quick Reference

### Build Image
```bash
docker build -t appointment-booking:1.0.0 .
```

### Run Container
```bash
docker run -d \
  --name appointment-app \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-host:3306/db \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  appointment-booking:1.0.0
```

### Docker Compose (Full Stack)
```bash
docker-compose up -d
# Starts: MySQL + Spring Boot App
```

---

## 🚨 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| Port 8080 already in use | Change in application.properties: `server.port=8081` |
| Cannot connect to MySQL | Check MySQL is running: `mysql -u root -p -e "SELECT 1"` |
| Table doesn't exist | Run schema: `mysql -u root -p < sql/schema.sql` |
| Admin login fails | Verify user exists in admin_users table |
| Time slots not showing | Check time_slot_templates has data (should have 32 slots) |
| Email not sending | Verify SMTP settings in application.properties |

---

## 📋 Implementation Checklist

### Phase 1: DAO Implementation (Week 1-2)
- [ ] CarDetailsDAOImpl
- [ ] AdminUserDAOImpl
- [ ] HolidayDAOImpl
- [ ] BreakTimeDAOImpl
- [ ] TimeSlotTemplateDAOImpl
- [ ] WorkingHoursDAOImpl
- [ ] AppointmentStatusDAOImpl
- [ ] AuditLogDAOImpl
- [ ] Write unit tests for all DAOs

### Phase 2: Service Implementation (Week 2-3)
- [ ] AppointmentServiceImpl
- [ ] UserServiceImpl
- [ ] AdminServiceImpl
- [ ] AppSettingServiceImpl
- [ ] CarDetailsServiceImpl
- [ ] HolidayServiceImpl
- [ ] Write unit tests for all services

### Phase 3: Remaining Features (Week 3-4)
- [ ] View controllers for HTML pages
- [ ] Session management
- [ ] Email service integration
- [ ] Email templates

### Phase 4: Testing (Week 4-5)
- [ ] Integration tests
- [ ] API endpoint tests
- [ ] Performance tests
- [ ] Security tests

### Phase 5: Deployment (Week 5-6)
- [ ] Docker configuration
- [ ] CI/CD pipeline
- [ ] Deployment automation
- [ ] Operations handbook

---

## 📞 Support Resources

### Documentation Links
- Full setup: See [README.md](README.md)
- API usage: See [API_REFERENCE.md](API_REFERENCE.md)
- Development plan: See [IMPLEMENTATION_ROADMAP.md](IMPLEMENTATION_ROADMAP.md)
- Testing guide: See [DEPLOYMENT_TESTING.md](DEPLOYMENT_TESTING.md)
- Operations: See [ADVANCED_CONFIG.md](ADVANCED_CONFIG.md)

### Key Configuration Files
- Database setup: `sql/schema.sql`
- App settings: `src/main/resources/application.properties`
- Maven: `pom.xml`

### Developer Contacts
- Architecture: See README.md
- Database: `DatabaseConfig.java`
- API: See `AppointmentController.java` & `AdminController.java`

---

## 🎯 Success Criteria

### Development Phase
- ✅ All 8 remaining DAOs implemented
- ✅ All 6 services implemented
- ✅ >85% unit test coverage
- ✅ All endpoints documented and tested
- ✅ Zero SQL injection vulnerabilities

### Production Phase
- ✅ 99.5% uptime
- ✅ <200ms response time (p95)
- ✅ <1% error rate
- ✅ Automated backups working
- ✅ Zero data loss incidents

---

## 📊 Project Statistics

| Metric | Value |
|--------|-------|
| Total Lines of Code (Design Phase) | ~2,500 |
| DAO Interfaces | 11 |
| Service Interfaces | 6 |
| REST Endpoints | 15 |
| Database Tables | 11 |
| HTML Templates | 3 |
| Documentation Files | 6 |
| Total Documentation Lines | 3,500+ |
| Estimated Remaining Dev Time | 6 weeks |
| Team Size | 2-3 developers |

---

## 🗓️ Key Dates

- **Architecture Complete**: March 2, 2026
- **Target DAO Implementation**: Week 1-2
- **Target Service Implementation**: Week 2-3
- **Target Testing Complete**: Week 4-5
- **Target Production Ready**: Week 6
- **Estimated Go-Live**: Mid-April 2026

---

## 📝 Document Versions

- README.md: v1.0 (600+ lines)
- API_REFERENCE.md: v1.0 (800+ lines)
- DEPLOYMENT_TESTING.md: v1.0 (500+ lines)
- ADVANCED_CONFIG.md: v1.0 (600+ lines)
- IMPLEMENTATION_ROADMAP.md: v1.0 (700+ lines)
- PROJECT_SUMMARY.md: v1.0
- QUICK_REFERENCE.md: v1.0 (this file)

**Last Updated**: March 2, 2026  
**Status**: Ready for Development Phase  
**Next Review**: After DAO Implementation Complete

---

## 🎓 Learning Path

**New to Project?**
1. Start with this QUICK_REFERENCE.md (you are here)
2. Read README.md for system overview
3. Review PROJECT_SUMMARY.md for status
4. Check API_REFERENCE.md for endpoints
5. Follow IMPLEMENTATION_ROADMAP.md for development

**Ready to Develop?**
1. Clone repository
2. Run `mvn clean install`
3. Set up MySQL with `sql/schema.sql`
4. Start with Task 1.1 in IMPLEMENTATION_ROADMAP.md
5. Reference DAO implementations as patterns

**Ready to Deploy?**
1. Build: `mvn clean package`
2. Follow DEPLOYMENT_TESTING.md
3. Use docker-compose for quick start
4. Reference ADVANCED_CONFIG.md for tuning

---

**Quick Reference Guide v1.0**  
**All Documents Available in `/docs` Directory**  
**Questions? Check the Full Documentation!**
