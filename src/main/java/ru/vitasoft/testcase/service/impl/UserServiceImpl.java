package ru.vitasoft.testcase.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.vitasoft.testcase.exception.exceptions.BadRequestException;
import ru.vitasoft.testcase.exception.exceptions.ObjectNotFoundException;
import ru.vitasoft.testcase.model.dto.responce.UserDto;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.model.enums.roles.RoleType;
import ru.vitasoft.testcase.model.mapper.UserMapper;
import ru.vitasoft.testcase.repository.UserRepository;
import ru.vitasoft.testcase.service.UserService;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsersList(Integer from, Integer size) {

        PageRequest page = PageRequest.of(from / size, size);

        return userRepository.findAll(page)
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto getUserByUsername(UserDto userDto) {

        if (userDto.getUsername().isBlank()) {
            throw new BadRequestException("No username fond in UserDto!");
        }

        return UserMapper.toDto(this.getAuthorFromDbByUsername(userDto.getUsername()));
    }

    @Override
    public UserDto approveUserToOperatorRole(Long userId) {

        User operator = userRepository.findById(userId).orElseThrow(()->
                new ObjectNotFoundException("No user in Db"));

        operator.setRoles(null);
        operator.setRoles(new HashSet<>(List.of(RoleType.ROLE_OPERATOR)));

        return UserMapper.toDto(operator);
    }


    private User getAuthorFromDbByUsername(String username) {

        return userRepository.getAuthorFromDbByUsername(username).orElseThrow();
    }
}
