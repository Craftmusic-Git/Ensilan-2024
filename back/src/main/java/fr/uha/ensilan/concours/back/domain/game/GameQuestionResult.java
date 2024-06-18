package fr.uha.ensilan.concours.back.domain.game;

import fr.uha.ensilan.concours.back.domain.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity(name = "game_question_results")
@Getter
@Setter
public class GameQuestionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "gameQuestionResult", cascade = CascadeType.ALL)
    private Set<GamePlayerResult> gamePlayerResults;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
}
