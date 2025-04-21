package com.littlebank.finance.domain.chat.controller;

import com.littlebank.finance.domain.chat.dto.request.ChatRoomRequest;
import com.littlebank.finance.domain.chat.domain.ChatRoom;
import com.littlebank.finance.domain.chat.service.ChatRoomService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api-user/chat-room")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@Tag(name = "채팅방 API", description = "채팅방 생성, 조회 등 기능 제공")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 생성", description = "채팅방을 생성하고 참여자들을 등록합니다.")
    @PostMapping
    public ResponseEntity<ChatRoom> createRoom(@RequestBody @Valid ChatRoomRequest request) {
        ChatRoom createdRoom = chatRoomService.createRoom(request);
        return ResponseEntity.ok(createdRoom);
    }

    @Operation(summary = "전체 채팅방 조회", description = "모든 채팅방을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ChatRoom>> listRooms() {
        return ResponseEntity.ok(chatRoomService.getAllRooms());
    }

    @GetMapping("/{roomId}/is-participant")
    public ResponseEntity<Boolean> isParticipant(@PathVariable String roomId, @RequestParam String userId) {
        boolean result = chatRoomService.isParticipant(roomId, userId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "채팅방 삭제", description = "채팅방 ID로 삭제합니다.")
    @Transactional
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomId) {
        chatRoomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}