package fr.uha.ensilan.concours.back.dto.message;

import fr.uha.ensilan.concours.back.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerJoinMessage extends BaseMessage {
    private UserDto player;

    public PlayerJoinMessage() {
        super(MessageType.PLAYER_JOIN);
    }
}
