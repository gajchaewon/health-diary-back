package com.bodytok.healthdiary.dto.hashtag;

import com.bodytok.healthdiary.domain.Hashtag;

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

    public Hashtag toEntity(){
        return Hashtag.of(hashtag);
    }

}
