import {GameStateEnum} from "@domain/games/game-state.enum";
import {QuestionSetDto} from "@domain/questions/question-set.dto";
import {QuestionDto} from "@domain/questions/question.dto";
import {UserDto} from "@domain/user.dto";

export class GameDto {
  id: string;
  name: string;
  status: GameStateEnum;
  questionSet: QuestionSetDto;
  currentQuestion: QuestionDto;
  players: UserDto[];
  waitingPlayers: UserDto[];
}
