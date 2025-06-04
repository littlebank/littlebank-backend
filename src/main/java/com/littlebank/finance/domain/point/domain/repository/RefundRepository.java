package com.littlebank.finance.domain.point.domain.repository;

import com.littlebank.finance.domain.point.domain.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
}
