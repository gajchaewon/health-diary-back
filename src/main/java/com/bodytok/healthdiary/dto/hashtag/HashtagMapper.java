package com.bodytok.healthdiary.dto.hashtag;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper()
public interface HashtagMapper {
    HashtagMapper INSTANCE = Mappers.getMapper(HashtagMapper.class);

    default Set<HashtagDto> mapHashtagStrings(Set<String> hashtagString) {
        return hashtagString.stream()
                .map(hashtag -> new HashtagDto(null, hashtag))
                .collect(Collectors.toSet());
    }
}
