package com.booking.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * AdminUser Entity - Represents an administrator user
 * 
 * Manages admin accounts with credentials, permissions, and audit trail.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {
    private Integer adminId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private LocalDateTime lastLogin;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
