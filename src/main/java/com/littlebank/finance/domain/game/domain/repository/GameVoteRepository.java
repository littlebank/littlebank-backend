package com.littlebank.finance.domain.game.domain.repository;

import com.littlebank.finance.domain.game.domain.Game;
import com.littlebank.finance.domain.game.domain.GameVote;
import com.littlebank.finance.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameVoteRepository extends JpaRepository<GameVote,Long> {
    boolean existsByGameAndUser(Game game, User user);

    GameVote findByGameAndUser(Game game, User user);
}
