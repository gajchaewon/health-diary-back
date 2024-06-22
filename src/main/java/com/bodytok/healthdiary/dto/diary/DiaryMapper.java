package com.bodytok.healthdiary.dto.diary;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DiaryMapper {

    DiaryMapper INSTANCE = Mappers.getMapper(DiaryMapper.class);
//
//    @Mapping(target = "userAccountDto", source = "userAccountDto")
//    @Mapping(target = "hashtagDtoSet", source = "request.hashtags")
//    @Mapping(target = "imageDtoSet", source = "request.imageIds")
//    DiaryDto toDtoFromRequest(DiaryRequest request, UserAccountDto userAccountDto);
//
//
//    default List<DiaryImageDto> mapImageIds(Set<Long> imageIds) {
//        return imageIds.stream()
//                .map(id -> new DiaryImageDto(id,null,null,null,null))
//                .collect(Collectors.toList());
//    }

}
