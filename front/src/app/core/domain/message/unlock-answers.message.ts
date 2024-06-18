import {MessageTypeEnum} from "@domain/message/message-type.enum";
import { BaseMessage } from "./base.message";

export class UnlockAnswersMessage extends BaseMessage {
  override readonly type = MessageTypeEnum.UNLOCK_ANSWERS;
}
