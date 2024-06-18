
package fr.uha.ensilan.concours.back.dto.message;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GameDeletedMessage extends BaseMessage {
    private UUID id;

    public GameDeletedMessage() {
        super(MessageType.GAME_DELETED);
    }
}
