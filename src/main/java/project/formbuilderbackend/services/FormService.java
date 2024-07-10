package project.formbuilderbackend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.formbuilderbackend.dtos.form.FormDto;
import project.formbuilderbackend.dtos.form.FormListDto;
import project.formbuilderbackend.dtos.form.FormRequestDto;
import project.formbuilderbackend.dtos.question.QuestionRequestDto;
import project.formbuilderbackend.entities.form.Form;
import project.formbuilderbackend.entities.form.FormStatus;
import project.formbuilderbackend.entities.question.*;
import project.formbuilderbackend.entities.user.UserEntity;
import project.formbuilderbackend.repositories.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final TextQuestionRepository textQuestionRepository;
    private final MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;
    private final ChoiceRepository choiceRepository;
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
                .build();
        return modelMapper.map(formRepository.save(form), FormDto.class);
    }

    public FormDto updateFormDetails(Long formId, FormRequestDto newForm, String username) {
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

    public FormDto updateFormQuestions(Long formId, List<QuestionRequestDto> questions, String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Form form = formRepository.findByIdAndUser(formId, user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found")
        );
        updateQuestions(questions, form);
        Form updatedForm = formRepository.findByIdAndUser(formId, user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found")
        );
        return modelMapper.map(updatedForm, FormDto.class);
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

    private void updateQuestions(List<QuestionRequestDto> questions, Form form) {
        for (QuestionRequestDto questionDto : questions) {
            Question question = questionRepository.findById(questionDto.getId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found")
            );
            if (!question.getForm().getId().equals(form.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question does not belong to the form");
            }
            question.setLabel(questionDto.getLabel());
            question.setRequired(questionDto.getRequired());
            if (question.getQuestionType().getType().equalsIgnoreCase("TEXT")) {
                TextQuestion textQuestion = (TextQuestion) question;
                questionRepository.save(textQuestion);
            } else if (question.getQuestionType().getType().equalsIgnoreCase("MULTIPLE_CHOICE")) {
                MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) question;
                multipleChoiceQuestion.setMultipleAnswers(questionDto.getMultipleAnswers());
                multipleChoiceQuestion.setMultipleType(questionDto.getMultipleAnswers() ? questionDto.getMultipleType() : null);
                multipleChoiceQuestion.setChooseLimit(questionDto.getMultipleAnswers() ? questionDto.getChooseLimit() : null);
                List<Choice> choices = choiceRepository.findByQuestionId(questionDto.getId());
                if (choices.size() < questionDto.getChoices().size()) {
                    for (int i = 0; i < choices.size(); i++) {
                        choices.get(i).setLabel(questionDto.getChoices().get(i));
                        choiceRepository.save(choices.get(i));
                    }
                    for (int i = choices.size(); i < questionDto.getChoices().size(); i++) {
                        Choice choice = Choice.builder()
                                .id(null)
                                .question(multipleChoiceQuestion)
                                .label(questionDto.getChoices().get(i))
                                .build();
                        choiceRepository.save(choice);
                    }
                } else if (choices.size() > questionDto.getChoices().size()) {
                    for (int i = 0; i < questionDto.getChoices().size(); i++) {
                        choices.get(i).setLabel(questionDto.getChoices().get(i));
                        choiceRepository.save(choices.get(i));
                    }
                    for (int i = questionDto.getChoices().size(); i < choices.size(); i++) {
                        choiceRepository.delete(choices.get(i));
                    }
                } else {
                    for (int i = 0; i < choices.size(); i++) {
                        choices.get(i).setLabel(questionDto.getChoices().get(i));
                        choiceRepository.save(choices.get(i));
                    }
                }
                questionRepository.save(multipleChoiceQuestion);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid question type");
            }
        }
    }
}
