package com.littlebank.finance.domain.feed.dto.response;

import com.littlebank.finance.domain.feed.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FeedResponseDto {
    private Long feedId;
    private String title;
    private List<String> imageUrls;
    private String content;
    private TagCategory tagCategory;
    private SubjectCategory subjectCategory;
    private GradeCategory gradeCategory;
    private Long writerId;
    private String writerName;
    private String writerProfileImageUrl;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private boolean liked;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public static FeedResponseDto of (Feed feed, List<FeedImage> images, int likeCount, boolean liked) {
        List<String> imageUrls = images.stream()
                .map(FeedImage::getUrl)
                .toList();
        return FeedResponseDto.builder()
                .feedId(feed.getId())
                .title(feed.getTitle())
                .imageUrls(imageUrls)
                .content(feed.getContent())
                .tagCategory(feed.getTagCategory())
                .subjectCategory(feed.getSubjectCategory())
                .gradeCategory(feed.getGradeCategory())
                .writerId(feed.getUser().getId())
                .writerName(feed.getUser().getName())
                .writerProfileImageUrl(feed.getUser().getProfileImagePath())
                .viewCount(feed.getViewCount())
                .likeCount(likeCount)
                .commentCount(feed.getCommentCount())
                .liked(liked)
                .createdDate(feed.getCreatedDate())
                .lastModifiedDate(feed.getLastModifiedDate())
                .build();
    }
}
