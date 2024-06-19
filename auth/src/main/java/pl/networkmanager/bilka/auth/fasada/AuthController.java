package pl.networkmanager.bilka.auth.fasada;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.networkmanager.bilka.auth.entity.*;
import pl.networkmanager.bilka.auth.exceptions.UserDontExistException;
import pl.networkmanager.bilka.auth.exceptions.UserExistingWithMail;
import pl.networkmanager.bilka.auth.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegisterDTO user)
            throws UserDontExistException, UserExistingWithMail {
        log.info("--START REGISTER USER");
        userService.register(user);
        log.info("--END REGISTER USER");
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        log.info("--TRY LOGIN USER");
        return userService.login(response, user);
    }

    @RequestMapping(path = "/auto-login", method = RequestMethod.GET)
    public ResponseEntity<?> autoLogin(HttpServletRequest request, HttpServletResponse response) {
        log.info("--TRY AUTO LOGIN USER");
        return userService.loginByToken(request, response);
    }

    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public ResponseEntity<AuthResponse> validateToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("--START VALIDATE TOKEN");
            userService.validateToken(request, response);
            log.info("--END VALIDATE TOKEN");
            return ResponseEntity.ok(new AuthResponse(Code.PERMIT));

        } catch (IllegalArgumentException | ExpiredJwtException e) {
            log.info("Token is invalid or expired");
            return ResponseEntity.status(401).body(new AuthResponse(Code.A3));
        }
    }

    @RequestMapping(path = "/logged-in", method = RequestMethod.GET)
    public ResponseEntity<LoginResponse> loggedIn(HttpServletRequest request, HttpServletResponse response) {
        log.info("--CHECK IF USER IS LOGGED IN");
        return userService.loggedIn(request, response);
    }

    @RequestMapping(path = "/activate", method = RequestMethod.GET)
    public ResponseEntity<AuthResponse> activate(@RequestParam String uid)
            throws UserDontExistException {
        log.info("--START ACTIVATE USER");
        userService.activateUser(uid);
        log.info("--END ACTIVATE USER");
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }

    @RequestMapping(path = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> sendMailRecovery(@RequestBody ResetPasswordDTO resetPasswordDTO)
            throws UserDontExistException {
        log.info("--START SEND MAIL RECOVERY");
        userService.recoverPassword(resetPasswordDTO.email());
        log.info("--END SEND MAIL RECOVERY");
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }

    @RequestMapping(path = "/reset-password", method = RequestMethod.PATCH)
    public ResponseEntity<AuthResponse> recoveryMail(@RequestBody ChangePasswordData changePasswordData)
            throws UserDontExistException {
        log.info("--START RECOVERY MAIL");
        userService.resetPassword(changePasswordData);
        log.info("--END RECOVERY MAIL");
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest request) {
        log.info("--LOGOUT USER");
        return userService.logout(request, response);
    }
}
