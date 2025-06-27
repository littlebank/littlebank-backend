package com.littlebank.finance.domain.share.controller;


import com.littlebank.finance.domain.share.dto.request.CreateLinkRequestDto;
import com.littlebank.finance.domain.share.dto.response.CreateLinkResponseDto;
import com.littlebank.finance.domain.share.service.ShareService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-user/share")
@RequiredArgsConstructor
@Tag(name = "Share")
public class ShareController {
    private final ShareService shareService;

    @Operation(summary = "구독 초대코드 링크 생성하기 API")
    @PostMapping("/create/link")
    public ResponseEntity<CreateLinkResponseDto> createLink(@RequestBody CreateLinkRequestDto request, @AuthenticationPrincipal CustomUserDetails user) {
        CreateLinkResponseDto response = shareService.createLink(user.getId(), request);
        return ResponseEntity.ok(response);
    }
}
