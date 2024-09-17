package project.formbuilderbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.formbuilderbackend.dtos.question.QuestionDto;
import project.formbuilderbackend.dtos.question.QuestionRequestDto;
import project.formbuilderbackend.services.QuestionService;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("")
    public ResponseEntity<QuestionDto> addQuestion(
            @RequestParam Long formId,
            @RequestParam String type,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(questionService.createQuestion(formId, type, userDetails.getUsername()));
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionDto> editQuestion(
            @PathVariable Long questionId,
            @RequestBody QuestionRequestDto question,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(questionService.updateQuestion(questionId, question, userDetails.getUsername()));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteForm(
            @PathVariable Long questionId,
            @AuthenticationPrincipal UserDetails userDetails) {
        questionService.deleteQuestion(questionId, userDetails.getUsername());
        return ResponseEntity.ok("Question deleted successfully");
    }
}
