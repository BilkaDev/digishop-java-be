package pl.networkmanager.bilka.features;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import pl.networkmanager.bilka.BaseIntegrationTest;
import pl.networkmanager.bilka.auth.configuration.EmailConfiguration;
import pl.networkmanager.bilka.auth.entity.User;
import pl.networkmanager.bilka.auth.repository.ResetOperationsRepository;
import pl.networkmanager.bilka.auth.repository.UserRepository;
import pl.networkmanager.bilka.auth.services.JwtService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TypicalScenarioAuthorizationUserIntegrationTest extends BaseIntegrationTest {
    @MockBean
    private EmailConfiguration emailConfiguration;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ResetOperationsRepository resetOperationsRepository;


    @Test
    public void user_wants_to_get_log_in() throws Exception {

        // step 1: user tried to validate token by requesting GET /validate and system returned UNAUTHORIZED(401)
        // given && when
        ResultActions failedValidateRequest = mockMvc.perform(get("/api/v1/auth/validate").contentType(MediaType.APPLICATION_JSON));
        // then
        failedValidateRequest.andExpect(status().isUnauthorized()).andExpect(content().json("""
                {
                    "code":"A3"
                }""".trim()));
        // step 2: user tried to log in by requesting POST /login with username=someUser, password=somePassword and system returned unauthorized(401)
        // given && when
        ResultActions failedLoginRequest = mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"login\":\"someUser\",\"password\":\"somePassword\"}"));
        // then
        failedLoginRequest.andExpect(status().isUnauthorized()).andExpect(content().json("""
                {
                    "code":"A2"
                }""".trim()));

        // step 3: register user by requesting POST /register with username=someUser, password=somePassword, email=someEmail and system returned OK(200)
        // given && when
        ResultActions registerAction = mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"login\":\"someUser\",\"password\":\"somePassword\",\"email\": \"some@email.com\"}"));
        // then
        registerAction.andExpect(status().isOk()).andExpect(content().json("""
                {
                    "code":"SUCCESS"
                }""".trim()));
        // step 3.1: register exist user by requesting POST /register with username=someUser, password=somePassword, email=someEmail and system returned bad request(400)
        // given && when
        ResultActions registerExistUserAction = mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"login\":\"someUser\",\"password\":\"somePassword\",\"email\": \"some@email.com\"}"));
        // then
        registerExistUserAction.andExpect(status().isBadRequest()).andExpect(content().json("""
                {
                    "code":"A4"
                }""".trim()));
        // step 4: user tried to log in without activate account by requesting POST /login with username=someUser, password=somePassword and system returned unauthorized(401)
        // given && when
        ResultActions loginWithoutActiveAccountAction = mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"login\":\"someUser\",\"password\":\"somePassword\"}"));
        // then
        loginWithoutActiveAccountAction.andExpect(status().isUnauthorized()).andExpect(content().json("""
                {
                    "code":"A2"
                }""".trim()));

        // step 5: user tried to active account by requesting GET /activate with uid=someUid and system returned OK(200)
        // given && when
        User user = userRepository.findUserByLogin("someUser").orElseThrow();
        ResultActions activateAction = mockMvc.perform(get("/api/v1/auth/activate").param("uid", user.getUuid()));
        // then
        activateAction.andExpect(status().isOk()).andExpect(content().json("""
                {
                    "code":"SUCCESS"
                }""".trim()));
        // step 6: user tried to log in  account by requesting POST /login with username=someUser, password=somePassword and system returned OK(200)
        // given && whenuse1 = {User@16798}
        ResultActions loginAction = mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"login\":\"someUser\",\"password\":\"somePassword\"}"));
        // then
        loginAction.andExpect(status().isOk()).andExpect(content().json("""
                {
                    "login":"someUser",
                    "email":"some@email.com",
                    "role":"USER"
                }""".trim()));

        Cookie authCookie = loginAction.andReturn().getResponse().getCookie("Authorization");
        Cookie refreshTokenCookie = loginAction.andReturn().getResponse().getCookie("refresh");


        // step 5: user tried to log in by requesting GET /auto-login and system returned OK(200)
        // given && when
        ResultActions autoLoginAction = mockMvc.perform(get("/api/v1/auth/auto-login").cookie(authCookie).cookie(refreshTokenCookie));
        // then
        autoLoginAction.andExpect(status().isOk()).andExpect(content().json("""
                {
                    "login":"someUser",
                    "email":"some@email.com",
                    "role":"USER"
                }""".trim()));

        // step 5.1: user tried to get log in without token by requesting GET /auto-login and system returned status(401)
        // given && when
        ResultActions autoLoginWithoutTokenAction = mockMvc.perform(get("/api/v1/auth/auto-login"));
        // then
        autoLoginWithoutTokenAction.andExpect(status().isUnauthorized()).andExpect(content().json("""
                {
                    "code":"A3"
                }""".trim()));
        // step 5.2: user tried to get log in with expired token by requesting GET /auto-login and system returned status(401)
        // given && when
        var token = jwtService.generateToken("someUser", -1);
        Cookie expiredRefreshTokenCookie = new Cookie("refresh", token);
        expiredRefreshTokenCookie.setHttpOnly(true);
        expiredRefreshTokenCookie.setPath("/");
        expiredRefreshTokenCookie.setMaxAge(10000);
        ResultActions autoLoginWithExpiredTokenAction = mockMvc.perform(get("/api/v1/auth/auto-login").cookie(authCookie).cookie(expiredRefreshTokenCookie));
        // then
        autoLoginWithExpiredTokenAction.andExpect(status().isUnauthorized()).andExpect(content().json("""
                {
                    "code":"A3"
                }""".trim()));
        // step 6: user tried to validate token by requesting GET /validate and system returned OK(200)
        // given && when
        ResultActions validateAction = mockMvc.perform(get("/api/v1/auth/validate").cookie(authCookie).cookie(refreshTokenCookie));
        // then
        validateAction.andExpect(status().isOk()).andExpect(content().json("""
                {
                    "code":"PERMIT"
                }""".trim()));
        // step 6.1 user tried to validate token with expired token by requesting GET /validate and system returned UNAUTHORIZED(401)
        // given && when
        Cookie expiredAuthTokenCookie = new Cookie("refresh", token);
        expiredAuthTokenCookie.setHttpOnly(true);
        expiredAuthTokenCookie.setPath("/");
        expiredAuthTokenCookie.setMaxAge(10000);
        ResultActions validateWithExpiredTokenAction = mockMvc.perform(get("/api/v1/auth/validate").cookie(expiredAuthTokenCookie).cookie(expiredRefreshTokenCookie));
        // then
        validateWithExpiredTokenAction.andExpect(status().isUnauthorized()).andExpect(content().json("""
                {
                    "code":"A3"
                }""".trim()));
        // step 7: user tried to get logged in by requesting GET /logged-in and system returned OK(200)
        // given && when
        ResultActions loggedInAction = mockMvc.perform(get("/api/v1/auth/logged-in").cookie(authCookie).cookie(refreshTokenCookie));
        // then
        loggedInAction.andExpect(status().isOk()).andExpect(content().json("""
                {
                    "access":true,
                    "code":"SUCCESS"
                }""".trim()));
        // step 7.1: user tried to get logged in without token by requesting GET /logged-in and system returned ok(200)
        // given && when
        ResultActions loggedInWithoutTokenAction = mockMvc.perform(get("/api/v1/auth/logged-in"));
        // then
        loggedInWithoutTokenAction.andExpect(status().isOk()).andExpect(content().json("""
                {
                    "access":false,
                    "code":"SUCCESS"
                }""".trim()));
        // step 7.2: user tried to get logged in with expired token by requesting GET /logged-in and system returned ok(200)
        // given && when
        ResultActions loggedInWithExpiredTokenAction = mockMvc.perform(get("/api/v1/auth/logged-in").cookie(expiredAuthTokenCookie).cookie(expiredRefreshTokenCookie));
        // then
        loggedInWithExpiredTokenAction.andExpect(status().isOk()).andExpect(content().json("""
                {
                    "access":false,
                    "code":"SUCCESS"
                }""".trim()));

        // step 8: user tried to log out by requesting POST /logout and system returned OK(200)
        // given && when
        ResultActions logoutAction = mockMvc.perform(get("/api/v1/auth/logout").cookie(authCookie).cookie(refreshTokenCookie));
        // then
        logoutAction.andExpect(status().isOk()).andExpect(content().json("""
                {
                    "code":"SUCCESS"
                }""".trim()));

        // step 9: user tried to recover password by requesting POST /reset-password with email=some@email.pl and system returned OK(200)
        // given && when
        ResultActions recoverPasswordAction = mockMvc.perform(post("/api/v1/auth/reset-password").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"some@email.com\"}"));
        // then
        recoverPasswordAction.andExpect(status().isOk()).andExpect(content().json("""
                {
                    "code":"SUCCESS"
                }""".trim()));

        // step 10: user tried to reset password by requesting PATCH /reset-password with reset operations uid and system returned OK(200)
        // given && when
        var resetOperation = resetOperationsRepository.findAll().getFirst();
        ResultActions resetPasswordAction = mockMvc.perform(patch("/api/v1/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"uid\":\"" + resetOperation.getUid() + "\",\"password\":\"newPassword\"}"));
        // then
        resetPasswordAction.andExpect(status().isOk()).andExpect(content().json("""
                {
                    "code":"SUCCESS"
                }""".trim()));

    }
}
