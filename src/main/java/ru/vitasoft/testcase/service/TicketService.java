package ru.vitasoft.testcase.service;

import ru.vitasoft.testcase.model.dto.TicketDto;
import ru.vitasoft.testcase.model.dto.TicketNewDto;
import ru.vitasoft.testcase.model.dto.UserDto;
import java.util.List;

public interface TicketService {

    List<TicketDto> getTicketCreatedByUser(Long userId,
                                           Boolean sort,
                                           Integer from,
                                           Integer  size) ;

    TicketDto creationTicketByUser(Long userId,
                                   TicketNewDto newTicket);

    TicketDto editTicketByUser(Long userId,
                               TicketNewDto newTicket);

    TicketDto sendTicketToOperatorToReview(Long authorId, Long tickerId);

    List<TicketDto> getTicketCreatedByUsersToCheckByOperator(Boolean sort,
                                                            Integer from,
                                                            Integer size);

    List<TicketDto> getTicketCreatedByUserToCheckByOperatorByUsername(Boolean sort,
                                                                      Integer from,
                                                                      Integer size,
                                                                      UserDto authorDto);

    TicketDto acceptUserTicket(Long authorId, Long ticketId);

    TicketDto rejectTicketByAuthorId(Long authorId, Long ticketId);
}
