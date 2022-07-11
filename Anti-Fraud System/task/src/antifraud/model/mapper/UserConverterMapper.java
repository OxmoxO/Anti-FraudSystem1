package antifraud.model.mapper;

import antifraud.model.dto.UserDto;
import antifraud.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverterMapper implements ConverterMapper<User, UserDto> {

    @Override
    public UserDto toDto(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public User toEntity(UserDto userDto) {
        return new User(userDto.getName(),
                userDto.getUsername(),
                userDto.getPassword());
    }
}
