import {BaseMessage} from "@domain/message/base.message";
import {MessageTypeEnum} from "@domain/message/message-type.enum";

export class GameDeletedMessage extends BaseMessage {
  override readonly type = MessageTypeEnum.GAME_DELETED;
  id: string;
}
