package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.UserAccountDto;
import com.bodytok.healthdiary.dto.userAccount.UserProfile;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PersonalExerciseDiaryService diaryService;

    @Transactional(readOnly = true)
    public UserAccount getUserById(Long id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));
    }

    @Transactional(readOnly = true)
    public UserAccount getUserByEmail(String email) {
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));
    }

    @Transactional(readOnly = true)
    public Boolean checkUserByEmail(String email) {
        return userAccountRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Boolean checkUserByNickname(String nickname) {
        return userAccountRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public UserProfile getUserProfile(Long userId) {
        //1.유저 조회
        //2. 다이어리 카운트 조회 후 profileInfo로 매핑
        UserAccount userAccount = this.getUserById(userId);
        int diaryCount = diaryService.getDiaryCount(userId);

        UserProfile userProfile = UserProfile.toProfileInfo(userAccount, diaryCount);
        return userProfile;
    }

}
