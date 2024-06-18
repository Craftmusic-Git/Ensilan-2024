package fr.uha.ensilan.concours.back.domain.game;

import fr.uha.ensilan.concours.back.domain.question.Question;
import fr.uha.ensilan.concours.back.domain.user.User;
import fr.uha.ensilan.concours.back.domain.question.QuestionSet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.Set;
import java.util.UUID;

@Entity(name = "games")
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "diffusion")
    private Boolean diffusion = false;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "state", columnDefinition = "game_state_enum", nullable = false)
    private GameState status;

    @ManyToOne
    @JoinColumn(name = "set_id")
    private QuestionSet questionSet;

    @ManyToOne
    @JoinColumn(name = "current_question_id")
    private Question currentQuestion;

    @OneToMany(mappedBy = "game", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<GameQuestionResult> results;

    @ManyToMany
    @JoinTable(
        name = "games_users",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> players;

    @ManyToMany
    @JoinTable(
        name = "waiting_games_users",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> waitingPlayers;
}
