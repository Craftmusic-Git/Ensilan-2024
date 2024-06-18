import {MessageTypeEnum} from "@domain/message/message-type.enum";
import {QuestionDto} from "@domain/questions/question.dto";
import {BaseMessage} from "./base.message";

export class NextQuestionMessage extends BaseMessage {
  override readonly type = MessageTypeEnum.NEXT_QUESTION;
  question: QuestionDto;
}
