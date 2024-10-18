package ru.vitasoft.testcase.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vitasoft.testcase.model.dto.TicketDto;
import ru.vitasoft.testcase.model.dto.TicketNewDto;
import ru.vitasoft.testcase.repository.TicketRepository;
import ru.vitasoft.testcase.repository.UserRepository;
import ru.vitasoft.testcase.service.UserService;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final TicketRepository ticketRepository;

    @Override
    public TicketDto getTokenCreatedByUser(Long userId, Boolean sort) {

        return null;
    }

    @Override
    @Transactional
    public TicketDto creationTicketByUser(Long userId, TicketNewDto newTicket) {

        return null;
    }

    @Override
    @Transactional
    public TicketDto editTicketByUser(Long userId, TicketNewDto newTicket) {

        return null;
    }

    @Override
    @Transactional
    public TicketDto sendTicketToOperatorToReview(Long userId) {

        return null;
    }
}
