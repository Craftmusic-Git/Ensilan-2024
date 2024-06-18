import {MessageTypeEnum} from "@domain/message/message-type.enum";
import { BaseMessage } from "./base.message";

export class StopGameMessage extends BaseMessage {
    override readonly type = MessageTypeEnum.STOP_GAME;
}
