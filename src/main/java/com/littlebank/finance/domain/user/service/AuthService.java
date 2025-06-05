package com.littlebank.finance.domain.user.service;

import com.littlebank.finance.domain.user.domain.Authority;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.dto.request.AccountHolderVerifyRequest;
import com.littlebank.finance.domain.user.dto.request.AccountPinVerifyRequest;
import com.littlebank.finance.domain.user.dto.request.LoginRequest;
import com.littlebank.finance.domain.user.dto.request.SocialLoginRequest;
import com.littlebank.finance.domain.user.dto.response.AccountHolderVerifyResponse;
import com.littlebank.finance.domain.user.dto.response.AccountPinVerifyResponse;
import com.littlebank.finance.domain.user.dto.response.ReissueResponse;
import com.littlebank.finance.domain.user.exception.AuthException;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.jwt.TokenProvider;
import com.littlebank.finance.global.jwt.dto.TokenDto;
import com.littlebank.finance.global.portone.PortoneService;
import com.littlebank.finance.global.portone.dto.AccountHolderDto;
import com.littlebank.finance.global.portone.exception.PortoneException;
import com.littlebank.finance.global.redis.RedisDao;
import com.littlebank.finance.global.redis.RedisPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PortoneService portoneService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RedisDao redisDao;

    public TokenDto login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        user.login(request.getFcmToken());

        TokenDto dto = generateTokenDto(authentication);
        registerRefreshTokenToRedis(dto.getRefreshToken());
        return dto;
    }

    public TokenDto kakaoLogin(SocialLoginRequest request) {
        // 1. 카카오 사용자 정보 조회
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(request.getAccessToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                Map.class
        );

        String kakaoUserId = response.getBody().get("id").toString();
        Map kakaoAccount = (Map) response.getBody().get("kakao_account");
        Map kakaoProfile = (Map) kakaoAccount.get("profile");

        String email = kakaoAccount.get("email").toString();
        String nickname = kakaoProfile.get("nickname").toString();
        String profileImageUrl;
        if (kakaoProfile.containsKey("profile_image_url")) {
            profileImageUrl = kakaoProfile.get("profile_image_url").toString();
        } else {
            profileImageUrl = null;
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .password(kakaoUserId)
                        .name(nickname)
                        .profileImagePath(profileImageUrl)
                        .authority(Authority.USER)
                        .build())
                );

        user.encodePassword(passwordEncoder);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        user.login(request.getFcmToken());

        TokenDto dto = generateTokenDto(authentication);
        registerRefreshTokenToRedis(dto.getRefreshToken());
        return dto;
    }

    public TokenDto naverLogin(SocialLoginRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(request.getAccessToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map naverAccount = (Map) response.getBody().get("response");

        String naverUserId = naverAccount.get("id").toString();
        String email = naverAccount.get("email").toString();
        String nickname = naverAccount.get("name").toString();
        String profileImageUrl;
        if (naverAccount.containsKey("profile_image")) {
            profileImageUrl = naverAccount.get("profile_image").toString();
        } else {
            profileImageUrl = null;
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .password(naverUserId)
                        .name(nickname)
                        .profileImagePath(profileImageUrl)
                        .authority(Authority.USER)
                        .build())
                );

        user.encodePassword(passwordEncoder);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        user.login(request.getFcmToken());

        TokenDto dto = generateTokenDto(authentication);
        registerRefreshTokenToRedis(dto.getRefreshToken());
        return dto;
    }

    public void logout(Long userId, String refreshToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        user.logout();

        redisDao.deleteValues(
                RedisPolicy.LOGIN_USER_KEY_PREFIX + tokenProvider.getAuthentication(refreshToken).getName()
        );
    }

    public ReissueResponse reissue(String refreshToken) {
        String loginUserKey = RedisPolicy.LOGIN_USER_KEY_PREFIX + tokenProvider.getAuthentication(refreshToken).getName();
        if (!refreshToken.equals(redisDao.getValues(loginUserKey)) || !tokenProvider.validateToken(refreshToken)) {
            throw new AuthException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        String accessToken = tokenProvider.provideAccessToken(authentication);

        return ReissueResponse.of(accessToken);
    }

    private TokenDto generateTokenDto(Authentication authentication) {
        String accessToken = tokenProvider.provideAccessToken(authentication);
        String refreshToken = tokenProvider.provideRefreshToken(authentication);

        return TokenDto.of(accessToken, refreshToken);
    }

    private void registerRefreshTokenToRedis(String refreshToken) {
        redisDao.setValues(
                RedisPolicy.LOGIN_USER_KEY_PREFIX + tokenProvider.getAuthentication(refreshToken).getName(),
                refreshToken,
                Duration.ofMillis(tokenProvider.getExpiration(refreshToken))
        );
    }

    @Transactional(readOnly = true)
    public AccountHolderVerifyResponse verifyAccountHolder(AccountHolderVerifyRequest request) {
        String accessToken = portoneService.getAccessToken();
        AccountHolderDto accountHolderDto = portoneService.getAccountHolder(request.getBankCode(), request.getBankNumber(), accessToken);

        if (!accountHolderDto.getBankHolder().equals(request.getHolderName())) {
            throw new PortoneException(ErrorCode.ACCOUNT_HOLDER_NOT_MATCHED);
        }

        return AccountHolderVerifyResponse.of(accountHolderDto.getBankHolder());
    }

    @Transactional(readOnly = true)
    public AccountPinVerifyResponse verifyAccountPin(Long userId, AccountPinVerifyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        if (!user.getAccountPin().equals(request.getPin())) {
            throw new UserException(ErrorCode.PIN_NOT_MATCHED);
        }

        return AccountPinVerifyResponse.of(user);
    }
}
