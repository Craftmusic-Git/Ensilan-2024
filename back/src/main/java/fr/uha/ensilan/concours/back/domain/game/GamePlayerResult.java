package fr.uha.ensilan.concours.back.domain.game;


import fr.uha.ensilan.concours.back.domain.user.User;
import fr.uha.ensilan.concours.back.domain.question.Answer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "game_player_results")
@Getter
@Setter
public class GamePlayerResult {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "game_question_result_id", nullable = false)
    private GameQuestionResult gameQuestionResult;

    @Column(name = "duration")
    private Long duration; // Durration in milliseconds
}
