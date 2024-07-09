package project.formbuilderbackend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.formbuilderbackend.entities.user.Role;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String username;
    @JsonIgnore
    private List<Role> roles;
    private List<String> roleNames;

    public List<String> getRoleNames() {
        return roles.stream().map(Role::getRole).toList();
    }
}