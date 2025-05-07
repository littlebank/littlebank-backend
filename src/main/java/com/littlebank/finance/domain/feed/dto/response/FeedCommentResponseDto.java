package com.littlebank.finance.domain.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FeedCommentResponseDto {
    private Long commentId;
    private Long feedId;
    private Long parentId;
    private String writerName;
    private String writerProfileUrl;
    private String content;
    private int likeCount;
    private boolean isliked;
    private LocalDateTime createdDate;
    public static FeedCommentResponseDto of(Long commentId,
                                            Long feedId,
                                            Long parentId,
                                            String writerName,
                                            String writerProfileUrl,
                                            String content,
                                            int likeCount,
                                            boolean isLiked) {
        return FeedCommentResponseDto.builder()
                .commentId(commentId)
                .feedId(feedId)
                .parentId(parentId)
                .writerName(writerName)
                .writerProfileUrl(writerProfileUrl)
                .content(content)
                .likeCount(likeCount)
                .isliked(isLiked)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
