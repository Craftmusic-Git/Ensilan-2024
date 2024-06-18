import {BaseMessage} from "@domain/message/base.message";
import {MessageTypeEnum} from "@domain/message/message-type.enum";
import {UserDto} from "@domain/user.dto";

export class PlayerJoinWaitingRoomMessage extends BaseMessage {
  override readonly type = MessageTypeEnum.PLAYER_JOIN_WAITING_ROOM;
  player: UserDto;
}
