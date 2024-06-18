package fr.uha.ensilan.concours.back.dto.message;

import fr.uha.ensilan.concours.back.dto.game.GameDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameCreatedMessage extends BaseMessage {
    private GameDto game;

    public GameCreatedMessage() {
        super(MessageType.GAME_CREATED);
    }
}
