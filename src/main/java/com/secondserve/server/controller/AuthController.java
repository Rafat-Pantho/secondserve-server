package com.secondserve.server.controller;

import com.secondserve.server.dto.AuthResponse;
import com.secondserve.server.dto.LoginRequest;
import com.secondserve.server.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
// MODIFIED: Base path now includes /api for consistency with server context path
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    // --- MODIFIED: All other service dependencies and registration endpoints have been removed. ---
    // The sole responsibility of this controller is now authentication (login).

    /**
     * Authenticates a user (Hotel Manager, Kitchen Staff, or NGO) and returns a JWT.
     * @param loginRequest The user's email, password, and userType.
     * @return On success, an AuthResponse containing the JWT token and user details.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.login(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            // It's good practice to log the error and return a specific status if possible
            e.printStackTrace(); // Helpful for debugging
            // For security, you might return 401 Unauthorized for bad credentials
            return ResponseEntity.status(401).build();
        }
    }
}