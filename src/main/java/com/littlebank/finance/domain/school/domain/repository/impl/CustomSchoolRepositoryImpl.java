package com.littlebank.finance.domain.school.domain.repository.impl;

import com.littlebank.finance.domain.school.domain.QSchool;
import com.littlebank.finance.domain.school.domain.repository.CustomSchoolRepository;
import com.littlebank.finance.domain.school.dto.response.SchoolInfoResponse;
import com.littlebank.finance.domain.school.dto.response.SchoolSearchResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.littlebank.finance.domain.school.domain.QSchool.school;

@RequiredArgsConstructor
public class CustomSchoolRepositoryImpl implements CustomSchoolRepository {

    private final JPAQueryFactory queryFactory;
    private final QSchool sch = school;

    @Override
    public Optional<SchoolSearchResponse> findSchoolSearchResponse(String schoolName) {
        List<SchoolInfoResponse> results = queryFactory
                .select(Projections.constructor(
                        SchoolInfoResponse.class,
                        sch.name,
                        sch.address))
                .from(sch)
                .where(sch.name.contains(schoolName))
                .orderBy(sch.name.asc())
                .limit(10)
                .fetch();

        int total = results.size();
        return Optional.of(new SchoolSearchResponse(total, results));
    }
}
