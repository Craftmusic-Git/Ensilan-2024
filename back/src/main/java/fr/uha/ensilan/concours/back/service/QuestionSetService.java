package fr.uha.ensilan.concours.back.service;

import fr.uha.ensilan.concours.back.domain.question.QuestionSet;
import fr.uha.ensilan.concours.back.dto.PageableRequest;
import fr.uha.ensilan.concours.back.repository.QuestionSetRepository;
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
public class QuestionSetService {
    private static final Logger log = LoggerFactory.getLogger(QuestionSetService.class);
    private final QuestionSetRepository questionSetRepository;

    @Transactional(readOnly = true)
    public Optional<QuestionSet> getQuestionSetById(UUID id) {
        return questionSetRepository.findById(id);
    }

    @Transactional
    public QuestionSet saveQuestionSet(QuestionSet questionSet) {
        log.info("Saving questionSet: {}", questionSet.getName());
        return questionSetRepository.save(questionSet);
    }

    @Transactional
    public QuestionSet updateQuestionSet(QuestionSet questionSet, UUID id) {
        if (!questionSetRepository.existsById(id)) {
            throw new IllegalArgumentException("QuestionSet not found");
        }
        questionSet.setId(id);
        questionSet.getQuestions().forEach(question -> question.setSet(questionSet));
        questionSet.getQuestions().forEach(question -> question.getAnswers().forEach(answer -> answer.setQuestion(question)));

        log.info("Updating questionSet: {}", questionSet.getName());
        return questionSetRepository.save(questionSet);

    }

    @Transactional(readOnly = true)
    public Page<QuestionSet> getAllQuestionSets(PageableRequest request) {
        return questionSetRepository.findAll(request.getPageable());
    }

    @Transactional
    public void deleteQuestionSetById(UUID id) {
        log.info("Deleting questionSet with id: {}", id);
        questionSetRepository.deleteById(id);
    }
}
