package fr.uha.ensilan.concours.back.service;

import fr.uha.ensilan.concours.back.domain.game.Game;
import fr.uha.ensilan.concours.back.domain.game.GamePlayerResult;
import fr.uha.ensilan.concours.back.domain.game.GameQuestionResult;
import fr.uha.ensilan.concours.back.domain.game.GameState;
import fr.uha.ensilan.concours.back.domain.question.Question;
import fr.uha.ensilan.concours.back.domain.user.User;
import fr.uha.ensilan.concours.back.dto.PageableRequest;
import fr.uha.ensilan.concours.back.repository.GameRepository;
import fr.uha.ensilan.concours.back.repository.QuestionSetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameConfigService {
    private final GameRepository gameRepository;
    private final QuestionSetRepository questionSetRepository;

    private final UserService userService;

    @Transactional
    public Game createGame(Game game) {
        game.setStatus(GameState.IN_PREPARATION);
        var savedGame = gameRepository.save(game);
        log.info("Game created: {}, id: {}", savedGame.getName(), savedGame.getId());
        return savedGame;
    }

    @Transactional(readOnly = true)
    public Game getGameById(UUID id) {
        return gameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Game not found"));
    }

    @Transactional(readOnly = true)
    public Page<Game> getAllGames(PageableRequest request) {
        return gameRepository.findAll(request.getPageable());
    }

    @Transactional
    public Game update(UUID id, Game game) {
        var savedGame = gameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        savedGame.setName(game.getName());

        if (game.getQuestionSet() != null && game.getQuestionSet().getId() != null) {
            questionSetRepository.findById(game.getQuestionSet().getId()).orElseThrow(() -> new IllegalArgumentException("Question set not found"));
            savedGame.setQuestionSet(game.getQuestionSet());
        }

        if (savedGame.getName() != null && savedGame.getQuestionSet() != null && savedGame.getStatus() == GameState.IN_PREPARATION) {
            savedGame.setStatus(GameState.READY);
        }

        var updatedGame = gameRepository.save(savedGame);
        log.info("Game updated: {}, id: {}", updatedGame.getName(), updatedGame.getId());
        return updatedGame;
    }

    @Transactional
    public User joinPlayerToWaiting(UUID gameId, UUID userId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        var user = userService.getUserByID(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        game.getWaitingPlayers().add(user);
        gameRepository.save(game);
        log.info("User joined to waiting: {}, game: {}", user.getUsername(), game.getName());
        return user;
    }

    @Transactional
    public User removeUserToWaiting(UUID gameId, UUID userId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        var user = userService.getUserByID(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        game.getWaitingPlayers().stream().filter(u -> u.getId().equals(userId)).findFirst().ifPresent(game.getWaitingPlayers()::remove);
        gameRepository.save(game);
        log.info("User removed from waiting: {}, game: {}", user.getUsername(), game.getName());
        return user;
    }

    @Transactional
    public User joinPlayerToPlaying(UUID gameId, UUID userId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        var user = userService.getUserByID(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        game.getPlayers().add(user);
        game.getWaitingPlayers().stream().filter(u -> u.getId().equals(userId)).findFirst().ifPresent(game.getWaitingPlayers()::remove);
        gameRepository.save(game);
        log.info("User joined to playing: {}, game: {}", user.getUsername(), game.getName());
        return user;
    }

    @Transactional
    public User removeUserToPlaying(UUID gameId, UUID userId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        var user = userService.getUserByID(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        game.getPlayers().stream().filter(u -> u.getId().equals(userId)).findFirst().ifPresent(game.getPlayers()::remove);
        game.getWaitingPlayers().add(user);
        gameRepository.save(game);
        log.info("User removed from playing: {}, game: {}", user.getUsername(), game.getName());
        return user;
    }

    @Transactional
    public void deleteGame(UUID id) {
        var game = gameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        if (game.getStatus() != GameState.IN_PREPARATION && game.getStatus() != GameState.READY) {
            throw new IllegalArgumentException("Game is not in preparation state or ready");
        }
        log.info("Game deleted: {}, id: {}", game.getName(), game.getId());
        gameRepository.deleteById(id);
    }

    @Transactional
    public Question startGame(UUID id) {
        var game = gameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        if (game.getStatus() != GameState.READY) {
            throw new IllegalArgumentException("Game is not ready");
        }

        game.setStatus(GameState.IN_PROGRESS);
        var firstQuestion = game.getQuestionSet().getQuestions().stream().filter(q -> q.getOrder().equals(1)).findFirst().orElseThrow(() -> new IllegalArgumentException("No question found"));
        game.setCurrentQuestion(firstQuestion);

        var questionResults = new GameQuestionResult();
        questionResults.setGame(game);
        questionResults.setQuestion(firstQuestion);
        game.getResults().add(questionResults);

        gameRepository.save(game);
        log.info("Game started: {}, id: {}", game.getName(), game.getId());
        return firstQuestion;
    }

    @Transactional
    public Question nextQuestion(UUID id) {
        var game = gameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        if (game.getStatus() != GameState.IN_PROGRESS) {
            throw new IllegalArgumentException("Game is not in progress");
        }

        var currentQuestion = game.getCurrentQuestion();
        var nextQuestion = game.getQuestionSet().getQuestions().stream().filter(q -> q.getOrder().equals(currentQuestion.getOrder() + 1)).findFirst().orElse(null);

        if (nextQuestion == null) {
            game.setStatus(GameState.FINISHED);
            game.setCurrentQuestion(null);
            gameRepository.save(game);
            log.info("Game finished: {}, id: {}", game.getName(), game.getId());
            return null;
        }

        game.setCurrentQuestion(nextQuestion);
        var questionResults = new GameQuestionResult();
        questionResults.setQuestion(nextQuestion);
        questionResults.setGame(game);
        questionResults.setGamePlayerResults(new HashSet<>());
        game.getResults().add(questionResults);

        gameRepository.save(game);
        log.info("Next question: {}, game: {}", nextQuestion.getText(), game.getName());
        return nextQuestion;
    }

    @Transactional
    public GamePlayerResult answerToQuestion(UUID gameId, UUID userId, UUID questionId, UUID answerId, Long duration) {
        var game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        if (game.getStatus() != GameState.IN_PROGRESS) {
            throw new IllegalArgumentException("Game is not in progress");
        }

        var question = game.getQuestionSet().getQuestions().stream().filter(q -> q.getId().equals(questionId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Question not found"));
        var answer = question.getAnswers().stream().filter(a -> a.getId().equals(answerId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Answer not found"));
        var user = userService.getUserByID(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        var questionResult = game.getResults().stream().filter(qr -> qr.getQuestion().getId().equals(questionId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Question result not found"));
        var playerResult = questionResult.getGamePlayerResults().stream().filter(pr -> pr.getUser().getId().equals(userId)).findFirst().orElse(null);

        playerResult = new GamePlayerResult();
        playerResult.setUser(user);
        playerResult.setAnswer(answer);
        playerResult.setDuration(duration);
        playerResult.setGameQuestionResult(questionResult);
        questionResult.getGamePlayerResults().add(playerResult);

        gameRepository.save(game);
        log.info("User answered to question: {}, game: {}", user.getUsername(), game.getName());
        return playerResult;
    }

    @Transactional
    public Game endGame(UUID id) {
        var game = gameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        game.setStatus(GameState.FINISHED);
        game.setCurrentQuestion(null);
        log.info("Game ended: {}, id: {}", game.getName(), game.getId());
        return gameRepository.save(game);
    }

    @Transactional(readOnly = true)
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    @Transactional
    public Game diffuseGame(UUID id) {
        var games = gameRepository.findByDiffusion(true);
        games.forEach(game -> {
            game.setDiffusion(false);
        });
        gameRepository.saveAll(games);
        var savedGame = gameRepository.getReferenceById(id);
        savedGame.setDiffusion(true);
        log.info("Game diffused: {}, id: {}", savedGame.getName(), savedGame.getId());
        return gameRepository.save(savedGame);
    }

    @Transactional(readOnly = true)
    public Game getDiffusedGame() {
        return gameRepository.findByDiffusion(true).stream().findFirst().orElse(null);
    }
}
