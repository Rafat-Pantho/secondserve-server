package com.secondserve.server.controller;

import com.secondserve.server.dto.HotelDto;
import com.secondserve.server.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // --- ADDED: Import for HttpStatus.CREATED
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
// --- MODIFIED: Standardized the base API path for consistency. ---
@RequestMapping("/hotels")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HotelController {

    @Autowired
    private HotelService hotelService;

    // ==============================================================
    // ======== ADDED: The new endpoint for Hotel Registration ========
    // ==============================================================
    /**
     * Handles the registration of a new Hotel Manager.
     * This is the endpoint called by the "Register as Hotel Manager" form.
     */
    @PostMapping("/register")
    public ResponseEntity<HotelDto> registerHotel(@Valid @RequestBody HotelDto hotelDto) {
        try {
            // The HotelService will now contain the logic to create the hotel,
            // encode the password, and generate the unique hotel_code.
            HotelDto createdHotel = hotelService.registerHotel(hotelDto);

            // Return HttpStatus.CREATED (201) for a successful creation.
            return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
        } catch (Exception e) {
            // It's good practice to return a more specific error in a real app,
            // but BadRequest is a safe default.
            return ResponseEntity.badRequest().body(null);
        }
    }


    // --- (The existing GET, PUT, and DELETE endpoints are perfectly fine and do not need changes) ---

    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        // This endpoint can be used by NGOs to browse all active hotels.
        List<HotelDto> hotels = hotelService.getAllActiveHotels();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id) {
        try {
            HotelDto hotel = hotelService.getHotelById(id);
            return ResponseEntity.ok(hotel);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDto> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelDto hotelDto) {
        // This endpoint is for the manager to update their profile information.
        // It requires security to ensure the user can only update their own hotel.
        try {
            HotelDto updatedHotel = hotelService.updateHotel(id, hotelDto);
            return ResponseEntity.ok(updatedHotel);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        // This endpoint would likely be restricted to admin users.
        try {
            hotelService.deleteHotel(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}