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
import ru.vitasoft.testcase.model.dto.responce.TicketDto;
import ru.vitasoft.testcase.model.dto.responce.UserDto;
import ru.vitasoft.testcase.service.TicketService;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/operators")
@RequiredArgsConstructor
public class OperatorController {

    private final TicketService ticketService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketDto> getTicketCreatedByUsersToCheckByOperator(
            @RequestParam(defaultValue = "false", required = false) Boolean sort,
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

    @PutMapping("accept/{authorId}/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDto createUserTicket(@Positive @PathVariable(name = "authorId") Long authorId,
                                      @Positive @PathVariable(name = "ticketId") Long  ticketId) {

        return ticketService.acceptUserTicket(authorId, ticketId);
    }

    @PutMapping("reject/{authorId}/{ticketId}")
    @ResponseStatus(HttpStatus.OK)
    public TicketDto rejectTicketByAuthorId(@Positive @PathVariable(name = "authorId") Long authorId,
                                            @Positive @PathVariable(name = "ticketId") Long  ticketId) {


        return ticketService.rejectTicketByAuthorId(authorId, ticketId);
    }
}
