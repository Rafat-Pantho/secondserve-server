package com.secondserve.server.controller;

import com.secondserve.server.dto.AuthResponse;
import com.secondserve.server.dto.KitchenStaffDto;
import com.secondserve.server.service.KitchenStaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
// The base path for all staff-related endpoints is /staff.
// Since your server's context path is /api, the final URL will be: /api/staff
@RequestMapping("/staff")
@CrossOrigin(origins = "*", maxAge = 3600)
public class KitchenStaffController {

    @Autowired
    private KitchenStaffService kitchenStaffService;

    /**
     * Handles the registration of a new Kitchen Staff member.
     * This is the specific endpoint that the "Register as Kitchen Staff" form will call.
     *
     * @param kitchenStaffDto The user's registration details, sent as a JSON object in the request body.
     * @return On success, an AuthResponse containing a JWT token for immediate login.
     *         On failure, a Bad Request response.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerKitchenStaff(@Valid @RequestBody KitchenStaffDto kitchenStaffDto) {
        try {
            // All the complex work (validating the hotel code, encoding the password, saving the user)
            // is handled by the KitchenStaffService. This keeps the controller clean and focused.
            AuthResponse authResponse = kitchenStaffService.registerStaff(kitchenStaffDto);

            // Return HTTP Status 201 Created, which is the correct standard for a successful registration.
            return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            // If the service throws an error (e.g., duplicate email, invalid hotel code),
            // log the error and return a 400 Bad Request status.
            e.printStackTrace(); // This is helpful for debugging on the server.
            return ResponseEntity.badRequest().build();
        }
    }

    // In the future, you could add other endpoints here to manage kitchen staff.
    // For example:
    //
    // @GetMapping("/profile")
    // @PreAuthorize("hasRole('KITCHEN_STAFF')") // You would secure this endpoint
    // public ResponseEntity<KitchenStaffDto> getMyProfile(...) {
    //     // Logic to get the logged-in staff member's profile
    // }
}