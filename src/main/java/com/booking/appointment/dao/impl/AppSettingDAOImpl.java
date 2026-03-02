package com.booking.appointment.dao.impl;

import com.booking.appointment.dao.AppSettingDAO;
import com.booking.appointment.model.AppSetting;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * AppSetting DAO Implementation - JDBC-based database operations for AppSetting entity
 * 
 * Provides concrete implementations of application settings data access operations.
 */
public class AppSettingDAOImpl implements AppSettingDAO {
    private final Connection connection;

    public AppSettingDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AppSetting save(AppSetting appSetting) {
        String sql = "INSERT INTO app_settings (setting_key, setting_value, setting_type, description, is_configurable) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, appSetting.getSettingKey());
            stmt.setString(2, appSetting.getSettingValue());
            stmt.setString(3, appSetting.getSettingType());
            stmt.setString(4, appSetting.getDescription());
            stmt.setBoolean(5, appSetting.getIsConfigurable());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                appSetting.setSettingId(rs.getInt(1));
                appSetting.setCreatedAt(LocalDateTime.now());
                appSetting.setUpdatedAt(LocalDateTime.now());
            }
            return appSetting;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving app setting: " + e.getMessage(), e);
        }
    }

    @Override
    public AppSetting update(AppSetting appSetting) {
        String sql = "UPDATE app_settings SET setting_value = ?, description = ?, is_configurable = ?, updated_by = ?, updated_at = NOW() WHERE setting_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appSetting.getSettingValue());
            stmt.setString(2, appSetting.getDescription());
            stmt.setBoolean(3, appSetting.getIsConfigurable());
            
            if (appSetting.getUpdatedBy() != null) {
                stmt.setInt(4, appSetting.getUpdatedBy());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setInt(5, appSetting.getSettingId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                appSetting.setUpdatedAt(LocalDateTime.now());
            }
            return appSetting;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating app setting: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<AppSetting> findById(Integer id) {
        String sql = "SELECT * FROM app_settings WHERE setting_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToAppSetting(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding app setting by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<AppSetting> findByKey(String settingKey) {
        String sql = "SELECT * FROM app_settings WHERE setting_key = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, settingKey);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToAppSetting(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding app setting by key: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public String getValueByKey(String settingKey) {
        return findByKey(settingKey).map(AppSetting::getSettingValue).orElse(null);
    }

    @Override
    public boolean updateByKey(String settingKey, String settingValue) {
        String sql = "UPDATE app_settings SET setting_value = ?, updated_at = NOW() WHERE setting_key = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, settingValue);
            stmt.setString(2, settingKey);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating app setting by key: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AppSetting> findAll() {
        String sql = "SELECT * FROM app_settings ORDER BY setting_key";
        List<AppSetting> settings = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                settings.add(mapResultSetToAppSetting(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all app settings: " + e.getMessage(), e);
        }
        return settings;
    }

    @Override
    public List<AppSetting> getConfigurableSettings() {
        String sql = "SELECT * FROM app_settings WHERE is_configurable = TRUE ORDER BY setting_key";
        List<AppSetting> settings = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                settings.add(mapResultSetToAppSetting(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting configurable settings: " + e.getMessage(), e);
        }
        return settings;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM app_settings WHERE setting_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting app setting: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(AppSetting appSetting) {
        return deleteById(appSetting.getSettingId());
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM app_settings";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting app settings: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public boolean keyExists(String settingKey) {
        return findByKey(settingKey).isPresent();
    }

    private AppSetting mapResultSetToAppSetting(ResultSet rs) throws SQLException {
        AppSetting setting = new AppSetting();
        setting.setSettingId(rs.getInt("setting_id"));
        setting.setSettingKey(rs.getString("setting_key"));
        setting.setSettingValue(rs.getString("setting_value"));
        setting.setSettingType(rs.getString("setting_type"));
        setting.setDescription(rs.getString("description"));
        setting.setIsConfigurable(rs.getBoolean("is_configurable"));
        
        Object updatedBy = rs.getObject("updated_by");
        if (updatedBy != null) {
            setting.setUpdatedBy((Integer) updatedBy);
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            setting.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            setting.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return setting;
    }
}
