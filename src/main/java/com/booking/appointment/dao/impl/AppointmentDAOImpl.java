package com.booking.appointment.dao.impl;

import com.booking.appointment.dao.AppointmentDAO;
import com.booking.appointment.model.Appointment;
import com.booking.appointment.model.AppointmentStatus;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Appointment DAO Implementation - JDBC-based database operations for Appointment entity
 * 
 * Provides concrete implementations of appointment data access operations.
 * This is a critical DAO for the appointment booking system.
 */
public class AppointmentDAOImpl implements AppointmentDAO {
    private final Connection connection;

    public AppointmentDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Appointment save(Appointment appointment) {
        String sql = "INSERT INTO appointments (user_id, car_id, appointment_date, appointment_time, end_time, status_id, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, appointment.getUserId());
            stmt.setInt(2, appointment.getCarId());
            stmt.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(4, Time.valueOf(appointment.getAppointmentTime()));
            
            if (appointment.getEndTime() != null) {
                stmt.setTime(5, Time.valueOf(appointment.getEndTime()));
            } else {
                stmt.setNull(5, Types.TIME);
            }
            
            stmt.setInt(6, appointment.getStatusId());
            stmt.setString(7, appointment.getNotes());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                appointment.setAppointmentId(rs.getInt(1));
                appointment.setCreatedAt(LocalDateTime.now());
                appointment.setUpdatedAt(LocalDateTime.now());
            }
            return appointment;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public Appointment update(Appointment appointment) {
        String sql = "UPDATE appointments SET user_id = ?, car_id = ?, appointment_date = ?, appointment_time = ?, end_time = ?, status_id = ?, notes = ?, updated_at = NOW() WHERE appointment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, appointment.getUserId());
            stmt.setInt(2, appointment.getCarId());
            stmt.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(4, Time.valueOf(appointment.getAppointmentTime()));
            
            if (appointment.getEndTime() != null) {
                stmt.setTime(5, Time.valueOf(appointment.getEndTime()));
            } else {
                stmt.setNull(5, Types.TIME);
            }
            
            stmt.setInt(6, appointment.getStatusId());
            stmt.setString(7, appointment.getNotes());
            stmt.setInt(8, appointment.getAppointmentId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                appointment.setUpdatedAt(LocalDateTime.now());
            }
            return appointment;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Appointment> findById(Integer id) {
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointment by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Appointment> findAll() {
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC, appointment_time DESC";
        List<Appointment> appointments = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all appointments: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByUserId(Integer userId) {
        String sql = "SELECT * FROM appointments WHERE user_id = ? ORDER BY appointment_date DESC, appointment_time DESC";
        List<Appointment> appointments = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointments by user ID: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByDate(LocalDate date) {
        String sql = "SELECT * FROM appointments WHERE appointment_date = ? ORDER BY appointment_time";
        List<Appointment> appointments = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointments by date: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM appointments WHERE appointment_date BETWEEN ? AND ? ORDER BY appointment_date, appointment_time";
        List<Appointment> appointments = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointments by date range: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByStatus(Integer statusId) {
        String sql = "SELECT * FROM appointments WHERE status_id = ? ORDER BY appointment_date DESC, appointment_time DESC";
        List<Appointment> appointments = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, statusId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointments by status: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public List<Appointment> findByCarId(Integer carId) {
        String sql = "SELECT * FROM appointments WHERE car_id = ? ORDER BY appointment_date DESC, appointment_time DESC";
        List<Appointment> appointments = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointments by car ID: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public boolean isTimeSlotAvailable(LocalDate date, String time) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE appointment_date = ? AND appointment_time = ? AND status_id != (SELECT status_id FROM appointment_statuses WHERE status_name = ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            stmt.setString(2, time);
            stmt.setString(3, AppointmentStatus.CANCELED);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking time slot availability: " + e.getMessage(), e);
        }
        return true;
    }

    @Override
    public List<Appointment> getCalendarAppointments(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM appointments WHERE appointment_date BETWEEN ? AND ? ORDER BY appointment_date, appointment_time";
        List<Appointment> appointments = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting calendar appointments: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public List<Appointment> getUpcomingAppointments(Integer userId, int limit) {
        String sql = "SELECT * FROM appointments WHERE user_id = ? AND appointment_date >= CURDATE() ORDER BY appointment_date, appointment_time LIMIT ?";
        List<Appointment> appointments = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting upcoming appointments: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public int countAppointmentsOnDate(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE appointment_date = ? AND status_id != (SELECT status_id FROM appointment_statuses WHERE status_name = ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            stmt.setString(2, AppointmentStatus.CANCELED);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting appointments on date: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Appointment appointment) {
        return deleteById(appointment.getAppointmentId());
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM appointments";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting appointments: " + e.getMessage(), e);
        }
        return 0;
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setUserId(rs.getInt("user_id"));
        appointment.setCarId(rs.getInt("car_id"));
        
        Date appointmentDate = rs.getDate("appointment_date");
        if (appointmentDate != null) {
            appointment.setAppointmentDate(appointmentDate.toLocalDate());
        }
        
        Time appointmentTime = rs.getTime("appointment_time");
        if (appointmentTime != null) {
            appointment.setAppointmentTime(appointmentTime.toLocalTime());
        }
        
        Time endTime = rs.getTime("end_time");
        if (endTime != null) {
            appointment.setEndTime(endTime.toLocalTime());
        }
        
        appointment.setStatusId(rs.getInt("status_id"));
        appointment.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            appointment.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            appointment.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return appointment;
    }
}
