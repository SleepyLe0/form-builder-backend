package project.formbuilderbackend;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
