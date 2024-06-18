package fr.uha.ensilan.concours.back.dto.message;

import fr.uha.ensilan.concours.back.dto.game.GamePlayerResultDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AnswerQuestionMessage extends BaseMessage {
    private GamePlayerResultDto response;

    public AnswerQuestionMessage() {
        super(MessageType.ANSWER_QUESTION);
    }
}
