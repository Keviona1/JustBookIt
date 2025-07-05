package com.justbookit.justbookit.service;

import com.justbookit.justbookit.dto.ReservationDTO;
import com.justbookit.justbookit.model.Reservation;
import com.justbookit.justbookit.model.Room;
import com.justbookit.justbookit.model.User;
import com.justbookit.justbookit.repository.ReservationRepository;
import com.justbookit.justbookit.repository.RoomRepository;
import com.justbookit.justbookit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public Reservation createReservation(ReservationDTO dto, Long userId) {
        Room room = roomRepository.findById(dto.getRoomId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Reservation reservation = new Reservation();
        reservation.setRoom(room);
        reservation.setUser(user);
        reservation.setCheckInDate(Date.valueOf(dto.getCheckInDate()));
        reservation.setCheckOutDate(Date.valueOf(dto.getCheckOutDate()));
        reservation.setGuests(dto.getGuests());
        reservation.setTotalPrice(room.getPricePerNight() * calculateDays(dto));
        reservation.setStatus("CONFIRMED");

        room.setAvailable(false);
        roomRepository.save(room);

        return reservationRepository.save(reservation);
    }

    private long calculateDays(ReservationDTO dto) {
        LocalDate start = LocalDate.parse(dto.getCheckInDate());
        LocalDate end = LocalDate.parse(dto.getCheckOutDate());
        return ChronoUnit.DAYS.between(start, end);
    }
}

