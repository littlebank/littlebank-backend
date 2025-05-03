package com.littlebank.finance.domain.relationship.domain.repository;

import com.littlebank.finance.domain.relationship.domain.Relationship;
import com.littlebank.finance.domain.relationship.domain.RelationshipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, Long>, CustomRelationshipRepository {
    Optional<Relationship> findByFromUserIdAndToUserIdAndRelationshipType(Long fromUserId, Long toUserId, RelationshipType relationshipType);
}
