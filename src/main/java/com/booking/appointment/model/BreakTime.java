package com.booking.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * BreakTime Entity - Manages break periods during working hours
 * 
 * Defines break times (lunch, maintenance, etc.) for day(s).
 * NULL day_of_week means the break applies to all working days.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreakTime {
    private Integer breakId;
    private Integer dayOfWeek; // NULL for all days, 0-6 for specific days
    private LocalTime startTime;
    private LocalTime endTime;
    private String breakType; // LUNCH, BREAK, MAINTENANCE, etc.
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
