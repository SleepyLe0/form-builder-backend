package project.formbuilderbackend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtRequestUser {
    @NotBlank
    String username;
    @NotBlank
    String password;
}
