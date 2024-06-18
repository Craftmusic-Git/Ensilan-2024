import {GamePlayerResultDto} from "@domain/games/game-player-result.dto";
import {MessageTypeEnum} from "@domain/message/message-type.enum";
import { BaseMessage } from "./base.message";

export class AnswerQuestionMessage extends BaseMessage {
    override readonly type = MessageTypeEnum.ANSWER_QUESTION;
    response: GamePlayerResultDto;
}
