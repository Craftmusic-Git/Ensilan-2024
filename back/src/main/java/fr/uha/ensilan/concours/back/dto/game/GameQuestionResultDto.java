package fr.uha.ensilan.concours.back.dto.game;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class GameQuestionResultDto {
    private UUID id;
    private Set<GamePlayerResultDto> gamePlayerResults;
    private UUID questionId;
    private UUID gameId;
}
