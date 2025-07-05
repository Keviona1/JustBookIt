package com.justbookit.justbookit.controller;

import com.justbookit.justbookit.dto.ReservationDTO;
import com.justbookit.justbookit.model.Reservation;
import com.justbookit.justbookit.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Reservation> makeReservation(@RequestBody ReservationDTO dto, @PathVariable Long userId) {
        Reservation reservation = reservationService.createReservation(dto, userId);
        return ResponseEntity.ok(reservation);
    }
}

