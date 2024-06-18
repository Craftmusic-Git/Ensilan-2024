import {GameDto} from "@domain/games/game.dto";
import {BaseMessage} from "@domain/message/base.message";
import {MessageTypeEnum} from "@domain/message/message-type.enum";

export class GameUpdatedMessage extends BaseMessage {
  override readonly type = MessageTypeEnum.GAME_UPDATED;
  game: GameDto;
}
