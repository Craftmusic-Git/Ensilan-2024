package fr.uha.ensilan.concours.back.dto.message;

import fr.uha.ensilan.concours.back.dto.question.QuestionDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartGameMessage extends BaseMessage {
    private QuestionDto question;

    public StartGameMessage() {
        super(MessageType.START_GAME);
    }
}
