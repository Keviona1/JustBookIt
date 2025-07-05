package com.justbookit.justbookit.repository;

import com.justbookit.justbookit.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByLocationContainingIgnoreCase(String location);
    List<Hotel> findByNameContainingIgnoreCase(String name);
    List<Hotel> findByStars(int stars);
    List<Hotel> findByLocationContainingAndStarsBetween(String location, Integer minStars, Integer maxStars);

}


