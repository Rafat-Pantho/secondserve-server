package com.secondserve.server.repository;

import com.secondserve.server.entity.FoodRequest;
import com.secondserve.server.entity.FoodRequest.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodRequestRepository extends JpaRepository<FoodRequest, Long> {

    // Gets the full request history for a specific NGO (for their "My Requests" page)
    List<FoodRequest> findByNgoIdOrderByRequestDateDesc(Long ngoId);

    // Gets the full request history for a specific Hotel
    List<FoodRequest> findByFoodItemHotelIdOrderByRequestDateDesc(Long hotelId);

    // Gets pending requests for a specific Hotel (for their "Donation Requests" dashboard)
    @Query("SELECT fr FROM FoodRequest fr WHERE fr.foodItem.hotel.id = ?1 AND fr.requestStatus = 'PENDING' ORDER BY fr.requestDate ASC")
    List<FoodRequest> findPendingRequestsForHotel(Long hotelId);
}