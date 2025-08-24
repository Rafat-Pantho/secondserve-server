package com.secondserve.server.service;

import com.secondserve.server.dto.HotelDto;
import com.secondserve.server.entity.Hotel;
import com.secondserve.server.exception.ResourceNotFoundException;
import com.secondserve.server.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public HotelDto createHotel(HotelDto hotelDto) {
        if (hotelRepository.existsByEmail(hotelDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Hotel hotel = convertToEntity(hotelDto);
        hotel.setPassword(passwordEncoder.encode(hotelDto.getPassword()));
        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToDto(savedHotel);
    }

    public HotelDto updateHotel(Long id, HotelDto hotelDto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        hotel.setHotelName(hotelDto.getHotelName());
        hotel.setManagerName(hotelDto.getManagerName());
        hotel.setPhone(hotelDto.getPhone());
        hotel.setAddress(hotelDto.getAddress());
        hotel.setCity(hotelDto.getCity());
        hotel.setState(hotelDto.getState());
        hotel.setPostalCode(hotelDto.getPostalCode());

        Hotel updatedHotel = hotelRepository.save(hotel);
        return convertToDto(updatedHotel);
    }

    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));
        hotel.setIsActive(false);
        hotelRepository.save(hotel);
    }

    public Hotel findByEmail(String email) {
        return hotelRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with email: " + email));
    }

    private HotelDto convertToDto(Hotel hotel) {
        HotelDto dto = new HotelDto();
        dto.setId(hotel.getId());
        dto.setHotelName(hotel.getHotelName());
        dto.setManagerName(hotel.getManagerName());
        dto.setEmail(hotel.getEmail());
        dto.setPhone(hotel.getPhone());
        dto.setAddress(hotel.getAddress());
        dto.setCity(hotel.getCity());
        dto.setState(hotel.getState());
        dto.setPostalCode(hotel.getPostalCode());
        dto.setRegistrationDate(hotel.getRegistrationDate());
        dto.setIsActive(hotel.getIsActive());
        dto.setTotalFoodDonated(hotel.getTotalFoodDonated());
        return dto;
    }

    private Hotel convertToEntity(HotelDto dto) {
        Hotel hotel = new Hotel();
        hotel.setHotelName(dto.getHotelName());
        hotel.setManagerName(dto.getManagerName());
        hotel.setEmail(dto.getEmail());
        hotel.setPassword(dto.getPassword());
        hotel.setPhone(dto.getPhone());
        hotel.setAddress(dto.getAddress());
        hotel.setCity(dto.getCity());
        hotel.setState(dto.getState());
        hotel.setPostalCode(dto.getPostalCode());
        return hotel;
    }
}