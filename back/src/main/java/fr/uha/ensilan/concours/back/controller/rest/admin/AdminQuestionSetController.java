package fr.uha.ensilan.concours.back.controller.rest.admin;

import fr.uha.ensilan.concours.back.dto.PageableRequest;
import fr.uha.ensilan.concours.back.dto.question.QuestionSetDto;
import fr.uha.ensilan.concours.back.mapper.QuestionSetMapper;
import fr.uha.ensilan.concours.back.service.QuestionSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/questions-set")
@RequiredArgsConstructor
@CrossOrigin
public class AdminQuestionSetController {
    private final QuestionSetService questionSetService;

    private final QuestionSetMapper questionSetMapper;

    @GetMapping("/{id}")
    public QuestionSetDto findQuestionSetById(@PathVariable UUID id) {
        return questionSetMapper.toDto(questionSetService.getQuestionSetById(id).orElseThrow());
    }

    @GetMapping
    public Page<QuestionSetDto> findAllQuestionsSet(@ModelAttribute PageableRequest request) {
        return questionSetService.getAllQuestionSets(request).map(questionSetMapper::toDto);
    }

    @PutMapping("/{id}")
    public QuestionSetDto updateQuestionSet(@PathVariable UUID id, @RequestBody QuestionSetDto questionSetDto) {
        return questionSetMapper.toDto(questionSetService.updateQuestionSet(questionSetMapper.toModel(questionSetDto), id));
    }

    @PostMapping
    public QuestionSetDto createQuestionSet(@RequestBody QuestionSetDto questionSetDto) {
        return questionSetMapper.toDto(questionSetService.saveQuestionSet(questionSetMapper.toModel(questionSetDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteQuestionSetById(@PathVariable UUID id) {
        questionSetService.deleteQuestionSetById(id);
    }
}
