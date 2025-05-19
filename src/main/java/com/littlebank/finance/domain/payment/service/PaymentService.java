package com.littlebank.finance.domain.payment.service;

import com.google.gson.JsonObject;
import com.littlebank.finance.domain.payment.domain.Payment;
import com.littlebank.finance.domain.payment.domain.PaymentStatus;
import com.littlebank.finance.domain.payment.domain.repository.PaymentRepository;
import com.littlebank.finance.domain.payment.dto.PortonePaymentDto;
import com.littlebank.finance.domain.payment.dto.PortoneTokenDto;
import com.littlebank.finance.domain.payment.dto.request.PaymentInfoSaveRequest;
import com.littlebank.finance.domain.payment.dto.response.PaymentInfoSaveResponse;
import com.littlebank.finance.domain.payment.exception.PaymentException;
import com.littlebank.finance.domain.user.domain.User;
import com.littlebank.finance.domain.user.domain.repository.UserRepository;
import com.littlebank.finance.domain.user.exception.UserException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
    private static final String TOKEN_URL = "https://api.iamport.kr/users/getToken";
    private static final String PAYMENT_INFO_URL = "https://api.iamport.kr/payments/";
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RestClient restClient;
    @Value("${portone.api-key}")
    private String apiKey;
    @Value("${portone.api-secret}")
    private String apiSecret;

    public PaymentInfoSaveResponse verifyAndSave(Long userId, PaymentInfoSaveRequest request) {
        String token = getAccessToken();
        PortonePaymentDto paymentDto = getPaymentInfo(request.getImpUid(), token);

        if (paymentDto.getStatus() != PaymentStatus.PAID) {
            throw new PaymentException(ErrorCode.PAYMENT_STATUS_NOT_PAID);
        }

        if (paymentRepository.existsByImpUid(request.getImpUid())) {
            throw new PaymentException(ErrorCode.PAYMENT_ALREADY_EXISTS); // 중복 방지
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        Payment payment = paymentRepository.save(Payment.builder()
                .impUid(paymentDto.getImpUid())
                .merchantUid(paymentDto.getMerchantUid())
                .amount(paymentDto.getAmount())
                .payMethod(paymentDto.getPayMethod())
                .pgProvider(paymentDto.getPgProvider())
                .status(paymentDto.getStatus())
                .paidAt(LocalDateTime.now())
                .user(user)
                .isDeleted(Boolean.FALSE)
                .build());

        user.addPoint(paymentDto.getAmount());

        return PaymentInfoSaveResponse.of(payment);
    }

    private String getAccessToken() {
        JsonObject body = new JsonObject();
        body.addProperty("imp_key", apiKey);
        body.addProperty("imp_secret", apiSecret);

        PortoneTokenDto dto = restClient.post()
                .uri(TOKEN_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(body.toString())
                .retrieve()
                .body(PortoneTokenDto.class);

        return dto.getResponse().getAccessToken();
    }

    private PortonePaymentDto getPaymentInfo(String impUid, String token) {
        Map<String, Object> data = (Map<String, Object>) restClient.get()
                .uri(PAYMENT_INFO_URL + impUid)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(Map.class)
                .get("response");

        return PortonePaymentDto.of(data);
    }
}
