package com.littlebank.finance.domain.challenge.domain.repository;

import com.littlebank.finance.domain.challenge.domain.Challenge;
import com.littlebank.finance.domain.challenge.domain.ChallengeCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepositoryCustom {
    Page<Challenge> findAllByCategory(ChallengeCategory challengeCategory, Pageable pageable);

}
