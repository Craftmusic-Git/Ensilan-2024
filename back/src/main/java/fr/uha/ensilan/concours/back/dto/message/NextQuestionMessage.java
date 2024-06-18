package fr.uha.ensilan.concours.back.dto.message;

import fr.uha.ensilan.concours.back.dto.question.QuestionDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NextQuestionMessage extends BaseMessage {
    QuestionDto question;

    public NextQuestionMessage() {
        super(MessageType.NEXT_QUESTION);
    }
}
