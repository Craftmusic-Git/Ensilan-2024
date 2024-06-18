package fr.uha.ensilan.concours.back.mapper;

import fr.uha.ensilan.concours.back.config.MapstructConfig;
import fr.uha.ensilan.concours.back.domain.game.GamePlayerResult;
import fr.uha.ensilan.concours.back.domain.game.GameQuestionResult;
import fr.uha.ensilan.concours.back.dto.game.GamePlayerResultDto;
import fr.uha.ensilan.concours.back.dto.game.GameQuestionResultDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapstructConfig.class)
public interface GameQuestionResultMapper extends DtoMapper<GameQuestionResult, GameQuestionResultDto> {
    GamePlayerResultMapper gamePlayerResultMapper = Mappers.getMapper(GamePlayerResultMapper.class);

    @Override
    @Mapping(target = "gameId", source = "game.id")
    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "gamePlayerResults", source = "gamePlayerResults", qualifiedByName = "toPlayerDto")
    GameQuestionResultDto toDto(GameQuestionResult model);

    @Named("toPlayerDto")
    static Set<GamePlayerResultDto> toPlayerDto(Set<GamePlayerResult> model) {;
        return model.stream().map(gamePlayerResultMapper::toDto).collect(Collectors.toSet());
    }
}
