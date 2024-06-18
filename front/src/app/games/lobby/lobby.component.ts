import {Component, inject, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {GameDto} from '@app/core/domain/games/game.dto';
import {QuestionDto} from '@app/core/domain/questions/question.dto';
import {GamesPlayerService} from "@core/service/games-player.service";
import {UserAuthService} from "@core/service/user-auth.service";
import {GameWebsocket} from "@core/websocket/game.websocket";
import {GameStateEnum} from "@domain/games/game-state.enum";
import {GameUpdatedMessage} from "@domain/message/game-updated.message";
import {MessageTypeEnum} from "@domain/message/message-type.enum";
import {NextQuestionMessage} from "@domain/message/next-question.message";
import {PlayerJoinWaitingRoomMessage} from "@domain/message/player-join-waiting-room.message";
import {PlayerJoinMessage} from "@domain/message/player-join.message";
import {PlayerLeaveWaitingRoomMessage} from "@domain/message/player-leave-waiting-room.message";
import {StartGameMessage} from "@domain/message/start-game.message";
import {AnswerDto} from "@domain/questions/answer.dto";
import {UserDto} from "@domain/user.dto";

@Component({
  selector: 'app-lobby',
  standalone: true,
  imports: [],
  templateUrl: './lobby.component.html',
  styleUrl: './lobby.component.scss',
})
export class LobbyComponent {
  gameService = inject(GamesPlayerService);
  webSocket = inject(GameWebsocket);
  userAuthService = inject(UserAuthService);
  private route = inject(ActivatedRoute);

  game = signal<GameDto>(null)
  actualQuestion = signal<QuestionDto>(null);
  lastActualisation = signal<number>(Date.now());
  lockResponse = signal<boolean>(true);
  showAnswer = signal<boolean>(false);
  selectedAnswer = signal<string>(null);

  constructor(){
    this.route.params.subscribe(params => {
      this.gameService.getById(params['id']).subscribe(game => {
        this.game.set(game)
        console.log("Game : ", this.game());
        this.webSocket.connect(params['id']);
        this.initGame()
        this.actualQuestion.set(this.game().currentQuestion);
      });
    });

  }

  initGame(){
    const user = this.userAuthService.getUser();
    if ( this.game().players.find(player => player.id === user.id) === undefined && this.game().waitingPlayers.find(player => player.id === user.id) === undefined ) {
      this.gameService.joinGame(this.game().id, user).subscribe();
    }
    this.websocketInit();
  }

  websocketInit(){
    this.webSocket.message$.subscribe(message => {
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
            return game;
          })
          break;
        case MessageTypeEnum.PLAYER_LEAVE:
          this.game.update(game => {
            game.players = game.players.filter(player => player.id !== (message as PlayerJoinMessage).player.id);
            return game;
          })
          break;
        case MessageTypeEnum.START_GAME:
          this.game.set(
            {
              ...this.game(),
              status: GameStateEnum.IN_PROGRESS,
            } as GameDto,
          );
          this.onNewQuestion(message as StartGameMessage);
          break;
        case MessageTypeEnum.NEXT_QUESTION:
          this.onNewQuestion(message as NextQuestionMessage);
          break;
        case MessageTypeEnum.UNLOCK_ANSWERS:
          this.lockResponse.set(false);
          this.lastActualisation.set(Date.now());
          setTimeout(() => {
            this.lockResponse.set(true);
          }, 5000);
          break;
        case MessageTypeEnum.STOP_GAME:
          this.game.set(
            {
              ...this.game(),
              status: GameStateEnum.FINISHED,
            } as GameDto,
          );
          break;
        case MessageTypeEnum.GAME_UPDATED:
          this.game.set(
            {
              ...this.game(),
              status: ((message as GameUpdatedMessage).game).status,
              name: ((message as GameUpdatedMessage).game).name,
            },
          );
          break;
        case MessageTypeEnum.SHOW_ANSWER:
          this.showAnswer.set(true);
          break;
        default:
          break;
      }
    });
  }

  onNewQuestion( message: NextQuestionMessage | StartGameMessage ){
    this.actualQuestion.set((message as StartGameMessage).question);
    this.showAnswer.set(false);
  }

  selectAnswer( answerId: string ){
    const user = this.userAuthService.getUser();
    this.selectedAnswer.set(answerId);
    console.log("Now : ", Date.now());
    console.log("Last actualisation : ", this.lastActualisation());
    console.log("Time diff : ", Date.now() - this.lastActualisation());
    this.gameService.answer(this.game().id, user.id, this.actualQuestion().id, answerId, Date.now() - this.lastActualisation()).subscribe();
  }

  calculateButtonBackground( answer: AnswerDto ){
    if ( this.showAnswer() && answer.isCorrect ) {
      return 'bg-green-500'
    } else if ( this.selectedAnswer() === answer.id && this.showAnswer() && !answer.isCorrect ) {
      return 'bg-red-500'
    } else if ( this.selectedAnswer() === answer.id && !this.showAnswer() ) {
      return 'bg-yellow-800'
    } else {
      return 'bg-purple-900 disabled:bg-purple-950'
    }
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
