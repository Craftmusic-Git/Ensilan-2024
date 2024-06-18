package fr.uha.ensilan.concours.back.controller.rest.unsecure;

import fr.uha.ensilan.concours.back.domain.game.Game;
import fr.uha.ensilan.concours.back.domain.game.GameState;
import fr.uha.ensilan.concours.back.domain.question.Question;
import fr.uha.ensilan.concours.back.domain.user.User;
import fr.uha.ensilan.concours.back.dto.unity.*;
import fr.uha.ensilan.concours.back.service.GameConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public/api")
@RequiredArgsConstructor
@CrossOrigin
public class ApiController {
    private final GameConfigService gameService;

    @GetMapping("/actual")
    public UnityActualQuestion getActualQuestion() {
        var diffusedGame = gameService.getDiffusedGame();
        if (diffusedGame == null) {
            return null;
        }

        var response = new UnityActualQuestion();
        if (Objects.nonNull(diffusedGame.getCurrentQuestion())) {
            response.setQuestion(buildUnityQuestion(diffusedGame.getCurrentQuestion()));
        }
        response.setState(buildUnityGameState(diffusedGame.getStatus()));
        response.setUsers(buildUnityUsers(diffusedGame.getPlayers(), diffusedGame));
        return response;
    }

    private UnityQuestion buildUnityQuestion(Question question) {
        var unityQuestion = new UnityQuestion();
        // unityQuestion.setId(question.getId());
        unityQuestion.setText(question.getText());
        unityQuestion.setAnswers(question.getAnswers().stream().map(answer -> {
            var unityAnswer = new UnityAnswers();
            //unityAnswer.setId(answer.getId());
            unityAnswer.setText(answer.getText());
            unityAnswer.setIsGood(answer.getIsCorrect());
            return unityAnswer;
        }).collect(Collectors.toList()));
        return unityQuestion;
    }

    private UnityGameState buildUnityGameState(GameState state) {
        return switch (state) {
            case READY:
                yield UnityGameState.READY;
            case IN_PROGRESS:
                yield UnityGameState.IN_PROGRESS;
            case FINISHED:
                yield UnityGameState.DONE;
            default:
                yield UnityGameState.IN_PREPARATION;
        };
    }

    private List<UnityUsers> buildUnityUsers(Set<User> users, Game game) {
        return users.stream().map(user -> {
            var unityUser = new UnityUsers();
            //unityUser.setId(user.getId());
            unityUser.setFirstName(user.getUsername());
            unityUser.setLastName(user.getLastname());
            unityUser.setUserClass(user.getUserClass());
            unityUser.setScore(getResultOfUser(game, user));
            return unityUser;
        }).collect(Collectors.toList());
    }

    private Long getResultOfUser(Game game, User user) {
        var playerResults = game.getResults().stream()
                .flatMap(result -> result.getGamePlayerResults().stream())
                .filter(playerResult -> Objects.equals(playerResult.getUser().getId(), user.getId()))
                .toList();

        var score = 0L;
        for (var playerResult : playerResults) {
            if (playerResult.getAnswer().getIsCorrect()) {
                score += 1;
            }
        }

        return score;
    }
}
