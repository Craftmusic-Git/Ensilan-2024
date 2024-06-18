import {QuestionDto} from "@domain/questions/question.dto";

export class QuestionSetDto {
  id: string;
  name: string;
  questions: QuestionDto[];
}
