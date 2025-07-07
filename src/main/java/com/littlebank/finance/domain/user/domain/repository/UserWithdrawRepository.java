package com.littlebank.finance.domain.user.domain.repository;


import com.littlebank.finance.domain.user.domain.UserWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWithdrawRepository extends JpaRepository<UserWithdraw, Long> {
}
