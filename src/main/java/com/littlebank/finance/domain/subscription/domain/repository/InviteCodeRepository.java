package com.littlebank.finance.domain.subscription.domain.repository;

import com.littlebank.finance.domain.subscription.domain.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InviteCodeRepository extends JpaRepository<InviteCode, Long> {
    Optional <InviteCode> findByCodeAndUsedFalse(String code);

    Optional<InviteCode> findFirstBySubscriptionIdAndUsedFalseOrderByIdAsc(Long id);

    @Query("SELECT i FROM InviteCode i WHERE i.subscription.owner.id = :ownerId")
    List<InviteCode> findAllByOwnerId(@Param("ownerId") Long ownerId);

}
