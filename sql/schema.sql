-- ============================================================================
-- Appointment Booking System Database Schema
-- Database: appointment_booking_db
-- Created: 2026-03-02
-- ============================================================================

-- Create Database
CREATE DATABASE IF NOT EXISTS appointment_booking_db;
USE appointment_booking_db;

-- ============================================================================
-- ADMIN USER TABLE
-- ============================================================================
CREATE TABLE admin_users (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    last_login TIMESTAMP NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- APPOINTMENT STATUS TABLE (Master Data)
-- ============================================================================
CREATE TABLE appointment_statuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(50) NOT NULL UNIQUE,
    status_description VARCHAR(255),
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_status_name (status_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- USERS TABLE
-- ============================================================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY unique_email (email),
    INDEX idx_first_name (first_name),
    INDEX idx_last_name (last_name),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- CAR DETAILS TABLE
-- ============================================================================
CREATE TABLE car_details (
    car_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    color VARCHAR(30),
    license_plate VARCHAR(20),
    vin VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_license_plate (license_plate),
    UNIQUE KEY unique_vin (vin)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- APPOINTMENTS TABLE
-- ============================================================================
CREATE TABLE appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    car_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    end_time TIME,
    status_id INT NOT NULL,
    notes VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (car_id) REFERENCES car_details(car_id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES appointment_statuses(status_id),
    
    INDEX idx_user_id (user_id),
    INDEX idx_car_id (car_id),
    INDEX idx_appointment_date (appointment_date),
    INDEX idx_status_id (status_id),
    INDEX idx_datetime (appointment_date, appointment_time),
    UNIQUE KEY unique_appointment (appointment_date, appointment_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- APP SETTINGS TABLE
-- ============================================================================
CREATE TABLE app_settings (
    setting_id INT PRIMARY KEY AUTO_INCREMENT,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value VARCHAR(500) NOT NULL,
    setting_type VARCHAR(20) NOT NULL, -- INT, FLOAT, STRING, BOOLEAN, JSON
    description VARCHAR(255),
    is_configurable BOOLEAN DEFAULT TRUE,
    updated_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (updated_by) REFERENCES admin_users(admin_id) ON DELETE SET NULL,
    INDEX idx_setting_key (setting_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- TIME SLOT TEMPLATES TABLE
-- ============================================================================
CREATE TABLE time_slot_templates (
    slot_id INT PRIMARY KEY AUTO_INCREMENT,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_start_time (start_time),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- WORKING HOURS TABLE
-- ============================================================================
CREATE TABLE working_hours (
    working_hour_id INT PRIMARY KEY AUTO_INCREMENT,
    day_of_week INT NOT NULL UNIQUE, -- 0=Sunday to 6=Saturday
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_working_day BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_day_of_week (day_of_week)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- BREAK TIMES TABLE
-- ============================================================================
CREATE TABLE break_times (
    break_id INT PRIMARY KEY AUTO_INCREMENT,
    day_of_week INT, -- NULL means all working days, 0-6 for specific days
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    break_type VARCHAR(50), -- LUNCH, BREAK, MAINTENANCE, etc.
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_day_of_week (day_of_week),
    INDEX idx_start_time (start_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- HOLIDAYS TABLE
-- ============================================================================
CREATE TABLE holidays (
    holiday_id INT PRIMARY KEY AUTO_INCREMENT,
    holiday_date DATE NOT NULL UNIQUE,
    holiday_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_holiday_date (holiday_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- AUDIT LOG TABLE
-- ============================================================================
CREATE TABLE audit_logs (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    admin_id INT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    entity_id INT,
    changes_before VARCHAR(1000),
    changes_after VARCHAR(1000),
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (admin_id) REFERENCES admin_users(admin_id) ON DELETE SET NULL,
    INDEX idx_admin_id (admin_id),
    INDEX idx_created_at (created_at),
    INDEX idx_entity (entity_type, entity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- INSERT DEFAULT DATA
-- ============================================================================

-- Insert Default Appointment Statuses
INSERT INTO appointment_statuses (status_name, status_description, display_order, is_active) VALUES
('BOOKED', 'Appointment is booked and confirmed', 1, TRUE),
('COMPLETED', 'Appointment has been completed', 2, TRUE),
('CANCELED', 'Appointment has been canceled', 3, TRUE),
('NO_SHOW', 'Customer did not show up to appointment', 4, TRUE),
('PENDING', 'Appointment is pending confirmation', 5, TRUE),
('RESCHEDULED', 'Appointment has been rescheduled', 6, TRUE);

-- Insert Default Working Hours (Mon-Fri 9 AM - 6 PM, Closed on Weekends)
INSERT INTO working_hours (day_of_week, start_time, end_time, is_working_day) VALUES
(0, '00:00:00', '00:00:00', FALSE), -- Sunday - Holiday
(1, '09:00:00', '18:00:00', TRUE),  -- Monday
(2, '09:00:00', '18:00:00', TRUE),  -- Tuesday
(3, '09:00:00', '18:00:00', TRUE),  -- Wednesday
(4, '09:00:00', '18:00:00', TRUE),  -- Thursday
(5, '09:00:00', '18:00:00', TRUE),  -- Friday
(6, '00:00:00', '00:00:00', FALSE); -- Saturday - Holiday

-- Insert Default Break Times (Lunch break 12-1 PM)
INSERT INTO break_times (day_of_week, start_time, end_time, break_type, is_active) VALUES
(NULL, '12:00:00', '13:00:00', 'LUNCH', TRUE);

-- Insert Default App Settings
INSERT INTO app_settings (setting_key, setting_value, setting_type, description, is_configurable) VALUES
('DEFAULT_APPOINTMENT_RANGE_DAYS', '40', 'INT', 'Default number of days to show available appointments', TRUE),
('DEFAULT_TIME_SLOT_DURATION_MINUTES', '15', 'INT', 'Default duration of each appointment time slot in minutes', TRUE),
('TIMEZONE', 'UTC', 'STRING', 'Application timezone', TRUE),
('APPOINTMENT_CONFIRMATION_EMAIL_ENABLED', 'true', 'BOOLEAN', 'Send confirmation emails for appointments', TRUE),
('MAX_APPOINTMENTS_PER_DAY', '32', 'INT', 'Maximum number of appointments per day (based on 15 min slots)', FALSE),
('ALLOW_SAME_DAY_BOOKING', 'true', 'BOOLEAN', 'Allow booking for same day appointments', TRUE);

-- Insert Default Time Slot Templates
INSERT INTO time_slot_templates (start_time, end_time, is_active) VALUES
('09:00:00', '09:15:00', TRUE),
('09:15:00', '09:30:00', TRUE),
('09:30:00', '09:45:00', TRUE),
('09:45:00', '10:00:00', TRUE),
('10:00:00', '10:15:00', TRUE),
('10:15:00', '10:30:00', TRUE),
('10:30:00', '10:45:00', TRUE),
('10:45:00', '11:00:00', TRUE),
('11:00:00', '11:15:00', TRUE),
('11:15:00', '11:30:00', TRUE),
('11:30:00', '11:45:00', TRUE),
('11:45:00', '12:00:00', TRUE),
-- Note: 12:00-13:00 is lunch break
('13:00:00', '13:15:00', TRUE),
('13:15:00', '13:30:00', TRUE),
('13:30:00', '13:45:00', TRUE),
('13:45:00', '14:00:00', TRUE),
('14:00:00', '14:15:00', TRUE),
('14:15:00', '14:30:00', TRUE),
('14:30:00', '14:45:00', TRUE),
('14:45:00', '15:00:00', TRUE),
('15:00:00', '15:15:00', TRUE),
('15:15:00', '15:30:00', TRUE),
('15:30:00', '15:45:00', TRUE),
('15:45:00', '16:00:00', TRUE),
('16:00:00', '16:15:00', TRUE),
('16:15:00', '16:30:00', TRUE),
('16:30:00', '16:45:00', TRUE),
('16:45:00', '17:00:00', TRUE),
('17:00:00', '17:15:00', TRUE),
('17:15:00', '17:30:00', TRUE),
('17:30:00', '17:45:00', TRUE),
('17:45:00', '18:00:00', TRUE);

-- Insert default admin user (username: admin, password hash pre-generated)
INSERT INTO admin_users (username, password, email, full_name, is_active) VALUES
('admin', '$2a$10$slYQmyNdGzins1d6lJU5C.6JzVmJdYwGhFqpg0sL.aKXn0GrEGbTq', 'admin@example.com', 'System Administrator', TRUE);

-- ============================================================================
-- CREATE VIEWS FOR EASIER QUERYING
-- ============================================================================

-- View for Available Time Slots
CREATE VIEW available_time_slots_view AS
SELECT 
    tst.slot_id,
    tst.start_time,
    tst.end_time,
    TIME_FORMAT(tst.start_time, '%H:%i') as formatted_start,
    TIME_FORMAT(tst.end_time, '%H:%i') as formatted_end,
    tst.is_active
FROM time_slot_templates tst
WHERE tst.is_active = TRUE
ORDER BY tst.start_time;

-- View for Appointments with User Details
CREATE VIEW appointments_detail_view AS
SELECT 
    a.appointment_id,
    a.appointment_date,
    a.appointment_time,
    a.end_time,
    u.user_id,
    u.first_name,
    u.last_name,
    u.email,
    u.phone,
    cd.car_id,
    cd.make,
    cd.model,
    cd.year,
    cd.license_plate,
    s.status_name,
    a.notes,
    a.created_at,
    a.updated_at
FROM appointments a
JOIN users u ON a.user_id = u.user_id
JOIN car_details cd ON a.car_id = cd.car_id
JOIN appointment_statuses s ON a.status_id = s.status_id;

-- ============================================================================
-- END OF SCHEMA CREATION
-- ============================================================================
