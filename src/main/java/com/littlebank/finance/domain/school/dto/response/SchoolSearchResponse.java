package com.littlebank.finance.domain.school.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SchoolSearchResponse {
    private Integer totalCount;
    private List<SchoolInfoResponse> results;
}
