package ru.vitasoft.testcase.security.jwt.service;

import ru.vitasoft.testcase.model.dto.in.LoginRequest;
import ru.vitasoft.testcase.model.dto.responce.AuthResponseDto;
import ru.vitasoft.testcase.model.dto.responce.UserDto;
import ru.vitasoft.testcase.security.dto.in.RefreshTokenRequestDto;
import ru.vitasoft.testcase.security.dto.out.RefreshTokenResponseDto;

public interface SecurityService {

    UserDto register(UserDto createUserRequest);

    AuthResponseDto authenticationUser(LoginRequest loginRequest);

    void logout();

    RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto request);
}
