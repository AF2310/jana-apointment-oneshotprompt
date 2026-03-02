package com.booking.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * WorkingHours Entity - Defines business working hours for each day
 * 
 * Manages daily operating hours. day_of_week: 0=Sunday to 6=Saturday
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHours {
    private Integer workingHourId;
    private Integer dayOfWeek; // 0=Sunday to 6=Saturday
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isWorkingDay;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Get day name
     * @return name of the day
     */
    public String getDayName() {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return days[dayOfWeek];
    }
}
