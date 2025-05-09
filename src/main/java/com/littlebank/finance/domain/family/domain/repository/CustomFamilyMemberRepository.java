package com.littlebank.finance.domain.family.domain.repository;

import com.littlebank.finance.domain.family.domain.FamilyMember;
import com.littlebank.finance.domain.family.dto.response.FamilyInfoResponse;

import java.util.List;
import java.util.Optional;

public interface CustomFamilyMemberRepository {
    Optional<FamilyMember> findByUserIdWithFamily(Long userId);
    List<FamilyMember> findByMemberIdWithFamilyAndUser(Long memberId);
    FamilyInfoResponse getFamilyInfoByUserId(Long userId);
}
