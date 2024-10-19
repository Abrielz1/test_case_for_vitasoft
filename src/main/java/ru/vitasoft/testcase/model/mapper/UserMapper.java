package ru.vitasoft.testcase.model.mapper;

import ru.vitasoft.testcase.exception.exceptions.BadRequestException;
import ru.vitasoft.testcase.model.dto.UserDto;
import ru.vitasoft.testcase.model.entity.User;

public class UserMapper {

    private UserMapper() {
        throw new BadRequestException("Utility Class!");
    }

    public static UserDto toDto(User user) {

        new UserDto();
        return UserDto
                .builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(user.getRoles())
                .build();
    }
}
