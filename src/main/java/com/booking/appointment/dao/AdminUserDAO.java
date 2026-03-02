package com.booking.appointment.dao;

import com.booking.appointment.model.AdminUser;
import java.util.Optional;

/**
 * AdminUser DAO Interface - Data access operations for AdminUser entity
 * 
 * Provides CRUD operations and specialized queries for admin users.
 */
public interface AdminUserDAO extends BaseDAO<AdminUser, Integer> {
    
    /**
     * Find admin by username
     * @param username the admin username
     * @return Optional containing the admin if found
     */
    Optional<AdminUser> findByUsername(String username);

    /**
     * Find admin by email
     * @param email the admin email
     * @return Optional containing the admin if found
     */
    Optional<AdminUser> findByEmail(String email);

    /**
     * Check if username exists
     * @param username the username to check
     * @return true if username exists
     */
    boolean usernameExists(String username);

    /**
     * Check if admin is active
     * @param adminId the admin identifier
     * @return true if admin is active
     */
    boolean isActive(Integer adminId);

    /**
     * Update last login timestamp
     * @param adminId the admin identifier
     * @return true if update was successful
     */
    boolean updateLastLogin(Integer adminId);

    /**
     * Get active admin count
     * @return number of active admins
     */
    long countActiveAdmins();
}
