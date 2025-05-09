package com.littlebank.finance.domain.family.domain.repository;

import com.littlebank.finance.domain.family.domain.FamilyMember;
import com.littlebank.finance.domain.family.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long>, CustomFamilyMemberRepository {
    boolean existsByUserIdAndStatus(Long userId, Status status);
    Optional<FamilyMember> findByFamilyIdAndUserId(Long familyId, Long userId);
    List<FamilyMember> findAllByUserIdAndStatus(Long userId, Status status);
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM family_member WHERE user = :userId AND status = :status", nativeQuery = true)
    int deleteIfExistsByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}
