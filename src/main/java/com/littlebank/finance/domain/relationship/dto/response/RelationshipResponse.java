package com.littlebank.finance.domain.relationship.dto.response;

import com.littlebank.finance.domain.relationship.domain.Relationship;
import com.littlebank.finance.domain.relationship.domain.RelationshipStatus;
import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RelationshipResponse {

    private Long relationshipId;
    private RelationshipType relationshipType;
    private RelationshipStatus relationshipStatus;

    public static RelationshipResponse of(Relationship relationship) {
        return RelationshipResponse.builder()
                .relationshipId(relationship.getId())
                .relationshipType(relationship.getRelationshipType())
                .relationshipStatus(relationship.getRelationshipStatus())
                .build();
    }
}
