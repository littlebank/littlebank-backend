package com.littlebank.finance.domain.report.service;

import com.littlebank.finance.domain.feed.domain.Feed;
import com.littlebank.finance.domain.feed.domain.FeedComment;
import com.littlebank.finance.domain.feed.domain.repository.FeedCommentRepository;
import com.littlebank.finance.domain.feed.domain.repository.FeedRepository;
import com.littlebank.finance.domain.feed.exception.FeedException;
import com.littlebank.finance.domain.report.dto.request.ReportRequestDto;
import com.littlebank.finance.domain.report.dto.response.ReportResponseDto;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.mail.ReportMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final FeedCommentRepository commentRepository;
    private final ReportMailService mailService;

    public ReportResponseDto report(ReportRequestDto request, Long reporterId) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        String subject = "[신고 접수] " + request.getType();
        Long targetUserId;
        String content;
        String targetContent = "";
        String targetUserName = "";

        switch (request.getType()) {
            case FEED -> {
                Feed feed = feedRepository.findById(request.getTargetId())
                        .orElseThrow(() -> new FeedException(ErrorCode.FEED_NOT_FOUND));
                targetUserId = feed.getUser().getId();
                targetUserName = feed.getUser().getName();
                targetContent = feed.getContent();
            }
            case COMMENT -> {
                FeedComment comment = commentRepository.findById(request.getTargetId())
                        .orElseThrow(() -> new FeedException(ErrorCode.COMMENT_NOT_FOUND));
                targetUserId = comment.getUser().getId();
                targetUserName = comment.getUser().getName();
                targetContent = comment.getContent();
            }
            case CHAT -> {
                targetUserId = null;
            }
            default -> throw new IllegalArgumentException("지원하지 않는 신고 유형입니다.");
        }

        mailService.sendReportEmail(
                request.getType().name(),
                request.getTargetId(),
                reporter.getId(),
                targetUserId,
                targetUserName,
                targetContent,
                reporter.getName()
        );

        return ReportResponseDto.of(
                request.getType(),
                request.getTargetId(),
                reporter.getId(),
                reporter.getName(),
                targetUserId,
                targetUserName,
                targetContent
        );
    }

}