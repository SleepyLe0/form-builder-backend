package project.formbuilderbackend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.formbuilderbackend.dtos.form.FormDto;
import project.formbuilderbackend.dtos.form.FormListDto;
import project.formbuilderbackend.dtos.form.FormRequestDto;
import project.formbuilderbackend.entities.form.Form;
import project.formbuilderbackend.entities.form.FormStatus;
import project.formbuilderbackend.entities.user.UserEntity;
import project.formbuilderbackend.repositories.*;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<FormListDto> findAllByCurrentUser(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        return formRepository.findAllByUserId(user.getId()).stream().map(form -> modelMapper.map(form, FormListDto.class)).toList();
    }

    public FormDto findById(Long formId, String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Form form = formRepository.findByIdAndUser(formId, user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found")
        );
        return modelMapper.map(form, FormDto.class);
    }

    public FormDto createForm(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Form form = Form.builder()
                .title("Untitled Form")
                .status(FormStatus.DRAFT)
                .user(user)
                .questions(List.of())
                .createdAt(ZonedDateTime.now())
                .build();
        return modelMapper.map(formRepository.save(form), FormDto.class);
    }

    public FormDto updateForm(Long formId, FormRequestDto newForm, String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Form form = formRepository.findByIdAndUser(formId, user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found")
        );
        form.setTitle(newForm.getTitle());
        form.setDescription(newForm.getDescription());
        form.setStatus(newForm.getStatus());
        return modelMapper.map(formRepository.save(form), FormDto.class);
    }

    public void deleteForm(Long formId, String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Form form = formRepository.findByIdAndUser(formId, user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found")
        );
        formRepository.delete(form);
    }
}
