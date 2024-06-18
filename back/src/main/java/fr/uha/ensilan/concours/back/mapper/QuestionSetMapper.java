package fr.uha.ensilan.concours.back.mapper;

import fr.uha.ensilan.concours.back.config.MapstructConfig;
import fr.uha.ensilan.concours.back.domain.question.Question;
import fr.uha.ensilan.concours.back.domain.question.QuestionSet;
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
public interface QuestionSetMapper extends DtoMapper<QuestionSet, QuestionSetDto> {
    QuestionMapper questionMapper = Mappers.getMapper(QuestionMapper.class);

    @Override
    @Mapping(target = "questions", source = "questions", qualifiedByName = "questionToDto")
    QuestionSetDto toDto(QuestionSet model);

    @Named("questionToDto")
    static Set<QuestionDto> questionToDto(Set<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            return Collections.emptySet();
        }

        return questions.stream().map(questionMapper::toDto).collect(Collectors.toSet());
    }
}
