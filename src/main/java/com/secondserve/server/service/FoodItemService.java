package com.secondserve.server.service;

import com.secondserve.server.dto.FoodItemDto;
import com.secondserve.server.entity.FoodItem;
import com.secondserve.server.entity.Hotel;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.FoodItemRepository;
import com.secondserve.server.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public List<FoodItemDto> getAllAvailableFoodItems() {
        return foodItemRepository.findAvailableAndNotExpired(LocalDate.now())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FoodItemDto> getFoodItemsByHotel(Long hotelId) {
        return foodItemRepository.findByHotelIdAndIsAvailableTrue(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FoodItemDto> getAvailableFoodItemsByCity(String city) {
        return foodItemRepository.findAvailableByCity(city, LocalDate.now())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public FoodItemDto getFoodItemById(Long id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));
        return convertToDto(foodItem);
    }

    public FoodItemDto createFoodItem(FoodItemDto foodItemDto) {
        Hotel hotel = hotelRepository.findById(foodItemDto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + foodItemDto.getHotelId()));

        FoodItem foodItem = convertToEntity(foodItemDto);
        foodItem.setHotel(hotel);
        FoodItem savedFoodItem = foodItemRepository.save(foodItem);
        return convertToDto(savedFoodItem);
    }

    public FoodItemDto updateFoodItem(Long id, FoodItemDto foodItemDto) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));

        foodItem.setFoodName(foodItemDto.getFoodName());
        foodItem.setFoodType(foodItemDto.getFoodType());
        foodItem.setQuantity(foodItemDto.getQuantity());
        foodItem.setAmountInKg(foodItemDto.getAmountInKg());
        foodItem.setUnit(foodItemDto.getUnit());
        foodItem.setExpiryDate(foodItemDto.getExpiryDate());
        foodItem.setPreparationTime(foodItemDto.getPreparationTime());
        foodItem.setDescription(foodItemDto.getDescription());
        foodItem.setIsAvailable(foodItemDto.getIsAvailable());

        FoodItem updatedFoodItem = foodItemRepository.save(foodItem);
        return convertToDto(updatedFoodItem);
    }

    public void deleteFoodItem(Long id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));
        foodItem.setIsAvailable(false);
        foodItemRepository.save(foodItem);
    }

    public void markAsUnavailable(Long id) {
        FoodItem foodItem = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));
        foodItem.setIsAvailable(false);
        foodItemRepository.save(foodItem);
    }

    private FoodItemDto convertToDto(FoodItem foodItem) {
        FoodItemDto dto = new FoodItemDto();
        dto.setId(foodItem.getId());
        dto.setHotelId(foodItem.getHotel().getId());
        dto.setHotelName(foodItem.getHotel().getHotelName());
        dto.setFoodName(foodItem.getFoodName());
        dto.setFoodType(foodItem.getFoodType());
        dto.setQuantity(foodItem.getQuantity());
        dto.setAmountInKg(foodItem.getAmountInKg());
        dto.setUnit(foodItem.getUnit());
        dto.setExpiryDate(foodItem.getExpiryDate());
        dto.setPreparationTime(foodItem.getPreparationTime());
        dto.setDescription(foodItem.getDescription());
        dto.setIsAvailable(foodItem.getIsAvailable());
        dto.setCreatedDate(foodItem.getCreatedDate());
        return dto;
    }

    private FoodItem convertToEntity(FoodItemDto dto) {
        FoodItem foodItem = new FoodItem();
        foodItem.setFoodName(dto.getFoodName());
        foodItem.setFoodType(dto.getFoodType());
        foodItem.setQuantity(dto.getQuantity());
        foodItem.setAmountInKg(dto.getAmountInKg());
        foodItem.setUnit(dto.getUnit());
        foodItem.setExpiryDate(dto.getExpiryDate());
        foodItem.setPreparationTime(dto.getPreparationTime());
        foodItem.setDescription(dto.getDescription());
        return foodItem;
    }
}