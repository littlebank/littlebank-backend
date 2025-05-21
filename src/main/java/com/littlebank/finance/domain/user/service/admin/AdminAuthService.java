package com.littlebank.finance.domain.user.service.admin;

import com.littlebank.finance.domain.user.dto.request.LoginRequest;
import com.littlebank.finance.domain.user.dto.response.LoginResponse;
import com.littlebank.finance.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String accessToken = tokenProvider.provideAccessTokenForAdmin(authentication);
        return LoginResponse.of(accessToken);
    }
}
