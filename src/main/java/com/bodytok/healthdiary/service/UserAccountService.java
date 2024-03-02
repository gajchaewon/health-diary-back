package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;


    public UserAccountDto getUserByEmail(String email) {
        return userAccountRepository.findByEmail(email)
                .map(UserAccountDto::from)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));
    }

}
