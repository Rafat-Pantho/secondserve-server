package com.secondserve.server.repository;

import com.secondserve.server.entity.FoodRequest;
import com.secondserve.server.entity.FoodRequest.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // --- ADDED IMPORT ---
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime; // --- ADDED IMPORT ---
import java.util.List;

@Repository
public interface FoodRequestRepository extends JpaRepository<FoodRequest, Long> {

    List<FoodRequest> findByNgoIdOrderByRequestDateDesc(Long ngoId);
    List<FoodRequest> findByFoodItemHotelIdOrderByRequestDateDesc(Long hotelId);
    List<FoodRequest> findByRequestStatus(RequestStatus status);

    @Query("SELECT fr FROM FoodRequest fr WHERE fr.foodItem.hotel.id = ?1 AND fr.requestStatus = 'PENDING' ORDER BY fr.requestDate ASC")
    List<FoodRequest> findPendingRequestsForHotel(Long hotelId);

    @Query("SELECT COALESCE(SUM(fr.requestedQuantity), 0.0) FROM FoodRequest fr WHERE fr.foodItem.hotel.id = :hotelId AND fr.requestStatus = 'COMPLETED' AND fr.requestDate >= :startDate")
    Double sumDonatedQuantityByHotelAndDateRange(@Param("hotelId") Long hotelId, @Param("startDate") LocalDateTime startDate);
}