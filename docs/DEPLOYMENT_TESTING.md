# Appointment Booking System - Deployment & Testing Guide

## Deployment Guide

### Prerequisites Checklist
- [ ] Java 11+ installed
- [ ] MySQL 8.0+ installed and running
- [ ] Maven 3.6+ installed
- [ ] Port 8080 is available
- [ ] Database credentials configured
- [ ] All source code files present

### Production Deployment Steps

#### 1. Environment Preparation
```bash
# Set production environment variables
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
export M2_HOME=/opt/maven
export PATH=$JAVA_HOME/bin:$M2_HOME/bin:$PATH

# Verify Java version
java -version  # Should show Java 11 or higher
```

#### 2. Build Application
```bash
cd AppointmentBooking
mvn clean package -DskipTests
```

#### 3. Database Migration
```bash
# Create database and tables
mysql -u root -p appointment_booking_db < sql/schema.sql

# Verify tables created
mysql -u root -p appointment_booking_db -e "SHOW TABLES;"
```

#### 4. Configure for Production
Edit `src/main/resources/application-prod.properties`:
```properties
server.port=8080
spring.profiles.active=prod

# Update database connection
spring.datasource.url=jdbc:mysql://localhost:3306/appointment_booking_db
spring.datasource.username=prod_user
spring.datasource.password=strong_password_here

# Security settings
security.jwt.secret=your-very-secret-key-change-this
security.jwt.expiration=86400000

# Logging
logging.level.root=WARN
logging.level.com.booking.appointment=INFO
logging.file.name=/var/log/appointment-booking/app.log
```

#### 5. Run Application
```bash
# Using JAR file
java -jar target/appointment-booking-1.0.0.jar --spring.profiles.active=prod

# Using embedded Tomcat with increased memory
java -Xmx512m -Xms256m -jar target/appointment-booking-1.0.0.jar --spring.profiles.active=prod
```

#### 6. Verify Deployment
```bash
# Check application is running
curl http://localhost:8080/
# Should return 200 OK

# Check database connection
curl http://localhost:8080/health
# Should show "UP" status
```

### Docker Deployment (Optional)

#### Create Dockerfile
```dockerfile
FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/appointment-booking-1.0.0.jar app.jar

EXPOSE 8080

CMD ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]
```

#### Build Docker Image
```bash
docker build -t appointment-booking:1.0.0 .
```

#### Run Docker Container
```bash
docker run -d \
  --name appointment-booking \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-host:3306/appointment_booking_db \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  appointment-booking:1.0.0
```

### Cloud Deployment

#### AWS Elastic Beanstalk
```bash
# Create .ebextensions/java.config
[nginx/proxy]
HTTP_UPGRADE = true

# Deploy to Beanstalk
eb deploy
```

#### Azure App Service
```bash
# Create App Service
az appservice plan create --name app-booking-plan --resource-group myResourceGroup --sku B2

# Deploy JAR
az webapp create --resource-group myResourceGroup --name app-booking --plan app-booking-plan
az webapp deployment source config-zip --resource-group myResourceGroup --name app-booking --src target/appointment-booking-1.0.0.jar
```

---

## Testing Guide

### Unit Testing

#### Run Unit Tests
```bash
mvn test
```

#### Example Unit Tests

##### Test User Service
```java
@Test
public void testRegisterUser() {
    User user = new User();
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("john@example.com");
    
    User registered = userService.registerUser(user);
    
    assertNotNull(registered.getUserId());
    assertEquals("john@example.com", registered.getEmail());
}

@Test
public void testDuplicateEmailValidation() {
    User user1 = new User();
    user1.setEmail("duplicate@example.com");
    userService.registerUser(user1);
    
    User user2 = new User();
    user2.setEmail("duplicate@example.com");
    
    assertThrows(Exception.class, () -> userService.registerUser(user2));
}
```

##### Test Appointment Service
```java
@Test
public void testBookAppointment() {
    Appointment apt = new Appointment();
    apt.setUserId(1);
    apt.setCarId(1);
    apt.setAppointmentDate(LocalDate.now().plusDays(5));
    apt.setAppointmentTime(LocalTime.of(10, 0));
    
    Appointment booked = appointmentService.bookAppointment(apt);
    
    assertNotNull(booked.getAppointmentId());
    assertEquals("BOOKED", booked.getStatusName());
}

@Test
public void testTimeSlotConflict() {
    LocalDate date = LocalDate.now().plusDays(5);
    LocalTime time = LocalTime.of(10, 0);
    
    Appointment apt1 = new Appointment();
    apt1.setAppointmentDate(date);
    apt1.setAppointmentTime(time);
    appointmentService.bookAppointment(apt1);
    
    Appointment apt2 = new Appointment();
    apt2.setAppointmentDate(date);
    apt2.setAppointmentTime(time);
    
    assertThrows(Exception.class, () -> appointmentService.bookAppointment(apt2));
}
```

### Integration Testing

#### Run Integration Tests
```bash
mvn verify
```

#### Database Integration Test
```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AppointmentRepositoryTest {
    
    @Autowired
    private AppointmentDAO appointmentDAO;
    
    @Test
    public void testSaveAndRetrieveAppointment() {
        Appointment apt = new Appointment();
        apt.setUserId(1);
        apt.setCarId(1);
        apt.setAppointmentDate(LocalDate.now().plusDays(5));
        
        Appointment saved = appointmentDAO.save(apt);
        Optional<Appointment> retrieved = appointmentDAO.findById(saved.getAppointmentId());
        
        assertTrue(retrieved.isPresent());
        assertEquals(1, retrieved.get().getUserId());
    }
}
```

### API Testing

#### Using cURL

##### Test Get Available Dates
```bash
curl -X GET http://localhost:8080/api/appointments/available-dates
```

##### Test Book Appointment
```bash
curl -X POST http://localhost:8080/api/appointments/book \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "licensePlate": "ABC123",
    "appointmentDate": "2026-03-10",
    "appointmentTime": "10:00",
    "notes": "Oil change"
  }'
```

##### Test Admin Login
```bash
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

#### Using Postman

1. Import collection (available in docs/postman-collection.json)
2. Set environment variables:
   - `base_url`: http://localhost:8080
   - `admin_token`: (obtained from login response)
3. Run tests by clicking "Send"

### Manual Testing Checklist

#### User Booking Flow
- [ ] User can access booking page
- [ ] All form fields are properly labeled and required
- [ ] Date picker shows correct date range (40 days)
- [ ] Time slots are displayed for selected date
- [ ] Form validates required fields
- [ ] Success message appears after booking
- [ ] Confirmation email is sent
- [ ] User can view upcoming appointments
- [ ] User can cancel or reschedule appointment
- [ ] Invalid data is rejected with error message

#### Admin Panel
- [ ] Admin can login with credentials
- [ ] Dashboard loads with statistics
- [ ] Can view appointments in calendar view
- [ ] Can view appointments in list view
- [ ] Can edit appointment details
- [ ] Can change appointment status
- [ ] Can delete appointments
- [ ] Can update system settings
- [ ] Settings persist after page refresh
- [ ] Can add and manage holidays
- [ ] Can configure working hours
- [ ] Audit logs track all actions

#### Edge Cases
- [ ] Cannot book past dates
- [ ] Cannot book during holidays
- [ ] Cannot book during break times
- [ ] Cannot book after working hours
- [ ] Cannot book when daily limit reached
- [ ] Time slots prevent conflicts
- [ ] Email validation works correctly
- [ ] Special characters handled properly
- [ ] Concurrent bookings handled safely

### Performance Testing

#### Load Testing with Apache JMeter

##### Create Test Plan
1. Add Thread Group (users: 100, ramp-up: 10s)
2. Add HTTP Request Sampler:
   - GET /api/appointments/available-dates
3. Add Listeners:
   - View Results Tree
   - Aggregate Report
   - Graph Results

##### Run Load Test
```bash
jmeter -n -t test-plan.jmx -l results.jtl
```

#### Expected Performance
- Response time < 200ms (p95)
- Throughput > 100 requests/second
- Success rate > 99.5%
- CPU usage < 80%
- Memory usage < 60% of available

### Security Testing

#### SQL Injection Testing
```bash
# Try SQL injection in email field
curl -X POST http://localhost:8080/api/appointments/book \
  -d "email=test@example.com' OR '1'='1"

# Should be rejected with validation error
```

#### Input Validation Testing
```bash
# Test with special characters
Emails: test@<script>alert('xss')</script>.com
Text: '; DROP TABLE users; --

# All should be sanitized or rejected
```

#### Authentication Testing
```bash
# Try accessing admin without login
curl -X GET http://localhost:8080/api/admin/appointments

# Should return 401 Unauthorized
```

### Regression Testing

#### Create Regression Test Suite
```bash
# Run all tests
mvn test -Dtest=*RegressionTest

# Run specific test
mvn test -Dtest=AppointmentRegressionTest
```

#### Key Regression Tests
- Booking flow after code changes
- Admin operations after code changes
- Date/time calculations after code changes
- Permission checks after security updates
- Database queries after schema changes

---

## Monitoring & Maintenance

### Application Monitoring

#### Enable Actuator Endpoints
```properties
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=when-authorized
```

#### Check Health Status
```bash
curl http://localhost:8080/actuator/health
```

#### View Application Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

### Database Monitoring

#### Connection Pool Status
```bash
# Check active connections
SHOW PROCESSLIST;

# Check connection pool size
SELECT COUNT(*) FROM information_schema.PROCESSLIST;
```

#### Query Performance
```bash
# Enable slow query log
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;

# View slow queries
SELECT * FROM mysql.slow_log;
```

#### Table Optimization
```bash
# Check table sizes
SELECT TABLE_NAME, ROUND(((data_length + index_length) / 1024 / 1024), 2) AS size_mb
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'appointment_booking_db';

# Optimize tables
OPTIMIZE TABLE appointments;
OPTIMIZE TABLE users;
```

### Backup & Recovery

#### Automated Backup Script
```bash
#!/bin/bash
# backup.sh

BACKUP_DIR="/backups/appointment-booking"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/backup_$DATE.sql"

mkdir -p $BACKUP_DIR

mysqldump -u root -p appointment_booking_db > $BACKUP_FILE

# Keep only last 30 backups
find $BACKUP_DIR -name "backup_*.sql" -mtime +30 -delete

echo "Backup completed: $BACKUP_FILE"
```

#### Schedule Backup
```bash
# Add to crontab
0 2 * * * /home/user/backup.sh  # Daily at 2 AM
```

#### Restore from Backup
```bash
mysql -u root -p appointment_booking_db < backup_20260302_020000.sql
```

---

## Troubleshooting During Deployment

### Application Won't Start
1. Check logs for errors
2. Verify database connection
3. Check if port is already in use
4. Review application.properties

### Database Connection Fails
1. Verify MySQL is running
2. Check credentials in properties
3. Test connection: `mysql -u root -p -e "SELECT 1"`
4. Check firewall rules

### High Memory Usage
1. Check for connection leaks
2. Monitor thread count
3. Increase heap size: `-Xmx1024m`
4. Profile with JProfiler

### Slow Performance
1. Run database ANALYZE
2. Check query performance
3. Verify indexes exist
4. Monitor CPU and disk I/O

---

**Testing Document Version**: 1.0
**Last Updated**: March 2, 2026
