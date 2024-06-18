import {Injectable} from "@angular/core";
import {GamesService} from "@core/service/games.service";
import {GameDto} from "@domain/games/game.dto";
import { AbstractStore } from "./abstract.store";

@Injectable({providedIn: 'root'})
export class GameAdminStore extends AbstractStore<GameDto>{
  constructor(gameService: GamesService){
    super(gameService);
  }
}
