package fr.uha.ensilan.concours.back.dto.question;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class QuestionDto {
    private UUID id;
    private String text;
    private Integer order;
    private Set<AnswerDto> answers;
}
