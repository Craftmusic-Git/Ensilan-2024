import {GamePlayerResultDto} from "@domain/games/game-player-result.dto";

export class GameQuestionResultDto {
  id: string;
  questionId: string;
  correctAnswerId: string;
  gamePlayerResults: GamePlayerResultDto[];
}
