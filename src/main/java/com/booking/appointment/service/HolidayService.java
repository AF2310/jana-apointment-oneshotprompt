package com.booking.appointment.service;

import com.booking.appointment.model.Holiday;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Holiday Service Interface - Business logic for holiday management
 * 
 * Handles company holidays and business closure dates.
 */
public interface HolidayService {
    
    /**
     * Add a holiday
     * @param holiday the holiday to add
     * @return the added holiday with ID
     */
    Holiday addHoliday(Holiday holiday);

    /**
     * Get holiday by ID
     * @param holidayId the holiday ID
     * @return Optional containing the holiday
     */
    Optional<Holiday> getHolidayById(Integer holidayId);

    /**
     * Get holiday by date
     * @param date the date
     * @return Optional containing the holiday if date is a holiday
     */
    Optional<Holiday> getHolidayByDate(LocalDate date);

    /**
     * Get all holidays
     * @return list of all holidays
     */
    List<Holiday> getAllHolidays();

    /**
     * Get holidays in date range
     * @param startDate range start
     * @param endDate range end
     * @return list of holidays in range
     */
    List<Holiday> getHolidaysByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Update a holiday
     * @param holiday the holiday to update
     * @return the updated holiday
     */
    Holiday updateHoliday(Holiday holiday);

    /**
     * Delete a holiday
     * @param holidayId the holiday ID
     * @return true if deletion was successful
     */
    boolean deleteHoliday(Integer holidayId);

    /**
     * Check if a date is a holiday
     * @param date the date to check
     * @return true if date is a holiday
     */
    boolean isHoliday(LocalDate date);

    /**
     * Check if a date is an active holiday
     * @param date the date to check
     * @return true if date is active holiday
     */
    boolean isActiveHoliday(LocalDate date);

    /**
     * Get next upcoming holiday
     * @return Optional containing the next holiday
     */
    Optional<Holiday> getNextHoliday();

    /**
     * Deactivate a holiday
     * @param holidayId the holiday ID
     * @return true if deactivation was successful
     */
    boolean deactivateHoliday(Integer holidayId);
}
