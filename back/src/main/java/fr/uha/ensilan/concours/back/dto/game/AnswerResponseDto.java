package fr.uha.ensilan.concours.back.dto.game;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AnswerResponseDto {
    private UUID questionId;
    private UUID userId;
    private UUID answerId;
    private Long duration;
}
