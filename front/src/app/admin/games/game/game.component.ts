import {ChangeDetectionStrategy, Component, inject, signal} from '@angular/core';
import {FormBuilder, ReactiveFormsModule} from '@angular/forms';
import {ActivatedRoute} from "@angular/router";
import {QuestionSetAdminService} from '@app/core/service/question-set-admin.service';
import {GameResultStore} from '@app/core/store/games-result.store';
import {GamesService} from "@core/service/games.service";
import {GameWebsocket} from "@core/websocket/game.websocket";
import {GameStateEnum} from "@domain/games/game-state.enum";
import {GameDto} from "@domain/games/game.dto";
import {MessageTypeEnum} from "@domain/message/message-type.enum";
import {PlayerJoinWaitingRoomMessage} from "@domain/message/player-join-waiting-room.message";
import {PlayerJoinMessage} from "@domain/message/player-join.message";
import {PlayerLeaveWaitingRoomMessage} from "@domain/message/player-leave-waiting-room.message";
import {StartGameMessage} from "@domain/message/start-game.message";
import {QuestionSetDto} from "@domain/questions/question-set.dto";
import {QuestionDto} from "@domain/questions/question.dto";
import {UserDto} from "@domain/user.dto";

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [
    ReactiveFormsModule,
  ],
  templateUrl: './game.component.html',
  styleUrl: './game.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GameComponent {
  questionSetService = inject(QuestionSetAdminService);
  gameService = inject(GamesService);
  gameWebsocket = inject(GameWebsocket)
  gameResultStore = inject(GameResultStore);
  route = inject(ActivatedRoute);
  private fb = inject(FormBuilder);

  form = this.fb.group({
    id: '',
    name: '',
    questionSet: '',
  });

  game = signal<GameDto>(null)
  questionsSet = signal<QuestionSetDto[]>([]);
  actualQuestion = signal<QuestionDto>(null);
  nextQuestion = signal<QuestionDto>(null);

  constructor(){
    this.route.params.subscribe(params => {
      this.questionSetService.findAll().subscribe(questionSets => {
        this.questionsSet.set(questionSets.content)
        this.gameService.get(params['id']).subscribe(game => this.refresh(game));
        this.gameWebsocket.connect(params['id']);
        this.gameResultStore.loadResultsForGame(params['id']);
      });
    });

    this.gameWebsocket.message$.subscribe(message => {
      console.log('Message', message);
      switch ( message.type ) {
        case MessageTypeEnum.PLAYER_JOIN_WAITING_ROOM:
          this.game.update(game => {
            game.waitingPlayers.push((message as PlayerJoinWaitingRoomMessage).player);
            return game;
          })
          break;
        case MessageTypeEnum.PLAYER_LEAVE_WAITING_ROOM:
          this.game.update(game => {
            game.waitingPlayers = game.waitingPlayers.filter(player => player.id !== (message as PlayerLeaveWaitingRoomMessage).player.id);
            return game;
          })
          break;
        case MessageTypeEnum.PLAYER_JOIN:
          this.game.update(game => {
            game.players.push((message as PlayerJoinMessage).player);
            game.waitingPlayers = game.waitingPlayers.filter(player => player.id !== (message as PlayerLeaveWaitingRoomMessage).player.id);
            return game;
          })
          break;
        case MessageTypeEnum.PLAYER_LEAVE:
          this.game.update(game => {
            game.players = game.players.filter(player => player.id !== (message as PlayerJoinMessage).player.id);
            game.waitingPlayers.push((message as PlayerJoinWaitingRoomMessage).player);
            return game;
          })
          break;
        case MessageTypeEnum.START_GAME:
          this.game.update(game => {
            game.status = GameStateEnum.IN_PROGRESS;
            const currentQuestion = (message as StartGameMessage).question;
            this.actualQuestion.set(currentQuestion);
            const nextQuestion = game.questionSet.questions.find(question => question.order === currentQuestion.order + 1);
            this.nextQuestion.set(nextQuestion);
            return game;
          })
          break;
        case MessageTypeEnum.STOP_GAME:
          this.game.update(game => {
            game.status = GameStateEnum.FINISHED;
            this.actualQuestion.set(null);
            this.nextQuestion.set(null);
            return game;
          })
          break;
        case MessageTypeEnum.NEXT_QUESTION:
          this.game.update(game => {
            const currentQuestion = (message as StartGameMessage).question;
            this.actualQuestion.set(currentQuestion);
            const nextQuestion = game.questionSet.questions.find(question => question.order === currentQuestion.order + 1);
            this.nextQuestion.set(nextQuestion);
            return game;
          })
          break;
        default:
          break;
      }
    });
  }

  removePlayerFromGame( player: UserDto ){
    this.gameService.removePlayerToGame(this.game().id, player).subscribe();
  }

  addPlayerFromGame( player: UserDto ){
    this.gameService.addPlayerToGame(this.game().id, player).subscribe();
  }

  removePlayerFromWaitingList( player: UserDto ){
    this.gameService.removePlayerFromWaitingList(this.game().id, player).subscribe();
  }

  startGame(){
    this.gameService.startGame(this.game().id).subscribe();
  }

  sendNextQuestion(){
    this.gameService.nextQuestion(this.game().id).subscribe();
  }

  endGame(){
    this.gameService.endGame(this.game().id).subscribe();
  }

  unlockAnswers(){
    this.gameService.unlockAnswers(this.game().id).subscribe();
  }

  showAnswer(){
    this.gameService.showAnswer(this.game().id, this.actualQuestion().answers.filter(answer => answer.isCorrect)[0]).subscribe();
  }

  diffuse(){
    this.gameService.diffuse(this.game().id).subscribe()
  }

  update(){
    const game = Object.assign(new GameDto(), {
      id: this.form.value.id,
      name: this.form.value.name,
      questionSet: {
        id: this.form.value.questionSet,
      },
    });
    this.gameService.update(game.id, game).subscribe(game => this.refresh(game));
  }

  refresh( game: GameDto ){
    this.game.set(game);
    this.form.patchValue({
      id: game.id,
      name: game.name,
      questionSet: game?.questionSet?.id,
    });

    const questionSet = this.questionsSet().find(questionSet => questionSet.id === game.questionSet.id);

    if ( game.status === GameStateEnum.READY || game.status === GameStateEnum.IN_PREPARATION ) {
      console.log(this.questionsSet())
      this.actualQuestion.set(null);
      const nextQuestion = questionSet.questions.find(question => question.order === 1);
      this.nextQuestion.set(nextQuestion);
    }

    if ( game.status === GameStateEnum.IN_PROGRESS ) {
      const actualQuestion = game.currentQuestion;
      this.actualQuestion.set(actualQuestion);
      const nextQuestion = questionSet.questions.find(question => question.order === actualQuestion.order + 1);
      this.nextQuestion.set(nextQuestion);
    }
  }

  getUserScore( userId: string ): number{
    const questionSet = this.questionsSet().find(questionSet => questionSet.id === this.game().questionSet.id);
    const playersScore = this.gameResultStore.calculatePlayerScores(this.game(), questionSet);
    this.gameResultStore.results();
    const playerScore = playersScore.find(score => score.userId === userId);
    return playerScore ? playerScore.score : 0;
  }

  getUserAverageResponseTime( userId: string ): number{
    const questionSet = this.questionsSet().find(questionSet => questionSet.id === this.game().questionSet.id);
    const playersScore = this.gameResultStore.calculatePlayerScores(this.game(), questionSet);
    console.log(playersScore)
    const playerScore = playersScore.find(score => score.userId === userId);
    return playerScore ? playerScore.averageResponseTime : 0;
  }

  statusClass( status: GameStateEnum ){
    switch ( status ) {
      case GameStateEnum.IN_PREPARATION:
        return 'text-blue-400';
      case GameStateEnum.READY:
        return 'text-green-400';
      case GameStateEnum.IN_PROGRESS:
        return 'text-yellow-800';
      case GameStateEnum.FINISHED:
        return 'text-red-500';
      default:
        return '';
    }
  }

}
