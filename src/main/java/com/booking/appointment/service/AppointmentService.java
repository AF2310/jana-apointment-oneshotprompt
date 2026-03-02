package com.booking.appointment.service;

import com.booking.appointment.model.Appointment;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Appointment Service Interface - Business logic for appointment operations
 * 
 * Handles appointment scheduling, availability checking, and status management.
 */
public interface AppointmentService {
    
    /**
     * Book a new appointment
     * @param appointment the appointment to book
     * @return the booked appointment with ID
     */
    Appointment bookAppointment(Appointment appointment);

    /**
     * Update an existing appointment
     * @param appointment the appointment to update
     * @return the updated appointment
     */
    Appointment updateAppointment(Appointment appointment);

    /**
     * Cancel an appointment
     * @param appointmentId the appointment ID
     * @return true if cancellation was successful
     */
    boolean cancelAppointment(Integer appointmentId);

    /**
     * Get appointment by ID
     * @param appointmentId the appointment ID
     * @return Optional containing the appointment
     */
    Optional<Appointment> getAppointmentById(Integer appointmentId);

    /**
     * Get all appointments for a user
     * @param userId the user ID
     * @return list of user's appointments
     */
    List<Appointment> getUserAppointments(Integer userId);

    /**
     * Get upcoming appointments for a user
     * @param userId the user ID
     * @return list of upcoming appointments
     */
    List<Appointment> getUpcomingAppointments(Integer userId);

    /**
     * Get appointments for a specific date
     * @param date the date
     * @return list of appointments on that date
     */
    List<Appointment> getAppointmentsByDate(LocalDate date);

    /**
     * Get appointments in a date range (for admin calendar)
     * @param startDate range start
     * @param endDate range end
     * @return list of appointments in range
     */
    List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get available time slots for a date
     * @param date the date to check
     * @return list of available time slots
     */
    List<String> getAvailableTimeSlots(LocalDate date);

    /**
     * Check if a specific time slot is available
     * @param date the date
     * @param time the time in HH:MM:SS format
     * @return true if slot is available
     */
    boolean isTimeSlotAvailable(LocalDate date, String time);

    /**
     * Get all appointments
     * @return list of all appointments
     */
    List<Appointment> getAllAppointments();

    /**
     * Delete an appointment
     * @param appointmentId the appointment ID
     * @return true if deletion was successful
     */
    boolean deleteAppointment(Integer appointmentId);

    /**
     * Update appointment status
     * @param appointmentId the appointment ID
     * @param statusName the new status name
     * @return true if update was successful
     */
    boolean updateAppointmentStatus(Integer appointmentId, String statusName);

    /**
     * Get appointment count for a specific date
     * @param date the date
     * @return number of appointments
     */
    int getAppointmentCountForDate(LocalDate date);

    /**
     * Get appointment capacity for a day
     * @param date the date
     * @return maximum appointments allowed for the day
     */
    int getDayCapacity(LocalDate date);

    /**
     * Check if a date is available for booking
     * @param date the date to check
     * @return true if date is available (not holiday, working day has capacity)
     */
    boolean isDateAvailable(LocalDate date);
}
