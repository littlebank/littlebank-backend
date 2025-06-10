package com.littlebank.finance.domain.subscription.domain.repository;

import com.littlebank.finance.domain.subscription.domain.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteCodeRepository extends JpaRepository<InviteCode, Long> {
}
