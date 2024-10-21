package ru.vitasoft.testcase.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.vitasoft.testcase.model.dto.responce.TicketDto;
import ru.vitasoft.testcase.model.entity.Ticket;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.model.enums.roles.RoleType;
import ru.vitasoft.testcase.model.mapper.TicketMapper;
import ru.vitasoft.testcase.repository.TicketRepository;
import ru.vitasoft.testcase.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private User userOperator;

    private User userAdmin;

    private User user;

    private Ticket ticket;

    private TicketDto ticketDto;

    @BeforeEach
    void setUp() {

        userAdmin = User.builder().build();

        user = User.builder().build();

        userOperator = User
                .builder()
                .username("petya")
                .email("a12@mail,com")
                .password("123")
                .roles(new HashSet<>(RoleType.ROLE_OPERATOR.ordinal()))
                .build();

        ticket = Ticket
                .builder()
                .id(1L)
                .message("Door Stuck!")
                .created(LocalDateTime.now())
                .author(user)
                .build();

        ticketDto = TicketMapper.toDto(ticket);
    }

    @Test
    public void getTicketCreatedByUsersToCheckByOperatorTest() {
        Integer from = 0;
        Integer size = 5;

        List<Ticket> ticketList = new ArrayList<>();

    }
}