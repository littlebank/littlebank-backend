package com.littlebank.finance.domain.relationship.dto.response;

import com.littlebank.finance.domain.relationship.domain.Relationship;
import com.littlebank.finance.domain.relationship.domain.RelationshipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RelationshipReqAcceptResponse {
    private Long relationshipId;
    private RelationshipStatus relationshipStatus;

    public static RelationshipReqAcceptResponse of(Relationship relationship) {
        return RelationshipReqAcceptResponse.builder()
                .relationshipId(relationship.getId())
                .relationshipStatus(relationship.getRelationshipStatus())
                .build();
    }
}
