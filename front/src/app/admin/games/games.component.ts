import {Component, inject} from '@angular/core';
import {RouterLink} from "@angular/router";
import {GamesService} from '@app/core/service/games.service';
import {GameAdminStore} from '@core/store/game-admin.store';
import {GamesWebsocket} from "@core/websocket/games.websocket";
import {GameDto} from "@domain/games/game.dto";

@Component({
  selector: 'app-games',
  standalone: true,
  imports: [
    RouterLink,
  ],
  templateUrl: './games.component.html',
  styleUrl: './games.component.scss',
})
export class GamesComponent {
  gameService = inject(GamesService);
  gameStore = inject(GameAdminStore);
  gamesWebService = inject(GamesWebsocket);

  constructor(){
    this.gameStore.refresh();
  }

  createGame(){
    this.gameService.create(Object.assign(new GameDto(), {name: 'New game'}))
      .subscribe(() => this.gameStore.refresh());
  }

  deleteGame( id: string ){
    this.gameService.delete(id).subscribe(() => this.gameStore.refresh());
  }
}
