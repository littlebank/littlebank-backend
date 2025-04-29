package com.littlebank.finance.domain.relationship.dto.request;

import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RelationshipRequest {
    @NotNull
    @Schema(description = "관계 요청 보낼 유저의 id", example = "1")
    private Long targetUserId;

    @NotNull
    @Schema(description = "관계 타입(무슨 관계 인지)", example = "FAMILY or FRIEND or MENTOR_MENTEE")
    private RelationshipType relationshipType;
}
