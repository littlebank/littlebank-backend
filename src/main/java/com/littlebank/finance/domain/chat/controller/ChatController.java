package com.littlebank.finance.domain.chat.controller;

import com.littlebank.finance.domain.chat.dto.request.ChatRoomCreateRequest;
import com.littlebank.finance.domain.chat.dto.response.ChatRoomCreateResponse;
import com.littlebank.finance.domain.chat.dto.response.ChatRoomSummaryResponse;
import com.littlebank.finance.domain.chat.service.ChatService;

import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-user/chat")
@RequiredArgsConstructor
@Tag(name = "Chat")
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "채팅방 생성 API")
    @PostMapping("/room")
    public ResponseEntity<ChatRoomCreateResponse> createRoom(
            @RequestBody @Valid ChatRoomCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ChatRoomCreateResponse createdRoom = chatService.createRoom(customUserDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @Operation(summary = "채팅방 목록 조회 API")
    @GetMapping("/room/list")
    public ResponseEntity<List<ChatRoomSummaryResponse>> getRoomList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<ChatRoomSummaryResponse> createdRoom = chatService.getRoomList(customUserDetails.getId());
        return ResponseEntity.ok(createdRoom);
    }
}