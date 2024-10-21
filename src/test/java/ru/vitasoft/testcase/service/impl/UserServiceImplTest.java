package ru.vitasoft.testcase.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.vitasoft.testcase.model.dto.responce.UserDto;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.model.enums.roles.RoleType;
import ru.vitasoft.testcase.model.mapper.UserMapper;
import ru.vitasoft.testcase.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("vasyan")
                .email("a11@mail.com")
                .password("123")
                .roles(new HashSet<>(RoleType.ROLE_ADMIN.ordinal()))
                .build();

        userDto = UserMapper.toDto(user);
    }

    @Test
    void whenUserServiceCalledGetUserCredentialList_ThenReceivedUserWithNameVasyan() {

        List<User> list = new ArrayList<>();
        list.add(user);

        when(repository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<User>(list));

        List<UserDto> userDtoList = this.userService.getAllUsersList(0, 5);

        assertEquals(1L, userDtoList.size(), "value: 1");
        assertEquals("vasyan", userDtoList.get(0).getUsername(), "value: vasyan");
        assertEquals(userDto, userDtoList.get(0), "value: UserDto(username=vasyan)");
    }
}