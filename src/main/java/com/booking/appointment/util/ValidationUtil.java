package com.booking.appointment.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Validation and Security Utility Class
 * 
 * Provides input validation and password hashing utilities.
 */
public class ValidationUtil {
    
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "^\\d{10,15}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    /**
     * Validate email format
     * @param email the email to validate
     * @return true if email is valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate phone number format
     * @param phone the phone number
     * @return true if phone is valid
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validate string is not empty
     * @param value the string to check
     * @return true if not empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validate name format
     * @param name the name to validate
     * @return true if name is valid
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().length() < 2 || name.length() > 50) {
            return false;
        }
        return name.matches("^[a-zA-Z\\s'-]+$");
    }

    /**
     * Hash password using SHA-256
     * @param password the password to hash
     * @return hashed password
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Compare password with hash
     * @param password the plain password
     * @param hash the hashed password
     * @return true if password matches hash
     */
    public static boolean verifyPassword(String password, String hash) {
        return hashPassword(password).equals(hash);
    }

    /**
     * Validate VIN format (17 characters, alphanumeric)
     * @param vin the VIN to validate
     * @return true if VIN is valid
     */
    public static boolean isValidVIN(String vin) {
        if (vin == null) {
            return false;
        }
        return vin.replaceAll("[A-HJ-NPR-Z0-9]", "").length() == 0 && vin.length() == 17;
    }

    /**
     * Validate license plate format
     * @param licensePlate the license plate
     * @return true if license plate is valid
     */
    public static boolean isValidLicensePlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return false;
        }
        return licensePlate.matches("^[A-Z0-9-]+$") && licensePlate.length() >= 3 && licensePlate.length() <= 20;
    }

    /**
     * Validate year is reasonable
     * @param year the year
     * @return true if year is valid
     */
    public static boolean isValidYear(Integer year) {
        if (year == null) {
            return false;
        }
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        return year >= 1900 && year <= currentYear + 1;
    }

    /**
     * Sanitize input string to prevent SQL injection
     * @param input the input string
     * @return sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[';\"\\\\]", "");
    }

    /**
     * Validate password strength
     * @param password the password to validate
     * @return validation message (empty if valid)
     */
    public static String validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }
        
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }
        
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit";
        }
        
        return "";
    }
}
