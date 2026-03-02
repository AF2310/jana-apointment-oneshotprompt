package com.booking.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Holiday Entity - Manages company holidays and closures
 * 
 * Stores dates when the business is closed.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {
    private Integer holidayId;
    private LocalDate holidayDate;
    private String holidayName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
