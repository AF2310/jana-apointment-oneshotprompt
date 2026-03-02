package com.booking.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * AppSetting Entity - Configuration settings for the application
 * 
 * Stores key-value pairs for configurable application parameters.
 * Tracks who made changes and when.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppSetting {
    private Integer settingId;
    private String settingKey;
    private String settingValue;
    private String settingType; // INT, FLOAT, STRING, BOOLEAN, JSON
    private String description;
    private Boolean isConfigurable;
    private Integer updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Get setting value as integer
     * @return parsed integer value
     */
    public Integer getAsInteger() {
        return Integer.parseInt(settingValue);
    }

    /**
     * Get setting value as boolean
     * @return parsed boolean value
     */
    public Boolean getAsBoolean() {
        return Boolean.parseBoolean(settingValue);
    }

    /**
     * Get setting value as string
     * @return string value
     */
    public String getAsString() {
        return settingValue;
    }
}
