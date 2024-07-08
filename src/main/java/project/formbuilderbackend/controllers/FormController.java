package project.formbuilderbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.formbuilderbackend.dtos.FormDto;
import project.formbuilderbackend.services.FormService;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;

    @GetMapping("")
    public ResponseEntity<List<FormDto>> getForms(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(formService.findAllByCurrentUser(userDetails.getUsername()));
    }
}
