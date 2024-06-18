import {HttpClient} from "@angular/common/http";
import { Injectable, inject } from '@angular/core';
import {GameQuestionResultDto} from "@domain/games/game-question-result.dto";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class GamesResultService {
  private readonly path = 'public/games-result';
  http = inject(HttpClient);

  constructor() { }

  getResultsForGame(gameId: string){
    return this.http.get<GameQuestionResultDto[]>(`${environment.apiUrl}/${this.path}/${gameId}/results`);
  }
}
