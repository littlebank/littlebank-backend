package com.littlebank.finance.global.portone;

import com.google.gson.JsonObject;
import com.littlebank.finance.global.error.exception.ErrorCode;
import com.littlebank.finance.global.portone.dto.AccountHolderDto;
import com.littlebank.finance.global.portone.dto.PortoneTokenDto;
import com.littlebank.finance.global.portone.config.PortoneProperties;
import com.littlebank.finance.global.portone.exception.PortoneException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PortoneService {
    private static final String BASE_URL = "https://api.iamport.kr";
    private final RestClient restClient;
    private final PortoneProperties portoneProperties;

    public String getAccessToken() {
        JsonObject body = new JsonObject();
        body.addProperty("imp_key", portoneProperties.getApiKey());
        body.addProperty("imp_secret", portoneProperties.getApiSecret());

        PortoneTokenDto dto = restClient.post()
                .uri(BASE_URL + "/users/getToken")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(body.toString())
                .retrieve()
                .body(PortoneTokenDto.class);

        return dto.getResponse().getAccessToken();
    }

    /**
     * 예금주 조회
     */
    public AccountHolderDto getAccountHolder(String bankCode, String bankNumber, String accessToken) {
        Map<String, Object> data = (Map<String, Object>) restClient.get()
                .uri(UriComponentsBuilder
                        .fromHttpUrl(BASE_URL + "/vbanks/holder")
                        .queryParam("bank_code", bankCode)
                        .queryParam("bank_num", bankNumber)
                        .toUriString()
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(Map.class)
                .get("response");

        if (data == null || data.isEmpty()) {
            throw new PortoneException(ErrorCode.ACCOUNT_INFO_NOT_FOUND);
        }

        return AccountHolderDto.of(data);
    }
}
