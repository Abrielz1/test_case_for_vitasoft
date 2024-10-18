package ru.vitasoft.testcase.service;

import ru.vitasoft.testcase.model.dto.TicketDto;
import ru.vitasoft.testcase.model.dto.TicketNewDto;

public interface UserService {

    TicketDto getTokenCreatedByUser(Long userId, Boolean sort) ;

    TicketDto creationTicketByUser(Long userId, TicketNewDto newTicket);

    TicketDto editTicketByUser(Long userId, TicketNewDto newTicket);

    TicketDto sendTicketToOperatorToReview(Long userId);
}
