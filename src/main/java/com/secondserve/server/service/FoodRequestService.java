package com.secondserve.server.service;

import com.secondserve.server.dto.FoodRequestDto;
import com.secondserve.server.entity.FoodItem;
import com.secondserve.server.entity.FoodRequest;
import com.secondserve.server.entity.FoodRequest.RequestStatus;
import com.secondserve.server.entity.Ngo;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.FoodItemRepository;
import com.secondserve.server.repository.FoodRequestRepository;
import com.secondserve.server.repository.NgoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodRequestService {

    @Autowired
    private FoodRequestRepository foodRequestRepository;

    @Autowired
    private NgoRepository ngoRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    public List<FoodRequestDto> getRequestsByNgo(Long ngoId) {
        return foodRequestRepository.findByNgoIdOrderByRequestDateDesc(ngoId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FoodRequestDto> getRequestsForHotel(Long hotelId) {
        return foodRequestRepository.findByFoodItemHotelIdOrderByRequestDateDesc(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FoodRequestDto> getPendingRequestsForHotel(Long hotelId) {
        return foodRequestRepository.findPendingRequestsForHotel(hotelId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public FoodRequestDto getFoodRequestById(Long id) {
        FoodRequest foodRequest = foodRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food request not found with id: " + id));
        return convertToDto(foodRequest);
    }

    public FoodRequestDto createFoodRequest(FoodRequestDto foodRequestDto) {
        Ngo ngo = ngoRepository.findById(foodRequestDto.getNgoId())
                .orElseThrow(() -> new ResourceNotFoundException("NGO not found with id: " + foodRequestDto.getNgoId()));

        FoodItem foodItem = foodItemRepository.findById(foodRequestDto.getFoodItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + foodRequestDto.getFoodItemId()));

        if (!foodItem.getIsAvailable()) {
            throw new RuntimeException("Food item is no longer available");
        }

        FoodRequest foodRequest = convertToEntity(foodRequestDto);
        foodRequest.setNgo(ngo);
        foodRequest.setFoodItem(foodItem);

        FoodRequest savedRequest = foodRequestRepository.save(foodRequest);
        return convertToDto(savedRequest);
    }

    public FoodRequestDto updateRequestStatus(Long id, RequestStatus status) {
        FoodRequest foodRequest = foodRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food request not found with id: " + id));

        foodRequest.setRequestStatus(status);

        // If approved, mark food item as unavailable
        if (status == RequestStatus.APPROVED) {
            FoodItem foodItem = foodRequest.getFoodItem();
            foodItem.setIsAvailable(false);
            foodItemRepository.save(foodItem);
        }

        FoodRequest updatedRequest = foodRequestRepository.save(foodRequest);
        return convertToDto(updatedRequest);
    }

    public FoodRequestDto updateFoodRequest(Long id, FoodRequestDto foodRequestDto) {
        FoodRequest foodRequest = foodRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food request not found with id: " + id));

        foodRequest.setRequestedQuantity(foodRequestDto.getRequestedQuantity());
        foodRequest.setPickupDate(foodRequestDto.getPickupDate());
        foodRequest.setNotes(foodRequestDto.getNotes());

        FoodRequest updatedRequest = foodRequestRepository.save(foodRequest);
        return convertToDto(updatedRequest);
    }

    public void deleteFoodRequest(Long id) {
        FoodRequest foodRequest = foodRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food request not found with id: " + id));
        foodRequestRepository.delete(foodRequest);
    }

    private FoodRequestDto convertToDto(FoodRequest foodRequest) {
        FoodRequestDto dto = new FoodRequestDto();
        dto.setId(foodRequest.getId());
        dto.setNgoId(foodRequest.getNgo().getId());
        dto.setNgoName(foodRequest.getNgo().getNgoName());
        dto.setFoodItemId(foodRequest.getFoodItem().getId());
        dto.setFoodItemName(foodRequest.getFoodItem().getFoodName());
        dto.setHotelName(foodRequest.getFoodItem().getHotel().getHotelName());
        dto.setRequestedQuantity(foodRequest.getRequestedQuantity());
        dto.setRequestStatus(foodRequest.getRequestStatus());
        dto.setRequestDate(foodRequest.getRequestDate());
        dto.setPickupDate(foodRequest.getPickupDate());
        dto.setNotes(foodRequest.getNotes());
        return dto;
    }

    private FoodRequest convertToEntity(FoodRequestDto dto) {
        FoodRequest foodRequest = new FoodRequest();
        foodRequest.setRequestedQuantity(dto.getRequestedQuantity());
        foodRequest.setPickupDate(dto.getPickupDate());
        foodRequest.setNotes(dto.getNotes());
        return foodRequest;
    }
}
