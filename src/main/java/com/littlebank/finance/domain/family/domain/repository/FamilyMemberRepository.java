package com.littlebank.finance.domain.family.domain.repository;

import com.littlebank.finance.domain.family.domain.FamilyMember;
import com.littlebank.finance.domain.family.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long>, CustomFamilyMemberRepository {
    Optional<FamilyMember> findByFamilyIdAndUserId(Long familyId, Long userId);
    List<FamilyMember> findAllByUserIdAndStatus(Long userId, Status status);
}
