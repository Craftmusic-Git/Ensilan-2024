import {GamePlayerResultDto} from "@domain/games/game-player-result.dto";
import {BaseMessage} from "@domain/message/base.message";
import {AnswerDto} from "@domain/questions/answer.dto";
import { MessageTypeEnum } from "./message-type.enum";

export class ShowAnswerMessage extends BaseMessage {
    override readonly type = MessageTypeEnum.SHOW_ANSWER;
    answer: AnswerDto;
}
