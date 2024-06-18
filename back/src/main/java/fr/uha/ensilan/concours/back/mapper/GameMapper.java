package fr.uha.ensilan.concours.back.mapper;


import fr.uha.ensilan.concours.back.config.MapstructConfig;
import fr.uha.ensilan.concours.back.domain.game.Game;
import fr.uha.ensilan.concours.back.domain.question.QuestionSet;
import fr.uha.ensilan.concours.back.dto.game.GameDto;
import fr.uha.ensilan.concours.back.dto.question.QuestionSetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public interface GameMapper extends DtoMapper<Game, GameDto>{
    QuestionSetMapper questionMapper = Mappers.getMapper(QuestionSetMapper.class);

    @Override
    @Mapping(target = "questionSet", source = "questionSet", qualifiedByName = "questionSetId")
    GameDto toDto(Game model);

    @Named("questionSetId")
    static QuestionSetDto questionSetId(QuestionSet questionSet) {
        if (questionSet == null) {
            return null;
        }

        var questionSetDto = new QuestionSetDto();
        questionSetDto.setId(questionSet.getId());
        return questionSetDto;
    }
}
