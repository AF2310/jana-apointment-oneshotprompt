package com.booking.appointment.service;

import com.booking.appointment.model.AppSetting;
import java.util.List;
import java.util.Optional;

/**
 * AppSetting Service Interface - Business logic for application settings
 * 
 * Handles retrieval and management of configurable application parameters.
 */
public interface AppSettingService {
    
    /**
     * Get setting by key
     * @param settingKey the setting key
     * @return Optional containing the setting
     */
    Optional<AppSetting> getSetting(String settingKey);

    /**
     * Get setting value as string
     * @param settingKey the setting key
     * @return setting value or default if not found
     */
    String getSettingValue(String settingKey);

    /**
     * Get setting value as integer
     * @param settingKey the setting key
     * @return setting value as integer
     */
    Integer getSettingAsInteger(String settingKey);

    /**
     * Get setting value as boolean
     * @param settingKey the setting key
     * @return setting value as boolean
     */
    Boolean getSettingAsBoolean(String settingKey);

    /**
     * Update a setting
     * @param settingKey the setting key
     * @param settingValue the new value
     * @return true if update was successful
     */
    boolean updateSetting(String settingKey, String settingValue);

    /**
     * Get all configurable settings
     * @return list of settings that can be configured
     */
    List<AppSetting> getConfigurableSettings();

    /**
     * Get all settings
     * @return list of all settings
     */
    List<AppSetting> getAllSettings();

    /**
     * Add a new setting
     * @param setting the new setting
     * @return the created setting with ID
     */
    AppSetting addSetting(AppSetting setting);

    /**
     * Get default appointment range in days
     * @return number of days to show available appointments
     */
    int getDefaultAppointmentRangeDays();

    /**
     * Get default time slot duration in minutes
     * @return slot duration in minutes
     */
    int getDefaultTimeSlotDurationMinutes();

    /**
     * Check if same-day booking is allowed
     * @return true if allowed
     */
    boolean isSameDayBookingAllowed();

    /**
     * Get maximum appointments per day
     * @return maximum appointment count
     */
    int getMaxAppointmentsPerDay();
}
