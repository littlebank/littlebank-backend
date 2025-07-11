package com.littlebank.finance.domain.school.domain.repository;

import com.littlebank.finance.domain.school.dto.response.SchoolSearchResponse;
import java.util.Optional;
public interface CustomSchoolRepository {
    Optional<SchoolSearchResponse> findSchoolSearchResponse(String schoolName);
}
