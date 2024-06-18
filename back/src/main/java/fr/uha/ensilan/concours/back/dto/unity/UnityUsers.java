package fr.uha.ensilan.concours.back.dto.unity;

import fr.uha.ensilan.concours.back.domain.user.UserClass;
import lombok.Data;

@Data
public class UnityUsers {
    private Long id;
    private String firstName;
    private String lastName;
    private UserClass userClass;
    private Long score;
}
