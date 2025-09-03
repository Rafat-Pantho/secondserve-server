package com.secondserve.server.controller;

import com.secondserve.server.dto.DashboardStatsDto; // --- ADDED: The missing import ---
import com.secondserve.server.dto.HotelDto;
import com.secondserve.server.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
// NOTE: Ensure your server.properties context-path is '/api' for this to work
@RequestMapping("/hotels")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HotelController {

    @Autowired
    private HotelService hotelService;

    /**
     * Handles the registration of a new Hotel Manager.
     */
    @PostMapping("/register")
    public ResponseEntity<HotelDto> registerHotel(@Valid @RequestBody HotelDto hotelDto) {
        try {
            HotelDto createdHotel = hotelService.registerHotel(hotelDto);
            return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Good for debugging
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves the statistics for the hotel manager's dashboard.
     */
    @GetMapping("/dashboard-stats")
    // @PreAuthorize("hasRole('HOTEL_MANAGER')") // Enable security later
    public ResponseEntity<DashboardStatsDto> getDashboardStats(/* @AuthenticationPrincipal CustomUserDetails userDetails */) {
        try {
            // Long hotelId = userDetails.getHotelId(); // Get ID securely from token in production
            Long hotelId = 1L; // <<< --- FOR DEVELOPMENT, USE A PLACEHOLDER

            DashboardStatsDto stats = hotelService.getDashboardStats(hotelId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // --- (The other GET, PUT, and DELETE endpoints are perfectly fine) ---

    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels() {
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
        try {
            HotelDto updatedHotel = hotelService.updateHotel(id, hotelDto);
            return ResponseEntity.ok(updatedHotel);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        try {
            hotelService.deleteHotel(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}