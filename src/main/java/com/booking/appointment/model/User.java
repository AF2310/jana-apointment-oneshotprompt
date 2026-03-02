package com.booking.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * User Entity - Represents a customer who can book appointments
 * 
 * This class models a user in the appointment booking system.
 * Users can have multiple appointments and car details associated with them.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Get full name of the user
     * @return concatenated first and last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
