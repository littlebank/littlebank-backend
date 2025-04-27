package com.littlebank.finance.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
@Builder
@AllArgsConstructor
public class EmailSendResponse {

    private String toEmail;

    public static EmailSendResponse of(String toEmail) {
        return EmailSendResponse.builder()
                .toEmail(toEmail)
                .build();
    }
}
