package com.justbookit.justbookit.controller;// src/main/java/com/justbookit/controller/ReservationController.java
import com.justbookit.justbookit.dto.ReservationRequest;
import com.justbookit.justbookit.model.Reservation;
import com.justbookit.justbookit.model.Room;
import com.justbookit.justbookit.model.User;
import com.justbookit.justbookit.repository.ReservationRepository;
import com.justbookit.justbookit.repository.RoomRepository;
import com.justbookit.justbookit.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    private final UserRepository userRepository;
    public ReservationController(ReservationRepository reservationRepository,
                                 RoomRepository roomRepository,
                                 UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }


    @PostMapping
    public ResponseEntity<Reservation> bookRoom(@RequestBody ReservationRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Room roomToBook = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!isRoomAvailable(request.getRoomId(), request.getCheckInDate(), request.getCheckOutDate())) {
            return ResponseEntity.badRequest().body(null);
        }

        Reservation newReservation = new Reservation();
        newReservation.setUser(currentUser);
        newReservation.setRoom(roomToBook);
        newReservation.setCheckInDate(request.getCheckInDate());
        newReservation.setCheckOutDate(request.getCheckOutDate());
        newReservation.setStatus("CONFIRMED");

        Reservation savedReservation = reservationRepository.save(newReservation);

        return ResponseEntity.ok(savedReservation);
    }

    private boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(roomId, checkIn, checkOut);
        return conflictingReservations.isEmpty();
    }

    @GetMapping("/my-reservations")
    public ResponseEntity<List<Reservation>> getUserReservations() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        List<Reservation> reservations = reservationRepository.findByUserIdOrderByCheckInDateDesc(currentUser.getId());
        return ResponseEntity.ok(reservations);
    }
    @GetMapping("/availability/{roomId}")
    public ResponseEntity<List<LocalDate>> getAvailableDates(@PathVariable Long roomId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<LocalDate> bookedDates = reservationRepository.findBookedDatesByRoomId(roomId, startDate, endDate);
        List<LocalDate> allDates = startDate.datesUntil(endDate.plusDays(1)).toList();
        List<LocalDate> availableDates = allDates.stream()
                .filter(date -> !bookedDates.contains(date))
                .toList();
        return ResponseEntity.ok(availableDates);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));

        if (!reservation.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to cancel this reservation.");
        }



        reservationRepository.delete(reservation);

        return ResponseEntity.noContent().build();
    }


}