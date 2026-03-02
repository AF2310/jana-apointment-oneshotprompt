package com.booking.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Appointment Booking System - Main Spring Boot Application
 * 
 * Entry point for the appointment booking system.
 * Starts the embedded Tomcat server and initializes Spring context.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.booking.appointment"})
public class AppointmentBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointmentBookingApplication.class, args);
        System.out.println("========================================");
        System.out.println("Appointment Booking System Started");
        System.out.println("Server running on http://localhost:8080");
        System.out.println("========================================");
    }
}
