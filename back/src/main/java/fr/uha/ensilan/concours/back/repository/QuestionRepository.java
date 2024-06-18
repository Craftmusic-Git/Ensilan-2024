package fr.uha.ensilan.concours.back.repository;

import fr.uha.ensilan.concours.back.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
}
