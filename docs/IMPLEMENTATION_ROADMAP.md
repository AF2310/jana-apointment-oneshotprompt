# Appointment Booking System - Implementation Roadmap

## Phase-Based Implementation Plan

### Current Status
**Completion Percentage**: 75% (Architecture & Design Complete)

**Completed Deliverables**:
- ✅ Database schema (11 tables, indexes, views, seed data)
- ✅ Java entity models (10 classes)
- ✅ DAO interfaces (11 interfaces with 100+ methods)
- ✅ Service interfaces (6 interfaces with 80+ methods)
- ✅ REST API controllers (2 controllers with 15 endpoints)
- ✅ HTML/CSS/JavaScript UI templates (3 complete templates)
- ✅ Utility classes (validation, date/time handling)
- ✅ Configuration files (application.properties)
- ✅ Comprehensive documentation

---

## Phase 1: DAO Implementation (Week 1-2)

### Task 1.1: Implement Remaining DAO Classes
**Priority**: CRITICAL
**Estimated Hours**: 40
**Dependencies**: Database schema must exist

#### Detailed Work Breakdown

##### 1.1.1 CarDetailsDAOImpl
```java
File: src/main/java/com/booking/appointment/dao/impl/CarDetailsDAOImpl.java
Methods to implement:
- save(CarDetails) - INSERT
- update(CarDetails) - UPDATE
- findById(Integer) - SELECT by ID
- findAll() - SELECT all
- deleteById(Integer) - DELETE by ID
- findByUserId(Integer) - SELECT by user_id
- findByLicensePlate(String) - SELECT by license_plate
- findByVin(String) - SELECT by vin
- licensePlateExists(String) - COUNT check
- vinExists(String) - COUNT check
- deleteByUserId(Integer) - DELETE by user_id

Estimated LOC: 280
Reference: UserDAOImpl pattern
```

##### 1.1.2 AdminUserDAOImpl
```java
File: src/main/java/com/booking/appointment/dao/impl/AdminUserDAOImpl.java
Methods to implement:
- save(AdminUser) - INSERT with password hashing
- update(AdminUser) - UPDATE
- findById(Integer) - SELECT by ID
- findAll() - SELECT all
- deleteById(Integer) - DELETE by ID
- findByUsername(String) - SELECT by username
- findByEmail(String) - SELECT by email
- usernameExists(String) - COUNT check
- isActive(Integer) - SELECT is_active flag
- updateLastLogin(Integer) - UPDATE last_login timestamp
- countActiveAdmins() - COUNT active admins
- count() - COUNT total

Estimated LOC: 300
Reference: UserDAOImpl pattern
Notes: Requires SHA-256 password hashing integration
```

##### 1.1.3 HolidayDAOImpl
```java
File: src/main/java/com/booking/appointment/dao/impl/HolidayDAOImpl.java
Methods to implement:
- save(Holiday) - INSERT
- update(Holiday) - UPDATE
- findById(Integer) - SELECT by ID
- findAll() - SELECT all
- deleteById(Integer) - DELETE by ID
- findByDate(LocalDate) - SELECT by holiday_date
- findByDateRange(LocalDate, LocalDate) - SELECT between dates
- isHoliday(LocalDate) - Check if date is holiday
- isActiveHoliday(LocalDate) - Check if date is active holiday
- getAllActiveHolidays() - SELECT where is_active = true
- deleteByDate(LocalDate) - DELETE by specific date
- count() - COUNT total

Estimated LOC: 250
Reference: AppointmentDAOImpl for date handling
```

##### 1.1.4 BreakTimeDAOImpl
```java
File: src/main/java/com/booking/appointment/dao/impl/BreakTimeDAOImpl.java
Methods to implement:
- save(BreakTime) - INSERT
- update(BreakTime) - UPDATE
- findById(Integer) - SELECT by ID
- findAll() - SELECT all
- deleteById(Integer) - DELETE by ID
- getBreaksForDay(Integer) - SELECT by day_of_week or NULL (global)
- getAllActiveBreaks() - SELECT where is_active = true
- isBreakTime(Integer, LocalTime) - Check if time is within break
- getGlobalBreakTimes() - SELECT where day_of_week IS NULL
- addBreak(BreakTime) - INSERT new break
- removeBreak(Integer) - DELETE break by ID
- count() - COUNT total

Estimated LOC: 260
Reference: AppointmentDAOImpl for time range logic
```

##### 1.1.5 TimeSlotTemplateDAOImpl
```java
File: src/main/java/com/booking/appointment/dao/impl/TimeSlotTemplateDAOImpl.java
Methods to implement:
- save(TimeSlotTemplate) - INSERT
- update(TimeSlotTemplate) - UPDATE
- findById(Integer) - SELECT by ID
- findAll() - SELECT all
- deleteById(Integer) - DELETE by ID
- getAllActiveSlots() - SELECT where is_active = true
- getAllSlotsSorted() - ORDER BY start_time
- getTotalActiveSlots() - COUNT active slots
- regenerateTimeSlots(int, String, String) - DELETE all and INSERT new range
- deleteAllSlots() - DELETE all slots
- count() - COUNT total

Estimated LOC: 220
Reference: AppointmentDAOImpl pattern
Notes: Handles time slot generation algorithm
```

##### 1.1.6 WorkingHoursDAOImpl
```java
File: src/main/java/com/booking/appointment/dao/impl/WorkingHoursDAOImpl.java
Methods to implement:
- save(WorkingHours) - INSERT
- update(WorkingHours) - UPDATE
- findById(Integer) - SELECT by ID
- findAll() - SELECT all
- deleteById(Integer) - DELETE by ID
- findByDayOfWeek(Integer) - SELECT by day_of_week
- isWorkingDay(Integer) - Check is_working_day flag
- getAllWorkingDays() - SELECT where is_working_day = true
- updateDayWorkingHours(Integer, String, String) - UPDATE times for day
- count() - COUNT total

Estimated LOC: 200
Reference: AppointmentDAOImpl pattern
```

##### 1.1.7 AppointmentStatusDAOImpl
```java
File: src/main/java/com/booking/appointment/dao/impl/AppointmentStatusDAOImpl.java
Methods to implement:
- save(AppointmentStatus) - INSERT
- update(AppointmentStatus) - UPDATE
- findById(Integer) - SELECT by ID
- findAll() - SELECT all
- deleteById(Integer) - DELETE by ID
- findByName(String) - SELECT by status_name
- statusExists(String) - COUNT check
- getAllActiveSorted() - ORDER BY display_order
- getStatusIdByName(String) - SELECT id WHERE status_name
- addNewStatus(String, String, int) - INSERT new status
- count() - COUNT total

Estimated LOC: 200
Reference: UserDAOImpl pattern
```

##### 1.1.8 AuditLogDAOImpl
```java
File: src/main/java/com/booking/appointment/dao/impl/AuditLogDAOImpl.java
Methods to implement:
- save(AuditLog) - INSERT
- update(AuditLog) - UPDATE
- findById(Integer) - SELECT by ID
- findAll() - SELECT all
- deleteById(Integer) - DELETE by ID
- getLogsForAdmin(Integer) - SELECT by admin_id
- getLogsForEntity(String, Integer) - SELECT by entity_type & entity_id
- getLogsByDateRange(LocalDateTime, LocalDateTime) - SELECT between created_at
- getLogsByAction(String) - SELECT by action
- logAction(Integer, String, String, Integer, String, String, String) - INSERT new log
- clearOldLogs(int) - DELETE created_at > days ago
- count() - COUNT total

Estimated LOC: 280
Reference: AppointmentDAOImpl for date handling
Notes: Handles JSON serialization for changes_before/after
```

### Task 1.2: Unit Test DAO Implementations
**Estimated Hours**: 20

```java
// Test files to create:
src/test/java/com/booking/appointment/dao/impl/
├── CarDetailsDAOImplTest.java
├── AdminUserDAOImplTest.java
├── HolidayDAOImplTest.java
├── BreakTimeDAOImplTest.java
├── TimeSlotTemplateDAOImplTest.java
├── WorkingHoursDAOImplTest.java
├── AppointmentStatusDAOImplTest.java
└── AuditLogDAOImplTest.java

Each test includes:
- CRUD operations tests
- Find by specific field tests
- Validation tests
- Edge case handling
```

---

## Phase 2: Service Implementation (Week 2-3)

### Task 2.1: Implement Service Classes
**Priority**: CRITICAL
**Estimated Hours**: 45
**Dependencies**: DAO implementations complete

#### Detailed Work Breakdown

##### 2.1.1 AppointmentServiceImpl
```java
File: src/main/java/com/booking/appointment/service/impl/AppointmentServiceImpl.java
Methods to implement: 16
Key logic:
- bookAppointment(): Validate date/time, check capacity, check conflicts
- updateAppointmentStatus(): Validate status exists, update with audit log
- isTimeSlotAvailable(): Check no overlapping bookings except CANCELED
- getAvailableTimeSlots(): Return slots minus booked times
- cancelAppointment(): Set status to CANCELED, send email notification
- getDayCapacity(): Return max appointments configured for date
- getUpcomingAppointments(userId): Filter future appointments
- getAppointmentHistory(userId): Filter past appointments

Estimated LOC: 320
Complexity: HIGH (date/time logic, availability checks)
```

##### 2.1.2 UserServiceImpl
```java
File: src/main/java/com/booking/appointment/service/impl/UserServiceImpl.java
Methods to implement: 9
Key logic:
- registerUser(): Validate inputs, check email unique, save to DB
- validateUser(): Check email format, name length, phone format
- searchByFirstName(): Delegate to DAO, return list
- searchByLastName(): Delegate to DAO, return list
- updateUserInfo(): Validate, update user record
- getUserWithAppointments(): Fetch user + related appointments

Estimated LOC: 180
Complexity: MEDIUM
```

##### 2.1.3 AdminServiceImpl
```java
File: src/main/java/com/booking/appointment/service/impl/AdminServiceImpl.java
Methods to implement: 13
Key logic:
- authenticateAdmin(): Hash password, compare with DB, return Optional
- changePassword(): Verify old password, hash new, update DB
- recordLastLogin(): Update last_login timestamp
- isAdmin(): Check admin exists and is_active
- getAdminByUsername(): Delegate to DAO
- validateAdmincredentials(): Input validation before auth

Estimated LOC: 220
Complexity: MEDIUM (password hashing integration)
```

##### 2.1.4 AppSettingServiceImpl
```java
File: src/main/java/com/booking/appointment/service/impl/AppSettingServiceImpl.java
Methods to implement: 11
Key logic:
- getSettingAsInteger(): Get value, parse to int, return with default fallback
- getSettingAsBoolean(): Get value, parse to boolean
- getSettingAsString(): Get value as-is
- updateSetting(): Validate value format for type, update in DB
- getDefaultAppointmentRangeDays(): getSettingAsInteger("default-range-days")
- getDefaultTimeSlotDurationMinutes(): getSettingAsInteger("default-slot-duration")
- getMaxAppointmentsPerDay(): getSettingAsInteger("max-appointments-per-day")

Estimated LOC: 200
Complexity: LOW
```

##### 2.1.5 CarDetailsServiceImpl
```java
File: src/main/java/com/booking/appointment/service/impl/CarDetailsServiceImpl.java
Methods to implement: 10
Key logic:
- validateCarDetails(): Check make/model/year format, validate VIN/license plate
- addCarDetails(): Validate, check duplicates, save
- updateCarDetails(): Validate, check license plate not duplicate, update
- removeCarDetails(): Delete car record, check no active appointments
- getUserCars(): Get all cars for user
- findByLicensePlate(): Delegate to DAO
- findByVin(): Delegate to DAO

Estimated LOC: 200
Complexity: MEDIUM (validation logic)
```

##### 2.1.6 HolidayServiceImpl
```java
File: src/main/java/com/booking/appointment/service/impl/HolidayServiceImpl.java
Methods to implement: 10
Key logic:
- addHoliday(): Validate date not past, check not duplicate, save
- updateHoliday(): Validate, check bookings won't conflict
- deleteHoliday(): Delete holiday record
- isHoliday(): Check date is holiday, disable bookings
- getNextHoliday(): Find next holiday from today
- deactivateHoliday(): Set is_active = false
- getHolidayList(): Return active holidays

Estimated LOC: 180
Complexity: MEDIUM
```

### Task 2.2: Unit Test Service Implementations
**Estimated Hours**: 25

```java
// Mock DAOs in tests using Mockito
@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceImplTest {
    @Mock private AppointmentDAO appointmentDAO;
    @Mock private UserDAO userDAO;
    @Mock private CarDetailsDAO carDetailsDAO;
    
    @InjectMocks private AppointmentServiceImpl appointmentService;
    
    // Test methods...
}
```

---

## Phase 3: View Controllers (Week 3)

### Task 3.1: Create View-Serving Controllers
**Priority**: HIGH
**Estimated Hours**: 15

```java
// New controllers to create (distinct from existing REST controllers)
src/main/java/com/booking/appointment/controller/

@Controller
public class BookingViewController {
    @GetMapping("/")
    public String showBookingPage(Model model) {
        // Add model attributes for template
        return "booking";
    }
}

@Controller
public class AdminViewController {
    @GetMapping("/admin/login")
    public String showLoginPage() {
        return "admin-login";
    }
    
    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        // Verify admin session
        return "admin-dashboard";
    }
}
```

### Task 3.2: Session Management & Security
**Estimated Hours**: 10

```java
// Create Spring Security configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/", "/api/appointments/**").permitAll()
            .antMatchers("/admin/**", "/api/admin/**").authenticated()
            .and()
            .formLogin()
            .loginPage("/admin/login")
            .permitAll()
            .and()
            .logout()
            .permitAll();
    }
}
```

---

## Phase 4: Email Integration (Week 4)

### Task 4.1: Implement Email Service
**Estimated Hours**: 12

```java
// Create email service
@Service
public class EmailService {
    @Autowired private JavaMailSender mailSender;
    @Autowired private TemplateEngine templateEngine;
    
    public void sendBookingConfirmation(Appointment apt, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Appointment Confirmation");
        message.setText(buildConfirmationText(apt, user));
        mailSender.send(message);
    }
    
    public void sendCancellationNotice(Appointment apt, User user) {
        // Similar implementation
    }
}
```

### Task 4.2: Email Templates
**Estimated Hours**: 5

```plain
# Email templates to create:
src/main/resources/email-templates/
├── booking-confirmation.html
├── cancellation-notice.html
├── rescheduling-notice.html
└── reminder.html
```

---

## Phase 5: Testing (Week 4-5)

### Task 5.1: Integration Tests
**Estimated Hours**: 30

```java
// Integration test classes to create:
src/test/java/com/booking/appointment/integration/
├── AppointmentBookingFlowTest.java (E2E user flow)
├── AdminOperationsTest.java (Admin CRUD operations)
├── AvailabilityCalculationTest.java (Complex logic)
└── SecurityTest.java (Authentication/authorization)
```

### Task 5.2: API Endpoint Tests
**Estimated Hours**: 20

```java
// Create MockMvc-based tests
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {
    @Autowired private MockMvc mockMvc;
    
    @Test
    public void testBookAppointmentSuccess() throws Exception {
        mockMvc.perform(post("/api/appointments/book")
            .contentType(MediaType.APPLICATION_JSON)
            .content(...))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }
}
```

### Task 5.3: Performance Tests
**Estimated Hours**: 15

```bash
# Load testing with Apache JMeter:
- Simulate 100 concurrent users
- Ramp-up time: 10 seconds
- Test duration: 5 minutes
- Verify response times < 200ms (p95)
- Verify success rate > 99.5%
```

---

## Phase 6: Documentation & DevOps (Week 5)

### Task 6.1: API Documentation
**Estimated Hours**: 10
**Deliverable**: Swagger/OpenAPI definition

```yaml
# docs/openapi.yaml
openapi: 3.0.0
info:
  title: Appointment Booking API
  version: 1.0.0
paths:
  /api/appointments/available-dates:
    get:
      summary: Get available appointment dates
      responses:
        200:
          description: List of available dates
```

### Task 6.2: Docker Configuration
**Estimated Hours**: 8

```dockerfile
# Dockerfile
FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/appointment-booking-*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

```yaml
# docker-compose.yml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: appointment_booking_db
  
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql
```

### Task 6.3: CI/CD Pipeline
**Estimated Hours**: 10

```yaml
# .github/workflows/ci-cd.yml
name: CI/CD Pipeline
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
      - run: mvn clean package
      - run: mvn test
      - run: docker build -t app:latest .
```

---

## Phase 7: Deployment & Operations (Week 6)

### Task 7.1: Production Deployment
**Estimated Hours**: 12

- Deploy to Azure App Service / AWS Elastic Beanstalk
- Configure database backup automation
- Set up monitoring and alerting
- Configure SSL/TLS certificates

### Task 7.2: Operations Handbook
**Estimated Hours**: 8

- Create runbooks for common operations
- Document backup/recovery procedures
- Create incident response procedures
- Document monitoring dashboards

---

## Risk Assessment & Mitigation

### Identified Risks

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|-----------|
| DAO implementation bugs | High | Medium | Comprehensive unit tests before integration |
| Database connection issues | Medium | High | Connection pooling, retry logic, monitoring |
| Timezone issues with date/time | Medium | High | Use Java 8+ java.time, UTC storage |
| Email delivery failures | Medium | Low | Implement retry logic, queue system |
| Performance degradation | Medium | High | Index optimization, caching, load testing |
| Security vulnerabilities | Low | Critical | Security code review, OWASP testing |

---

## Success Criteria

### Phase 1 (DAO Implementation)
- [ ] All 8 DAO classes implemented
- [ ] 100% CRUD functionality working
- [ ] All unit tests passing (>90% code coverage)
- [ ] No SQL injection vulnerabilities

### Phase 2 (Service Implementation)
- [ ] All 6 service classes implemented
- [ ] All business logic rules enforced
- [ ] Service unit tests passing (>85% code coverage)
- [ ] Performance benchmarks met (<100ms per service call)

### Phase 3 (View Controllers)
- [ ] All HTML pages render correctly
- [ ] User can complete full booking flow
- [ ] Admin can perform all CRUD operations
- [ ] Session management working

### Phase 4 (Email Integration)
- [ ] Emails send on booking
- [ ] Emails send on cancellation
- [ ] Email templates render correctly

### Phase 5 (Testing)
- [ ] Integration tests: 100% pass rate
- [ ] API tests: >95% endpoint coverage
- [ ] Performance tests: All targets met
- [ ] Security tests: No vulnerabilities found

### Phase 6 (Documentation)
- [ ] API documentation complete and accurate
- [ ] Deployment guides tested
- [ ] Operations handbook complete

### Phase 7 (Deployment)
- [ ] Application running in production
- [ ] Monitoring dashboards active
- [ ] Backup system operational
- [ ] Zero downtime deployment possible

---

## Timeline Summary

| Phase | Duration | Start | End |
|-------|----------|-------|-----|
| DAO Implementation | 2 weeks | Week 1 | Week 2 |
| Service Implementation | 1 week | Week 2 | Week 3 |
| View Controllers | 1 week | Week 3 | Week 3 |
| Email Integration | 1 week | Week 4 | Week 4 |
| Testing | 2 weeks | Week 4 | Week 5 |
| Documentation & DevOps | 1 week | Week 5 | Week 5 |
| Deployment & Operations | 1 week | Week 6 | Week 6 |
| **Total** | **6 weeks** | | |

---

## Resource Requirements

- **Development Team**: 2-3 Java developers
- **QA/Testing**: 1 QA engineer
- **DevOps**: 0.5 DevOps engineer
- **Database Admin**: 0.5 DBA (part-time)

---

## Next Immediate Actions

1. **Create DAO implementations** (Task 1.1) - Start with CarDetailsDAOImpl
2. **Write unit tests** for DAO implementations (Task 1.2)
3. **Code review** DAO implementations before moving to services
4. **Create service implementations** (Task 2.1)
5. **Write integration tests** (Task 5.1)

---

**Roadmap Version**: 1.0
**Last Updated**: March 2, 2026
**Next Review**: After Phase 2 completion
