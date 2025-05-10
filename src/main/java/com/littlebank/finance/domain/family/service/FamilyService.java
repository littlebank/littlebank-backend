package com.littlebank.finance.domain.family.service;

import com.littlebank.finance.domain.family.domain.Family;
import com.littlebank.finance.domain.family.domain.FamilyMember;
import com.littlebank.finance.domain.family.domain.Status;
import com.littlebank.finance.domain.family.domain.repository.FamilyMemberRepository;
import com.littlebank.finance.domain.family.domain.repository.FamilyRepository;
import com.littlebank.finance.domain.family.dto.request.FamilyMemberAddRequest;
import com.littlebank.finance.domain.family.dto.request.MyFamilyNicknameUpdateRequest;
import com.littlebank.finance.domain.family.dto.response.*;
import com.littlebank.finance.domain.family.exception.FamilyMemberException;
import com.littlebank.finance.domain.friend.dto.response.FamilyInvitationAcceptResponse;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.UserRole;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FamilyService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final FamilyMemberRepository familyMemberRepository;

    public FamilyMemberAddResponse addFamilyMember(Long userId, FamilyMemberAddRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        User targetUser = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        // 요청자의 familyMember 데이터 조회
        FamilyMember member = familyMemberRepository.findByUserIdWithFamily(userId)
                .orElseGet(() -> {
                    // 없을 경우 가족 생성하고, 맴버 생성
                    Family newFamily = familyRepository.save(Family.builder()
                            .createdBy(user)
                            .build());

                    return familyMemberRepository.save(FamilyMember.builder()
                            .nickname(user.getName())
                            .family(newFamily)
                            .user(user)
                            .invitedBy(user)
                            .status(Status.JOINED)
                            .build());
                });

        // 가족에 이미 소속되어 있는 맴버인지 검증
        familyMemberRepository.findByFamilyIdAndUserId(member.getFamily().getId(), request.getTargetUserId())
                .ifPresent(targetMember -> {
                    if (targetMember.getStatus() == Status.REQUESTED) {
                        throw new FamilyMemberException(ErrorCode.FAMILY_INVITE_ALREADY_SENT);
                    }
                    if (targetMember.getStatus() == Status.JOINED) {
                        throw new FamilyMemberException(ErrorCode.FAMILY_MEMBER_ALREADY_EXISTS);
                    }
                });

        // 맴버 요청 상태로 저장
        FamilyMember newMember = familyMemberRepository.save(FamilyMember.builder()
                .nickname(targetUser.getName())
                .family(member.getFamily())
                .user(targetUser)
                .invitedBy(user)
                .status(Status.REQUESTED)
                .build());

        return FamilyMemberAddResponse.of(newMember);
    }

    @Transactional(readOnly = true)
    public FamilyInfoResponse getFamilyInfo(Long userId) {
        return familyMemberRepository.getFamilyInfoByUserId(userId);
    }

    public MyFamilyNicknameUpdateResponse updateMyFamilyNickname(MyFamilyNicknameUpdateRequest request) {
        FamilyMember familyMember = familyMemberRepository.findById(request.getFamilyMemberId())
                .orElseThrow(() -> new FamilyMemberException(ErrorCode.FAMILY_MEMBER_NOT_FOUND));

        familyMember.updateNickname(request.getNickname());

        return MyFamilyNicknameUpdateResponse.of(familyMember);
    }

    @Transactional(readOnly = true)
    public List<ReceivedFamilyInvitationResponse> getReceivedFamilyInvitations(Long userId) {
        List<FamilyMember> members = familyMemberRepository.findAllByUserIdAndStatus(userId, Status.REQUESTED);
        return members.stream()
                .map(member -> ReceivedFamilyInvitationResponse.of(member))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FamilyEnterCheckResponse checkFamilyMembership(Long userId) {
        if (familyMemberRepository.existsByUserIdAndStatus(userId, Status.JOINED)) {
            return FamilyEnterCheckResponse.of(Boolean.TRUE);
        }
        return FamilyEnterCheckResponse.of(Boolean.FALSE);
    }

    public FamilyInvitationAcceptResponse acceptFamilyInvitation(Long userId, Long familyMemberId) {
        familyMemberRepository.deleteByUserIdAndStatus(userId, Status.JOINED.name());

        FamilyMember member = familyMemberRepository.findById(familyMemberId)
                .orElseThrow(() -> new FamilyMemberException(ErrorCode.FAMILY_MEMBER_NOT_FOUND));

        // 부모 2명 검증
        if (member.getUser().getRole() == UserRole.PARENT &&
                familyMemberRepository.findByMemberIdWithFamilyAndUser(familyMemberId).stream()
                        .filter(m -> m.getUser().getRole() == UserRole.PARENT).count() >= 2
        ) {
            throw new FamilyMemberException(ErrorCode.MULTIPLE_PARENTS_NOT_ALLOWED);
        }

        member.accept();

        return FamilyInvitationAcceptResponse.of(member);
    }

    @Transactional(readOnly = true)
    public List<SentFamilyInvitationResponse> getSentFamilyInvitations(Long familyId) {
        List<FamilyMember> members = familyMemberRepository.findAllByFamilyIdAndStatusWithUser(familyId, Status.REQUESTED);
        return members.stream()
                .map(m -> SentFamilyInvitationResponse.of(m))
                .collect(Collectors.toList());
    }
}
