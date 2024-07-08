package project.formbuilderbackend;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.formbuilderbackend.dtos.MultipleChoiceQuestionDto;
import project.formbuilderbackend.dtos.QuestionDto;
import project.formbuilderbackend.dtos.TextQuestionDto;
import project.formbuilderbackend.entities.MultipleChoiceQuestion;
import project.formbuilderbackend.entities.Question;
import project.formbuilderbackend.entities.TextQuestion;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.createTypeMap(Question.class, QuestionDto.class)
//                .include(TextQuestion.class, TextQuestionDto.class)
//                .include(MultipleChoiceQuestion.class, MultipleChoiceQuestionDto.class);
        return modelMapper;
    }
}
