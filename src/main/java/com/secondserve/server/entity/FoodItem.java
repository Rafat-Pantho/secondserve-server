package com.secondserve.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "food_items_table")
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @NotBlank
    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(name = "food_type")
    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    @NotNull
    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(name = "amount_in_kg")
    private BigDecimal amountInKg;

    private String unit;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @NotNull
    @Column(name = "preparation_time", nullable = false)
    private LocalDateTime preparationTime;

    private String description;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FoodRequest> foodRequests;

    public enum FoodType {
        VEGETARIAN, NON_VEGETARIAN, VEGAN
    }

    // Constructors
    public FoodItem() {}

    public FoodItem(Hotel hotel, String foodName, FoodType foodType, BigDecimal quantity, String unit, LocalDate expiryDate, LocalDateTime preparationTime) {
        this.hotel = hotel;
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

    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }

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

    public List<FoodRequest> getFoodRequests() { return foodRequests; }
    public void setFoodRequests(List<FoodRequest> foodRequests) { this.foodRequests = foodRequests; }
}
