package com.secondserve.server.repository;

import com.secondserve.server.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Hotel> findByIsActiveTrue();

    @Query("SELECT h FROM Hotel h WHERE h.city = ?1 AND h.isActive = true")
    List<Hotel> findActiveByCityy(String city);
}