package fr.uha.ensilan.concours.back.controller.rest.admin;

import fr.uha.ensilan.concours.back.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/questions")
@RequiredArgsConstructor
@CrossOrigin
public class AdminQuestionController {
    private final QuestionService questionService;

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable UUID id) {
        questionService.deleteQuestionById(id);
    }
}
