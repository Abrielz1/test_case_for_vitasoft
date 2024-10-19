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
import ru.vitasoft.testcase.model.dto.TicketDto;
import ru.vitasoft.testcase.model.dto.TicketNewDto;
import ru.vitasoft.testcase.model.dto.UserDto;
import ru.vitasoft.testcase.service.TicketService;
import ru.vitasoft.testcase.utils.Update;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/operator")
@RequiredArgsConstructor
public class OperatorController {

    private final TicketService ticketService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketDto> getTicketCreatedByUsersToCheckByOperator(
            @RequestParam Boolean sort,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "5")  Integer size) {

        return ticketService.getTicketCreatedByUsersToCheckByOperator(sort, from, size);
    }

    @GetMapping("/author")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketDto> getTicketCreatedByUserToCheckByOperatorByUsername(
            @RequestParam Boolean sort,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "5")  Integer size,
            @NotBlank @RequestBody UserDto authorDto) {

        return ticketService.getTicketCreatedByUserToCheckByOperatorByUsername(sort, from, size, authorDto);
    }

    @PutMapping("accept/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDto createUserTicket(@Validated(Update.class) @Positive @PathVariable(name = "authorId") Long authorId,
                                      @NotBlank @RequestBody TicketNewDto newTicket) {

        return ticketService.acceptUserTicket(authorId, newTicket);
    }

    @PutMapping("reject/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDto rejectTicketByAuthorId(@Validated(Update.class)
                                                @Positive @PathVariable(name = "authorId") Long authorId,
                                            @NotBlank @RequestBody TicketNewDto newTicket) {

        return ticketService.rejectTicketByAuthorId(authorId, newTicket);
    }
}
