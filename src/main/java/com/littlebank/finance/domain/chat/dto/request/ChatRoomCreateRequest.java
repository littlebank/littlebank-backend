package com.littlebank.finance.domain.chat.dto.request;

import com.littlebank.finance.domain.chat.domain.RoomRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatRoomCreateRequest {
    @NotBlank
    @Schema(description = "채팅방 이름", example = "ltbk 채팅방")
    private String name;
    @NotNull
    @Schema(description = "채팅방 범위", example = "PRIVATE (1:1 채팅) OR GROUP (그룹 채팅)")
    private RoomRange roomRange;
    @Size(min = 2, message = "최소 두 명 이상의 참여자가 필요합니다.")
    @Schema(description = "참여자 유저 식별 id 목록 (본인 id 포함)", example = "[1, 2]")
    private List<Long> participantIds;
}