import {BaseMessage} from "@domain/message/base.message";
import {MessageTypeEnum} from "@domain/message/message-type.enum";
import {UserDto} from "@domain/user.dto";

export class PlayerLeaveMessage extends BaseMessage {
  override readonly type = MessageTypeEnum.PLAYER_LEAVE;
  player: UserDto;
}
