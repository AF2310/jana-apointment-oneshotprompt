package com.booking.appointment.dao;

import com.booking.appointment.model.WorkingHours;
import java.util.Optional;

/**
 * WorkingHours DAO Interface - Data access operations for WorkingHours entity
 * 
 * Provides operations for managing business working hours.
 */
public interface WorkingHoursDAO extends BaseDAO<WorkingHours, Integer> {
    
    /**
     * Find working hours by day of week
     * @param dayOfWeek 0=Sunday to 6=Saturday
     * @return Optional containing the working hours
     */
    Optional<WorkingHours> findByDayOfWeek(Integer dayOfWeek);

    /**
     * Check if day is a working day
     * @param dayOfWeek 0=Sunday to 6=Saturday
     * @return true if it's a working day
     */
    boolean isWorkingDay(Integer dayOfWeek);

    /**
     * Get all working days
     * @return list of all working days configuration
     */
    java.util.List<WorkingHours> getAllWorkingDays();

    /**
     * Update working hours for a day
     * @param dayOfWeek 0=Sunday to 6=Saturday
     * @param startTime start time in "HH:MM:SS" format
     * @param endTime end time in "HH:MM:SS" format
     * @return true if update was successful
     */
    boolean updateDayWorkingHours(Integer dayOfWeek, String startTime, String endTime);
}
