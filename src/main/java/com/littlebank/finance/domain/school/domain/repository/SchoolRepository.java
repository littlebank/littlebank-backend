package com.littlebank.finance.domain.school.domain.repository;

import com.littlebank.finance.domain.school.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
    boolean existsByNameAndAddress(String name, String address);
}
