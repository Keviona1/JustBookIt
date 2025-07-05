package com.justbookit.justbookit.controller;

import com.justbookit.justbookit.dto.HotelSearchDTO;
import com.justbookit.justbookit.model.Hotel;
import com.justbookit.justbookit.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotel(@PathVariable Long id) {
        return hotelService.getHotelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/search")
    public List<Hotel> searchHotels(@RequestBody HotelSearchDTO dto) {
        return hotelService.searchHotels(dto.getLocation(), dto.getMinStars(), dto.getMaxStars());
    }
}
