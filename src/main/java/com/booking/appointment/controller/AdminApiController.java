package com.booking.appointment.controller;

import com.booking.appointment.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9._-]{3,50}$");

    private final ConcurrentMap<String, FailedLoginState> failedLogins = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, SessionState> sessions = new ConcurrentHashMap<>();

    @Value("${admin.auth.username:admin}")
    private String configuredUsername;

    @Value("${admin.auth.password:}")
    private String configuredPassword;

    @Value("${admin.auth.max-failed-attempts:5}")
    private int maxFailedAttempts;

    @Value("${admin.auth.lock-minutes:15}")
    private long lockMinutes;

    @Value("${admin.auth.session-minutes:30}")
    private long sessionMinutes;

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody(required = false) Map<String, String> payload,
                                                  HttpServletRequest request) {
        if (payload == null) {
            return ApiResponse.error("Invalid request payload", 400);
        }

        String username = normalize(payload.get("username"));
        String password = normalize(payload.get("password"));
        String clientKey = resolveClientKey(request);

        if (isClientLocked(clientKey)) {
            return ApiResponse.error("Too many failed attempts. Try again later.", 429);
        }

        if (username.isEmpty() || password.isEmpty()) {
            return ApiResponse.error("Username and password are required", 400);
        }

        if (!isValidUsername(username)) {
            return ApiResponse.error("Invalid username format", 400);
        }

        if (password.length() < 8 || password.length() > 128) {
            return ApiResponse.error("Invalid password format", 400);
        }

        if (configuredPassword == null || configuredPassword.isBlank()) {
            return ApiResponse.error("Admin authentication is not configured", 503);
        }

        if (constantTimeEquals(configuredUsername, username)
            && constantTimeEquals(configuredPassword, password)) {
            failedLogins.remove(clientKey);
            String sessionToken = UUID.randomUUID().toString();
            sessions.put(sessionToken, new SessionState(username, LocalDateTime.now().plusMinutes(sessionMinutes)));

            Map<String, Object> adminData = Map.of(
                "adminId", 1,
                "username", username,
                "fullName", "System Administrator",
                "token", sessionToken
            );
            return ApiResponse.success("Login successful", adminData);
        }

        registerFailedAttempt(clientKey);
        return ApiResponse.error("Invalid username or password", 401);
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        String token = extractBearerToken(authorization);
        if (!token.isEmpty()) {
            sessions.remove(token);
        }
        return ApiResponse.success("Logged out", "");
    }

    @GetMapping("/dashboard/stats")
    public ApiResponse<Map<String, Object>> dashboardStats(
        @RequestHeader(value = "Authorization", required = false) String authorization) {

        String token = extractBearerToken(authorization);
        if (!isSessionValid(token)) {
            return ApiResponse.error("Unauthorized", 401);
        }

        Map<String, Object> stats = Map.of(
            "totalAppointments", 0,
            "todayAppointments", 0,
            "date", LocalDate.now().toString()
        );
        return ApiResponse.success("Dashboard statistics retrieved", stats);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isValidUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    private boolean constantTimeEquals(String expected, String actual) {
        byte[] expectedBytes = expected.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = actual.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(expectedBytes, actualBytes);
    }

    private String resolveClientKey(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private void registerFailedAttempt(String clientKey) {
        failedLogins.compute(clientKey, (key, current) -> {
            FailedLoginState state = current == null ? new FailedLoginState() : current;
            state.failedCount++;
            if (state.failedCount >= maxFailedAttempts) {
                state.lockUntil = LocalDateTime.now().plusMinutes(lockMinutes);
                state.failedCount = 0;
            }
            return state;
        });
    }

    private boolean isClientLocked(String clientKey) {
        FailedLoginState state = failedLogins.get(clientKey);
        if (state == null || state.lockUntil == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(state.lockUntil)) {
            failedLogins.remove(clientKey);
            return false;
        }

        return true;
    }

    private String extractBearerToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return "";
        }
        return authorization.substring(7).trim();
    }

    private boolean isSessionValid(String token) {
        if (token.isEmpty()) {
            return false;
        }

        SessionState session = sessions.get(token);
        if (session == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(session.expiresAt)) {
            sessions.remove(token);
            return false;
        }

        return true;
    }

    private static class FailedLoginState {
        private int failedCount;
        private LocalDateTime lockUntil;
    }

    private static class SessionState {
        private final String username;
        private final LocalDateTime expiresAt;

        private SessionState(String username, LocalDateTime expiresAt) {
            this.username = username;
            this.expiresAt = expiresAt;
        }
    }
}
