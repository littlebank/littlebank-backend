package com.littlebank.finance.domain.school.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class SchoolSyncResponse {
    private Integer totalSaved;
    private Map<String, Integer> savedByGubun;
    private String message;
}
