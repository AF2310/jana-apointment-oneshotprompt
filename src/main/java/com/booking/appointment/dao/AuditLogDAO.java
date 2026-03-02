package com.booking.appointment.dao;

import com.booking.appointment.model.AuditLog;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AuditLog DAO Interface - Data access operations for AuditLog entity
 * 
 * Provides operations for tracking administrative actions.
 */
public interface AuditLogDAO extends BaseDAO<AuditLog, Long> {
    
    /**
     * Get audit logs for an admin
     * @param adminId the admin identifier
     * @return list of audit logs for that admin
     */
    List<AuditLog> getLogsForAdmin(Integer adminId);

    /**
     * Get audit logs for an entity
     * @param entityType the entity type (e.g., "APPOINTMENT", "SETTING")
     * @param entityId the entity identifier
     * @return list of audit logs for that entity
     */
    List<AuditLog> getLogsForEntity(String entityType, Integer entityId);

    /**
     * Get audit logs within a date range
     * @param startDate range start date
     * @param endDate range end date
     * @return list of audit logs in the range
     */
    List<AuditLog> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get all audit logs by action type
     * @param action the action type
     * @return list of audit logs for that action
     */
    List<AuditLog> getLogsByAction(String action);

    /**
     * Log an admin action
     * @param adminId the admin identifier
     * @param action the action performed
     * @param entityType the type of entity affected
     * @param entityId the entity affected
     * @param changesBefore JSON before state (optional)
     * @param changesAfter JSON after state (optional)
     * @param ipAddress the IP address of the request
     * @return the created audit log
     */
    AuditLog logAction(Integer adminId, String action, String entityType, 
                       Integer entityId, String changesBefore, String changesAfter, String ipAddress);

    /**
     * Clear old audit logs
     * @param daysOld delete logs older than this many days
     * @return number of logs deleted
     */
    int clearOldLogs(int daysOld);
}
