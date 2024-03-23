package com.bodytok.healthdiary.service;


import com.bodytok.healthdiary.domain.Hashtag;
import com.bodytok.healthdiary.dto.hashtag.HashtagDto;
import com.bodytok.healthdiary.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    @Transactional(readOnly = true)
    public Set<Hashtag> findAllByHashtags(Set<String> hashtags){
        return new HashSet<>(hashtagRepository.findHashtagsByHashtagIn(hashtags));
    }


    public Set<Hashtag> renewHashtagsFromRequest(Set<HashtagDto> hashtagDtoSet) {
        //요청으로 들어온 hashtagSet 중 일치하는 모든 해시태그들 가져오기
        Set<Hashtag> hashtags = this.findAllByHashtags(hashtagDtoSet.stream()
                .map(HashtagDto::hashtag).collect(Collectors.toUnmodifiableSet()));

        //Set<Hashtag> -> Set<String>
        Set<String> existingHashtagString = hashtags.stream()
                .map(Hashtag::getHashtag)
                .collect(Collectors.toUnmodifiableSet());

        Set<String> requestHashtagSet = hashtagDtoSet.stream()
                .map(HashtagDto::hashtag).collect(Collectors.toUnmodifiableSet());

        //새로운 해시태그만 인스턴스 생성하여 Set에 추가
        requestHashtagSet.forEach(newHashtagName -> {
            if (!existingHashtagString.contains(newHashtagName)) {
                hashtags.add(Hashtag.of(newHashtagName));
            }
        });

        return Collections.unmodifiableSet(hashtags);
    }

}
