package com.littlebank.finance.domain.feed.dto.response;

import com.littlebank.finance.domain.feed.domain.FeedComment;
import com.littlebank.finance.domain.feed.domain.QFeedComment;
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
    private int replyCount;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static FeedCommentResponseDto of(FeedComment feedComment,
                                            Long feedId,
                                            Long parentId,
                                            String writerName,
                                            String writerProfileUrl,
                                            String content,
                                            int likeCount,
                                            boolean isLiked,
                                            int replyCount) {
        return FeedCommentResponseDto.builder()
                .commentId(feedComment.getId())
                .feedId(feedId)
                .parentId(parentId)
                .writerName(writerName)
                .writerProfileUrl(writerProfileUrl)
                .content(content)
                .likeCount(likeCount)
                .isliked(isLiked)
                .replyCount(replyCount)
                .createdDate(feedComment.getCreatedDate())
                .lastModifiedDate(feedComment.getLastModifiedDate())
                .build();
    }
}
