package com.littlebank.finance.domain.relationship.domain.repository;

import com.littlebank.finance.domain.relationship.domain.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationshipRepository extends JpaRepository<Relationship, Long>, CustomRelationshipRepository {
    List<Relationship> findAllByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
