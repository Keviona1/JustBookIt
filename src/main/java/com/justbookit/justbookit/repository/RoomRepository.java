package com.justbookit.justbookit.repository;

import com.justbookit.justbookit.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);
    List<Room> findByHotelIdAndAvailable(Long hotelId, boolean available);
    List<Room> findByTypeAndAvailable(String type, boolean available);
}
