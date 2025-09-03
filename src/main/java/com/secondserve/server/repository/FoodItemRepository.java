package com.secondserve.server.repository;

import com.secondserve.server.entity.FoodItem;
import com.secondserve.server.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime; // --- ADDED IMPORT ---
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    List<FoodItem> findByIsAvailableTrueOrderByCreatedDateDesc();
    List<FoodItem> findByHotelAndIsAvailableTrue(Hotel hotel);
    List<FoodItem> findByHotelIdAndIsAvailableTrue(Long hotelId);
    List<FoodItem> findByHotelAndIsAvailableFalseOrderByCreatedDateDesc(Hotel hotel);

    @Query("SELECT f FROM FoodItem f WHERE f.isAvailable = true AND f.expiryDate > :currentDate ORDER BY f.expiryDate ASC")
    List<FoodItem> findAvailableAndNotExpired(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT COALESCE(SUM(fi.quantity), 0.0) FROM FoodItem fi WHERE fi.hotel.id = :hotelId AND fi.createdDate >= :startDate")
    Double sumQuantityByHotelAndDateRange(@Param("hotelId") Long hotelId, @Param("startDate") LocalDateTime startDate);
}