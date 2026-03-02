package com.booking.appointment.dao;

import com.booking.appointment.model.CarDetails;
import java.util.List;
import java.util.Optional;

/**
 * CarDetails DAO Interface - Data access operations for CarDetails entity
 * 
 * Provides CRUD operations and specialized queries for vehicle information.
 */
public interface CarDetailsDAO extends BaseDAO<CarDetails, Integer> {
    
    /**
     * Find all cars for a user
     * @param userId the user identifier
     * @return list of user's cars
     */
    List<CarDetails> findByUserId(Integer userId);

    /**
     * Find car by license plate
     * @param licensePlate the license plate
     * @return Optional containing the car if found
     */
    Optional<CarDetails> findByLicensePlate(String licensePlate);

    /**
     * Find car by VIN
     * @param vin the vehicle identification number
     * @return Optional containing the car if found
     */
    Optional<CarDetails> findByVin(String vin);

    /**
     * Check if license plate exists
     * @param licensePlate the license plate
     * @return true if license plate exists
     */
    boolean licensePlateExists(String licensePlate);

    /**
     * Check if VIN exists
     * @param vin the vehicle identification number
     * @return true if VIN exists
     */
    boolean vinExists(String vin);

    /**
     * Delete all cars for a user
     * @param userId the user identifier
     * @return number of cars deleted
     */
    int deleteByUserId(Integer userId);
}
