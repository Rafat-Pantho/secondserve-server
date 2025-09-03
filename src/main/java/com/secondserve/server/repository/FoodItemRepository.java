package com.secondserve.server.repository;

import com.secondserve.server.entity.FoodItem;
import com.secondserve.server.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findByIsAvailableTrueOrderByCreatedDateDesc();
    List<FoodItem> findByHotelAndIsAvailableTrue(Hotel hotel);
    List<FoodItem> findByHotelIdAndIsAvailableTrue(Long hotelId);

    @Query("SELECT f FROM FoodItem f WHERE f.isAvailable = true AND f.expiryDate > :currentDate ORDER BY f.expiryDate ASC")
    List<FoodItem> findAvailableAndNotExpired(@Param("currentDate") LocalDate currentDate);
    List<FoodItem> findByHotelAndIsAvailableFalseOrderByCreatedDateDesc(Hotel hotel);
//    @Query("SELECT f FROM FoodItem f WHERE f.hotel.city = :city AND f.isAvailable = true AND f.expiryDate > :currentDate")
//    List<FoodItem> findAvailableByCity(@Param("city") String city, @Param("currentDate") LocalDate currentDate);
}