package com.bodytok.healthdiary.dto.comment;


import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.dto.comment.request.CommentCreate;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import com.bodytok.healthdiary.dto.diary.response.DiaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CommentMapper {


    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    //request
    @Mapping(target = "userAccountDto", source = "userAccountDto")
    @Mapping(target = "diaryDto", source = "commentCreate.diaryId", qualifiedByName = "mapDiaryIdToDto")
    CommentDto toDtoFromCreate(CommentCreate commentCreate, UserAccountDto userAccountDto);

    @Named("mapDiaryIdToDto")
    default DiaryDto mapDiaryIdToDto(Long diaryId) {
        return DiaryDto.builder().id(diaryId).build();
    }

    @Mapping(target = "userAccountDto", source = "userAccountDto")
    @Mapping(target = "id", source = "commentId")
    CommentDto toDtoFromUpdate(Long commentId, CommentUpdate update, UserAccountDto userAccountDto);

    //response
    @Mapping(target = "userId", source = "userAccountDto.id")
    @Mapping(target = "email",source = "userAccountDto.email")
    @Mapping(target = "nickname", expression = "java(getNickname(commentDto))")
    CommentResponse toResponse(CommentDto commentDto);
    default String getNickname(CommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        return (nickname == null || nickname.isBlank()) ? dto.userAccountDto().email() : nickname;
    }


    @Mapping( target = "userId", source = "userAccountDto.id")
    @Mapping(target = "userEmail", source = "userAccountDto.email")
    @Mapping(target = "diary", source = "diaryDto",  qualifiedByName = "mapDiaryResponse")
    CommentWithDiaryResponse  toCommentWithDiaryResponse(CommentDto dto);

    @Named("mapDiaryResponse")
    default DiaryResponse mapDiaryResponse(DiaryDto diaryDto) {
        return DiaryResponse.from(diaryDto);
    }


}
