package com.littlebank.finance.domain.school.controller;

import com.littlebank.finance.domain.school.dto.response.SchoolSearchResponse;
import com.littlebank.finance.domain.school.service.SchoolService;
import com.littlebank.finance.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-user/school")
@RequiredArgsConstructor
@Tag(name = "School")
public class SchoolController {
    private final SchoolService schoolService;

    @Operation(summary = "학교 검색")
    @GetMapping("/get-schoolList")
    public ResponseEntity<SchoolSearchResponse> getAllSchoolInfo (
            @Parameter(
                    name = "schoolName",
                    description = "학교명 키워드",
                    required = true,
                    in = ParameterIn.QUERY,
                    example = "진주"
            )
            @RequestParam(value = "schoolName") String schoolName,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        SchoolSearchResponse response = schoolService.searchSchoolName(schoolName);
        return ResponseEntity.ok(response);
    }
}
