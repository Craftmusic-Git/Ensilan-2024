package fr.uha.ensilan.concours.back.repository;

import fr.uha.ensilan.concours.back.domain.question.QuestionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionSetRepository extends JpaRepository<QuestionSet, UUID> {
}
