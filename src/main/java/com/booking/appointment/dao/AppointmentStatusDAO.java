package com.booking.appointment.dao;

import com.booking.appointment.model.AppointmentStatus;
import java.util.Optional;

/**
 * AppointmentStatus DAO Interface - Data access operations for AppointmentStatus entity
 * 
 * Provides operations for managing appointment status master data.
 */
public interface AppointmentStatusDAO extends BaseDAO<AppointmentStatus, Integer> {
    
    /**
     * Find status by name
     * @param statusName the status name
     * @return Optional containing the status if found
     */
    Optional<AppointmentStatus> findByName(String statusName);

    /**
     * Check if status exists by name
     * @param statusName the status name
     * @return true if status exists
     */
    boolean statusExists(String statusName);

    /**
     * Get all active statuses sorted by display order
     * @return list of active statuses in order
     */
    java.util.List<AppointmentStatus> getAllActiveSorted();

    /**
     * Get status ID by name
     * @param statusName the status name
     * @return status ID or -1 if not found
     */
    int getStatusIdByName(String statusName);

    /**
     * Add a new appointment status (for extensibility)
     * @param statusName the name of the new status
     * @param description description of the status
     * @param displayOrder display order for UI
     * @return the created status or null if failed
     */
    AppointmentStatus addNewStatus(String statusName, String description, int displayOrder);
}
