package com.littlebank.finance.domain.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "채팅방 생성 요청 DTO")
public class ChatRoomRequest {

    @NotNull
    @Schema(description = "채팅방 이름", example = "ltbk 채팅방", required = true)
    private String name;

    @Size(min=2, message="최소 두 명 이상의 참여자가 필요합니다.")
    @Schema(description = "참여자 사용자 ID 목록", example = "[1, 2]", required = true)
    private List<Long> participantIds;
}