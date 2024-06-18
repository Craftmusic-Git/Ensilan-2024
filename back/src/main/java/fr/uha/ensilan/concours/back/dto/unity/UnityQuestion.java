package fr.uha.ensilan.concours.back.dto.unity;

import lombok.Data;

import java.util.List;

@Data
public class UnityQuestion {
    private Long id;
    private String text;
    private List<UnityAnswers> answers;
}
