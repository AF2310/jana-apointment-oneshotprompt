package com.booking.appointment.dao;

import com.booking.appointment.model.AppSetting;
import java.util.Optional;

/**
 * AppSetting DAO Interface - Data access operations for AppSetting entity
 * 
 * Provides CRUD operations and specialized queries for application settings.
 */
public interface AppSettingDAO extends BaseDAO<AppSetting, Integer> {
    
    /**
     * Find setting by key
     * @param settingKey the setting key
     * @return Optional containing the setting if found
     */
    Optional<AppSetting> findByKey(String settingKey);

    /**
     * Get setting value as string
     * @param settingKey the setting key
     * @return setting value or null if not found
     */
    String getValueByKey(String settingKey);

    /**
     * Update setting by key
     * @param settingKey the setting key
     * @param settingValue the new value
     * @return true if update was successful
     */
    boolean updateByKey(String settingKey, String settingValue);

    /**
     * Check if setting key exists
     * @param settingKey the setting key
     * @return true if setting exists
     */
    boolean keyExists(String settingKey);

    /**
     * Get all configurable settings
     * @return list of all settings that can be configured
     */
    java.util.List<AppSetting> getConfigurableSettings();
}
