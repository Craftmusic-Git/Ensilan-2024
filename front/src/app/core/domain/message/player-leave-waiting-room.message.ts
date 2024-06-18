import {BaseMessage} from "@domain/message/base.message";
import {MessageTypeEnum} from "@domain/message/message-type.enum";
import {UserDto} from "@domain/user.dto";

export class PlayerLeaveWaitingRoomMessage extends BaseMessage {
  override readonly type = MessageTypeEnum.PLAYER_LEAVE_WAITING_ROOM;
  player: UserDto;
}

