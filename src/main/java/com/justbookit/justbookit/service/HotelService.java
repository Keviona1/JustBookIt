package com.justbookit.justbookit.service;

import com.justbookit.justbookit.model.Hotel;
import com.justbookit.justbookit.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    public List<Hotel> searchHotels(String location, Integer minStars, Integer maxStars) {
        return hotelRepository.findByLocationContainingAndStarsBetween(location, minStars, maxStars);
    }
}

