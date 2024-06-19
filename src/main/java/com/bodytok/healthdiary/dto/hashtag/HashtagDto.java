package com.bodytok.healthdiary.dto.hashtag;

import com.bodytok.healthdiary.domain.Hashtag;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public record HashtagDto(
        Long id,
        String hashtag
) {

    public static HashtagDto of(String hashtag) {
        return new HashtagDto(null,hashtag);
    }

    public static HashtagDto of(Long id, String hashtag) {
        return new HashtagDto(id,hashtag);
    }

    public static  HashtagDto from(Hashtag entity) {
        return new HashtagDto(
                entity.getId(),
                entity.getHashtag()
        );
    }

    public static Set<HashtagDto> mapFromHashtagString(Set<String> hashtags) {
        return hashtags != null ?
                hashtags.stream().map(HashtagDto::of).collect(Collectors.toUnmodifiableSet()) :
                Collections.emptySet();
    }

    public Hashtag toEntity(){
        return Hashtag.of(hashtag);
    }

}
