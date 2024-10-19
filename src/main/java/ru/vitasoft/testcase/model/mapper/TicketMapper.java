package ru.vitasoft.testcase.model.mapper;

import ru.vitasoft.testcase.exception.exceptions.BadRequestException;
import ru.vitasoft.testcase.model.dto.TicketDto;
import ru.vitasoft.testcase.model.dto.TicketNewDto;
import ru.vitasoft.testcase.model.entity.Ticket;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.model.enums.status.Status;
import java.time.LocalDateTime;

public class TicketMapper {

    private TicketMapper() {
        throw new BadRequestException("Utility Class!");
    }

    public static TicketDto toDto(Ticket ticket) {

        new TicketDto();
        return TicketDto
                .builder()
                .message(ticket.getMessage())
                .created(ticket.getCreated())
                .status(ticket.getStatus())
                .author(ticket.getAuthor())
                .build();
    }

    public static Ticket toTicketFromNew(TicketNewDto newDtoTicket, User author) {

        new Ticket();
        return Ticket
                .builder()
                .message(newDtoTicket.getMessage())
                .created(LocalDateTime.now())
                .status(Status.DRAFT)
                .author(author)
                .build();
    }

    public static Ticket toDtoFromNew(TicketNewDto ticketToUpdate) {

        new Ticket();
        return Ticket
                .builder()
                .message(ticketToUpdate.getMessage())
                .status(ticketToUpdate.getStatus())
                .build();
    }
}
