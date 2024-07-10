package project.formbuilderbackend.dtos.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import project.formbuilderbackend.entities.form.FormStatus;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class FormListDto {
    Long id;
    String title;
    FormStatus status;
    ZonedDateTime createdAt;
}