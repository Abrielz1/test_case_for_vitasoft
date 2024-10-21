package ru.vitasoft.testcase.security.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vitasoft.testcase.exception.exceptions.RefreshTokenException;
import ru.vitasoft.testcase.model.dto.in.LoginRequest;
import ru.vitasoft.testcase.model.dto.responce.AuthResponseDto;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.repository.UserRepository;
import ru.vitasoft.testcase.security.dto.in.RefreshTokenRequestDto;
import ru.vitasoft.testcase.security.dto.out.RefreshTokenResponseDto;
import ru.vitasoft.testcase.security.entity.RefreshToken;
import ru.vitasoft.testcase.security.jwt.util.JwtUtils;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponseDto authenticationUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.create(userDetails.getUserId());

        return new AuthResponseDto(
                userDetails.getUserId(),
                userDetails.getUsername(),
                refreshToken.getToken(),
                jwtUtils.generateJwtToken(userDetails),
                roles);

    }

    @Override
    public void logout() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            refreshTokenService.deleteByUserId(userDetails.getUserId());
        }
    }

    @Override
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto request) {
        String requestTokenRefresh = request.getRefreshToken();

        return refreshTokenService.getByRefreshToken(requestTokenRefresh)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getId)
                .map(userId -> {
                    User user = userRepository.findById(userId).orElseThrow(() ->
                            new RefreshTokenException("Exception for userId: " + userId));
                    String token = jwtUtils.generateTokenFromUserName(user.getUsername());

                    return new RefreshTokenResponseDto(token, refreshTokenService.create(userId).getToken());
                })
                .orElseThrow(() -> new RefreshTokenException("RefreshToken is not found!"));
    }
}
