package com.littlebank.finance.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AccountHolderVerifyRequest {
    @NotBlank
    @Schema(description = "은행 고유 코드", example = "011")
    private String bankCode;
    @NotBlank
    @Schema(description = "계좌 번호", example = "3021469002611")
    private String bankNumber;
    @NotBlank
    @Schema(description = "예금주 명", example = "장태현")
    private String holderName;
}
