package com.secondserve.server.dto;

import com.secondserve.server.entity.FoodItem; // Required for using the Enums
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This DTO is used to transfer FoodItem data between the client and server.
 * It has been modified to exactly match the fields provided by the Kitchen Staff's FXML form.
 */
public class FoodItemDto {

    // These fields are mostly used for sending data FROM server TO client
    private Long id;
    private Long hotelId; // Can be removed from request DTO, but useful for response
    private String hotelName;
    private Boolean isAvailable;
    private LocalDateTime createdDate;

    // --- These fields PERFECTLY MATCH the data sent FROM the Kitchen Staff's FXML ---
    @NotBlank(message = "Food name is required")
    private String foodName;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    @NotBlank(message = "Unit is required") // Unit is now essential
    private String unit;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    // Optional notes field
    private String description;

    // --- ADDED: These fields replace the old 'foodType' ---
    @NotNull(message = "Category is required")
    private FoodItem.Category category;

    @NotNull(message = "Condition is required")
    private FoodItem.Condition condition;

    // --- DELETED: The following fields have been removed as they are no longer in the database or FXML ---
    // - private FoodType foodType;
    // - private BigDecimal amountInKg;
    // - private LocalDateTime preparationTime;


    // --- Constructors, Getters, and Setters ---
    // A no-argument constructor is required for JSON deserialization
    public FoodItemDto() {}

    // Getters and Setters for all remaining fields...
    // You can generate these in your IDE (Alt+Insert or Right Click -> Generate)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHotelId() { return hotelId; }
    // Note: We don't need a setHotelId because the server should determine this, not the client.
    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public FoodItem.Category getCategory() { return category; }
    public void setCategory(FoodItem.Category category) { this.category = category; }

    public FoodItem.Condition getCondition() { return condition; }
    public void setCondition(FoodItem.Condition condition) { this.condition = condition; }
}