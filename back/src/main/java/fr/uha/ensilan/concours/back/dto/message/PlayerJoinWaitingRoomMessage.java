package fr.uha.ensilan.concours.back.dto.message;

import fr.uha.ensilan.concours.back.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerJoinWaitingRoomMessage extends BaseMessage {
    private UserDto player;

    public PlayerJoinWaitingRoomMessage() {
        super(MessageType.PLAYER_JOIN_WAITING_ROOM);
    }
}
