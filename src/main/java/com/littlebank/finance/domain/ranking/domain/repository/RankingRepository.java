package com.littlebank.finance.domain.ranking.domain.repository;


import com.littlebank.finance.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<User, Long>, CustomRankingRepository {
}
