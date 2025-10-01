package com.secondserve.server.service;

import java.time.LocalDateTime;
import com.secondserve.server.dto.FoodItemDto;
import com.secondserve.server.entity.FoodItem;
import com.secondserve.server.entity.FoodRequest;
import com.secondserve.server.entity.Hotel;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.FoodItemRepository;
import com.secondserve.server.repository.FoodRequestRepository;
import com.secondserve.server.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private FoodRequestRepository foodRequestRepository;
    public List<FoodItemDto> getAllAvailableFoodItems() {
        return foodItemRepository.findAvailableAndNotExpired(LocalDate.now())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FoodItemDto> getFoodItemsByHotel(Long hotelId,Long ngoId) {
        List<FoodItem> foodItems = foodItemRepository.findByHotelIdAndIsAvailableTrue(hotelId);
        return foodItems.stream().map(foodItem -> {
            // A. Start by converting the basic entity to DTO using your existing helper
            FoodItemDto dto = convertToDto(foodItem);

            // B. Check for an existing request from this NGO for this item
            Optional<FoodRequest> existingRequest = foodRequestRepository
                    .findTopByFoodItemIdAndNgoIdOrderByRequestDateDesc(foodItem.getId(), ngoId);

            // C. If a request exists, set its status on the DTO
            if (existingRequest.isPresent()) {
                String status = existingRequest.get().getRequestStatus().name();
                // We only care about statuses that mean the request is "active"
                if ("PENDING".equals(status) || "APPROVED".equals(status)) {
                    dto.setCurrentUserRequestStatus(status);
                }
            }
            return dto;

        }).collect(Collectors.toList());
    }

    

    public FoodItemDto getFoodItemById(Long id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));
        return convertToDto(foodItem);
    }

    public FoodItemDto createFoodItem(FoodItemDto foodItemDto, Long hotelIdOfLoggedInStaff) {
        Hotel hotel = hotelRepository.findById(hotelIdOfLoggedInStaff)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel for user not found with id: " + hotelIdOfLoggedInStaff));

        FoodItem foodItem = convertToEntity(foodItemDto);
        foodItem.setHotel(hotel);
        foodItem.setIsAvailable(false); // New items require manager approval

        FoodItem savedFoodItem = foodItemRepository.save(foodItem);
        return convertToDto(savedFoodItem);
    }

    // --- MODIFIED: This method now uses the new fields ---
    public FoodItemDto updateFoodItem(Long id, FoodItemDto foodItemDto) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));

        // Update with the new, correct fields
        foodItem.setFoodName(foodItemDto.getFoodName());
        foodItem.setQuantity(foodItemDto.getQuantity());
        foodItem.setUnit(foodItemDto.getUnit());
        foodItem.setExpiryDate(foodItemDto.getExpiryDate());
        foodItem.setDescription(foodItemDto.getDescription());
        foodItem.setCategory(foodItemDto.getCategory());
        foodItem.setCondition(foodItemDto.getCondition());

        // This would likely be handled by a manager, but is included for completeness
        if(foodItemDto.getIsAvailable() != null) {
            foodItem.setIsAvailable(foodItemDto.getIsAvailable());
        }

        FoodItem updatedFoodItem = foodItemRepository.save(foodItem);
        return convertToDto(updatedFoodItem);
    }

    public void markAsUnavailable(Long id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));
        foodItem.setIsAvailable(false);
        foodItemRepository.save(foodItem);
    }

    public void deleteFoodItem(Long id) {
        if (!foodItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Food item not found with id: " + id);
        }
        foodItemRepository.deleteById(id);
    }

 
    private FoodItemDto convertToDto(FoodItem foodItem) {
        FoodItemDto dto = new FoodItemDto();
        dto.setId(foodItem.getId());
        dto.setHotelId(foodItem.getHotel().getId());
        dto.setHotelName(foodItem.getHotel().getHotelName());
        dto.setFoodName(foodItem.getFoodName());
        dto.setQuantity(foodItem.getQuantity());
        dto.setUnit(foodItem.getUnit());
        dto.setExpiryDate(foodItem.getExpiryDate());
        dto.setDescription(foodItem.getDescription());
        dto.setCategory(foodItem.getCategory());
        dto.setCondition(foodItem.getCondition());
        dto.setIsAvailable(foodItem.getIsAvailable());
        dto.setCreatedDate(foodItem.getCreatedDate());
        return dto;
    }
    public void markAsAvailable(Long id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));

        // Here we would add security to check if the manager owns this item.

        foodItem.setIsAvailable(true); // Set the flag to true
        foodItemRepository.save(foodItem);
    }
   
    private FoodItem convertToEntity(FoodItemDto dto) {
        FoodItem foodItem = new FoodItem();
        foodItem.setFoodName(dto.getFoodName());
        foodItem.setQuantity(dto.getQuantity());
        foodItem.setUnit(dto.getUnit());
        foodItem.setExpiryDate(dto.getExpiryDate());
        foodItem.setDescription(dto.getDescription());
        foodItem.setCategory(dto.getCategory());
        foodItem.setCondition(dto.getCondition());
        return foodItem;
    }
    public List<FoodItemDto> getFoodItemsByHotelPending(Long hotelId) {
        return foodItemRepository.findByHotelIdAndIsAvailableFalse(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<FoodItemDto> getPendingFoodItemsByHotel(Long hotelId) {
        return foodItemRepository.findByHotelIdAndIsAvailableFalse(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    

    public List<FoodItemDto> getTodaysFoodItemsByHotel(Long hotelId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return foodItemRepository.findByHotelIdAndCreatedDateToday(hotelId, startOfDay, endOfDay)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}