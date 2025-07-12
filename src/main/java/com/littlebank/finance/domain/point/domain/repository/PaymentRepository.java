package com.littlebank.finance.domain.point.domain.repository;

import com.littlebank.finance.domain.point.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long>, CustomPaymentRepository {
    // Admin
    @Query("SELECT p FROM Payment p JOIN FETCH p.user")
    List<Payment> findAllFetchJoinUser();
}
