package com.littlebank.finance.domain.challenge.domain.repository;

import com.littlebank.finance.domain.challenge.domain.ChallengeParticipation;
import com.littlebank.finance.domain.challenge.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChallengeParticipationRepository extends JpaRepository<ChallengeParticipation, Long> {
    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);

    @Query("SELECT COUNT(cp) FROM ChallengeParticipation cp " +
            "WHERE cp.challenge.id = :challengeId " +
            "AND cp.status IN (:statuses) " +
            "AND cp.isDeleted = false")
    int countByChallengeIdAndStatuses(@Param("challengeId") Long challengeId,
                                      @Param("statuses") List<Status> statuses);}
