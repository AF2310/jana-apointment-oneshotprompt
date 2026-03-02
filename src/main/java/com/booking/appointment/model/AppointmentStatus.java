package com.booking.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * AppointmentStatus Entity - Master data for appointment statuses
 * 
 * Defines the various states an appointment can have.
 * Extensible for future status additions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatus {
    private Integer statusId;
    private String statusName;
    private String statusDescription;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;

    /**
     * Predefined status constants
     */
    public static final String BOOKED = "BOOKED";
    public static final String COMPLETED = "COMPLETED";
    public static final String CANCELED = "CANCELED";
    public static final String NO_SHOW = "NO_SHOW";
    public static final String PENDING = "PENDING";
    public static final String RESCHEDULED = "RESCHEDULED";
}
