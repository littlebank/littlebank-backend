package com.littlebank.finance.domain.user.domain;

import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Table(name = "userConsent")
@SQLDelete(sql = "UPDATE user_consent SET is_deleted = true WHERE user_id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor()
public class UserConsent extends BaseEntity {
    @Id
    private Long userId;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private Boolean agreedTermsOfService;
    @Column(nullable = false)
    private Boolean agreedPrivacyCollection;
    @Column(nullable = false)
    private Boolean agreedMinorGuardian;
    @Column(nullable = false)
    private Boolean agreedElectronicFinance;
    @Column(nullable = false)
    private Boolean agreedRewardGuardian;
    @Column(nullable = false)
    private Boolean agreedThirdPartySharing;
    @Column(nullable = false)
    private Boolean agreedDataProcessingDelegation;
    @Column(nullable = false)
    private Boolean agreedMarketing;
    @Column(nullable = false)
    private Boolean isDeleted;
    @Builder
    public UserConsent(
            User user,
            Boolean agreedTermsOfService,
            Boolean agreedPrivacyCollection,
            Boolean agreedMinorGuardian,
            Boolean agreedElectronicFinance,
            Boolean agreedRewardGuardian,
            Boolean agreedThirdPartySharing,
            Boolean agreedDataProcessingDelegation,
            Boolean agreedMarketing,
            Boolean isDeleted
    ) {
        this.user = user;
        this.agreedTermsOfService = agreedTermsOfService;
        this.agreedPrivacyCollection = agreedPrivacyCollection;
        this.agreedMinorGuardian = agreedMinorGuardian;
        this.agreedElectronicFinance = agreedElectronicFinance;
        this.agreedRewardGuardian = agreedRewardGuardian;
        this.agreedThirdPartySharing = agreedThirdPartySharing;
        this.agreedDataProcessingDelegation = agreedDataProcessingDelegation;
        this.agreedMarketing = agreedMarketing;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }
}
