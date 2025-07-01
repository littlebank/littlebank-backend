package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.user.domain.Authority;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.UserConsent;
import com.littlebank.finance.domain.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignupResponse {

    private Long userId;
    private String email;
    private UserRole role;
    private Authority authority;
    private Boolean agreedTermsOfService;
    private Boolean agreedPrivacyCollection;
    private Boolean agreedMinorGuardian;
    private Boolean agreedElectronicFinance;
    private Boolean agreedRewardGuardian;
    private Boolean agreedThirdPartySharing;
    private Boolean agreedDataProcessingDelegation;
    private Boolean agreedMarketing;

    public static SignupResponse of(User user, UserConsent consent) {
        return SignupResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .authority(user.getAuthority())
                .agreedTermsOfService(consent.getAgreedTermsOfService())
                .agreedPrivacyCollection(consent.getAgreedPrivacyCollection())
                .agreedMinorGuardian(consent.getAgreedMinorGuardian())
                .agreedElectronicFinance(consent.getAgreedElectronicFinance())
                .agreedRewardGuardian(consent.getAgreedRewardGuardian())
                .agreedThirdPartySharing(consent.getAgreedThirdPartySharing())
                .agreedDataProcessingDelegation(consent.getAgreedDataProcessingDelegation())
                .agreedMarketing(consent.getAgreedMarketing())
                .build();
    }
}