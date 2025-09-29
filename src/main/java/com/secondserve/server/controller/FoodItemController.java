package com.secondserve.server.controller;

import com.secondserve.server.dto.FoodItemDto;
import com.secondserve.server.service.FoodItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal; // For later
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/food-items") // Using the /api prefix
@CrossOrigin(origins = "*", maxAge = 3600)
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    @GetMapping("/available")
    public ResponseEntity<List<FoodItemDto>> getAvailableFoodItems() {
        List<FoodItemDto> foodItems = foodItemService.getAllAvailableFoodItems();
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<FoodItemDto>> getFoodItemsByHotel(@PathVariable Long hotelId) {
        List<FoodItemDto> foodItems = foodItemService.getFoodItemsByHotel(hotelId);
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItemDto> getFoodItemById(@PathVariable Long id) {
        try {
            FoodItemDto foodItem = foodItemService.getFoodItemById(id);
            return ResponseEntity.ok(foodItem);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FoodItemDto> createFoodItem(@Valid @RequestBody FoodItemDto foodItemDto) {
        try {
            // Using placeholder hotel ID for now.
            // In a real app, this would come from the logged-in user's security token.
            Long hotelIdOfLoggedInUser = 1L;
            FoodItemDto createdFoodItem = foodItemService.createFoodItem(foodItemDto, hotelIdOfLoggedInUser);
            return new ResponseEntity<>(createdFoodItem, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItemDto> updateFoodItem(@PathVariable Long id, @Valid @RequestBody FoodItemDto foodItemDto) {
        try {
            FoodItemDto updatedFoodItem = foodItemService.updateFoodItem(id, foodItemDto);
            return ResponseEntity.ok(updatedFoodItem);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/unavailable")
    public ResponseEntity<Void> markAsUnavailable(@PathVariable Long id) {
        try {
            foodItemService.markAsUnavailable(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        try {
            foodItemService.deleteFoodItem(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}/approve")
// @PreAuthorize("hasRole('HOTEL_MANAGER')") // Enable this when security is complete
    public ResponseEntity<Void> approveFoodItem(@PathVariable Long id) {
        try {
            foodItemService.markAsAvailable(id); // We'll need to create this service method
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/hotel/{hotelId}/pending")
    public ResponseEntity<List<FoodItemDto>> getPendingFoodItemsByHotel(@PathVariable Long hotelId) {
        List<FoodItemDto> foodItems = foodItemService.getPendingFoodItemsByHotel(hotelId);
        return ResponseEntity.ok(foodItems);
    }
}