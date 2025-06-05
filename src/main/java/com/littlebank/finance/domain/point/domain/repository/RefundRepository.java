package com.littlebank.finance.domain.point.domain.repository;

import com.littlebank.finance.domain.point.domain.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Long>, CustomRefundRepository {

    // Admin
    @Query("SELECT r FROM Refund r JOIN FETCH r.user")
    List<Refund> findAllFetchJoinUser();
}
