package fr.uha.ensilan.concours.back.mapper;

import fr.uha.ensilan.concours.back.config.MapstructConfig;
import fr.uha.ensilan.concours.back.domain.game.GamePlayerResult;
import fr.uha.ensilan.concours.back.dto.game.GamePlayerResultDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface GamePlayerResultMapper extends DtoMapper<GamePlayerResult, GamePlayerResultDto> {
    @Override
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "answerId", source = "answer.id")
    GamePlayerResultDto toDto(GamePlayerResult gamePlayerResult);
}
