package com.booking.appointment.dao;

import java.util.List;
import java.util.Optional;

/**
 * Base DAO Interface - Generic data access operations
 * 
 * Provides common CRUD operations for all entities.
 * @param <T> Entity type
 * @param <ID> Primary key type
 */
public interface BaseDAO<T, ID> {
    
    /**
     * Create/Insert a new record
     * @param entity the entity to insert
     * @return the inserted entity with auto-generated ID
     */
    T save(T entity);

    /**
     * Update an existing record
     * @param entity the entity to update
     * @return the updated entity
     */
    T update(T entity);

    /**
     * Find record by ID
     * @param id the primary key
     * @return Optional containing the entity if found
     */
    Optional<T> findById(ID id);

    /**
     * Get all records
     * @return list of all entities
     */
    List<T> findAll();

    /**
     * Delete record by ID
     * @param id the primary key
     * @return true if deletion was successful
     */
    boolean deleteById(ID id);

    /**
     * Delete an entity
     * @param entity the entity to delete
     * @return true if deletion was successful
     */
    boolean delete(T entity);

    /**
     * Count total records
     * @return total number of records
     */
    long count();
}
