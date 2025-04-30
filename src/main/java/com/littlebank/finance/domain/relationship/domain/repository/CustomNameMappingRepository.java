package com.littlebank.finance.domain.relationship.domain.repository;

import com.littlebank.finance.domain.relationship.domain.CustomNameMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomNameMappingRepository extends JpaRepository<CustomNameMapping, Long> {
    boolean existsByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
