package fr.uha.ensilan.concours.back.controller.rest.unsecure;

import fr.uha.ensilan.concours.back.domain.game.GameState;
import fr.uha.ensilan.concours.back.dto.game.AnswerResponseDto;
import fr.uha.ensilan.concours.back.dto.game.GameDto;
import fr.uha.ensilan.concours.back.dto.game.GamePlayerResultDto;
import fr.uha.ensilan.concours.back.dto.game.GameQuestionResultDto;
import fr.uha.ensilan.concours.back.dto.message.AnswerQuestionMessage;
import fr.uha.ensilan.concours.back.dto.message.PlayerJoinWaitingRoomMessage;
import fr.uha.ensilan.concours.back.dto.message.PlayerLeaveWaitingRoomMessage;
import fr.uha.ensilan.concours.back.dto.user.UserDto;
import fr.uha.ensilan.concours.back.mapper.GameMapper;
import fr.uha.ensilan.concours.back.mapper.GamePlayerResultMapper;
import fr.uha.ensilan.concours.back.mapper.GameQuestionResultMapper;
import fr.uha.ensilan.concours.back.mapper.UserMapper;
import fr.uha.ensilan.concours.back.service.GameConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public/games")
@RequiredArgsConstructor
@CrossOrigin
public class GameController {
    private final GameConfigService gameService;
    private final GameMapper gameMapper;
    private final UserMapper userMapper;
    private final GamePlayerResultMapper gamePlayerResultMapper;
    private final GameQuestionResultMapper gameQuestionResultMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public Set<GameDto> findAll() {
        return gameService.findAll().stream().filter(game -> !Objects.equals(game.getStatus(), GameState.IN_PREPARATION)).map(gameMapper::toDto).collect(Collectors.toSet());
    }

    @GetMapping("/{id}")
    public GameDto findById(@PathVariable UUID id) {
        return gameMapper.toDto(gameService.getGameById(id));
    }

    @PostMapping("/{id}/join-waiting")
    public UserDto joinGame(@PathVariable UUID id, @RequestBody UserDto user) {
        var player = userMapper.toDto(gameService.joinPlayerToWaiting(id, user.getId()));
        var msg = new PlayerJoinWaitingRoomMessage();
        msg.setPlayer(player);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
        return player;
    }

    @PostMapping("/{id}/leave-waiting")
    public UserDto leaveGame(@PathVariable UUID id, @RequestBody UserDto user) {
        var player = userMapper.toDto(gameService.removeUserToWaiting(id, user.getId()));
        var msg = new PlayerLeaveWaitingRoomMessage();
        msg.setPlayer(player);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
        return player;
    }

    @PostMapping("/{gameId}/answer")
    public GamePlayerResultDto answerQuestion(@PathVariable UUID gameId, @RequestBody AnswerResponseDto answer) {
        var result = gameService.answerToQuestion(gameId, answer.getUserId(), answer.getQuestionId(), answer.getAnswerId(), answer.getDuration());
        var resultDto = gamePlayerResultMapper.toDto(result);
        var msg = new AnswerQuestionMessage();
        msg.setResponse(resultDto);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, msg);
        return resultDto;
    }

    @GetMapping("/{gameId}/results")
    public Set<GameQuestionResultDto> getResults(@PathVariable UUID gameId) {
        return gameService.getGameById(gameId).getResults().stream().map(gameQuestionResultMapper::toDto).collect(Collectors.toSet());
    }
}
