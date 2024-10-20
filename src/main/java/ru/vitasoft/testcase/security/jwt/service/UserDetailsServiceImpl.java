package ru.vitasoft.testcase.security.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.vitasoft.testcase.exception.exceptions.ObjectNotFoundException;
import ru.vitasoft.testcase.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return new AppUserDetails(userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("No user with such name: %s !".formatted(username))));
    }
}
