package com.bodytok.healthdiary.dto.userAccount;


import com.bodytok.healthdiary.dto.auth.request.UserLogin;
import com.bodytok.healthdiary.dto.auth.request.UserRegister;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserAccountMapper {

    UserAccountMapper INSTANCE = Mappers.getMapper(UserAccountMapper.class);

    @Mapping(target = "userPassword", source = "password")
    UserAccountDto toDtoFromLogin(UserLogin login);

    @Mapping(target = "userPassword", source = "password")
    UserAccountDto toDtoFromRegister(UserRegister register);
}
