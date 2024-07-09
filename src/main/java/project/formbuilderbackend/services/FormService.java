package project.formbuilderbackend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.formbuilderbackend.dtos.FormDto;
import project.formbuilderbackend.entities.user.UserEntity;
import project.formbuilderbackend.repositories.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<FormDto> findAllByCurrentUser(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        return formRepository.findAllByUserId(user.getId()).stream().map(form -> modelMapper.map(form, FormDto.class)).toList();
    }
}
