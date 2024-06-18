import {GameDto} from "@domain/games/game.dto";
import { BaseMessage } from "./base.message";
import { MessageTypeEnum } from "./message-type.enum";

export class GameCreatedMessage extends BaseMessage {
  override readonly type = MessageTypeEnum.GAME_CREATED;
  game: GameDto;
}
