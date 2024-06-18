import {inject, Injectable, signal} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {GamesPlayerService} from "@core/service/games-player.service";
import {PlayerScore} from "@core/store/player-score";
import {GameWebsocket} from "@core/websocket/game.websocket";
import {GameQuestionResultDto} from "@domain/games/game-question-result.dto";
import {GameDto} from "@domain/games/game.dto";
import {AnswerQuestionMessage} from "@domain/message/answer-question.message";
import {MessageTypeEnum} from "@domain/message/message-type.enum";
import {QuestionSetDto} from "@domain/questions/question-set.dto";

@Injectable({
  providedIn: 'root',
})
export class GameResultStore {
  service = inject(GamesPlayerService);
  websocket = inject(GameWebsocket);
  route = inject(ActivatedRoute);


  results = signal<GameQuestionResultDto[]>([]);
  isReady = signal(false);

  constructor(){
    this.websocket.message$.subscribe(message => {
      if ( message.type === MessageTypeEnum.ANSWER_QUESTION ) {
        const answerQuestionMessage = message as AnswerQuestionMessage;
        // Update the results array with the new response
        this.results.update(results =>
          results.map(result =>
            result.gamePlayerResults.some(answer => answer.id === answerQuestionMessage.response.id)
              ? result
              : {...result, answers: [...result.gamePlayerResults, answerQuestionMessage.response]},
          ),
        );
      }
    });
  }


  calculatePlayerScores( game: GameDto, questionSet: QuestionSetDto ): PlayerScore[]{
    const playerScores: PlayerScore[] = [];
    // Iterate over the results array
    this.results().forEach(questionResult => {
      // Iterate over the answers array for each result
      questionResult.gamePlayerResults.forEach(playerAnswer => {
        // Find the player score object for the user ID
        let playerScore = playerScores.find(score => score.userId === playerAnswer.userId);

        // If the player score object doesn't exist, create a new one
        if ( !playerScore ) {
          const user = game.players.find(player => player.id === playerAnswer.userId);
          playerScore = {
            userId: playerAnswer.userId,
            username: user ? user.username : 'Unknown',
            score: 0,
            averageResponseTime: 0,
          };
          playerScores.push(playerScore);
        }

        // Update the player score based on the answer
        questionSet.questions.forEach(question => question.answers.forEach(answer => {
          if ( answer.isCorrect && playerAnswer.answerId === answer.id ) {
            playerScore.score++;
          }
        }));

        playerScore.averageResponseTime += playerAnswer.duration;
      });
    });

    playerScores.forEach(playerScore => {
      playerScore.averageResponseTime /= this.results().length;
    });

    return playerScores;
  }

  loadResultsForGame( gameId: string ){

    this.service.getResultsForGame(gameId).subscribe(results => {
      this.results.set(results);
      this.isReady.set(true);
    });

  }
}
