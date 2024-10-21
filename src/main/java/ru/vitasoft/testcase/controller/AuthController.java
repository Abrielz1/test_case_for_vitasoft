package ru.vitasoft.testcase.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.vitasoft.testcase.model.dto.in.LoginRequest;
import ru.vitasoft.testcase.model.dto.responce.AuthResponseDto;
import ru.vitasoft.testcase.model.dto.responce.UserDto;
import ru.vitasoft.testcase.security.dto.in.RefreshTokenRequestDto;
import ru.vitasoft.testcase.security.dto.out.RefreshTokenResponseDto;
import ru.vitasoft.testcase.security.jwt.service.SecurityService;
import ru.vitasoft.testcase.utils.Create;
import ru.vitasoft.testcase.utils.Update;

@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SecurityService securityService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseDto loginUserToAccount(@NotBlank @Validated(Update.class) @RequestBody LoginRequest userLogin) {

        return securityService.authenticationUser(userLogin);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public RefreshTokenResponseDto refreshTokenAuthorizedUserSession(@NotBlank @Validated(Update.class)
                                                                     @RequestBody RefreshTokenRequestDto tokenRequestDto) {
        return securityService.refreshToken(tokenRequestDto);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public String logoutCurrentUserSession(@AuthenticationPrincipal UserDetails details) {

        securityService.logout();
        return "User with username: %s canceled session".formatted(details.getUsername());
    }
}
