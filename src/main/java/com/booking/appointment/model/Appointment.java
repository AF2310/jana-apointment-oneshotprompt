package com.booking.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Appointment Entity - Core class representing an appointment booking
 * 
 * Manages appointment details including date, time, user info, car info, and status.
 * Tracks creation and modification timestamps.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private Integer appointmentId;
    private Integer userId;
    private Integer carId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalTime endTime;
    private Integer statusId;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Non-database fields for display/business logic
    private String statusName;
    private User user;
    private CarDetails carDetails;

    /**
     * Check if appointment is in the future
     * @return true if appointment is yet to occur
     */
    public boolean isFutureAppointment() {
        return appointmentDate.isAfter(LocalDate.now()) || 
               (appointmentDate.isEqual(LocalDate.now()) && 
                appointmentTime.isAfter(LocalTime.now()));
    }

    /**
     * Check if appointment can be modified (not completed or too old)
     * @return true if appointment can be edited
     */
    public boolean isEditable() {
        return isFutureAppointment() && !statusName.equals(AppointmentStatus.COMPLETED);
    }
}
