package project.formbuilderbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.formbuilderbackend.dtos.user.JwtRequestUser;
import project.formbuilderbackend.dtos.user.JwtResponseUser;
import project.formbuilderbackend.dtos.user.RegisterUser;
import project.formbuilderbackend.entities.user.Role;
import project.formbuilderbackend.entities.user.UserEntity;
import project.formbuilderbackend.repositories.RoleRepository;
import project.formbuilderbackend.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public JwtResponseUser login(JwtRequestUser jwtRequestUser) {
        UserEntity user = userRepository.findByUsernameOrEmail(jwtRequestUser.getUsername(), jwtRequestUser.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), jwtRequestUser.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateNewTokens(userDetails);
    }

    public JwtResponseUser register(RegisterUser registerUser) {
        if (userRepository.existsByUsername(registerUser.getUsername()) || userRepository.existsByEmail(registerUser.getEmail())) {
            throw new RuntimeException("User already exists");
        }
        Role role = roleRepository.findByRole("USER").orElseThrow(() -> new RuntimeException("Role not found"));
        UserEntity user = UserEntity.builder()
                .email(registerUser.getEmail())
                .username(registerUser.getUsername())
                .password(passwordEncoder.encode(registerUser.getPassword()))
                .roles(List.of(role))
                .build();
        userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return generateNewTokens(userDetails);
    }

    public JwtResponseUser refreshToken(String refreshToken) {
        String jwtToken;
        if (!refreshToken.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid refresh token");
        } else {
            jwtToken = refreshToken.substring(7);
        }
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        if (username == null) {
            throw new RuntimeException("Invalid refresh token");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtTokenUtil.validateRefreshToken(jwtToken, userDetails)) {
            throw new RuntimeException("Invalid refresh token");
        }
        return generateNewAccessToken(userDetails, jwtToken);
    }

    private JwtResponseUser generateNewAccessToken(UserDetails userDetails, String refreshToken) {
        String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
        return JwtResponseUser.builder()
                .username(userDetails.getUsername())
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .roles(userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .tokenType("Bearer ")
                .build();
    }

    private JwtResponseUser generateNewTokens(UserDetails userDetails) {
        String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
        return JwtResponseUser.builder()
                .username(userDetails.getUsername())
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .roles(userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .tokenType("Bearer ")
                .build();
    }
}
