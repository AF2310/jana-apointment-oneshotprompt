package com.booking.appointment.controller;

import com.booking.appointment.model.Appointment;
import com.booking.appointment.model.User;
import com.booking.appointment.model.CarDetails;
import com.booking.appointment.service.AppointmentService;
import com.booking.appointment.service.UserService;
import com.booking.appointment.service.CarDetailsService;
import com.booking.appointment.service.AppSettingService;
import com.booking.appointment.util.ApiResponse;
import com.booking.appointment.util.DateTimeUtil;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Appointment Controller - Handles appointment booking operations for users
 * 
 * REST API endpoints for creating, viewing, and managing user appointments.
 */
public class AppointmentController {
    
    private final AppointmentService appointmentService;
    private final UserService userService;
    private final CarDetailsService carDetailsService;
    private final AppSettingService appSettingService;

    public AppointmentController(AppointmentService appointmentService, 
                                UserService userService,
                                CarDetailsService carDetailsService,
                                AppSettingService appSettingService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.carDetailsService = carDetailsService;
        this.appSettingService = appSettingService;
    }

    /**
     * Get available appointment dates within the configured range
     * @return API response with list of available dates
     */
    public ApiResponse<List<LocalDate>> getAvailableDates() {
        try {
            int rangeDays = appSettingService.getDefaultAppointmentRangeDays();
            LocalDate today = LocalDate.now();
            List<LocalDate> availableDates = new ArrayList<>();
            
            for (int i = 0; i < rangeDays; i++) {
                LocalDate date = today.plusDays(i);
                if (appointmentService.isDateAvailable(date)) {
                    availableDates.add(date);
                }
            }
            
            return ApiResponse.success("Available dates retrieved", availableDates);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving available dates: " + e.getMessage());
        }
    }

    /**
     * Get available time slots for a specific date
     * @param date the date to check
     * @return API response with list of available time slots
     */
    public ApiResponse<List<String>> getAvailableTimeSlots(LocalDate date) {
        try {
            if (DateTimeUtil.isPast(date)) {
                return ApiResponse.error("Cannot book appointments for past dates");
            }
            
            List<String> timeSlots = appointmentService.getAvailableTimeSlots(date);
            return ApiResponse.success("Available time slots retrieved", timeSlots);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving time slots: " + e.getMessage());
        }
    }

    /**
     * Book a new appointment
     * @param firstName user's first name
     * @param lastName user's last name
     * @param email user's email
     * @param licensePlate car license plate
     * @param appointmentDate booking date
     * @param appointmentTime booking time
     * @param notes additional notes
     * @return API response with booked appointment
     */
    public ApiResponse<Appointment> bookAppointment(String firstName, String lastName, String email,
                                                     String licensePlate, LocalDate appointmentDate,
                                                     LocalTime appointmentTime, String notes) {
        try {
            // Validate inputs
            if (firstName == null || firstName.trim().isEmpty()) {
                return ApiResponse.error("First name is required");
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                return ApiResponse.error("Last name is required");
            }
            if (email == null || email.trim().isEmpty()) {
                return ApiResponse.error("Email is required");
            }
            
            // Get or create user
            Optional<User> existingUser = userService.getUserByEmail(email);
            User user;
            if (existingUser.isPresent()) {
                user = existingUser.get();
            } else {
                user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user = userService.registerUser(user);
            }
            
            // Get or create car
            Optional<CarDetails> existingCar = carDetailsService.findByLicensePlate(licensePlate);
            CarDetails car;
            if (existingCar.isPresent()) {
                car = existingCar.get();
            } else {
                return ApiResponse.error("Please register your car first");
            }
            
            // Create appointment
            Appointment appointment = new Appointment();
            appointment.setUserId(user.getUserId());
            appointment.setCarId(car.getCarId());
            appointment.setAppointmentDate(appointmentDate);
            appointment.setAppointmentTime(appointmentTime);
            appointment.setNotes(notes);
            appointment.setStatusId(1); // BOOKED status
            
            // Book appointment
            Appointment bookedAppointment = appointmentService.bookAppointment(appointment);
            
            return ApiResponse.success("Appointment booked successfully", bookedAppointment);
        } catch (Exception e) {
            return ApiResponse.error("Error booking appointment: " + e.getMessage());
        }
    }

    /**
     * Get user's upcoming appointments
     * @param userId the user ID
     * @return API response with list of upcoming appointments
     */
    public ApiResponse<List<Appointment>> getUpcomingAppointments(Integer userId) {
        try {
            List<Appointment> appointments = appointmentService.getUpcomingAppointments(userId);
            return ApiResponse.success("Upcoming appointments retrieved", appointments);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving appointments: " + e.getMessage());
        }
    }

    /**
     * Get user's appointment history
     * @param userId the user ID
     * @return API response with list of past appointments
     */
    public ApiResponse<List<Appointment>> getAppointmentHistory(Integer userId) {
        try {
            List<Appointment> allAppointments = appointmentService.getUserAppointments(userId);
            // Filter to only past appointments
            List<Appointment> history = new ArrayList<>();
            for (Appointment apt : allAppointments) {
                if (!apt.isFutureAppointment()) {
                    history.add(apt);
                }
            }
            return ApiResponse.success("Appointment history retrieved", history);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving appointment history: " + e.getMessage());
        }
    }

    /**
     * Update an appointment
     * @param appointmentId the appointment ID
     * @param appointmentDate new date
     * @param appointmentTime new time
     * @param notes new notes
     * @return API response with updated appointment
     */
    public ApiResponse<Appointment> updateAppointment(Integer appointmentId, LocalDate appointmentDate,
                                                       LocalTime appointmentTime, String notes) {
        try {
            Optional<Appointment> existing = appointmentService.getAppointmentById(appointmentId);
            if (!existing.isPresent()) {
                return ApiResponse.error("Appointment not found", 404);
            }
            
            Appointment appointment = existing.get();
            if (!appointment.isEditable()) {
                return ApiResponse.error("This appointment cannot be modified");
            }
            
            appointment.setAppointmentDate(appointmentDate);
            appointment.setAppointmentTime(appointmentTime);
            appointment.setNotes(notes);
            
            Appointment updated = appointmentService.updateAppointment(appointment);
            return ApiResponse.success("Appointment updated successfully", updated);
        } catch (Exception e) {
            return ApiResponse.error("Error updating appointment: " + e.getMessage());
        }
    }

    /**
     * Cancel an appointment
     * @param appointmentId the appointment ID
     * @return API response indicating success
     */
    public ApiResponse<String> cancelAppointment(Integer appointmentId) {
        try {
            boolean success = appointmentService.cancelAppointment(appointmentId);
            if (success) {
                return ApiResponse.success("Appointment cancelled successfully", "");
            } else {
                return ApiResponse.error("Failed to cancel appointment");
            }
        } catch (Exception e) {
            return ApiResponse.error("Error cancelling appointment: " + e.getMessage());
        }
    }
}
