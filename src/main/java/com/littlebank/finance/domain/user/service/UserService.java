package com.littlebank.finance.domain.user.service;

import com.littlebank.finance.domain.relationship.domain.Relationship;
import com.littlebank.finance.domain.relationship.domain.repository.RelationshipRepository;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.dto.request.SocialLoginAdditionalInfoRequest;
import com.littlebank.finance.domain.user.dto.response.*;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponse saveUser(User user) {
        verifyDuplicatedEmail(user.getEmail());
        verifyDuplicatedPhone(user.getPhone());

        user.encodePassword(passwordEncoder);

        return SignupResponse.of(userRepository.save(user));
    }

    public ProfileImagePathUpdateResponse updateProfileImagePath(long userId, String profileImagePath) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        user.updateProfileImagePath(profileImagePath);

        return ProfileImagePathUpdateResponse.of(user);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getMyInfo(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        return UserInfoResponse.of(user);
    }

    public UserInfoResponse updateMyInfo(long userId, User updateInfo) {
        verifyDuplicatedPhone(updateInfo.getPhone());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        user.update(updateInfo);

        return UserInfoResponse.of(user);
    }

    public void deleteUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        userRepository.deleteById(user.getId());
    }

    public SocialLoginAdditionalInfoResponse updateAdditionalInfoAfterSocialLogin(SocialLoginAdditionalInfoRequest request, long userId) {
        verifyDuplicatedPhone(request.getPhone());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        user.updateRequiredInfo(request.toEntity());

        return SocialLoginAdditionalInfoResponse.of(user);
    }

    public UserSearchResponse searchUser(String phone, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        User searchUser = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        List<Relationship> relationship = relationshipRepository.findAllByFromUserIdAndToUserId(user.getId(), searchUser.getId());

        return UserSearchResponse.of(searchUser, relationship);
    }

    private void verifyDuplicatedEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(ErrorCode.EMAIL_DUPLICATED);
        }
    }

    private void verifyDuplicatedPhone(String phone) {
        if (userRepository.existsByPhone(phone)) {
            throw new UserException(ErrorCode.PHONE_DUPLICATED);
        }
    }
}
