package com.littlebank.finance.domain.friend.controller;

import com.littlebank.finance.domain.friend.dto.request.FriendAddRequest;
import com.littlebank.finance.domain.friend.dto.response.FriendAddResponse;
import com.littlebank.finance.domain.friend.service.FriendService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
