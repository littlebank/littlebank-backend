package com.littlebank.finance.domain.family.domain.repository;

import com.littlebank.finance.domain.family.domain.FamilyMember;
import com.littlebank.finance.domain.family.dto.response.FamilyInfoResponse;

import java.util.Optional;

public interface CustomFamilyMemberRepository {
    Optional<FamilyMember> findByUserIdWithFamily(Long userId);
    FamilyInfoResponse getFamilyInfoByUserId(Long userId);
}
