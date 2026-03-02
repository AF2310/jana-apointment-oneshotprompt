package com.booking.appointment.dao;

import com.booking.appointment.model.BreakTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * BreakTime DAO Interface - Data access operations for BreakTime entity
 * 
 * Provides operations for managing break periods during working hours.
 */
public interface BreakTimeDAO extends BaseDAO<BreakTime, Integer> {
    
    /**
     * Get all breaks for a specific day
     * @param dayOfWeek 0=Sunday to 6=Saturday (or null for all days)
     * @return list of breaks for that day
     */
    List<BreakTime> getBreaksForDay(Integer dayOfWeek);

    /**
     * Get all active breaks
     * @return list of all active break times
     */
    List<BreakTime> getAllActiveBreaks();

    /**
     * Check if time is within a break period for a day
     * @param dayOfWeek 0=Sunday to 6=Saturday
     * @param time the time to check
     * @return true if time falls within a break
     */
    boolean isBreakTime(Integer dayOfWeek, LocalTime time);

    /**
     * Get break times that apply to all working days
     * @return list of global break times
     */
    List<BreakTime> getGlobalBreakTimes();

    /**
     * Add a new break time
     * @param breakTime the break time configuration
     * @return the inserted break time
     */
    BreakTime addBreak(BreakTime breakTime);

    /**
     * Remove a break time
     * @param breakId the break ID
     * @return true if removal was successful
     */
    boolean removeBreak(Integer breakId);
}
