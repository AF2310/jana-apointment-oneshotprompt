# Appointment Booking System - Advanced Configuration Guide

## Table of Contents
1. [Database Tuning](#database-tuning)
2. [Application Configuration](#application-configuration)
3. [Security Configuration](#security-configuration)
4. [Performance Optimization](#performance-optimization)
5. [Clustering & High Availability](#clustering--high-availability)
6. [API Gateway Integration](#api-gateway-integration)
7. [Monitoring & Logging](#monitoring--logging)
8. [Disaster Recovery](#disaster-recovery)

---

## Database Tuning

### MySQL Connection Pool Configuration

#### HikariCP Configuration (application.properties)
```properties
# Connection Pool Settings
spring.datasource.hikari.maximumPoolSize=20
spring.datasource.hikari.minimumIdle=5
spring.datasource.hikari.idleTimeout=300000
spring.datasource.hikari.maxLifetime=1200000
spring.datasource.hikari.connectionTimeout=30000
spring.datasource.hikari.leakDetectionThreshold=60000

# Test Queries
spring.datasource.hikari.connection-test-query=SELECT 1
spring.datasource.hikari.auto-commit=true
```

#### MySQL Server Configuration (my.cnf)
```ini
[mysqld]
# Connection Settings
max_connections=500
max_user_connections=100
connect_timeout=10
wait_timeout=28800
interactive_timeout=28800

# InnoDB Settings
innodb_buffer_pool_size=256M
innodb_log_file_size=100M
innodb_flush_log_at_trx_commit=1
innodb_flush_method=O_DIRECT

# Optimization
query_cache_size=64M
query_cache_type=1
slow_query_log=1
long_query_time=2

# Performance Schema
performance_schema=ON
```

### Index Optimization

#### Create Additional Indexes
```sql
-- Email queries optimization
CREATE INDEX idx_users_email ON users(email) USING BTREE;

-- Appointment status queries
CREATE INDEX idx_appointments_status ON appointments(status_id);

-- Date range queries
CREATE INDEX idx_appointments_date_time ON appointments(appointment_date, appointment_time);

-- User appointments query
CREATE INDEX idx_appointments_user_date ON appointments(user_id, appointment_date);

-- Admin user queries
CREATE INDEX idx_admin_username ON admin_users(username);

-- Holiday date queries
CREATE INDEX idx_holidays_date ON holidays(holiday_date);

-- Working hours queries
CREATE INDEX idx_working_hours_day ON working_hours(day_of_week);
```

#### Monitor Index Usage
```sql
-- Check index statistics
SELECT * FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'appointment_booking_db'
ORDER BY TABLE_NAME, SEQ_IN_INDEX;

-- Find unused indexes
SELECT object_schema, object_name, count_read, count_write, count_delete, count_insert
FROM performance_schema.table_io_waits_summary_by_index_usage
WHERE object_schema != 'mysql'
ORDER BY count_read ASC;
```

### Query Optimization

#### EXPLAIN Analysis
```sql
-- Analyze slow queries
EXPLAIN SELECT a.* FROM appointments a
WHERE a.appointment_date BETWEEN '2026-03-01' AND '2026-03-31'
AND a.status_id = 1
ORDER BY a.appointment_date ASC;

-- Should show: rows < 100, type != ALL
```

#### Partitioning for Large Tables
```sql
-- Partition appointments by month
ALTER TABLE appointments
PARTITION BY RANGE (YEAR(appointment_date) * 100 + MONTH(appointment_date)) (
    PARTITION p202601 VALUES LESS THAN (202602),
    PARTITION p202602 VALUES LESS THAN (202603),
    PARTITION p202603 VALUES LESS THAN (202604),
    PARTITION p202604 VALUES LESS THAN (202605),
    PARTITION pmax VALUES LESS THAN (MAXVALUE)
);
```

---

## Application Configuration

### Multi-Environment Configuration

#### Development (application-dev.properties)
```properties
server.port=8080
server.servlet.context-path=/
logging.level.root=INFO
logging.level.com.booking.appointment=DEBUG

spring.datasource.url=jdbc:mysql://localhost:3306/appointment_booking_db_dev
spring.datasource.username=dev_user
spring.datasource.password=dev_password

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Development defaults
appointment.default-range-days=60
appointment.default-slot-duration-minutes=30
appointment.max-appointments-per-day=8
```

#### Testing (application-test.properties)
```properties
server.port=8080

spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop

logging.level.root=WARN
logging.level.com.booking.appointment=INFO
```

#### Production (application-prod.properties)
```properties
server.port=8080
server.compression.enabled=true
server.compression.min-response-size=1024
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true

logging.level.root=WARN
logging.level.com.booking.appointment=INFO
logging.file.name=/var/log/appointment-booking/app.log
logging.file.max-size=10MB
logging.file.max-history=30

spring.datasource.url=jdbc:mysql://production-db-host:3306/appointment_booking_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# Production values
appointment.default-range-days=40
appointment.default-slot-duration-minutes=15
appointment.max-appointments-per-day=32
```

### JVM Tuning

#### Startup Parameters
```bash
# Development
java -server -Xmx512m -Xms256m -XX:+UseG1GC -Dpring.profiles.active=dev -jar app.jar

# Production
java -server -Xmx2g -Xms1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 \
  -XX:+PrintGCDetails -XX:+PrintGCDateStamps \
  -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M \
  -XX:GCLogFileSize=100M \
  -Djava.awt.headless=true \
  -Dspring.profiles.active=prod \
  -jar app.jar
```

#### GC Tuning
```properties
# G1GC settings
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:InitiatingHeapOccupancyPercent=35
-XX:G1NewCollectionHeuristicWeight=20

# Young generation sizing
-XX:G1SummarizeRSetStatsPeriod=86400
```

---

## Security Configuration

### HTTPS/SSL Configuration

#### Generate SSL Certificate
```bash
# Generate self-signed certificate
keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 \
  -keystore keystore.p12 -validity 365 \
  -dname "CN=localhost, OU=IT, O=Company, L=City, ST=State, C=Country" \
  -storepass changeit
```

#### Enable HTTPS
```properties
# application-prod.properties
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=changeit
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=tomcat
server.ssl.enabled=true
```

### CORS Configuration

#### Allow Specific Origins
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("https://example.com", "https://app.example.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
```

### Rate Limiting

#### Implement Rate Limit Filter
```java
@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    private final RateLimiter rateLimiter = RateLimiter.create(100); // 100 req/sec
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain)
            throws ServletException, IOException {
        if (!rateLimiter.tryAcquire()) {
            response.setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
            response.getWriter().write("Rate limit exceeded");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
```

### Password Policy

#### Password Validation Enhanced
```java
public class PasswordPolicyValidator {
    private static final Pattern STRONG_PASSWORD = 
        Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,}$");
    
    public static boolean isStrongPassword(String password) {
        return STRONG_PASSWORD.matcher(password).matches();
    }
}
```

### SQL Injection Prevention

#### Use Parameterized Queries
```java
// SAFE
String sql = "SELECT * FROM users WHERE email = ?";
PreparedStatement stmt = connection.prepareStatement(sql);
stmt.setString(1, email);

// UNSAFE (Never do this)
String sql = "SELECT * FROM users WHERE email = '" + email + "'";
```

---

## Performance Optimization

### Caching Strategy

#### Implement Redis Caching
```properties
# application.properties
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=
spring.redis.timeout=2000
```

#### Cache Common Queries
```java
@Service
public class AppointmentService {
    @Cacheable(value = "availableTimeSlots", key = "#date")
    public List<TimeSlotTemplate> getAvailableTimeSlots(LocalDate date) {
        // This result will be cached
        return appointmentDAO.getTimeSlots(date);
    }
    
    @CacheEvict(value = "availableTimeSlots", key = "#date")
    public void updateTimeSlots(LocalDate date) {
        // Cache is invalidated
    }
}
```

### Connection Pooling Optimization

#### Tomcat Connection Pool
```properties
# Servlet container thread pool
server.tomcat.threads.max=200
server.tomcat.threads.min-spare=10
server.tomcat.max-connections=10000
server.tomcat.accept-count=100
```

### Lazy Loading Configuration
```properties
spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

---

## Clustering & High Availability

### Database Replication Setup

#### Master-Slave Configuration
```sql
-- On Master
CHANGE MASTER TO
MASTER_HOST='slave-server',
MASTER_USER='replication_user',
MASTER_PASSWORD='password',
MASTER_LOG_FILE='mysql-bin.000001',
MASTER_LOG_POS=154;

START SLAVE;

-- Verify replication
SHOW SLAVE STATUS\G
```

### Session Clustering

#### Sticky Sessions Configuration
```java
@Configuration
public class SessionConfig {
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }
}
```

#### application.properties
```properties
spring.session.store-type=redis
spring.session.redis.namespace=spring:session
server.servlet.session.cookie.name=APPOINTMENTID
server.servlet.session.timeout=1800
server.servlet.session.cookie.path=/
server.servlet.session.cookie.domain=example.com
```

### Load Balancing

#### Nginx Configuration
```nginx
upstream appointment_app {
    least_conn;  # Load balancing algorithm
    server app1.local:8080;
    server app2.local:8080;
    server app3.local:8080;
}

server {
    listen 80;
    server_name booking.example.com;
    
    location / {
        proxy_pass http://appointment_app;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }
}
```

---

## API Gateway Integration

### API Gateway Configuration

#### Kong Integration
```yaml
# kong.yml
services:
  - name: appointment-booking
    host: appointment-app.local
    port: 8080
    protocol: http
    
plugins:
  - name: http-log
    config:
      http_endpoint: http://logging-server:8000/logs
  
  - name: rate-limiting
    config:
      minute: 1000
      hour: 10000
  
  - name: auth-bearer
    config:
      hide_credentials: true
```

### API Versioning

#### Implement Version Strategy
```java
@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentControllerV1 {
    // Version 1 endpoints
}

@RestController
@RequestMapping("/api/v2/appointments")
public class AppointmentControllerV2 {
    // Version 2 endpoints with enhancements
}
```

---

## Monitoring & Logging

### Structured Logging

#### Logback Configuration
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property name="LOG_FILE_PATH" value="/var/log/appointment-booking"/>
    
    <appender name="ASYNC_FILE" class="ch.qos.logback.classic.AsyncAppender">
        <queueSize>512</queueSize>
        <appender-ref ref="FILE"/>
    </appender>
    
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_FILE_PATH}/app.log</file>
        <encoder>
            <pattern>
                %d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
            </pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_FILE_PATH}/app.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>100MB</maxFileSize>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="ASYNC_FILE"/>
    </root>
</configuration>
```

### Centralized Logging

#### ELK Stack Integration
```properties
# Logback appender for Elasticsearch
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# Application properties
logging.elasticsearch.enabled=true
logging.elasticsearch.url=http://elasticsearch:9200
```

### Metrics Collection

#### Micrometer Configuration
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      application: appointment-booking
      environment: prod
```

### Health Checks

#### Custom Health Indicator
```java
@Component
public class AppointmentBookingHealthIndicator extends AbstractHealthIndicator {
    @Autowired
    private AppointmentDAO appointmentDAO;
    
    @Override
    protected void doHealthCheck(Health.Builder builder) {
        try {
            long count = appointmentDAO.count();
            builder.up()
                .withDetail("appointment_count", count)
                .withDetail("status", "Appointment system operational");
        } catch (Exception e) {
            builder.down()
                .withDetail("error", e.getMessage());
        }
    }
}
```

---

## Disaster Recovery

### Backup Strategy

#### Automated Backup
```bash
#!/bin/bash
# backup-strategy.sh

BACKUP_ROOT="/backups/appointment-booking"
DATE=$(date +%Y%m%d_%H%M%S)
WEEK=$(date +%u)
MONTH=$(date +%d)

# Daily backup
DAILY_BACKUP="$BACKUP_ROOT/daily/backup_$DATE.sql"
mkdir -p "$BACKUP_ROOT/daily"
mysqldump -u root -p appointment_booking_db > "$DAILY_BACKUP"

# Weekly backup (Sunday)
if [ "$WEEK" == "7" ]; then
    WEEKLY_BACKUP="$BACKUP_ROOT/weekly/backup_week_$DATE.sql"
    mkdir -p "$BACKUP_ROOT/weekly"
    cp "$DAILY_BACKUP" "$WEEKLY_BACKUP"
fi

# Monthly backup (1st of month)
if [ "$MONTH" == "01" ]; then
    MONTHLY_BACKUP="$BACKUP_ROOT/monthly/backup_month_$DATE.sql"
    mkdir -p "$BACKUP_ROOT/monthly"
    cp "$DAILY_BACKUP" "$MONTHLY_BACKUP"
fi

# Cleanup old backups
find "$BACKUP_ROOT/daily" -mtime +7 -delete
find "$BACKUP_ROOT/weekly" -mtime +30 -delete
find "$BACKUP_ROOT/monthly" -mtime +365 -delete
```

### Recovery Procedures

#### Point-in-Time Recovery
```bash
# Get binary log position before failure
mysql-bin-log-parser --input-file=/var/lib/mysql/mysql-bin.000123 \
    --start-datetime='2026-03-02 10:00:00' \
    --stop-datetime='2026-03-02 15:00:00'

# Restore from backup and replay logs
mysql -u root -p appointment_booking_db < backup.sql
mysqlbinlog --start-datetime='2026-03-02 14:30:00' \
    mysql-bin.000123 mysql-bin.000124 | mysql -u root -p
```

### RTO/RPO Targets
- **Recovery Time Objective (RTO)**: < 1 hour
- **Recovery Point Objective (RPO)**: < 15 minutes (incremental backups every 15 min)

---

**Configuration Guide Version**: 1.0
**Last Updated**: March 2, 2026
