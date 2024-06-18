package fr.uha.ensilan.concours.back.dto.message;

import fr.uha.ensilan.concours.back.dto.question.AnswerDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowAnswerMessage extends BaseMessage {
    private AnswerDto answer;

    public ShowAnswerMessage() {
        super(MessageType.SHOW_ANSWER);
    }
}
