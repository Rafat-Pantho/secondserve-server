package com.secondserve.server.service;

import com.secondserve.server.dto.HotelDto;
import com.secondserve.server.entity.Hotel;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // --- ADDED: For transactional integrity ---
import java.util.List;
import java.util.Random; // --- ADDED: For generating the random part of the hotel code ---
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- (The getAllActiveHotels and getHotelById methods are fine and need no changes) ---
    public List<HotelDto> getAllActiveHotels() {
        return hotelRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public HotelDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        return convertToDto(hotel);
    }


    /**
     * MODIFIED: This method now handles the full registration logic, including hotel_code generation.
     */
    @Transactional // Ensures that if any part fails, the whole operation is rolled back.
    public HotelDto registerHotel(HotelDto hotelDto) {
        if (hotelRepository.existsByEmail(hotelDto.getEmail())) {
            throw new RuntimeException("Error: Email address is already in use.");
        }

        Hotel newHotel = convertToEntity(hotelDto);

        // Securely encode the user's password before saving.
        newHotel.setPassword(passwordEncoder.encode(hotelDto.getPassword()));

        // Generate and set the unique, shareable hotel code.
        newHotel.setHotelCode(generateUniqueHotelCode());

        Hotel savedHotel = hotelRepository.save(newHotel);

        // Return the DTO, which now includes the generated hotel_code.
        return convertToDto(savedHotel);
    }

    /**
     * ADDED: This helper method generates a unique, random hotel code in the format "HTL-XXX".
     * It ensures the code is not already in use by checking the database.
     */
    private String generateUniqueHotelCode() {
        String code;
        Random random = new Random();
        do {
            int number = 100 + random.nextInt(900); // Generates a random number between 100 and 999.
            code = "HTL-" + number;
        } while (hotelRepository.existsByHotelCode(code)); // Guarantees uniqueness.

        return code;
    }


    // --- (The updateHotel and deleteHotel methods are fine for now) ---
    public HotelDto updateHotel(Long id, HotelDto hotelDto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        // ... update logic here, ensuring to only update fields that are meant to be changed ...
        hotel.setManagerName(hotelDto.getManagerName());
        hotel.setPhone(hotelDto.getPhone());
        hotel.setAddress(hotelDto.getAddress());

        Hotel updatedHotel = hotelRepository.save(hotel);
        return convertToDto(updatedHotel);
    }

    public void deleteHotel(Long id) {
        // This is more of a "deactivate" which is good practice.
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        hotel.setIsActive(false);
        hotelRepository.save(hotel);
    }

    // This method is fine, used by AuthService for login
    public Hotel findByEmail(String email) {
        return hotelRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with email: " + email));
    }


    // --- MODIFIED: The helper methods now include the new fields ---
    private HotelDto convertToDto(Hotel hotel) {
        HotelDto dto = new HotelDto();
        dto.setId(hotel.getId());
        dto.setHotelName(hotel.getHotelName());
        dto.setManagerName(hotel.getManagerName());
        dto.setEmail(hotel.getEmail());
        dto.setPhone(hotel.getPhone());
        dto.setAddress(hotel.getAddress());
        // Add the new fields
        dto.setHotelLicense(hotel.getHotelLicense());
        dto.setHotelCode(hotel.getHotelCode());
        // The password should never be sent back to the client

        return dto;
    }

    private Hotel convertToEntity(HotelDto dto) {
        Hotel hotel = new Hotel();
        hotel.setHotelName(dto.getHotelName());
        hotel.setManagerName(dto.getManagerName());
        hotel.setEmail(dto.getEmail());
        hotel.setPhone(dto.getPhone());
        hotel.setAddress(dto.getAddress());
        // Add the new field
        hotel.setHotelLicense(dto.getHotelLicense());
        // Note: We do NOT set password or hotel_code here. That is handled by the main `registerHotel` method.

        return hotel;
    }
}