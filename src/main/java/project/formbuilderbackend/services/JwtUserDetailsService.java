package project.formbuilderbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.formbuilderbackend.entities.UserEntity;
import project.formbuilderbackend.repositories.UserRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRole()))
                        .collect(Collectors.toList())
        );
    }
}
