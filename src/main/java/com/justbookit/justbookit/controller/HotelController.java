package com.justbookit.justbookit.controller;

import com.justbookit.justbookit.dto.HotelDTO;
import com.justbookit.justbookit.dto.HotelSearchDTO;
import com.justbookit.justbookit.dto.RoomDTO;
import com.justbookit.justbookit.model.Hotel;
import com.justbookit.justbookit.model.Room;
import com.justbookit.justbookit.repository.HotelRepository;
import com.justbookit.justbookit.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;
private final HotelRepository hotelRepository;
    public HotelController(HotelService hotelService, HotelRepository hotelRepository) {
        this.hotelService = hotelService;
        this.hotelRepository = hotelRepository;
    }

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody HotelDTO hotelDTO) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelDTO.getName());
        hotel.setDescription(hotelDTO.getDescription());
        hotel.setLocation(hotelDTO.getLocation());
        hotel.setAddress(hotelDTO.getAddress());
        hotel.setStars(hotelDTO.getStars());
        hotel.setAmenities(hotelDTO.getAmenities());

        if (hotelDTO.getRooms() != null) {
            for (RoomDTO roomDTO : hotelDTO.getRooms()) {
                Room room = new Room();
                room.setRoomNumber(roomDTO.getRoomNumber());
                room.setType(roomDTO.getType());
                room.setCapacity(roomDTO.getCapacity());
                room.setPricePerNight(roomDTO.getPricePerNight());
                room.setFeatures(roomDTO.getFeatures());
                room.setAvailable(true);
                hotel.addRoom(room);
            }
        }

        Hotel savedHotel = hotelRepository.save(hotel);
        return ResponseEntity.ok(savedHotel);
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
