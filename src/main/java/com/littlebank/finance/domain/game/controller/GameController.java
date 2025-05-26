package com.littlebank.finance.domain.game.controller;

import com.littlebank.finance.domain.game.dto.request.GameVoteRequestDto;
import com.littlebank.finance.domain.game.dto.response.GameVoteResponseDto;
import com.littlebank.finance.domain.game.service.GameService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-user/game")
@RequiredArgsConstructor
@Tag(name = "game")
public class GameController {
    private final GameService gameService;

    @Operation(summary = "게임 참여")
    @PostMapping("/join/{gameId}")
    public ResponseEntity<GameVoteResponseDto> joinGame (
            @PathVariable Long gameId,
            @RequestBody GameVoteRequestDto request,
            @AuthenticationPrincipal CustomUserDetails user
            ) {
        GameVoteResponseDto response = gameService.joinGame(user.getId(), gameId, request);
        return ResponseEntity.ok(response);
    }
}
