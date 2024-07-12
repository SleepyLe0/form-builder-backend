package project.formbuilderbackend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.formbuilderbackend.dtos.question.QuestionDto;
import project.formbuilderbackend.dtos.question.QuestionRequestDto;
import project.formbuilderbackend.entities.form.Form;
import project.formbuilderbackend.entities.question.*;
import project.formbuilderbackend.entities.user.UserEntity;
import project.formbuilderbackend.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final UserRepository userRepository;
    private final FormRepository formRepository;
    private final ModelMapper modelMapper;
    private final QuestionTypeRepository questionTypeRepository;

    public QuestionDto createQuestion(Long formId, String type, String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Form form = formRepository.findByIdAndUser(formId, user).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form not found")
        );
        QuestionType questionType = questionTypeRepository.findByType(type.toUpperCase()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question type not found")
        );
        Question question = switch (questionType.getType()) {
            case "TEXT" -> TextQuestion.builder().build();
            case "MULTIPLE_CHOICE" -> MultipleChoiceQuestion.builder()
                    .multipleAnswers(false)
                    .build();
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question type not found");
        };
        question.setLabel("Untitled Question");
        question.setRequired(false);
        question.setForm(form);
        question.setQuestionType(questionType);
        Question savedQuestion = questionRepository.save(question);
        if (question instanceof TextQuestion) {
            savedQuestion.setTextQuestion((TextQuestion) savedQuestion);
        } else if (question instanceof MultipleChoiceQuestion) {
            createChoiceQuestion((MultipleChoiceQuestion) savedQuestion);
            savedQuestion.setMultipleChoiceQuestion((MultipleChoiceQuestion) savedQuestion);
        }
        return modelMapper.map(savedQuestion, QuestionDto.class);
    }

    public QuestionDto updateQuestion(Long questionId, QuestionRequestDto questionDto, String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found")
        );
        question.setLabel(questionDto.getLabel());
        question.setRequired(questionDto.getRequired());
        switch (question.getQuestionType().getType()) {
            case "TEXT" :
                TextQuestion textQuestion = (TextQuestion) question;
                question = questionRepository.save(textQuestion);
                break;
            case "MULTIPLE_CHOICE" :
                MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) question;
                multipleChoiceQuestion.setMultipleAnswers(questionDto.getMultipleAnswers());
                multipleChoiceQuestion.setMultipleType(questionDto.getMultipleAnswers() ? questionDto.getMultipleType() : null);
                multipleChoiceQuestion.setChooseLimit(questionDto.getMultipleAnswers() ? questionDto.getChooseLimit() : null);
                updateChoiceQuestion(multipleChoiceQuestion, questionDto);
                question = questionRepository.save(multipleChoiceQuestion);
                break;
        }
        return modelMapper.map(question, QuestionDto.class);
    }

    public void deleteQuestion(Long questionId, String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found")
        );
        if (!question.getForm().getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this question");
        }
        questionRepository.delete(question);
    }

    private void createChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion) {
        List<Choice> choices = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Choice choice = Choice.builder()
                    .question(multipleChoiceQuestion)
                    .label("Option " + (i + 1))
                    .build();
            choices.add(choice);
            choiceRepository.save(choice);
        }
        multipleChoiceQuestion.setChoices(choices);
    }

    private void updateChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion, QuestionRequestDto questionDto) {
        List<Choice> choices = choiceRepository.findByQuestionId(questionDto.getId());
        if (choices.size() > questionDto.getChoices().size()) {
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
            if (choices.size() < questionDto.getChoices().size()) {
                for (int i = choices.size(); i < questionDto.getChoices().size(); i++) {
                    Choice choice = Choice.builder()
                            .question(multipleChoiceQuestion)
                            .label(questionDto.getChoices().get(i))
                            .build();
                    choiceRepository.save(choice);
                }
            }
        }
    }
}
