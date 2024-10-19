package ru.vitasoft.testcase.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vitasoft.testcase.exception.exceptions.BadRequestException;
import ru.vitasoft.testcase.model.dto.TicketDto;
import ru.vitasoft.testcase.model.dto.TicketNewDto;
import ru.vitasoft.testcase.model.dto.UserDto;
import ru.vitasoft.testcase.model.entity.Ticket;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.model.enums.status.Status;
import ru.vitasoft.testcase.repository.TicketRepository;
import ru.vitasoft.testcase.repository.UserRepository;
import ru.vitasoft.testcase.service.TicketService;
import ru.vitasoft.testcase.model.mapper.TicketMapper;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final UserRepository userRepository;

    private final TicketRepository ticketRepository;

    @Override
    public List<TicketDto> getTicketCreatedByUsersToCheckByOperator(Boolean sort,
                                                                   Integer from,
                                                                   Integer size) {

        PageRequest pageRequest = PageRequest.of(from, from / size);

        if (Boolean.TRUE.equals(sort)) {

            return ticketRepository.findAll(pageRequest)
                    .stream()
                    .filter(ticket -> !ticket.getStatus().equals(Status.SEND))
                    .sorted(Comparator.comparing(Ticket::getCreated).reversed())
                    .map(TicketMapper::toDto)
                    .toList();
        }

        return ticketRepository.findAll(pageRequest)
                .stream()
                .filter(ticket -> !ticket.getStatus().equals(Status.SEND))
                .sorted(Comparator.comparing(Ticket::getCreated))
                .map(TicketMapper::toDto)
                .toList();
    }

    @Override
    public List<TicketDto> getTicketCreatedByUserToCheckByOperatorByUsername(Boolean sort,
                                                                             Integer from,
                                                                             Integer size,
                                                                             UserDto authorDto) {

        PageRequest pageRequest = PageRequest.of(from, from / size);

        User authorFromDb = this.getAuthorFromDbByUsername(authorDto.getUsername());

        if (Boolean.TRUE.equals(sort)) {

            return ticketRepository.findAllByAuthor_Id(authorFromDb.getId(), pageRequest)
                    .stream()
                    .filter(ticket -> !ticket.getStatus().equals(Status.SEND))
                    .sorted(Comparator.comparing(Ticket::getCreated).reversed())
                    .map(TicketMapper::toDto)
                    .toList();
        }

        return ticketRepository.findAllByAuthor_Id(authorFromDb.getId(), pageRequest)
                .stream()
                .filter(ticket -> !ticket.getStatus().equals(Status.SEND))
                .sorted(Comparator.comparing(Ticket::getCreated))
                .map(TicketMapper::toDto)
                .toList();
    }

    @Override
    public TicketDto acceptUserTicket(Long authorId, TicketNewDto newTicket) {

        User author = this.getUserById(authorId);
        Ticket ticket = TicketMapper.toTicketFromNew(newTicket, author);

        if (!ticket.getStatus().equals(Status.SEND)) {
            throw new BadRequestException("Ticket is not in the status: " + Status.SEND + " !");
        }

        ticket.setStatus(Status.ACCEPTED);

        return this.saveTicket(ticket);
    }

    @Override
    public TicketDto rejectTicketByAuthorId(Long authorId, TicketNewDto newTicket) {

        User author = this.getUserById(authorId);
        Ticket ticket = TicketMapper.toTicketFromNew(newTicket, author);

        if (!ticket.getStatus().equals(Status.SEND)) {
            throw new BadRequestException("Ticket is not in the status: " + Status.SEND + " !");
        }

        ticket.setStatus(Status.REJECTED);

        return this.saveTicket(ticket);
    }

    private User getAuthorFromDbByUsername(String username) {

       return userRepository.getAuthorFromDbByUsername(username).orElseThrow();
    }

    @Override
    public List<TicketDto> getTicketCreatedByUser(Long authorId,
                                                  Boolean sort,
                                                  Integer from,
                                                  Integer size) {

        PageRequest pageRequest = PageRequest.of(from, from / size);

        if (Boolean.TRUE.equals(sort)) {

            return ticketRepository.getAllByAuthor_Id(authorId, pageRequest)
                    .stream()
                    .filter(ticket -> !ticket.getStatus().equals(Status.SEND))
                    .sorted(Comparator.comparing(Ticket::getCreated).reversed())
                    .map(this::textManipulator)
                    .map(TicketMapper::toDto)
                    .toList();

        }

        return ticketRepository.getAllByAuthor_Id(authorId, pageRequest)
                .stream()
                .filter(ticket -> !ticket.getStatus().equals(Status.SEND))
                .sorted(Comparator.comparing(Ticket::getCreated))
                .map(this::textManipulator)
                .map(TicketMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public TicketDto creationTicketByUser(Long authorId,
                                          TicketNewDto newTicket) {

        User author = this.getUserById(authorId);
        Ticket ticket = TicketMapper.toTicketFromNew(newTicket, author);

        return this.saveTicket(ticket);
    }

    @Override
    @Transactional
    public TicketDto editTicketByUser(Long authorId,
                                      TicketNewDto ticketToUpdate) {

        this.getUserById(authorId);
        Ticket ticketFromDb = this.getTicketByUserId(authorId);

        return this.saveTicket(this.updaterFields(ticketFromDb, ticketToUpdate));
    }

    @Override
    @Transactional
    public TicketDto sendTicketToOperatorToReview(Long authorId) {

        return TicketMapper.toDto(this.getTicketByUserId(authorId));
    }

    private Ticket getTicketByUserId(Long authorId) {

        Ticket ticketFromDb = ticketRepository.findByAuthorId(authorId).orElseThrow();
        ticketFromDb.setStatus(Status.SEND);

        return ticketFromDb;
    }

    private User getUserById(Long userId) {

        return userRepository.findById(userId).orElseThrow();
    }

    private TicketDto saveTicket(Ticket ticket) {

        return TicketMapper.toDto(ticketRepository.saveAndFlush(ticket));
    }

    private Ticket updaterFields(Ticket ticket,
                                 TicketNewDto ticketToUpdate) {

        if (ticketToUpdate.getMessage() != null) {
            ticket.setMessage(ticketToUpdate.getMessage());
        }

        if (!ticketToUpdate.getStatus().equals(ticket.getStatus())) {
            ticketToUpdate.setStatus(ticket.getStatus());
        }

        return TicketMapper.toDtoFromNew(ticketToUpdate);
    }

    private Ticket textManipulator(Ticket ticket) {

        ticket.setMessage(ticket.getMessage()
                .chars()
                .mapToObj(chars -> String.valueOf((char) chars))
                .collect(Collectors.joining("_")));

        return ticket;
    }
}
