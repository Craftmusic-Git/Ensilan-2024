package fr.uha.ensilan.concours.back.controller.rest.admin;

import fr.uha.ensilan.concours.back.dto.PageableRequest;
import fr.uha.ensilan.concours.back.dto.game.GameDto;
import fr.uha.ensilan.concours.back.dto.message.*;
import fr.uha.ensilan.concours.back.dto.question.AnswerDto;
import fr.uha.ensilan.concours.back.dto.question.QuestionDto;
import fr.uha.ensilan.concours.back.dto.user.UserDto;
import fr.uha.ensilan.concours.back.mapper.GameMapper;
import fr.uha.ensilan.concours.back.mapper.QuestionMapper;
import fr.uha.ensilan.concours.back.mapper.UserMapper;
import fr.uha.ensilan.concours.back.service.GameConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/games")
@RequiredArgsConstructor
@CrossOrigin
public class AdminGameController {
    private final GameConfigService gameService;
    private final GameMapper gameMapper;
    private final UserMapper userMapper;
    private final QuestionMapper questionMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public Page<GameDto> findAllGames(@ModelAttribute PageableRequest request) {
        return gameService.getAllGames(request).map(gameMapper::toDto);
    }

    @GetMapping("/{id}")
    public GameDto findGameById(@PathVariable UUID id) {
        return gameMapper.toDto(gameService.getGameById(id));
    }

    @PutMapping("/{id}")
    public GameDto updateGame(@PathVariable UUID id, @RequestBody GameDto gameDto) {
        var savedGame = gameMapper.toDto(gameService.update(id, gameMapper.toModel(gameDto)));
        var msg = new GameUpdatedMessage();
        msg.setGame(savedGame);
        simpMessagingTemplate.convertAndSend("/topic/games", msg);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
        return savedGame;
    }

    @PostMapping
    public GameDto createGame(@RequestBody GameDto gameDto) {
        var savedGame = gameService.createGame(gameMapper.toModel(gameDto));
        var msg = new GameCreatedMessage();
        var dto = gameMapper.toDto(savedGame);
        msg.setGame(dto);
        simpMessagingTemplate.convertAndSend("/topic/games", msg);
        return dto;
    }

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable UUID id) {
        var msg = new GameDeletedMessage();
        msg.setId(id);
        simpMessagingTemplate.convertAndSend("/topic/games", msg);
        gameService.deleteGame(id);
    }

    @PostMapping("/{id}/leave-player")
    public UserDto leavePlayer(@PathVariable UUID id, @RequestBody UserDto userDto) {
        var player = userMapper.toDto(gameService.removeUserToWaiting(id, userDto.getId()));
        var msg = new PlayerLeaveWaitingRoomMessage();
        msg.setPlayer(userDto);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
        return player;
    }

    @PostMapping("/{id}/join-player")
    public UserDto joinPlayer(@PathVariable UUID id, @RequestBody UserDto userDto) {
        var player = userMapper.toDto(gameService.joinPlayerToPlaying(id, userDto.getId()));
        var msg = new PlayerJoinMessage();
        msg.setPlayer(userDto);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
        return player;
    }

    @PostMapping("/{id}/remove-player")
    public UserDto removePlayer(@PathVariable UUID id, @RequestBody UserDto userDto) {
        var player = userMapper.toDto(gameService.removeUserToPlaying(id, userDto.getId()));
        var msg = new PlayerLeaveMessage();
        msg.setPlayer(userDto);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
        return player;
    }

    @PostMapping("/{id}/start")
    public QuestionDto startGame(@PathVariable UUID id) {
        var question = questionMapper.toDto(gameService.startGame(id));
        var msg = new StartGameMessage();
        msg.setQuestion(question);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
        return question;
    }

    @PostMapping("/{id}/next")
    public QuestionDto nextQuestion(@PathVariable UUID id) {
        var question = questionMapper.toDto(gameService.nextQuestion(id));
        if (question == null) {
            var msg = new StopGameMessage();
            simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
            return null;
        }
        var msg = new NextQuestionMessage();
        msg.setQuestion(question);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
        return question;
    }

    @PostMapping("/{id}/unlock-answers")
    public void unlockAnswers(@PathVariable UUID id) {
        var msg = new UnlockAnswersMessage();
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
    }

    @PostMapping("/{id}/show-answer")
    public AnswerDto showAnswer(@PathVariable UUID id, @RequestBody AnswerDto answer) {
        var msg = new ShowAnswerMessage();
        msg.setAnswer(answer);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
        return answer;
    }

    @PostMapping("/{id}/end")
    public GameDto endGame(@PathVariable UUID id) {
        var game = gameMapper.toDto(gameService.endGame(id));
        var msg = new StopGameMessage();
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, msg);
        return game;
    }

    @PostMapping("/{id}/diffuse")
    public void diffuseGame(@PathVariable UUID id) {
        gameService.diffuseGame(id);
    }
}
