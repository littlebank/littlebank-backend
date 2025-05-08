package com.littlebank.finance.domain.family.domain.repository;

import com.littlebank.finance.domain.family.domain.FamilyMember;

import java.util.Optional;

public interface CustomFamilyMemberRepository {
    Optional<FamilyMember> findByUserIdWithFamily(Long userId);
}
