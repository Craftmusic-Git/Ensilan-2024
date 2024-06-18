package fr.uha.ensilan.concours.back.domain.question;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity(name = "questions")
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "text")
    private String text;

    @Column(name = "question_order")
    private Integer order;

    @OneToMany(mappedBy = "question", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Answer> answers;

    @ManyToOne
    @JoinColumn(name = "set_id", nullable = false)
    private QuestionSet set;
}
