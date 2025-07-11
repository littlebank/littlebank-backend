package com.littlebank.finance.domain.school.service;


import com.littlebank.finance.domain.school.domain.repository.SchoolRepository;
import com.littlebank.finance.domain.school.dto.response.SchoolSearchResponse;
import com.littlebank.finance.domain.school.exception.SchoolException;
import com.littlebank.finance.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public SchoolSearchResponse searchSchoolName(String schoolName) {
        return schoolRepository.findSchoolSearchResponse(schoolName)
                .orElseThrow(() -> new SchoolException(ErrorCode.SCHOOL_NOT_FOUND));
    }
}
