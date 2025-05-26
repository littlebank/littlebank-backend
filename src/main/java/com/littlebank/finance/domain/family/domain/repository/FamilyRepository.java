package com.littlebank.finance.domain.family.domain.repository;

import com.littlebank.finance.domain.family.domain.Family;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Long>, CustomFamilyRepository {
}
