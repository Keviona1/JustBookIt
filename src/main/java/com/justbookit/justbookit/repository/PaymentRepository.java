package com.justbookit.justbookit.repository;

import com.justbookit.justbookit.model.Payment;
import com.justbookit.justbookit.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByReservationId(Long reservationId);
    List<Payment> findByPaymentStatus(PaymentStatus status);
}
