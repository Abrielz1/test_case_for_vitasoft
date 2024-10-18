package ru.vitasoft.testcase.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.vitasoft.testcase.model.dto.TicketDto;
import ru.vitasoft.testcase.model.dto.TicketNewDto;
import ru.vitasoft.testcase.service.UserService;
import ru.vitasoft.testcase.utils.Create;
import ru.vitasoft.testcase.utils.Update;

@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET with sort avers and revers by date of creation

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDto getTokenCreatedByUser(@Positive @PathVariable(name = "userId") Long userId,
                                           @RequestParam Boolean sort) {

       return userService.getTokenCreatedByUser(userId, sort);
    }

    // PUT create tickets

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDto creationTicketByUser(@Positive @PathVariable(name = "userId") Long userId,
                                          @Validated(Create.class)@RequestBody TicketNewDto newTicket) {

        return userService.creationTicketByUser(userId, newTicket);
    }

    // POST edit tickets with DRAFT status

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDto editTicketByUser(@Positive @PathVariable(name = "userId") Long userId,
                                      @Validated(Update.class)@RequestBody TicketNewDto newTicket) {

        return userService.editTicketByUser(userId, newTicket);
    }

    // PUT SEND tickets to OPERATOR for review

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDto sendTicketToOperatorToReview(@Positive @PathVariable(name = "userId") Long userId) {

        return userService.sendTicketToOperatorToReview(userId);
    }
}
