package com.littlebank.finance.global.toss;


import com.littlebank.finance.domain.point.dto.request.ConfirmPaymentRequest;
import com.littlebank.finance.global.toss.config.TossProperties;
import com.littlebank.finance.global.toss.dto.PaymentConfirmResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossService {
    private final TossProperties tossProperties;
    private RestClient restClient;

    @PostConstruct
    private void initRestClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.tosspayments.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64.getEncoder().encodeToString((tossProperties.getSecretKey() + ":").getBytes(StandardCharsets.UTF_8))
                )
                .build();
    }

    public ResponseEntity<PaymentConfirmResponse> confirm(ConfirmPaymentRequest request) {
        Map<String, Object> payload = Map.of(
                "paymentKey", request.getPaymentKey(),
                "orderId", request.getOrderId(),
                "amount", request.getAmount()
        );

        ResponseEntity<PaymentConfirmResponse> result = restClient.post()
                .uri("/v1/payments/confirm")
                .body(payload)
                .retrieve()
                .toEntity(PaymentConfirmResponse.class);

        log.info("Toss 결제 승인 응답 StatusCode: {}", result.getStatusCode());
        log.info("Toss 결제 승인 응답 Headers: {}", result.getHeaders());
        log.info("Toss 결제 승인 응답 Body: {}", result.getBody());

        return result;
    }

    public void cancelPayment(String paymentKey, int cancelAmount, String reason) {
        Map<String, Object> body = Map.of(
                "cancelReason", reason,
                "cancelAmount", cancelAmount
        );

        restClient.post()
                .uri("/v1/payments/{paymentKey}/cancel", paymentKey)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

}
