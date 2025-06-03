package com.littlebank.finance.domain.user.domain;

import com.littlebank.finance.domain.chat.domain.ChatMessage;
import com.littlebank.finance.domain.feed.domain.Feed;
import com.littlebank.finance.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE user_id = ?")
@Where(clause = "is_deleted = false")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 100, nullable = false)
    private String statusMessage;
    @Column(length = 11, unique = true)
    private String phone;
    @Column(length = 6)
    private String rrn;
    @Column(length = 10)
    private String bankName;
    @Column(length = 3)
    private String bankCode;
    @Column(length = 20)
    private String bankAccount;
    @Column(length = 150)
    private String profileImagePath;
    private Integer point;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;
    @Column(nullable = false)
    private Boolean isSubscribe;
    @Column(length = 200, nullable = false)
    private String fcmToken;
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> senderMessages = new ArrayList<>();
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> receiverMessages = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feed> feeds = new ArrayList<>();
    @Column(nullable = false)
    private Boolean isDeleted;

    @Builder
    public User(
            String email, String password, String name, String statusMessage, String phone, String rrn,
            String bankName, String bankCode, String bankAccount, String profileImagePath, Integer point, UserRole role,
            Authority authority, Boolean isSubscribe, String fcmToken, LocalDateTime lastLoginAt, Boolean isDeleted
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.statusMessage = statusMessage == null ? "" : statusMessage;
        this.phone = phone;
        this.rrn = rrn;
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.bankAccount = bankAccount;
        this.profileImagePath = profileImagePath;
        this.point = point == null ? 0 : point;
        this.role = role;
        this.authority = authority;
        this.isSubscribe = isSubscribe == null ? false : isSubscribe;
        this.fcmToken = fcmToken == null ? "" : fcmToken;
        this.lastLoginAt = lastLoginAt;
        this.isDeleted = isDeleted == null ? false : isDeleted;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public void update(User updateInfo) {
        this.email = updateInfo.getEmail();
        this.name = updateInfo.getName();
        this.bankName = updateInfo.getBankName();
        this.bankAccount = updateInfo.getBankAccount();
        this.bankCode = updateInfo.getBankCode();
    }

    public void updateRequiredInfo(User updateInfo) {
        this.phone = updateInfo.getPhone();
        this.rrn = updateInfo.getRrn();
        this.role = updateInfo.getRole();
    }

    public void updateStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void addPoint(Integer amount) {
        this.point += amount;
    }

    public void login(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void logout() {
        this.fcmToken = "";
    }

    public void sendPoint(Integer point) {
        this.point -= point;
    }

    public void receivePoint(Integer point) {
        this.point += point;
    }
}
