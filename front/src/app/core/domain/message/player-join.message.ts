import {BaseMessage} from "@domain/message/base.message";
import {MessageTypeEnum} from "@domain/message/message-type.enum";
import {UserDto} from "@domain/user.dto";

export class PlayerJoinMessage extends BaseMessage {
  override readonly type = MessageTypeEnum.PLAYER_JOIN;
  player: UserDto;
}
