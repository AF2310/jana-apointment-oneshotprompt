package com.booking.appointment.service;

import com.booking.appointment.model.AdminUser;
import java.util.List;
import java.util.Optional;

/**
 * Admin Service Interface - Business logic for administrative operations
 * 
 * Handles admin authentication, user management, and system configuration.
 */
public interface AdminService {
    
    /**
     * Authenticate admin user
     * @param username admin username
     * @param password admin password
     * @return Optional containing the admin if credentials are valid
     */
    Optional<AdminUser> authenticateAdmin(String username, String password);

    /**
     * Get admin by ID
     * @param adminId the admin ID
     * @return Optional containing the admin
     */
    Optional<AdminUser> getAdminById(Integer adminId);

    /**
     * Get admin by username
     * @param username the admin username
     * @return Optional containing the admin
     */
    Optional<AdminUser> getAdminByUsername(String username);

    /**
     * Get all admin users
     * @return list of all admins
     */
    List<AdminUser> getAllAdmins();

    /**
     * Get all active admin users
     * @return list of active admins
     */
    List<AdminUser> getActiveAdmins();

    /**
     * Create new admin user
     * @param admin the admin to create (password will be hashed)
     * @return the created admin with ID
     */
    AdminUser createAdmin(AdminUser admin);

    /**
     * Update admin user
     * @param admin the admin to update
     * @return the updated admin
     */
    AdminUser updateAdmin(AdminUser admin);

    /**
     * Deactivate an admin account
     * @param adminId the admin ID
     * @return true if deactivation was successful
     */
    boolean deactivateAdmin(Integer adminId);

    /**
     * Activate an admin account
     * @param adminId the admin ID
     * @return true if activation was successful
     */
    boolean activateAdmin(Integer adminId);

    /**
     * Change admin password
     * @param adminId the admin ID
     * @param oldPassword the current password
     * @param newPassword the new password
     * @return true if password change was successful
     */
    boolean changePassword(Integer adminId, String oldPassword, String newPassword);

    /**
     * Update last login timestamp
     * @param adminId the admin ID
     * @return true if update was successful
     */
    boolean recordLastLogin(Integer adminId);

    /**
     * Check if username is available
     * @param username the username to check
     * @return true if available
     */
    boolean isUsernameAvailable(String username);

    /**
     * Validate admin credentials
     * @param username the username
     * @param password the password
     * @return validation result message (empty if valid)
     */
    String validateCredentials(String username, String password);
}
