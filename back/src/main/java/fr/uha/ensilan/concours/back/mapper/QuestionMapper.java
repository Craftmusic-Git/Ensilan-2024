package fr.uha.ensilan.concours.back.mapper;

import fr.uha.ensilan.concours.back.config.MapstructConfig;
import fr.uha.ensilan.concours.back.domain.question.Answer;
import fr.uha.ensilan.concours.back.domain.question.Question;
import fr.uha.ensilan.concours.back.domain.question.QuestionSet;
import fr.uha.ensilan.concours.back.dto.question.AnswerDto;
import fr.uha.ensilan.concours.back.dto.question.QuestionDto;
import fr.uha.ensilan.concours.back.dto.question.QuestionSetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapstructConfig.class)
public interface QuestionMapper extends DtoMapper<Question, QuestionDto> {
    AnswerMapper answerMapper = Mappers.getMapper(AnswerMapper.class);

    @Override
    @Mapping(target = "answers", source = "answers", qualifiedByName = "answerToDto")
    QuestionDto toDto(Question model);

    @Named("questionSetId")
    static QuestionSetDto questionSetId(QuestionSet question) {
        if (question == null) {
            return null;
        }

        QuestionSetDto questionSetDto = new QuestionSetDto();
        questionSetDto.setId(question.getId());
        return questionSetDto;
    }

    @Named("answerToDto")
    static Set<AnswerDto> answerToDto(Set<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            return Collections.emptySet();
        }

        return answers.stream().map(answerMapper::toDto).collect(Collectors.toSet());
    }
}
