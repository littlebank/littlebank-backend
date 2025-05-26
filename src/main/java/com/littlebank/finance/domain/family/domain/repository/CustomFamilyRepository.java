package com.littlebank.finance.domain.family.domain.repository;

import com.littlebank.finance.domain.family.domain.Family;

import java.util.Optional;

public interface CustomFamilyRepository {
    Optional<Family> findByUserIdWithMember(Long userId);
}
