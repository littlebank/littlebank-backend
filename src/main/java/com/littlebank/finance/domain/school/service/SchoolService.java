package com.littlebank.finance.domain.school.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.littlebank.finance.domain.school.domain.RegionCode;
import com.littlebank.finance.domain.school.domain.School;
import com.littlebank.finance.domain.school.domain.repository.SchoolRepository;
import com.littlebank.finance.domain.school.dto.response.SchoolSyncResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${career.api.key}")
    private String apiKey;

    public SchoolSyncResponse fetchAndSaveAllSchools(int perPage) {
        Map<String, Integer> savedByGubun = new HashMap<>();
        Integer totalSaved = 0;
        List<String> gubunList = List.of("elem_list", "midd_list", "high_list");

        for (String gubun : gubunList) {
            int thisPage = 1;
            Integer saved = 0;

            while (true) {

                String url = UriComponentsBuilder.fromHttpUrl("https://www.career.go.kr/cnet/openapi/getOpenApi.json")
                        .queryParam("apiKey", apiKey)
                        .queryParam("svcType", "api")
                        .queryParam("svcCode", "SCHOOL")
                        .queryParam("contentType", "json")
                        .queryParam("gubun", gubun)
                        .queryParam("thisPage", thisPage)
                        .queryParam("perPage", perPage)
                        .toUriString();

                ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
                JsonNode contents = response.getBody().path("dataSearch").path("content");

                // 종료
                if (!contents.isArray() || contents.isEmpty()) {
                    log.info("더 이상 데이터 없음. gubun={}, page={}", gubun, thisPage);
                    break;
                }

                for (JsonNode node : contents) {
                    String name = node.path("schoolName").asText();
                    String address = node.path("adres").asText();

                    if (schoolRepository.existsByNameAndAddress(name, address)) continue;

                    int regionCode = RegionCode.getRegionCode(node.path("region").asText());
                    School school = School.of(
                            name,
                            gubun,
                            node.path("schoolType").asText(),
                            node.path("estType").asText(),
                            regionCode,
                            address,
                            node.path("link").asText()
                    );
                    schoolRepository.save(school);
                    saved++;
                }

                thisPage++;
            }

            savedByGubun.put(gubun, saved);
            totalSaved += saved;
        }
        return SchoolSyncResponse.builder()
                .totalSaved(totalSaved)
                .savedByGubun(savedByGubun)
                .message("초중고 학교 데이터 저장 완료")
                .build();
    }
}