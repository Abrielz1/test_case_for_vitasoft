package ru.vitasoft.testcase.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.vitasoft.testcase.model.dto.in.TicketNewDto;
import ru.vitasoft.testcase.model.dto.responce.TicketDto;
import ru.vitasoft.testcase.model.dto.responce.UserDto;
import ru.vitasoft.testcase.model.entity.Ticket;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.model.enums.roles.RoleType;
import ru.vitasoft.testcase.model.enums.status.Status;
import ru.vitasoft.testcase.model.mapper.TicketMapper;
import ru.vitasoft.testcase.repository.TicketRepository;
import ru.vitasoft.testcase.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;


    private User user;

    private Ticket ticket0;

    private Ticket ticket1;

    private TicketDto ticketDto;

    private TicketDto ticketDto1;

    @BeforeEach
    void setUp() {

        user = User
                   .builder()
                   .id(3L)
                   .email("a11@mail.com")
                   .username("senya")
                   .password("123")
                   .roles(new HashSet<>(RoleType.ROLE_USER.ordinal()))
                   .build();

        ticket0 = Ticket
                .builder()
                .id(1L)
                .message("Door Stuck!")
                .created(LocalDateTime.now())
                .status(Status.SEND)
                .author(user)
                .build();

        ticket1 = Ticket
                .builder()
                .id(2L)
                .message("Window got broken!")
                .created(LocalDateTime.now())
                .status(Status.SEND)
                .author(user)
                .build();

        ticketRepository.saveAndFlush(ticket0);
        ticketRepository.saveAndFlush(ticket1);

        ticketDto = TicketMapper.toDto(ticket0);

        ticketDto1 = TicketMapper.toDto(ticket1);
    }

    @Test
    void getTicketCreatedByUsersToCheckByOperatorAversTest() {

        Integer from = 0;
        Integer size = 5;

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket0);
        ticketList.add(ticket1);

        when(ticketRepository.findAll(any(PageRequest.class)))
                            .thenReturn(new PageImpl<Ticket>(ticketList));

        List<TicketDto> list = ticketService.getTicketCreatedByUsersToCheckByOperator(true, from, size);

        assertEquals(2L, list.size(), "value: 2");
        assertEquals("D_o_o_r_ _S_t_u_c_k_!", list.get(0).getMessage(), "value: D_o_o_r_ _S_t_u_c_k_!");
        assertEquals("W_i_n_d_o_w_ _g_o_t_ _b_r_o_k_e_n_!", list.get(1).getMessage(), "value: W_i_n_d_o_w_ _g_o_t_ _b_r_o_k_e_n_!");
        assertEquals(ticketDto.getAuthor().getUsername(), list.get(0).getAuthor().getUsername(), "value: senya");
        assertEquals(ticketDto1.getAuthor().getUsername(), list.get(1).getAuthor().getUsername(), "value: senya");

        assertEquals(ticketDto1.getAuthor(), list.get(0).getAuthor(),  "right values for user credentials: UserDto(" +
                                                                                                        "eamil=a13@mail,com" +
                                                                                                        "username=vasya," +
                                                                                                        "password=123" +
                                                                                                        "ROLE_ADMIN");

        assertEquals(ticketDto1.getAuthor(), list.get(1).getAuthor(), "right values for user credentials: UserDto(" +
                                                                                                        "eamil=a13@mail,com" +
                                                                                                        "username=vasya," +
                                                                                                        "password=123" +
                                                                                                        "ROLE_ADMIN");
    }

    @Test
    void getTicketCreatedByUsersToCheckByOperatorReversTest() {

        Integer from = 0;
        Integer size = 5;

        ticketDto.setMessage("D_o_o_r_ _S_t_u_c_k_!");
        ticketDto1.setMessage("W_i_n_d_o_w_ _g_o_t_ _b_r_o_k_e_n_!");
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket0);
        ticketList.add(ticket1);

        when(ticketRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<Ticket>(ticketList));

        List<TicketDto> list = ticketService.getTicketCreatedByUsersToCheckByOperator(false, from, size);

        assertEquals(2L, list.size(), "value: 2");
        assertEquals(ticketDto1.getMessage(), list.get(1).getMessage(), "value: W_i_n_d_o_w_ _g_o_t_ _b_r_o_k_e_n_!");
        assertEquals(ticketDto.getMessage(), list.get(0).getMessage(), "value: D_o_o_r_ _S_t_u_c_k_!");
        assertEquals(ticketDto1.getAuthor().getUsername(), list.get(1).getAuthor().getUsername(), "value: senya");
        assertEquals(ticketDto.getAuthor().getUsername(), list.get(0).getAuthor().getUsername(), "value: senya");
        assertEquals(ticketDto1.getAuthor(), list.get(0).getAuthor(),  "right values for user credentials: UserDto(" +
                                                                                                        "eamil=a13@mail,com" +
                                                                                                        "username=vasya," +
                                                                                                        "password=123" +
                                                                                                        "ROLE_ADMIN");

        assertEquals(ticketDto1.getAuthor(), list.get(1).getAuthor(), "right values for user credentials: UserDto(" +
                                                                                                        "eamil=a13@mail,com" +
                                                                                                        "username=vasya," +
                                                                                                        "password=123" +
                                                                                                        "ROLE_ADMIN");
    }

    @Test
    void getTicketCreatedByUserToCheckByOperatorByUsernameAversTest() {

        Integer from = 0;
        Integer size = 5;

        ticketDto.setMessage("D_o_o_r_ _S_t_u_c_k_!");
        ticketDto1.setMessage("W_i_n_d_o_w_ _g_o_t_ _b_r_o_k_e_n_!");
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket0);
        ticketList.add(ticket1);

        PageRequest p = PageRequest.of(from / size, size);

        when(userRepository.getAuthorFromDbByUsername(user.getUsername()))
                .thenReturn(Optional.ofNullable(user));

        when(ticketRepository.findAllByAuthor_Username(user.getUsername(), p))
                .thenReturn((ticketList));

        UserDto userDto = UserDto
                .builder()
                .email("a11@mail.com")
                .username("senya")
                .password("123")
                .roles(new HashSet<>(RoleType.ROLE_USER.ordinal()))
                .build();

        List<TicketDto> list = ticketService.getTicketCreatedByUserToCheckByOperatorByUsername(true, from, size, userDto);

        assertEquals(2L, list.size(), "value: 2");
        assertEquals(ticketDto.getMessage(), list.get(0).getMessage(), "value: D_o_o_r_ _S_t_u_c_k_!");
        assertEquals(ticketDto1.getMessage(), list.get(1).getMessage(), "value: W_i_n_d_o_w_ _g_o_t_ _b_r_o_k_e_n_!");
        assertEquals(ticketDto.getAuthor().getUsername(), list.get(0).getAuthor().getUsername(), "value: senya");
        assertEquals(ticketDto1.getAuthor().getUsername(), list.get(1).getAuthor().getUsername(), "value: senya");

        assertEquals(ticketDto1.getAuthor(), list.get(0).getAuthor(),  "right values for user credentials: UserDto(" +
                "eamil=a13@mail,com" +
                "username=vasya," +
                "password=123" +
                "ROLE_ADMIN");

        assertEquals(ticketDto1.getAuthor(), list.get(1).getAuthor(), "right values for user credentials: UserDto(" +
                "eamil=a13@mail,com" +
                "username=vasya," +
                "password=123" +
                "ROLE_ADMIN");
    }

    @Test
    void getTicketCreatedByUserToCheckByOperatorByUsernameReversTest() {

        Integer from = 0;
        Integer size = 5;

        ticketDto.setMessage("D_o_o_r_ _S_t_u_c_k_!");
        ticketDto1.setMessage("W_i_n_d_o_w_ _g_o_t_ _b_r_o_k_e_n_!");
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket0);
        ticketList.add(ticket1);

        PageRequest p = PageRequest.of(from / size, size);

        when(userRepository.getAuthorFromDbByUsername(user.getUsername()))
                .thenReturn(Optional.ofNullable(user));

        when(ticketRepository.findAllByAuthor_Username(user.getUsername(), p))
                .thenReturn((ticketList));

        UserDto userDto = UserDto
                .builder()
                .email("a11@mail.com")
                .username("senya")
                .password("123")
                .roles(new HashSet<>(RoleType.ROLE_USER.ordinal()))
                .build();

        List<TicketDto> list = ticketService.getTicketCreatedByUserToCheckByOperatorByUsername(false, from, size, userDto);

        assertEquals(2L, list.size(), "value: 2");
        assertEquals(ticketDto1.getMessage(), list.get(1).getMessage(), "value: W_i_n_d_o_w_ _g_o_t_ _b_r_o_k_e_n_!");
        assertEquals(ticketDto.getMessage(), list.get(0).getMessage(), "value: D_o_o_r_ _S_t_u_c_k_!");
        assertEquals(ticketDto1.getAuthor().getUsername(), list.get(1).getAuthor().getUsername(), "value: senya");
        assertEquals(ticketDto.getAuthor().getUsername(), list.get(0).getAuthor().getUsername(), "value: senya");
        assertEquals(ticketDto1.getAuthor(), list.get(0).getAuthor(),  "right values for user credentials: UserDto(" +
                "eamil=a13@mail,com" +
                "username=vasya," +
                "password=123" +
                "ROLE_ADMIN");

        assertEquals(ticketDto1.getAuthor(), list.get(1).getAuthor(), "right values for user credentials: UserDto(" +
                "eamil=a13@mail,com" +
                "username=vasya," +
                "password=123" +
                "ROLE_ADMIN");
    }

    @Test
    void getTicketCreatedByUserToCheckByUserByUserIdAversTest() {

        Integer from = 0;
        Integer size = 5;

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket0);
        ticketList.add(ticket1);

        when(ticketRepository.getAllByAuthor_Id(anyLong(), any(PageRequest.class)))
                .thenReturn((ticketList));

        List<TicketDto> list = ticketService.getTicketCreatedByUser(user.getId(), true, from, size);

        assertEquals(2L, list.size(), "value: 2");
        assertEquals(ticketDto1.getMessage(), list.get(1).getMessage(), "value: W_i_n_d_o_w_ _g_o_t_ _b_r_o_k_e_n_!");
        assertEquals(ticketDto.getMessage(), list.get(0).getMessage(), "value: D_o_o_r_ _S_t_u_c_k_!");
        assertEquals(ticketDto1.getAuthor().getUsername(), list.get(1).getAuthor().getUsername(), "value: senya");
        assertEquals(ticketDto.getAuthor().getUsername(), list.get(0).getAuthor().getUsername(), "value: senya");
        assertEquals(ticketDto1.getAuthor(), list.get(0).getAuthor(),  "right values for user credentials: UserDto(" +
                "eamil=a13@mail,com" +
                "username=vasya," +
                "password=123" +
                "ROLE_ADMIN");

        assertEquals(ticketDto1.getAuthor(), list.get(1).getAuthor(), "right values for user credentials: UserDto(" +
                "eamil=a13@mail,com" +
                "username=vasya," +
                "password=123" +
                "ROLE_ADMIN");
    }

    @Test
    void getTicketCreatedByUserToCheckByUserByUserIdReversTest() {

        Integer from = 0;
        Integer size = 5;

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket0);
        ticketList.add(ticket1);

        when(ticketRepository.getAllByAuthor_Id(anyLong(), any(PageRequest.class)))
                .thenReturn((ticketList));

        List<TicketDto> list = ticketService.getTicketCreatedByUser(user.getId(), false, from, size);

        assertEquals(2L, list.size(), "value: 2");
        assertEquals(ticketDto1.getMessage(), list.get(1).getMessage(), "value: W_i_n_d_o_w_ _g_o_t_ _b_r_o_k_e_n_!");
        assertEquals(ticketDto.getMessage(), list.get(0).getMessage(), "value: D_o_o_r_ _S_t_u_c_k_!");
        assertEquals(ticketDto1.getAuthor().getUsername(), list.get(0).getAuthor().getUsername(), "value: senya");
        assertEquals(ticketDto.getAuthor().getUsername(), list.get(1).getAuthor().getUsername(), "value: senya");
        assertEquals(ticketDto1.getAuthor(), list.get(0).getAuthor(),  "right values for user credentials: UserDto(" +
                "eamil=a13@mail,com" +
                "username=vasya," +
                "password=123" +
                "ROLE_ADMIN");

        assertEquals(ticketDto1.getAuthor(), list.get(1).getAuthor(), "right values for user credentials: UserDto(" +
                "eamil=a13@mail,com" +
                "username=vasya," +
                "password=123" +
                "ROLE_ADMIN");
    }

    @Test
    void editTicketByUserTest() {

        when(userRepository.findById(3L))
                .thenReturn(Optional.ofNullable(user));

        when(ticketRepository.findByAuthorIdAndTicketId(3L, 1L))
                .thenReturn(Optional.ofNullable(ticket0));

        when(ticketRepository.saveAndFlush(ticket0)).thenReturn(ticket0);

        ticket0.setStatus(Status.DRAFT);

        TicketNewDto newDto = TicketNewDto
                .builder()
                .message("New-Message!")
                .status(Status.DRAFT)
                .build();

        TicketDto ticketDtoFromDb = ticketService.editTicketByUser(3L, 1L, newDto);

        assertEquals("New-Message!", ticketDtoFromDb.getMessage());
        assertEquals(user, ticketDtoFromDb.getAuthor());
    }

    @Test
    void acceptUserTicketTest() {

        when(userRepository.findById(3L))
                .thenReturn(Optional.ofNullable(user));

        when(ticketRepository.findByAuthorIdAndTicketId(3L, 1L))
                .thenReturn(Optional.ofNullable(ticket0));

        when(ticketRepository.saveAndFlush(ticket0)).thenReturn(ticket0);

        ticket0.setStatus(Status.SEND);

        TicketDto ticketDtoFromDb = ticketService.acceptUserTicket(3l, 1L);

        assertEquals(Status.ACCEPTED, ticketDtoFromDb.getStatus());
    }

    @Test
    void rejectTicketByAuthorIdTest() {

        when(userRepository.findById(3L))
                .thenReturn(Optional.ofNullable(user));

        when(ticketRepository.findByAuthorIdAndTicketId(3L, 1L))
                .thenReturn(Optional.ofNullable(ticket0));

        when(ticketRepository.saveAndFlush(ticket0)).thenReturn(ticket0);

        ticket0.setStatus(Status.SEND);

        TicketDto ticketDtoFromDb = ticketService.rejectTicketByAuthorId(3l, 1L);

        assertEquals(Status.REJECTED, ticketDtoFromDb.getStatus());
    }
}