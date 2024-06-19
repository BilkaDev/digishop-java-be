package pl.networkmanager.bilka.auth.services;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.networkmanager.bilka.auth.entity.*;
import pl.networkmanager.bilka.auth.exceptions.UserExistingWithLogin;
import pl.networkmanager.bilka.auth.exceptions.UserExistingWithMail;
import pl.networkmanager.bilka.auth.repository.UserRepository;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CookieService cookieService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        Field refreshExpField = UserService.class.getDeclaredField("REFRESH_EXP");
        refreshExpField.setAccessible(true);
        refreshExpField.setInt(userService, 1200);
        Field expField = UserService.class.getDeclaredField("EXP");
        expField.setAccessible(true);
        expField.setInt(userService, 600);
    }

    @Test
    void test_register_user() throws UserExistingWithLogin, UserExistingWithMail {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("login", "email@example.com", "password", Role.USER);

        userService.register(userRegisterDTO);

        verify(userRepository, times(1)).findUserByLogin("login");
        verify(userRepository, times(1)).findUserByEmail("email@example.com");
        verify(userRepository, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    void test_register_user_with_existing_login() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("login", "password", "email@example.com", null);
        when(userRepository.findUserByLogin(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(UserExistingWithLogin.class, () -> userService.register(userRegisterDTO));
    }

    @Test
    void test_register_user_with_existing_email() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("login", "password", "email@example.com", null);
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(UserExistingWithMail.class, () -> userService.register(userRegisterDTO));
    }

    @Test
    void test_login_successful() {
        User authRequest = new User();
        authRequest.setLogin("login");
        authRequest.setPassword("password");
        User user = new User();
        user.setLogin("login");
        user.setEmail("email@example.com");
        user.setRole(Role.USER);

        when(userRepository.findUserByLogin(anyString())).thenReturn(Optional.of(user));
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(anyString(), anyInt())).thenReturn("token");
        when(cookieService.generateCookie(anyString(), anyString(), anyInt())).thenReturn(new Cookie("name", "value"));

        ResponseEntity<?> responseEntity = userService.login(mock(HttpServletResponse.class), authRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(UserRegisterDTO.class, responseEntity.getBody());
    }

    @Test
    void test_login_unsuccessful() {
        User authRequest = new User();
        authRequest.setLogin("login");
        authRequest.setPassword("password");

        when(userRepository.findUserByLogin(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = userService.login(mock(HttpServletResponse.class), authRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertInstanceOf(AuthResponse.class, responseEntity.getBody());
        assertEquals(Code.A2, ((AuthResponse) responseEntity.getBody()).getCode());
    }

    @Test
    void test_validate_token() throws ExpiredJwtException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Cookie authCookie = new Cookie("Authorization", "authToken");
        Cookie refreshCookie = new Cookie("refresh", "refreshToken");

        when(request.getCookies()).thenReturn(new Cookie[]{authCookie, refreshCookie});
        doThrow(new ExpiredJwtException(null, null, "expired")).when(jwtService).validateToken("authToken");
        doNothing().when(jwtService).validateToken("refreshToken");
        when(jwtService.refreshToken(anyString(), anyInt())).thenReturn("newToken");
        when(cookieService.generateCookie(anyString(), anyString(), anyInt())).thenReturn(new Cookie("name", "value"));

        userService.validateToken(request, response);

        verify(jwtService).validateToken("authToken");
        verify(jwtService).validateToken("refreshToken");
        verify(jwtService, times(2)).refreshToken(anyString(), anyInt());
        verify(cookieService, times(2)).generateCookie(anyString(), anyString(), anyInt());
        verify(response, times(2)).addCookie(any(Cookie.class));
    }
}
