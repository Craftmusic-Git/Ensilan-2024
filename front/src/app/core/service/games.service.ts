import {Injectable} from '@angular/core';
import {GameQuestionResultDto} from "@domain/games/game-question-result.dto";
import {AnswerDto} from "@domain/questions/answer.dto";
import {QuestionDto} from "@domain/questions/question.dto";
import {UserDto} from "@domain/user.dto";
import {environment} from "../../../environments/environment";
import {BaseService} from './base.service';
import {GameDto} from '@domain/games/game.dto';

@Injectable({
  providedIn: 'root',
})
export class GamesService extends BaseService<GameDto> {

  constructor(){
    super('admin/games');
  }

  removePlayerFromWaitingList( gameId: string, player: UserDto ){
    return this.http.post<UserDto>(`${environment.apiUrl + `/${this.path}`}/${gameId}/leave-player`, player);
  }

  addPlayerToGame( gameId: string, player: UserDto ){
    return this.http.post<UserDto>(`${environment.apiUrl + `/${this.path}`}/${gameId}/join-player`, player);
  }

  removePlayerToGame( gameId: string, player: UserDto ){
    return this.http.post<UserDto>(`${environment.apiUrl + `/${this.path}`}/${gameId}/remove-player`, player);
  }

  startGame( gameId: string ){
    return this.http.post<QuestionDto>(`${environment.apiUrl + `/${this.path}`}/${gameId}/start`, null);
  }

  nextQuestion( gameId: string ){
    return this.http.post<QuestionDto>(`${environment.apiUrl + `/${this.path}`}/${gameId}/next`, null);
  }

  endGame( gameId: string ){
    return this.http.post<GameDto>(`${environment.apiUrl + `/${this.path}`}/${gameId}/end`, null);
  }

  unlockAnswers( gameId: string ){
    return this.http.post<QuestionDto>(`${environment.apiUrl + `/${this.path}`}/${gameId}/unlock-answers`, null);
  }

  showAnswer( gameId: string, answer: AnswerDto ){
    return this.http.post<AnswerDto>(`${environment.apiUrl + `/${this.path}`}/${gameId}/show-answer`, answer);
  }

  diffuse( gameId: string ){
    return this.http.post(`${environment.apiUrl + `/${this.path}`}/${gameId}/diffuse`, {});
  }
}
