package pl.networkmanager.bilka.auth.fasada;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.networkmanager.bilka.auth.entity.*;
import pl.networkmanager.bilka.auth.exceptions.UserDontExistException;
import pl.networkmanager.bilka.auth.exceptions.UserExistingWithLogin;
import pl.networkmanager.bilka.auth.exceptions.UserExistingWithMail;
import pl.networkmanager.bilka.auth.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegisterDTO user) {
        try {

            userService.register(user);
        } catch (UserExistingWithLogin ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A4));
        } catch (UserExistingWithMail ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A5));

        }
        return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        return userService.login(response, user);
    }

    @RequestMapping(path = "/auto-login", method = RequestMethod.GET)
    public ResponseEntity<?> autoLogin(HttpServletRequest request, HttpServletResponse response) {
        return userService.loginByToken(request, response);
    }

    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public ResponseEntity<AuthResponse> validateToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            userService.validateToken(request, response);
            return ResponseEntity.ok(new AuthResponse(Code.PERMIT));

        } catch (IllegalArgumentException | ExpiredJwtException e) {
            return ResponseEntity.status(401).body(new AuthResponse(Code.A3));
        }
    }

    @RequestMapping(path = "/logged-in", method = RequestMethod.GET)
    public ResponseEntity<?> loggedIn(HttpServletRequest request, HttpServletResponse response) {
        return userService.loggedIn(request, response);
    }

    @RequestMapping(path = "/activate", method = RequestMethod.GET)
    public ResponseEntity<?> activate(@RequestParam String uid) {
        try {
            userService.activateUser(uid);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (UserDontExistException e) {
            return ResponseEntity.status(401).body(new AuthResponse(Code.A6));
        }
    }

    @RequestMapping(path = "/reset-password", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> sendMailRecovery(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            userService.recoverPassword(resetPasswordDTO.email());
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (UserDontExistException e) {
            return ResponseEntity.status(401).body(new AuthResponse(Code.A6));
        }
    }

    @RequestMapping(path = "/reset-password", method = RequestMethod.PATCH)
    public ResponseEntity<AuthResponse> recoveryMail(@RequestBody ChangePasswordData changePasswordData) {
        try {
            userService.resetPassword(changePasswordData);
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        } catch (UserDontExistException e) {
            return ResponseEntity.status(401).body(new AuthResponse(Code.A6));
        }
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationMessage handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        return new ValidationMessage(ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage());
    }
}
