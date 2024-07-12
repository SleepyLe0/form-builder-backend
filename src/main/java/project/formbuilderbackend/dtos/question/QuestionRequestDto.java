package project.formbuilderbackend.dtos.question;

import lombok.Data;
import lombok.NoArgsConstructor;
import project.formbuilderbackend.entities.question.*;

import java.util.List;

@Data
@NoArgsConstructor
public class QuestionRequestDto {
    Long id;
    String label;
    Boolean required;
    Boolean multipleAnswers;
    MultipleChooseType multipleType;
    Integer chooseLimit;
    List<String> choices;
}