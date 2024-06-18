package fr.uha.ensilan.concours.back.mapper;

import fr.uha.ensilan.concours.back.config.MapstructConfig;
import fr.uha.ensilan.concours.back.domain.question.Answer;
import fr.uha.ensilan.concours.back.domain.question.Question;
import fr.uha.ensilan.concours.back.dto.question.AnswerDto;
import fr.uha.ensilan.concours.back.dto.question.QuestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapstructConfig.class)
public interface AnswerMapper extends DtoMapper<Answer, AnswerDto>{
    @Override
    AnswerDto toDto(Answer answer);

    @Named("questionToDto")
    static QuestionDto questionToDto(Question question) {
        if (question == null) {
            return null;
        }

        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(question.getId());
        return questionDto;
    }
}
