package com.littlebank.finance.domain.payment.domain.repository;

import com.littlebank.finance.domain.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByImpUid(String impUid);
}
