package fr.uha.ensilan.concours.back.dto.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseMessage {
    protected MessageType type;

    public BaseMessage(MessageType type) {
        this.type = type;
    }
}
