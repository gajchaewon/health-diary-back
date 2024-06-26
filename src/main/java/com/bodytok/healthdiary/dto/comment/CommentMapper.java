package com.bodytok.healthdiary.dto.comment;


import com.bodytok.healthdiary.dto.auth.response.UserResponse;
import com.bodytok.healthdiary.dto.comment.request.CommentUpdate;
import com.bodytok.healthdiary.dto.comment.response.CommentResponse;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import com.bodytok.healthdiary.dto.comment.request.CommentCreate;
import com.bodytok.healthdiary.dto.diary.DiaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
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
    @Mapping(target = "userInfo", expression = "java(mapToUerInfo(commentDto))")
    CommentResponse toResponse(CommentDto commentDto);
    default UserResponse mapToUerInfo(CommentDto dto) {
        return UserResponse.from(dto.userAccountDto());
    }

}
