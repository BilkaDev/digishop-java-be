package pl.networkmanager.bilka.auth.services;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
    jwtService = new JwtService("secret11111111111111111111111111111111111111111111111111111111111111111111111111");
    }

    @Test
    void validate_token_should_not_throw_exception_when_token_is_valid() {
        String token = jwtService.generateToken("username", 3600);
        assertDoesNotThrow(() -> jwtService.validateToken(token));
    }

    @Test
    void validate_token_should_throw_exception_when_token_is_invalid() {
        String token = "invalidToken";
        assertThrows(MalformedJwtException.class, () -> jwtService.validateToken(token));
    }

    @Test
    void validate_token_should_throw_exception_when_token_is_expired() {
        String token = jwtService.generateToken("username", -1);
        assertThrows(ExpiredJwtException.class, () -> jwtService.validateToken(token));
    }

    @Test
    void generate_token_should_return_different_tokens_for_different_usernames() {
        String token1 = jwtService.generateToken("username1", 3600);
        String token2 = jwtService.generateToken("username2", 3600);
        assertNotEquals(token1, token2);
    }

    @Test
    void get_subject_should_return_correct_username_when_token_is_valid() {
        String token = jwtService.generateToken("username", 3600);
        assertEquals("username", jwtService.getSubject(token));
    }

    @Test
    void get_subject_should_throw_exception_when_token_is_invalid() {
        String token = "invalidToken";
        assertThrows(MalformedJwtException.class, () -> jwtService.getSubject(token));
    }

    @Test
    void refresh_token_should_return_new_token_with_same_username() {
        String token = jwtService.generateToken("username", 3600);
        String refreshedToken = jwtService.refreshToken(token, 7200);
        assertEquals(jwtService.getSubject(token), jwtService.getSubject(refreshedToken));
    }

    @Test
    void refresh_token_should_throw_exception_when_token_is_invalid() {
        String token = "invalidToken";
        assertThrows(MalformedJwtException.class, () -> jwtService.refreshToken(token, 7200));
    }
}