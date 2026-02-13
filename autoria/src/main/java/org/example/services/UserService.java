package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.user.RegisterUserDTO;
import org.example.entities.UserEntity;
import org.example.mappers.UserMapper;
import org.example.repository.IRoleRepository;
import org.example.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final FileService fileService;
    private final UserMapper userMapper;

    public String registerUser(RegisterUserDTO dto) {
        UserEntity user = userMapper.fromRegisterDTO(dto);

        var file = dto.getImageFile();
        if (file != null) {
            String fileName = fileService.load(file);
            user.setImage(fileName);
        }

        return null;
    }
}
