import {inject, Injectable, signal} from "@angular/core";
import {GamesPlayerService} from "@core/service/games-player.service";
import {GamesWebsocket} from "@core/websocket/games.websocket";
import {GameDto} from "@domain/games/game.dto";
import {GameCreatedMessage} from "@domain/message/game-created.message";
import {GameDeletedMessage} from "@domain/message/game-deleted.message";
import {GameUpdatedMessage} from "@domain/message/game-updated.message";
import {MessageTypeEnum} from "@domain/message/message-type.enum";

@Injectable({providedIn: 'root'})
export class GamesPlayerStore {
  service = inject(GamesPlayerService);
  webSocket = inject(GamesWebsocket);

  games = signal<GameDto[]>([]);

  constructor(){
    this.service.findAll().subscribe(games => {
      this.games.set(games);
    });

    this.webSocket.message$.subscribe(message => {
      if (message.type === MessageTypeEnum.GAME_CREATED) {
        this.games.set([...this.games(), (message as GameCreatedMessage).game]);
      } else if (message.type === MessageTypeEnum.GAME_DELETED) {
        this.games.set(this.games().filter(game => game.id !== (message as GameDeletedMessage).id));
      } else if (message.type === MessageTypeEnum.GAME_UPDATED) {
        const game = (message as GameUpdatedMessage).game;
        this.games.set(this.games().map(g => g.id === game.id ? game : g));
      }
    });
  }
}
