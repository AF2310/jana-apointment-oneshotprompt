package com.booking.appointment.service;

import com.booking.appointment.model.CarDetails;
import java.util.List;
import java.util.Optional;

/**
 * CarDetails Service Interface - Business logic for vehicle management
 * 
 * Handles car information associated with user appointments.
 */
public interface CarDetailsService {
    
    /**
     * Add a car for a user
     * @param carDetails the car to add
     * @return the added car with ID
     */
    CarDetails addCar(CarDetails carDetails);

    /**
     * Get car by ID
     * @param carId the car ID
     * @return Optional containing the car
     */
    Optional<CarDetails> getCarById(Integer carId);

    /**
     * Get all cars for a user
     * @param userId the user ID
     * @return list of user's cars
     */
    List<CarDetails> getUserCars(Integer userId);

    /**
     * Update car information
     * @param carDetails the car to update
     * @return the updated car
     */
    CarDetails updateCar(CarDetails carDetails);

    /**
     * Delete a car
     * @param carId the car ID
     * @return true if deletion was successful
     */
    boolean deleteCar(Integer carId);

    /**
     * Find car by license plate
     * @param licensePlate the license plate
     * @return Optional containing the car
     */
    Optional<CarDetails> findByLicensePlate(String licensePlate);

    /**
     * Find car by VIN
     * @param vin the vehicle identification number
     * @return Optional containing the car
     */
    Optional<CarDetails> findByVin(String vin);

    /**
     * Check if license plate exists
     * @param licensePlate the license plate
     * @return true if it exists
     */
    boolean licensePlateExists(String licensePlate);

    /**
     * Check if VIN exists
     * @param vin the VIN
     * @return true if it exists
     */
    boolean vinExists(String vin);

    /**
     * Validate car information
     * @param carDetails the car to validate
     * @return validation result message (empty if valid)
     */
    String validateCarDetails(CarDetails carDetails);
}
