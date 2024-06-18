package fr.uha.ensilan.concours.back.dto.user;

import fr.uha.ensilan.concours.back.domain.user.UserClass;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String username;
    private String lastname;
    private UserClass userClass;
}
