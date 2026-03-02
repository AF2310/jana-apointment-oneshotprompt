package com.booking.appointment.service;

import com.booking.appointment.model.User;
import java.util.List;
import java.util.Optional;

/**
 * User Service Interface - Business logic for user operations
 * 
 * Handles user registration, retrieval, and management.
 */
public interface UserService {
    
    /**
     * Register a new user
     * @param user the user to register
     * @return the registered user with ID
     */
    User registerUser(User user);

    /**
     * Get user by ID
     * @param userId the user ID
     * @return Optional containing the user
     */
    Optional<User> getUserById(Integer userId);

    /**
     * Get user by email
     * @param email the user's email
     * @return Optional containing the user
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Get all users
     * @return list of all users
     */
    List<User> getAllUsers();

    /**
     * Update user information
     * @param user the user to update
     * @return the updated user
     */
    User updateUser(User user);

    /**
     * Delete a user
     * @param userId the user ID
     * @return true if deletion was successful
     */
    boolean deleteUser(Integer userId);

    /**
     * Search users by name
     * @param firstName first name to search
     * @param lastName last name to search
     * @return list of matching users
     */
    List<User> searchUsers(String firstName, String lastName);

    /**
     * Check if email is already registered
     * @param email the email to check
     * @return true if email exists
     */
    boolean emailExists(String email);

    /**
     * Validate user information
     * @param user the user to validate
     * @return validation result message (empty if valid)
     */
    String validateUser(User user);
}
