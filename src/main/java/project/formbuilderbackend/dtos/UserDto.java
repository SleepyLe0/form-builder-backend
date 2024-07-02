package project.formbuilderbackend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.formbuilderbackend.entities.Role;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    Long id;
    String username;
    @JsonIgnore
    List<Role> roles;
    List<String> roleNames;

    public List<String> getRoleNames() {
        return roles.stream().map(Role::getRole).toList();
    }
}