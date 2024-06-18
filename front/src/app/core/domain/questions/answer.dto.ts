import {QuestionDto} from "@domain/questions/question.dto";

export class AnswerDto {
  id: string;
  text: string;
  isCorrect: boolean;
  question: QuestionDto;
}
