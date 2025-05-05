package com.littlebank.finance.domain.friend.controller;

import com.littlebank.finance.domain.friend.dto.request.FriendAddRequest;
import com.littlebank.finance.domain.friend.dto.request.FriendRenameRequest;
import com.littlebank.finance.domain.friend.dto.response.*;
import com.littlebank.finance.domain.friend.service.FriendService;
import com.littlebank.finance.global.common.CustomPageResponse;
import com.littlebank.finance.global.common.PaginationPolicy;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-user/friend")
@RequiredArgsConstructor
@Tag(name = "Friend")
public class FriendController {
    private final FriendService friendService;

    @Operation(summary = "친구 추가 API")
    @PostMapping
    public ResponseEntity<FriendAddResponse> addFriend(
            @RequestBody @Valid FriendAddRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        FriendAddResponse response = friendService.addFriend(request, customUserDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "친구 목록 조회 API")
    @GetMapping("/list")
    public ResponseEntity<CustomPageResponse<FriendInfoResponse>> getFriendList(
            @Parameter(description = "페이지 번호, 0부터 시작")
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Pageable pageable = PageRequest.of(pageNumber, PaginationPolicy.FRIEND_LIST_PAGE_SIZE);
        CustomPageResponse<FriendInfoResponse> response = friendService.getFriendList(customUserDetails.getId(), pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "나를 친구로 추가한 유저 목록 조회 API")
    @GetMapping("/added-me")
    public ResponseEntity<CustomPageResponse<FriendInfoResponse>> getFriendAddedMe(
            @Parameter(description = "페이지 번호, 0부터 시작")
            @RequestParam(name = "pageNumber") Integer pageNumber,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Pageable pageable = PageRequest.of(pageNumber, PaginationPolicy.ADDED_ME_FRIEND_LIST_PAGE_SIZE);
        CustomPageResponse<FriendInfoResponse> response = friendService.getFriendAddedMe(customUserDetails.getId(), pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "친구 삭제 API")
    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteFriend(
            @Parameter(description = "삭제할 friend id")
            @PathVariable(name = "friendId") Long friendId
    ) {
        friendService.deleteFriend(friendId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "친한 친구 등록 API")
    @PatchMapping("/mark-best/{friendId}")
    public ResponseEntity<BestFriendMarkResponse> markBestFriend(
            @Parameter(description = "친한 친구로 등록할 friend id")
            @PathVariable(name = "friendId") Long friendId
    ) {
        BestFriendMarkResponse response = friendService.markBestFriend(friendId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "친한 친구 등록 취소 API")
    @PatchMapping("/unmark-best/{friendId}")
    public ResponseEntity<BestFriendMarkResponse> unmarkBestFriend(
            @Parameter(description = "친한 친구 등록 취소할 friend id")
            @PathVariable(name = "friendId") Long friendId
    ) {
        BestFriendMarkResponse response = friendService.unmarkBestFriend(friendId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "친구 차단 API")
    @PatchMapping("/block/{friendId}")
    public ResponseEntity<FriendBlockStatusResponse> blockFriend(
            @Parameter(description = "차단할 friend id")
            @PathVariable(name = "friendId") Long friendId
    ) {
        FriendBlockStatusResponse response = friendService.blockFriend(friendId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "친구 차단 해제 API")
    @PatchMapping("/unblock/{friendId}")
    public ResponseEntity<FriendBlockStatusResponse> unblockFriend(
            @Parameter(description = "차단 해제할 friend id")
            @PathVariable(name = "friendId") Long friendId
    ) {
        FriendBlockStatusResponse response = friendService.unblockFriend(friendId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "친구 이름 설정 API")
    @PatchMapping("/rename")
    public ResponseEntity<FriendRenameResponse> renameFriend(
            @RequestBody @Valid FriendRenameRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        FriendRenameResponse response = friendService.renameFriend(customUserDetails.getId(), request);
        return ResponseEntity.ok(response);
    }
}
