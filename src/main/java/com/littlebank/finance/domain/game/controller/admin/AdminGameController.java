package com.littlebank.finance.domain.game.controller.admin;

import com.littlebank.finance.domain.game.domain.Game;
import com.littlebank.finance.domain.game.dto.request.GameRequestDto;
import com.littlebank.finance.domain.game.dto.response.GameResponseDto;
import com.littlebank.finance.domain.game.service.admin.AdminGameService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-admin/game")
@Tag(name = "game")
@RequiredArgsConstructor
public class AdminGameController {
    private final AdminGameService adminGameService;

    @Operation(summary = "게임 생성")
    @PostMapping("/create")
    public ResponseEntity<GameResponseDto> createGame(
            @RequestBody GameRequestDto request,
            @AuthenticationPrincipal CustomUserDetails admin
            ) {
        GameResponseDto response = adminGameService.createGame(admin.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게임 수정")
    @PutMapping("/update/{gameId}")
    public ResponseEntity<GameResponseDto> updateGame (
            @RequestBody GameRequestDto request,
            @PathVariable Long gameId,
            @AuthenticationPrincipal CustomUserDetails admin
    ) {
        GameResponseDto response = adminGameService.updateGame(admin.getId(), gameId, request);
        return ResponseEntity.ok(response);
    }
}
