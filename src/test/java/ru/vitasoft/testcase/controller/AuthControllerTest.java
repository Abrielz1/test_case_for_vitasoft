package ru.vitasoft.testcase.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import ru.vitasoft.testcase.model.entity.User;
import ru.vitasoft.testcase.model.enums.roles.RoleType;
import ru.vitasoft.testcase.security.jwt.service.AppUserDetails;
import ru.vitasoft.testcase.security.jwt.service.SecurityService;
import ru.vitasoft.testcase.security.jwt.service.UserDetailsServiceImpl;
import ru.vitasoft.testcase.security.jwt.util.JwtTokenFilter;
import ru.vitasoft.testcase.security.jwt.util.JwtUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AuthController.class)
class AuthControllerTest {

    @MockBean
    private JwtTokenFilter tokenFilter;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    AppUserDetails userDetails;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    private String validToken;

    @BeforeEach
    void setUp() {
        String payload = "{\"sub\":\"testuser@gmail.com\"}";
        String encodedPayload = Base64.getEncoder().encodeToString(payload.getBytes());
        String tokenHeader = Base64.getEncoder().encodeToString("{\"alg\":\"HS256\"}".getBytes());
        String signature = UUID.randomUUID().toString();
        validToken = tokenHeader + "." + encodedPayload + "." + signature;
    }

        @Test
    void testValidTokenFilter() throws ServletException, IOException {
        when(restTemplate.exchange(
                Mockito.eq("http://auth-service/api/v1/auth/validate"),
                Mockito.eq(HttpMethod.GET),
                any(),
                Mockito.eq(Boolean.class))
        ).thenReturn(ResponseEntity.ok(true));

        when(jwtUtils.getUsername(anyString())).thenReturn("testuser@gmail.com");
            AppUserDetails userDetails = new AppUserDetails(new User(1L,"", "", "", null));
        when(userDetailsService.loadUserByUsername("testuser@gmail.com")).thenReturn(userDetails);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + validToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = new MockFilterChain();
        tokenFilter.doFilter(request, response, filterChain);
        assertEquals("testuser@gmail.com", SecurityContextHolder.getContext().getAuthentication().getName());
    }


//    @Test
//    void loginUserToAccount() {
//    }
//
//    @Test
//    void registerUserOnServer() {
//    }
//
//    @Test
//    void refreshTokenAuthorizedUserSession() {
//    }
//
//    @Test
//    void logoutCurrentUserSession() {
//    }
}