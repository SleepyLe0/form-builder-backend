package project.formbuilderbackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.formbuilderbackend.dtos.JwtRequestUser;
import project.formbuilderbackend.dtos.JwtResponseUser;
import project.formbuilderbackend.dtos.RegisterUser;
import project.formbuilderbackend.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseUser> login(@RequestBody @Valid JwtRequestUser jwtRequestUser) {
        return ResponseEntity.ok(authenticationService.login(jwtRequestUser));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponseUser> register(@RequestBody RegisterUser registerUser) {
        return ResponseEntity.ok(authenticationService.register(registerUser));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponseUser> refreshToken(@RequestHeader("Authorization") String refreshtoken) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshtoken));
    }
}
