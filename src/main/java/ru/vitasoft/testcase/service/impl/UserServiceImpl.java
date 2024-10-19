package ru.vitasoft.testcase.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.vitasoft.testcase.exception.exceptions.BadRequestException;
import ru.vitasoft.testcase.exception.exceptions.ObjectNotFoundException;
import ru.vitasoft.testcase.model.dto.UserDto;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.model.mapper.UserMapper;
import ru.vitasoft.testcase.repository.UserRepository;
import ru.vitasoft.testcase.service.UserService;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsersList(Integer from, Integer size) {

        PageRequest page = PageRequest.of(from, from / size);

        return userRepository.findAll(page)
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
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

        return UserMapper.toDto(userRepository.findById(userId).orElseThrow(()->
                new ObjectNotFoundException("No user in Db")));
    }

    private User getAuthorFromDbByUsername(String username) {

        return userRepository.getAuthorFromDbByUsername(username).orElseThrow();
    }
}
