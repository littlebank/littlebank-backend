package com.littlebank.finance.domain.feed.controller;

import com.littlebank.finance.domain.feed.dto.request.FeedCreateRequestDto;
import com.littlebank.finance.domain.feed.service.FeedService;
import com.littlebank.finance.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-user/feed/create")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @PostMapping
    public ResponseEntity<String> createFeed(@RequestBody FeedCreateRequestDto request,
                                           @AuthenticationPrincipal CustomUserDetails currentUser){
        feedService.createFeed(currentUser.getId(), request);
        return ResponseEntity.ok("업로드 완료");
    }
}
