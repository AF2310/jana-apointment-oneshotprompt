package com.booking.appointment.controller;

import com.booking.appointment.util.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, String> payload) {
        String username = payload.getOrDefault("username", "").trim();
        String password = payload.getOrDefault("password", "").trim();

        if (username.isEmpty() || password.isEmpty()) {
            return ApiResponse.error("Username and password are required", 400);
        }

        if ("admin".equals(username) && "admin123".equals(password)) {
            Map<String, Object> adminData = Map.of(
                "adminId", 1,
                "username", username,
                "fullName", "System Administrator"
            );
            return ApiResponse.success("Login successful", adminData);
        }

        return ApiResponse.error("Invalid username or password", 401);
    }

    @GetMapping("/dashboard/stats")
    public ApiResponse<Map<String, Object>> dashboardStats() {
        Map<String, Object> stats = Map.of(
            "totalAppointments", 0,
            "todayAppointments", 0,
            "date", LocalDate.now().toString()
        );
        return ApiResponse.success("Dashboard statistics retrieved", stats);
    }
}
