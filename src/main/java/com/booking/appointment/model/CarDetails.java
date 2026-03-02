package com.booking.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * CarDetails Entity - Represents vehicle information for an appointment
 * 
 * This class stores car/vehicle information associated with a user.
 * Multiple cars can be associated with a single user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDetails {
    private Integer carId;
    private Integer userId;
    private String make;
    private String model;
    private Integer year;
    private String color;
    private String licensePlate;
    private String vin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Get vehicle display name
     * @return formatted string with make, model, and year
     */
    public String getVehicleDisplay() {
        return String.format("%d %s %s", year, make, model);
    }
}
