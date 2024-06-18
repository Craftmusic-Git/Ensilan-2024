package fr.uha.ensilan.concours.back.dto.question;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class QuestionSetDto {
    private UUID id;
    private String name;
    private Set<QuestionDto> questions;
}
