package com.littlebank.finance.domain.relationship.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RelationshipReqAcceptRequest {
    @NotNull
    @Schema(description = "수락할 관계 요청 id", example = "1")
    private Long relationshipId;
}
