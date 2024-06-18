package fr.uha.ensilan.concours.back.service;

import fr.uha.ensilan.concours.back.domain.question.Question;
import fr.uha.ensilan.concours.back.dto.PageableRequest;
import fr.uha.ensilan.concours.back.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public Optional<Question> getQuestionByID(UUID id) {
        return questionRepository.findById(id);
    }

    @Transactional
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Transactional
    public Question updateQuestion(Question question, UUID id) {
        if (questionRepository.existsById(id)) {
            question.setId(id);
            return questionRepository.save(question);
        }
        throw new IllegalArgumentException("Question not found");
    }

    @Transactional(readOnly = true)
    public Page<Question> getAllQuestions(PageableRequest request) {
        return questionRepository.findAll(request.getPageable());
    }

    @Transactional
    public void deleteQuestionById(UUID id) {
        log.info("Deleting question with id: {}", id);
        questionRepository.deleteById(id);
    }
}
