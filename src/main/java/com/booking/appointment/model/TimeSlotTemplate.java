package com.booking.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * TimeSlotTemplate Entity - Predefined time slots for appointments
 * 
 * Defines available time slots based on the configured slot duration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotTemplate {
    private Integer slotId;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Get formatted display string
     * @return time slot in format "HH:MM - HH:MM"
     */
    public String getDisplayText() {
        return String.format("%02d:%02d - %02d:%02d", 
            startTime.getHour(), startTime.getMinute(),
            endTime.getHour(), endTime.getMinute());
    }
}
