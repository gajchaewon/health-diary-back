package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.UserAccount;
import com.bodytok.healthdiary.dto.userAccount.response.UserProfile;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bodytok.healthdiary.exepction.CustomError.*;

@Transactional
@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PersonalExerciseDiaryService diaryService;

    @Transactional(readOnly = true)
    public UserAccount getUserById(Long id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new CustomBaseException(USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public UserAccount getUserByEmail(String email) {
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new CustomBaseException(USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Boolean checkUserByEmail(String email) {
        return userAccountRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Boolean checkUserByNickname(String nickname) {
        return userAccountRepository.existsByNickname(nickname);
    }


    // TODO : 팔로우 카운트만 조회하려면 커스텀 쿼리를 작성해야함.
    @Transactional(readOnly = true)
    public UserProfile getUserProfile(Long myId, Long userId) {
        //1.유저 조회
        //2. 다이어리 카운트 조회 후 profileInfo로 매핑
        UserAccount userAccount = this.getUserById(userId);

        Integer diaryCount = diaryService.getDiaryCount(userId);

        return UserProfile.toProfileInfo(userAccount, diaryCount, myId);
    }
}
