package project.formbuilderbackend.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class AuthUser extends User implements Serializable {
    public AuthUser() {
        super("anonymous", "", new ArrayList<GrantedAuthority>());
    }

    public AuthUser(String username, String password) {
        super(username, password, new ArrayList<GrantedAuthority>());
    }

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
