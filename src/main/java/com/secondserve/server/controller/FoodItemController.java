package com.secondserve.server.controller;

import com.secondserve.server.dto.FoodItemDto;
import com.secondserve.server.service.FoodItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.secondserve.server.repository.NgoRepository;
import com.secondserve.server.repository.HotelRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/food-items") 
@CrossOrigin(origins = "*", maxAge = 3600)
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;
    @Autowired
    private NgoRepository ngoRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @GetMapping("/available")
    public ResponseEntity<List<FoodItemDto>> getAvailableFoodItems() {
        List<FoodItemDto> foodItems = foodItemService.getAllAvailableFoodItems();
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<FoodItemDto>> getFoodItemsByHotel(@PathVariable Long hotelId, Authentication authentication) {

        // 1. Get the email of the logged-in user from the Authentication object.
        String email = authentication.getName();

        // 2. Look up the user's ID.
        // First, try to find them in the Ngo repository.
        // If they aren't there, try the Hotel repository.
        // If they aren't in either, something is wrong, so we throw an error.
        Long userId = ngoRepository.findByEmail(email)
                .map(ngo -> ngo.getId()) // If found as NGO, get their ID
                .orElseGet(() -> hotelRepository.findByEmail(email)
                        .map(hotel -> hotel.getId()) // If found as Hotel, get their ID
                        .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found in database: " + email))
                );


        // 3. Call the updated service method, passing BOTH the hotelId and the found userId.
        List<FoodItemDto> foodItems = foodItemService.getFoodItemsByHotel(hotelId, userId);

        // 4. Return the "smarter" list to the frontend.
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
    // Add this endpoint to FoodItemController class:

    @GetMapping("/hotel/{hotelId}/today")
    public ResponseEntity<List<FoodItemDto>> getTodaysFoodItemsByHotel(@PathVariable Long hotelId) {
        List<FoodItemDto> foodItems = foodItemService.getTodaysFoodItemsByHotel(hotelId);
        return ResponseEntity.ok(foodItems);
    }
}