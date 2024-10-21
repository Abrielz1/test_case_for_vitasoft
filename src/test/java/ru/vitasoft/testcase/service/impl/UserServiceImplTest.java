package ru.vitasoft.testcase.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.vitasoft.testcase.exception.exceptions.ObjectNotFoundException;
import ru.vitasoft.testcase.model.dto.responce.UserDto;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.model.enums.roles.RoleType;
import ru.vitasoft.testcase.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

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

        userDto = UserDto.builder()
                .username("vasyan")
                .email("a11@mail.com")
                .password("123")
                .roles(new HashSet<>(RoleType.ROLE_ADMIN.ordinal()))
                .build();
    }

    @Test
    void When_userServiceCalledGetUserCredentialList_ThenReceivedUserWithName_VasyaTest() {

        List<User> list = new ArrayList<>();
        list.add(user);

        when(userRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<User>(list));

        List<UserDto> userDtoList = this.userService.getAllUsersList(0, 5);

        assertEquals(1L, userDtoList.size(), "value: 1");
        assertEquals("vasyan", userDtoList.get(0).getUsername(), "value: vasyan");
        assertEquals(userDto, userDtoList.get(0), "value: UserDto(username=vasyan)");
    }

    @Test
    void When_getUserByUsername_WhenGetUserMyUsernameVasyanTest() {

        when(userRepository.getAuthorFromDbByUsername("vasyan"))
                .thenReturn(Optional.ofNullable(user));

        UserDto userDto1 = userService.getUserByUsername(userDto);

        assertEquals(userDto.getUsername(), userDto1.getUsername(), "value: vasyan");
    }

    @Test
    void When_getUserByUsernameWithWrongUsername_ThenGetsUserWithUsername_VasyanTest() {

        when(userRepository.getAuthorFromDbByUsername("vasyan"))
                .thenReturn(Optional.ofNullable(user));

        UserDto userDto1 = userService.getUserByUsername(userDto);

        assertEquals(userDto.getUsername(), userDto1.getUsername());
    }

    @Test
    void When_getUserWithIncorrectUserName_ThenThrowsObjectNotFoundExceptionTest(){

        assertThrows(ObjectNotFoundException.class, ()-> userService.getUserByUsername(userDto));
    }
}