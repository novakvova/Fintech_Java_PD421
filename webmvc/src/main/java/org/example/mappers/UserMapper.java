package org.example.mappers;

import org.example.dtos.user.UserRegisterDTO;
import org.example.entities.UserEntity;
import org.example.services.FileService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public class UserMapper {
    @Autowired
    private FileService fileService;

    public UserEntity fromCreateDto(UserRegisterDTO dto) {
        if (dto == null)return null;
        UserEntity user = new UserEntity();
        if(dto.getUsername() == null) return null;
        if(dto.getEmail() == null) return null;
        if(dto.getPassword() == null) return null;

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        if (dto.getImage() != null){
            String fileName = fileService.load(dto.getImage());
            user.setImage(fileName);
        }
        return user;
    }
}
