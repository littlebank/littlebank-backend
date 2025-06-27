package com.littlebank.finance.domain.user.domain.repository;

import com.littlebank.finance.domain.user.domain.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByEmailAndIdNot(String email, Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdWithLock(@Param("id") Long userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByBankCodeAndBankAccount(String bankCode, String account);
}
