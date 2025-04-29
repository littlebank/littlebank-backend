package com.littlebank.finance.domain.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FeedResponseDto {
    private Long feedId;
    private String title;
    private List<String> imageUrls;
    private String content;
    private String writerName;
    private String writerProfileImageUrl;
    private int viewCount;
    private int likeCount;
    private int commentCount;
}
