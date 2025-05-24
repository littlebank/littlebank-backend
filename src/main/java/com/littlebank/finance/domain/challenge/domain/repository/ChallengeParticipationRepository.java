package com.littlebank.finance.domain.challenge.domain.repository;

import com.littlebank.finance.domain.challenge.domain.ChallengeParticipation;
import com.littlebank.finance.domain.challenge.domain.ChallengeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChallengeParticipationRepository extends JpaRepository<ChallengeParticipation, Long> {
    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);

    @Query("SELECT COUNT(cp) FROM ChallengeParticipation cp " +
            "WHERE cp.challenge.id = :challengeId " +
            "AND cp.challengeStatus IN (:statuses) " +
            "AND cp.isDeleted = false")
    int countByChallengeIdAndStatuses(@Param("challengeId") Long challengeId,
                                      @Param("statuses") List<ChallengeStatus> challengeStatuses);

    @Query("SELECT cp FROM ChallengeParticipation cp " +
            "JOIN FETCH cp.challenge c " +
            "WHERE cp.user.id = :userId " +
            "AND cp.challengeStatus IN :statuses " +
            "AND cp.isDeleted = false " +
            "AND c.isDeleted = false")
    Page<ChallengeParticipation> findMyValidParticipations(
            @Param("userId") Long userId,
            @Param("statuses") List<ChallengeStatus> statuses,
            Pageable pageable);}
