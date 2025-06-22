package com.justbookit.justbookit.repository;

import com.justbookit.justbookit.model.Reservation;
import com.justbookit.justbookit.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND " +
            "((r.checkInDate <= :checkOut AND r.checkOutDate >= :checkIn) AND " +
            "r.status != 'CANCELLED')")
    List<Reservation> findConflictingReservations(@Param("roomId") Long roomId,
                                                  @Param("checkIn") Date checkIn,
                                                  @Param("checkOut") Date checkOut);
}
