package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.DiaryLike;
import com.bodytok.healthdiary.exepction.CustomBaseException;
import com.bodytok.healthdiary.repository.DiaryLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bodytok.healthdiary.exepction.CustomError.LIKE_NOT_FOUND;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {

    private final DiaryLikeRepository likeRepository;

    @Transactional(readOnly = true)
    public Page<DiaryLike> getLikesByUserId(Long userId, Pageable pageable) {
        return likeRepository.findByUserAccount_Id(userId, pageable)
                .orElseThrow(() -> new CustomBaseException(LIKE_NOT_FOUND));
    }
}
