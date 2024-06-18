package fr.uha.ensilan.concours.back.dto.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StopGameMessage extends BaseMessage {
    public StopGameMessage() {
        super(MessageType.STOP_GAME);
    }
}
