package com.littlebank.finance.domain.game.service;

import com.littlebank.finance.domain.game.domain.Game;
import com.littlebank.finance.domain.game.domain.GameChoice;
import com.littlebank.finance.domain.game.domain.GameVote;
import com.littlebank.finance.domain.game.domain.repository.GameRepository;
import com.littlebank.finance.domain.game.domain.repository.GameVoteRepository;
import com.littlebank.finance.domain.game.dto.request.GameVoteRequestDto;
import com.littlebank.finance.domain.game.dto.response.GameMainResponseDto;
import com.littlebank.finance.domain.game.dto.response.GameVoteResponseDto;
import com.littlebank.finance.domain.game.exception.GameException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.redis.RedisPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final GameVoteRepository gameVoteRepository;
    private final UserRepository userRepository;
    private final RedissonClient redissonClient;
    public GameVoteResponseDto joinGame(Long userId, Long gameId, GameVoteRequestDto request) {
        String lockKey = RedisPolicy.GAME_VOTE_LOCK_PREFIX + gameId;
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(5, 3, TimeUnit.SECONDS); // 최대 5초 대기, 3초 보유
            if (!isLocked) {
                throw new GameException(ErrorCode.FAIL_TO_GET_LOCK);
            }

            Game game = gameRepository.findById(gameId)
                    .orElseThrow(() -> new GameException(ErrorCode.GAME_NOT_FOUND));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
            if (gameVoteRepository.existsByGameAndUser(game, user)) {
                throw new GameException(ErrorCode.ALREADY_VOTED);
            }

            GameVote vote = GameVote.builder()
                    .game(game)
                    .user(user)
                    .choice(request.getChoice())
                    .build();
            gameVoteRepository.save(vote);

            if (request.getChoice() == GameChoice.A) {
                game.setVote_a(game.getVote_a() + 1);
            } else {
                game.setVote_b(game.getVote_b() + 1);
            }

            gameRepository.save(game);

            return GameVoteResponseDto.of(
                    game.getId(),
                    request.getChoice(),
                    game.getVote_a(),
                    game.getVote_b()
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락 획득 중 인터럽트 발생", e);
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public List<GameMainResponseDto> getGames(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        List<Game> allGames = gameRepository.findAllByIsDeletedFalse();

        // 날짜 기반 seed
        long seed = LocalDate.now().toEpochDay();
        Collections.shuffle(allGames, new Random(seed));

        List<Game> selectedGames = allGames.stream()
                .limit(2)
                .toList();

        return selectedGames.stream().map(game -> {
            GameVote vote = gameVoteRepository.findByGameAndUser(game, user);
            if (vote!= null) {
                return GameMainResponseDto.ofWithResult(game, vote);
            } else {
                return GameMainResponseDto.ofWithoutResult(game);
            }
                })
                .collect(Collectors.toList());
    }
}
