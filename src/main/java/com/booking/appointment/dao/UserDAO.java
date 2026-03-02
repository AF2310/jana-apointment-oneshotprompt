package com.booking.appointment.dao;

import com.booking.appointment.model.User;
import java.util.List;
import java.util.Optional;

/**
 * User DAO Interface - Data access operations for User entity
 * 
 * Provides CRUD operations and specialized queries for users.
 */
public interface UserDAO extends BaseDAO<User, Integer> {
    
    /**
     * Find user by email
     * @param email the user's email
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Search users by name
     * @param firstName first name to search for
     * @param lastName last name to search for
     * @return list of matching users
     */
    List<User> searchByName(String firstName, String lastName);

    /**
     * Find users by partial first name
     * @param firstName partial first name
     * @return list of matching users
     */
    List<User> findByFirstNameContaining(String firstName);

    /**
     * Find users by partial last name
     * @param lastName partial last name
     * @return list of matching users
     */
    List<User> findByLastNameContaining(String lastName);

    /**
     * Check if email already exists
     * @param email the email to check
     * @return true if email exists
     */
    boolean emailExists(String email);
}
