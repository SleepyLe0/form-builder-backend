package project.formbuilderbackend.dtos.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import project.formbuilderbackend.entities.form.FormStatus;

@Data
@NoArgsConstructor
public class FormRequestDto {
    Long id;
    String title;
    String description;
    FormStatus status;
}