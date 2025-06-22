package com.justbookit.justbookit.repository;

import com.justbookit.justbookit.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByHotelId(Long hotelId);
    List<Review> findByUserId(Long userId);
    List<Review> findByHotelIdOrderByCreatedAtDesc(Long hotelId);
}
