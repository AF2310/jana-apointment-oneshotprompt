package com.booking.appointment.dao;

import com.booking.appointment.model.TimeSlotTemplate;
import java.util.List;

/**
 * TimeSlotTemplate DAO Interface - Data access operations for TimeSlotTemplate entity
 * 
 * Provides operations for managing available appointment time slots.
 */
public interface TimeSlotTemplateDAO extends BaseDAO<TimeSlotTemplate, Integer> {
    
    /**
     * Get all active time slots
     * @return list of all active time slots
     */
    List<TimeSlotTemplate> getAllActiveSlots();

    /**
     * Get all time slots sorted by start time
     * @return sorted list of all time slots
     */
    List<TimeSlotTemplate> getAllSlotsSorted();

    /**
     * Get number of time slots per day (based on configuration)
     * @return total active time slots
     */
    int getTotalActiveSlots();

    /**
     * Regenerate time slots based on slot duration
     * @param slotDurationMinutes duration of each slot in minutes
     * @param startTime working start time in "HH:MM:SS" format
     * @param endTime working end time in "HH:MM:SS" format
     * @return number of time slots created
     */
    int regenerateTimeSlots(int slotDurationMinutes, String startTime, String endTime);

    /**
     * Delete all time slots
     * @return number of slots deleted
     */
    int deleteAllSlots();
}
