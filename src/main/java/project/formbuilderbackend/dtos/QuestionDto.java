package project.formbuilderbackend.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import project.formbuilderbackend.entities.QuestionType;

@Data
@NoArgsConstructor
public class QuestionDto {
    Long id;
    String label;
    Boolean required;
    QuestionType questionType;
}