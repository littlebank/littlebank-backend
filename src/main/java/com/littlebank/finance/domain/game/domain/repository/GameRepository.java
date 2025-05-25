package com.littlebank.finance.domain.game.domain.repository;


import com.littlebank.finance.domain.game.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game,Long> {
}
