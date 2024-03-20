package com.bodytok.healthdiary.repository;

import com.bodytok.healthdiary.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);
}