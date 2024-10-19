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

    TicketDto sendTicketToOperatorToReview(Long userId);

    List<TicketDto> getTicketCreatedByUsersToCheckByOperator(Boolean sort,
                                                            Integer from,
                                                            Integer size);

    List<TicketDto> getTicketCreatedByUserToCheckByOperatorByUsername(Boolean sort,
                                                                      Integer from,
                                                                      Integer size,
                                                                      UserDto authorDto);

    TicketDto acceptUserTicket(Long authorId, TicketNewDto newTicket);

    TicketDto rejectTicketByAuthorId(Long authorId, TicketNewDto newTicket);
}
