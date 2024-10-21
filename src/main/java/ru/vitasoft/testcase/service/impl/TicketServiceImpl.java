package ru.vitasoft.testcase.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vitasoft.testcase.exception.exceptions.BadRequestException;
import ru.vitasoft.testcase.exception.exceptions.ObjectNotFoundException;
import ru.vitasoft.testcase.model.dto.responce.TicketDto;
import ru.vitasoft.testcase.model.dto.in.TicketNewDto;
import ru.vitasoft.testcase.model.dto.responce.UserDto;
import ru.vitasoft.testcase.model.entity.Ticket;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.model.enums.roles.RoleType;
import ru.vitasoft.testcase.model.enums.status.Status;
import ru.vitasoft.testcase.repository.TicketRepository;
import ru.vitasoft.testcase.repository.UserRepository;
import ru.vitasoft.testcase.service.TicketService;
import ru.vitasoft.testcase.model.mapper.TicketMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final UserRepository userRepository;

    private final TicketRepository ticketRepository;

    @Override
    public List<TicketDto> getTicketCreatedByUsersToCheckByOperator(Boolean sort,
                                                                   Integer from,
                                                                   Integer size) {

        PageRequest pageRequest = this.pageRequestCalculator(from, size);

        List<TicketDto> listDto = this.getTicketListAndManipulateIt(this.getListTicketsAndFilterBySENT(pageRequest));

        if (Boolean.TRUE.equals(sort)) {

             return listDto;
        }

        return listDto.stream()
                .sorted(Comparator.comparing(TicketDto::getCreated).reversed())
                .toList();
    }

    @Override
    public List<TicketDto> getTicketCreatedByUserToCheckByOperatorByUsername(Boolean sort,
                                                                             Integer from,
                                                                             Integer size,
                                                                             UserDto authorDto) {

        PageRequest pageRequest = this.pageRequestCalculator(from, size);

        User authorFromDb = this.getAuthorFromDbByUsername(authorDto.getUsername());

        List<TicketDto> listDto = this.getTicketCreatedByUserListAndManipulateIt(
                this.getListTicketCreatedByUsersAndFilterBySENT(authorFromDb, pageRequest)
        );


        if (Boolean.TRUE.equals(sort)) {

            return listDto;
        }

        return listDto.stream()
                .sorted(Comparator.comparing(TicketDto::getCreated).reversed())
                .toList();
    }

    @Override
    public TicketDto acceptUserTicket(Long authorId, Long ticketId) {

        User author = this.getUserById(authorId);
        Ticket ticket = this.getTicketByUserIdAndTicketId(author.getId(), ticketId);

        if (!ticket.getStatus().equals(Status.SEND)) {
            throw new BadRequestException("Ticket is not in the status: " + Status.SEND + " !");
        }

        ticket.setStatus(Status.ACCEPTED);

        return this.saveTicket(ticket);
    }

    @Override
    public TicketDto rejectTicketByAuthorId(Long authorId, Long ticketId) {

        User author = this.getUserById(authorId);
        Ticket ticket = this.getTicketByUserIdAndTicketId(author.getId(), ticketId);

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

        PageRequest pageRequest = this.pageRequestCalculator(from, size);

        if (Boolean.TRUE.equals(sort)) {

            return ticketRepository.getAllByAuthor_Id(authorId, pageRequest)
                    .stream()
                    .filter(ticket -> ticket.getStatus().equals(Status.SEND))
                    .sorted(Comparator.comparing(Ticket::getCreated).reversed())
                    .map(TicketMapper::toDto)
                    .toList();

        }

        return ticketRepository.getAllByAuthor_Id(authorId, pageRequest)
                .stream()
                .filter(ticket -> ticket.getStatus().equals(Status.SEND))
                .sorted(Comparator.comparing(Ticket::getCreated))
                .map(TicketMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public TicketDto creationTicketByUser(Long authorId,
                                          TicketNewDto newTicket) {

        User author = this.getUserById(authorId);

        List<RoleType> list = new ArrayList<>(author.getRoles());

        if (!list.get(0).equals(RoleType.ROLE_USER)) {
            throw new BadRequestException("Only User can Create Tickets!");
        }

        Ticket ticket = TicketMapper.toTicketFromNew(newTicket, author);

        return this.saveTicket(ticket);
    }

    @Override
    @Transactional
    public TicketDto editTicketByUser(Long authorId,
                                      Long tickerId,
                                      TicketNewDto ticketToUpdate) {

        User author = this.getUserById(authorId);
        Ticket ticketFromDb = this.getTicketByUserId(authorId, tickerId);

        if (!ticketToUpdate.getStatus().equals(Status.DRAFT)) {
            throw new BadRequestException("Status MUST be DAFT!");
        }

        ticketFromDb = this.updaterFields(ticketFromDb, ticketToUpdate, author);

        return this.saveTicket(ticketFromDb);
    }

    @Override
    @Transactional
    public TicketDto sendTicketToOperatorToReview(Long authorId, Long tickerId) {

        return this.getTicketByUserIdToSentToOperator(authorId, tickerId);
    }

    private TicketDto getTicketByUserIdToSentToOperator(Long authorId, Long tickerId) {

        Ticket ticketFromDb = ticketRepository.findByAuthorIdAndTicketId(authorId, tickerId).orElseThrow(()->
                new ObjectNotFoundException("No ticket To SEND"));

        ticketFromDb.setStatus(Status.SEND);
        ticketRepository.saveAndFlush(ticketFromDb);

        return TicketMapper.toDto(ticketFromDb);
    }

    private Ticket getTicketByUserId(Long authorId, Long tickerId) {

        return ticketRepository.findByAuthorIdAndTicketId(authorId, tickerId).orElseThrow(()->
                new ObjectNotFoundException("No ticket in Db"));
    }

    private User getUserById(Long userId) {

        return userRepository.findById(userId).orElseThrow(()->
                new ObjectNotFoundException("No user in Db"));
    }

    private TicketDto saveTicket(Ticket ticket) {

        return TicketMapper.toDto(ticketRepository.saveAndFlush(ticket));
    }

    private Ticket updaterFields(Ticket ticket,
                                 TicketNewDto ticketToUpdate, User author) {

        if (ticketToUpdate.getMessage() != null) {
            ticket.setMessage(ticketToUpdate.getMessage());
        }

        return TicketMapper.toDtoFromNew(ticketToUpdate, ticket, author);
    }

    private TicketDto textManipulator(TicketDto ticket) {

        ticket.setMessage(ticket.getMessage()
                .chars()
                .mapToObj(chars -> String.valueOf((char) chars))
                .collect(Collectors.joining("_")));

        return ticket;
    }

    private Ticket getTicketByUserIdAndTicketId(Long authorId, Long ticketId) {

        return ticketRepository.findByAuthorIdAndTicketId(authorId, ticketId)
                .orElseThrow(() -> new ObjectNotFoundException("No ticket to ACCEPT"));
    }

    private List<Ticket> getListTicketsAndFilterBySENT(PageRequest pageRequest) {

        return ticketRepository.findAll(pageRequest)
                .stream()
                .filter(ticket -> ticket.getStatus().equals(Status.SEND))
                .sorted(Comparator.comparing(Ticket::getCreated))
                .toList();
    }

    private List<TicketDto> getTicketListAndManipulateIt(List<Ticket> list) {

        return list.stream()
                .map(TicketMapper::toDto)
                .map(this::textManipulator)
                .toList();
    }

    private PageRequest pageRequestCalculator(Integer from,
                                              Integer size) {

        return PageRequest.of(from / size, size);
    }

    private List<TicketDto> getTicketCreatedByUserListAndManipulateIt(List<Ticket> listTicketCreatedByUsersAndFilterBySENT) {

        return listTicketCreatedByUsersAndFilterBySENT.stream()
                .map(TicketMapper::toDto)
                .map(this::textManipulator)
                .toList();
    }

    private List<Ticket> getListTicketCreatedByUsersAndFilterBySENT(User authorFromDb, PageRequest pageRequest) {

        return ticketRepository.findAllByAuthor_Id(authorFromDb.getId(), pageRequest);
    }
}
