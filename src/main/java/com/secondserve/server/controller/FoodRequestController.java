package com.secondserve.server.controller;

import com.secondserve.server.dto.FoodRequestDto;
import com.secondserve.server.entity.FoodRequest.RequestStatus;
import com.secondserve.server.service.FoodRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/food-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FoodRequestController {

    @Autowired
    private FoodRequestService foodRequestService;

    // --- Endpoint for an NGO to CREATE a new request ---
    @PostMapping
    public ResponseEntity<FoodRequestDto> createFoodRequest(@Valid @RequestBody FoodRequestDto foodRequestDto) {
        try {
            // FOR DEVELOPMENT: Placeholder for the logged-in NGO's ID
            Long loggedInNgoId = 1L; // In production, get this from the security token

            FoodRequestDto createdRequest = foodRequestService.createFoodRequest(foodRequestDto, loggedInNgoId);
            return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // --- Endpoints for a Hotel Manager to UPDATE a request's status ---
    @PutMapping("/{id}/approve")
    public ResponseEntity<FoodRequestDto> approveRequest(@PathVariable Long id) {
        // Add security here to verify the user is a manager of the hotel that owns this request
        try {
            FoodRequestDto updatedRequest = foodRequestService.updateRequestStatus(id, RequestStatus.APPROVED);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<FoodRequestDto> rejectRequest(@PathVariable Long id) {
        // Add security check here as well
        try {
            FoodRequestDto updatedRequest = foodRequestService.updateRequestStatus(id, RequestStatus.REJECTED);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // --- Endpoints for fetching request lists ---
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<FoodRequestDto>> getRequestsForHotel(@PathVariable Long hotelId) {
        List<FoodRequestDto> requests = foodRequestService.getRequestsForHotel(hotelId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<FoodRequestDto>> getRequestsByNgo(@PathVariable Long hotelId) {
        List<FoodRequestDto> requests = foodRequestService.getRequestsByNgo(hotelId);
        return ResponseEntity.ok(requests);
    }
}