package com.littlebank.finance.domain.user.dto.response;

import com.littlebank.finance.domain.user.domain.SchoolType;
import com.littlebank.finance.domain.user.domain.constant.Authority;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MyInfoResponse {
    private Long userId;
    private String email;
    private String name;
    private String statusMessage;
    private String phone;
    private String rrn;
    private String bankName;
    private String bankCode;
    private String bankAccount;
    private String profileImagePath;
    private Integer point;
    private Integer accumulatedPoint;
    private Integer targetAmount;
    private String schoolName;
    private SchoolType schoolType;
    private Integer region;
    private String address;
    private UserRole role;
    private Authority authority;
    private boolean isSubscribe;
    private LocalDateTime lastLoginAt;
    private LocalDateTime registeredAt;

    public static MyInfoResponse of(User user) {
        return MyInfoResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .statusMessage(user.getStatusMessage())
                .phone(user.getPhone())
                .rrn(user.getRrn())
                .bankName(user.getBankName())
                .bankCode(user.getBankCode())
                .bankAccount(user.getBankAccount())
                .profileImagePath(user.getProfileImagePath())
                .point(user.getPoint())
                .accumulatedPoint(user.getAccumulatedPoint())
                .targetAmount(user.getTargetAmount())
                .schoolName(user.getSchoolName())
                .schoolType(user.getSchoolType())
                .region(user.getRegion())
                .address(user.getAddress())
                .role(user.getRole())
                .authority(user.getAuthority())
                .isSubscribe(user.getIsSubscribe())
                .registeredAt(user.getCreatedDate())
                .build();
    }
}
