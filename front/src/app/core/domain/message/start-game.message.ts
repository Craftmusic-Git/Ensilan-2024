import {BaseMessage} from "@domain/message/base.message";
import {MessageTypeEnum} from "@domain/message/message-type.enum";
import {QuestionDto} from "@domain/questions/question.dto";

export class StartGameMessage extends BaseMessage {
    override readonly type = MessageTypeEnum.START_GAME;
    question: QuestionDto;
}
