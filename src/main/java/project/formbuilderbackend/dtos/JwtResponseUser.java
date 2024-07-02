package project.formbuilderbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseUser {
    private String username;
    private String refreshToken;
    private String accessToken;
    private List<String> roles;
    private String tokenType;
}
