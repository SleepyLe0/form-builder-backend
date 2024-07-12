package project.formbuilderbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.formbuilderbackend.dtos.form.FormDto;
import project.formbuilderbackend.dtos.form.FormListDto;
import project.formbuilderbackend.dtos.form.FormRequestDto;
import project.formbuilderbackend.services.FormService;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;

    @GetMapping("")
    public ResponseEntity<List<FormListDto>> getForms(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(formService.findAllByCurrentUser(userDetails.getUsername()));
    }

    @GetMapping("/{formId}")
    public ResponseEntity<FormDto> getFormById(@PathVariable Long formId, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(formService.findById(formId, userDetails.getUsername()));
    }

    @PostMapping("")
    public ResponseEntity<FormDto> addForm(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(formService.createForm(userDetails.getUsername()));
    }

    @PutMapping("/{formId}")
    public ResponseEntity<FormDto> editForm(@PathVariable Long formId, @RequestBody FormRequestDto form, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(formService.updateForm(formId, form, userDetails.getUsername()));
    }

    @DeleteMapping("/{formId}")
    public ResponseEntity<String> deleteForm(@PathVariable Long formId, @AuthenticationPrincipal UserDetails userDetails) {
        formService.deleteForm(formId, userDetails.getUsername());
        return ResponseEntity.ok("Form deleted successfully");
    }
}
