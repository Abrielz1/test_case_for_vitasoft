package ru.vitasoft.testcase.service;

import ru.vitasoft.testcase.model.dto.UserDto;
import java.util.List;

public interface UserService {

    List<UserDto> getAllUsersList(Integer from, Integer size);

    UserDto getUserByUsername(UserDto userDto);

    UserDto approveUserToOperatorRole(Long userId);

    UserDto registerUserOnServer(UserDto newUser);
}
