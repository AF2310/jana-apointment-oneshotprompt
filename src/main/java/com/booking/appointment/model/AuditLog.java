package com.booking.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * AuditLog Entity - Tracks administrative actions for security and audit purposes
 * 
 * Records all admin operations for compliance and investigation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    private Long logId;
    private Integer adminId;
    private String action;
    private String entityType;
    private Integer entityId;
    private String changesBefore;
    private String changesAfter;
    private String ipAddress;
    private LocalDateTime createdAt;
}
