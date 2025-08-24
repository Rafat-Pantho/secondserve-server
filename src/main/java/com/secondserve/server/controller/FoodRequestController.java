package com.secondserve.server.controller;

import com.secondserve.server.dto.FoodRequestDto;
import com.secondserve.server.entity.FoodRequest.RequestStatus;
import com.secondserve.server.service.FoodRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/food-requests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FoodRequestController {

    @Autowired
    private FoodRequestService foodRequestService;

    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<FoodRequestDto>> getRequestsByNgo(@PathVariable Long ngoId) {
        List<FoodRequestDto> requests = foodRequestService.getRequestsByNgo(ngoId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<FoodRequestDto>> getRequestsForHotel(@PathVariable Long hotelId) {
        List<FoodRequestDto> requests = foodRequestService.getRequestsForHotel(hotelId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/hotel/{hotelId}/pending")
    public ResponseEntity<List<FoodRequestDto>> getPendingRequestsForHotel(@PathVariable Long hotelId) {
        List<FoodRequestDto> requests = foodRequestService.getPendingRequestsForHotel(hotelId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodRequestDto> getFoodRequestById(@PathVariable Long id) {
        try {
            FoodRequestDto request = foodRequestService.getFoodRequestById(id);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FoodRequestDto> createFoodRequest(@Valid @RequestBody FoodRequestDto foodRequestDto) {
        try {
            FoodRequestDto createdRequest = foodRequestService.createFoodRequest(foodRequestDto);
            return ResponseEntity.ok(createdRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodRequestDto> updateFoodRequest(@PathVariable Long id, @Valid @RequestBody FoodRequestDto foodRequestDto) {
        try {
            FoodRequestDto updatedRequest = foodRequestService.updateFoodRequest(id, foodRequestDto);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<FoodRequestDto> updateRequestStatus(@PathVariable Long id, @RequestParam RequestStatus status) {
        try {
            FoodRequestDto updatedRequest = foodRequestService.updateRequestStatus(id, status);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodRequest(@PathVariable Long id) {
        try {
            foodRequestService.deleteFoodRequest(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}