package fr.uha.ensilan.concours.back.dto.game;

import fr.uha.ensilan.concours.back.domain.game.GameState;
import fr.uha.ensilan.concours.back.dto.question.QuestionDto;
import fr.uha.ensilan.concours.back.dto.question.QuestionSetDto;
import fr.uha.ensilan.concours.back.dto.user.UserDto;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class GameDto {
    private UUID id;
    private String name;
    private GameState status;
    private QuestionSetDto questionSet;
    private QuestionDto currentQuestion;
    private Set<UserDto> players;
    private Set<UserDto> waitingPlayers;
}
