package com.littlebank.finance.domain.point.domain.repository;

import com.littlebank.finance.domain.point.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>, CustomPaymentRepository {
}
