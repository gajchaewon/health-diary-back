package com.bodytok.healthdiary.dto.exercise_routine;


import com.bodytok.healthdiary.dto.exercise_routine.request.RoutineCreate;
import com.bodytok.healthdiary.dto.exercise_routine.request.RoutineUpdate;
import com.bodytok.healthdiary.dto.userAccount.UserAccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RoutineMapper {


    RoutineMapper INSTANCE = Mappers.getMapper(RoutineMapper.class);


    RoutineDto toDtoFromCreate(RoutineCreate create);


    @Mapping(target = "id", source = "routineId")
    @Mapping(target = "userAccountDto", source = "userAccountDto")
    RoutineDto toDtoFromUpdate(RoutineUpdate update, Long routineId, UserAccountDto userAccountDto);

    @Mapping(target = "id", source = "routineId")
    @Mapping(target = "userAccountDto", source = "userAccountDto")
    RoutineDto toDtoFromDelete(Long routineId, UserAccountDto userAccountDto);

}
