package com.littlebank.finance.domain.chat.controller;

import com.littlebank.finance.domain.chat.dto.request.ChatRoomCreateRequest;
import com.littlebank.finance.domain.chat.dto.response.*;
import com.littlebank.finance.domain.chat.service.ChatService;

import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.common.PaginationPolicy;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "단일 채팅방 상세 조회 API")
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomDetailsResponse> getRoomDetails(
            @Parameter(description = "채팅방 식별 id")
            @PathVariable("roomId") Long roomId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ChatRoomDetailsResponse createdRoom = chatService.getRoomDetails(customUserDetails.getId(), roomId);
        return ResponseEntity.ok(createdRoom);
    }

    @Operation(summary = "메시지 조회 API")
    @GetMapping("/message/{roomId}")
    public ResponseEntity<CustomPageResponse<APIMessageResponse>> getMessages(
            @Parameter(description = "채팅방 식별 id")
            @PathVariable("roomId") Long roomId,
            @Parameter(description = "페이징 조회 기준 메시지 식별 id")
            @RequestParam(name = "lastMessageId") Long lastMessageId,
            @Parameter(description = "페이지 번호, 0부터 시작")
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Pageable pageable = PageRequest.of(pageNumber, PaginationPolicy.CHAT_MESSAGE_PAGE_SIZE);
        CustomPageResponse<APIMessageResponse> createdRoom = chatService.getMessages(customUserDetails.getId(), roomId, lastMessageId, pageable);
        return ResponseEntity.ok(createdRoom);
    }

    @Operation(summary = "채팅방 초대&나가기 로그 조회 API")
    @GetMapping("/event-log/{roomId}")
    public ResponseEntity<List<EventLogResponse>> getMessages(
            @Parameter(description = "채팅방 식별 id")
            @PathVariable("roomId") Long roomId,
            @Parameter(description = "페이지 갯수 1개 이하 여부")
            @RequestParam(name = "isOnlyOnePage") Boolean isOnlyOnePage,
            @Parameter(description = "로그 조회 기준이 되는 시작 메시지 식별 id")
            @RequestParam(name = "startMessageId") Long startMessageId,
            @Parameter(description = "로그 조회 기준이 되는 마지막 메시지 식별 id")
            @RequestParam(name = "endMessageId") Long endMessageId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<EventLogResponse> createdRoom = chatService.getEventLog(customUserDetails.getId(), roomId, isOnlyOnePage, startMessageId, endMessageId);
        return ResponseEntity.ok(createdRoom);
    }
}