package com.booking.appointment.dao;

import com.booking.appointment.model.Appointment;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Appointment DAO Interface - Data access operations for Appointment entity
 * 
 * Provides CRUD operations and specialized queries for appointments.
 */
public interface AppointmentDAO extends BaseDAO<Appointment, Integer> {
    
    /**
     * Find appointments by user ID
     * @param userId the user identifier
     * @return list of user's appointments
     */
    List<Appointment> findByUserId(Integer userId);

    /**
     * Find appointments by date
     * @param date the appointment date
     * @return list of appointments on that date
     */
    List<Appointment> findByDate(LocalDate date);

    /**
     * Find appointments within date range
     * @param startDate range start date
     * @param endDate range end date
     * @return list of appointments in the range
     */
    List<Appointment> findByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Find appointments by status
     * @param statusId the status identifier
     * @return list of appointments with that status
     */
    List<Appointment> findByStatus(Integer statusId);

    /**
     * Find appointments by car ID
     * @param carId the car identifier
     * @return list of appointments for the car
     */
    List<Appointment> findByCarId(Integer carId);

    /**
     * Check if time slot is available
     * @param date the appointment date
     * @param time the appointment time in "HH:MM:SS" format
     * @return true if slot is available
     */
    boolean isTimeSlotAvailable(LocalDate date, String time);

    /**
     * Get appointments for admin calendar view
     * @param startDate range start date
     * @param endDate range end date
     * @return list of appointments in full details
     */
    List<Appointment> getCalendarAppointments(LocalDate startDate, LocalDate endDate);

    /**
     * Get upcoming appointments for a user
     * @param userId the user identifier
     * @param limit maximum number to return
     * @return list of upcoming appointments
     */
    List<Appointment> getUpcomingAppointments(Integer userId, int limit);

    /**
     * Count appointments on a specific date
     * @param date the date to check
     * @return number of appointments on that date
     */
    int countAppointmentsOnDate(LocalDate date);
}
