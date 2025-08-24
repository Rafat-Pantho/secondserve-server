package com.secondserve.server.controller;

import com.secondserve.server.dto.AuthResponse;
import com.secondserve.server.dto.HotelDto;
import com.secondserve.server.dto.LoginRequest;
import com.secondserve.server.dto.NgoDto;
import com.secondserve.server.service.AuthService;
import com.secondserve.server.service.HotelService;
import com.secondserve.server.service.NgoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private NgoService ngoService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.login(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register/hotel")
    public ResponseEntity<HotelDto> registerHotel(@Valid @RequestBody HotelDto hotelDto) {
        try {
            HotelDto createdHotel = hotelService.createHotel(hotelDto);
            return ResponseEntity.ok(createdHotel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register/ngo")
    public ResponseEntity<NgoDto> registerNgo(@Valid @RequestBody NgoDto ngoDto) {
        try {
            NgoDto createdNgo = ngoService.createNgo(ngoDto);
            return ResponseEntity.ok(createdNgo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}