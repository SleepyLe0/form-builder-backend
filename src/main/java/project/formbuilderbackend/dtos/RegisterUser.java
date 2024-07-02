package project.formbuilderbackend.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterUser {
    private String username;
    private String password;
}
