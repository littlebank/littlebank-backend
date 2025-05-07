package com.littlebank.finance.domain.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FeedLikeResponseDto {
    private Long feedId;
    private int likeCount;
    private boolean liked;
    private LocalDateTime createdDate;
    public static FeedLikeResponseDto of(Long feedId, int likeCount, boolean liked) {
        return FeedLikeResponseDto.builder()
                .feedId(feedId)
                .likeCount(likeCount)
                .liked(liked)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
