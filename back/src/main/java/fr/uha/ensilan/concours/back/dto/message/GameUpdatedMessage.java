package fr.uha.ensilan.concours.back.dto.message;

import fr.uha.ensilan.concours.back.dto.game.GameDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameUpdatedMessage extends BaseMessage {
    private GameDto game;

    public GameUpdatedMessage() {
        super(MessageType.GAME_UPDATED);
    }
}
