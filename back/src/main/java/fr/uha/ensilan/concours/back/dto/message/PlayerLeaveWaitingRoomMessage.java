package fr.uha.ensilan.concours.back.dto.message;

import fr.uha.ensilan.concours.back.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerLeaveWaitingRoomMessage extends BaseMessage {
    private UserDto player;

    public PlayerLeaveWaitingRoomMessage() {
        super(MessageType.PLAYER_LEAVE_WAITING_ROOM);
    }
}
