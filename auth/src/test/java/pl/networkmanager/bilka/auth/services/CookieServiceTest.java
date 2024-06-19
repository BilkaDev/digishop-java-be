package pl.networkmanager.bilka.auth.services;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CookieServiceTest {
    private CookieService cookieService;

    @BeforeEach
    void setUp() {
        cookieService = new CookieService();
    }

    @Test
    void generate_cookie_should_return_cookie_with_given_name_value_and_expiry() {
        Cookie cookie = cookieService.generateCookie("test", "value", 3600);

        assertEquals("test", cookie.getName());
        assertEquals("value", cookie.getValue());
        assertEquals(3600, cookie.getMaxAge());
        assertTrue(cookie.isHttpOnly());
    }

    @Test
    void remove_cookie_should_return_null_when_no_cookie_matches_given_name() {
        Cookie[] cookies = new Cookie[]{new Cookie("test", "value")};

        assertNull(cookieService.removeCookie(cookies, "nonexistent"));
    }

    @Test
    void remove_cookie_should_return_cookie_with_zero_max_age_when_cookie_matches_given_name() {
        Cookie[] cookies = new Cookie[]{new Cookie("test", "value")};

        Cookie cookie = cookieService.removeCookie(cookies, "test");

        assertNotNull(cookie);
        assertEquals(0, cookie.getMaxAge());
    }
}