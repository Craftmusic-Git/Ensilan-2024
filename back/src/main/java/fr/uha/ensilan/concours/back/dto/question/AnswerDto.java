package fr.uha.ensilan.concours.back.dto.question;

import lombok.Data;

import java.util.UUID;

@Data
public class AnswerDto {
    private UUID id;
    private String text;
    private Boolean isCorrect;
}
