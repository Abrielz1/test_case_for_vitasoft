package ru.vitasoft.testcase.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
import ru.vitasoft.testcase.service.TicketService;
import ru.vitasoft.testcase.utils.Create;
import ru.vitasoft.testcase.utils.Update;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final TicketService userService;

    @GetMapping("/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketDto> getTicketCreatedByUser(@Positive @PathVariable(name = "authorId") Long authorId,
                                                 @RequestParam Boolean sort,
                                                 @PositiveOrZero  @RequestParam(defaultValue = "0")  Integer from,
                                                 @Positive @RequestParam(defaultValue = "5")  Integer size) {

       return userService.getTicketCreatedByUser(authorId, sort, from, size);
    }

    @PutMapping("/{authorId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDto creationTicketByUser(@Positive @PathVariable(name = "authorId") Long authorId,
                                          @Validated(Create.class)@RequestBody TicketNewDto newTicket) {

        return userService.creationTicketByUser(authorId, newTicket);
    }

    @PostMapping("/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDto editTicketByUser(@Positive @PathVariable(name = "authorId") Long authorId,
                                      @Validated(Update.class)@RequestBody TicketNewDto ticketToUpdate) {

        return userService.editTicketByUser(authorId, ticketToUpdate);
    }

    @PutMapping("/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDto sendTicketToOperatorToReview(@Positive @PathVariable(name = "authorId") Long authorId) {

        return userService.sendTicketToOperatorToReview(authorId);
    }
}
