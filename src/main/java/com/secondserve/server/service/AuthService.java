package com.secondserve.server.service;

import com.secondserve.server.dto.AuthResponse;
import com.secondserve.server.dto.LoginRequest;
import com.secondserve.server.entity.Hotel;
import com.secondserve.server.entity.Ngo;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.HotelRepository;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest loginRequest) {
        if ("HOTEL".equals(loginRequest.getUserType())) {
            Hotel hotel = hotelRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with email: " + loginRequest.getEmail()));

            if (!passwordEncoder.matches(loginRequest.getPassword(), hotel.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            if (!hotel.getIsActive()) {
                throw new RuntimeException("Account is inactive");
            }

            String token = jwtUtil.generateToken(hotel.getEmail(), "HOTEL", hotel.getId());
            return new AuthResponse(token, "HOTEL", hotel.getId(), hotel.getManagerName(), hotel.getEmail());

        } else if ("NGO".equals(loginRequest.getUserType())) {
            Ngo ngo = ngoRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("NGO not found with email: " + loginRequest.getEmail()));

            if (!passwordEncoder.matches(loginRequest.getPassword(), ngo.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            if (!ngo.getIsActive()) {
                throw new RuntimeException("Account is inactive");
            }

            String token = jwtUtil.generateToken(ngo.getEmail(), "NGO", ngo.getId());
            return new AuthResponse(token, "NGO", ngo.getId(), ngo.getContactPerson(), ngo.getEmail());

        } else {
            throw new RuntimeException("Invalid user type");
        }
    }
}