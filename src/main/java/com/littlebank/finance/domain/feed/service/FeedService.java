package com.littlebank.finance.domain.feed.service;

import com.littlebank.finance.domain.feed.domain.Feed;
import com.littlebank.finance.domain.feed.domain.FeedImage;
import com.littlebank.finance.domain.feed.domain.repository.FeedImageRepository;
import com.littlebank.finance.domain.feed.domain.repository.FeedRepository;
import com.littlebank.finance.domain.feed.dto.request.FeedCreateRequestDto;
import com.littlebank.finance.domain.feed.dto.request.FeedImageRequestDto;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final FeedImageRepository feedImageRepository;

    @Transactional
    public void createFeed(Long userId, FeedCreateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Feed feed = Feed.builder()
                .user(user)
                .title(request.getTitle())
                .gradeCategory(request.getGradeCategory())
                .subjectCategory(request.getSubjectCategory())
                .tagCategory(request.getTagCategory())
                .content(request.getContent())
                .viewCount(0)
                .likeCount(0)
                .commentCount(0)
                .build();

        Feed savedFeed = feedRepository.save(feed);

        if (request.getImages() != null) {
            for (FeedImageRequestDto imageDto : request.getImages()) {
                FeedImage feedImage = FeedImage.builder()
                        .feed(savedFeed)
                        .url(imageDto.getUrl())
                        .build();
                feedImageRepository.save(feedImage);
            }
        }
    }
}
