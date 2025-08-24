package com.secondserve.server.dto;

import com.secondserve.server.entity.FoodItem.FoodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FoodItemDto {
    private Long id;
    private Long hotelId;
    private String hotelName;

    @NotBlank(message = "Food name is required")
    private String foodName;

    private FoodType foodType;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    private BigDecimal amountInKg;

    private String unit;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    @NotNull(message = "Preparation time is required")
    private LocalDateTime preparationTime;

    private String description;
    private Boolean isAvailable;
    private LocalDateTime createdDate;

    // Constructors
    public FoodItemDto() {}

    public FoodItemDto(String foodName, FoodType foodType, BigDecimal quantity, String unit, LocalDate expiryDate, LocalDateTime preparationTime) {
        this.foodName = foodName;
        this.foodType = foodType;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.preparationTime = preparationTime;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHotelId() { return hotelId; }
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public FoodType getFoodType() { return foodType; }
    public void setFoodType(FoodType foodType) { this.foodType = foodType; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getAmountInKg() { return amountInKg; }
    public void setAmountInKg(BigDecimal amountInKg) { this.amountInKg = amountInKg; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public LocalDateTime getPreparationTime() { return preparationTime; }
    public void setPreparationTime(LocalDateTime preparationTime) { this.preparationTime = preparationTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}