package com.secondserve.server.service;

import com.secondserve.server.dto.AuthResponse;
import com.secondserve.server.dto.LoginRequest;
import com.secondserve.server.entity.Hotel;
import com.secondserve.server.entity.KitchenStaff; // --- ADDED: Import the KitchenStaff entity ---
import com.secondserve.server.entity.Ngo;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.HotelRepository;
import com.secondserve.server.repository.KitchenStaffRepository; // --- ADDED: Import the KitchenStaff repository ---
import com.secondserve.server.repository.NgoRepository;
import com.secondserve.server.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private NgoRepository ngoRepository;

    // --- ADDED: Dependency for the kitchen staff database table ---
    @Autowired
    private KitchenStaffRepository kitchenStaffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest loginRequest) {
        // --- MODIFIED: Standardized to avoid case sensitivity issues ---
        String userType = loginRequest.getUserType().toUpperCase();

        if ("HOTEL_MANAGER".equals(userType)) { // Changed to match the more descriptive role name
            Hotel hotel = hotelRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail()));

            if (!passwordEncoder.matches(loginRequest.getPassword(), hotel.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            if (!hotel.getIsActive()) {
                throw new RuntimeException("Account is inactive");
            }

            String token = jwtUtil.generateToken(hotel.getEmail(), "HOTEL_MANAGER", hotel.getId());
            return new AuthResponse(token, "HOTEL_MANAGER", hotel.getId(), hotel.getManagerName(), hotel.getEmail());

        } else if ("NGO".equals(userType)) {
            Ngo ngo = ngoRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail()));

            if (!passwordEncoder.matches(loginRequest.getPassword(), ngo.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            if (!ngo.getIsActive()) {
                throw new RuntimeException("Account is inactive");
            }

            String token = jwtUtil.generateToken(ngo.getEmail(), "NGO", ngo.getId());
            return new AuthResponse(token, "NGO", ngo.getId(), ngo.getContactPerson(), ngo.getEmail());

            // =======================================================
            // ======== THIS IS THE NEW BLOCK YOU NEED TO ADD ========
            // =======================================================
        } else if ("KITCHEN_STAFF".equals(userType)) {
            KitchenStaff staff = kitchenStaffRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail()));

            if (!passwordEncoder.matches(loginRequest.getPassword(), staff.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            if (!staff.getIsActive()) {
                throw new RuntimeException("Account is inactive");
            }

            String token = jwtUtil.generateToken(staff.getEmail(), "KITCHEN_STAFF", staff.getId());
            return new AuthResponse(token, "KITCHEN_STAFF", staff.getId(), staff.getStaffName(), staff.getEmail());

        } else {
            // This is the line that was being triggered. It now correctly handles unknown types.
            throw new RuntimeException("Invalid user type");
        }
    }
}