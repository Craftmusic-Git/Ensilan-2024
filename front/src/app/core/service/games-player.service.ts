import {HttpClient, HttpParams} from "@angular/common/http";
import {inject, Injectable, signal} from '@angular/core';
import {AnswerResponseDto} from "@domain/answer-response.dto";
import {GameQuestionResultDto} from "@domain/games/game-question-result.dto";
import {GameDto} from "@domain/games/game.dto";
import {UserDto} from "@domain/user.dto";
import {catchError, EMPTY, finalize, first} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class GamesPlayerService {
  protected http = inject(HttpClient);
  protected path = "public/games";

  protected _loading = signal(false);

  get loading() {
    return this._loading();
  }

  getById(id: string) {
    this._loading.set(true);
    return this.http
      .get<GameDto>(environment.apiUrl + `/${this.path}/${id}`)
      .pipe(
        first(),
        catchError(() => EMPTY),
        finalize(() => this._loading.set(false))
      );
  }

  joinGame(gameId: string, player: UserDto) {
    return this.http
      .post<UserDto>(environment.apiUrl + `/${this.path}/${gameId}/join-waiting`, player)
      .pipe(first());
  }

  leaveGame(gameId: string, player: UserDto) {
    return this.http
      .post<UserDto>(environment.apiUrl + `/${this.path}/${gameId}/leave-waiting`, player)
      .pipe(first());
  }

  findAll() {
    this._loading.set(true);
    return this.http
      .get<GameDto[]>(environment.apiUrl + `/${this.path}`)
      .pipe(
        first(),
        catchError(() => EMPTY),
        finalize(() => this._loading.set(false))
      );
  }

  answer(gameId: string, userId: string, questionId: string, answerId: string, duration: number) {
    return this.http
      .post<AnswerResponseDto>(environment.apiUrl + `/${this.path}/${gameId}/answer`, {userId, questionId, answerId, duration} as AnswerResponseDto)
      .pipe(first());
  }

  getResultsForGame(gameId: string){
    return this.http.get<GameQuestionResultDto[]>(`${environment.apiUrl}/${this.path}/${gameId}/results`);
  }
}
