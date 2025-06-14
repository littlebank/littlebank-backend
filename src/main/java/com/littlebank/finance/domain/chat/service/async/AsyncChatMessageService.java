package com.littlebank.finance.domain.chat.service.async;

import com.littlebank.finance.domain.chat.domain.ChatMessage;
import com.littlebank.finance.domain.chat.domain.UserChatRoom;
import com.littlebank.finance.domain.chat.domain.repository.ChatMessageRepository;
import com.littlebank.finance.domain.chat.domain.repository.UserChatRoomRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Async
@Transactional
@RequiredArgsConstructor
public class AsyncChatMessageService {
    private static final int MAX_RETRY = 100;
    private final ChatMessageRepository chatMessageRepository;
    private final UserChatRoomRepository userChatRoomRepository;

    public void decreaseReadCounts(List<Long> messageIds) {
        List<ChatMessage> messages = chatMessageRepository.findAllByIdIn(messageIds);
        int retryCount = 0;
        while (retryCount++ < MAX_RETRY) {
            try {
                for (ChatMessage message : messages) {
                    if (message.getReadCount() > 0) {
                        message.readMessage();
                    }
                }

                chatMessageRepository.saveAll(messages);
                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                log.warn("낙관적 락 충돌 발생 - 재시도 {}/{}", retryCount, MAX_RETRY);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } catch (Exception ex) {
                log.error("메시지 읽음 처리 중 알 수 없는 오류 발생", ex);
                return;
            }
        }
        log.error("낙관적 락 재시도 초과 - 메시지 읽음 처리 실패 (messageIds: {})",
                messages.stream().map(m -> m.getId()).collect(Collectors.toList())
        );
    }

    public void updateLastReadMessageId(Long userId, Long roomId, Long newMessageId) {
        UserChatRoom participant = userChatRoomRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        int retryCount = 0;
        while (retryCount++ < MAX_RETRY) {
            try {
                participant.updateLastReadMessageId(newMessageId);
                userChatRoomRepository.save(participant);
                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                log.warn("낙관적 락 충돌 발생 - UserChatRoom update 재시도 {}/{}", retryCount, MAX_RETRY);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } catch (Exception ex) {
                log.error("UserChatRoom 업데이트 중 알 수 없는 오류 발생", ex);
                return;
            }
        }

        log.error("UserChatRoom 업데이트 실패: 재시도 초과 (userId: {}, roomId: {})",
                participant.getUser().getId(), participant.getRoom().getId()
        );
    }

}

