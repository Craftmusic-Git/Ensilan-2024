package fr.uha.ensilan.concours.back.mapper;

import fr.uha.ensilan.concours.back.config.MapstructConfig;
import fr.uha.ensilan.concours.back.domain.user.User;
import fr.uha.ensilan.concours.back.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface UserMapper extends DtoMapper<User, UserDto> {

}
