package com.littlebank.finance.domain.friend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class FriendSearchHistoryDeleteRequest {
    @NotNull
    @Schema(description = "삭제할 검색 기록 식별 id 리스트", example = "[1, 2]")
    @Size(min = 1, message = "최소 한 개 이상의 삭제할 검색 기록 식별 id가 포함되어야 합니다")
    private List<Long> searchHistoryIds;
}
