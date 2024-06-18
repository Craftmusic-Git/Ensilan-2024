import {MessageTypeEnum} from "@domain/message/message-type.enum";

export abstract class BaseMessage {
  readonly type: MessageTypeEnum;
}
