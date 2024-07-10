package project.formbuilderbackend.dtos.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import project.formbuilderbackend.dtos.question.QuestionDto;
import project.formbuilderbackend.entities.form.FormStatus;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class FormDto {
    Long id;
    String title;
    String description;
    FormStatus status;
    ZonedDateTime createdAt;
    List<QuestionDto> questions;
}