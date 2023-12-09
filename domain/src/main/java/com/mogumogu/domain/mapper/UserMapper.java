package com.mogumogu.domain.mapper;

import com.mogumogu.domain.UserEntity;
import com.mogumogu.domain.dto.UserDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {

    /**
     * Entity -> Dto
     */
    UserDto.UserResponseDto toResponseDto(UserEntity userEntity);

    /**
     * Dto -> Entity
     */
    @Mapping(target="id", ignore = true)
    @Mapping(target="role", ignore = true)
    @Mapping(target = "emailCode", ignore = true)
    UserEntity toRequestEntity(UserDto.UserRequestDto userRequestDto);



}
