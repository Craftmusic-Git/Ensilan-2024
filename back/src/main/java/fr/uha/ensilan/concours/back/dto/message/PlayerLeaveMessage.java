package fr.uha.ensilan.concours.back.dto.message;

import fr.uha.ensilan.concours.back.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerLeaveMessage extends BaseMessage {
    private UserDto player;

    public PlayerLeaveMessage() {
        super(MessageType.PLAYER_LEAVE);
    }
}
