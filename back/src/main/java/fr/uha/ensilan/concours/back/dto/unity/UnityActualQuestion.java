package fr.uha.ensilan.concours.back.dto.unity;

import lombok.Data;

import java.util.List;

@Data
public class UnityActualQuestion {
    private UnityQuestion question;
    private UnityGameState state;
    private List<UnityUsers> users;
}
