package project.formbuilderbackend.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import project.formbuilderbackend.entities.FormStatus;

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
//    List<QuestionDto> questions;
}