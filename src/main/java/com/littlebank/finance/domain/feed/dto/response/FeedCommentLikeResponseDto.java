package com.littlebank.finance.domain.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FeedCommentLikeResponseDto {
    private Long commentId;
    private int likeCount;
    private boolean isLiked;
    private LocalDateTime createdDate;
    public static FeedCommentLikeResponseDto of(Long commentId, int likeCount, boolean isLiked) {
        return FeedCommentLikeResponseDto.builder()
                .commentId(commentId)
                .likeCount(likeCount)
                .isLiked(isLiked)
                .createdDate(LocalDateTime.now())
                .build();
    }
}