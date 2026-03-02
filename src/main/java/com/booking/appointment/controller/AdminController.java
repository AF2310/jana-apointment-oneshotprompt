package com.booking.appointment.controller;

import com.booking.appointment.model.AdminUser;
import com.booking.appointment.model.Appointment;
import com.booking.appointment.service.*;
import com.booking.appointment.util.ApiResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Admin Controller - Handles administrative operations
 * 
 * REST API endpoints for admin login, appointment management, and system settings.
 */
public class AdminController {
    
    private final AdminService adminService;
    private final AppointmentService appointmentService;
    private final AppSettingService appSettingService;
    private final HolidayService holidayService;

    public AdminController(AdminService adminService,
                         AppointmentService appointmentService,
                         AppSettingService appSettingService,
                         HolidayService holidayService) {
        this.adminService = adminService;
        this.appointmentService = appointmentService;
        this.appSettingService = appSettingService;
        this.holidayService = holidayService;
    }

    /**
     * Admin login
     * @param username admin username
     * @param password admin password
     * @return API response with admin info if successful
     */
    public ApiResponse<AdminUser> login(String username, String password) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return ApiResponse.error("Username is required");
            }
            if (password == null || password.trim().isEmpty()) {
                return ApiResponse.error("Password is required");
            }
            
            Optional<AdminUser> admin = adminService.authenticateAdmin(username, password);
            if (admin.isPresent()) {
                adminService.recordLastLogin(admin.get().getAdminId());
                return ApiResponse.success("Login successful", admin.get());
            } else {
                return ApiResponse.error("Invalid username or password", 401);
            }
        } catch (Exception e) {
            return ApiResponse.error("Error during login: " + e.getMessage());
        }
    }

    /**
     * Get all appointments for admin calendar view
     * @param startDate calendar start date
     * @param endDate calendar end date
     * @return API response with appointments
     */
    public ApiResponse<List<Appointment>> getAppointmentsForCalendar(LocalDate startDate, LocalDate endDate) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByDateRange(startDate, endDate);
            return ApiResponse.success("Appointments retrieved", appointments);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving appointments: " + e.getMessage());
        }
    }

    /**
     * Get all appointments (list view)
     * @return API response with all appointments
     */
    public ApiResponse<List<Appointment>> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            return ApiResponse.success("All appointments retrieved", appointments);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving appointments: " + e.getMessage());
        }
    }

    /**
     * Update appointment status
     * @param appointmentId the appointment ID
     * @param statusName new status name
     * @return API response indicating success
     */
    public ApiResponse<String> updateAppointmentStatus(Integer appointmentId, String statusName) {
        try {
            boolean success = appointmentService.updateAppointmentStatus(appointmentId, statusName);
            if (success) {
                return ApiResponse.success("Status updated successfully", "");
            } else {
                return ApiResponse.error("Failed to update appointment status");
            }
        } catch (Exception e) {
            return ApiResponse.error("Error updating status: " + e.getMessage());
        }
    }

    /**
     * Delete an appointment
     * @param appointmentId the appointment ID
     * @return API response indicating success
     */
    public ApiResponse<String> deleteAppointment(Integer appointmentId) {
        try {
            boolean success = appointmentService.deleteAppointment(appointmentId);
            if (success) {
                return ApiResponse.success("Appointment deleted successfully", "");
            } else {
                return ApiResponse.error("Failed to delete appointment");
            }
        } catch (Exception e) {
            return ApiResponse.error("Error deleting appointment: " + e.getMessage());
        }
    }

    /**
     * Update application settings
     * @param settingKey the setting key
     * @param settingValue the new value
     * @return API response indicating success
     */
    public ApiResponse<String> updateSetting(String settingKey, String settingValue) {
        try {
            boolean success = appSettingService.updateSetting(settingKey, settingValue);
            if (success) {
                return ApiResponse.success("Setting updated successfully", "");
            } else {
                return ApiResponse.error("Failed to update setting");
            }
        } catch (Exception e) {
            return ApiResponse.error("Error updating setting: " + e.getMessage());
        }
    }

    /**
     * Get all configurable settings
     * @return API response with settings
     */
    public ApiResponse<Object> getConfigurableSettings() {
        try {
            Object settings = appSettingService.getConfigurableSettings();
            return ApiResponse.success("Settings retrieved", settings);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving settings: " + e.getMessage());
        }
    }

    /**
     * Get dashboard statistics
     * @return API response with statistics
     */
    public ApiResponse<Object> getDashboardStats() {
        try {
            int totalAppointments = (int) appointmentService.getAllAppointments().size();
            int todayAppointments = appointmentService.getAppointmentsByDate(LocalDate.now()).size();
            
            Object stats = new Object() {
                public final int totalAppointments() { return totalAppointments; }
                public final int todayAppointments() { return todayAppointments; }
            };
            
            return ApiResponse.success("Dashboard statistics retrieved", stats);
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving statistics: " + e.getMessage());
        }
    }
}
