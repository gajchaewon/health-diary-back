package com.bodytok.healthdiary.dto.diary;


import com.bodytok.healthdiary.dto.diary.request.DiaryRequest;
import com.bodytok.healthdiary.dto.diaryImage.DiaryImageDto;
import com.bodytok.healthdiary.dto.hashtag.HashtagMapper;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(
        unmappedTargetPolicy = ReportingPolicy.WARN,
        uses = {HashtagMapper.class}
)
public interface DiaryMapper {

    DiaryMapper INSTANCE = Mappers.getMapper(DiaryMapper.class);

    @Mapping(target = "userAccountDto", source = "userAccountDto")
    @Mapping(target = "hashtagDtoSet", source = "request.hashtags")
    @Mapping(target = "imageDtoSet", source = "request.imageIds")
    DiaryDto toDtoFromRequest(DiaryRequest request, UserAccountDto userAccountDto);


    default List<DiaryImageDto> mapImageIds(Set<Long> imageIds) {
        return imageIds.stream()
                .map(id -> new DiaryImageDto(id,null,null,null,null))
                .collect(Collectors.toList());
    }

}
