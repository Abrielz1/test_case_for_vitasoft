package ru.vitasoft.testcase.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.vitasoft.testcase.service.UserService;
import ru.vitasoft.testcase.model.dto.responce.UserDto;
import ru.vitasoft.testcase.utils.Update;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsersList(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Positive @RequestParam(defaultValue = "5")  Integer size) {

        return userService.getAllUsersList(from, size);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserByUsername(@NotBlank @Validated(Update.class) @RequestBody UserDto userDto) {

        return userService.getUserByUsername(userDto);
    }

    @PutMapping("/operator/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto approveUserToOperatorRole(@Positive @PathVariable(name = "userId") Long userId) {

        return userService.approveUserToOperatorRole(userId);
    }
}
