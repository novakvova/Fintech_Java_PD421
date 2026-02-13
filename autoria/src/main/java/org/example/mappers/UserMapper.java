package org.example.mappers;

import org.example.dto.user.RegisterUserDTO;
import org.example.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserEntity fromRegisterDTO(RegisterUserDTO dto);
}
