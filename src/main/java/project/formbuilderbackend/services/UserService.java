package project.formbuilderbackend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.formbuilderbackend.dtos.UserDto;
import project.formbuilderbackend.entities.UserEntity;
import project.formbuilderbackend.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userEntity -> modelMapper.map(userEntity, UserDto.class)).toList();
    }

    public UserDto findById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        return modelMapper.map(userEntity, UserDto.class);
    }
}
