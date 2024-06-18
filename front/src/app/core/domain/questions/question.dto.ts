import {AnswerDto} from "@domain/questions/answer.dto";

export class QuestionDto {
  id: string;
  text: string;
  order: number;
  answers: AnswerDto[];
}
