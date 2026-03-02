package com.booking.appointment.dao;

import com.booking.appointment.model.Holiday;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Holiday DAO Interface - Data access operations for Holiday entity
 * 
 * Provides operations for managing company holidays and closures.
 */
public interface HolidayDAO extends BaseDAO<Holiday, Integer> {
    
    /**
     * Find holiday by date
     * @param date the date to check
     * @return Optional containing the holiday if found
     */
    Optional<Holiday> findByDate(LocalDate date);

    /**
     * Get holidays in a date range
     * @param startDate range start date
     * @param endDate range end date
     * @return list of holidays in the range
     */
    List<Holiday> findByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Check if date is a holiday
     * @param date the date to check
     * @return true if date is a holiday
     */
    boolean isHoliday(LocalDate date);

    /**
     * Check if date is an active holiday
     * @param date the date to check
     * @return true if date is an active holiday
     */
    boolean isActiveHoliday(LocalDate date);

    /**
     * Get all active holidays
     * @return list of all active holidays
     */
    List<Holiday> getAllActiveHolidays();

    /**
     * Delete holiday by date
     * @param date the holiday date
     * @return true if deletion was successful
     */
    boolean deleteByDate(LocalDate date);
}
