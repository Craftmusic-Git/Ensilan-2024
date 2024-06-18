package fr.uha.ensilan.concours.back.dto.message;

public enum MessageType {
    START_GAME,
    STOP_GAME,
    NEXT_QUESTION,
    ANSWER_QUESTION,
    UNLOCK_ANSWERS,
    SHOW_ANSWER,
    PLAYER_JOIN_WAITING_ROOM,
    PLAYER_JOIN,
    PLAYER_LEAVE_WAITING_ROOM,
    PLAYER_LEAVE,
    GAME_CREATED,
    GAME_DELETED,
    GAME_UPDATED,
}
